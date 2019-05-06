package com.jinglitong.springshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName ShopKJApp
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/2/27 18:53
 * @Version 1.0
 **/
@SpringBootApplication
@MapperScan("com.jinglitong.springshop.mapper")
@EnableScheduling
public class ShopKJApp {
    public static void main(String[] args){
        SpringApplication.run(ShopKJApp.class,args);
        System.out.println("ShopKJApp started....");
    }
}
