/*
 * Copyright 2005-2017 jingtum.com. All rights reserved.
 * Support: http://www.jingtum.com
 * License: http://www.jingtum.com/license
 */
package com.jinglitong.springshop.servcie;

import com.jinglitong.springshop.plugin.PaymentPlugin;

/**
 * Service - 插件
 * 
 * @author JMALL Team
 * @version 0.0.1
 */
public interface PluginService {

	/**
	 * 获取支付插件
	 * 
	 * @param id
	 *            ID
	 * @return 支付插件
	 */
	PaymentPlugin getPaymentPlugin(String id);


}