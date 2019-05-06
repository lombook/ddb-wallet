package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_should_frozen")
public class DdbShouldFrozen {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * ��������id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * flowid
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * ��Ʒ����id
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * ��������id
     */
    @Column(name = "grow_rule_id")
    private String growRuleId;

    /**
     * �ܽ��
     */
    private Integer amount;

    /**
     * ʣ����
     */
    @Column(name = "left_amount")
    private Integer leftAmount;

    /**
     * ÿ����Ҫ�ͷŽ��
     */
    @Column(name = "rfrozen_amount")
    private Integer rfrozenAmount;

    /**
     * �ͷű���
     */
    private String proportion;

    /**
     * ��ǰ�ͷ�����
     */
    @Column(name = "frozen_days")
    private Integer frozenDays;

    /**
     * ִ��ʱ��
     */
    @Column(name = "excute_time")
    private String excuteTime;

    /**
     * ���ֳ���
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
    @Column(name = "inte_name")
    private String inteName;

    /**
     * ����������
     */
    @Column(name = "inte_cname")
    private String inteCname;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;


}