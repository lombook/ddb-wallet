package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_order")
@Data
public class DdbOrder {
    private Integer id;

    @Column(name = "flow_id")
    private String flowId;

    /**
     * ����id����Ӧ����Ʒ�����Ʒ��
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * ������
     */
    @Column(name = "shop_trade")
    private String shopTrade;

    /**
     * ��Ʒ����
     */
    @Column(name = "product_num")
    private String productNum;

    /**
     * �۸��ܶ
     */
    private String price;

    /**
     * �����
     */
    @Column(name = "nonce_str")
    private String nonceStr;

    /**
     * �û�id
     */
    @Column(name = "user_id")
    private String userId;

    private Boolean state;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "order_create_time")
    private String orderCreateTime;

    @Column(name = "kj_state")
    private Boolean kjState;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "active_state")
    private Boolean activeState;

    @Column(name = "gf_value")
    private Integer gfValue;
    
    @Column(name = "rc_value")
    private Integer rcValue;
    
    @Column(name = "special_num")
    private Integer specialNum;
    
}