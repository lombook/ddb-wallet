package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_cust_exchange_record")
public class DdbCustExchangeRecord {

    @Id
    private Integer id;
    
    @Column(name = "record_id")
    private String recordId;


    @Column(name = "account_id")
    private String accountId;


    @Column(name = "cust_id")
    private String custId;


    @Column(name = "chain_id")
    private String chainId;


    private String amount;


    private String currency;


    @Column(name = "token_adress")
    private String tokenAdress;


    @Column(name = "pay_hash")
    private String payHash;


    private Integer state;


    private Integer count;



    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;
    
    


}