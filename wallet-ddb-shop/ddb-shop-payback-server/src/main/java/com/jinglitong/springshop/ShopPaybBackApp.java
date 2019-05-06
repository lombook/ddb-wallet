package com.jinglitong.springshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import tk.mybatis.spring.annotation.MapperScan;


/**
 * @ClassName ShopPaybBackApp
 * @Description 支付回调启动入口
 * @Author zili.zong
 * @Date 2019/1/7 13:47
 * @Version 1.0
 **/
@SpringBootApplication
@MapperScan("com.jinglitong.springshop.mapper")
public class ShopPaybBackApp {
    public static void main(String[] args){
        SpringApplication.run(ShopPaybBackApp.class,args);
        System.out.println("ShopPaybBackApp started....");
    }
}
