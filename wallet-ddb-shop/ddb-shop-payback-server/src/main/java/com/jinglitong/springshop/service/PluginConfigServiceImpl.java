/*
 * Copyright 2005-2017 jingtum.com. All rights reserved.
 * Support: http://www.jingtum.com
 * License: http://www.jingtum.com/license
 */
package com.jinglitong.springshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.springshop.entity.PayConfig;
import com.jinglitong.springshop.mapper.PayConfigMapper;

/**
 * Service - 插件配置
 * 
 * @author JMALL Team
 * @version 0.0.1
 */
@Service
public class PluginConfigServiceImpl  implements PayConfigService {

	@Autowired
	private PayConfigMapper payConfigMapper;


	@Transactional(readOnly = true)
	@Cacheable(value = "pluginConfig#${select.cache.timeout:10000}", keyGenerator = "redisKeyGenerator")
	public PayConfig findByPluginId(String pluginId) {
		return payConfigMapper.selectByPrimaryKey(pluginId);
	}


	@Override
	public boolean pluginIdExists(String pluginId) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void deleteByPluginId(String pluginId) {
		// TODO Auto-generated method stub
		
	}
}