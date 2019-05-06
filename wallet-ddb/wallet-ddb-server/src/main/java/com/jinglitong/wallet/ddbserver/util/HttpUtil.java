package com.jinglitong.wallet.ddbserver.util;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("deprecation")
@Slf4j
public class HttpUtil {
    private static PoolingHttpClientConnectionManager connMgr;  
    private static RequestConfig requestConfig;  
    private static final int MAX_TIMEOUT = 7000;  
  
    static {  
        // 设置连接池  
        connMgr = new PoolingHttpClientConnectionManager();  
        // 设置连接池大小  
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());  
  
        RequestConfig.Builder configBuilder = RequestConfig.custom();  
        // 设置连接超时  
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时  
        configBuilder.setSocketTimeout(MAX_TIMEOUT);  
        // 设置从连接池获取连接实例的超时  
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);  
        // 在提交请求之前 测试连接是否可用  
        //configBuilder.setStaleConnectionCheckEnabled(true);  
        requestConfig = configBuilder.build();  
    }
  public static void main(String[] args) throws Exception{
	  /**
	   * https post json请求例子
	   */
	  //1.把请求参数放入map
	   Map<String , Object> param = new HashMap<>();
	   param.put("flowId", "16864564642313");
	   param.put("ruleId", "3");
	   param.put("shopTrade", "132");
	   param.put("productNum", "2");
	   param.put("price", "200");
	   param.put("userId", "0027f2f99a8c4f99b57f5091d2a85cf0");
	   param.put("nonceStr", Math.round(Math.random()*20));
	   String key=MD5Utils.makeSign(JSON.toJSONString(param));//2、对请求参数加密
	   param.put("sign", key);
	 
	   System.out.println("请求参数:"+JSON.toJSONString(param));//3、带加密的请求参数
	   //String  resultStr =sendHttpsPostJson("https://gateway.dabtland.cn/wallet-customer/customer/common/provinces.json", JSON.toJSONString(param));
	  // System.out.println("返回结果："+resultStr);
  
  }

	/**
	 * 接收conroller例子
	 */
/*	@RequestMapping("/test.json")
	public String test(@RequestBody String resultStr) throws Exception {
		Map<String, Object> m = MD5Utils.json2Map(resultStr);//json 转map
		String oldKey = (String) m.get("sign");
		m.remove("sign");//map去掉sign
		String key = MD5Utils.makeSign(JSON.toJSONString(m));//对参数加密
		if(oldKey.equals(key)) {//比较加密串是否一致
			 
		}
		return null;
	}*/
	
	
    private static void closeHttp(HttpResponse response){
    	try{
    		if(response != null){
    			EntityUtils.consume(response.getEntity());
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    /** 
     * 发送 POST 请求（HTTP），K-V形式 
     * 
     * @param apiUrl API接口URL 
     * @param params 参数map 
     * @return 
     */  
    public static String sendPost(String apiUrl, Map<String, Object> params) {  
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connMgr).build();  
        String httpStr = null;  
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;  
  
        try {  
            httpPost.setConfig(requestConfig);  
            List<NameValuePair> pairList = new ArrayList<>(params.size());  
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));  
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            log.info(String.valueOf(response.getStatusLine().getStatusCode()));
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {
        	closeHttp(response);
        	httpPost.releaseConnection();
        }  
        return httpStr;  
    }  
  
    /** 
     * 发送 POST 请求（HTTP），JSON形式 
     * 
     * @param apiUrl 
     * @param json   json对象 
     * @return 
     */  
    public static String sendPostJson(String apiUrl, Object json) {  
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connMgr).build(); 
        String httpStr = null;  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  

        try {  
            httpPost.setConfig(requestConfig);  
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");//解决中文乱码问题  
            stringEntity.setContentEncoding("UTF-8");  
            stringEntity.setContentType("application/json");  
            httpPost.setEntity(stringEntity);  
            response = httpClient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
            log.info(String.valueOf(response.getStatusLine().getStatusCode()));  
            httpStr = EntityUtils.toString(entity, "UTF-8");  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
        	closeHttp(response);
        	httpPost.releaseConnection();
        }  
        return httpStr;  
    }  
  
    /** 
     * 发送 SSL POST 请求（HTTPS），JSON形式 
     * @param apiUrl API接口URL 
     * @param json   JSON对象 
     * @return 
     */  
    public static String sendHttpsPostJson(String apiUrl, Object json) {  
    	CloseableHttpClient httpClient = createSSLClientDefault();  
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;  
        String httpStr = null;  
  
        try {
        	httpPost.setHeader("appId", "BdB2TPx26i5oKREBFjWv3bWhf6e7wvXc");
            httpPost.setConfig(requestConfig);  
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");//解决中文乱码问题  
            stringEntity.setContentEncoding("UTF-8");  
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);  
            int statusCode = response.getStatusLine().getStatusCode();  
            if (statusCode != HttpStatus.SC_OK) {  
                return null;
            }  
            HttpEntity entity = response.getEntity();  
            if (entity == null) {  
                return null;  
            }  
            httpStr = EntityUtils.toString(entity, "utf-8");  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
        	closeHttp(response);
        	httpPost.releaseConnection();
        }
        return httpStr;  
    }  
    
    @SuppressWarnings("deprecation")
	public static CloseableHttpClient createSSLClientDefault(){  
        try {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,  
                    new TrustStrategy() {  
                        // 信任所有  
                        public boolean isTrusted(X509Certificate[] chain,  
                                String authType) throws CertificateException {  
                            return true;  
                        }  
                    }).build();
            // ALLOW_ALL_HOSTNAME_VERIFIER:这个主机名验证器基本上是关闭主机名验证的,实现的是一个空操作，并且不会抛出javax.net.ssl.SSLException异常。  
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
                    sslContext, new String[] { "TLSv1" }, null,  
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();  
        } catch (KeyManagementException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (KeyStoreException e) {  
            e.printStackTrace();  
        }
        return  HttpClients.createDefault();  
    }
}
