package com.jinglitong.wallet.ddbserver;//特别注意，下面的是 tk.MapperScan


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.MultipartConfigElement;





@EnableWebMvc
@SpringBootApplication
@MapperScan(basePackages = "com.jinglitong.wallet.ddbserver.mapper")
@EnableScheduling
@EnableFeignClients(basePackages = {"com.jinglitong.wallet.ddbserver","com.jinglitong.wallet.api","com.jinglitong.wallet.ddbapi"})
@EnableCaching
@EnableDiscoveryClient
@EnableHystrix
@EnableTransactionManagement
public class WalletDDBServerApplication extends SpringBootServletInitializer implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(WalletDDBServerApplication.class);

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(WalletDDBServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("服务启动完成!");
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }



    @Value("${tomcat.tmp.upload.path}")
    private String tmpPath;

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(tmpPath);
        return factory.createMultipartConfig();
    }

}
