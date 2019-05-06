package com.jinglitong.springshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;


/**
 * @ClassName ShopBusinessApp
 * @Description 商铺启动入口
 * @Author zili.zong
 * @Date 2019/1/7 13:47
 * @Version 1.0
 **/
@SpringBootApplication
@MapperScan("com.jinglitong.springshop.mapper")
public class ShopBusinessApp {
    public static void main(String[] args){
        SpringApplication.run(ShopBusinessApp.class,args);
        System.out.println("ShopBusinessApp started....");
    }
}
