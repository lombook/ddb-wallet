package com.jinglitong.wallet.server.controller.console;

import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.InvestAccountService;
import com.jinglitong.wallet.server.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinglitong.wallet.api.model.view.InvestAccountVO;
import com.jinglitong.wallet.server.service.MainChainService;

@Controller
@RequestMapping(value="/console")
public class InvestAccountController extends BaseController{
	
	@Autowired
    InvestAccountService investAccountService;
	
	@Autowired
    MainChainService mainChainService;
	
	/**
	 * 获取主链id和name
	 */
	@ResponseBody
	@RequestMapping(value="getMainChainInfo.json",method=RequestMethod.POST)
	public Map getMainChainInfo() {
		List list=mainChainService.getMainChainInfo(true);
		return JsonUtil.toJsonSuccess("获取链信息成功", list);
	}
	
	/**
	 * 增加激活账户
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="addInvestAccount.json",method=RequestMethod.POST)
	public Map addInvestAccount(@RequestBody InvestAccountVO vo ) throws Exception {
		setGlobalAdminAppId(vo);
		try {
			investAccountService.addInvestAccount(vo);
		}catch(Exception e) {
			if(ErrorEnum.ERROR_31460.getCode().toString().equals(e.getMessage())) {
				return JsonUtil.toJsonError(ErrorEnum.ERROR_31460);
			}else {
				return JsonUtil.toJsonError(ErrorEnum.ERROR_31461);
			}
		}
		
		
		return JsonUtil.toJsonSuccess("添加激活账户成功", null);
	}
	
	/**
	 * 修改激活账户
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="updInvestAccount.json",method=RequestMethod.POST)
	public Map updInvestAccount(@RequestBody InvestAccountVO vo )  {
		try {
			investAccountService.updInvestAccount(vo);
		} catch (Exception e) {
			e.printStackTrace();
			if(ErrorEnum.ERROR_31460.getCode().toString().equals(e.getMessage())) {
				return JsonUtil.toJsonError(ErrorEnum.ERROR_31460);
			}else {
				return JsonUtil.toJsonError(ErrorEnum.ERROR_31463);
			}
		}
		
		return JsonUtil.toJsonSuccess("修改激活账户成功", null);
	}
	
	/**
	 * 查询激活账户列表
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="qryInvestAccounts.json",method=RequestMethod.POST)
	public Map qryInvestAccounts(@RequestBody InvestAccountVO vo ) throws Exception {
		setGlobalAdminAppId(vo);
		Map<String , Object> map=investAccountService.qryInvestAccounts(vo);
		
		return JsonUtil.toJsonSuccess("查询账户成功", map);
		
	}
	
	
	

	

}
