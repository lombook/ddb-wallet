package com.jinglitong.wallet.job;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tk.mybatis.spring.annotation.MapperScan;

@EnableWebMvc
@SpringBootApplication
@MapperScan(basePackages = "com.jinglitong.wallet.job.mapper")
@EnableScheduling
@EnableFeignClients(basePackages = {"com.jinglitong.wallet.job","com.jinglitong.wallet.api","com.jinglitong.wallet.ddbapi"})
@EnableCaching
@EnableDiscoveryClient
@EnableHystrix
@EnableTransactionManagement
public class WalletJobApplication {




	public static void main(String[] args) {
		SpringApplication.run(WalletJobApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}


//
//	@Bean
//	public CorsFilter corsFilter() {
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		final CorsConfiguration config = new CorsConfiguration();
//		config.setAllowCredentials(true); // 允许cookies跨域
//		config.addAllowedOrigin(CorsConfiguration.ALL);// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
//		config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
//		config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
//		config.addAllowedMethod("OPTIONS");// 允许提交请求的方法，*表示全部允许
//		config.addAllowedMethod("HEAD");
//		config.addAllowedMethod("GET");// 允许Get的请求方法
//		config.addAllowedMethod("PUT");
//		config.addAllowedMethod("POST");
//		config.addAllowedMethod("DELETE");
//		config.addAllowedMethod("PATCH");
//		source.registerCorsConfiguration("/**", config);
//		return new CorsFilter(source);
//	}



}


