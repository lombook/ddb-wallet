package com.jinglitong.springshop.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SwaggerConfiguration
 * @Description TODO
 * @Author zili.zong
 * @Date 2018/11/19 11:05
 * @Version 1.0
 **/
@Configuration
@ConditionalOnProperty(prefix = "swagger",value = {"enable"},havingValue = "true")
@EnableSwagger2
public class SwaggerConfiguration implements ApplicationContextAware {
    @Autowired
    private Environment environment;
    @Value("${swagger.host}")
    private String host;
    @Bean
    public Docket createRestApi() {
        Docket docket =  new Docket(DocumentationType.SWAGGER_2);
        docket.groupName(this.environment.getProperty("info.name")).apiInfo(apiInfo());
        docket.globalOperationParameters(setHeaderToken());
        setHeaderToken();
        if(host!=null){
            docket.host(host);
        }
        ParameterBuilder aParameterBuilder1 = new ParameterBuilder();
        aParameterBuilder1.name("X-Auth-Token").description("在令牌字段中输入用于身份验证的令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        ParameterBuilder aParameterBuilder2 = new ParameterBuilder();
        aParameterBuilder2.name("clientId").description("客户端ID").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder1.build());
        aParameters.add(aParameterBuilder2.build());
        return docket.apiInfo(apiInfo()).useDefaultResponseMessages(false).globalOperationParameters(aParameters)
                .select().apis(RequestHandlerSelectors.basePackage("com.jinglitong.springshop.controller")).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(environment.getProperty("info.component"))
                .version("v1.0.0")
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //force the bean to get loaded as soon as possible
        applicationContext.getBean("requestMappingHandlerAdapter");
    }
    private List<Parameter> setHeaderToken() {
//		ParameterBuilder tokenPar = new ParameterBuilder();
//		List<Parameter> pars = new ArrayList<>();
//		tokenPar.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
//		pars.add(tokenPar.build());
//
////		ParameterBuilder langPar = new ParameterBuilder();
////		langPar.name("lang").description("语言").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
////		pars.add(langPar.build());
//		return pars;
        return null;
    }
}
