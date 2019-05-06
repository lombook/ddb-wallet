/*
 * Copyright 2005-2017 jingtum.com. All rights reserved.
 * Support: http://www.jingtum.com
 * License: http://www.jingtum.com/license
 */
package com.jinglitong.springshop.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Utils - Web
 * 
 * @author JMALL Team
 * @version 0.0.1
 */
public final class WebUtils {

	/**
	 * PoolingHttpClientConnectionManager
	 */
	private static final PoolingHttpClientConnectionManager HTTP_CLIENT_CONNECTION_MANAGER;

	/**
	 * CloseableHttpClient
	 */
	private static final CloseableHttpClient HTTP_CLIENT;

	static {
		HTTP_CLIENT_CONNECTION_MANAGER = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build());
		HTTP_CLIENT_CONNECTION_MANAGER.setDefaultMaxPerRoute(100);
		HTTP_CLIENT_CONNECTION_MANAGER.setMaxTotal(200);
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000).setConnectTimeout(60000).setSocketTimeout(60000).build();
		HTTP_CLIENT = HttpClientBuilder.create().setConnectionManager(HTTP_CLIENT_CONNECTION_MANAGER).setDefaultRequestConfig(requestConfig).build();
	}

	/**
	 * 不可实例化
	 */
	private WebUtils() {
	}

	/**
	 * 获取HttpServletRequest
	 * 
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return requestAttributes != null && requestAttributes instanceof ServletRequestAttributes ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
	}

	/**
	 * 获取HttpServletResponse
	 * 
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return requestAttributes != null && requestAttributes instanceof ServletRequestAttributes ? ((ServletRequestAttributes) requestAttributes).getResponse() : null;
	}

	/**
	 * 判断是否为AJAX请求
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 是否为AJAX请求
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		Assert.notNull(request);

		return StringUtils.equalsIgnoreCase(request.getHeader("X-Requested-With"), "XMLHttpRequest");
	}

	/**
	 * 移除cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param name
	 *            Cookie名称
	 * @param path
	 *            路径
	 * @param domain
	 *            域
	 */
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name, String path, String domain) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);

		try {
			name = URLEncoder.encode(name, "UTF-8");
			Cookie cookie = new Cookie(name, null);
			cookie.setMaxAge(0);
			if (StringUtils.isNotEmpty(path)) {
				cookie.setPath(path);
			}
			if (StringUtils.isNotEmpty(domain)) {
				cookie.setDomain(domain);
			}
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 移除cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param name
	 *            Cookie名称
	 */


	/**
	 * 参数解析
	 * 
	 * @param query
	 *            查询字符串
	 * @param encoding
	 *            编码格式
	 * @return 参数
	 */
	public static Map<String, String> parse(String query, String encoding) {
		Assert.hasText(query);

		Charset charset;
		if (StringUtils.isNotEmpty(encoding)) {
			charset = Charset.forName(encoding);
		} else {
			charset = Charset.forName("UTF-8");
		}
		List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(query, charset);
		Map<String, String> parameterMap = new HashMap<>();
		for (NameValuePair nameValuePair : nameValuePairs) {
			parameterMap.put(nameValuePair.getName(), nameValuePair.getValue());
		}
		return parameterMap;
	}

	/**
	 * 解析参数
	 * 
	 * @param query
	 *            查询字符串
	 * @return 参数
	 */
	public static Map<String, String> parse(String query) {
		Assert.hasText(query);

		return parse(query, null);
	}

	/**
	 * 重定向
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param url
	 *            URL
	 * @param contextRelative
	 *            是否相对上下文路径
	 * @param http10Compatible
	 *            是否兼容HTTP1.0
	 */
	public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url, boolean contextRelative, boolean http10Compatible) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(url);

		StringBuilder targetUrl = new StringBuilder();
		if (contextRelative && url.startsWith("/")) {
			targetUrl.append(request.getContextPath());
		}
		targetUrl.append(url);
		String encodedRedirectURL = response.encodeRedirectURL(targetUrl.toString());
		if (http10Compatible) {
			try {
				response.sendRedirect(encodedRedirectURL);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		} else {
			response.setStatus(303);
			response.setHeader("Location", encodedRedirectURL);
		}
	}

	/**
	 * 重定向
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param url
	 *            URL
	 */
	public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) {
		sendRedirect(request, response, url, true, true);
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 *            URL
	 * @param parameterMap
	 *            请求参数
	 * @return 返回结果
	 */
	public static String post(String url, Map<String, Object> parameterMap) {
		Assert.hasText(url);

		String result = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			if (parameterMap != null) {
				for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
			try {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					result = EntityUtils.toString(httpEntity);
					EntityUtils.consume(httpEntity);
				}
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * GET请求
	 * 
	 * @param url
	 *            URL
	 * @param parameterMap
	 *            请求参数
	 * @return 返回结果
	 */
	public static String get(String url, Map<String, Object> parameterMap) {
		Assert.hasText(url);

		String result = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			if (parameterMap != null) {
				for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			String url1 = url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpGet httpGet = new HttpGet(url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
			CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpGet);
			try {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					result = EntityUtils.toString(httpEntity);
					EntityUtils.consume(httpEntity);
				}
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 *            URL
	 * @param xml
	 *            XML
	 * @return 返回结果
	 */
	public static String post(String url, String xml) {
		Assert.hasText(url);

		String result = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(xml, "UTF-8"));
			CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
			try {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					result = EntityUtils.toString(httpEntity, "UTF-8");
					EntityUtils.consume(httpEntity);
				}
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 获得客户端真实IP
	 * @return
	 */
	public static String getClientRealIP() {
		HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }


	/**
	 * 测试POST
	 * @param url
	 * @param parameterMap
	 * @param Header
	 * @return
	 */
	public static String post(String url, Map<String, Object> parameterMap,Map<String,Object> Header) {
		Assert.hasText(url);

		String result = null;
		String json = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			if (parameterMap != null) {
				StringBuffer stringBuffer = new StringBuffer("{");
				for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					stringBuffer = stringBuffer.append('"' +name + '"' + ":\""   +value + "\",");
//					if (StringUtils.isNotEmpty(name)) {
//						nameValuePairs.add(new BasicNameValuePair(name, value));
//					}
				}
				json = stringBuffer.substring(0,stringBuffer.length()-1) + "}";
			}

			HttpPost httpPost = new HttpPost(url);
			//httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			StringEntity stringEntity = new StringEntity(json);
			httpPost.setEntity(stringEntity);
			if(Header != null){
				for (Map.Entry<String, Object> entry : Header.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						httpPost.setHeader(name,value);
					}
				}
			}
			CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
			try {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					result = EntityUtils.toString(httpEntity);
					EntityUtils.consume(httpEntity);
				}
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}
}