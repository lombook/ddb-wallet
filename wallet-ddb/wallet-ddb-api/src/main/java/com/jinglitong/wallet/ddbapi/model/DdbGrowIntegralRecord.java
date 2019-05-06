package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_grow_integral_record")
public class DdbGrowIntegralRecord {
    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    /**
     * ҵ��id
     */
    private String zid;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * ����ֵ
     */
    private Integer amount;

    /**
     * ʣ����
     */
    @Column(name = "left_amount")
    private Integer leftAmount;

    /**
     * �ܽ��
     */
    @Column(name = "sum_amount")
    private Integer sumAmount;

    /**
     * �ͷű���
     */
    private String proportion;

    @Column(name = "rule_id")
    private String ruleId;

    /**
     * ��������
     */
    @Column(name = "grule_id")
    private String gruleId;

    /**
     * ����id
     */
    @Column(name = "inte_zid")
    private String inteZid;

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
    @Column(name = "integ_name")
    private String integName;

    /**
     * ����������
     */
    @Column(name = "integ_cname")
    private String integCname;

    /**
     * ��������id
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * ��ע
     */
    private String remark;


    @Column(name = "frozen_days")
    private Integer frozenDays;
    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

}