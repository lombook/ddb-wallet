package com.jinglitong.springshop.vo.response;

import lombok.Data;

/**
 * @author fyy
 * @create 2019-01-22-19:56}
 */
@Data
public class OrderShopingVo {
	private String zid;
    private String deliveryCorp;
    private String trackingNo;
    private String coordinateAdd;//发货位置
    private String orderHash;
    private String imageUrl;
}
