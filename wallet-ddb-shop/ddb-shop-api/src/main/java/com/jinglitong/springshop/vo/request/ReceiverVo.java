package com.jinglitong.springshop.vo.request;

import javax.persistence.Column;
import java.util.Date;

public class ReceiverVo {

    private Integer id;

    private String zid;

    private String address;


    private String areaname;

    private String consignee;


    private Integer isdefault;

    private String phone;


    private String zipcode;


    private Integer areaId;

    @Column(name = "cust_id")
    private String custId;


    private Date createdTime;


    private Date updateTime;

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
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * @return areaName
     */
    public String getAreaname() {
        return areaname;
    }

    /**
     * @param areaname
     */
    public void setAreaname(String areaname) {
        this.areaname = areaname == null ? null : areaname.trim();
    }

    /**
     * @return consignee
     */
    public String getConsignee() {
        return consignee;
    }

    /**
     * @param consignee
     */
    public void setConsignee(String consignee) {
        this.consignee = consignee == null ? null : consignee.trim();
    }

    /**
     * @return isDefault
     */
    public Integer getIsdefault() {
        return isdefault;
    }

    /**
     * @param isdefault
     */
    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
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
     * @return area_id
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * @param areaId
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * @return cust_id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * @param custId
     */
    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
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
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
