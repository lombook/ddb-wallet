package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.SellerCoinInfo;

public class SellerCoinInfoVo extends SellerCoinInfo{

    private Integer page =1;
    private Integer rows =10;
    private Integer id;


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
