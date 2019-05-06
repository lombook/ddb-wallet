package com.jinglitong.wallet.job.service;

import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpClient {

	public static String client(String url,HttpMethod method,MultiValueMap<String, String> params){
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = template.getForEntity(url, String.class);
		return responseEntity.getBody();
	}
}
