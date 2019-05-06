package com.jinglitong.springshop.entity;

import javax.persistence.*;

@Table(name = "propertie_tab")
public class PropertieTab{
    /**
     * id
     */
    @Id
    private Integer id;

    @Column(name = "zid")
    private String zid;

    /**
     * 类别名字
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 类别code
     */
    @Column(name = "group_key")
    private String groupKey;

    /**
     * 具体值
     */
    @Column(name = "group_value")
    private String groupValue;

    /**
     * 描述
     */
    @Column(name = "key_desc")
    private String keyDesc;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }

    public String getKeyDesc() {
        return keyDesc;
    }

    public void setKeyDesc(String keyDesc) {
        this.keyDesc = keyDesc;
    }

}