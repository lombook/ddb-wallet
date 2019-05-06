package com.jinglitong.springshop.vo.request;

import com.jinglitong.springshop.entity.Cart;
import com.jinglitong.springshop.entity.CartItems;
import lombok.Data;

import java.util.List;

/**
 * @author fyy
 * @create 2019-01-10-13:47}
 */
@Data
public class CartVo extends Cart {
    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 行数
     */
    private Integer pageSize = 10;

    /**
     * 具体商品
     */
    private String skuId;

    private String skuPriceId;
    /**
     * skuId集合
     */
    private List<String> skuIdList;
    
    private List<String> skuPriceIdList;

    /**
     * 增减数量
     */
    private Integer num = 1;

    /**
     * 增减类型
     * 增add
     * 减sub
     */
    String type = "add";

    /**
     * sku  数量集合
     */
    List<CartItems> cartItemsList ;
}
