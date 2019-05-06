package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.Seller;

public class SellerVo extends Seller {

    private Integer page =1;
    private Integer rows =10;


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
}
