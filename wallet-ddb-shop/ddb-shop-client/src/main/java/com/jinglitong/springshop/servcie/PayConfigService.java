/*
 * Copyright 2005-2017 jingtum.com. All rights reserved.
 * Support: http://www.jingtum.com
 * License: http://www.jingtum.com/license
 */
package com.jinglitong.springshop.servcie;

import com.jinglitong.springshop.entity.PayConfig;


/**
 * Service - 插件配置
 * 
 * @author JMALL Team
 * @version 0.0.1
 */
public interface PayConfigService  {

	/**
	 * 判断插件ID是否存在
	 * 
	 * @param pluginId
	 *            插件ID
	 * @return 插件ID是否存在
	 */
	boolean pluginIdExists(String pluginId);

	/**
	 * 根据插件ID查找插件配置
	 * 
	 * @param pluginId
	 *            插件ID
	 * @return 插件配置，若不存在则返回null
	 */
	PayConfig findByPluginId(String pluginId);

	/**
	 * 根据插件ID删除插件配置
	 * 
	 * @param pluginId
	 *            插件ID
	 */
	void deleteByPluginId(String pluginId);

}