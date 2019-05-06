package com.jinglitong.springshop.vo.request;

import lombok.Data;

import java.util.Date;

@Data
public class AppNoticeVO {
    private Integer id;
    private String noticeId;
    private String title;
    private String subTitle;
    private String isTop;
    private Integer orderTop;
    private String noticeType;
    private String noticeImg;
    private String state;
    private Date viewCtime;
    private String body;
    private Boolean isPush;
    private String type;
    private String subType;

}
