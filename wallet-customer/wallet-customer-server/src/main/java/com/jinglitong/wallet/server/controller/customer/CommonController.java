package com.jinglitong.wallet.server.controller.customer;

import com.alibaba.fastjson.JSONObject;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.logic.LSmsVO;
import com.jinglitong.wallet.api.model.view.*;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.SMSEnum;
import com.jinglitong.wallet.server.common.storage.AliCloudMailService;
import com.jinglitong.wallet.server.common.storage.AliCloudStorageService;
import com.jinglitong.wallet.server.service.*;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.SessionUtil;
import com.jinglitong.wallet.server.util.SmsUtil;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import static com.jinglitong.wallet.server.common.CertifacateTypeEnum.IDCARD;
import static com.jinglitong.wallet.server.common.CertifacateTypeEnum.PASSPORT;
import static com.jinglitong.wallet.server.common.FeedbackTypeEnum.*;

@RestController
@RequestMapping("/customer/common")
public class CommonController extends CusBaseController{

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${sms.codelength}")
    private Integer codeLength;

    @Value("${sms.mod.prefix}")
    private String smsModPrefix;

    @Value("${sms.bind.prefix}")
    private String smsBindPrefix;

    @Value("${sms.reg.prefix}")
    private String smsRegPrefix;

    @Value("${sms.forget.prefix}")
    private String smsForgtPrefix;

    @Value("${sms.export.prefix}")
    private String smsExportPrefix;

    @Value("${sms.time.valid}")
    private Long smsTime;

    @Value("${aliyun.mail.MCodeLength}")
    private Integer MailCodeLength;

    @Value("${aliyun.mail.reg.MRprefix}")
    private String MRprefix;

    @Value("${aliyun.mail.forget.MFprefix}")
    private String MFprefix;

    @Value("${aliyun.mail.MexpireTime}")
    private Long MexpireTime;

    //数据收集
    @Value("${isEnable}")
    private int isEnable;

    @Value("${web_ip}")
    private String webIp;

    @Autowired
    private CountryService countryService;

    @Autowired
    private SmsUtil smsService;

    @Autowired
    private MainChainService mainChainService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PropertieTabService propertieTabService;

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private AliCloudMailService aliCloudMailService;
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    AliCloudStorageService aliCloudStorageService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppWalletService appWalletService;
    
    @Autowired
    private CustomerBankService  customerBankService;


    @ResponseBody
    @RequestMapping(value = "/countries.json", method = RequestMethod.POST)
    public Map getountries() {
        return countryService.getAll();
    }
    /**
     *
     * 功能说明:发送短信验证码
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sms.json", method = RequestMethod.POST)
    public Map sms(@RequestBody SmsVO vo) {
        PropertieVO propertieVO = new PropertieVO();
        propertieVO.setAppId(vo.getAppId());
        List<Map<String, Object>> propertiesMap = appWalletService.getPropertiesMap(propertieVO);
        String  mailTitle = propertiesMap.get(0).get("mailTitle").toString();
        if (StringUtils.isEmpty(vo.getType())){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_26003);
        }
        String param = vo.getPhone();
        boolean contains = param.contains("@");
        if(contains){
            String reg = "\\w+@(\\w+\\.){1,3}\\w+";
            Pattern pattern = Pattern.compile(reg);
            boolean flag = false;
            if (param != null) {
                Matcher matcher = pattern.matcher(param);
                flag = matcher.matches();
                if(!flag){
                    return JsonUtil.toJsonError(ErrorEnum.ERROR_26001);
                }
            }
        }
        if(contains){
            String mailString ="";
            if (SMSEnum.FORGET.toString().equals(vo.getType())){
                mailString = MFprefix ;
            }

            if (SMSEnum.REGIST.toString().equals(vo.getType())){
                mailString = MRprefix;
            }
            String  toAddress = param;
            String subject = mailTitle;
            String code = AliCloudMailService.createCode(MailCodeLength);
             mailString += code + param;
            String body = "您好：您的注册验证码为:"+code+"。有效期15分钟，不能告诉别人哦！";
            Boolean aBoolean = aliCloudMailService.sendMail(toAddress, subject, body);
            if(!aBoolean){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_26002);
            }else {
                try {
                    redisTemplate.opsForValue().set(mailString, code, MexpireTime, TimeUnit.MINUTES);
                } catch (Exception e){
                    logger.error("redis error : ",e);
                    return JsonUtil.toJsonError(ErrorEnum.ERROR_26002);
                }
                return JsonUtil.toJsonSuccess("发送成功",null);
            }
        }else{
            String prefixStr = "";
            if (SMSEnum.FORGET.toString().equals(vo.getType())){
                prefixStr = smsForgtPrefix + vo.getAppId();
            }

            if (SMSEnum.REGIST.toString().equals(vo.getType())){
                prefixStr = smsRegPrefix + vo.getAppId();
            }

            if (SMSEnum.MODPASS.toString().equals(vo.getType())){
                prefixStr = smsModPrefix + vo.getAppId();
            }
            if (SMSEnum.EXPORT.toString().equals(vo.getType())){
                prefixStr = smsExportPrefix + vo.getAppId();
            }
            if (SMSEnum.BINDDEVICE.toString().equals(vo.getType())){
                prefixStr = smsBindPrefix + vo.getAppId();
            }

            String smsCode = SmsUtil.createCode(codeLength);
            LSmsVO sendVO = new LSmsVO();
            sendVO.setCode(smsCode);
            String allPhone = "+"+vo.getCountryCode()+vo.getPhone();
            sendVO.setPhone(allPhone);
            sendVO.setType(vo.getType());
            sendVO.setAppId(vo.getAppId());
            boolean result = smsService.sendSms(sendVO);
            if (result) {
                try {
                    String redisKey = prefixStr.trim() + allPhone.trim();
                    logger.info("短信验证码key："+ redisKey);
                    redisTemplate.opsForValue().set(redisKey, smsCode, smsTime, TimeUnit.MINUTES);
                } catch (Exception e){
                    logger.error("redis error : ",e);
                    return JsonUtil.toJsonError(ErrorEnum.ERROR_26002);
                }
                return JsonUtil.toJsonSuccess("发送成功",null);
            } else {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_26002);
            }
        }
    }
    /**
     *
     * 功能说明:获取所有的链
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getallchain.json", method = RequestMethod.POST)
    public Map getAllChina (@RequestBody BalanceVO vo) {
    	setGlobalCustomAppId(vo);
        return JsonUtil.toJsonSuccess("查询成功", mainChainService.getAllChain(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/propertie.json", method = RequestMethod.POST)
    public Map getAllChina (@RequestBody PropertieVO vo) {
        if(StringUtils.isEmpty(vo.getAppId())){
            setGlobalCustomAppId(vo);
        }
        if ("inviteCode".equals(vo.getgName())) {
            return JsonUtil.toJsonSuccess("邀请码查询成功", appWalletService.getPropertiesMap(vo));
        }
        if ("licai".equals(vo.getgName())) {
            return JsonUtil.toJsonSuccess("理财查询成功", appWalletService.getPropMap(vo));
        }
        if ("downloadpage".equals(vo.getgName())) {
            return JsonUtil.toJsonSuccess("下载页查询成功", appWalletService.getDownLoadMap(vo));
        }
        if ("about".equals(vo.getgName())) {
            return JsonUtil.toJsonSuccess("关于获取成功", propertieTabService.getPropertiesMap(vo));
        }
        if("protocol".equals(vo.getgName())){
            return JsonUtil.toJsonSuccess("协议查询成功", appWalletService.getProtocolMap(vo));
        }
        return null;
    }

    /*@ResponseBody
    @RequestMapping(value = "/propertie.json", method = RequestMethod.POST)
    public Map getAllChina (@RequestBody PropertieVO vo) {
        return JsonUtil.toJsonSuccess("查询成功", propertieTabService.getPropertiesMap(vo));

    }*/


    /**
     * 进行身份认证
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/certificate.json")
    public Map certificate(@RequestBody CertificateVO co) {
    	setGlobalCustomAppId(co);
        try {
            if(certificationService.certificate(co)){
                if(isEnable==1){
                    try {
                        Customer customer = (Customer) SessionUtil.getUserInfo();
                        co.setAccount(customer.getAccount());
                        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                        Object json = JSONObject.toJSON(co);
                        logger.info("身份验证数据收集参数:",json.toString());
                        ResponseEntity<String> postForEntity = restTemplate.postForEntity(webIp+"/authentification/certificateSave",json,String.class);//传递实体
                        String result = postForEntity.getBody();
                        if(result.equals("error")){
                            logger.info("身份验证数据收集失败:",result);
                            return JsonUtil.toJsonSuccess("认证成功",null);
                        }else {
                            logger.info("身份验证数据收集成功:",result);
                            return JsonUtil.toJsonSuccess("认证成功",null);
                        }
                    }catch (Exception e) {
                        logger.info("身份验证数据收集失败",e.getMessage());
                        e.printStackTrace();
                        return JsonUtil.toJsonSuccess("认证成功",null);
                    }
                }
                return JsonUtil.toJsonSuccess("认证成功",null);
            } else {
                return JsonUtil.toJsonError( ErrorEnum.ERROR_21111);
            }
        } catch (Exception e) {
        	logger.error("CERTIFICATE:",e);
        	String code=e.getMessage();
        	if(ErrorEnum.ERROR_21112.getCode().toString().equals(code)) {
        		return JsonUtil.toJsonError( ErrorEnum.ERROR_21112);
        	}
        	if(ErrorEnum.ERROR_21113.getCode().toString().equals(code)) {
        		return JsonUtil.toJsonError( ErrorEnum.ERROR_21113);
        	}
            return JsonUtil.toJsonError( ErrorEnum.ERROR_21111);
        }
    }

    /**
     * 检查是否已身份认证
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isOrNotCertif.json")
    public Map isOrNotCertif() {
    	Map<String, String> map= new HashMap<String, String>();

        if(certificationService.isOrNotCertif()){
        	map.put("code", "0");
    		map.put("msg", "已进行实名认证");
    		return map;

        } else {
        	map.put("code", "1");
    		map.put("msg", "未实名认证");
    		return map;
        }

    }

    /**
     * 查询身份认证
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findCertif.json")
    public Map findCertif() {
    	return JsonUtil.toJsonSuccess("查询成功", certificationService.findCertif());
    }

    /**
     * 查询省份列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/provinces.json", method = RequestMethod.POST)
    public Map getprovinces() {
        return JsonUtil.toJsonSuccess("查询成功", certificationService.getprovinces());

    }

    /**
     * 查询城市列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/cities.json", method = RequestMethod.POST)
    public Map getcities(@RequestBody Map<String,String> map ) {
        return JsonUtil.toJsonSuccess("查询成功", certificationService.getcities(map.get("provinceId")));

    }

    /**
     * 查询证件类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/certificateType.json", method = RequestMethod.POST)
    public Map getCertificateType() {
    	Map map=new HashMap<String,Object>();
    	map.put(PASSPORT.getCode(), PASSPORT.getName());
    	map.put(IDCARD.getCode(), IDCARD.getName());

        return JsonUtil.toJsonSuccess("查询成功", map);

    }

    /**
     * 查询反馈类型
     * create by zhu
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/feedbackType.json", method = RequestMethod.POST)
    public Map getFeedbackType() {
    	List list = new ArrayList<>();

    	Map map1=new HashMap<String,Object>();
    	map1.put("code",OPION_FEEDBACK.getCode());
    	map1.put("name", OPION_FEEDBACK.getName());
    	list.add(map1);

    	Map map2=new HashMap<String,Object>();
    	map2.put("code",PRODUCT_SUGGEST.getCode());
    	map2.put("name", PRODUCT_SUGGEST.getName());
    	list.add(map2);

    	Map map3=new HashMap<String,Object>();
    	map3.put("code",FEATURE_APPLY.getCode());
    	map3.put("name", FEATURE_APPLY.getName());
    	list.add(map3);

    	Map map4=new HashMap<String,Object>();
    	map4.put("code",OTHER.getCode());
    	map4.put("name", OTHER.getName());
    	list.add(map4);

        return JsonUtil.toJsonSuccess("查询成功", list);

    }

    /**
     * 反馈录入
     * create by zhu
     */
    @ResponseBody
    @RequestMapping(value="/addFeedback.json",method=RequestMethod.POST)
    public Map addFeedback( @RequestBody FeedbackVo vo) {
    	//setGlobalCustomAppId(vo);
        setGlobalFromAppId(vo);
    	Customer cust =(Customer) SessionUtil.getUserInfo();
    	vo.setCustId(cust.getCustId());
    	int  i=feedbackService.addFeedback(vo);
    	if(i==1) {
    		return JsonUtil.toJsonSuccess("提交成功");
    	}else {
    		return JsonUtil.toJsonError(ErrorEnum.ERROR_31464.getCode(), "提交失败");
    	}

    }
    
    /**
     * 获得未读消息数量，包括未读公告和未读客服回复
     * create by peng
     */
    @ResponseBody
    @RequestMapping(value="/getUnreadNum.json",method=RequestMethod.POST)
    public Map<?, ?> getUnreadNum() {
    	Customer cust =(Customer) SessionUtil.getUserInfo();   
    	/*Customer cust = new Customer();
    	cust.setCustId("73a22e5d6467483c9e2822fa68b71e57");
    	cust.setAppId("33a0298a8f7a4ba39b470b37ce7613de");*/
    	if(cust == null){
    		return JsonUtil.toJsonError(ErrorEnum.ERROR_31421);
    	}
    	Map<String,String> map = feedbackService.getUnreadNum(cust.getCustId(),cust.getAppId());
    	if(map.size() == 0){
    		return JsonUtil.toJsonError(ErrorEnum.ERROR_31421);
    	}
    	return JsonUtil.toJsonSuccess("成功", map);
    }
    
    /**
     * 根据用户和类型获得信息列表，1：公告 2：反馈
     * create by peng
     */
    @ResponseBody
    @RequestMapping(value="/getInforList.json",method=RequestMethod.POST)
    public Map<?, ?> getInforList(@RequestBody Map<String,String> map) {
    	Customer cust =(Customer) SessionUtil.getUserInfo();
    	if(cust == null || cust.getCustId() == null){
    		return JsonUtil.toJsonError(ErrorEnum.ERROR_31421);
    	}    	
    	if(map == null || map.get("type") == null ||(!(map.get("type").equals("1") || map.get("type").equals("2")))){
    		return JsonUtil.toJsonError(ErrorEnum.ERROR_26001);
    	}
    	map.put("cust_id", cust.getCustId());
    	map.put("app_id", cust.getAppId());
    	if(map.get("type").equals("2")){
    		return JsonUtil.toJsonSuccess("成功", feedbackService.getAlreadyRepliedFeedbackList(map));
    	}
    	return JsonUtil.toJsonSuccess("成功", feedbackService.getAllPushNoticeList(map));
    }
    
    /**
     * 获得反馈详情
     * create by peng
     */
    @ResponseBody
    @RequestMapping(value="/getFeedbackDetail.json",method=RequestMethod.POST)
    public Map<?, ?> getFeedbackDetail(@RequestBody FeedbackVo vo) {
    	Customer cust =(Customer) SessionUtil.getUserInfo();
    	if(cust == null || cust.getCustId() == null){
    		return JsonUtil.toJsonError(ErrorEnum.ERROR_31421);
    	}
    	if(vo == null || vo.getFeedbackid() == null ){
    		return JsonUtil.toJsonError(ErrorEnum.ERROR_26001);
    	}
    	return JsonUtil.toJsonSuccess("成功", feedbackService.getFeedbackDetail(cust.getCustId(),vo.getFeedbackid(),cust.getAppId()));  
    }

    @ResponseBody
    @RequestMapping(value = "/uploadOpionImg.json")
    public Map upload(@RequestParam("file") MultipartFile file) {
        logger.debug("开始上传图片");
        Map map = new HashMap();
        try {
            String url = aliCloudStorageService.uploadFile(file);
            logger.debug("上传图片URL" + url);
            map.put("url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("ok", file.getName());
        logger.debug("图片上传成功");
        return JsonUtil.toJsonSuccess("成功", map);
    }
    

    /** 查询用户银行卡信息
    * @return
    */
   @ResponseBody
   @RequestMapping(value = "/getCustomerBankInfo.json", method = RequestMethod.POST)
   public Map getCustomerBankInfo() {
       return JsonUtil.toJsonSuccess("查询成功", customerBankService.getCustomerBankInfo());

   }
   
   /** 解绑用户银行卡
    * @return
    */
   @ResponseBody
   @RequestMapping(value = "/deleteCustomerBankInfoByZid.json", method = RequestMethod.POST)
   public Map deleteCustomerBankInfoByZid(@RequestBody CustomerBankVo vo) {
	   int num = customerBankService.deleteCustomerBankInfoByZid(vo.getZid());
	   if(num == 1) {
		   return JsonUtil.toJsonSuccess("解绑成功", null);
	   }else {
		   return JsonUtil.toJsonError(ErrorEnum.ERROR_41110.getCode(), "解绑失败");
	   }

   }
   
   /** 获得所有的银行卡
    * @return
    */
   @ResponseBody
   @RequestMapping(value = "/getAllBankInfo.json", method = RequestMethod.POST)
   public Map getAllBankInfo() {
       return JsonUtil.toJsonSuccess("查询成功", customerBankService.getAllBankInfo());

   }
   
   /** 绑定用户银行卡
    * @return
    */
   @ResponseBody
   @RequestMapping(value = "/addCustomerBankInfo.json", method = RequestMethod.POST)
   public Map addCustomerBankInfo(@RequestBody CustomerAddBankVo vo) {
	   Integer num = null;
	try {
		num = customerBankService.addCustomerBankInfo(vo);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		String code=e.getMessage();
		if(ErrorEnum.ERROR_41112.getCode().toString().equals(code)) {
    		return JsonUtil.toJsonError( ErrorEnum.ERROR_41112);
    	}
		if(ErrorEnum.ERROR_41113.getCode().toString().equals(code)) {
    		return JsonUtil.toJsonError( ErrorEnum.ERROR_41113);
    	}
		if(ErrorEnum.ERROR_41114.getCode().toString().equals(code)) {
    		return JsonUtil.toJsonError( ErrorEnum.ERROR_41114);
    	}
		if(ErrorEnum.ERROR_41111.getCode().toString().equals(code)) {
    		return JsonUtil.toJsonError( ErrorEnum.ERROR_41111);
    	}
	}
	   if(num != null && num == 1) {
		   return JsonUtil.toJsonSuccess("绑定成功", null);
	   }else {
		   return JsonUtil.toJsonError(ErrorEnum.ERROR_41111.getCode(), "银行卡添加失败");
	   }

   }
}
