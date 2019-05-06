package com.jinglitong.wallet.server.controller.console;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.wallet.api.model.BasicCustomerInfo;
import com.jinglitong.wallet.api.model.view.CertificateVO;
import com.jinglitong.wallet.api.model.view.CustSelVO;
import com.jinglitong.wallet.server.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("console/auth/")
@Slf4j
public class CertAuthController extends BaseController {
	
	@Value("${ddb.appId}")
	private  String ddbAppId;

	@Autowired
	private com.jinglitong.wallet.server.service.CertificationService certificationService;
	
	@Autowired
	private com.jinglitong.wallet.server.service.CustomerService customerService;

	/**
	 * 实名认证审核
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "authCert.json", method = RequestMethod.POST)
	public Map authCert(@RequestBody CertificateVO vo) {
		return certificationService.authCert(vo);
	}
	
	 /**
     * 获取用户列表
     * @param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "getCustomers.json", method = RequestMethod.POST)
    public Map getCustomers(@RequestBody CustSelVO custSelVO ) {
    	custSelVO.setAppId(ddbAppId);
        HashMap<String, Object> map = customerService.getCustomers(custSelVO);
        return JsonUtil.toJsonSuccess("获用户列表成功", map);
    }
    
    @ResponseBody
    @RequestMapping(value = "customerInfo.json", method = RequestMethod.POST)
    public Map getBasicCustomerInfo(@RequestBody Map map) {
    	log.info(map.toString());
    	BasicCustomerInfo custInfo = customerService.getBasicCustomerInfo(map);
        return JsonUtil.toJsonSuccess("获取用户基本信息", custInfo);
    }

}
