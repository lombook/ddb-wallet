package com.jinglitong.springshop;

import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName ShopWebApp
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/7 13:51
 * @Version 1.0
 **/
@SpringBootApplication
@MapperScan("com.jinglitong.springshop.mapper")
@EnableScheduling
public class ShopWebApp {
    public static void main(String[] args){
        SpringApplication.run(ShopWebApp.class,args);
        System.out.println("server started........");
    }
}
