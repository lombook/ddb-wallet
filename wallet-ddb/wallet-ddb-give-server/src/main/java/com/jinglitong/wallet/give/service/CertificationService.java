package com.jinglitong.wallet.give.service;

import com.jinglitong.wallet.api.model.Area;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.IdentityInfo;
import com.jinglitong.wallet.api.model.view.CertificateVO;
import com.jinglitong.wallet.give.mapper.AreaMapper;
import com.jinglitong.wallet.give.mapper.CustomerMapper;
import com.jinglitong.wallet.give.mapper.IdentityInfoMapper;
import com.jinglitong.wallet.give.util.DateUtils;
import com.jinglitong.wallet.give.util.UuidUtil;
import com.jinglitong.wallet.give.common.CertifacateTypeEnum;
import com.jinglitong.wallet.give.common.ErrorEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CertificationService {
	
	@Autowired
    private CustomerMapper customerMapper;

	@Autowired
    private IdentityInfoMapper identityInfoMapper;
	
	@Autowired
    private AreaMapper areaMapper;

	
	@Transactional
	public Boolean certificate(CertificateVO co) throws Exception {
		if(CertifacateTypeEnum.IDCARD.getCode().equals(co.getIdentityType())) {
			String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
	                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
			String idNo=co.getIdentityNo().trim();
			if(idNo!=null) {
				if(!idNo.matches(regularExpression)) {
					throw new Exception(ErrorEnum.ERROR_21113.getCode()+"");
				}
				// 添加 年龄的校验
				int age = getAgeByIDNumber(idNo);
				if(age < 0) {
					throw new Exception(ErrorEnum.ERROR_21113.getCode()+"");
				}
				
				
			}
		}
		if(isOrNotCertif()) {
			throw new Exception(ErrorEnum.ERROR_21112.getCode()+"");
		}
		//得到custid
    	Subject currentUser = SecurityUtils.getSubject();
    	Customer cus= (Customer)currentUser.getPrincipal();
    	String custId=cus.getCustId();
    	
		if(isOrNotExist(co,cus.getAppId())) {// 如果返回true  说明存在重复身份证   则提示 不能继续     false   则继续
			throw new Exception(ErrorEnum.ERROR_41008.getCode()+"");
		}
		
    	
    	
    	String date = DateUtils.getDateTime();
    	
        // 查询数据库  是否认证  返回 IdentityInfo 对象  
    	Weekend<IdentityInfo> weekendInfo = Weekend.of(IdentityInfo.class);
        WeekendCriteria<IdentityInfo, Object> criteria = weekendInfo.weekendCriteria();
        criteria.andEqualTo(IdentityInfo::getCustId,custId);
        IdentityInfo certi=identityInfoMapper.selectOneByExample(weekendInfo);
    	//   判断是否为空  不为空：  已认证  则  直接使用该对象  ; 为空  ： new一个对象
        if(certi==null) {
        	certi=new IdentityInfo();
        	certi.setZid(UuidUtil.getUUID());
        	certi.setCustId(custId);
        	certi.setCustName(co.getCustName());
        	certi.setIdentityType(co.getIdentityType());
        	certi.setIdentityNo(co.getIdentityNo().trim());
        	certi.setProvinceId(co.getProvinceId());
        	certi.setCityId(co.getCityId());
        	certi.setFrontUrl(co.getFrontUrl());
        	certi.setBackUrl(co.getBackUrl());
        	certi.setCreatedTime(date);
        	certi.setUpdatedTime(date);
        	certi.setAuditState("0");
        	certi.setAppId(co.getAppId());
        	//将身份认证信息插入数据库
        	identityInfoMapper.insertSelective(certi);
        }else {
        	certi.setCustName(co.getCustName());
        	certi.setIdentityType(co.getIdentityType());
        	certi.setIdentityNo(co.getIdentityNo().trim());
        	certi.setProvinceId(co.getProvinceId());
        	certi.setCityId(co.getCityId());
        	certi.setFrontUrl(co.getFrontUrl());
        	certi.setBackUrl(co.getBackUrl());
        	certi.setUpdatedTime(date);
        	certi.setAuditState("0");
        	//将身份认证信息更新到数据库
        	identityInfoMapper.updateByPrimaryKey(certi);
        }
    	
    	
    	
    	/*Customer customer = new Customer();
    	customer.setCertificateState(true);
    	customer.setUpdatedTime(DateUtils.getDateTime());
    	Weekend<Customer>weekend = Weekend.of(Customer.class);
    	WeekendCriteria<Customer,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(Customer::getCustId, custId);
    	customerMapper.updateByExampleSelective(customer, weekend);
		ddbAssetRecordMapper.updateRealStateByCustId(1,custId);*/
        return true;
    }


	// 判断 是否存在重复身份证号
	private boolean isOrNotExist(CertificateVO co,String appID) {
		// TODO Auto-generated method stub
		// 根据身份证   查询数据库   返回 IdentityInfo 对象  
    	Weekend<IdentityInfo> weekendInfo = Weekend.of(IdentityInfo.class);
        WeekendCriteria<IdentityInfo, Object> criteria = weekendInfo.weekendCriteria();
        criteria.andEqualTo(IdentityInfo::getIdentityNo,co.getIdentityNo().trim());
        criteria.andEqualTo(IdentityInfo::getAuditState,"1");// 查询 已经认证通过的
        criteria.andEqualTo(IdentityInfo::getAppId,appID);
//        IdentityInfo certi=identityInfoMapper.selectOneByExample(weekendInfo);// 可能会存在垃圾数据   ， 可能返回是一个集合   所以不用此方法,使用下边 ： 返回集合的方法
        List<IdentityInfo> identityInfos = identityInfoMapper.selectByExample(weekendInfo);
        if(identityInfos==null || identityInfos.size() == 0) {// 因为在审核的时候 认证表中只能有一个  身份证号。在app端用户提交时 一个都不能有
			return false;
		}
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
        if(CertifacateTypeEnum.IDCARD.getCode().equals(code)) {
        	identityType= CertifacateTypeEnum.IDCARD.getName();
        }else if(CertifacateTypeEnum.PASSPORT.getCode().equals(code)) {
        	identityType= CertifacateTypeEnum.PASSPORT.getName();
        }
        
        Map<Object, Object> map=new HashMap<>();
        map.put("userName", certi.getCustName());
        map.put("identityNo", certi.getIdentityNo());
        map.put("identityType", identityType);
        map.put("provinceName", area2.getName());
        map.put("cityName",area.getName() );
        map.put("frontUrl",certi.getFrontUrl() );
        map.put("backUrl",certi.getBackUrl() );
    	return map;
    }
    
    
    public IdentityInfo findCertState() {
    	//得到custid
    	Subject currentUser = SecurityUtils.getSubject();
    	Customer cus= (Customer)currentUser.getPrincipal();
    	String custId=cus.getCustId();
    	
    	// 查询数据库  是否认证  返回 IdentityInfo 对象  
    	Weekend<IdentityInfo> weekendInfo = Weekend.of(IdentityInfo.class);
        WeekendCriteria<IdentityInfo, Object> criteria = weekendInfo.weekendCriteria();
        criteria.andEqualTo(IdentityInfo::getCustId,custId);
        return identityInfoMapper.selectOneByExample(weekendInfo);
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
    
}
