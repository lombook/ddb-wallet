package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.entity.Product;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.request.OrderRequestVo;
import com.jinglitong.springshop.vo.request.OrderVO;
import com.jinglitong.springshop.vo.response.IetOrderVo;
import com.jinglitong.springshop.vo.response.OrderDetailShippingVo;
import com.jinglitong.springshop.vo.response.OrderPushChildVo;
import com.jinglitong.springshop.vo.response.SupplierOrderVo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersMapper extends MyMapper<Orders> {
    List<OrderVO> getOrderList(OrderVO orderVO);

    @Select("select count(0) from orders  where cust_id = #{zid} and order_parent = 'P' ")
    Integer selectCountByCustId(OrderVO orderVO);
    @Update("update orders s,orders os set s.`status` = #{status} , os.`status` = #{status},os.`completeDate` = #{completeDate},s.`completeDate` = #{completeDate} where s.zid = os.order_parent and s.zid = #{pzid} and os.cust_id =#{custId}")
    Integer updateOrderStatusByCustIdAndOrderZid(@Param("custId") String zid, @Param("pzid") String pzid, @Param("status") int status,@Param("completeDate") Date completeDate);

    /**
     * 根据状态用户id和父订单id还有指定upde时间之前的订单
     * @param status 订单状态
     * @param custId 用户id
     * @param pzid 父订单id
     * @param date 指定修改时间之前的订单
     * @return
     */
    Integer selectCountBystatusAndCustId(@Param("status") String status, @Param("custId") String custId, @Param("zid") String pzid, @Param("updateDate") Date date);

    Orders selectByZid(String pzid);

    List<Orders> selectPSByZid(@Param("pzid") String zid);

    Map<String,String> selectPayMentNameByOrderId(String pzid);
    Integer selectCountByParentOrder(String orderParent);

    List<Orders> selectExpireOrder();

    @Update("update orders set status = 2 where expire < NOW() and `status` = 0;")
    Integer updateExpireStatus();

    @Update("update orders set status = #{status} where zid = #{zid};")
    Integer updateStatus(@Param("status") int status, @Param("zid") String zid);

    List<Orders> selectByUpdateTime(@Param("date") Date date, @Param("zid") String zid);

    List<OrderPushChildVo> selectChildOrder(String orderId);
    
    List<IetOrderVo> selectIetOrder(String orderId);

    int batchUpdateOrderStatusAfterPayCallBack(List<Orders> orders);
    
    int batchUpdateOrderReceiver(List<Orders> orders);

    //List<Orders> selectOrdersList(OrderRequestVo orderRequestVo);

    List<Map<String,Object>> selectOrdersList(OrderRequestVo orderRequestVo);

    int updateProductSellNum(List<Map<String,Object>> list);
    
    List<Map<String,Object>> selelctSellNumByOrder(@Param("orderId") String orderId);

    List<Orders> selectByCustIdAndStatus(@Param("custId") String custId,@Param("status") Integer status,@Param("status1") Integer status1);

    List<Orders> selectPSByZidAndPartId(@Param("pzid") String pzid,@Param("parentId") String pzid1);
    
    List<Orders> selectForeignByCustIdAndStatus(@Param("custId") String custId,@Param("status") Integer status,@Param("currencyId") String currencyId);
    
    List<String> selectOrderByStatus();
    
    String selectCurrencyCodeByOrder(@Param("orderId") String orderId);
    
    //获得订单对应的物流信息
    List<OrderDetailShippingVo> getOrderShipping(@Param("orderSn") String orderSn);

    @Select("select * from orders where order_parent = #{zid}")
    List<Orders> selectByChildOrder(Orders orders);

    @Update("update orders set order_hash = #{hash} where zid = #{zid};")
    Integer updateHash(@Param("hash") String hash, @Param("zid") String zid);

    @Select("select zid,price,status,currency_id from orders where cust_id = #{custId} " +
            " and order_parent = 'P' and status != 2 order by create_time ASC limit 1")
    Map<String,Object> getRewardOrder(@Param("custId") String custId);
    
    List<Orders> selectExpireParentOrder();	
    
    List<SupplierOrderVo> selectCompleteOrderBySuppl(SupplierOrderVo vo);
    
    List<SupplierOrderVo> selectOrderBySuppl(SupplierOrderVo vo);
}