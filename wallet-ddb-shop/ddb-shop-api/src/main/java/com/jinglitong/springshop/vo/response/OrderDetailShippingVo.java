package com.jinglitong.springshop.vo.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 订单详情中的物流。包含物流公司、物流单号和发货时间
 * @author fyy
 * @create 2019-01-22-19:48}
 */
@Data
public class OrderDetailShippingVo {
	private String zid;
	private String deliveryCorp;
    private String trackingNo;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}
