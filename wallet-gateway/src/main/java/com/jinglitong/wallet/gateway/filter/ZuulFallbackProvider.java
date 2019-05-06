package com.jinglitong.wallet.gateway.filter;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * The class Uac fallback provider.
 *
 * @author paascloud.net@gmail.com
 */
@Component
@Slf4j
public class ZuulFallbackProvider implements FallbackProvider {


	@Override
	public String getRoute() {
		return "*";
	}


	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		if (cause instanceof HystrixTimeoutException) {
			return response(HttpStatus.GATEWAY_TIMEOUT);
		} else {
			cause.printStackTrace();
			return response(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ClientHttpResponse response(final HttpStatus status) {
		return new ClientHttpResponse() {
			@Override
			public HttpStatus getStatusCode() {
				return status;
			}

			@Override
			public int getRawStatusCode() {
				return status.value();
			}

			@Override
			public String getStatusText() {
				return status.getReasonPhrase();
			}

			@Override
			public void close() {
//				log.info("AAAAAAAAA");
			}

			@Override
			public InputStream getBody() {
				String message = "{\n" +
						"\"code\": 200,\n" +
						"\"message\": \"微服务故障, 请稍后再试\"\n" +
						"}";
				return new ByteArrayInputStream(message.getBytes());
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return headers;
			}
		};
	}


}
