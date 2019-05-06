package com.jinglitong.springshop.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ProfileInfoVO
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/3/20 15:52
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public  class ProfileInfoVO{
    private String endpointName;
    private String regionId;
    private String prouct;
    private String domain;
}