package com.jinglitong.springshop.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.entity.Paymenttransaction;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.plugin.PaymentPlugin;
import com.jinglitong.springshop.servcie.CustomerService;
import com.jinglitong.springshop.servcie.MqOrderPayService;
import com.jinglitong.springshop.servcie.PaymentTransactionService;
import com.jinglitong.springshop.servcie.PluginService;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.request.OmiPayParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
@RestController
@RequestMapping("/orderPay")
@Api(value = "订单支付 API", tags = "订单支付 API")
public class OrderPayController {
	
	@Value("${aliyun.mq.order.topic}")
    private String topic;
	
	@Value("${aliyun.mq.order.nonepaynotice.group}")
    private String groupId;
	
	@Value("${aliyun.mq.order.nonepaynotice.tag}")
	private String tag;
	
	@Autowired
    private CustomerService customerService;
	@Autowired
    private PluginService pluginService;
	
	@Autowired
    private OrdersMapper ordersMapper;
	
	@Autowired
    private PaymentTransactionService paymentTransactionService;
	
	@Autowired
    private MqOrderPayService mqOrderPayService;
	
	@ApiOperation(value = "支付")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ShopRespose<Object> pay(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                  @RequestBody OmiPayParam payParam) {
        LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
        if(info == null || info.getZid() == null){
        	return new ShopRespose<Object>(ErrorEnum.ERROR_205);
        }
        
        // 1、获得参数：订单号和支付方式zid
        String paymentPluginId = payParam.getPaymentPluginId();
        String orderId = payParam.getOrderNo();
        Orders order = new Orders();
		order.setZid(orderId);
		order = ordersMapper.selectOne(order);

		// 3、根据支付插件id获得支付插件
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        // 4、生成支付事务     
        Paymenttransaction paymentTransaction = paymentTransactionService.generate(orderId, paymentPlugin);  
        //paymentTransaction.setAmount(BigDecimal.ZERO);
        if(paymentTransaction.getAmount().compareTo(BigDecimal.ZERO) == 0){
        	//需要支付的法币为0，发送mq消息直接支付完成*/
        	mqOrderPayService.sendMqToPaybank(topic, groupId, tag, paymentTransaction.getZid());
        	return new ShopRespose<Object>(ErrorEnum.ERROR_5403);
        }    
        // 5、调用支付插件处理函数，获得给前端返回的支付参数
        String res = paymentPlugin.payHandle(paymentPlugin, paymentTransaction, order.getSn(), payParam.getPlatform()); 
        
        JSONObject jasonObject = null;
		try {
			jasonObject = new JSONObject(res);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Map maps = (Map)JSON.parse(jasonObject.toString());*/
        return new ShopRespose<Object>(IConstants.SUCCESS, "成功",jasonObject.toString() );

        
    } 
	

}
