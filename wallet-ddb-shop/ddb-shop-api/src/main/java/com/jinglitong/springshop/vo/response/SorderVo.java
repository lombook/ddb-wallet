package com.jinglitong.springshop.vo.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author fyy
 * @create 2019-01-22-19:48}
 */
@Data
public class SorderVo {
    private String ssn;
    private BigDecimal cPrice;
    private String status;
    private String name;
    private String thumbnail;    
    private String skuId;    
    private Integer quantity;
    private String subAmount;
    private String integralCode;
    private String priceCode;
    private String integralPrice;
    private String deliveryCorp;
    private String trackingNo;
    private String createdTime;
    private String priceId;
    private BigDecimal cIntegralPrice;
    private String cIntegralCode;
    private List<OrderDetailShippingVo> ordershippings = new ArrayList<OrderDetailShippingVo>();
}
