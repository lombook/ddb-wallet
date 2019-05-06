package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;
@Data
@ToString
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;
    
    /**
     * 显示优先级 0最高
     */
    private Byte orders;

    /**
     * logo图标
     */
    private String logo;

    /**
     * 名称
     */
    private String name;

    /**
     * 网址
     */
    private String url;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 介绍
     */
    private String introduction;


}