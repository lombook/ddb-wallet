package com.jinglitong.springshop.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "chain_information")
public class ChainInformation {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private Date createdDate;

    /**
     * 子订单号
     */
    @Column(name = "sub_order")
    private String orderNumber;

    /**
     * 子订单编号
     */
    @Column(name ="sub_order_id")
    private String subOrderSn;

    /**
     * 主订单号
     */
    @Column(name = "master_order")
    private String masterOrder;

    /**
     * 哈希
     */
    @Column(name = "hash_codde")
    private String hashCodde;

    /**
     * 状态  0已上链，未确认 1已上链，已确认
     */
    @Column(name = "chain_status")
    private Integer chainStatus;

    /**
     * 上链确认时间
     */
    @Column(name = "confirmation_date")
    private Date confirmationDate;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return created_date - 创建时间
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * 设置创建时间
     *
     * @param createdDate 创建时间
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * 获取子订单号
     *
     * @return order_number - 子订单号
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * 设置子订单号
     *
     * @param orderNumber 子订单号
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    /**
     * 获取主订单号
     *
     * @return master_order - 主订单号
     */
    public String getMasterOrder() {
        return masterOrder;
    }

    /**
     * 设置主订单号
     *
     * @param masterOrder 主订单号
     */
    public void setMasterOrder(String masterOrder) {
        this.masterOrder = masterOrder == null ? null : masterOrder.trim();
    }

    /**
     * 获取哈希
     *
     * @return hash_codde - 哈希
     */
    public String getHashCodde() {
        return hashCodde;
    }

    /**
     * 设置哈希
     *
     * @param hashCodde 哈希
     */
    public void setHashCodde(String hashCodde) {
        this.hashCodde = hashCodde == null ? null : hashCodde.trim();
    }

    /**
     * 获取状态  0已上链，未确认 1已上链，已确认
     *
     * @return chain_status - 状态  0已上链，未确认 1已上链，已确认
     */
    public Integer getChainStatus() {
        return chainStatus;
    }

    /**
     * 设置状态  0已上链，未确认 1已上链，已确认
     *
     * @param chainStatus 状态  0已上链，未确认 1已上链，已确认
     */
    public void setChainStatus(Integer chainStatus) {
        this.chainStatus = chainStatus;
    }

    /**
     * 获取上链确认时间
     *
     * @return confirmation_date - 上链确认时间
     */
    public Date getConfirmationDate() {
        return confirmationDate;
    }

    /**
     * 设置上链确认时间
     *
     * @param confirmationDate 上链确认时间
     */
    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
}