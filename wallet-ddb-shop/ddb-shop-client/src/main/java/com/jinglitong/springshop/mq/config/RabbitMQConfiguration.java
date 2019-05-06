/*package com.jinglitong.springshop.mq.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix="spring.rabbitmq")
public class RabbitMQConfiguration {

	@Value("spring.rabbitmq.host")
	private String host;
	
	@Value("spring.rabbitmq.port")
	private String port;
	
	@Value("spring.rabbitmq.virtualHost")
	private String virtualHost;
	
	@Value("spring.rabbitmq.userName")
	private String userName;
	
	@Value("spring.rabbitmq.password")
	private String password;

	@Bean
	public ConnectionFactory connectionFactory() {
		log.info("建立rabbitmq连接......");
		int p = Integer.valueOf(port);
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host,p);
		cachingConnectionFactory.setUsername(userName);
		cachingConnectionFactory.setPassword(password);
		cachingConnectionFactory.setVirtualHost(virtualHost);
		cachingConnectionFactory.setPublisherConfirms(true);
		return cachingConnectionFactory;
	}

	@Bean
	@Scope("prototype")
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		return rabbitTemplate;
	}
}*/