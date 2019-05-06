package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_levelup_welfare_rule")
public class DdbLevelupWelfareRule {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    @Column(name = "levelup_define")
    private String levelupDefine;

    @Column(name = "inte_amount")
    private Integer inteAmount;

    @Column(name = "inte_id")
    private String inteId;

    @Column(name = "create_time")
    private Date createTime;

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
        this.zid = zid;
    }

    /**
     * @return levelup_define
     */
    public String getLevelupDefine() {
        return levelupDefine;
    }

    /**
     * @param levelupDefine
     */
    public void setLevelupDefine(String levelupDefine) {
        this.levelupDefine = levelupDefine;
    }

    /**
     * @return inte_amount
     */
    public Integer getInteAmount() {
        return inteAmount;
    }

    /**
     * @param inteAmount
     */
    public void setInteAmount(Integer inteAmount) {
        this.inteAmount = inteAmount;
    }

    /**
     * @return inte_id
     */
    public String getInteId() {
        return inteId;
    }

    /**
     * @param inteId
     */
    public void setInteId(String inteId) {
        this.inteId = inteId;
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
}