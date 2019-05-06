package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.*;

@Table(name = "ddb_propertieddb")
public class DdbPropertieddb {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * �߼�id
     */
    @Column(name = "z_id")
    private String zId;

    /**
     * �������
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * ���code
     */
    @Column(name = "group_key")
    private String groupKey;

    /**
     * ����ֵ
     */
    @Column(name = "group_value")
    private String groupValue;

    /**
     * ����
     */
    @Column(name = "key_desc")
    private String keyDesc;

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
     * ��ȡ�߼�id
     *
     * @return z_id - �߼�id
     */
    public String getzId() {
        return zId;
    }

    /**
     * �����߼�id
     *
     * @param zId �߼�id
     */
    public void setzId(String zId) {
        this.zId = zId;
    }

    /**
     * ��ȡ�������
     *
     * @return group_name - �������
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * �����������
     *
     * @param groupName �������
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * ��ȡ���code
     *
     * @return group_key - ���code
     */
    public String getGroupKey() {
        return groupKey;
    }

    /**
     * �������code
     *
     * @param groupKey ���code
     */
    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    /**
     * ��ȡ����ֵ
     *
     * @return group_value - ����ֵ
     */
    public String getGroupValue() {
        return groupValue;
    }

    /**
     * ���þ���ֵ
     *
     * @param groupValue ����ֵ
     */
    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }

    /**
     * ��ȡ����
     *
     * @return key_desc - ����
     */
    public String getKeyDesc() {
        return keyDesc;
    }

    /**
     * ��������
     *
     * @param keyDesc ����
     */
    public void setKeyDesc(String keyDesc) {
        this.keyDesc = keyDesc;
    }
}