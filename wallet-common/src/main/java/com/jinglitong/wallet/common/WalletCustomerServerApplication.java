//package com.jinglitong.wallet.common;//特别注意，下面的是 tk.MapperScan
//
//import com.jinglitong.wallter.customerserver.conf.JingtongWalletProperty;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import tk.mybatis.spring.annotation.MapperScan;
//
//import javax.servlet.MultipartConfigElement;
//
//
//@EnableWebMvc
//@SpringBootApplication
//@MapperScan(basePackages = "com.jingtum.j.mapper")
//@EnableConfigurationProperties(JingtongWalletProperty.class)
//@EnableFeignClients(basePackages = {"com.jingtum.wallter"})
//@EnableCaching
//@EnableScheduling
//public class WalletCustomerServerApplication extends SpringBootServletInitializer implements CommandLineRunner {
//    private Logger logger = LoggerFactory.getLogger(WalletCustomerServerApplication.class);
//
//    public static void main(String[] args) {
//        System.setProperty("spring.devtools.restart.enabled", "false");
//        SpringApplication.run(WalletCustomerServerApplication.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        logger.info("服务启动完成!");
//    }
//
//
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
//
//
//
//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setLocation("/app/pttms/tmp");
//        return factory.createMultipartConfig();
//    }
//
//}
