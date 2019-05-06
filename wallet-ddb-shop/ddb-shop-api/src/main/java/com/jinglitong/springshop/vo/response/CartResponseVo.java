package com.jinglitong.springshop.vo.response;

import com.jinglitong.springshop.entity.Cart;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author fyy
 * @create 2019-01-11-10:47}
 */
@Data
public class CartResponseVo extends Cart {
    private Integer quantity;
    private String image;
    private String name;
    private String skuZid;
    //private String currencyItems;
    private BigDecimal price;
    private String currency;
    private Integer isList;
    
    private BigDecimal integralPrice;
    private String integralCode;
    private String skuPriceId;
    private String priceCode;
    private String priceType;
    
}
