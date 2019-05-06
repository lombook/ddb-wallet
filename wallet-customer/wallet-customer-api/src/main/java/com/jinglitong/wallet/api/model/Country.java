package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class Country {
    @Id
    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "nameEN")
    private String nameen;

    @Column(name = "nameCH")
    private String namech;

    @Column(name = "countryCode")
    private String countrycode;

    @Column(name = "areaCode")
    private String areacode;

    private Integer type;

    @Column(name = "createdAt")
    private Date createdat;

    @Column(name = "updatedAt")
    private Date updatedat;

    /**
     * @return country_id
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * @param countryId
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     * @return nameEN
     */
    public String getNameen() {
        return nameen;
    }

    /**
     * @param nameen
     */
    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    /**
     * @return nameCH
     */
    public String getNamech() {
        return namech;
    }

    /**
     * @param namech
     */
    public void setNamech(String namech) {
        this.namech = namech;
    }

    /**
     * @return countryCode
     */
    public String getCountrycode() {
        return countrycode;
    }

    /**
     * @param countrycode
     */
    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    /**
     * @return areaCode
     */
    public String getAreacode() {
        return areacode;
    }

    /**
     * @param areacode
     */
    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    /**
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return createdAt
     */
    public Date getCreatedat() {
        return createdat;
    }

    /**
     * @param createdat
     */
    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    /**
     * @return updatedAt
     */
    public Date getUpdatedat() {
        return updatedat;
    }

    /**
     * @param updatedat
     */
    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

}