package com.jinglitong.wallet.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@EnableEurekaServer
@SpringBootApplication
public class WalletEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletEurekaApplication.class, args);
	}
}
