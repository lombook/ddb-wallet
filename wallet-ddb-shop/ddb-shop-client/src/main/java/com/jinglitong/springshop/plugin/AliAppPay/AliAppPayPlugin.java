package com.jinglitong.springshop.plugin.AliAppPay;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.jinglitong.springshop.entity.Paymenttransaction;
import com.jinglitong.springshop.plugin.PaymentPlugin;

import lombok.extern.slf4j.Slf4j;

@Component("ALI_APP_PAY")
@Slf4j
public class AliAppPayPlugin extends PaymentPlugin{
	
	public static final String CHARSET = "utf-8";//请求使用的编码格式，如utf-8,gbk,gb2312等
	
	public static final String SIGN_TYPE = "RSA2";

	@Override
	public int compareTo(PaymentPlugin arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSiteUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prePayHandle(PaymentPlugin paymentPlugin, Paymenttransaction paymentTransaction,
			String paymentDescription, String extra) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String payHandle(PaymentPlugin paymentPlugin, Paymenttransaction paymentTransaction,
			String paymentDescription, String extra) {
		String alipay_appId = paymentPlugin.getAttribute("appId") ;
		String alipay_private_key = paymentPlugin.getAttribute("appPrivateKey");
		String alipay_public_key = paymentPlugin.getAttribute("appPublicKey");
		
		/*String alipay_appId = "2019022863389459" ;
		String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9UJJ6f7fHKvtBLFoxiMj810b8c9PmeOgiFvifoeJkT8R5WITvcGvLi5mk4UQJaJGwr89vx7OBUccbr3WqMEnayN0VGJU1PQZQXFEIg4EHmXaj69vT+1pI44lSRVB2PRTT6Gg78MBtWaLAoh/7uQAlA86PIQFiZiBmuQ2+6zF6yh9K5IF1hSKtYHLBNc8wkJsQezDgpT/nuzUUISNTN3sgqnSm11dknOoy86rq31yGrU0F9kuwNEYeApPdS4impAIWmdaKAwbkwouuPNaGdth1MkRAAX6rQd9f58FUF5tFIi9GCzvjVV4M40xLv3rAuAUsFgc3eB+E+foJMfgYpu1owIDAQAB";
		String alipay_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD1Qknp/t8cq+0EsWjGIyPzXRvxz0+Z46CIW+J+h4mRPxHlYhO9wa8uLmaThRAlokbCvz2/Hs4FRxxuvdaowSdrI3RUYlTU9BlBcUQiDgQeZdqPr29P7WkjjiVJFUHY9FNPoaDvwwG1ZosCiH/u5ACUDzo8hAWJmIGa5Db7rMXrKH0rkgXWFIq1gcsE1zzCQmxB7MOClP+e7NRQhI1M3eyCqdKbXV2Sc6jLzqurfXIatTQX2S7A0Rh4Ck91LiKakAhaZ1ooDBuTCi6481oZ22HUyREABfqtB31/nwVQXm0UiL0YLO+NVXgzjTEu/esC4BSwWBzd4H4T5+gkx+Bim7WjAg"
				+ "MBAAECggEATwW4wwg34wkSKTzwQUb5QmRxqIvfXfoVs1nHtHT/QilMHthm+/D3/K0+p7Fr7nRG0GDGCFpojxLGenf4Pg9eIAsgZD/6jVLJAGj6Ru61LQYsUbd46gvYoHggU1fiC4FNho7RR+9REZ2klIBemB4mgY4WeYEgN6FHwRxfCuA0NPwJP/nmpLhI+K+A1Z3ABqGHm0P+NafC3MZ02cntxYMikoxa6rlP3upHdOhIZfBtQmD7TzqpMGUVbCx8n1FhWxnUmW/G4EgV/pmFmiK4eQkLNcE7GykERMxY6HLPSpbuP1+645Ju7aI81dR6KcSovMhhRSa1DKa5B+F9P3QvOWMyIQKBgQD651gjyCajQsOrEr6G3VHvijh+K5M8w91StsGZmkicM6ghDxzcaHQ1A"
				+ "CMLBBYy0z6hbFgUlw5iQSp3vSgtTQ5goG+XWxfWaisdsgPRwP12fE+4y9ckkJ0nmdFScMKUwo7nmsTQaf/HwjHfOXTyJYNAjXM4ZZcG0lClowcwZ+TKdQKBgQD6PZe8V8AOhcuKCXzhGQ03jjK5tJ/9rCaeQfQhVlYsZxKZpsaCto5V4nc8+MQr5WGN9qVrThqDLvJbk5R5WJ8YPdGLOcmRXBZAEglx/o12+XgGV1bypugoKHx5cIfOi4Osz9Se960LqMT9HadlYHoYGzt540R/wnUbNxpecRuMtwKBgQDuPT9m8oUlnp6W0TySaJKFhlRwEUZEe6KrCJe6F1J851vs0CHSc2hsbw3XRvEhH46nzm2wV2K0DzaY7ca5hSJyw8RSDpAK61K3ZrHTuJGzsQQt73gA"
				+ "40QmNxsYUbOhd0AkUMQzNYNHFXEviaoux0niwK04TtZiBs/HB48q+mu3hQKBgH3msUn8aE+mN0LLBT6HiSPTcmgGnZxWmdNz4W1Ib/DFJxGRXDz1tmU1nMJbcu+W598yIgX79NDdtOgoN0JaOSlMKPpAZ86voy43JPNqnCgo2Z9vH/rTyOgzfnTnDhz4CpbOte4JNNIx5nJnEgqqYKNySBlKI/OXla3MqoKrq62/AoGABxWkt1YYj7N3pw0ibYI1o3/56aY3GtSbLUreAEehEdUZzsvV5UJzqwlyc/hccaUfbNwZylcttgMHrpN0H7btR9zoMO7vFtrIxoRyEFSdK7a3UcjbJQg2CIJA3a0SX4wIMEFdTXgwp3RcKC6nXjUX62utNYegT0QbSwzePYXd/Xk=";*/
		
		 AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", alipay_appId, alipay_private_key , "json", CHARSET, alipay_public_key, SIGN_TYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        //model.setPassbackParams(URLEncoder.encode(extra));;  //描述信息  添加附加数据
        model.setSubject("捎袋手-订单编号" + paymentDescription); //商品标题
        model.setOutTradeNo(paymentTransaction.getZid()); //商家订单编号
        //model.setTimeoutExpress("30m"); //超时关闭该订单时间
        model.setTotalAmount(paymentTransaction.getAmount() + "");  //订单总金额
        //model.setTotalAmount("0.01");  //订单总金额
        model.setProductCode("QUICK_MSECURITY_PAY"); //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        request.setBizModel(model);
        String url = paymentPlugin.getAttribute("url");
        request.setNotifyUrl(url);  //回调地址
        String orderStr = "";
        try {
                //这里和普通的接口调用不同，使用的是sdkExecute
        	    logger.info("给支付宝传递数据：{}",request.toString());
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                orderStr = response.getBody();
                logger.info("支付宝返回数据：{}",orderStr);
                
            } catch (AlipayApiException e) {
            	 e.printStackTrace();
            	throw new RuntimeException("支付宝支付错误！");
               
        }
        return orderStr;
	}

	@Override
	public void postPayHandle(PaymentPlugin paymentPlugin, String paymentDescription, String extra,
			boolean isPaySuccess) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean veritySign(Map<String, Object> map, PaymentPlugin paymentPlugin) {
		// TODO Auto-generated method stub
		return false;
	}

}
