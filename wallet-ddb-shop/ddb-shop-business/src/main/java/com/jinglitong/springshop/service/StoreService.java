package com.jinglitong.springshop.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Store;
import com.jinglitong.springshop.mapper.StoreMapper;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.request.PageVo;
import com.jinglitong.springshop.vo.request.StoreAddVo;
import com.jinglitong.springshop.vo.response.KukaReceiverAuditVo;
import com.jinglitong.springshop.vo.response.StoreVo;

import tk.mybatis.mapper.util.StringUtil;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
public class StoreService {
	
	@Autowired
	private StoreMapper storeMapper;
	
	public PageInfo<StoreVo> getEnabledStores(PageVo vo){
		if(vo != null && vo.getPageNum()!= null && vo.getPageSize() != null){
			PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		}		
		List<StoreVo> list = storeMapper.selectEnabledStores();
		return new PageInfo<StoreVo>(list);
	}
	public List<StoreVo> getEnabledStores(){		
		List<StoreVo> list = storeMapper.selectEnabledStores();
		return list;
	}
	
	public ShopRespose<Object> addStore(StoreAddVo vo) {
		if(StringUtil.isEmpty(vo.getName())){
			return new ShopRespose<Object>(ErrorEnum.ERROR_5603); 
		}
		if(StringUtil.isEmpty(vo.getContacts())){
			return new ShopRespose<Object>(ErrorEnum.ERROR_5604); 
		}
		if(StringUtil.isEmpty(vo.getMobile())){
			return new ShopRespose<Object>(ErrorEnum.ERROR_5605); 
		}
		Store store = new Store();
		store.setZid(UuidUtil.getUUID());		
		store.setName(vo.getName());
		store.setContacts(vo.getContacts());
		store.setMobile(vo.getMobile());
		store.setIsenabled(new Byte("1"));
		store.setEmail("1");
		store.setPhone("1");
		store.setStatus(new Byte("1"));
		store.setType(new Byte("1"));	
		store.setBusinessId("1");
		Date date = new Date();
		store.setCreatedTime(date);
		store.setUpdatedTime(date);
		if(storeMapper.insert(store) == 1){
			return new ShopRespose(IConstants.SUCCESS,"成功");
		}
		return new ShopRespose<Object>(ErrorEnum.ERROR_5601);

	}
	
	public StoreVo getStore(String zid){
		if(StringUtil.isEmpty(zid)){
			return null;
		}
		return storeMapper.selectByZid(zid);	
	}
	
	public int updateStore(StoreAddVo vo){		
		Store store = new Store();
		store.setZid(vo.getZid());
		if(!StringUtil.isEmpty(vo.getContacts())){
			store.setContacts(vo.getContacts());
		}
		if(!StringUtil.isEmpty(vo.getMobile())){
			store.setMobile(vo.getMobile());
		}
		if(!StringUtil.isEmpty(vo.getName())){
			store.setName(vo.getName());
		}
    	Weekend<Store>weekend = Weekend.of(Store.class);
    	WeekendCriteria<Store,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(Store::getZid, vo.getZid());    	
    	return storeMapper.updateByExampleSelective(store, weekend);
	}

}
