package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_seplit_order")
@Data
public class DdbSeplitOrder {
    private Integer id;

    private String zid;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    @Column(name = "flow_id")
    private String flowId;

    /**
     * ��Ʒ���id
     */
    @Column(name = "rule_id")
    private String ruleId;

    @Column(name = "zrule_id")
    private String zruleId;

    @Column(name = "frule_id")
    private String fruleId;

    @Column(name = "grule_id")
    private String grule_id;
    /**
     * ����id
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * ����zid
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
     * Ӧת���ֽ��
     */
    private Integer amount;

    /**
     * ת��״̬
     */
    private Integer state;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;


}