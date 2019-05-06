package com.jinglitong.springshop.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Deliverycorp;
import com.jinglitong.springshop.entity.Orderitem;
import com.jinglitong.springshop.entity.Orderlog;
import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.entity.Ordershipping;
import com.jinglitong.springshop.entity.Ordershippingitem;
import com.jinglitong.springshop.mapper.DeliverycorpMapper;
import com.jinglitong.springshop.mapper.OrderitemMapper;
import com.jinglitong.springshop.mapper.OrderlogMapper;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.mapper.OrdershippingMapper;
import com.jinglitong.springshop.mapper.OrdershippingitemMapper;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.response.OrderDeliverVo;
import com.jinglitong.springshop.vo.response.SupplierOrderVo;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
public class SupplierOrderService {

	@Autowired
	private OrdersMapper ordersMapper;
	
	@Autowired
	private OrderitemMapper orderitemMapper;
	
	@Autowired
	private OrdershippingMapper ordershippingMapper;
	
	@Autowired
	private OrdershippingitemMapper ordershippingitemMapper;
	
	@Autowired
	private DeliverycorpMapper deliverycorpMapper;
	
	@Autowired
	private OrderlogMapper orderlogMapper;
	
	
	public PageInfo<SupplierOrderVo> selectOrderBySuppl(SupplierOrderVo vo) {
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
    	List<SupplierOrderVo> list = ordersMapper.selectOrderBySuppl(vo);
    	PageInfo<SupplierOrderVo> info = new PageInfo<SupplierOrderVo>(list);
    	return info;
	}
	
	public PageInfo<SupplierOrderVo> selectCompleteOrderBySuppl(SupplierOrderVo vo) {
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
    	List<SupplierOrderVo> list = ordersMapper.selectCompleteOrderBySuppl(vo);
    	PageInfo<SupplierOrderVo> info = new PageInfo<SupplierOrderVo>(list);
    	return info;
	}
	
	public List<Deliverycorp> getDeliverycorp(){
		return deliverycorpMapper.selectAll();
	}
	@Transactional
	public ShopRespose addDeliverycorp(OrderDeliverVo vo) {
		ShopRespose respose = new ShopRespose<>();
		String orderId =vo.getOrderId();
		String code = vo.getCode();
		Deliverycorp de = new Deliverycorp();
		de.setCode(code);
		Deliverycorp deliverycorp = deliverycorpMapper.selectOne(de);

		Ordershipping orderShipRecord = new Ordershipping();
		orderShipRecord.setOrderId(orderId);
		List<Ordershipping> orderShipRecords = ordershippingMapper.select(orderShipRecord);
		if (orderShipRecords.size() != 0) {
			respose = new ShopRespose<>(ErrorEnum.ERROR_0001);
		}
		String logisticsOrderSn = vo.getLogisticsId();
		Orders orderRec = new Orders();
		orderRec.setZid(orderId);
		orderRec = ordersMapper.selectOne(orderRec);
		if (orderRec == null) {
			respose = new ShopRespose<>(ErrorEnum.ERROR_5304);
		}
		Orderitem orderItemRec = new Orderitem();
		orderItemRec.setOrdersId(orderId);
		orderItemRec = orderitemMapper.selectOne(orderItemRec);
		if (orderItemRec == null) {
			respose = new ShopRespose<>(ErrorEnum.ERROR_5305);
		}
		// 物流信息组装、入库
		String[] arr = logisticsOrderSn.split(",");
		List<Ordershipping> os = new LinkedList<>();
		List<Ordershippingitem> ost = new LinkedList<>();
		for (int i = 0; i < arr.length; i++) {
			Ordershipping ordershipping = buidOrdershipping(orderRec, deliverycorp, arr[i]);
			Ordershippingitem ordershippingitem = buildOrdershippingitem(orderItemRec, ordershipping.getZid());
			os.add(ordershipping);
			ost.add(ordershippingitem);
		}
		int ship = ordershippingMapper.insertList(os);
		int shipIteme = ordershippingitemMapper.insertList(ost);
		if (ship != arr.length || shipIteme != arr.length) {
			throw new RuntimeException("物流入库错误");
		}

		// 更新子订单状态
		Orders update = new Orders();
		update.setZid(orderId);
		update.setStatus(5);
		update.setUpdateTime(new Date());
		Weekend<Orders> weekend = Weekend.of(Orders.class);
		WeekendCriteria<Orders, Object> Criteria = weekend.weekendCriteria();
		Criteria.andEqualTo(Orders::getZid, orderId);
		int res = ordersMapper.updateByExampleSelective(update, weekend);
		if (res <= 0) {
			throw new RuntimeException("物流信息数据库修改异常");
		}

		Orderlog orderlog = new Orderlog();
		orderlog.setDetail("子订单状态改为已发货");
		orderlog.setType(5);
		orderlog.setOrderId(orderId);
		orderlog.setCreatedTime(new Date());
		orderlogMapper.insert(orderlog);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setCode(IConstants.SUCCESS);
		return respose;
	}
	
	
	/**
	 * 
	 * 功能说明:构建实体Ordershippingitem
	 * @param orderItemRec
	 * @param ordershippingId
	 * @return
	 */
	private Ordershippingitem buildOrdershippingitem(Orderitem orderItemRec,String ordershippingId) {
		Ordershippingitem ordershippingitem = new Ordershippingitem();
		ordershippingitem.setZid(UuidUtil.getUUID());
		ordershippingitem.setIsdelivery((byte)1);
		ordershippingitem.setName(orderItemRec.getName());
		ordershippingitem.setQuantity(orderItemRec.getQuantity());
		ordershippingitem.setSn(orderItemRec.getSn());
		ordershippingitem.setOrdershippingId(ordershippingId);
		ordershippingitem.setSkuId(orderItemRec.getSkuId());
		ordershippingitem.setCreatedTime(new Date());
		ordershippingitem.setUpdateTime(new Date());
		return ordershippingitem;
	}
	/**
	 * 
	 * 功能说明:构建实体Ordershipping
	 * @param orderRec
	 * @param deliverycorp
	 * @param logisticsOrderSn
	 * @return
	 */
	private Ordershipping buidOrdershipping(Orders orderRec,Deliverycorp deliverycorp,String logisticsOrderSn) {
		Ordershipping orderShip =  new Ordershipping();
		orderShip.setZid(UuidUtil.getUUID());
		orderShip.setAddress(orderRec.getAddress());
		orderShip.setArea(orderRec.getAreaname());
		orderShip.setConsignee(orderRec.getConsignee());
		orderShip.setDeliverycorp(deliverycorp.getName());
		orderShip.setDeliverycorpcode(deliverycorp.getCode());
		orderShip.setDeliverycorpurl(deliverycorp.getUrl());
		orderShip.setFreight(BigDecimal.ZERO);
		orderShip.setPhone(orderRec.getPhone());
		orderShip.setTrackingno(logisticsOrderSn);
		orderShip.setOrderId(orderRec.getZid());
		orderShip.setCreatedTime(new Date());
		orderShip.setUpdateTime(new Date());
		return orderShip;
		
	}
}
