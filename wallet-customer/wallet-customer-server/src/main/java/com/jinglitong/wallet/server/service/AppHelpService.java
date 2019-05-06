package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.AppHelp;
import com.jinglitong.wallet.api.model.view.AppHelpCreateVO;
import com.jinglitong.wallet.api.model.view.AppHelpSelVO;
import com.jinglitong.wallet.server.mapper.AdminRoleMapper;
import com.jinglitong.wallet.server.util.UuidUtil;

import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.jinglitong.wallet.server.mapper.AppHelpMapper;
import com.jinglitong.wallet.server.util.DateUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class AppHelpService {

    @Autowired
    AppHelpMapper appHelpMapper;

    public HashMap<String,Object> getapplists(AppHelpSelVO appHelpSelVO) {

        if (appHelpSelVO.getPage() != null && appHelpSelVO.getRows() != null) {
            PageHelper.startPage(appHelpSelVO.getPage(), appHelpSelVO.getRows(),"show_order,create_time");
        }
        List<AppHelp> appHelps = appHelpMapper.getAppHelps(appHelpSelVO);
        PageInfo pageinfo = new PageInfo(appHelps);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("appHelps",appHelps);
        return  map;
    }


    public int createApphelp(AppHelpCreateVO appHelpCreateVO) {
        AppHelp appHelp = new AppHelp();
        appHelp.setHelpId(UuidUtil.getUUID());
        if(!StringUtils.isEmpty(appHelpCreateVO.getAppType()))
            appHelp.setAppType(appHelpCreateVO.getAppType());
        if(!StringUtils.isEmpty(appHelpCreateVO.getBody()))
            appHelp.setBody(appHelpCreateVO.getBody());
        if(!StringUtils.isEmpty(appHelpCreateVO.getRemark()))
            appHelp.setRemark(appHelpCreateVO.getRemark());
        if(appHelpCreateVO.getShowOrder() != null)
            appHelp.setShowOrder(appHelpCreateVO.getShowOrder());
        if(!StringUtils.isEmpty(appHelpCreateVO.getTitle()))
            appHelp.setTitle(appHelpCreateVO.getTitle());
        if(appHelpCreateVO.getState() != null)
            appHelp.setState(appHelpCreateVO.getState());
        appHelp.setAppId(appHelpCreateVO.getAppId());
        appHelp.setCreateTime(new Timestamp(new Date().getTime()));
        appHelp.setUpdateTime(new Timestamp(new Date().getTime()));
        int insert = appHelpMapper.insert(appHelp);
        return insert;
    }

    public int updateapphelp(AppHelpCreateVO appHelpCreateVO) {
    	Weekend<AppHelp> weekend = Weekend.of(AppHelp.class);
    	WeekendCriteria<AppHelp,Object>criteria = weekend.weekendCriteria();
    	 criteria.andEqualTo(AppHelp::getHelpId, appHelpCreateVO.getAppHelpId());
        AppHelp appHelp = appHelpMapper.selectOneByExample(weekend);
        appHelp.setHelpId(appHelpCreateVO.getAppHelpId());
        if(!StringUtils.isEmpty(appHelpCreateVO.getAppType()))
            appHelp.setAppType(appHelpCreateVO.getAppType());
        if(!StringUtils.isEmpty(appHelpCreateVO.getBody()))
            appHelp.setBody(appHelpCreateVO.getBody());
        if(!StringUtils.isEmpty(appHelpCreateVO.getRemark()))
            appHelp.setRemark(appHelpCreateVO.getRemark());
        if(appHelpCreateVO.getShowOrder() != null)
            appHelp.setShowOrder(appHelpCreateVO.getShowOrder());
        if(!StringUtils.isEmpty(appHelpCreateVO.getTitle()))
            appHelp.setTitle(appHelpCreateVO.getTitle());
        if(appHelpCreateVO.getState() != null)
            appHelp.setState(appHelpCreateVO.getState());
        if(appHelpCreateVO.getAppId() != null)
            appHelp.setAppId(appHelpCreateVO.getAppId());
        appHelp.setUpdateTime(new Timestamp(new Date().getTime()));
        int update = appHelpMapper.updateByPrimaryKey(appHelp);
        return update;
    }

    public AppHelp getapphelp(String appHelpId) {
        AppHelp appHelp = appHelpMapper.selectByPrimaryKey(appHelpId);
        return appHelp;
    }


    public int updateHelpState(String helpId, Boolean state) {
        AppHelp appHelp = new AppHelp();
        appHelp.setHelpId(helpId);
        appHelp.setState(state);
        int update = appHelpMapper.updateByPrimaryKeySelective(appHelp);
        return update;
    }
}
