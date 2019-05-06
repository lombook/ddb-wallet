package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.CompanyInfo;

public class CompanyInfoVo extends CompanyInfo {

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
