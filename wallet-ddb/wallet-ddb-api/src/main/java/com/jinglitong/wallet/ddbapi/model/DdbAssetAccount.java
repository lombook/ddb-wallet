package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_asset_account")
public class DdbAssetAccount {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    @Column(name = "account_name")
    private String accountName;


    private String address;


    private String salt;


    @Column(name = "chain_id")
    private String chainId;


    @Column(name = "coin_id")
    private String coinId;


    private String currency;


    private Boolean state;


    @Column(name = "create_time")
    private String createTime;


    @Column(name = "update_time")
    private String updateTime;
    
    @Column(name = "type")
    private Integer type;


    private String secret;

}