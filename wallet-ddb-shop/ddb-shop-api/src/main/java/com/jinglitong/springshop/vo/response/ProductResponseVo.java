package com.jinglitong.springshop.vo.response;

import com.jinglitong.springshop.entity.Product;
import com.jinglitong.springshop.entity.Sku;
import lombok.Data;

/**
 * @author fyy
 * @create 2019-01-09-12:50}
 * 商品列表
 */
@Data
public class ProductResponseVo  extends Product {

    private Sku sku;
}
