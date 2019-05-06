package com.jinglitong.wallet.api.model;

import javax.persistence.*;
import java.util.Date;
import lombok.Data;

@Data
@Table(name = "company_info")
public class CompanyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * uuid
     */
    private String zid;

    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 公司地址
     */
    private String address;

    /**
     * 是否显示
     */
    @Column(name = "state")
    private Boolean state;

    /**
     * 电话
     */
    private String phone;

    /**
     * 法人
     */
    @Column(name = "legal_name")
    private String legalName;

    /**
     * 信用代码
     */
    @Column(name = "credit_code")
    private String creditCode;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private String createdTime;

    /**
     * 创建人
     */
    @Column(name = "created_user")
    private String createdUser;

    /**
     * 修改时间
     */
    @Column(name = "updated_time")
    private String updatedTime;

    /**
     * 修改人
     */
    @Column(name = "updated_user")
    private String updatedUser;


    /**
     * 获取uuid
     *
     * @return zid - uuid
     */
    public String getZid() {
        return zid;
    }

    /**
     * 设置uuid
     *
     * @param zid uuid
     */
    public void setZid(String zid) {
        this.zid = zid;
    }

    /**
     * 获取公司名称
     *
     * @return company_name - 公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置公司名称
     *
     * @param companyName 公司名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取公司地址
     *
     * @return address - 公司地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置公司地址
     *
     * @param address 公司地址
     */
    public void setAddress(String address) {
        this.address = address;
    }



    /**
     * 获取电话
     *
     * @return phone - 电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话
     *
     * @param phone 电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取法人
     *
     * @return legal_name - 法人
     */
    public String getLegalName() {
        return legalName;
    }

    /**
     * 设置法人
     *
     * @param legalName 法人
     */
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    /**
     * 获取信用代码
     *
     * @return credit_code - 信用代码
     */
    public String getCreditCode() {
        return creditCode;
    }

    /**
     * 设置信用代码
     *
     * @param creditCode 信用代码
     */
    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }



    /**
     * 获取创建人
     *
     * @return created_user - 创建人
     */
    public String getCreatedUser() {
        return createdUser;
    }

    /**
     * 设置创建人
     *
     * @param createdUser 创建人
     */
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }



    /**
     * 获取修改人
     *
     * @return updated_user - 修改人
     */
    public String getUpdatedUser() {
        return updatedUser;
    }

    /**
     * 设置修改人
     *
     * @param updatedUser 修改人
     */
    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}