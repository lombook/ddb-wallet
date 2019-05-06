package com.jinglitong.wallet.ddbserver.controller.customer;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.IdentityInfo;
import com.jinglitong.wallet.api.model.view.CertificateVO;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.ddbapi.model.view.CustomerVo;
import com.jinglitong.wallet.ddbserver.common.ErrorEnum;
import com.jinglitong.wallet.ddbserver.common.storage.AliCloudMailService;
import com.jinglitong.wallet.ddbserver.common.storage.AliCloudStorageService;
import com.jinglitong.wallet.ddbserver.service.*;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;
import com.jinglitong.wallet.ddbserver.util.SmsUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jinglitong.wallet.ddbserver.common.CertifacateTypeEnum.IDCARD;
import static com.jinglitong.wallet.ddbserver.common.CertifacateTypeEnum.PASSPORT;

@RestController
@RequestMapping("/customer/common")
public class CommonController extends CusBaseController{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerService customerService;
    @Autowired
    private RequireHistoryService requireHistoryService;
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
    private SmsUtil smsService;

    @Autowired
    private MainChainService mainChainService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private CertificationService certificationService;

    @Autowired
    private AliCloudMailService aliCloudMailService;

    @Autowired
    AliCloudStorageService aliCloudStorageService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppWalletService appWalletService;


    
    @Autowired
    private DdbCustIntegralRecordService ddbCustIntegralRecordService;



    /**
     * 进行身份认证
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/certificate.json" , method = RequestMethod.POST)
    public Map certificate(@RequestBody CertificateVO co) {
    	setGlobalCustomAppId(co);
        try {
            if(certificationService.certificate(co)){
                Subject currentUser = SecurityUtils.getSubject();
                Customer cus= (Customer)currentUser.getPrincipal();
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
        	if(ErrorEnum.ERROR_41008.getCode().toString().equals(code)) {
        		return JsonUtil.toJsonError( ErrorEnum.ERROR_41008);
        	}
            return JsonUtil.toJsonError( ErrorEnum.ERROR_21111);
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/getCerUrl.json",method = RequestMethod.POST)
    public Map getCerUrl(@RequestParam("file") MultipartFile file) {
    	 Map map = new HashMap();
    	logger.debug("开始上传图片");
        try {
        	 String url = aliCloudStorageService.uploadFile(file);
             map.put("url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtil.toJsonSuccess("成功", map);
    }

    /**
     * 检查是否已身份认证
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isOrNotCertif.json")
    public Map isOrNotCertif() {
    	Map<String, String> map= new HashMap<String, String>();
    	IdentityInfo idInfo=certificationService.findCertState();
    	if(idInfo!=null) {
    		map.put("state", idInfo.getAuditState());
    	}else {
    		map.put("state", "9");
    	}
    	return JsonUtil.toJsonSuccess("查询成功", map);
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
    
  /*  @ResponseBody
    @RequestMapping(value = "/getIntegralWallet.json")
    public Map getIntegralWallet() {
    	return ddbCustIntegralWalletService.getIntegralWallet();
    }*/
    
	@ResponseBody
	@RequestMapping(value = "/getIntegralRecord.json")
	public Map getIntegralRecord(@RequestBody PageVO vo) {
		return ddbCustIntegralRecordService.getIntegralRecord(vo);
	}

	@ResponseBody
	@RequestMapping(value = "/modifyNickNameAndPhoto.json")
	public Map modifyNickNameAndPhoto(@RequestParam(value="file",required=false) MultipartFile file,
			@RequestParam(value="nickname",required=false) String nickname) {
		System.out.println(nickname);
		String url = null;// 设置为null，只要上传了图片 一定不为null，后边修改昵称的方法  根据null做判断
		if (file!=null && !file.isEmpty()) {// 如果不为空，则上传图片

			// 首先上传图片 利用原有接口
			logger.debug("开始上传头像图片");
			url = "";
			try {
				if (file.getBytes().length > 1024 * 1024) {
					return JsonUtil.toJsonError(ErrorEnum.ERROR_31443);
				}
				String fileName = file.getOriginalFilename();
				if (!fileName.endsWith(".png") && !fileName.endsWith(".PNG") && !fileName.endsWith(".jpg")
						&& !fileName.endsWith(".JPG")) {
					return JsonUtil.toJsonError(ErrorEnum.ERROR_31444);
				}
//				url = aliCloudStorageService.uploadFile(file);
				url = aliCloudStorageService.uploadFaviconFile(file);
				
				logger.debug("上传图片URL" + url);
				// map.put("url", url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// map.put("ok", file.getName());
			logger.debug("头像图片上传成功");
		}

		// 上传完之后 保存 用户信息 邮箱账号 可以修改
		customerService.modifyNickNameAndPhoto(nickname, url);
		return JsonUtil.toJsonSuccess("成功", null);
	}

	@ResponseBody
	@RequestMapping(value = "/getNickNameAndPhoto.json")
	public Map getNickNameAndPhoto() {

		CustomerVo cusvo = customerService.getNickNameAndPhoto();

		return JsonUtil.toJsonSuccess("成功", cusvo);
	}

}
