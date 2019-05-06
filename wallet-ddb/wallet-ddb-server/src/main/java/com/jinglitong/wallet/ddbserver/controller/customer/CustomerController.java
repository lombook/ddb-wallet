package com.jinglitong.wallet.ddbserver.controller.customer;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.logic.CustomerAndCountry;
import com.jinglitong.wallet.api.model.view.CustRegVO;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbShoreholder;
import com.jinglitong.wallet.ddbapi.model.view.DdbCustOrderTreeVO;
import com.jinglitong.wallet.ddbapi.model.view.RankListVo;
import com.jinglitong.wallet.ddbserver.common.ErrorEnum;
import com.jinglitong.wallet.ddbserver.service.CustomerService;
import com.jinglitong.wallet.ddbserver.service.OrderTreeService;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.*;


@RestController
@Slf4j
@RequestMapping("/customer/ddb")
public class CustomerController extends CusBaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderTreeService orderTreeService;



    @Value("${isEnable}")
    private int isEnable;

    @Value("${web_ip}")
    private String webIp;

    /**
     * 功能说明:用戶注冊
     *
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reg.json", method = RequestMethod.POST)
    public Map doReg(@RequestBody CustRegVO vo) {
        try {
            String account = vo.getAccount();
            String pattern = "^1\\d{10}$";
            boolean isPhone = Pattern.matches(pattern, account);
            if(!isPhone){
                return JsonUtil.toJsonError(ERROR_21104);
            }
            String custId = customerService.reg(vo);
            if (!StringUtils.isEmpty(custId)) {
                return JsonUtil.toJsonSuccess("注册成功");
            } else {
                return JsonUtil.toJsonError(ERROR_21001);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            logger.error("REG CUSTER ERROR:", e);
            if (ERROR_21003.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError(ERROR_21003);
            } else if (ERROR_21002.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError(ERROR_21002);
            } else if (ERROR_21004.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError(ERROR_21004);
            } else if (ERROR_31458.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError(ERROR_31458);
            } else if (ERROR_31459.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError(ERROR_31459);
            }
            return JsonUtil.toJsonError(ERROR_21001);
        }
    }


    /**
     * 获得当前用户的信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCustCode.json")
    public Map getCustCode() {
        //Customer cus = customerService.getCustCode();
        Subject currentUser = SecurityUtils.getSubject();
        Customer cus = (Customer) currentUser.getPrincipal();
        CustomerAndCountry custInfo = customerService.getCustInfo(cus.getCustId());
        custInfo.setPassword("Hello world!jinglitong!");
        return JsonUtil.toJsonSuccess("查询邀请码成功", custInfo);
    }

    /**
     * 填写邀请码
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertInviCode.json")
    public Map insertInviCode(@RequestBody Customer customer) {
        Customer cust = (Customer) SessionUtil.getUserInfo();
        if(StringUtils.isEmpty(customer.getInviteCode()) || customer.getInviteCode().toLowerCase().equals(cust.getSelfInvite().toLowerCase())){
            return JsonUtil.toJsonError(ERROR_21115);
        }
        customer.setCustId(cust.getCustId());
        Boolean flag = customerService.insertIviCode(customer);
        if (flag) {
            return JsonUtil.toJsonSuccess("填写邀请码成功", flag);
        } else {
            return JsonUtil.toJsonError(ERROR_21115);
        }

    }

    /**
     * 用户信息同步
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userInfo.json")
    public Map UserInfo(HttpServletRequest request) {
        Customer customer = (Customer) SessionUtil.getUserInfo();
        Map data = customerService.getGameUserInfo(customer);
        return JsonUtil.toJsonSuccess("success", data);
    }


    /**
     * MEI金额
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/MEIReleaseAmount.json")
    public Map MEIFrozenRelease(@RequestBody Map<String, String> walletId) {
        Customer customer = (Customer) SessionUtil.getUserInfo();

        Map data = customerService.getMEIReleaseAmount(customer, walletId.get("walletId"));
        return JsonUtil.toJsonSuccess("success", data);
    }


    /**
     * 获取用户级别信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCustLevelType.json")
    public Map getCustLevelType(HttpServletRequest request) {
        Customer customer = (Customer) SessionUtil.getUserInfo();
        if (customer == null) {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21099, "未登录");
        }
        DdbShoreholder ddbShoreholder = customerService.getShoreholder(customer.getCustId());
        Map data = Maps.newHashMap();
        if (ddbShoreholder == null || ddbShoreholder.getLevelDefine() == null ||"".equals(ddbShoreholder.getLevelDefine())) {
            //查询无结果，返回默认值0
            data.put("levelType", 0);
        } else {
            data.put("levelType", ddbShoreholder.getLevelDefine());
        }
        return JsonUtil.toJsonSuccess("success", data);
    }


	 /**
	  * 
	  * 功能说明:获取财富排行榜
	  * @param vo inteName 积分名(xianbei_all)
	  * @return list
	  */
	@ResponseBody
	@RequestMapping(value ="/getRankList.json" , method = RequestMethod.POST)
	public Map getRankList(@RequestBody RankListVo vo) {
		if(StringUtils.isEmpty(vo.getInteName())) {
			return JsonUtil.toJsonError(ErrorEnum.ERROR_411201);
		}
		return JsonUtil.toJsonSuccess("查询成功", customerService.getRankList(vo));
	}

    /**
     * 用户购买的树
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userOrderTree.json")
    public Map userOrderTree() {
        Customer customer = (Customer) SessionUtil.getUserInfo();
        ArrayList<DdbIntegralWallet> data = new ArrayList<>();
        try{
            data = orderTreeService.userOrderTree(customer);
        }catch ( Exception e ){
            logger.info("服务器异常{}",e);
            return JsonUtil.toJsonError(ERROR_3);
        }
        return JsonUtil.toJsonSuccess("success", data);
    }
    /**
     * 指定产品的树订单
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/userOrderTreeDetail.json")
    public Map userOrderTreeDetail(@RequestBody DdbCustOrderTreeVO orderTree ) {
        Customer customer = (Customer) SessionUtil.getUserInfo();
        PageInfo pageInfo = null;
        if(StringUtils.isEmpty(orderTree.getInteName())){
            JsonUtil.toJsonError(ERROR_2);
        }
        try{
            pageInfo = orderTreeService.userOrderTreeDetail(customer,orderTree);
        }catch ( Exception e ){
            logger.info("服务器异常{}",e);
            return JsonUtil.toJsonError(ERROR_3);
        }
        return JsonUtil.toJsonSuccess("success", pageInfo);
    }
    
    /**
     * 获得用户得到过的升级福利
     *
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getWelfareRecord.json")
    public Map getWelfareRecord() {
        Customer customer = (Customer) SessionUtil.getUserInfo();
        /*Customer customer = new Customer();
        customer.setCustId("12");*/
        if(customer == null || customer.getCustId() == null){
        	return JsonUtil.toJsonError(ERROR_21099);
        }
        List<String> list = customerService.getWelfareRecord(customer.getCustId());      
        return JsonUtil.toJsonSuccess("success", list);
    }

}
