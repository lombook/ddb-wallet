package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.CartItems;
import com.jinglitong.springshop.utils.MyMapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CartItemsMapper extends MyMapper<CartItems> {

    /**
     * 通过cartId和skuId查询购物项
     * @param cartid
     * @param skuId
     * @return
     */
    CartItems selectByCartIdAndSkuId(@Param("cartId") String cartid, @Param("skuId") String skuId,@Param("skuPriceId")String skuPriceId);

    /**
     *通过cartId和skuId增加或则减少购物项数量 不超过最大值，和最小值
     * @param cartId
     * @param skuId
     * @param cartBigNum
     * @param cartLittleNum
     * @param num
     * @return
     */
    @Update("update cart_items set quantity = quantity + #{num} where cart_id = #{cartId} and sku_id = #{skuId} and sku_price_id =#{skuPriceId} and quantity + #{num} <= #{BigNum}  and quantity + #{num} >= #{littleNum} ")
    Integer ASOneNumByCartIdAndSkuId(@Param("cartId") String cartId, @Param("skuId") String skuId,@Param("skuPriceId")String skuPriceId,
                                     @Param("BigNum") Integer cartBigNum, @Param("littleNum") Integer cartLittleNum, @Param("num") Integer num);

    /**
     * 通过cartId判断商品数量
     * @param cartId
     * @return
     */
    @Select("select count(1) from cart_items where cart_id = #{cartId}")
    Integer selectCountByCartId(@Param("cartId") String cartId);

    /**
     *
     * @param cartId
     * @return
     */
    List<CartItems> selectByCartId(@Param("cartId") String cartId);
    
    @Delete("DELETE FROM cart WHERE zid IN (SELECT zid from (SELECT c.zid FROM cart c WHERE NOT EXISTS (SELECT ci.* FROM cart_items ci WHERE ci.cart_id = c.zid))t)")
    Integer deleteCart ();
}