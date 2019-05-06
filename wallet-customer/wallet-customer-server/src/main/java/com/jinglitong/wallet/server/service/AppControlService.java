package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.view.AppConSelVO;
import com.jinglitong.wallet.api.model.view.AppControlVO;
import com.jinglitong.wallet.api.model.view.AppHelpVO;
import com.jinglitong.wallet.server.mapper.AppControlMapper;
import com.jinglitong.wallet.server.mapper.AppHelpMapper;
import com.jinglitong.wallet.server.mapper.AppNoticeMapper;
import com.jinglitong.wallet.server.mapper.AppNoticeSubMapper;
import com.jinglitong.wallet.server.mapper.NoticeCustmerCenterMapper;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;

import java.util.Date;
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
public class AppControlService {
    @Autowired
    private AppControlMapper appControlMapper;

    @Autowired
    private AppNoticeMapper appNoticeMapper;
    
    @Autowired
    private AppNoticeSubMapper appNoticeSubMapper;

    @Autowired
    private AppHelpMapper appHelpMapper;

    @Autowired
    private NoticeCustmerCenterMapper noticeCustmerCenterMapper;
    
    public List<AppControl> getAppVersionByType(String type,String appId){
        Weekend<AppControl> weekend = Weekend.of(AppControl.class);
        WeekendCriteria<AppControl, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(AppControl::getAppType,type);
        criteria.andEqualTo(AppControl::getAppId,appId);
        weekend.setOrderByClause("app_version_code desc limit 1");
        return appControlMapper.selectByExample(weekend);
    }

    public List<AppNotice> getAppNotices(AppNotice appNotice) {
        if (appNotice.getPage() != null && appNotice.getRows() != null) {
            PageHelper.startPage(appNotice.getPage(), appNotice.getRows(),"is_top desc,order_top desc,create_time desc");
        }
        return appNoticeMapper.getTitle(appNotice);
    }

    public Object getAppNotice(String noticeId,String cust_id) {
//    	AppNotice notice=appNoticeMapper.selectByPrimaryKey(noticeId);
    	AppNotice notice = null;
    	AppNoticeSub noticesub = null;
    	//根据noticeId查找到type和subType
    	Weekend<AppNoticeSub> weekend = Weekend.of(AppNoticeSub.class);
        WeekendCriteria<AppNoticeSub, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(AppNoticeSub::getNoticeId,noticeId);
        noticesub = appNoticeSubMapper.selectOneByExample(weekend);
        if(noticesub == null){
        	//不是系统升级，也不是系统给个人发的，在大表里
        	Weekend<AppNotice> weekendappNotice = Weekend.of(AppNotice.class);
            WeekendCriteria<AppNotice, Object> criteriaappNotice = weekendappNotice.weekendCriteria();
            criteriaappNotice.andEqualTo(AppNotice::getNoticeId,noticeId);
        	notice=appNoticeMapper.selectOneByExample(weekendappNotice);
        	if(notice != null){
                notice.setViewCtime(notice.getViewCtime().substring(0, 19));
            }
        }else{
        	if(noticesub.getSubType().equals("2") && cust_id == null){
        		//用户没登录，不能查看subType为2的通知公告
        		return 0;
        	}
        	noticesub.setViewCtime(noticesub.getViewCtime().substring(0, 19));
        }
    	
    	
    	//如果用户登录了，并且查看的是系统升级类型的 通知  向通中心表添加改用户查看过该通知的记录
    	if(cust_id != null && noticesub != null){
    		Weekend<NoticeCustmerCenter> weekendRecord = Weekend.of(NoticeCustmerCenter.class);
            WeekendCriteria<NoticeCustmerCenter, Object> criteriaRecord = weekendRecord.weekendCriteria();
            criteriaRecord.andEqualTo(NoticeCustmerCenter::getAppNoticeId,noticeId);
            criteriaRecord.andEqualTo(NoticeCustmerCenter::getCustId,cust_id);
            NoticeCustmerCenter record = noticeCustmerCenterMapper.selectOneByExample(weekendRecord);
    		if(record == null){
        		NoticeCustmerCenter noticeCenter = new NoticeCustmerCenter();
            	noticeCenter.setAppNoticeId(noticesub.getNoticeId());
            	noticeCenter.setCreateTime(new Date());
            	noticeCenter.setCustId(cust_id);
            	noticeCenter.setUpdateTime(new Date());
            	noticeCenter.setZid(UuidUtil.getUUID());
            	noticeCenter.setAppId(noticesub.getAppId());
            	noticeCustmerCenterMapper.insert(noticeCenter);
        	} 
    	}
    	  	
        if(notice != null){
        	return notice;
        }
        return noticesub;
    }

    public List<AppHelp> getAppHelpByType(AppHelpVO vo) {
        return appHelpMapper.getAppHelpByType(vo);
    }

    public AppHelp getAppHelp(String appHelpId) {
//        return appHelpMapper.selectByPrimaryKey(appHelpId);
        
        Weekend<AppHelp> weekend = Weekend.of(AppHelp.class);
        WeekendCriteria<AppHelp, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(AppHelp::getHelpId,appHelpId);
    	
        AppHelp notice=appHelpMapper.selectOneByExample(weekend);
//    	notice.setViewCtime(notice.getViewCtime().substring(0, 19));
        return notice;
    }


    public HashMap<String,Object> applists(AppConSelVO appConSelVO) {
        if (appConSelVO.getPage() != null && appConSelVO.getRows() != null) {
            PageHelper.startPage(appConSelVO.getPage(), appConSelVO.getRows());
        }
        List<AppConSelVO> appControl = appControlMapper.getAppControls(appConSelVO);
        PageInfo pageinfo = new PageInfo(appControl);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("appHelps",appControl);
        return  map;
    }

    public int createappControl(AppControlVO appControlVO) {
        /*Integer max = appControlMapper.getVersionCount(appControlVO.getAppType());
        if(max == null){
            max = 1;
        }else{
            max = max+1;
        }*/
        AppControl appControl = new AppControl();
        appControl.setzId(UuidUtil.getUUID());
        appControl.setAppId(appControlVO.getAppId());
        if(appControlVO.getAppFource() != null)
            appControl.setAppFource(appControlVO.getAppFource());
        if(!StringUtils.isEmpty(appControlVO.getAppName()))
            appControl.setAppName(appControlVO.getAppName());
        if(!StringUtils.isEmpty(appControlVO.getAppRemark()))
            appControl.setAppRemark(appControlVO.getAppRemark());
        if(!StringUtils.isEmpty(appControlVO.getAppUrl()))
            appControl.setAppUrl(appControlVO.getAppUrl());
        if(!StringUtils.isEmpty(appControlVO.getAppVersion()))
            appControl.setAppVersion(appControlVO.getAppVersion());
        if(appControlVO.getAppVersionCode() != null)
            appControl.setAppVersionCode(appControlVO.getAppVersionCode());
        if(!StringUtils.isEmpty(appControlVO.getAppType()))
            appControl.setAppType(appControlVO.getAppType());
        appControl.setCreatedTime(DateUtils.getDateTime());
       appControl.setUpdatedTime(appControlVO.getUpdatedTime());
        int insert = appControlMapper.insert(appControl);
        return insert;
    }

    public int updateAappControl(AppControlVO appControlVO) {
        AppControl appControl = appControlMapper.selectByPrimaryKey(appControlVO.getId());
        appControl.setAppId(appControlVO.getAppId());
        if(appControlVO.getAppFource() != null)
            appControl.setAppFource(appControlVO.getAppFource());
        if(!StringUtils.isEmpty(appControlVO.getAppName()))
            appControl.setAppName(appControlVO.getAppName());
        if(!StringUtils.isEmpty(appControlVO.getAppRemark()))
            appControl.setAppRemark(appControlVO.getAppRemark());
        if(!StringUtils.isEmpty(appControlVO.getAppUrl()))
            appControl.setAppUrl(appControlVO.getAppUrl());
        if(!StringUtils.isEmpty(appControlVO.getAppVersion()))
            appControl.setAppVersion(appControlVO.getAppVersion());
        if(appControlVO.getAppVersionCode() != null)
            appControl.setAppVersionCode(appControlVO.getAppVersionCode());
        if(!StringUtils.isEmpty(appControlVO.getAppType()))
            appControl.setAppType(appControlVO.getAppType());
       appControl.setUpdatedTime(appControlVO.getUpdatedTime());
        int update = appControlMapper.updateByPrimaryKeySelective(appControl);
        return update;
    }

    public AppControl getAppControl(Integer id) {
        AppControl appControl = appControlMapper.selectByPrimaryKey(id);
        return appControl;
    }
}
