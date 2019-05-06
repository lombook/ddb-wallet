package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "ddb_integral_sub_account_transfer")
public class DdbIntegralSubAccountTransfer {
   @Id
    private Integer id;

    /**
     * ҵ��id
     */
    private String zid;

    /**
     * ���ҵ��id
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * ת�˽��
     */
    private Long amount;

    /**
     * ��������id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * ������
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * ����������
     */
    @Column(name = "real_cname")
    private String realCname;

    /**
     * ��������
     */
    @Column(name = "integ_name")
    private String integName;

    /**
     * ����������
     */
    @Column(name = "integ_cname")
    private String integCname;

    /**
     * ��������:�������ͣ�1������ 2������
     */
    private Integer type;

    /**
     * ��ע
     */
    private String remark;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

}