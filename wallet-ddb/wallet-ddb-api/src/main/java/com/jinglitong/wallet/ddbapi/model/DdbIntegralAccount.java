package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_integral_account")
public class DdbIntegralAccount {
    /**
     * id
     */
    private Integer id;

    /**
     * ҵ��id
     */
    private String zid;

    /**
     * ���
     */
    private Long amount;

    /**
     * �ϼ�id
     */
    @Column(name = "parent_id")
    private String parentId;

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

    @Column(name = "integ_name")
    private String integName;

    @Column(name = "integ_cname")
    private String integCname;

    private Integer state;
    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * ����ʱ��
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * ��ȡ���
     *
     * @return amount - ���
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * ���ý��
     *
     * @param amount ���
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * ��ȡ�ϼ�id
     *
     * @return parent_id - �ϼ�id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * �����ϼ�id
     *
     * @param parentId �ϼ�id
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
     * @return integ_name
     */
    public String getIntegName() {
        return integName;
    }

    /**
     * @param integName
     */
    public void setIntegName(String integName) {
        this.integName = integName;
    }

    /**
     * @return integ_cname
     */
    public String getIntegCname() {
        return integCname;
    }

    /**
     * @param integCname
     */
    public void setIntegCname(String integCname) {
        this.integCname = integCname;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return create_time - ����ʱ��
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * ���ô���ʱ��
     *
     * @param createTime ����ʱ��
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return update_time - ����ʱ��
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * ���ø���ʱ��
     *
     * @param updateTime ����ʱ��
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}