package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    private BigDecimal balance;

    @Column(name = "bankAccount")
    private String bankaccount;

    @Column(name = "bankName")
    private String bankname;

    private String email;

    private String password;

    @Column(name = "frozenFund")
    private BigDecimal frozenfund;

    @Column(name = "idCard")
    private String idcard;

    @Column(name = "idCardImage")
    private String idcardimage;

    @Column(name = "identificationNumber")
    private String identificationnumber;

    @Column(name = "legalPerson")
    private String legalperson;

    @Column(name = "licenseImage")
    private String licenseimage;

    @Column(name = "licenseNumber")
    private String licensenumber;

    private String mobile;

    private String name;

    @Column(name = "organizationCode")
    private String organizationcode;

    @Column(name = "organizationImage")
    private String organizationimage;

    private String phone;

    @Column(name = "safeKeyValue")
    private String safekeyvalue;

    @Column(name = "taxImage")
    private String taximage;

    private String username;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "update_time")
    private Date updateTime;

    private String salt;

    @Column(name = "store_id")
    private String storeId;

    private Integer state;

    private String remark;

    /**
     *
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
     * @return balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return bankAccount
     */
    public String getBankaccount() {
        return bankaccount;
    }

    /**
     * @param bankaccount
     */
    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount == null ? null : bankaccount.trim();
    }

    /**
     * @return bankName
     */
    public String getBankname() {
        return bankname;
    }

    /**
     * @param bankname
     */
    public void setBankname(String bankname) {
        this.bankname = bankname == null ? null : bankname.trim();
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * @return frozenFund
     */
    public BigDecimal getFrozenfund() {
        return frozenfund;
    }

    /**
     * @param frozenfund
     */
    public void setFrozenfund(BigDecimal frozenfund) {
        this.frozenfund = frozenfund;
    }

    /**
     * @return idCard
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * @param idcard
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    /**
     * @return idCardImage
     */
    public String getIdcardimage() {
        return idcardimage;
    }

    /**
     * @param idcardimage
     */
    public void setIdcardimage(String idcardimage) {
        this.idcardimage = idcardimage == null ? null : idcardimage.trim();
    }

    /**
     * @return identificationNumber
     */
    public String getIdentificationnumber() {
        return identificationnumber;
    }

    /**
     * @param identificationnumber
     */
    public void setIdentificationnumber(String identificationnumber) {
        this.identificationnumber = identificationnumber == null ? null : identificationnumber.trim();
    }

    /**
     * @return legalPerson
     */
    public String getLegalperson() {
        return legalperson;
    }

    /**
     * @param legalperson
     */
    public void setLegalperson(String legalperson) {
        this.legalperson = legalperson == null ? null : legalperson.trim();
    }

    /**
     * @return licenseImage
     */
    public String getLicenseimage() {
        return licenseimage;
    }

    /**
     * @param licenseimage
     */
    public void setLicenseimage(String licenseimage) {
        this.licenseimage = licenseimage == null ? null : licenseimage.trim();
    }

    /**
     * @return licenseNumber
     */
    public String getLicensenumber() {
        return licensenumber;
    }

    /**
     * @param licensenumber
     */
    public void setLicensenumber(String licensenumber) {
        this.licensenumber = licensenumber == null ? null : licensenumber.trim();
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
     * @return organizationCode
     */
    public String getOrganizationcode() {
        return organizationcode;
    }

    /**
     * @param organizationcode
     */
    public void setOrganizationcode(String organizationcode) {
        this.organizationcode = organizationcode == null ? null : organizationcode.trim();
    }

    /**
     * @return organizationImage
     */
    public String getOrganizationimage() {
        return organizationimage;
    }

    /**
     * @param organizationimage
     */
    public void setOrganizationimage(String organizationimage) {
        this.organizationimage = organizationimage == null ? null : organizationimage.trim();
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
     * @return safeKeyValue
     */
    public String getSafekeyvalue() {
        return safekeyvalue;
    }

    /**
     * @param safekeyvalue
     */
    public void setSafekeyvalue(String safekeyvalue) {
        this.safekeyvalue = safekeyvalue == null ? null : safekeyvalue.trim();
    }

    /**
     * @return taxImage
     */
    public String getTaximage() {
        return taximage;
    }

    /**
     * @param taximage
     */
    public void setTaximage(String taximage) {
        this.taximage = taximage == null ? null : taximage.trim();
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
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
    /**
     * @return salt
     */
    public String getSalt() {
        return salt;
    }
    /**
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}