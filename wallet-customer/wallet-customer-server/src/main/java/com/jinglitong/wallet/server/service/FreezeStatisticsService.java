package com.jinglitong.wallet.server.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.server.mapper.FreezeStatisticsMapper;
import com.jinglitong.wallet.api.model.FreezeUserInfo;
import com.jinglitong.wallet.api.model.view.FreezeDataVO;

@Service
public class FreezeStatisticsService {
	@Autowired
	private FreezeStatisticsMapper freezeStatisticsMapper;

	public Map<String, Object> qryFreezeData(FreezeDataVO vo) {

		if (vo.getPage() != null && vo.getRows() != null) {
			PageHelper.startPage(vo.getPage(), vo.getRows());
		}
		List<FreezeUserInfo> freezeUserInfos = freezeStatisticsMapper.getFreezeData(vo);
		for (FreezeUserInfo fui : freezeUserInfos) {
			BigDecimal allAmount = new BigDecimal(fui.getTotalAmount());
			BigDecimal releaseAmount = new BigDecimal(fui.getFreezeAmount());
			fui.setReleaseAmount(allAmount.subtract(releaseAmount).toString());
			String status = fui.getStatus();
			if ("0".equals(status)) {
				fui.setStatus("未开始");
			} else if ("1".equals(status)) {
				fui.setStatus("进行中");
			} else if ("2".equals(status)) {
				fui.setStatus("已完成");
			} else if ("3".equals(status)) {
				fui.setStatus("终止");
			}

		}
		PageInfo pageinfo = new PageInfo(freezeUserInfos);
		Map<String, Object> freezeUser = new HashMap<String, Object>();
		freezeUser.put("pageCount", pageinfo.getTotal());
		freezeUser.put("freezeUserList", freezeUserInfos);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("freezeData", freezeUser);
		int totalCount = freezeStatisticsMapper.getTotalCount(vo.getAppId());
		List<Map<String, Object>> totalAmount = freezeStatisticsMapper.getTotalAmount(vo.getAppId());
		map.put("totalCount", totalCount);
		map.put("totalAmount", totalAmount);

		return map;
	}
}
