package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.IAppIdModel;
import lombok.Data;

@Data
public class AdminCreateVO implements IAppIdModel{
    private String adminId;
    private String username;
    private String passwd;
    private Boolean state;
    private String remark;
    private Boolean isSystem;
    private String appId;

    /**
     * 查询单个时封装ID
     */
    private String admin_id;

    private String roles;

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }
}
