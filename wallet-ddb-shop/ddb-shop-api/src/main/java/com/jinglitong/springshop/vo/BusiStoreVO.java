package com.jinglitong.springshop.vo;

import lombok.Data;

/**
 * @ClassName BusiStoreVO
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/4/10 11:28
 * @Version 1.0
 **/
@Data
public class BusiStoreVO {

    private String businessName;
    private String storeName;
    private String bZid;
    private Integer state;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
