package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_give_rule")
public class DdbGiveRule {
    @Id
    private Integer id;

    private String zid;
    /**
     * zid
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * ��������
     */
    @Column(name = "group_name")
    private String groupName;

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
     * ���ͽ���λ�֣�
     */
    private Integer amount;

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

    @Column(name = "create_time")
    private String createTime;

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
     * ��ȡzid
     *
     * @return inte_zid - zid
     */
    public String getInteZid() {
        return inteZid;
    }

    /**
     * ����zid
     *
     * @param inteZid zid
     */
    public void setInteZid(String inteZid) {
        this.inteZid = inteZid;
    }

    /**
     * ��ȡ��������
     *
     * @return group_name - ��������
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * ���ù�������
     *
     * @param groupName ��������
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * ��ȡ������
     *
     * @return real_name - ������
     */
    public String getRealName() {
        return realName;
    }

    /**
     * ���û�����
     *
     * @param realName ������
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * ��ȡ����������
     *
     * @return real_cname - ����������
     */
    public String getRealCname() {
        return realCname;
    }

    /**
     * ���û���������
     *
     * @param realCname ����������
     */
    public void setRealCname(String realCname) {
        this.realCname = realCname;
    }

    /**
     * ��ȡ���ͽ���λ�֣�
     *
     * @return amount - ���ͽ���λ�֣�
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * �������ͽ���λ�֣�
     *
     * @param amount ���ͽ���λ�֣�
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }


}