package com.jinglitong.springshop.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author fyy
 * @create 2019-01-22-14:29}
 */
@Data
public class OrderVO extends PageVo{

    private String type ;
    private Integer status;
    private String zid;
    private String pzid;
    private String psn;
    private String currency;//currencysign字段
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private List<OrderIteamVO> orderIteamVOList;
    private Integer delivered;
    private Integer orderNum;
    private BigDecimal amount;
    
    private String integralCode;
    
    private String priceCode;
    private BigDecimal price;
    private BigDecimal integralPrice;
    

}
