package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_require_integral")
public class DdbDigAssetTodayDemand {
    /**
     * id
     */
    @Id
    private Integer id;


    @Column(name = "flow_id")
    private String flowId;


    @Column(name = "cust_id")
    private String custId;


    private String amount;


    @Column(name = "create_time")
    private String createTime;

}