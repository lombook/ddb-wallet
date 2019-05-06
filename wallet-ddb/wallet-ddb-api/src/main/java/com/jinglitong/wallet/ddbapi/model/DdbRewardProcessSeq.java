package com.jinglitong.wallet.ddbapi.model;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;

import java.util.Date;
@Data
@Table(name = "ddb_reward_process_seq")
public class DdbRewardProcessSeq {
    /**
     * id
     */
    private Integer id;

    /**
     * 分红对象类型1订单,2育苗
     */
    private Integer type;

    /**
     * 分红对象ZID,对应订单表zid或育苗基地购买记录表zid
     */
    @Column(name = "order_zid")
    private String orderZid;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "state")
    private Boolean state;

    @Column(name = "process_time")
    private Date processTime;
}