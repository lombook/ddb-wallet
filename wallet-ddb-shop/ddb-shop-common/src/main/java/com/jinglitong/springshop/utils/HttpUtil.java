package com.jinglitong.springshop.utils;

import com.alibaba.fastjson.JSON;
import com.jinglitong.springshop.md5.MD5Utils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
	   param.put("logisticsId", "5");
	   param.put("orderId", "755c866474a34e1eadfacc8eeb5f0180");
	  // param.put("hashCode", "dasldhasdjkasdj");
	   param.put("logisticsOrderSn", "31231231231");
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
    
    
    public static String sendGetRequest(String url) throws URIException, MalformedURLException {  
        return sendGetRequest(url, new HashMap<String, Object>(),false);  
    }  
  
    /** 
     * 发送 GET 请求（HTTP），K-V形式 
     * 
     * @param url 
     * @param params 
     * @return 
     * @throws MalformedURLException 
     * @throws URIException 
     */  
    public static String sendGetRequest(String url, Map<String, Object> params,boolean isHttps) throws MalformedURLException, URIException {  
        HttpResponse response=null;  
        String apiUrl = url;  
        StringBuffer param = new StringBuffer();  
        int i = 0;  
        for (String key : params.keySet()) {  
            if (i == 0)  
                param.append("?");  
            else  
                param.append("&");  
            param.append(key).append("=").append(params.get(key));  
            i++;  
        }
        apiUrl += param;  
        String result = null;  
        CloseableHttpClient httpclient =null;  
        if(isHttps){  
            httpclient=createSSLClientDefault();  
        }else {
            httpclient=HttpClients.createDefault();  
        }  
        URL urls = new URL(apiUrl);
        URI uri = new URI(urls.getProtocol(), urls.getHost(), urls.getPath(), urls.getQuery(), null);
        HttpGet httpGet = new HttpGet(uri.toString());
        try {
            response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();  
            if (entity != null) {
                //InputStream instream = entity.getContent();
                //result = IOUtils.toString(instream, "UTF-8");
                result=EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeHttp(response);
            httpGet.releaseConnection();
        }
        return result;
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
