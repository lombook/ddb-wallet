package com.jinglitong.springshop.service;

import com.jinglitong.springshop.entity.*;
import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.mapper.*;
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
    private OrderlogMapper orderlogMapper;

    @Autowired
    private PaymenttransactionMapper paymentTransactionMapper;

    @Autowired
    private CurrencyMapper currencyMapper;

    /**
     * 订单支付成功后做的处理
     * @param order
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

            List<Map<String,Object>> list = ordersMapper.selelctSellNumByOrder(o.getZid());
            ordersMapper.updateProductSellNum(list);
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

    public List<Orders> getChildren(Orders order){
        Weekend<Orders> weekend = Weekend.of(Orders.class);
        WeekendCriteria<Orders,Object> Criteria =weekend.weekendCriteria();
        Criteria.andEqualTo(Orders::getOrderParent, order.getZid());
        weekend.setOrderByClause("update_time desc");
        return ordersMapper.selectByExample(weekend);
    }
    
    private List<Orders> getFinishedForeignOrders(String cust_id,String currencyId){      	
    	return ordersMapper.selectForeignByCustIdAndStatus(cust_id, 3, currencyId);
    }

}
