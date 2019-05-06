package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@ToString
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 店铺email
     */
    private String email;

    /**
     * 是否启用
     */
    @Column(name = "isEnabled")
    private Byte isenabled;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * logo
     */
    private String logo;

    private String mobile;

    private String name;

    private String phone;

    private Byte status;

    private Byte type;

    @Column(name = "zipCode")
    private String zipcode;

    @Column(name = "business_id")
    private String businessId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 店铺介绍
     */
    private String introduction;
    
    /**
     * 联系人
     */
    private String contacts;
    

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
     * 获取店铺地址
     *
     * @return address - 店铺地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置店铺地址
     *
     * @param address 店铺地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取店铺email
     *
     * @return email - 店铺email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置店铺email
     *
     * @param email 店铺email
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取是否启用
     *
     * @return isEnabled - 是否启用
     */
    public Byte getIsenabled() {
        return isenabled;
    }

    /**
     * 设置是否启用
     *
     * @param isenabled 是否启用
     */
    public void setIsenabled(Byte isenabled) {
        this.isenabled = isenabled;
    }

    /**
     * 获取关键词
     *
     * @return keyword - 关键词
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * 设置关键词
     *
     * @param keyword 关键词
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    /**
     * 获取logo
     *
     * @return logo - logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置logo
     *
     * @param logo logo
     */
    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
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
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * @return type
     */
    public Byte getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * @return zipCode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode == null ? null : zipcode.trim();
    }

    /**
     * @return business_id
     */
    public String getBusinessId() {
        return businessId;
    }

    /**
     * @param businessId
     */
    public void setBusinessId(String businessId) {
        this.businessId = businessId == null ? null : businessId.trim();
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取店铺介绍
     *
     * @return introduction - 店铺介绍
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置店铺介绍
     *
     * @param introduction 店铺介绍
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}