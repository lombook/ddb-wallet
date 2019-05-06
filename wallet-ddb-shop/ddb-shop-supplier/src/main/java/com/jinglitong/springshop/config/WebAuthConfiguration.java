package com.jinglitong.springshop.config;

import com.jinglitong.springshop.auth.Http403AccessDeniedHandler;
import com.jinglitong.springshop.auth.SkipPathRequestMatcher;
import com.jinglitong.springshop.filter.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName com.jinglitong.springshop.config.com.jinglitong.springshop.config.WebAuthConfiguration
 * @Description 配置spring security 将上述的拦截器配置到spring security 的拦截器链上 和拦截规则
 * @Author zili.zong
 * @Date 2018/11/19 11:00
 * @Version 1.0
 **/

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthConfiguration extends WebSecurityConfigurerAdapter {

    private final String[] matcherStr = {"/supplier/login","/*swagger*/**","/webjars/**","/v2/**","/configuration/**"};

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * desc 覆盖此方法是解决AuthenticationManager 无法注入问题
     * @return
     * @throws Exception
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthTokenFilter authenticationTokenFilterBean() throws Exception {
        //允许匿名访问 swagger
        //跳过的验证的地址
        List<String> pathsToSkip = Arrays.asList(matcherStr);
        List<String> processingPath = Arrays.asList("/**","/logout");//处理的请求
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, processingPath);
        AuthTokenFilter filter = new AuthTokenFilter(matcher);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new Http403AccessDeniedHandler();
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()// 使用redis 不用CSRF 自带的跨域处理，设置disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //设置不用Spring Security 创建session
                .and().authorizeRequests()
                //放行
                .antMatchers(matcherStr).permitAll()
                //需要ADMIN角色权限
                .antMatchers("/admin/**").hasRole("ADMIN")
                //需要USER角色权限
                .antMatchers("/**").hasRole("USER")
                //需要ADMIN或USER角色权限
                //.antMatchers("/order/**").access("hasRole('ADMIN') and hasRole('USER')")
                //需要127.0.0.1/32IP段的IP访问
                //.antMatchers("/public/**").access("hasIpAddress('127.0.0.1/32')  or  hasIpAddress('172.19.0.1/32')")
                //验证所有的请求除了放行的外
                //.antMatchers("/**").authenticated()
                .and().addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(getAccessDeniedHandler());
        httpSecurity.cors().configurationSource(corsConfigurationSource());//添加 CORS 支持
        httpSecurity.logout().disable();
        // disable page caching
        httpSecurity.headers().cacheControl().disable();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }


}