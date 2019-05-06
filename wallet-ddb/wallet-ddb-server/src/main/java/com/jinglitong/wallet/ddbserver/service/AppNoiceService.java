package com.jinglitong.wallet.ddbserver.service;

import com.jinglitong.wallet.api.model.AppNoticeSub;
import com.jinglitong.wallet.api.model.view.AppNoticeSubVo;
import com.jinglitong.wallet.ddbserver.mapper.AppNoticeMapper;
import com.jinglitong.wallet.ddbserver.mapper.AppNoticeSubMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AppNoiceService {
    @Autowired
    AppNoticeMapper appNoticeMapper;

	@Autowired
    AppNoticeSubMapper appNoticeSubMapper;
	


    /**
     * 添加系统给个人发的消息
     * 需要添加字段：
     * 		custID:用户custID
     * 		body:消息内容
     * 		title：标题
     * 		sub_title：副标题
     * 		viewCtime：显示时间
     * 		state：状态 true有效  false无效
     * 		appId:appid
     * @param appNoticeSubVO
     * @return 0：参数错误或失败    1：成功
     */
    public int createNoticeSub(AppNoticeSubVo appNoticeSubVO) {
    	if(appNoticeSubVO == null || appNoticeSubVO.getCustId() == null){
			return 0;
		}
        AppNoticeSub appNoticeSub = new AppNoticeSub();
        appNoticeSub.setAppId(appNoticeSubVO.getAppId());
        appNoticeSub.setZid(UuidUtil.getUUID());
        appNoticeSub.setNoticeId(UuidUtil.getUUID());
        appNoticeSub.setBody(appNoticeSubVO.getBody());
        
        if (appNoticeSubVO.getIsTop() != null) {
            if ("1".equals(appNoticeSubVO.getIsTop())) {
            	appNoticeSub.setIsTop(true);
            } else {
            	appNoticeSub.setIsTop(false);
            }
        } else {
        	appNoticeSub.setIsTop(false);
        }
        appNoticeSub.setNoticeImg(appNoticeSubVO.getNoticeImg());
        appNoticeSub.setNoticeType(appNoticeSubVO.getNoticeType());
        appNoticeSub.setNoticeType("1");
        Integer orderTop = appNoticeMapper.selecMaxOrderTop();
        if(orderTop == null){
        	appNoticeSub.setOrderTop(0);
        }else{
        	appNoticeSub.setOrderTop(orderTop+1);

        }
        appNoticeSub.setState(appNoticeSubVO.getState());
        /*if (appNoticeSubVO.getState() != null) {
            if (appNoticeSubVO.getState().equals(true)) {
            	appNoticeSub.setState(true);
            } else {
            	appNoticeSub.setState(false);
            }
        } else {
        	appNoticeSub.setState(false);
        }*/
        appNoticeSub.setSubTitle(appNoticeSubVO.getSubTitle());
        appNoticeSub.setTitle(appNoticeSubVO.getTitle());
        if (!StringUtils.isEmpty(appNoticeSubVO.getViewCtime()))
        	appNoticeSub.setViewCtime(appNoticeSubVO.getViewCtime());
        appNoticeSub.setType(appNoticeSubVO.getType());

        appNoticeSub.setIsPush(true);
        appNoticeSub.setCustId(appNoticeSubVO.getCustId());
		appNoticeSub.setSubType("2");

        String dateTime = DateUtils.getDateTime();
        appNoticeSub.setCreateTime(dateTime);
        appNoticeSub.setUpdateTime(dateTime);
     	
        int insert = appNoticeSubMapper.insert(appNoticeSub);

        return insert;
    }


}
