package com.jinglitong.wallet.api.model;

import java.io.Serializable;

/**
 * appId 钱包唯一标识
 */
public interface IAppIdModel extends Serializable{

	String getAppId();

    void setAppId(String appId);
}
