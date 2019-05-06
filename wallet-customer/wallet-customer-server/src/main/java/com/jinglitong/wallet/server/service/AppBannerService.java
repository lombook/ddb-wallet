package com.jinglitong.wallet.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.server.common.storage.AliCloudStorageService;
import com.jinglitong.wallet.server.mapper.BannerMapper;
import com.jinglitong.wallet.api.model.AppBanner;
import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.view.AppBannerVO;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;

import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Transactional
public class AppBannerService {

	@Autowired
    public BannerMapper bannerMapper;
	@Resource
    private AliCloudStorageService aliCloudStorageService;
	
	public HashMap<String, Object> getBannerList(AppBannerVO vo){
		if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
        List<AppBannerVO> banners = bannerMapper.getBannerList(vo);
        PageInfo pageinfo = new PageInfo(banners);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("appHelps",banners);
        return  map;
	}
	
	public int  addBanner(AppBannerVO vo) throws Exception {
		AppBanner appBanner=new AppBanner();
		appBanner.setAppId(vo.getAppId());
		appBanner.setBanId(UuidUtil.getUUID());
		appBanner.setName(vo.getName());
		appBanner.setImgUrl(vo.getImgUrl());
		appBanner.setOrderId(vo.getOrderId());
		appBanner.setRemark(vo.getRemark());
		appBanner.setClickType(vo.getClickType());
		appBanner.setClickId(vo.getClickId());
		appBanner.setState("1");
		String date=DateUtils.getDateTime();
		appBanner.setCreateTime(date);
		appBanner.setUpdateTime(date);
		
		int insert = bannerMapper.insert(appBanner);
        return insert;
	}
	
	public int updBanner(AppBannerVO vo) {
		Weekend<AppBanner> weekend = Weekend.of(AppBanner.class);
		WeekendCriteria<AppBanner, Object> criteria = weekend.weekendCriteria();
		criteria.andEqualTo(AppBanner::getBanId, vo.getBanId());
		AppBanner appBanner = bannerMapper.selectOneByExample(weekend);
		appBanner.setAppId(vo.getAppId());
		appBanner.setName(vo.getName());
		appBanner.setImgUrl(vo.getImgUrl());
		appBanner.setOrderId(vo.getOrderId());
		appBanner.setClickType(vo.getClickType());
		appBanner.setClickId(vo.getClickId());
		appBanner.setRemark(vo.getRemark());
		appBanner.setState(vo.getState());
		appBanner.setUpdateTime(DateUtils.getDateTime());
		int i=bannerMapper.updateByPrimaryKeySelective(appBanner);
		return i;
	}
	
	public AppBanner bannerDetail(Map map) {
		Weekend<AppBanner> weekend = Weekend.of(AppBanner.class);
        WeekendCriteria<AppBanner, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(AppBanner::getBanId,map.get("id"));
        return bannerMapper.selectOneByExample(weekend);
        
	}
	
	public List getAppBanners(AppBanner appBanner ) {
		List<AppBanner> banners = bannerMapper.getAppBanners(appBanner);
		return banners;
		
	}
	
	public List getAppNotices() {
		List<AppNotice> notices = bannerMapper.getAppNotices();
		return notices;
		
	}
	
	
	
	
	
	
	
}
