package com.jinglitong.wallet.server.controller.console;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinglitong.wallet.api.model.view.FreezeDataVO;
import com.jinglitong.wallet.server.service.FreezeStatisticsService;
import com.jinglitong.wallet.server.util.JsonUtil;

@Controller
@RequestMapping(value = "/console")
public class FreezeStatisticsController extends BaseController{
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FreezeStatisticsService freezeStatisticsService;

	@ResponseBody
	@RequestMapping(value = "/freeze/freezeStatistics.json", method = RequestMethod.POST)
	public Map<String, Object> freezeStatistics(@RequestBody FreezeDataVO vo) {
		Map<String, Object> map = freezeStatisticsService.qryFreezeData(vo);
		return JsonUtil.toJsonSuccess("查询成功", map);
	}

}
