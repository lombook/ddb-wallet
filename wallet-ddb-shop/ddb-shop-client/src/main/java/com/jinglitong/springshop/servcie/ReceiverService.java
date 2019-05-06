package com.jinglitong.springshop.servcie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.AreaDic;
import com.jinglitong.springshop.entity.Customer;
import com.jinglitong.springshop.entity.Receiver;
import com.jinglitong.springshop.mapper.AreaDicMapper;
import com.jinglitong.springshop.mapper.CustomerMapper;
import com.jinglitong.springshop.mapper.ReceiverMapper;
import com.jinglitong.springshop.utils.UuidUtil;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Slf4j
@Service
public class ReceiverService {
	
	@Autowired
    private CustomerMapper customerMapper;
	
	@Autowired
	private AreaDicMapper areaDicMapper;
	
	@Autowired
	private ReceiverMapper receiverMapper;
	
	public ShopRespose<List<Receiver>> getList(String custId){
		List<Receiver> list = new ArrayList<Receiver>();
		Weekend<Receiver>weekend = Weekend.of(Receiver.class);
    	WeekendCriteria<Receiver,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(Receiver::getCustId, custId);
    	weekend.setOrderByClause("update_time desc");
    	list = receiverMapper.selectByExample(weekend);
   
    	return new ShopRespose<>(IConstants.SUCCESS,"查询成功",list);
	}
	public ShopRespose<Object> add(String custId,Integer areaID,String consignee,String address,String zipCode,String phone,String isdefault){
		AreaDic area = areaDicMapper.selectByPrimaryKey(areaID);
		if(area == null ){
			//没有对应的地区
			return new ShopRespose<Object>(ErrorEnum.ERROR_5001);
			//return -1;
		}
		Customer customer = new Customer();
		customer.setCustId(custId);
        customer = customerMapper.selectOne(customer);

		if(customer == null){
			return new ShopRespose<Object>(ErrorEnum.ERROR_203);
		}
			
		Date date = new Date(System.currentTimeMillis());
		Receiver receiver = new Receiver();
		receiver.setZid(UuidUtil.getUUID());
		receiver.setAddress(address);
		receiver.setAreaId(areaID);
		receiver.setPhone(phone);
		receiver.setConsignee(consignee);
		
		String fullName = area.getFullName();
		AreaDic areaprarent = area;
		while(true){
			areaprarent = areaDicMapper.selectByPrimaryKey(areaprarent.getParentId());
			fullName = areaprarent.getFullName() + " " + fullName;
			if(areaprarent.getParentId().equals("G00156")){
				break;
			}
		}
		receiver.setAreaname(fullName);
		
		
		if(isdefault != null && isdefault.equals("1")){			
			Receiver receivertmp = new Receiver();
			receivertmp.setIsdefault(new Byte("0"));
            Weekend<Receiver>weekendr = Weekend.of(Receiver.class);
        	WeekendCriteria<Receiver,Object> Criteriar =weekendr.weekendCriteria();
        	Criteriar.andEqualTo(Receiver::getCustId, custId);
			receiverMapper.updateByExampleSelective(receivertmp,weekendr);
			receiver.setIsdefault(new Byte("1"));
			
		}else{
			receiver.setIsdefault(new Byte("0"));
		}
		receiver.setZipcode(zipCode);
		receiver.setCustId(custId);
		receiver.setCreatedTime(date);
		receiver.setUpdateTime(date);

		
		int res = receiverMapper.insertSelective(receiver);
		if(res == 1){
			return new ShopRespose<Object>(IConstants.SUCCESS,"添加成功");
		}
		else{
			return new ShopRespose<Object>(ErrorEnum.ERROR_1);
		}		
	}
	
	public ShopRespose<?> update(String zid,Integer areaID,String consignee,String address,String zipCode,String phone,String isdefault){	
		AreaDic area = areaDicMapper.selectByPrimaryKey(areaID);
		
		Receiver receiver = new Receiver();
		receiver.setZid(zid);
		receiver = receiverMapper.selectOne(receiver);
        if(area == null ){
			//没有对应的地区
        	return new ShopRespose<>(ErrorEnum.ERROR_5001);
		}
		if(receiver == null){
			//收货地址
			return new ShopRespose<>(ErrorEnum.ERROR_5002);
		}
		Date date = new Date(System.currentTimeMillis());
		receiver.setAddress(address);
		receiver.setAreaId(areaID);
		receiver.setPhone(phone);
		receiver.setConsignee(consignee);
		String fullName = area.getFullName();
		AreaDic areaprarent = area;
		while(true){
			areaprarent = areaDicMapper.selectByPrimaryKey(areaprarent.getParentId());
			fullName = areaprarent.getFullName() + " " + fullName;
			if(areaprarent.getParentId().equals("G00156")){
				break;
			}
		}
		receiver.setAreaname(fullName);
		if(isdefault != null && isdefault.equals("1")){			
			Receiver receivertmp = new Receiver();
			receivertmp.setIsdefault(new Byte("0"));
            Weekend<Receiver>weekend = Weekend.of(Receiver.class);
        	WeekendCriteria<Receiver,Object> Criteria =weekend.weekendCriteria();
        	Criteria.andEqualTo(Receiver::getCustId, receiver.getCustId());
			receiverMapper.updateByExampleSelective(receivertmp,weekend);
			receiver.setIsdefault(new Byte("1"));
			
		}else{
			receiver.setIsdefault(new Byte("0"));
		}
		receiver.setZipcode(zipCode);		
		receiver.setCustId(receiver.getCustId());
		receiver.setUpdateTime(date);
		int res = receiverMapper.updateByPrimaryKey(receiver);
		if(res == 1){
			return new ShopRespose<>(IConstants.SUCCESS,"更新成功");
		}else{
			return new ShopRespose<>(ErrorEnum.ERROR_1);
		}
	}
	//删除收货地址
	public ShopRespose<?> del(String custId,String zid){	
		Receiver receiver = new Receiver();
		receiver.setZid(zid);
		receiver = receiverMapper.selectOne(receiver);
		if(receiver == null){
			return new ShopRespose<>(ErrorEnum.ERROR_5002);
		}
		if(!receiver.getCustId().equals(custId)){
			return new ShopRespose<>(ErrorEnum.ERROR_5002);
		}

		int res = receiverMapper.deleteByPrimaryKey(receiver.getId());
		if(res == 1){
			return new ShopRespose<>(IConstants.SUCCESS,"删除成功");
		}else{
			return new ShopRespose<>(ErrorEnum.ERROR_1);
		}
	}
	
	public ShopRespose<List<?>> getAreaChildren(String strareaID){
		ShopRespose<List<?>> shopRespose = new ShopRespose<List<?>>();
		AreaDic area = new AreaDic();
		if(strareaID == null){
			area.setParentId(null);
			
		}else{
			area.setParentId(strareaID);
		}
		shopRespose.setCode(IConstants.SUCCESS);
    	shopRespose.setMessage("查询成功");
    	shopRespose.setData(areaDicMapper.selectByParent(strareaID)); 
    	return shopRespose;
	}

    public Receiver selectByDefault(String custId) {
		return receiverMapper.selectByDefault(custId);
    }
}
