package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Orderitem;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.request.OrderIteamVO;
import com.jinglitong.springshop.vo.request.OrderVO;
import com.jinglitong.springshop.vo.response.OrderResponseVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderitemMapper extends MyMapper<Orderitem> {
    /**
     * 通过购物车ZID查询购物项数量
     * @param cartId
     * @return
     */
    @Select("select count(1) from cart_items where cart_id = #{cartId}")
    Integer selectCountByCartid(@Param("cartId") String cartId);

    List<OrderIteamVO> selectByOrdersId(String pzid);

    OrderResponseVo selectbyOrderDetail(OrderVO orderVO);

    OrderResponseVo selectbyOrderDetailManage(OrderVO orderVO);
}