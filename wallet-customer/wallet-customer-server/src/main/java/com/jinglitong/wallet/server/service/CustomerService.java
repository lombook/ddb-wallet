package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.view.PropertieVO;
import com.jinglitong.wallet.server.common.CusOriginEnum;
import com.jinglitong.wallet.server.mapper.DeviceInfoMapper;
import com.jinglitong.wallet.server.mapper.IdentityInfoMapper;
import com.jinglitong.wallet.server.mapper.SubChainMapper;
import com.jinglitong.wallet.server.util.PasswordUtil;
import com.jinglitong.wallet.server.mapper.CustomerMapper;
import com.jinglitong.wallet.server.mapper.InviteSystemMapper;
import com.jinglitong.wallet.server.mapper.MainChainMapper;
import com.jinglitong.wallet.server.mapper.WalletMapper;
import com.jinglitong.wallet.api.model.logic.CustomerAndCountry;
import com.jinglitong.wallet.api.model.view.CustRegVO;
import com.jinglitong.wallet.api.model.view.CustSelVO;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import static com.jinglitong.wallet.server.common.ErrorEnum.*;

@Service
@Transactional
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MainChainMapper mainChainMapper;

    @Autowired
    private SubChainMapper subChainMapper;
    @Autowired
    private WalletMapper walletMapper;
    
    @Autowired
    InviteSystemMapper inviteSystemMapper;
    
    @Autowired
    DeviceInfoMapper deviceInfoMapper;

    @Autowired
    private AppWalletService appWalletService;
    
    @Autowired
    IdentityInfoMapper identityInfoMapper;

    @Value("${sms.reg.prefix}")
    private String smsPrefix;


  @Value("${sms.forget.prefix}")
    private String forgetPrefix;



    @Value("${sms.bind.prefix}")
    private String bindPrefix;

    @Value("${aliyun.mail.reg.MRprefix}")
    private String MRprefix;

    @Value("${aliyun.mail.forget.MFprefix}")
    private String MFprefix;


    @Value("${changeDevice.open}")
    private String deviceFlag;

 

    /**
     * 账号和appId 查询账号
     * @param account
     * @param appId
     * @return
     */
    public Customer findByAccount(String account,String appId) {
        Weekend<Customer> weekend = Weekend.of(Customer.class);
        WeekendCriteria<Customer, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(Customer::getAccount,account);
        criteria.andEqualTo(Customer::getAppId,appId);
        return customerMapper.selectOneByExample(weekend);
    }

    @Transactional
    public Boolean reg(CustRegVO vo) throws Exception{
		PropertieVO propertieVO = new PropertieVO();
		propertieVO.setAppId(vo.getAppId());
		List<Map<String, Object>> propertiesMap = appWalletService.getPropertiesMap(propertieVO);
		String codeSwitch = "0";
		codeSwitch = propertiesMap.get(0).get("mustInvite").toString();
		// 是否存在输入的邀请码
		String inviteCode = vo.getInviteCode();
		if (!StringUtils.isEmpty(inviteCode)) {
			Customer customer = customerMapper.getCustByCode(inviteCode,vo.getAppId());
			if (customer == null) {
				log.info(ERROR_31458.getMsg());
				throw new Exception(ERROR_31458.getCode() + "");
			}
		} else if ("1".equals(codeSwitch)) {
			throw new Exception(ERROR_31459.getCode() + "");
		}

		String code = vo.getSmsCode();
		String account = vo.getAccount();
		String redisKey = "";
		String redisCode = "";

		if (account.contains("@")) {
			redisKey = MRprefix + vo.getSmsCode().trim() + account.trim();
			redisCode = (String) redisTemplate.opsForValue().get(redisKey);
		} else {
			redisKey = smsPrefix + vo.getAppId().trim() + "+" + vo.getCountryCode().trim() + vo.getAccount().trim();
			redisCode = (String) redisTemplate.opsForValue().get(redisKey);
		}
		log.info("regkey :"+redisCode);
		if (StringUtils.isEmpty(redisCode)) {
			throw new Exception(ERROR_21003.getCode() + "");
		} else if (!redisCode.equals(code)) {
			throw new Exception(ERROR_21002.getCode() + "");
		} else {
			redisTemplate.delete(redisKey);
		}

		Customer cus = findByAccount(vo.getAccount(), vo.getAppId());
		if (cus != null) {
			throw new Exception(ERROR_21004.getCode() + "");
		}
		String date = DateUtils.getDateTime();
		Customer entity = new Customer();

		entity.setCustId(UuidUtil.getUUID());
		entity.setAccount(vo.getAccount());
		entity.setCreatedTime(date);
		entity.setUpdatedTime(date);
		entity.setState(true);
		String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
		entity.setSalt(salt);
		entity.setCountryId(vo.getCountryId());
		entity.setInviteCode(vo.getInviteCode());
		String password = PasswordUtil.createCustomPwd(vo.getPasswd(), entity.getSalt());
		entity.setPassword(password);
		entity.setInviteCode(vo.getInviteCode());
		entity.setOrigin(CusOriginEnum.FORMAPP.getValue());

		// 生成自己的邀请码
		String selfCode = getInviteCode();
		entity.setSelfInvite(selfCode);

		if (account.contains("@")) {
			entity.setEmail(account);
			entity.setRegWay(1);
		} else {
			entity.setPhone(vo.getAccount());
			entity.setAllPhone("+" + vo.getCountryCode() + vo.getAccount());
			entity.setRegWay(0);
		}
		entity.setAppId(vo.getAppId());
		customerMapper.insertSelective(entity);

		/// 插入邀请码体系表
		if (inviteCode != null && !"".equals(inviteCode)) {

			InviteSystem cs = new InviteSystem();
			cs.setzId(UuidUtil.getUUID());
			cs.setAppId(vo.getAppId());
			// 当前注册用户的id和account
			cs.setCustId(entity.getCustId());
			cs.setCustAccount(vo.getAccount());

			// 创建时间
			cs.setCreatedTime(date);

			// 二级用户的id和account
			Customer customer1 = customerMapper.getCustByCode(inviteCode,vo.getAppId());
			cs.setLevelOneCustId(customer1.getCustId());
			cs.setLevelOneCustAccount(customer1.getAccount());

			// 查询二级用户是否有上级用户
			InviteSystem inviteSel = new InviteSystem();
			inviteSel.setCustId(customer1.getCustId());
			List<InviteSystem> list = inviteSystemMapper.select(inviteSel);
			if (list.size() != 0) {
				InviteSystem cs2 = list.get(0);
				if (cs2 != null) {
					if (cs2.getLevelOneCustId() != null && !"".equals(cs2.getLevelOneCustId())) {
						cs.setLevelTwoCustId(cs2.getLevelOneCustId());
						cs.setLevelTwoCustAccount(cs2.getLevelOneCustAccount());
						if (cs2.getLevelTwoCustId() != null && !"".equals(cs2.getLevelTwoCustId())) {
							cs.setLevelThreeCustId(cs2.getLevelTwoCustId());
							cs.setLevelThreeCustAccount(cs2.getLevelTwoCustAccount());
						}
					}
				}

			}

			inviteSystemMapper.insertSelective(cs);

		}

		return true;
	}

    public Boolean forget(CustRegVO vo) throws Exception{

       // PropertieVO propertieVO = new PropertieVO();
       // propertieVO.setAppId(vo.getAppId());
       // List<Map<String, Object>> propertiesMap = appWalletService.getPropertiesMap(propertieVO);
      // String  forgetPrefix  = propertiesMap.get(0).get("smsValidationCode").toString();

        String code = vo.getSmsCode();
        String account = vo.getAccount();
        String redisKey = "";
        String redisCode = "";
        if(account.contains("@")){
            redisKey = MFprefix+vo.getSmsCode()+account;
        }else{
            redisKey= forgetPrefix + vo.getAppId()+"+"+vo.getCountryCode()+vo.getAccount();
        }
        redisCode = (String)redisTemplate.opsForValue().get(redisKey);
        if(null == redisCode) {
        	throw new Exception(ERROR_21003.getCode()+"");
        }else if(!redisCode.equals(code)){
        	throw new Exception(ERROR_21002.getCode()+"");
        } else {
            redisTemplate.delete(redisKey);
        }
        Customer customer = new Customer();
        if(account.contains("@")){
            Weekend<Customer> weekend = Weekend.of(Customer.class);
            WeekendCriteria<Customer, Object> criteria = weekend.weekendCriteria();
            criteria.andEqualTo(Customer::getEmail,vo.getAccount());
            criteria.andEqualTo(Customer::getAppId,vo.getAppId());
             customer = customerMapper.selectOneByExample(weekend);

        }else{

            Weekend<Customer> weekend = Weekend.of(Customer.class);
            WeekendCriteria<Customer, Object> criteria = weekend.weekendCriteria();
            criteria.andEqualTo(Customer::getPhone,vo.getAccount());
            criteria.andEqualTo(Customer::getAppId,vo.getAppId());
             customer = customerMapper.selectOneByExample(weekend);
        }
        if(null == customer) {
        	throw new Exception(ERROR_21005.getCode()+"");
        }
        String password = PasswordUtil.createCustomPwd(vo.getPasswd(), customer.getSalt());
        Customer upedObj = new Customer();
        upedObj.setId(customer.getId());
        upedObj.setPassword(password);
        customerMapper.updateByPrimaryKeySelective(upedObj);
        return true;
    }

    /**
     * 查看用户列表
     * @param
     * @return
     */
    public HashMap<String, Object> getCustomers(CustSelVO custSelVO) {
        List<CustomerAndCountry> customers = null;
        if (custSelVO.getPage() != null && custSelVO.getRows() != null) {
            PageHelper.startPage(custSelVO.getPage(), custSelVO.getRows());
        }
        customers = customerMapper.getCustomerList(custSelVO);
        PageInfo pageinfo = new PageInfo(customers);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("customers",customers);
        return  map;
    }
    /**
     * 查看用户列表
     * @param
     * @return
     */
    public HashMap<String, Object> getCusts(CustSelVO custSelVO) {
        List<CustomerAndCountry> customers = null;
        if (custSelVO.getPage() != null && custSelVO.getRows() != null) {
            PageHelper.startPage(custSelVO.getPage(), custSelVO.getRows());
        }
        customers = customerMapper.getCusts(custSelVO);
        PageInfo pageinfo = new PageInfo(customers);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("customers",customers);
        return  map;
    }
    


	public HashMap<String,Object> getPandec(Customer cus) {
        Integer customercount = customerMapper.selectCounts(cus);//用户总数量
        Integer mainChaincount = mainChainMapper.selectCounts(cus);//链的总数量
        List<HashMap<String, Object>> currycount = walletMapper.selectChain(cus.getAppId());//链对应的人数
        Weekend<SubChain> weekend = Weekend.of(SubChain.class);
        WeekendCriteria<SubChain, Object> criteria = weekend.weekendCriteria();
        if(!StringUtils.isEmpty(cus.getAppId())){
            criteria.andEqualTo(SubChain::getAppId,cus.getAppId());
        }
        criteria.andEqualTo(SubChain::getState,1);
        List<SubChain> currenyCountAll = subChainMapper.selectByExample(weekend);//所有的链
        HashMap<String, Object> map = new HashMap<>();
        map.put("customercount",customercount);
        map.put("mainChaincount",mainChaincount);
        map.put("customercount",customercount);
        map.put("currycount",currycount);
        map.put("currenyCountAll",currenyCountAll);
        return map;
    }

    public HashMap<String,Object> getCustomerPropertyList(Customer cus) {
        List<Map> userwallets = walletMapper.userwallets(cus);
        HashMap<String, Object> map = new HashMap<>();
        map.put("walletList",userwallets);
        return map;
    }
    
    
    public BasicCustomerInfo getBasicCustomerInfo(Map map){
    	BasicCustomerInfo customerInfo=customerMapper.getBasicCustomerInfo(map);
    	String custId = customerInfo.getCustId();
    	// 查询数据库  是否认证  返回 IdentityInfo 对象  
    	Weekend<IdentityInfo> weekendInfo = Weekend.of(IdentityInfo.class);
        WeekendCriteria<IdentityInfo, Object> criteria = weekendInfo.weekendCriteria();
        criteria.andEqualTo(IdentityInfo::getCustId,custId);
        IdentityInfo info =  identityInfoMapper.selectOneByExample(weekendInfo);
        if(info==null) {
        	customerInfo.setCertState("9");
        }else {
        	customerInfo.setCertState(info.getAuditState());
        }
        
    	return customerInfo;
    }
    public void updCustomerInfo(Map map) {
    	customerMapper.updCustomerInfo(map);
    }
    
    
    public String getInviteCode(){
    	char[] chs = { 'A', 'B', 'C', 'D',  'E', 'F', 'G', 'H', 'I', 'J', 'K','L', 'M', 'N','O' ,'P', 'Q','R','S', 'T', 'U', 'V',
    				'W', 'X', 'Y', 'Z' };
		SecureRandom random = new SecureRandom();
		final char[] value = new char[8];
		for (int i = 0; i < value.length; i++) {
			value[i] = (char) chs[random.nextInt(chs.length)];
		}
		final String code = new String(value);
		//查询数据库
		Customer cus=customerMapper.getTheCustByCode(code);
		
		if(cus==null){
			return code;
		}else{
			return getInviteCode();
		}

    }
    public HashMap<String, Object> getInviteList(PageVO vo){
	    Subject currentUser = SecurityUtils.getSubject();
		Customer cus= (Customer)currentUser.getPrincipal();
		String code= cus.getSelfInvite();
		
		if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
		
		//查询被邀请的用户列表
		List<Customer> list = customerMapper.getInvitedCustomers(code);
		for (int i=0;i<list.size();i++) {
			Customer cust=list.get(i);
			cust.setCreatedTime(cust.getCreatedTime().substring(0, 10));
			if(cust.getCertificateState()) {
				cust.setCstate(1);
			}else {
				cust.setCstate(0);
			}
		}
		PageInfo pageinfo = new PageInfo(list);
        HashMap<String, Object> map = new HashMap<>();
        map.put("count",pageinfo.getTotal());
        map.put("customers",list);
        return  map;
    
    
    }
    
    public HashMap<String, Object> getInvitors(CustSelVO custSelVO){
		String code= custSelVO.getInviteCode();
        String appId = custSelVO.getAppId();
        if (custSelVO.getPage() != null && custSelVO.getRows() != null) {
            PageHelper.startPage(custSelVO.getPage(), custSelVO.getRows());
        }
		
		//查询被邀请的用户列表
		List<CustomerAndCountry> list = customerMapper.getInvitors(custSelVO);
		PageInfo pageinfo = new PageInfo(list);
        HashMap<String, Object> map = new HashMap<>();
        map.put("count",pageinfo.getTotal());
        map.put("customers",list);
        return  map;
    
    
    }
     public CustomerAndCountry getCustInfo(String id) {
    	 CustomerAndCountry custInfo= customerMapper.getCustomerInfo(id);
    	 return custInfo;
     }
     
     public HashMap<String, Object> checkDevice(String custId,String deviceId ,String account,String appId){
    	 HashMap<String, Object> map=new HashMap<>();
    	 String now=DateUtils.getDateTime();
    	 DeviceInfo device=new DeviceInfo();
    	 if(!"1".equals(deviceFlag)) {
    		 map.put("repalceFlag", 0);
    		 map.put("account",null);
    		 return map;
    	 }
    	 
    	 
    	 //验证是否是首次登陆
    	 device.setCustId(custId);
    	 List list =deviceInfoMapper.select(device);
    	 int n=list.size();
    	 if(n==0) {
    		 map.put("repalceFlag", 0);
    		 map.put("account",null);
        	 device.setDeviceId(deviceId);
    		 device.setLoginTime(now);
    		 device.setAppId(appId);
    		 deviceInfoMapper.insert(device);
    		 return map;
    	 }
    	 
    	 //验证是否是邮箱
    	 if(account.contains("@")) {
    		 map.put("repalceFlag", 0);
    		 map.put("account",null);
    		 return map;
    	 }
    	 
    	 //当为手机登陆时，看是否更换设备
    	 device.setDeviceId(deviceId);
    	 List<DeviceInfo> list2 =deviceInfoMapper.select(device);
    	 int n2=list2.size();
    	 if(n2>0) {
    		 DeviceInfo record=new DeviceInfo();
    		 record.setId(list2.get(0).getId());
    		 record.setLoginTime(DateUtils.getDateTime());
    		 deviceInfoMapper.updateByPrimaryKeySelective(record);
    		 map.put("repalceFlag", 0);
    		 map.put("account",null);
    		 return map;
    	 }else {
    		 map.put("repalceFlag", 1);
    		 HashMap<String, Object> accountInfo =new HashMap<>();
    		 accountInfo = deviceInfoMapper.getAccountInfo(account,appId);
    		 map.put("account",accountInfo);
    		 return map;
    	 }
    	 
     }
     
     public void checkBingSMS(HashMap<String ,Object> map) throws Exception {

		 String appId = (String)map.get("appId");
    	 String code = map.get("smsCode").toString();
         String account = map.get("account").toString();
         if(StringUtils.isEmpty(appId)||StringUtils.isEmpty(appId)||StringUtils.isEmpty(appId)){
         	throw new RuntimeException("appId smsCode account 都不能为空");
		 }
         String countryCode = map.get("countryCode").toString();
         String deviceId = map.get("deviceId").toString();
         String redisKey = "";
         String redisCode = "";
         redisKey = bindPrefix + appId+"+"+countryCode+account;
		 redisKey = redisKey.replaceAll(" ","");
		 redisCode = (String)redisTemplate.opsForValue().get(redisKey);
         if(StringUtils.isEmpty(redisCode)) {
         	throw new Exception(ERROR_21003.getCode()+"");
         }else if(!redisCode.equals(code)){
         	throw new Exception(ERROR_21002.getCode()+"");
         } else {
             redisTemplate.delete(redisKey);
         }
         Customer customer = findByAccount(account,appId);
         DeviceInfo device=new DeviceInfo();
         device.setDeviceId(deviceId);
         device.setCustId(customer.getCustId());
         device.setLoginTime(DateUtils.getDateTime());
         device.setAppId(customer.getAppId());
         deviceInfoMapper.insert(device);
         
     }
}
