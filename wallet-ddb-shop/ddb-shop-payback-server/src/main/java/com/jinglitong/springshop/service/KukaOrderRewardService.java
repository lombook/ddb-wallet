package com.jinglitong.springshop.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.springshop.entity.DeedOrderAwardSplit;
import com.jinglitong.springshop.entity.Orderitem;
import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.mapper.DeedOrderAwardSplitMapper;
import com.jinglitong.springshop.mapper.OrderitemMapper;
import com.jinglitong.springshop.utils.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Transactional
@Slf4j
public class KukaOrderRewardService {
	
	private static final String INTEGRALID = "7";	//支付完成后给自己袋金币,袋金币id
	@Autowired
    private OrderService orderService;
    
    @Autowired
    private OrdersMapper ordersMapper;	
    
    @Autowired
    private OrderitemMapper orderitemMapper;
    
    @Autowired
	private DeedOrderAwardSplitMapper deedOrderAwardSplitMapper;
    
    @Autowired
	private AccountTranferService accountTranferService;
    
    
    @Value("${aliyun.mq.order.topic}")
    private String topic;
    
    /**
     * 支付完成后赠送带金币
     * @param orderId
     */
    public void giveOrderReward(String orderId){
    	log.info("订单{}处理赠送袋金币开始",orderId);
    	Orders order = new Orders();
		order.setZid(orderId);
		order = ordersMapper.selectOne(order);
		List<Orders> orders = orderService.getChildren(order);

		BigDecimal djbValue = BigDecimal.ZERO;
		String integralId = null;
		for(Orders o:orders){  			
		    //获得每个订单中所有的orderitem,对应的sku 和对应数量
			Weekend<Orderitem> weekend = Weekend.of(Orderitem.class);
			WeekendCriteria<Orderitem, Object> criteria = weekend.weekendCriteria();
			criteria.andEqualTo(Orderitem::getOrdersId, o.getZid());
			List<Orderitem> items = orderitemMapper.selectByExample(weekend);

			for(Orderitem item : items){
				BigDecimal djbValueTmp = BigDecimal.ZERO;
				if(item.getDjbValue() != null){
					djbValueTmp = item.getDjbValue();
				}
				if(djbValueTmp.compareTo(BigDecimal.ZERO) == 0){
					continue;
				}
				
				DeedOrderAwardSplit deedOrderAwardSplit = new DeedOrderAwardSplit();
				Date date = new Date();
				deedOrderAwardSplit.setZid(UuidUtil.getUUID());
				deedOrderAwardSplit.setCustId(o.getCustId());
				deedOrderAwardSplit.setIntegralId(INTEGRALID);
				deedOrderAwardSplit.setOrderId(orderId);
				deedOrderAwardSplit.setSkuId(item.getSkuId());			
				deedOrderAwardSplit.setSkuNum(item.getQuantity());				
				deedOrderAwardSplit.setAmount(djbValueTmp.multiply(new BigDecimal(100)).longValue());				
				deedOrderAwardSplit.setCreateTime(date);
				deedOrderAwardSplit.setUpdateTime(date);
				deedOrderAwardSplitMapper.insert(deedOrderAwardSplit);						
			}			
		}
		if(order.getDjbValue() != null){
			djbValue = order.getDjbValue();
		}
		log.info("袋金币：" + djbValue);
		if(djbValue.compareTo(BigDecimal.ZERO) > 0){
			accountTranferService.addInteg(djbValue.multiply(new BigDecimal(100)).longValue(), order.getCustId(), INTEGRALID, order.getZid(), 1, "支付完成后送袋金币");
		}
		
		log.info("订单{}处理赠送袋金币结束",orderId);
	
    }

}
