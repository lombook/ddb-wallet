package com.jinglitong.springshop.vo.request;

import lombok.Data;

/**
 * @author fyy
 * @create 2019-01-25-11:36}
 */
@Data
public class OrderRequestVo extends PageVo {
    private String zid;
    private String sn;
    private String phone;
    private String custId;
    private Integer status;
    private String account;
    private String startTime;
    private String endTime;
}
