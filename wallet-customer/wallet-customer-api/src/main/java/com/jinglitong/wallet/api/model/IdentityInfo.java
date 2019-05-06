package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import lombok.Data;

@Data
public class IdentityInfo implements Serializable , IAppIdModel{
    @Id
	private Integer id;
    @Column(name = "zid")
    private String zid;

    @Column(name = "cust_id")
    private String custId;

    @Column(name = "cust_name")
    private String custName;

    @Column(name = "identity_no")
    private String identityNo;

    @Column(name = "identity_type")
    private String identityType;

    @Column(name = "province_id")
    private Long provinceId;
    
    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "front_url")
    private String frontUrl;
    
    @Column(name ="back_url")
    private String backUrl;
    
	@Column(name = "created_time")
	private String createdTime;

	@Column(name = "updated_time")
	private String updatedTime;

	@Column(name = "auditstate")
	private String auditState;
    /**
     * 钱包app
     */
    private String appId;


}
