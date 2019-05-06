package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.CustomerAPIService;
import com.jinglitong.wallet.server.service.CustomerService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.api.model.BasicCustomerInfo;
import com.jinglitong.wallet.api.model.view.CustSelVO;
import com.jinglitong.wallet.api.model.view.PaymentVO;
import com.jinglitong.wallet.api.model.view.WalletVO;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("customer")
@RequestMapping(value = "/console")
public class CustomerController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerAPIService apiService;

    /**
     * 获取用户列表
     * @param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getCustomers.json", method = RequestMethod.POST)
    public Map getCustomers(@RequestBody CustSelVO custSelVO ) {
        if(StringUtils.isEmpty(custSelVO.getAppId())){
            setGlobalAdminAppId(custSelVO);
        }
        HashMap<String, Object> map = customerService.getCusts(custSelVO);
        return JsonUtil.toJsonSuccess("获用户列表成功", map);
    }

    /**
     * 管理总览
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pandec.json", method = RequestMethod.POST)
    public Map pandec() {
        Customer cus = new Customer();
        setGlobalAdminAppId(cus,true);
        HashMap<String, Object> map = customerService.getPandec(cus);
        return JsonUtil.toJsonSuccess("获取管理总览", map);
    }
    /**
     * 获取用户钱包
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customerPropertyList.json", method = RequestMethod.POST)
    public Map pandec(@RequestBody Map<String,String> customerId) {
        if(customerId.get("customerId") == null){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31412);
        }
        Customer cus = new Customer();
        setGlobalAdminAppId(cus);
        cus.setCustId(customerId.get("customerId"));
        HashMap<String, Object> map = customerService.getCustomerPropertyList(cus);
        return JsonUtil.toJsonSuccess("获取用户钱包", map);
    }
    /**
     * 查看钱包资产
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customerProperty.json", method = RequestMethod.POST)
    public Map customerProperty(@RequestBody WalletVO vo) {
        if(StringUtils.isEmpty(vo.getWalletId()) || StringUtils.isEmpty(vo.getCustId()))
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31413);
        return  apiService.getSubChainAmount(vo);

    }
    /**
     * 查看钱包交易记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customerTransactionList.json", method = RequestMethod.POST)
    public Map customerTransactionList(@RequestBody PaymentVO vo) {
        if(StringUtils.isEmpty(vo.getWalletId()) || StringUtils.isEmpty(vo.getCustId()))
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31413);
        Map<String, Object> payments = apiService.getPayments(vo);
        if((payments.get("code")).equals(0)){
            return JsonUtil.toJsonSuccess("查询成功", payments);
        }
        return JsonUtil.toJsonError(ErrorEnum.ERROR_31423);

    }
    
    @ResponseBody
    @RequestMapping(value = "/customerInfo.json", method = RequestMethod.POST)
    public Map getBasicCustomerInfo(@RequestBody Map map) {
    	BasicCustomerInfo custInfo = customerService.getBasicCustomerInfo(map);
        return JsonUtil.toJsonSuccess("获取用户基本信息", custInfo);
    }
    @ResponseBody
    @RequestMapping(value = "/customerUpd.json", method = RequestMethod.POST)
    public Map updCustomerInfo(@RequestBody Map map) {
    	 customerService.updCustomerInfo(map);
        return JsonUtil.toJsonSuccess("修改用户基本信息", null);

    }
    
    
    @ResponseBody
    @RequestMapping(value = "/getInviteList.json", method = RequestMethod.POST)
    public Map getInviteList(@RequestBody CustSelVO custSelVO) {
    	 Map map=customerService.getInvitors(custSelVO);
        return JsonUtil.toJsonSuccess("获取邀请列表成功", map);

    }


}