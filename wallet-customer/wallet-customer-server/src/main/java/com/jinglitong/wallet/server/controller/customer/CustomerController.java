package com.jinglitong.wallet.server.controller.customer;

import com.alibaba.fastjson.JSONObject;
import com.jinglitong.wallet.api.model.view.WalletVO;
import com.jinglitong.wallet.server.common.LoginEnum;
import com.jinglitong.wallet.server.common.shiro.CustomerAuthenticationToken;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.logic.CustomerAndCountry;
import com.jinglitong.wallet.api.model.view.CustRegVO;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.server.service.CustomerService;
import com.jinglitong.wallet.server.service.WalletService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.SessionUtil;
import com.netflix.ribbon.proxy.annotation.Http;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.jinglitong.wallet.server.common.ErrorEnum.*;


@RestController
@RequestMapping("/customer")
public class CustomerController extends CusBaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private CustomerService customerService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerAPIController customerAPIController;

    @Value("${isEnable}")
    private int isEnable;

    @Value("${web_ip}")
    private String webIp;

    /**
     * 
     * 功能说明:用戶注冊
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reg.json", method = RequestMethod.POST)
    public Map doReg(@RequestBody CustRegVO vo) {
    	logger.info("begin reg========================");
        try {
            if(customerService.reg(vo)){
                return JsonUtil.toJsonSuccess("注册成功");
            } else {
                return JsonUtil.toJsonError( ERROR_21001);
            }
        } catch (Exception e) {
        	String message=e.getMessage();
            logger.error("REG CUSTER ERROR:",e);
        	if(ERROR_21003.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_21003);
        	}else if(ERROR_21002.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_21002);
        	}else if(ERROR_21004.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_21004);
        	}else if(ERROR_31458.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_31458);
            }else if(ERROR_31459.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_31459);
            }
            return JsonUtil.toJsonError( ERROR_21001);
        }
    }
    /**
     * 
     * 功能说明:app用户登录
     * @param vo
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public Map doLogin(@RequestBody @Valid CustRegVO vo,BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonUtil.toJsonError(ERROR_37006);
        }
        //先做登出
        if(SecurityUtils.getSubject() != null)
            SecurityUtils.getSubject().logout();

        String account = vo.getAccount();
        CustomerAuthenticationToken token = new CustomerAuthenticationToken(vo.getAccount(), vo.getPasswd(),vo.getAppId(), false);
        token.setLoginType(LoginEnum.CUSTOMER.toString());
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            logger.info("对用户[" + account + "]进行登录验证..验证开始");
            currentUser.login(token);
            logger.info("对用户[" + account + "]进行登录验证..验证通过");
        } catch (UnknownAccountException uae) {
            logger.info("对用户[" + account + "]进行登录验证..验证未通过,未知账户");
            return JsonUtil.toJsonError(ERROR_21011);
        } catch (IncorrectCredentialsException ice) {
            logger.info("对用户[" + account + "]进行登录验证..验证未通过,错误的凭证");
            return JsonUtil.toJsonError(ERROR_21012);
        } catch (LockedAccountException lae) {
            logger.info("对用户[" + account + "]进行登录验证..验证未通过,账户已锁定");
            return JsonUtil.toJsonError(ERROR_21013);
        } catch (ExcessiveAttemptsException eae) {
            logger.info("对用户[" + account + "]进行登录验证..验证未通过,错误次数过多");
            return JsonUtil.toJsonError(ERROR_21014);
        } catch (AuthenticationException ae) {
            logger.info("对用户[" + account + "]进行登录验证..验证未通过,堆栈轨迹如下");
            return JsonUtil.toJsonError(ERROR_21015);
        } catch (Exception e){

            return JsonUtil.toJsonError(ERROR_21015);
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
            Session session = currentUser.getSession();
            session.setAttribute("loginType", LoginEnum.CUSTOMER.toString());
            logger.info("前台用户[" + account + "]登录认证通过");
            //验证是否更换设备
            Customer customer = (Customer)SessionUtil.getUserInfo();
            HashMap<String, Object> map=customerService.checkDevice(customer.getCustId(),vo.getDeviceId(),vo.getAccount(),vo.getAppId());
            /*if(isEnable==0){
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    ResponseEntity<String> postForEntity = restTemplate.postForEntity(webIp+"/logsend/loginSave",vo,String.class);//传递实体
                    String result = postForEntity.getBody();
                    if(result.equals("error")){
                        return JsonUtil.toJsonSuccess("成功",map);
                    }else {
                        return JsonUtil.toJsonSuccess("成功",map);
                    }
                }catch (Exception e) {
//                    e.printStackTrace();
                    return JsonUtil.toJsonSuccess("成功",map);
                }
            }*/
            if(isEnable==1){
                try {
                    restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                    Object json = JSONObject.toJSON(vo);
                    logger.info("登录记录数据收集参数:",json.toString());
                    ResponseEntity<String> postForEntity = restTemplate.postForEntity(webIp+"/logsend/loginSave",json,String.class);//传递实体
                    String result = postForEntity.getBody();
                    if(result.equals("error")){
                        logger.info("登录记录数据收集失败:",result);
                        return JsonUtil.toJsonSuccess("成功",map);
                    }else {
                        logger.info("登录记录数据收集成功:",result);
                        return JsonUtil.toJsonSuccess("成功",map);
                    }
                }catch (Exception e) {
                    logger.info("登录记录数据收集失败:",e.getMessage());
                    e.printStackTrace();
                    return JsonUtil.toJsonSuccess("成功",map);
                }
            }
            return JsonUtil.toJsonSuccess("成功",map);
        } else {
            token.clear();
            return JsonUtil.toJsonError(ERROR_21016);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/accounterr")
    public Map accounterr() {
        return JsonUtil.toJsonError(ERROR_21099);
    }

    /**
     * 退出
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout.json")
    public Map logout() {
        try {
            if(SecurityUtils.getSubject() != null)
                SecurityUtils.getSubject().logout();
        }catch (Exception e){
            logger.error("登出错误",e);
            return JsonUtil.toJsonError(ERROR_21021);
        }
        return JsonUtil.toJsonSuccess("登出成功",null);
    }

    /**
     * 修改密码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/forget.json")
    public Map forget(@RequestBody CustRegVO vo) {
        try {
            if(customerService.forget(vo)){
                return JsonUtil.toJsonSuccess("修改成功",null);
            } else {
                return JsonUtil.toJsonError( ERROR_21031);
            }
        } catch (Exception e) {
        	String message=e.getMessage();
            logger.error("REG CUSTER ERROR:",e);
        	if(ERROR_21003.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_21003);
        	}else if(ERROR_21002.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_21002);
        	}else if(ERROR_21005.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_21005);
        	}
            return JsonUtil.toJsonError( ERROR_21031);
        }
    }
    
    /**
     * 获得当前用户的信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCustCode.json")
    public Map getCustCode() {
    	//Customer cus = customerService.getCustCode();
    	Subject currentUser = SecurityUtils.getSubject();
    	Customer cus= (Customer)currentUser.getPrincipal();
    	CustomerAndCountry custInfo=customerService.getCustInfo(cus.getCustId());
        custInfo.setPassword("Hello world!jinglitong!");
    	return JsonUtil.toJsonSuccess("查询邀请码成功",custInfo);
    }
    
    
    /**
     * 获得当前用户的邀请列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getInviteList.json", method = RequestMethod.POST)
    public Map getInviteList(@RequestBody PageVO vo) {
    	HashMap<String, Object> map = customerService.getInviteList(vo);
    	
    	return JsonUtil.toJsonSuccess("获得邀请列表成功",map);
    }
    
    /**
     * 验证绑定设备验证码
     */
    @ResponseBody
    @RequestMapping(value="checkBindSMS.json",method=RequestMethod.POST)
    public Map checkBingSMS(@RequestBody HashMap<String, Object> map) {
    	try {
            map.put("appId",map.get("appId"));
            customerService.checkBingSMS(map);
		} catch (Exception e) {
			String message=e.getMessage();
			logger.error("REG CUSTER ERROR:",e);
			if(ERROR_21003.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_21003);
        	}else if(ERROR_21002.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ERROR_21002);
        	}
		}
    	return JsonUtil.toJsonSuccess("验证通过");
    	
    }



    /**
     * 触发生成订单
     */
   /* @ResponseBody
    @RequestMapping(value ="/createNewOrder.json", method = RequestMethod.POST)
    public Map createnewOrder(@RequestBody Customer cust) throws Exception {
        // orderService.createNewOrder();
        for (int i = 1; i <= 1000; i++) {
            CustRegVO custRegVO = new CustRegVO();
            String after = "";
            if (i < 10) {
                after = "000" + i;
            } else if (i < 100) {
                after = "00" + i;
            } else if (i < 1000) {
                after = "0" + i;
            }
           String account = "1571387" + i;
            custRegVO.setAccount(account);
            custRegVO.setPasswd("123456");
            custRegVO.setSmsCode("1234");
            custRegVO.setCountryId(1);
            custRegVO.setCountryCode("86");
            custRegVO.setAppId("33a0298a8f7a4ba39b470b37ce7613de");
            Boolean reg = customerService.reg(custRegVO);
            if(reg){
                Customer byAccount = customerService.findByAccount(account, "33a0298a8f7a4ba39b470b37ce7613de");
                WalletVO walletVO = new WalletVO();
                walletVO.setCustId(byAccount.getCustId());
                walletVO.setChainId("test");
                walletVO.setPasswd("123456");
                walletVO.setAppId("33a0298a8f7a4ba39b470b37ce7613de");
                walletVO.setWalletName("fyyTest");
                customerAPIController.createwallet(walletVO);
                logger.info("生成成功account="+account);
            }

        }
        return JsonUtil.toJsonSuccess("true");
    }
*/
}
