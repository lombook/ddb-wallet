package com.jinglitong.springshop.vo.request;

import lombok.Data;

/**
 * @author fyy
 * @create 2019-01-09-11:21}
 * 分页基类
 */
@Data
public class PageVo {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 行数
     */
    private Integer pageSize = 10;

    /**
     * 排序类型
     */
    private String orderType = "DESC";


}
