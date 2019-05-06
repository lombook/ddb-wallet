package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Cart;
import com.jinglitong.springshop.entity.CartItems;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.request.CartVo;
import com.jinglitong.springshop.vo.response.CartResponseVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface CartMapper extends MyMapper<Cart> {

    /**
     *通过CustId或clientId查询用户购物车商品列表
     * @param cart
     * @return
     */
    List<CartResponseVo> selectCustomerCart(CartVo cart);

    /**
     * 通过CustId或clientId查询用户购物车
     * @param cart
     * @return
     */
    Cart selectByCustIdOrClientId(CartVo cart);

    /**
     * 清空購物車
     * @param cartId
     */
    @Delete("DELETE  cart ,cart_items  from cart ,cart_items where cart.zid = #{cartId} and cart_items.cart_id = #{cartId}")
    void deleteByCartId(@Param("cartId") String cartId);

    /**
     * 根据skuId查询商品
     * @param skuList
     * @return
     */
    List<Map<String,Object>> calculateCart(List<CartItems> skuList);

    /**
     * 通过用户ID和skuId查询指定数量的购物项
     * @param cartId
     * @param skuId
     * @param quantity
     * @return
     */
    @Update("update cart_items set quantity = quantity - #{quantity} where cart_id = #{cartId} and sku_price_id = #{skuPriceId} and quantity -#{quantity} > 0 ")
    Integer updateByCartIdAndSkuId(@Param("cartId") String cartId, @Param("skuPriceId") String skuPriceId, @Param("quantity") Integer quantity);

    /**
     * 根据skuId和cartId删除购物项
     * @param cartId
     * @param skuId
     */
    @Delete("DELETE from  cart_items  where  cart_id = #{cartId} and sku_price_id = #{skuPriceId}")
    void deleteBycartIdAndskuId(@Param("cartId") String cartId, @Param("skuPriceId") String skuPriceId);
}