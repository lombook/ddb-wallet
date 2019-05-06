package com.jinglitong.springshop.entity;

import lombok.Data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "ddb_integral_wallet")
public class DdbIntegralWallet {
    private Integer id;

    private String zid;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * ������
     */
    @Column(name = "inte_name")
    private String inteName;

    /**
     * ����������
     */
    @Column(name = "inte_cname")
    private String inteCname;

    /**
     * ���
     */
    private Long amount;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}