package com.jinglitong.wallet.reportserver;//特别注意，下面的是 tk.MapperScan


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tk.mybatis.spring.annotation.MapperScan;


@EnableWebMvc
@SpringBootApplication
@MapperScan(basePackages = "com.jinglitong.wallet.reportserver.mapper")
@EnableScheduling
@EnableFeignClients(basePackages = {"com.jinglitong.wallet.ddbreportserver","com.jinglitong.wallet.api"})
@EnableCaching
@EnableDiscoveryClient
@EnableHystrix
@EnableTransactionManagement
public class DDBReportServerApplication extends SpringBootServletInitializer implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(DDBReportServerApplication.class);

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(DDBReportServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("服务启动完成!");
    }





}
