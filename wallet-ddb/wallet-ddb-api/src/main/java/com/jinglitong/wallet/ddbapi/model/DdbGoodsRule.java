package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "ddb_goods_rule")
@Data
public class DdbGoodsRule {
    /**
     * id
     */
    private Integer id;

    /**
     * ҵ��id
     */
    private String zid;

    /**
     * ��Ʒ�۸�
     */
    @Column(name = "product_price")
    private Integer productPrice;

    /**
     * ���ͱ���
     */
    @Column(name = "zrule_group")
    private String zruleGroup;

    @Column(name = "frule_group")
    private String fruleGroup;

    @Column(name = "grule_group")
    private String gruleGroup;

    @Column(name ="active_group")
    private String activeGroup;
    /**
     * ��Ч�ڿ�ʼʱ��
     */
    @Column(name = "effective_time")
    private String effectiveTime;

    /**
     * ��Ч�ڽ���ʱ��
     */
    @Column(name = "expiry_time")
    private String expiryTime;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * ����ʱ��
     */
    @Column(name = "update_time")
    private String updateTime;


    private List<DdbGiveRule> ddbGiveRuleList;

    private List<DdbGrowRule> ddbGrowRuleList;

    
    
    /**
     * ��ȡid
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * ����id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * ��ȡҵ��id
     *
     * @return zid - ҵ��id
     */
    public String getZid() {
        return zid;
    }

    /**
     * ����ҵ��id
     *
     * @param zid ҵ��id
     */
    public void setZid(String zid) {
        this.zid = zid;
    }

    /**
     * ��ȡ��Ʒ�۸�
     *
     * @return product_price - ��Ʒ�۸�
     */
    public Integer getProductPrice() {
        return productPrice;
    }

    /**
     * ���ò�Ʒ�۸�
     *
     * @param productPrice ��Ʒ�۸�
     */
    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * ��ȡ���ͱ���
     *
     * @return zrule_group - ���ͱ���
     */
    public String getZruleGroup() {
        return zruleGroup;
    }

    /**
     * �������ͱ���
     *
     * @param zruleGroup ���ͱ���
     */
    public void setZruleGroup(String zruleGroup) {
        this.zruleGroup = zruleGroup;
    }

    /**
     * @return frule_group
     */
    public String getFruleGroup() {
        return fruleGroup;
    }

    /**
     * @param fruleGroup
     */
    public void setFruleGroup(String fruleGroup) {
        this.fruleGroup = fruleGroup;
    }


}