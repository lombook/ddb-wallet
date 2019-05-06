package com.jinglitong.springshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName ShopSupplierApp
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/4/8 11:29
 * @Version 1.0
 **/
@SpringBootApplication
@MapperScan("com.jinglitong.springshop.mapper")
public class ShopSupplierApp {
    public static void main(String[] args){
        SpringApplication.run(ShopSupplierApp.class,args);
        System.out.println("ShopSupplierApp started........");
    }
}
