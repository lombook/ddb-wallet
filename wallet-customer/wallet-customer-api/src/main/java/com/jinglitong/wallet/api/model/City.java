package com.jinglitong.wallet.api.model;

import javax.persistence.*;
import lombok.Data;

@Data
public class City extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String state;


}