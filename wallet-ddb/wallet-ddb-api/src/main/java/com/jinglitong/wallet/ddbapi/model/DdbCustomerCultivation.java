package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
@Data
@Table(name = "ddb_customer_cultivation")
public class DdbCustomerCultivation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "z_id")
    private String zId;

    /**
     * �û�id
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * ��Ա�����
     */
    @Column(name = "cultivation_amount")
    private Integer cultivationAmount;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * �޸�ʱ��
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return z_id
     */
    public String getzId() {
        return zId;
    }

    /**
     * @param zId
     */
    public void setzId(String zId) {
        this.zId = zId;
    }

    /**
     * ��ȡ�û�id
     *
     * @return customer_id - �û�id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * �����û�id
     *
     * @param customerId �û�id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * ��ȡ��Ա�����
     *
     * @return cultivation_amount - ��Ա�����
     */
    public Integer getCultivationAmount() {
        return cultivationAmount;
    }

    /**
     * ���û�Ա�����
     *
     * @param cultivationAmount ��Ա�����
     */
    public void setCultivationAmount(Integer cultivationAmount) {
        this.cultivationAmount = cultivationAmount;
    }


}