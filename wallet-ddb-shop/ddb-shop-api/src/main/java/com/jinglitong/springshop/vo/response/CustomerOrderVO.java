package com.jinglitong.springshop.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CustomerOrderVO
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/3/19 17:33
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderVO {
    private String custId;
    private String selfInvite;
    private String inviteCode;
    private String orderParent;
    private String orderId;
    private String orderSn;
}
