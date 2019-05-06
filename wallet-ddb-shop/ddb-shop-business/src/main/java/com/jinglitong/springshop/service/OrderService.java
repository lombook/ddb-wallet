package com.jinglitong.springshop.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.entity.*;
import com.jinglitong.springshop.mapper.*;
import com.jinglitong.springshop.vo.request.OrderIteamVO;
import com.jinglitong.springshop.vo.request.OrderRequestVo;
import com.jinglitong.springshop.vo.request.OrderVO;
import com.jinglitong.springshop.vo.response.OrderResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author fyy
 * @create 2019-01-21-12:22}
 */
@Service
@Slf4j
@Transactional
public class OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderitemMapper orderitemMapper;

    @Autowired
    private OrderlogMapper orderlogMapper;

    @Autowired
    private PaymenttransactionMapper paymentTransactionMapper;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private OrderRealPayMapper orderRealPayMapper; 

    public Map<String,Object> getOrderList(String zid, OrderVO orderVO) {
        if (orderVO.getPageNum() != null && orderVO.getPageSize() != null) {
            PageHelper.startPage(orderVO.getPageNum(), orderVO.getPageSize());
        }
        orderVO.setZid(zid);
        List<OrderVO> orderList = ordersMapper.getOrderList(orderVO);
        for (OrderVO vo:orderList) {
            List<OrderIteamVO> OrderIteamVOList = orderitemMapper.selectByOrdersId(vo.getPzid());
            vo.setOrderIteamVOList(OrderIteamVOList);
            Integer count = ordersMapper.selectCountBystatusAndCustId("5",zid,vo.getPzid(),null);
            vo.setDelivered(count);
             count = ordersMapper.selectCountBystatusAndCustId(null,zid,vo.getPzid(),null);
            vo.setOrderNum(count);
            Orders orders =ordersMapper.selectByZid(vo.getPzid());
            vo.setAmount(orders.getAmount());
        }
        PageInfo pageinfo = new PageInfo(orderList);
        HashMap<String, Object> map = new HashMap<>();

        map.put("total", pageinfo.getTotal());
        map.put("orderList", orderList);
        return map;
    }

    public Map<String,Object> getOrderDetail(String zid, OrderVO orderVO) {
        HashMap<String, Object> resultMap = new HashMap<>();
        OrderResponseVo orderList = orderitemMapper.selectbyOrderDetail(orderVO);
        resultMap.put("order",orderList);
      //如果是物流消息中点进来的，需要设置物流消息未已读状态
//        if(orderVO.getZid() != null){
//        	 logisticsInformService.updatelogisticsInform( orderVO.getZid());
//        }
        return resultMap;
    }

    public Map<String,Object> getOrderDetailManage(String zid, OrderVO orderVO) {
        HashMap<String, Object> resultMap = new HashMap<>();
        OrderResponseVo orderList = orderitemMapper.selectbyOrderDetailManage(orderVO);
        resultMap.put("order",orderList);
        //如果是物流消息中点进来的，需要设置物流消息未已读状态
//        if(orderVO.getZid() != null){
//            logisticsInformService.updatelogisticsInform(orderVO.getZid());
//        }
        OrderRealPay orderRealPay = new OrderRealPay();
        orderRealPay.setOrderId(orderVO.getPzid());
        //查询订单实际支付，包含法币，代金币，现金贷
        List<OrderRealPay> realPayList = orderRealPayMapper.select(orderRealPay);
        for(OrderRealPay orderRealPay1 : realPayList){
            resultMap.put(orderRealPay1.getCurrencyCode(),orderRealPay1.getAmount());
        }
        return resultMap;
    }
    private Orderlog createOrderLog(String detail,Integer type,String orderId) {
        Orderlog orderlog = new Orderlog();
        orderlog.setDetail(detail);
        orderlog.setType(type);
        orderlog.setOrderId(orderId);
        orderlog.setCreatedTime(new Date());
        return orderlog;
    }

    /**
     * 订单支付成功后做的处理
     * @param paymentTransaction
     * @param cny_amount
     */
    public void dealAfterPaySuccess(Paymenttransaction paymentTransaction,int cny_amount){
        //将支付事务设置为支付成功
        paymentTransaction.setPaystatus(1);
        BigDecimal realAmount = new BigDecimal(cny_amount).divide(new BigDecimal(100));
        paymentTransaction.setRealAmount(realAmount);
        paymentTransaction.setIssuccess(true);
        Currency currency = new Currency();
        currency.setCurrencycode("CNY");
        currency = currencyMapper.selectOne(currency);
        paymentTransaction.setRealCurrencyId(currency.getZid());
        paymentTransaction.setUpdatedTime(new Date());
        paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransaction);

        //获得对应主订单,并对总订单进行处理
        Date date = new Date();
        Orders order = getByPaymentTransaction(paymentTransaction);
        order.setStatus(1);//更新主订单状态为发货中 主订单1为发货中
        order.setExpire(null);
        order.setUpdateTime(date);
        //ordersMapper.updateByPrimaryKey(order);
        List<Orderlog> orderloglist = new ArrayList<Orderlog>();
        Orderlog log = new Orderlog();
        log.setCreatedTime(new Date());
        log.setOrderId(order.getZid());
        log.setType(1);
        log.setDetail("主订单支付成功");
        orderloglist.add(log);

        //获得对应子订单,并对子订单进行处理
        List<Orders> orders = getChildren(order);

        for(int i = 0; i < orders.size(); i ++){
            //更新子订单状态为代发货 子订单4为待发货
            Orders o = orders.get(i);
            o.setStatus(4);
            o.setExpire(null);
            o.setUpdateTime(date);
            //ordersMapper.updateByPrimaryKey(o);
            Orderlog orderlog = new Orderlog();
            orderlog.setCreatedTime(new Date());
            orderlog.setOrderId(o.getZid());
            orderlog.setType(4);
            orderlog.setDetail("子订单支付成功");
            orderloglist.add(orderlog);
        }
        orders.add(order);
        ordersMapper.batchUpdateOrderStatusAfterPayCallBack(orders);
        orderlogMapper.insertList(orderloglist);
    }

    private Orders getByPaymentTransaction(Paymenttransaction paymentTransaction){

        Orders order = new Orders();
        order.setZid(paymentTransaction.getOrderId());
        return ordersMapper.selectOne(order);
    }

    private List<Orders> getChildren(Orders order){
        Weekend<Orders> weekend = Weekend.of(Orders.class);
        WeekendCriteria<Orders,Object> Criteria =weekend.weekendCriteria();
        Criteria.andEqualTo(Orders::getOrderParent, order.getZid());
        weekend.setOrderByClause("update_time desc");
        return ordersMapper.selectByExample(weekend);
    }


    /**
     * 订单自动过期
     */
    public void orderExpire(){
       Integer count = ordersMapper.updateExpireStatus();
       log.info("取消订单数量：{}",count);
    }

    public Map<String,Object> getOrders(OrderRequestVo orderRequestVo) {
        if (orderRequestVo.getPageNum() != null && orderRequestVo.getPageSize() != null) {
            PageHelper.startPage(orderRequestVo.getPageNum(), orderRequestVo.getPageSize());
        }
        List<Map<String,Object>> orderList = ordersMapper.selectOrdersList(orderRequestVo);
        PageInfo pageinfo = new PageInfo(orderList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", pageinfo.getTotal());
        map.put("orderList", orderList);
        for (Map<String,Object> order : orderList) {
            OrderRealPay orderRealPay = new OrderRealPay();
            orderRealPay.setOrderId(order.get("orderZid").toString());
            //查询订单实际支付，包含法币，代金币，现金贷
            List<OrderRealPay> realPayList = orderRealPayMapper.select(orderRealPay);
            for(OrderRealPay orderRealPay1 : realPayList){
                order.put(orderRealPay1.getCurrencyCode(),orderRealPay1.getAmount());
            }
        }
        return map;
    }
}
