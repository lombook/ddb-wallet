package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.AppBanner;
import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.view.AppBannerVO;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface BannerMapper extends MyMapper<AppBanner> {
    
	List<AppBannerVO> getBannerList(AppBannerVO vo);

	List<AppBanner> getAppBanners(AppBanner appBanner );

	List<AppNotice>  getAppNotices();


}