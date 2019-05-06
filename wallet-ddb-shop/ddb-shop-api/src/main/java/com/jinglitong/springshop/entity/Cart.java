package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;

@Data
@ToString
public class Cart {

    public Cart(String zid, String clientId, String custId, Date expire, String cartkey, Date createdTime, Date updatedTime) {
        this.zid = zid;
        this.clientId = clientId;
        this.custId = custId;
        this.expire = expire;
        this.cartkey = cartkey;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public Cart(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主键
     */
    private String zid;

    /**
     * 设备id
     */
    @Column(name = "client_id")
    private String clientId;

    /**
     * 用户主键
     */
    @Column(name = "cust_id")
    private String custId;

    private Date expire;

    @Column(name = "cartKey")
    private String cartkey;
    
    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

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
     * 获取主键
     *
     * @return zid - 主键
     */
    public String getZid() {
        return zid;
    }

    /**
     * 设置主键
     *
     * @param zid 主键
     */
    public void setZid(String zid) {
        this.zid = zid == null ? null : zid.trim();
    }

    /**
     * 获取设备id
     *
     * @return client_id - 设备id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 设置设备id
     *
     * @param clientId 设备id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    /**
     * 获取用户主键
     *
     * @return cust_id - 用户主键
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置用户主键
     *
     * @param custId 用户主键
     */
    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }

    /**
     * @return expire
     */
    public Date getExpire() {
        return expire;
    }

    /**
     * @param expire
     */
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    /**
     * @return cartKey
     */
    public String getCartkey() {
        return cartkey;
    }

    /**
     * @param cartkey
     */
    public void setCartkey(String cartkey) {
        this.cartkey = cartkey == null ? null : cartkey.trim();
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
}