package com.jinglitong.wallet.api.model.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LWalletCoin extends ErrorCode {
    private List<Map> dataList = new ArrayList();

    public List<Map> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map> dataList) {
        this.dataList = dataList;
    }
}
