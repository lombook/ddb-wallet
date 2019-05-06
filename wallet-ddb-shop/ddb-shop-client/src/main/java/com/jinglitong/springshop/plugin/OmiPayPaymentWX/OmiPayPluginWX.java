package com.jinglitong.springshop.plugin.OmiPayPaymentWX;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.jinglitong.springshop.entity.Paymenttransaction;
import com.jinglitong.springshop.plugin.PaymentPlugin;
import com.jinglitong.springshop.util.WebUtils;

@Component("OMI_APP_PAY_WX")
public class OmiPayPluginWX extends PaymentPlugin{

	@Override
	public int compareTo(PaymentPlugin arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "0";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getSiteUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void postPayHandle(PaymentPlugin paymentPlugin, String paymentDescription, String extra,
			boolean isPaySuccess) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prePayHandle(PaymentPlugin paymentPlugin, Paymenttransaction paymentTransaction,
			String paymentDescription, String extra) throws Exception {
		// 根据支付事务中的订单号，获得订单信息，生成支付事务
		
		
	}
	
	@Override
	public String payHandle(PaymentPlugin paymentPlugin, Paymenttransaction p,
			String paymentDescription, String extra) {
		//组装支付参数   ps:这里用extra表示支付平台时支付宝还是微信，paymentDescription表示主订单sn号
		Map<String,Object> map =  preparParameters(p.getZid(), p.getAmount(), p.getCurrencyId(), paymentPlugin,"捎袋手-订单编号" + paymentDescription , extra);	
		logger.info("post to omipay:" + map);
		String res = WebUtils.get("https://www.omipay.com.cn/omipay/api/v2/MakeAPPOrder",map);
		logger.info("receive from omipay:" + res);
		return res;
		
		
	}
	
	/**
     * OmiPay组装预下单参数接口
     *
     * @param tradeNo  订单编号
     * @param money
     * @param ip
     * @return
     * @throws
     */
    private  Map<String, Object> preparParameters(String tradeNo, BigDecimal amount,String currency, PaymentPlugin paymentPlugin,String  name,String plat) {    
        SortedMap<String, Object> sortedMap = new TreeMap<>();
        if(paymentPlugin.getId().equals("OMI_APP_PAY_WX")){
        	sortedMap.put("app_id", paymentPlugin.getAttribute("appId"));
        }       
        sortedMap.put("order_name",name);//商品描述或订单描述
        sortedMap.put("currency", paymentPlugin.getAttribute("currency"));
       // sortedMap.put("currency", "AUD");//币种
       // sortedMap.put("amount", amount.multiply(new BigDecimal("100")).intValue());//随机字符串
        sortedMap.put("amount", amount.multiply(new BigDecimal(100)).intValue());//随机字符串
        sortedMap.put("notify_url", paymentPlugin.getAttribute("notifyUrl"));//回调地址
        sortedMap.put("out_order_no", tradeNo);//订单sn
        sortedMap.put("platform", plat);//ALIPAYONLINE:支付宝        WECHATPAY:微信
        sortedMap.put("m_number", paymentPlugin.getAttribute("m_number"));//下单门店编号
        sortedMap.put("timestamp", System.currentTimeMillis());
        sortedMap.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));//随机字符串
        String sign = createSign(sortedMap,paymentPlugin);
        sortedMap.put("sign", sign);//数字签证      
        sortedMap.put("secret_key", paymentPlugin.getAttribute("secret_key"));//数字签证      
        return sortedMap;
    }
    
    /**
     * 数字签证
     * @param values
     * @return
     */
    private  String createSign(Map<String, Object> values,PaymentPlugin paymentPlugin) {

        String stringSignTemp = (String) values.get("m_number") + "&" + values.get("timestamp")+"&" + values.get("nonce_str") + "&" + paymentPlugin.getAttribute("secret_key");
       
        return getMD5(stringSignTemp).toUpperCase();
    }
    
    /**
     * 获取字符串的MD5值
     *
     * @param str
     * @return
     */
    private  String getMD5(String str) {
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
    
    public boolean veritySign(Map<String, Object> map,PaymentPlugin paymentPlugin)  {
        map.put("m_number", paymentPlugin.getAttribute("m_number"));
        String sign = createSign(map,paymentPlugin);
        if (sign.equalsIgnoreCase((String)(map.get("sign")))) {
            return true;
        }
        return false;
    }

}
