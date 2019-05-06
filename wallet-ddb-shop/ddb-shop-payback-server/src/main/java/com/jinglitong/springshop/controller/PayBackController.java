package com.jinglitong.springshop.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.internal.util.AlipaySignature;
import com.jinglitong.springshop.entity.Paymenttransaction;
import com.jinglitong.springshop.mapper.PaymenttransactionMapper;
import com.jinglitong.springshop.plugin.PaymentPlugin;
import com.jinglitong.springshop.service.CustomerTeamService;
import com.jinglitong.springshop.service.KukaOrderRewardService;
import com.jinglitong.springshop.service.OrderService;
import com.jinglitong.springshop.service.PayMessageService;
import com.jinglitong.springshop.service.PluginService;
import com.jinglitong.springshop.util.XmlUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payback")
@Api(value = "omipay回调API",tags = "omipay回调 API")
@Slf4j
public class PayBackController {
	@Autowired
    private PluginService pluginService;
	
	@Autowired
	private PaymenttransactionMapper paymentTransactionMapper;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PayMessageService payMessageService;
	
	@Autowired
	private KukaOrderRewardService kukaOrderRewardService;
	
	@Autowired
    private CustomerTeamService customerTeamService;
	
	@RequestMapping(value = "/wechatApp", /*produces = "application/json;charset=UTF-8",*/ method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getWechatNotify(HttpServletRequest request) throws DocumentException {
		
		log.info(" payment  weixin_post_pay start .............");
        //解决微信重复回调
        String message = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

        StringBuilder content = new StringBuilder();
        try {
            ServletInputStream ris = request.getInputStream();
            byte[] b = new byte[1024];
            int lens = -1;
            while ((lens = ris.read(b)) > 0) {
                content.append(new String(b, 0, lens));
            }
        } catch (IOException e) {
            log.error("payment  weixin_post_pay end.............shop trading wx unknow");
            return "";
        }
        SortedMap<String, String> sortedMap = XmlUtils.fromXml(content.toString());

        // 添加微信支付记录
        List<String> keys = new ArrayList<>(sortedMap.keySet());
        for (String key : keys) {
            String value = sortedMap.get(key);
            log.info("weChatPayNotice " + key + " ==== " + value);
        }
        // 验证请求
        Map<String,Object> map = new HashMap<String,Object>(sortedMap);
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin("WX_APP_PAY");
        if (!paymentPlugin.veritySign(map,paymentPlugin)) {
            log.error("payment  weixin_post_pay end...........shop trading wx args");
            return "";
        }
        String return_code = sortedMap.get("return_code");
        String result_code = sortedMap.get("result_code");

        if (!"SUCCESS".equals(return_code) || !"SUCCESS".equals(result_code)) {
            log.error("payment  weixin_post_pay end...........shop trading wx args");
            return "";
        }
        Paymenttransaction p = new Paymenttransaction();
		p.setZid(sortedMap.get("out_trade_no"));
		p = paymentTransactionMapper.selectOne(p);
        
        double aumout = Double.valueOf(sortedMap.get("total_fee")) / 100;
        if (p.getPaystatus() == 1) {
            return message;
        }
        if (p == null || !Double.valueOf(p.getAmount().toString()).equals(aumout)) {
            //支付异常
            log.error("payment  weixin_post_pay end..........shop trading information abnormal");
            return "";
        }
        payMessageService.add(p.getZid(),"3",sortedMap.toString());
        int cny_amount = Integer.valueOf(sortedMap.get("total_fee"));		
		orderService.dealAfterPaySuccess(p,cny_amount);
		
		//支付完成之后给用户自己赠送带金币
		kukaOrderRewardService.giveOrderReward(p.getOrderId());		
		customerTeamService.cusTeam(p.getOrderId());
        log.info("weChatPayNotice method end");
        return message;
		
	}
    
	@ApiOperation(value = "omipay微信支付回调")
    @RequestMapping(value = "/omipayWX",method = RequestMethod.POST)
    public Map<String,String> add(@RequestBody Map<String,Object> body){
		String pZid = (String) body.get("out_order_no");		
		log.info("omipay支付回调开始，pZid=" + pZid);
		
		//根据订单号获得支付事务
		Paymenttransaction p = new Paymenttransaction();
		p.setZid(pZid);
		p = paymentTransactionMapper.selectOne(p);
		if(p == null){
			log.error("支付回调参数错误，找不到对应的支付事务zid");
			//payMessageService.add(payMessage);
			payMessageService.add(pZid,"2",body.toString());
			return null;
		}
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(p.getPaymentpluginid());		
		boolean signRes = paymentPlugin.veritySign(body, paymentPlugin);		
		if(!signRes){		
			//payMessageService.add(payMessage);
			payMessageService.add(pZid,"2",body.toString());
			log.error("支付回调验签错误，Paymenttransaction zid= " + p.getZid());
			return null;
		}
				
		if(body.get("return_code").equals("SUCCESS")){
			//如果已经时支付成功状态	
			Map<String,String> map = new HashMap<String,String>();
			map.put("return_code", "SUCCESS");
			if(p.getPaystatus().equals(1)){
				//如果时已支付状态，直接返回成功
				return map;
			}
			//添加支付消息日志
			payMessageService.add(pZid,"2",body.toString());
			
			//检查支付的金额和支付事务的金额是否一直
			//实际支付金额的100倍
			/*if((int)(body.get("total_amount")) == (p.getAmount().multiply(new BigDecimal(100)).intValue())){
				int cny_amount = (int) body.get("cny_amount");		
				orderService.dealAfterPaySuccess(p,cny_amount);
				Map<String,String> map = new HashMap<String,String>();
				map.put("return_code", "SUCCESS");
				return map;
			}*/
			int cny_amount = (int) body.get("cny_amount");		
			orderService.dealAfterPaySuccess(p,cny_amount);
			
			//支付完成之后给用户自己赠送带金币
			kukaOrderRewardService.giveOrderReward(p.getOrderId());
			
			//sendMqService.sendAz(p.getOrderId());
			customerTeamService.cusTeam(p.getOrderId());
			return map;
		}
		return null;
	}
	/**
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/aliApp", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getAlipayNotify(HttpServletRequest request) {
		//String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArtVKvmCqPnMEUF76XqYTPpPR2o0TmFEcUpkb+GGEMAYQTpAEMG3AzY4VUwHZwcohQzETUo9hr1m5a8jjowc2Y3Nm3YfSSphgFnYmlohQPejM9UZ0TU5wCAfo/h5LtRiHEyh2s6EE9zFi6l67a9mwXlzc4wfHXNI2O3CU2p1D02iNeNTAROCeYqc9zkVbFCW2GsIZ7cAO+OjcJRtj1hJNCg46gPYtPTuAt6eMGSrNzZWs6aGlTyW/yUkhGYkLmDSjIpd1/Kx/ARWMYjsbl8aOqWguDg/TeaE2Z8PgDiwfwQLLnfHnnmJ3l7KVOFHIxhR9JQFJuJAnhXQeZQ2whKt/uQIDAQAB";
		//找到支付插件 支付宝，定义编码
		String paymentPluginId = "ALI_APP_PAY";
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		String charset = "utf-8";//请求使用的编码格式，如utf-8,gbk,gb2312等		
		String sign_type = "RSA2";
		String res = "success";
		//接收并验证回调传递的参数
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		try {
			boolean verify_result = AlipaySignature.rsaCheckV1(params, paymentPlugin.getAttribute("appPublicKey"), charset, sign_type);
		if (verify_result) {
			String trade_status = params.get("trade_status");
			String out_trade_no = params.get("out_trade_no");
			//String trade_no = params.get("trade_no");
			if ("TRADE_SUCCESS".equals(trade_status)) { // 交易支付成功的执行相关业务逻辑
				//具体的业务处理
				//根据订单号获得支付事务
				Paymenttransaction p = new Paymenttransaction();
				p.setZid(out_trade_no);
				p = paymentTransactionMapper.selectOne(p);
				if(p == null || !p.getPaymentpluginid().equals(paymentPlugin.getId())){
					log.error("支付回调参数错误，找不到对应的支付事务zid");
					//payMessageService.add(payMessage);
					payMessageService.add(out_trade_no,"1",params.toString());
					return null;
				}
				if(p.getPaystatus().equals(1)){
					//如果时已支付状态，直接返回成功
					return res;
				}				
				payMessageService.add(out_trade_no,"1",params.toString());
				
				String total_amount =  params.get("total_amount");	//receipt_amount实收金额
				BigDecimal amount = new BigDecimal(total_amount);
				if(amount.compareTo(p.getAmount()) != 0){
					log.error("{}支付金额不正确！",p.getZid() );
					return null;
				}

				orderService.dealAfterPaySuccess(p,amount.multiply(new BigDecimal(100)).intValue());				
				//支付完成之后给用户自己赠送带金币
				kukaOrderRewardService.giveOrderReward(p.getOrderId());

				//sendMqService.sendIet(p.getOrderId());
				customerTeamService.cusTeam(p.getOrderId());
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("支付宝回调验签错误！" );
			return null;
		}
		return "success";
	}
}
