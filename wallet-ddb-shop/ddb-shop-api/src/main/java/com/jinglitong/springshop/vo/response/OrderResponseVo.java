package com.jinglitong.springshop.vo.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author fyy
 * @create 2019-01-22-19:44}
 */
@Data
public class OrderResponseVo {

    private String pzid;
    private String psn;
    private String consignee;
    private String phone;
    private String areaName;
    private String address;
    private String amount;
    private Integer status ;
    private String currency;//currencysign字段
    private String create_time;
    private Integer deliver;
    private String paymentPluginName;
    private Integer orderAllnum;
    private String  integralCId;
    private String  priceCode;
    List<SorderVo> sorderVoList;
    
    private BigDecimal integralPrice;
    private String integralCode;
    private BigDecimal realPrice;
    private String realCode;
    
    private BigDecimal price;
    
    private List<OrderDKVo> orderDKVo;
    

}
