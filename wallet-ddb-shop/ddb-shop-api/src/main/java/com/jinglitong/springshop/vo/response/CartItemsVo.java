package com.jinglitong.springshop.vo.response;

import com.jinglitong.springshop.entity.CartItems;
import lombok.Data;

/**
 * @author fyy
 * @create 2019-01-10-14:05}
 */
@Data
public class CartItemsVo extends CartItems {
    private ProductResponseVo product;
}
