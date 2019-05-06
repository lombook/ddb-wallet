package com.jinglitong.springshop.entity;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.Map;

import javax.persistence.*;


@Table(name = "pay_config")
public class PayConfig {
    @Id
    private String zid;

    @Column(name = "isEnabled")
    private Boolean isenabled;

    @Column(name = "create_time")
    private Date createTime;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "remark")
    private String remark;
    
    @Column(name = "attributes")
    private String attributes;
    
    @Transient
    private Map<String,Object> map;
    
    @Transient
    public static PayConfig pluginConfig = null;

    /**
     * @return zid
     */
    public String getZid() {
        return zid;
    }

    /**
     * @param zid
     */
    public void setZid(String zid) {
        this.zid = zid == null ? null : zid.trim();
    }
    
    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    
    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return isEnabled
     */
    public Boolean getIsenabled() {
        return isenabled;
    }

    /**
     * @param isenabled
     */
    public void setIsenabled(Boolean isenabled) {
        this.isenabled = isenabled;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return attributes
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     */
    @SuppressWarnings("unchecked")
	public void setAttributes(String attributes) {
        this.attributes = attributes == null ? null : attributes.trim();
        if(attributes != null){
        	map = (Map<String, Object>) JSON.parse(attributes);
        }
    }
    
    @Transient
	public String getAttribute(String name) {
		return (String) map.get(name);
	}
}