/*
 * Copyright 2005-2017 jingtum.com. All rights reserved.
 * Support: http://www.jingtum.com
 * License: http://www.jingtum.com/license
 */
package com.jinglitong.springshop.plugin.weixinNativePayment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.jinglitong.springshop.entity.Paymenttransaction;
import com.jinglitong.springshop.plugin.PaymentPlugin;
import com.jinglitong.springshop.util.WebUtils;
import com.jinglitong.springshop.util.XmlUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Plugin - 微信支付(扫码支付)
 * 
 * @author JMALL Team
 * @version 0.0.1
 */
@Component("WX_APP_PAY")
public class WeixinNativePaymentPlugin extends PaymentPlugin {

	/**
	 * code_url请求URL
	 */
	private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	@Override
	public String payHandle(PaymentPlugin paymentPlugin, Paymenttransaction p,
			String paymentDescription, String extra) {
		//这里是微信统一下单的过程
        SortedMap<String, String> sortedMap = new TreeMap<>();
        sortedMap.put("appid", paymentPlugin.getAttribute("appId"));
        sortedMap.put("body",paymentDescription);//商品描述
        sortedMap.put("mch_id", paymentPlugin.getAttribute("mchId"));//商家账号
        sortedMap.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));//随机字符串
        sortedMap.put("notify_url", paymentPlugin.getAttribute("notifyUrl"));//回调地址
        sortedMap.put("out_trade_no", p.getZid());//商户订单号
        InetAddress ia=null;
        try {
			ia=ia.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "error";
		}       
        sortedMap.put("spbill_create_ip", ia.getHostAddress());//消费ip
        sortedMap.put("total_fee", String.valueOf((p.getAmount().multiply(new BigDecimal(100)).intValue())));//消费金额
        sortedMap.put("trade_type", "APP");//交易类型app
        String sign = createSign(sortedMap,paymentPlugin);
        sortedMap.put("sign", sign);//数字签证
        String requestXml = toXml(sortedMap);
            //调用微信接口
            String responseXml = WebUtils.post(UNIFIED_ORDER_URL, requestXml);
            //解析xml为map
            SortedMap<String, String> responseMap = null;
			try {
				responseMap = XmlUtils.fromXml(responseXml);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error";
			}
            SortedMap<String, String> payMap = new TreeMap<>();
            payMap.put("appid", responseMap.get("appid"));
            payMap.put("partnerid", responseMap.get("mch_id"));
            payMap.put("prepayid", responseMap.get("prepay_id"));
            payMap.put("package", "Sign=WXPay");
            payMap.put("noncestr", UUID.randomUUID().toString().replace("-", ""));
            payMap.put("timestamp", String.valueOf((int) (System.currentTimeMillis() / 1000)));
            String paySign = createSign(payMap,paymentPlugin);
            payMap.put("sign", paySign);
                       
   		    JSONObject json=new JSONObject(payMap);
            return json.toString();

	}


	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	public static String createSign(SortedMap<String, String> values,PaymentPlugin paymentPlugin) {
        String stringSignTemp = toStringSignTemp(values);
        stringSignTemp += "&key=" + paymentPlugin.getAttribute("apiKey");
        //logger.info("create sign stringSignTemp = " + stringSignTemp);
        return getMD5(stringSignTemp).toUpperCase();
    }
	
	public static boolean veritySign(SortedMap<String, String> map,PaymentPlugin paymentPlugin)  {
        List<String> keys = new ArrayList<>(map.keySet());
        SortedMap<String, String> sortedMap = new TreeMap<>();
        for (String key : keys) {
            String value = map.get(key);
            if ("sign".equals(key)) {
                continue;
            }
            sortedMap.put(key, value);
        }
        String sign = createSign(sortedMap,paymentPlugin);
        if (sign.equalsIgnoreCase(map.get("sign"))) {
            return true;
        }
        return false;
    }
	public static String getMD5(String str) {
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            StringBuilder sbu = new StringBuilder();
            byte[] out = new byte[0];
            try {
                out = md5.digest(str.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (byte b : out) {
                int o = b & 0xFF;
                if (o < 16) {
                    sbu.append("0");
                }
                sbu.append(Integer.toHexString(o));
            }
            result = sbu.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
	 public static String toStringSignTemp(SortedMap<String, String> values) {
	        StringBuffer buff = new StringBuffer();
	        Iterator<Entry<String, String>> iter = values.entrySet().iterator();
	        while (iter.hasNext()) {
	            Entry<String, String> entry = iter.next();
	            String key = entry.getKey();
	            String value = entry.getValue();
	            if (!isEmpty(value)) ;
	            buff.append(key + "=" + value + "&");
	        }
	        return buff.toString().substring(0, buff.length() - 1);
	    }
	 public static boolean isEmpty(Object str) {
	        return (str == null || "".equals(str) || "".equals(str.toString().trim()) || "undefined".equals(str));
	    }
	 
	 public static String toXml(SortedMap<String, String> values) {
	        StringBuffer xml = new StringBuffer();
	        xml.append("<xml>");
	        Iterator<Map.Entry<String, String>> iter = values.entrySet().iterator();
	        while (iter.hasNext()) {
	            Map.Entry<String, String> entry = iter.next();
	            String key = entry.getKey();
	            Object value = entry.getValue();
	            if ("notify_url".equals(key)) {
	                xml.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
	            } else {
	                xml.append("<" + key + ">" + value + "</" + key + ">");
	            }
	        }
	        xml.append("</xml>");
	        return xml.toString();
	    }

	/**
	 * 生成二维码图片
	 * 
	 * @param text
	 *            内容
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @return 二维码图片
	 */
	public BufferedImage generateQrcode(String text, int width, int height) {
		int WHITE = 0xFFFFFFFF;
		int BLACK = 0xFF000000;
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 0);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
				}
			}
			return image;
		} catch (WriterException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

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
	public void postPayHandle(PaymentPlugin paymentPlugin, String paymentDescription, String extra,
			boolean isPaySuccess) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean veritySign(Map<String, Object> map, PaymentPlugin paymentPlugin) {
		List<String> keys = new ArrayList<>(map.keySet());
        SortedMap<String, String> sortedMap = new TreeMap<>();
        for (String key : keys) {
            String value = (String) map.get(key);
            if ("sign".equals(key)) {
                continue;
            }
            sortedMap.put(key, value);
        }
        String sign = createSign(sortedMap,paymentPlugin);
        if (sign.equalsIgnoreCase((String) map.get("sign"))) {
            return true;
        }
        return false;
	}

}