package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

import com.jinglitong.wallet.api.model.view.PageVO;
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
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

}