package com.jinglitong.springshop.vo.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceVo {
    private String resultcode;
    private String resultmsg;
    private BigDecimal balance;
}
