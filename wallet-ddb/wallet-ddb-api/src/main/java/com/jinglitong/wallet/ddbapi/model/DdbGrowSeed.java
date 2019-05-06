package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_grow_seed")
@Data
public class DdbGrowSeed {
    @Id
    private Integer id;

    private String zid;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * ״̬ 0��δ���� 1���Ѵ���
     */
    private Boolean state;

    /**
     * ���
     */
    private Integer amount;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name= "process_time")
    private String processTime;

}