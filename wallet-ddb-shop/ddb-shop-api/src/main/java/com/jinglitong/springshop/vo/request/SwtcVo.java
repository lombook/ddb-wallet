package com.jinglitong.springshop.vo.request;

import lombok.Data;

@Data
public class SwtcVo {
    /**
     * 转出的用户id
     */
    private String custId;
    /**
     * 转出地址
     */
    private String address;
    /**
     * 数量
     */
    private String amount;
    /**
     * 备注
     */
    private String memos;
}
