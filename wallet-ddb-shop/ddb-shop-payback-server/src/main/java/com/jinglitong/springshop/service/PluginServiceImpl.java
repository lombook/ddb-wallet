/*
 * Copyright 2005-2017 jingtum.com. All rights reserved.
 * Support: http://www.jingtum.com
 * License: http://www.jingtum.com/license
 */
package com.jinglitong.springshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.plugin.PaymentPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service - 插件
 * 
 * @author JMALL Team
 * @version 0.0.1
 */
@Service
public class PluginServiceImpl implements PluginService {

	@Autowired
	private List<PaymentPlugin> paymentPlugins = new ArrayList<>();

	@Autowired
	private Map<String, PaymentPlugin> paymentPluginMap = new HashMap<>();


	public List<PaymentPlugin> getPaymentPlugins() {
        Collections.sort(paymentPlugins);
		return paymentPlugins;
	}

	public PaymentPlugin getPaymentPlugin(String id) {
		return paymentPluginMap.get(id);
	}

}