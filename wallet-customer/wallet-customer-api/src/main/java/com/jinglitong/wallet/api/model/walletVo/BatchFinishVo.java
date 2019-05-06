package com.jinglitong.wallet.api.model.walletVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 2018/6/21.
 */
public class BatchFinishVo {
    private List<String> sourceIdList = new ArrayList<>();

    public List<String> getSourceIdList() {
        return sourceIdList;
    }

    public void setSourceIdList(List<String> sourceIdList) {
        this.sourceIdList = sourceIdList;
    }
}
