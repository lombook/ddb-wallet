package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.server.mapper.AppNoticeMapper;
import com.jinglitong.wallet.server.mapper.AppNoticeSubMapper;
import com.jinglitong.wallet.server.mapper.BannerMapper;
import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.AppNoticeSub;
import com.jinglitong.wallet.api.model.view.AppNSelVO;
import com.jinglitong.wallet.api.model.view.AppNoticeSubVo;
import com.jinglitong.wallet.api.model.view.AppNoticeVO;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;


@Service
@Transactional
public class AppNoiceService {
    @Autowired
    AppNoticeMapper appNoticeMapper;

	@Autowired
	AppNoticeSubMapper appNoticeSubMapper;
    
    @Autowired
    BannerMapper bannerMapper;

    public HashMap<String, Object> getNoticeList(AppNSelVO appNSelVO) {
        HashMap<String, Object> selectmap = new HashMap<>();
        if (appNSelVO.getPage() != null && appNSelVO.getRows() != null) {
            PageHelper.startPage(appNSelVO.getPage(), appNSelVO.getRows());
        }
        List<AppNoticeVO> notices = appNoticeMapper.getNoticeList(appNSelVO);
        PageInfo pageinfo = new PageInfo(notices);
        HashMap<String, Object> map = new HashMap<>();

        map.put("pageCount", pageinfo.getTotal());
        map.put("notices", notices);
        return map;
    }

    public int createNotice(AppNoticeVO appNoticeVO) {
        AppNotice appNotice = new AppNotice();
        appNotice.setAppId(appNoticeVO.getAppId());
        appNotice.setNoticeId(UuidUtil.getUUID());
        appNotice.setBody(appNoticeVO.getBody());
        if (appNoticeVO.getIsTop() != null) {
            if ("1".equals(appNoticeVO.getIsTop())) {
                appNotice.setIsTop(true);
            } else {
                appNotice.setIsTop(false);
            }
        } else {
            appNotice.setIsTop(false);
        }
        appNotice.setNoticeImg(appNoticeVO.getNoticeImg());
        appNotice.setNoticeType(appNoticeVO.getNoticeType());
        Integer orderTop = appNoticeMapper.selecMaxOrderTop();
        if(orderTop == null){
            appNotice.setOrderTop(0);
        }else{
            appNotice.setOrderTop(orderTop+1);

        }
        if (appNoticeVO.getState() != null) {
            if (appNoticeVO.getState().equals("1")) {
                appNotice.setState(true);
            } else {
                appNotice.setState(false);
            }
        } else {
            appNotice.setState(false);
        }
        appNotice.setSubTitle(appNoticeVO.getSubTitle());
        appNotice.setTitle(appNoticeVO.getTitle());
        if (!StringUtils.isEmpty(appNoticeVO.getViewCtime()))
            appNotice.setViewCtime(appNoticeVO.getViewCtime());
        appNotice.setType(appNoticeVO.getType());
		if(appNotice.getType().equals("1")){
			appNotice.setIsPush(true);   	
		}
		else{
			appNotice.setIsPush(false); 
		}
        String dateTime = DateUtils.getDateTime();
        appNotice.setCreateTime(dateTime);
        appNotice.setUpdateTime(dateTime);
        int insert = appNoticeMapper.insert(appNotice);
        if(appNotice.getType().equals("1")){
        	AppNoticeSub appNoticesub = new AppNoticeSub();
        	appNoticesub.setAppId(appNotice.getAppId());
        	appNoticesub.setZid(UuidUtil.getUUID());
        	appNoticesub.setNoticeId(appNotice.getNoticeId());
        	appNoticesub.setBody(appNotice.getBody());
        	appNoticesub.setIsTop(appNotice.getIsTop());           
            appNoticesub.setNoticeImg(appNotice.getNoticeImg());
            appNoticesub.setNoticeType(appNotice.getNoticeType());
            appNoticesub.setOrderTop(appNotice.getOrderTop());
            appNoticesub.setState(appNotice.getState());
            appNoticesub.setSubTitle(appNotice.getSubTitle());
            appNoticesub.setTitle(appNotice.getTitle());
            appNoticesub.setViewCtime(appNotice.getViewCtime());            	
            appNoticesub.setType(appNotice.getType());
            appNoticesub.setIsPush(appNotice.getIsPush()); 
            appNoticesub.setCreateTime(dateTime);
            appNoticesub.setUpdateTime(dateTime);
            appNoticesub.setSubType("1");
            appNoticeSubMapper.insert(appNoticesub);
        }
        return insert;
    }
    
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
		if(appNoticeSub.getType().equals("1")){
			appNoticeSub.setIsPush(true);   	
		}
		else{
			appNoticeSub.setIsPush(false); 
		}
				
		appNoticeSub.setCustId(appNoticeSubVO.getCustId());
		appNoticeSub.setSubType("2");

        String dateTime = DateUtils.getDateTime();
        appNoticeSub.setCreateTime(dateTime);
        appNoticeSub.setUpdateTime(dateTime);
     	
        int insert = appNoticeSubMapper.insert(appNoticeSub);

        return insert;
    }

    public AppNotice getNotice(AppNoticeVO appNoticeVO ) {
        Weekend<AppNotice> weekend = Weekend.of(AppNotice.class);
        WeekendCriteria<AppNotice, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(AppNotice::getNoticeId, appNoticeVO.getNoticeId());
        if(StringUtils.isEmpty(appNoticeVO.getAppId())){
            criteria.andEqualTo(AppNotice::getAppId, appNoticeVO.getAppId());
        }
        AppNotice appNotice = appNoticeMapper.selectOneByExample(weekend);
        return appNotice;
    }

    public int updateNotice(AppNoticeVO appNoticeVO) {
        Weekend<AppNotice> weekend = Weekend.of(AppNotice.class);
        WeekendCriteria<AppNotice, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(AppNotice::getNoticeId, appNoticeVO.getNoticeId());
        AppNotice appNotice = appNoticeMapper.selectOneByExample(weekend);
        
        Weekend<AppNoticeSub> weekendsub = Weekend.of(AppNoticeSub.class);
        WeekendCriteria<AppNoticeSub, Object> criteriasub = weekendsub.weekendCriteria();
        criteriasub.andEqualTo(AppNoticeSub::getNoticeId, appNoticeVO.getNoticeId());
        
        AppNoticeSub appNoticeSub = appNoticeSubMapper.selectOneByExample(weekendsub);
        
        
        appNotice.setAppId(appNoticeVO.getAppId());
        appNotice.setNoticeId(appNoticeVO.getNoticeId());
        if (appNoticeVO.getBody() != null) {
            appNotice.setBody(appNoticeVO.getBody());
        }
        if (appNoticeVO.getIsTop() != null) {
            if (appNoticeVO.getIsTop().equals("1")) {
                appNotice.setIsTop(true);
            } else {
                appNotice.setIsTop(false);
            }
        } else {
            appNotice.setIsTop(false);
        }
        if (appNotice.getNoticeImg() != null) {
            appNotice.setNoticeImg(appNoticeVO.getNoticeImg());
        }
        if (appNoticeVO.getNoticeType() != null) {
            appNotice.setNoticeType(appNoticeVO.getNoticeType());
        }
        if (appNoticeVO.getOrderTop() != null) {
            appNotice.setOrderTop(appNoticeVO.getOrderTop());
        }
        if (appNoticeVO.getState() != null) {
            if (appNoticeVO.getState().equals("1")) {
                appNotice.setState(true);
            } else {
            	appNoticeMapper.deleteBannerRelation(appNoticeVO.getNoticeId());
                appNotice.setState(false);
            }
        } else {
            appNotice.setState(false);
        }
        if (appNoticeVO.getSubTitle() != null) {
            appNotice.setSubTitle(appNoticeVO.getSubTitle());
        }
        if (appNoticeVO.getTitle() != null) {
            appNotice.setTitle(appNoticeVO.getTitle());
        }
        if (!StringUtils.isEmpty(appNoticeVO.getViewCtime()))
            appNotice.setViewCtime(appNoticeVO.getViewCtime());
        appNotice.setUpdateTime(DateUtils.getDateTime());
        int update = appNoticeMapper.updateByPrimaryKeySelective(appNotice);
        
        if(appNotice.getType().equals("1")){
        	appNoticeSub.setAppId(appNotice.getAppId());
        	appNoticeSub.setBody(appNotice.getBody());
        	appNoticeSub.setIsTop(appNotice.getIsTop());
        	appNoticeSub.setNoticeType(appNotice.getNoticeType());
        	appNoticeSub.setOrderTop(appNotice.getOrderTop());
        	appNoticeSub.setState(appNotice.getState());
        	appNoticeSub.setSubTitle(appNotice.getSubTitle());
        	appNoticeSub.setTitle(appNotice.getTitle());
        	appNoticeSub.setViewCtime(appNotice.getViewCtime());
        	appNoticeSub.setUpdateTime(appNotice.getUpdateTime());
        	appNoticeSubMapper.updateByPrimaryKeySelective(appNoticeSub);
        }
        return update;
    }

    public int updateNoticeby(AppNoticeVO appNoticeVO) {
        Weekend<AppNotice> weekend = Weekend.of(AppNotice.class);
        WeekendCriteria<AppNotice, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(AppNotice::getNoticeId, appNoticeVO.getNoticeId());
        AppNotice appNotice = appNoticeMapper.selectOneByExample(weekend);
        if (appNoticeVO.getState() != null) {
            Boolean state = true;
            if (!"1".equals(appNoticeVO.getState()))
                state = false;
            appNotice.setState(state);
        }
        if (appNoticeVO.getIsTop() != null) {
            Boolean isTop = true;
            if (!"1".equals(appNoticeVO.getIsTop()))
                isTop = false;
            appNotice.setIsTop(isTop);
        }
        if (appNoticeVO.getOrderTop() != null) {
            appNotice.setOrderTop(appNoticeVO.getOrderTop());
        }
        appNotice.setNoticeId(appNoticeVO.getNoticeId());
        appNotice.setUpdateTime(DateUtils.getDateTime());
        int update = appNoticeMapper.updateByPrimaryKeySelective(appNotice);

        return update;
    }

}
