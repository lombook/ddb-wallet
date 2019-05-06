package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "ddb_shoreholder_rule")
public class DdbShoreholderRule {
    /**
     * id
     */
    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 层级高低排序（最低级为1）
     */
    @Column(name = "level_seq_no")
    private Integer levelSeqNo;

    /**
     * 股东升级条件
     */
    @Column(name = "upgrade_conditions")
    private String upgradeConditions;

    /**
     * 层级定义
     */
    @Column(name = "level_define")
    private String levelDefine;

    /**
     * 层级中文名
     */
    @Column(name = "level_cname")
    private String levelCname;

    /**
     * 生效时间
     */
    @Column(name = "effective_time")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @Column(name = "expiry_time")
    private Date expiryTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取业务id
     *
     * @return zid - 业务id
     */
    public String getZid() {
        return zid;
    }

    /**
     * 设置业务id
     *
     * @param zid 业务id
     */
    public void setZid(String zid) {
        this.zid = zid;
    }

    /**
     * 获取层级高低排序（最低级为1）
     *
     * @return level_seq_no - 层级高低排序（最低级为1）
     */
    public Integer getLevelSeqNo() {
        return levelSeqNo;
    }

    /**
     * 设置层级高低排序（最低级为1）
     *
     * @param levelSeqNo 层级高低排序（最低级为1）
     */
    public void setLevelSeqNo(Integer levelSeqNo) {
        this.levelSeqNo = levelSeqNo;
    }

    /**
     * 获取股东升级条件
     * @param upgrade_conditions
     */
    public String getUpgrade_conditions() {
		return upgradeConditions;
	}
    /**
     * 设置股东升级条件
     * @param upgradeConditions
     */
	public void setUpgradeConditions(String upgradeConditions) {
		this.upgradeConditions = upgradeConditions;
	}

	/**
     * 获取层级定义
     *
     * @return level_define - 层级定义
     */
    public String getLevelDefine() {
        return levelDefine;
    }

    /**
     * 设置层级定义
     *
     * @param levelDefine 层级定义
     */
    public void setLevelDefine(String levelDefine) {
        this.levelDefine = levelDefine;
    }

    /**
     * 获取层级中文名
     *
     * @return level_cname - 层级中文名
     */
    public String getLevelCname() {
        return levelCname;
    }

    /**
     * 设置层级中文名
     *
     * @param levelCname 层级中文名
     */
    public void setLevelCname(String levelCname) {
        this.levelCname = levelCname;
    }

    /**
     * 获取生效时间
     *
     * @return effective_time - 生效时间
     */
    public Date getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * 设置生效时间
     *
     * @param effectiveTime 生效时间
     */
    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * 获取失效时间
     *
     * @return expiry_time - 失效时间
     */
    public Date getExpiryTime() {
        return expiryTime;
    }

    /**
     * 设置失效时间
     *
     * @param expiryTime 失效时间
     */
    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}