package com.jinglitong.wallet.server.service;

import static com.jinglitong.wallet.server.common.CertifacateTypeEnum.IDCARD;
import static com.jinglitong.wallet.server.common.CertifacateTypeEnum.PASSPORT;
import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_21112;
import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_21113;
import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_41009;
import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_41116;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.api.model.Area;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.IdentityInfo;
import com.jinglitong.wallet.api.model.view.CertificateVO;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.feign.SynchronizeToGameFeignApi;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.server.mapper.AreaMapper;
import com.jinglitong.wallet.server.mapper.CustomerMapper;
import com.jinglitong.wallet.server.mapper.IdentityInfoMapper;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.UuidUtil;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Transactional
public class CertificationService {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private CustomerMapper customerMapper;

	@Autowired
    private IdentityInfoMapper identityInfoMapper;
	
	@Autowired
    private AreaMapper areaMapper;
	
    
	
	@Resource
	SynchronizeToGameFeignApi synchronizeToGameFeignApi;
	
	@Transactional
	public Boolean certificate(CertificateVO co) throws Exception {
		if(IDCARD.getCode().equals(co.getIdentityType())) {
			String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
	                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
			String idNo=co.getIdentityNo();
			if(idNo!=null) {
				if(!idNo.matches(regularExpression)) {
					throw new Exception(ERROR_21113.getCode()+"");
				}
			}
		}
		if(isOrNotCertif()) {
			throw new Exception(ERROR_21112.getCode()+"");
		}
		
    	//得到custid
    	Subject currentUser = SecurityUtils.getSubject();
    	Customer cus= (Customer)currentUser.getPrincipal();
    	String custId=cus.getCustId();
    	
    	String date = DateUtils.getDateTime();
    	IdentityInfo certi=new IdentityInfo();
    	certi.setZid(UuidUtil.getUUID());
    	certi.setCustId(custId);
    	certi.setCustName(co.getCustName());
    	certi.setIdentityType(co.getIdentityType());
    	certi.setIdentityNo(co.getIdentityNo());
    	certi.setProvinceId(co.getProvinceId());
    	certi.setCityId(co.getCityId());
    	certi.setCreatedTime(date);
    	certi.setUpdatedTime(date);
    	certi.setAuditState("1");
    	certi.setAppId(co.getAppId());
    	//将身份认证信息插入数据库
    	identityInfoMapper.insertSelective(certi);
    	
    	Customer customer = new Customer();
    	customer.setCertificateState(true);
    	customer.setUpdatedTime(DateUtils.getDateTime());
    	Weekend<Customer>weekend = Weekend.of(Customer.class);
    	WeekendCriteria<Customer,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(Customer::getCustId, custId);
    	customerMapper.updateByExampleSelective(customer, weekend);
    	
        return true;
    }
    
    
    
    public Boolean isOrNotCertif() {
    	//得到custid
    	Subject currentUser = SecurityUtils.getSubject();
    	Customer cus= (Customer)currentUser.getPrincipal();
    	String custId=cus.getCustId();
    	
    	Weekend<Customer> weekend = Weekend.of(Customer.class);
        WeekendCriteria<Customer, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(Customer::getCustId,custId);
        Customer cust=customerMapper.selectOneByExample(weekend);
        if(cust==null) {
        	return false;
        }
        Boolean state=cust.getCertificateState();
        return state;
    }
    
    public List<Area> getprovinces(){
    	long begin =System.currentTimeMillis();
    	List<Area> province = areaMapper.getprovinces();
    	long middle=System.currentTimeMillis();
    	
    	List<Area> province2 = areaMapper.getprovinces();
    	long end=System.currentTimeMillis();
    	
    	System.out.println("第一次用时:"+(middle-begin)+"ms,第二次用时:"+(end-middle));
    	return province;
    }
    public List<Area> getcities(String provinceId){
    	List<Area> cities = areaMapper.getcities(provinceId);
    	
    	return cities;
    }
    
    public Map<Object,Object> findCertif(){
    	Subject currentUser = SecurityUtils.getSubject();
    	Customer cus= (Customer)currentUser.getPrincipal();
    	String custId=cus.getCustId();
    	
    	//获得实名认证信息
    	Weekend<IdentityInfo> weekend = Weekend.of(IdentityInfo.class);
        WeekendCriteria<IdentityInfo, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(IdentityInfo::getCustId,custId);
        IdentityInfo certi=identityInfoMapper.selectOneByExample(weekend);
        //得到城市名称
        Weekend<Area> weekend2 = Weekend.of(Area.class);
        WeekendCriteria<Area, Object> areacri = weekend2.weekendCriteria();
        areacri.andEqualTo(Area::getAreaId,certi.getCityId());
        Area area=areaMapper.selectOneByExample(weekend2);
        //得到省份名称
        Weekend<Area> weekend3 = Weekend.of(Area.class);
        WeekendCriteria<Area, Object> areacri2 = weekend3.weekendCriteria();
        areacri2.andEqualTo(Area::getAreaId,certi.getProvinceId());
        Area area2=areaMapper.selectOneByExample(weekend3);
        String identityType=null;
        String code=certi.getIdentityType();
        if(IDCARD.getCode().equals(code)) {
        	identityType=IDCARD.getName();
        }else if(PASSPORT.getCode().equals(code)) {
        	identityType=PASSPORT.getName();
        }
        
        Map<Object, Object> map=new HashMap<>();
        map.put("userName", certi.getCustName());
        map.put("identityNo", certi.getIdentityNo());
        map.put("frontUrl", certi.getFrontUrl());
        map.put("backUrl", certi.getBackUrl());
        map.put("identityType", identityType);
        map.put("provinceName", area2.getName());
        map.put("cityName",area.getName() );
    	return map;
    }
    public Map<Object,Object> authCert(CertificateVO vo){
    	String custId = vo.getCustId();
		Weekend<IdentityInfo> weekendInfo = Weekend.of(IdentityInfo.class);
        WeekendCriteria<IdentityInfo, Object> criteria = weekendInfo.weekendCriteria();
        criteria.andEqualTo(IdentityInfo::getCustId,custId);
        IdentityInfo certi=identityInfoMapper.selectOneByExample(weekendInfo);
        
        // begin add by  2018.10.10,如果存在身份证号，应该审核不通过，但是选择了通过  ，则 直接通知管理员选择  审核不通过
        // 存在重复身份证，并且选择了通过    提示
        if(isOrNotExist(certi) && vo.isFlag()) {
			return JsonUtil.toJsonError(ERROR_41009);
		}
        // end
        
        Weekend<Customer> weekendInfo2 = Weekend.of(Customer.class);
        WeekendCriteria<Customer, Object> criteria2 = weekendInfo2.weekendCriteria();
        criteria2.andEqualTo(Customer::getCustId,custId);
        Customer cust1=customerMapper.selectOneByExample(weekendInfo2);
        
    	if(vo.isFlag()) {
    		
    		
    		//向ddb方发送上下级关系，同步邀请人到game方
    		String inviteCode = cust1.getInviteCode();
    		if(!StringUtils.isEmpty(inviteCode)) {
    			Customer cust2 = new Customer();
    			cust2.setSelfInvite(inviteCode);
    			cust2 = customerMapper.selectOne(cust2);
    			if(cust2!=null) {
    				Map<String,Object> map=new HashMap<>();
        			map.put("userId", cust1.getCustId());
        			map.put("parentId", cust2.getCustId());
        			Map<String,Object> result=synchronizeToGameFeignApi.SynchronizeInviteToGame(map);
        			String code = result.get("code").toString();
//        			if(!code.equals("0") && !code.equals("501")) {
//        				logger.info((String)result.get("message"));
//        				return JsonUtil.toJsonError(ERROR_39001);
//        			}
        			if(!code.equals("0")) {
        				logger.info((String)result.get("message"));
        				return JsonUtil.toJsonError(ERROR_41116);
        			}
    			}
    		}
    		String dateTime = DateUtils.getDateTime();
    		//更新认证表
            certi.setAuditState("1");
            certi.setUpdatedTime(dateTime);
            identityInfoMapper.updateByPrimaryKey(certi);
            //更新用户表
            Customer customer = new Customer();
        	customer.setCertificateState(true);
        	customer.setUpdatedTime(dateTime);
        	Weekend<Customer>weekend = Weekend.of(Customer.class);
        	WeekendCriteria<Customer,Object> Criteria =weekend.weekendCriteria();
        	Criteria.andEqualTo(Customer::getCustId, custId);
        	customerMapper.updateByExampleSelective(customer, weekend);
    		
            
    	}else{
    		certi.setAuditState("2");
    		certi.setUpdatedTime(DateUtils.getDateTime());
            identityInfoMapper.updateByPrimaryKey(certi);
            // begin add by liangtf 2018.10.10  
            // 审核通过后  也可以再审核不通过，需要更新用户表
            //更新用户表   需要把用户表改为未认证
            Customer customer = new Customer();
        	customer.setCertificateState(false);
        	customer.setUpdatedTime(DateUtils.getDateTime());
        	Weekend<Customer>weekend = Weekend.of(Customer.class);
        	WeekendCriteria<Customer,Object> Criteria =weekend.weekendCriteria();
        	Criteria.andEqualTo(Customer::getCustId, custId);
        	customerMapper.updateByExampleSelective(customer, weekend);
        	// end
            
    	}
    	return JsonUtil.toJsonSuccess("审核成功");
    }
    
    
    
    
	// 判断 是否存在重复身份证号
 	private boolean isOrNotExist(IdentityInfo co) {
 		// TODO Auto-generated method stub
 		// 根据身份证   查询数据库   返回 IdentityInfo 对象  
     	Weekend<IdentityInfo> weekendInfo = Weekend.of(IdentityInfo.class);
         WeekendCriteria<IdentityInfo, Object> criteria = weekendInfo.weekendCriteria();
         criteria.andEqualTo(IdentityInfo::getIdentityNo,co.getIdentityNo());
         criteria.andEqualTo(IdentityInfo::getAuditState,"1");// 查询 已经认证通过的
         criteria.andEqualTo(IdentityInfo::getAppId,co.getAppId());
//         IdentityInfo certi=identityInfoMapper.selectOneByExample(weekendInfo);// 可能会存在垃圾数据   ， 可能返回是一个集合   所以不用此方法,使用下边 ： 返回集合的方法
         List<IdentityInfo> identityInfos = identityInfoMapper.selectByExample(weekendInfo);
         if(identityInfos!=null && identityInfos.size() >= 1) {// 因为在审核的时候 认证表中只能有一个  身份证号。在app端用户提交时 一个都不能有
 			return true;
 		}
 		return false;
 	}
    
}
