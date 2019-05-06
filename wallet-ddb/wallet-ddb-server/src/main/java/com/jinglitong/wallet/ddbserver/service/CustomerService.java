package com.jinglitong.wallet.ddbserver.service;

import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_21002;
import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_21003;
import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_21004;
import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_21005;
import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_31458;
import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_31459;
import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_41116;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.BasicCustomerInfo;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.DeviceInfo;
import com.jinglitong.wallet.api.model.IdentityInfo;
import com.jinglitong.wallet.api.model.InviteSystem;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.api.model.logic.CustomerAndCountry;
import com.jinglitong.wallet.api.model.view.CustRegVO;
import com.jinglitong.wallet.api.model.view.CustSelVO;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.api.model.view.PropertieVO;
import com.jinglitong.wallet.api.model.view.WalletVO;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.ddbapi.model.DdbShoreholder;
import com.jinglitong.wallet.ddbapi.model.view.CustomerVo;
import com.jinglitong.wallet.ddbapi.model.view.RankListVo;
import com.jinglitong.wallet.ddbserver.common.CusOriginEnum;
import com.jinglitong.wallet.ddbserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWalletMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbMqMessageRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbShoreholderMapper;
import com.jinglitong.wallet.ddbserver.mapper.DeviceInfoMapper;
import com.jinglitong.wallet.ddbserver.mapper.IdentityInfoMapper;
import com.jinglitong.wallet.ddbserver.mapper.InviteSystemMapper;
import com.jinglitong.wallet.ddbserver.mapper.MainChainMapper;
import com.jinglitong.wallet.ddbserver.mapper.SubChainMapper;
import com.jinglitong.wallet.ddbserver.mapper.WalletMapper;
import com.jinglitong.wallet.ddbserver.util.AliMQServiceUtil;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.MD5Utils;
import com.jinglitong.wallet.ddbserver.util.PasswordUtil;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Transactional
@Slf4j
public class CustomerService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
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
    private CustomerAPIService apiService;

	@Autowired
	private DdbShoreholderMapper ddbShoreholderMapper;

	@Autowired
	private DdbIntegralWalletMapper ddbIntegralWalletMapper;

	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;
	
	@Autowired
	private DdbMqMessageRecordMapper ddbMqMessageRecordMapper;
	
	@Autowired
    private IdentityInfoMapper identityInfoMapper;
	
	@Value("${ali.mq.topic}")
    private String topic;
    
    @Value("${ali.mq.send.tag}")
    private String tag;
    
//    @Value("game.updateUserInfo.url")
//    private String gameUserInfoUrl;// 个人信息  向游戏同步   请不要删除！！！
    
//    @Value("${game.userAudit.url}")
//    private String gameUserAuditUrl;
    
    @Value("${ali.mq.send.group}")
    private String producerId;

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

	@Value("${game_ip}")
    private String game_ip;



	@Value("${MEI_chain_id}")
	private String MEIChainId;

	@Value("${MEI_coin_id}")
	private String MEICoinId;


	@Value("${bf_caizai}")
	private String bfChangJing;
 

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
    public String reg(CustRegVO vo) throws Exception{
		PropertieVO propertieVO = new PropertieVO();
		propertieVO.setAppId(vo.getAppId());
		List<Map<String, Object>> propertiesMap = appWalletService.getPropertiesMap(propertieVO);
		String codeSwitch = "0";
		codeSwitch = propertiesMap.get(0).get("mustInvite").toString();
		int treeLevel = 1;
		// 是否存在输入的邀请码
		String inviteCode = vo.getInviteCode();
		if (!StringUtils.isEmpty(inviteCode)) {
			Customer customer = customerMapper.getCustByCode(inviteCode,vo.getAppId());
			if (customer == null) {
				throw new Exception(ERROR_31458.getCode() + "");
			}

			treeLevel =customer.getTreeLevel() + 1;



			
		} else if ("1".equals(codeSwitch)) {
			throw new Exception(ERROR_31459.getCode() + "");
		}

		String code = vo.getSmsCode();
		String account = vo.getAccount();
		String redisKey = "";
		String redisCode = "";

		if (account.contains("@")) {
			redisKey = MRprefix + vo.getSmsCode() + account;
			redisCode = (String) redisTemplate.opsForValue().get(redisKey);
		} else {
			redisKey = smsPrefix + vo.getAppId() + "+" + vo.getCountryCode() + vo.getAccount();
			redisCode = (String) redisTemplate.opsForValue().get(redisKey);
		}

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

		entity.setTreeLevel(treeLevel);


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

		//查询总账表
		List<DdbIntegralAccount> DdbIntegralAccountList = ddbIntegralAccountMapper.selectAllExpireParentIdAndState(1);
		String dateTime = DateUtils.getDateTime();
		//生成钱包
		for (DdbIntegralAccount acc: DdbIntegralAccountList) {
			DdbIntegralWallet ddbIntegralWallet = new DdbIntegralWallet();
			ddbIntegralWallet.setZid(UuidUtil.getUUID());
			ddbIntegralWallet.setAmount(0L);
			ddbIntegralWallet.setCreateTime(dateTime);
			ddbIntegralWallet.setCustId(entity.getCustId());
			ddbIntegralWallet.setInteName(acc.getRealName());
			ddbIntegralWallet.setInteCname(acc.getIntegCname());
			ddbIntegralWallet.setUpdateTime(dateTime);
			ddbIntegralWalletMapper.insert(ddbIntegralWallet);
		}
		//生成股东记录
		DdbShoreholder ddbShoreholder = new DdbShoreholder();
		ddbShoreholder.setZId(UuidUtil.getUUID());
		ddbShoreholder.setBaseStone(0);
		ddbShoreholder.setCreateTime(dateTime);
		ddbShoreholder.setCustId(entity.getCustId());
		ddbShoreholder.setLevelDefine("");
		ddbShoreholder.setSeedAmount(0);
		ddbShoreholder.setSumAmount(0);
		ddbShoreholder.setTeamSumAmount(0L);
		ddbShoreholder.setUpdateTime(dateTime);
		ddbShoreholderMapper.insert(ddbShoreholder);

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


		return entity.getCustId();
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
    	 String code = map.get("smsCode").toString();
         String account = map.get("account").toString();
         String countryCode = map.get("countryCode").toString();
         String deviceId = map.get("deviceId").toString();
         String redisKey = "";
         String redisCode = "";
         redisKey = bindPrefix + map.get("appId")+"+"+countryCode+account;
         redisCode = (String)redisTemplate.opsForValue().get(redisKey);

         if(StringUtils.isEmpty(redisCode)) {
         	throw new Exception(ERROR_21003.getCode()+"");
         }else if(!redisCode.equals(code)){
         	throw new Exception(ERROR_21002.getCode()+"");
         } else {
             redisTemplate.delete(redisKey);
         }
         Customer customer =new Customer();
          customer = findByAccount(account,customer.getAppId());
         DeviceInfo device=new DeviceInfo();
         device.setDeviceId(deviceId);
         device.setCustId(customer.getCustId());
         device.setLoginTime(DateUtils.getDateTime());
         device.setAppId(customer.getAppId());
         deviceInfoMapper.insert(device);
         
     }

	public Boolean insertIviCode(Customer customer)  {
    	Customer cust = customerMapper.selectBySelCode(customer);
    	if(cust != null){
    		Integer flag = customerMapper.updateByCustId(customer);
			Customer cu = customerMapper.selectByCustId(customer.getCustId());
			String inviteCode = cu.getInviteCode();
			if (inviteCode != null && !"".equals(inviteCode)) {

				InviteSystem cs = new InviteSystem();
				cs.setzId(UuidUtil.getUUID());
				cs.setAppId(cu.getAppId());
				// 当前注册用户的id和account
				cs.setCustId(cu.getCustId());
				cs.setCustAccount(cu.getAccount());

				// 创建时间
				cs.setCreatedTime(DateUtils.getDateTime());

				// 二级用户的id和account
				Customer customer1 = customerMapper.getCustByCode(inviteCode,cu.getAppId());
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
		}else {
    		return false;
		}
	}

	public Map getGameUserInfo(Customer customer) {
        logger.info("游戏获取用户信息"+customer.getCustId());
    	Map<String,Object> map = new HashMap<>();
    	if(customer.getCertificateState()) {
    		
    		map.put("isValid",1);
    		// 查询用户认证表  得到身份信息，获取年龄
    		String userAge = queryIndentyInfoForAge(customer);
    		map.put("userAge", userAge);
    	}
    	else {
    		map.put("isValid",0);
    		map.put("userAge", "");
    	}
    	map.put("userId",customer.getCustId());
    	map.put("account",customer.getAccount());
		//DdbCustIntegralWallet ddbCustIntegralWallet =ddbCustIntegralWalletMapper.selectByCustId(customer.getCustId());
		DdbIntegralAccount ddbIntegralAccount = ddbIntegralAccountMapper.selectByRealName(bfChangJing);
		DdbIntegralWallet ddbCustIntegralWallet = ddbIntegralWalletMapper.selectByRealNameAdnCustId(ddbIntegralAccount.getIntegName(),customer.getCustId());
		if(ddbCustIntegralWallet == null){
			map.put("baoCoin",0);
		}else {
			map.put("baoCoin",(float)ddbCustIntegralWallet.getAmount()/100);
		}
    	//宝分查询
        Wallet wallet = walletMapper.selectByCustIdAndAppIdAndChainId(customer.getCustId(),customer.getAppId(),MEIChainId);
		SubChain MEIsubChain = subChainMapper.selectByCoinId(MEICoinId);


		if(wallet==null || MEIsubChain == null){
            map.put("meiCoin",0);
        }else{
            WalletVO  vo = new WalletVO();
            vo.setCustId(customer.getCustId());
            vo.setWalletId(wallet.getWalletId());
            vo.setAppId(wallet.getAppId());
            Map<String, Object> stringObjectMap = apiService.chaindetailBao(vo);
            if(stringObjectMap.get(MEIsubChain.getCurrency()) == null){
                map.put("meiCoin",0);
            }else {
                map.put("meiCoin",stringObjectMap.get(MEIsubChain.getCurrency()));
            }
            logger.info("宝分信息"+stringObjectMap.toString());
        }

        return map;
	}

	private String queryIndentyInfoForAge(Customer customer) {
		// TODO Auto-generated method stub
		Integer ageByIDNumber = null;
		String userAge = "";
		// 根据用户ID 已认证 查询 认证信息
		IdentityInfo record = new IdentityInfo();
		record.setCustId(customer.getCustId());
		record = identityInfoMapper.selectOne(record);
		if(record == null || record.getIdentityNo() == null || "".equals(record.getIdentityNo())) {
			return userAge;
		}
		// 判断证件类型
		if("00".equals(record.getIdentityType())) {//身份证
			// 身份证
			String idNumber = record.getIdentityNo();
			// 获取年龄
			ageByIDNumber = getAgeByIDNumber(idNumber);
		}else {//其他,  设置有效年龄，返回
			ageByIDNumber = 50;
			return ageByIDNumber.toString();
		}
		
		// 改为只要是合法年龄 大于0岁   均返回年龄值    2018.10.26
		if(ageByIDNumber < 0) {
			userAge = "";
		}else {
			userAge = ageByIDNumber.toString();
		}

		return userAge;
	}

	public int getAgeByIDNumber(String idNumber) {
		String dateStr = "";
		if (idNumber.length() == 15) {
			dateStr = "19" + idNumber.substring(6, 12);
		} else if (idNumber.length() == 18) {
			dateStr = idNumber.substring(6, 14);
		}else {
			return -1;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date birthday = null;
		if(dateStr.indexOf(" ") > -1) {//存在空格  身份证年月日不合法   
			return -1;
		}
		
		// 在parse方法中已经做了 格式(非数字)认证，不必再做
		try {
			birthday = simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			// 有问题直接把年龄置为-1   最后判断年龄小于1大于70，年龄均为""
			return -1;
		}
		return getAgeByDate(birthday);
	}

	public int getAgeByDate(Date birthday) {
		Calendar calendar = Calendar.getInstance();

		int yearNow = calendar.get(Calendar.YEAR);
		int monthNow = calendar.get(Calendar.MONTH);
		int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

		calendar.setTime(birthday);

		int yearBirthday = calendar.get(Calendar.YEAR);
		int monthBirthday = calendar.get(Calendar.MONTH);
		int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirthday;

		if (monthNow <= monthBirthday && dayOfMonthNow < dayOfMonthBirthday || monthNow < monthBirthday) {
			age--;
		}

		return age;
	}

	public Map getMEIReleaseAmount(Customer customer,String walletId) {
		HashMap<Object, Object> map = new HashMap<>();
       /* DdbCustIntegralWallet ddbCustIntegralWallet = ddbCustIntegralWalletMapper.selectByCustId(customer.getCustId());
        BigDecimal amount = new BigDecimal(ddbCustIntegralWallet.getBaofen() + ddbCustIntegralWallet.getFreezBaofen()).divide(new BigDecimal("100"));
        map.put("amount",amount);
		map.put("currency","");*/
		return  map;
        /*Wallet wallet = walletMapper.selectByWalletId(walletId);
        if(wallet == null){
			map.put("amount",0);
			map.put("currency",MEICoinCurrency);
			return  map;
		}
        if(!wallet.getChainId().equals(MEIChainId)){
            map.put("amount",0);
            map.put("currency",MEICoinCurrency);
            return  map;
        }
        List<DdbAssetRecord> ddbAssetRecordList = ddbAssetRecordMapper.selectByMEIReleseAmount(customer);
		List<DdbDigAssetTodayDemand> digAssetTodayDemandList = ddbDigAssetTodayDemandMapper.selectByCustId(customer);
		BigDecimal amount = BigDecimal.ZERO;
		for (DdbAssetRecord ddb: ddbAssetRecordList) {
			amount = amount.add(new BigDecimal(ddb.getAmount()));
		}
		for (DdbDigAssetTodayDemand ddb:digAssetTodayDemandList) {
			amount = amount.add(new BigDecimal(ddb.getAmount()));
		}*/
	}


	/**
	 * 根据用户ID获取用户股东信息
	 * @param userId
	 * @return
	 */
	public DdbShoreholder getShoreholder(String userId){
		DdbShoreholder shoreholder = ddbShoreholderMapper.selectbyCustId(userId);
		return shoreholder;
	}
	/**
	 * 同步邀请人到游戏方
	 * Update by liangtf 20181121,将直接同步游戏方  改为   同步到MQ
	 * @param map
	 * @return
	 */
	public Map SynchronizeInviteToGame(Map map){
		Map<String , Object> param = new HashMap<>();
		String flowId = UuidUtil.getUUID();
		param.put("flowId", flowId);
		param.put("parentId",map.get("parentId") );
		param.put("userId", map.get("userId"));
		param.put("nonceStr", Math.round(Math.random()*10)+1);
		try {
			String key= MD5Utils.makeSign(JSON.toJSONString(param));
			param.put("sign", key);
		}catch (Exception e){

		}
		logger.info("同步邀请人信息"+param.toString());
		//同步game
        try{
        	
        	
        	Map<String, String> data = new HashMap<>();
			data.put("address", game_ip+"/app/ddb/userrel");
	        data.put("body", JSON.toJSONString(param));
	        data.put("flowId", flowId);
			String msgId = AliMQServiceUtil.createNewOrder(flowId, JSON.toJSONString(data), topic, tag);
			DdbMqMessageRecord ddbMqMessageRecord = buidBean(flowId,msgId,tag,JSON.toJSONString(data));
			if(!StringUtils.isEmpty(msgId)) {
				ddbMqMessageRecord.setStatus(true);
			}else {
				ddbMqMessageRecord.setStatus(false);
				logger.info("后台用户审核同步邀请人信息到mq flowId="+ddbMqMessageRecord.getFlowId()+", error:hd put MQ oneStep failure");
			}
			ddbMqMessageRecordMapper.insert(ddbMqMessageRecord);
			if(!StringUtils.isEmpty(msgId)) {
				return JsonUtil.toJsonSuccess("同步mq成功",null);
			}else {
				return JsonUtil.toJsonError(ERROR_41116);
			}
        }catch (Exception e){
            logger.info("注册邀请人同步mq失败"+e);
            Map<String , Object> backContent = new HashMap<>();
            backContent.put("code","ERROR");
            backContent.put("message",e.toString());
            return backContent;
        }
	}
//	public Map SynchronizeInviteToGame(Map map){
//		Map<String , Object> param = new HashMap<>();
//		param.put("flowId", UuidUtil.getUUID());
//		param.put("parentId",map.get("parentId") );
//		param.put("userId", map.get("userId"));
//		param.put("nonceStr", Math.round(Math.random()*10)+1);
//		try {
//			String key= MD5Utils.makeSign(JSON.toJSONString(param));
//			param.put("sign", key);
//		}catch (Exception e){
//
//		}
//		logger.info("同步邀请人信息"+param.toString());
//		//同步game
//        try{
//			String s = HttpUtil.sendPostJson(game_ip+"/app/ddb/userrel", JSON.toJSONString(param));
//			logger.info("游戏方返回信息------》"+s);
//			Map<String,Object> result = (Map<String,Object>)JSON.parse(s);
//            int co = (int)result.get("code");
//            if(co == 0 || co == 501){
//                logger.info("同步邀请人成功"+param.toString());
//            }else {
//				logger.error("同步邀请人失败"+param.toString());
//			}
//            return result;
//        }catch (Exception e){
//            logger.info("注册邀请人同步失败"+e);
//            Map<String , Object> backContent = new HashMap<>();
//            backContent.put("code","ERROR");
//            backContent.put("message",e.toString());
//            return backContent;
//        }
//	}

	public void modifyNickNameAndPhoto(String nickname, String url) {
		// TODO Auto-generated method stub
		// 刚进入我的页面 url 一定为空
		Customer cus = (Customer) SessionUtil.getUserInfo();
		cus = customerMapper.selectByCustId(cus.getCustId());
		if(nickname != null) {
			cus.setNickname(nickname);
		}
		if(url != null) {
			cus.setFavicon(url);
		}

		if(nickname != null || url != null) {
			/*
			// 新需求：修改昵称和头像需要同步到游戏方   add by liangtf 2018.11.20
			// 需要在此处添加同步mq的操作
			Map<String,Object> map=new HashMap<>();
			String flowId = UuidUtil.getUUID();
			map.put("flowId", flowId);
			map.put("userId", cus.getCustId());
			if(StringUtils.isEmpty(nickname)) {
				
				nickname = cus.getNickname();
				if(StringUtils.isEmpty(nickname)) {
					nickname= "";
				}
				map.put("nickName", nickname);
			}else {
				map.put("nickName", nickname);
			}
			if(StringUtils.isEmpty(url)) {
				url = cus.getFavicon();
				if(StringUtils.isEmpty(url)) {
					url= "";
				}
				map.put("favicon", url);
			}else{
				map.put("favicon", url);
			}
//			map.put("favicon", cus.getFavicon());
			map.put("nonceStr",Math.round(Math.random() * 10) + 1);
			String key = "";
			try {
				key = MD5Utils.makeSign(JSON.toJSONString(map));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("sign", key);
			Map<String, String> data = new HashMap<>();
			data.put("address", game_ip+"/app/ddbuser/info");// 向游戏同步的地址
	        data.put("body", JSON.toJSONString(map));
	        data.put("flowId", flowId);
	        String msgId = AliMQServiceUtil.createNewOrder(flowId, JSON.toJSONString(data), topic, tag);
	        DdbMqMessageRecord ddbMqMessageRecord = buidBean(flowId,msgId,tag,JSON.toJSONString(map));
			if(!StringUtils.isEmpty(msgId)) {
				ddbMqMessageRecord.setStatus(true);
			}else {
				ddbMqMessageRecord.setStatus(false);
				logger.info("app修改个人信息同步mq flowId="+ddbMqMessageRecord.getFlowId()+", error:hd put MQ oneStep failure");
			}
			ddbMqMessageRecordMapper.insert(ddbMqMessageRecord);
			*/
			
			Weekend<Customer> weekend = Weekend.of(Customer.class);
			WeekendCriteria<Customer, Object> Criteria = weekend.weekendCriteria();
			Criteria.andEqualTo(Customer::getCustId, cus.getCustId());
			customerMapper.updateByExample(cus, weekend);
		}
	}
	private DdbMqMessageRecord buidBean(String orderId, String msgId, String tag2, String jsonString) {
		 DdbMqMessageRecord msg = new DdbMqMessageRecord();
	 	msg.setFlowId(orderId);
	 	msg.setGroupName(producerId);
	 	msg.setTopic(topic);
	 	msg.setTag(tag2);
	 	msg.setMsgId(msgId);
	 	msg.setDataBody(jsonString);
	 	msg.setSendType(3);// 有序消费
	 	msg.setGroupType(1);// 生产
	 	msg.setCreateTime(DateUtils.getDateTime());
	 	return msg;
		}
	public CustomerVo getNickNameAndPhoto() {
		// TODO Auto-generated method stub

		Customer cus = (Customer) SessionUtil.getUserInfo();
		CustomerVo vo = new CustomerVo();
		cus = customerMapper.selectByCustId(cus.getCustId());
		vo.setFavicon(cus.getFavicon() == null?"":cus.getFavicon());
		vo.setNickname(cus.getNickname() == null?"":cus.getNickname());
//		Integer regWay = cus.getRegWay();// 手机0；邮箱1
//		if (cus.getRegWay() == 0) {
//			vo.setAccount(cus.getAccount());
//		} else {
//			vo.setAccount(cus.getEmail());
//		}
		vo.setAccount(cus.getAccount());
		return vo;
	}
	
	
	public List<RankListVo> getRankList(RankListVo vo){
		List<RankListVo> list = customerMapper.getRankList(vo);
		for (RankListVo v : list) {
			String num = new BigDecimal(v.getAmount()).divide(new BigDecimal("100")).stripTrailingZeros().toPlainString();
			v.setAmount(num);
			if(StringUtils.isEmpty(v.getNickName())) {
				 if(v.getAccount().contains("@")) {
					 v.setNickName(v.getAccount().replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4"));
				 }else {
					 v.setNickName(v.getAccount().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
				 }
			}
		}
		return list;
	}
	
	public List<String> getWelfareRecord(String custId){
		return customerMapper.selectWelfareRecord(custId);
	}
}
