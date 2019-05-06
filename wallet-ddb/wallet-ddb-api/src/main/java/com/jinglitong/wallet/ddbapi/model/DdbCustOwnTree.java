package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "ddb_cust_own_tree")
public class DdbCustOwnTree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    private String zid;

    /**
     * 用户id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 树的数量
     */
    @Column(name = "tree_num")
    private Integer treeNum;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
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
     * 获取用户id
     *
     * @return cust_id - 用户id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置用户id
     *
     * @param custId 用户id
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * 获取树的数量
     *
     * @return tree_num - 树的数量
     */
    public Integer getTreeNum() {
        return treeNum;
    }

    /**
     * 设置树的数量
     *
     * @param treeNum 树的数量
     */
    public void setTreeNum(Integer treeNum) {
        this.treeNum = treeNum;
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