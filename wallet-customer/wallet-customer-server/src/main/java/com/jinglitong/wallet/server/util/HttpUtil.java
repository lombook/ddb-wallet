package com.jinglitong.wallet.server.util;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import lombok.extern.slf4j.Slf4j;

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
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

 
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
	   Map<String , Object> param = new HashMap<>();
	   param.put("flowId", UuidUtil.getUUID());
	   param.put("parentId", "你好");
	   param.put("sonId", "001");
	   param.put("r", "123456");
	   String key=MD5Utils.makeSign(JSON.toJSONString(param));
	   param.put("sign", key);
	 
	   System.out.println(JSON.toJSONString(param));
	  String  resultStr =sendPostJson("http://dadibao.fudeshu.com/app/ddb/userrel",  JSON.toJSONString(param));
	  System.out.println(resultStr);
     /* try {
        getFileRequest("https://sporttery-test-1256360884.cos.ap-beijing.myqcloud.com/img/1001/e49d9ef1-b53c-42fd-b348-b2c089be4477.jpg");
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }*/

	  
  }
    /** 
     * 发送 GET 请求（HTTP），不带输入数据 
     * 
     * @param url 
     * @return 
     */  
    public static String sendGetRequest(String url) {  
        return sendGetRequest(url, new HashMap<String, Object>(),false);  
    }  
  
    private static void closeHttp(HttpResponse response){
    	try{
    		if(response != null){
    			EntityUtils.consume(response.getEntity());
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public static InputStream getFileRequest(String url) throws Exception{
        HttpResponse response= null;
        InputStream instream = null;
        boolean isHttps = false;
        if(url.indexOf("https") > -1){
            isHttps = true;
        }
        
        CloseableHttpClient httpclient =null;  
        if(isHttps){
            httpclient=createSSLClientDefault();  
        }else {
            httpclient=HttpClients.createDefault();  
        }
        HttpGet httpGet = new HttpGet(url);
        try {
            response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();  
            if (entity != null) {
                byte[] data = EntityUtils.toByteArray(entity);
                instream = new ByteArrayInputStream(data); 
                System.out.println("-------"+instream.available());
            }
        } catch (Exception e) {
            throw e;
        }finally {
            closeHttp(response);
            httpGet.releaseConnection();
        }
        return instream;
    }
    
    /** 
     * 发送 GET 请求（HTTP），K-V形式 
     * 
     * @param url 
     * @param params 
     * @return 
     */  
    public static String sendGetRequest(String url, Map<String, Object> params,boolean isHttps) {  
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
        HttpGet httpGet = new HttpGet(apiUrl);
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
     * 发送 POST 请求（HTTP），不带输入数据 
     * 
     * @param apiUrl 
     * @return 
     */  
    public static String sendPost(String apiUrl) {  
        return sendPost(apiUrl, new HashMap<String, Object>());  
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
     * 发送 SSL POST 请求（HTTPS），K-V形式 
     * 
     * @param apiUrl API接口URL 
     * @param params 参数map 
     * @return 
     */  
    public static String sendPostSSL(String apiUrl, Map<String, Object> params) {  
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  
        String httpStr = null;  
  
        try {  
            httpPost.setConfig(requestConfig);  
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());  
            for (Map.Entry<String, Object> entry : params.entrySet()) {  
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry  
                        .getValue().toString());  
                pairList.add(pair);  
            }  
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));  
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
  
    /** 
     * 发送 SSL POST 请求（HTTPS），JSON形式 
     * 需要提前导入服务方证书
     * @param apiUrl API接口URL 
     * @param json   JSON对象 
     * @return 
     */  
    public static String sendPostJsonSSL(String apiUrl, Object json) {  
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).build();  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  
        String httpStr = null;  
        log.info("调用HTTPS接口开始执行---{}-{}",apiUrl,json);
        try {
            httpPost.setConfig(requestConfig);  
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");//解决中文乱码问题  
            stringEntity.setContentEncoding("UTF-8");  
            stringEntity.setContentType("application/json");  
            httpPost.setEntity(stringEntity);  
            response = httpClient.execute(httpPost);  
            log.info("接口返回内容为-----{}",response.toString());
            int statusCode = response.getStatusLine().getStatusCode();  
            log.info("结果码为-----{}",statusCode);
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
            log.error("调用接口异常-----{}",e);
        } finally {  
        	closeHttp(response);
        	httpPost.releaseConnection();
        }  
        return httpStr;  
    }  
    /** 
     * 发送 SSL POST 请求（HTTPS），JSON形式 
     * 无证书
     * @param apiUrl API接口URL 
     * @param json   JSON对象 
     * @return 
     */  
    public static String sendPostJsonSSLNoCert(String apiUrl, Object json) {  
    	CloseableHttpClient httpClient = createSSLClientDefault();  
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;  
        String httpStr = null;  
  
        try {
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
    /** 
     * 创建SSL安全连接 
     * 
     * @return 
     */  
    @SuppressWarnings({ "deprecation"})
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {  
        SSLConnectionSocketFactory sslsf = null;  
        try {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {  
  
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
                    return true;  
                }  
            }).build();  
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {  
  
                @Override
                public boolean verify(String arg0, SSLSession arg1) {  
                    return true;  
                }  
  
                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {  
                }  
  
                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {  
                }  
  
                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {  
                }  
            });  
        } catch (GeneralSecurityException e) {  
            e.printStackTrace();  
        }  
        return sslsf;  
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
