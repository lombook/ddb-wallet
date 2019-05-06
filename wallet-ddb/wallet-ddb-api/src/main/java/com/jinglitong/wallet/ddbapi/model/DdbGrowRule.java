package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
@Data
@Table(name = "ddb_grow_rule")
public class DdbGrowRule {
    /**
     * ҵ��id
     */
    private String zid;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ��������id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * ���͹���
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * ���ֳ�����
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * ���ֳ���������
     */
    @Column(name = "real_cname")
    private String realCname;

    /**
     * ������
     */
    private Integer amount;

    /**
     * ����
     */
    private String proportion;

    /**
     * ƽ������
     */
    @Column(name = "avg_day")
    private String avgDay;

    /**
     * ��Ч����
     */
    @Column(name = "effective_time")
    private String effectiveTime;

    /**
     * ʧЧ����
     */
    @Column(name = "expiry_time")
    private String expiryTime;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;


}