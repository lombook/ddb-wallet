package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;
import lombok.Data;

@Data
public class PropertieVO implements IAppIdModel{

    private String gName;
    private String gKey;
    private String appId;


    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgKey() {
        return gKey;
    }

    public void setgKey(String gKey) {
        this.gKey = gKey;
    }
}
