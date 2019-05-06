package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.AreaDic;
import com.jinglitong.springshop.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AreaDicMapper extends MyMapper<AreaDic> {
	List<Map<String,String>> selectByParent(@Param("parentId") String parentId);

}