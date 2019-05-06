package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Store;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.response.StoreVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreMapper extends MyMapper<Store> {
	
	List<StoreVo> selectEnabledStores();
	StoreVo selectByZid(@Param("zid") String zid);
}