package com.jinglitong.springshop.vo.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author fyy
 * @create 2019-01-24-18:16}
 */
@Data
public class CustVO extends PageVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    private String custId;

    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 带区域码手机号
     */
    @Column(name = "all_phone")
    private String allPhone;

    /**
     * 是否有效 1：有效 0：无效
     */
    private Boolean state;

    /**
     * 邀请码
     */
    @Column(name = "invite_code")
    private String inviteCode;
    
    private String superInviteCode;// 上级邀请码

    /**
     * 国家id
     */
    @Column(name = "country_id")
    private Integer countryId;

    /**
     * 盐
     */
    private String salt;

    /**
     * 语言
     */
    private String language;

    /**
     * 来源 :0手机app
     */
    private Integer origin;

    /**
     * 备注
     */
    private String remark;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "created_time")
    private Date createdTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "updated_time")
    private Date updatedTime;

    private String startTime;
    private String endTime;
}
