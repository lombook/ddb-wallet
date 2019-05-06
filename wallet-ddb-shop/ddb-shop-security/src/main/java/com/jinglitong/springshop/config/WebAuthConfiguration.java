package com.jinglitong.springshop.config;

import com.jinglitong.springshop.annotation.SkipPathRequestMatcher;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName WebAuthConfiguration
 * @Description 配置spring security 将上述的拦截器配置到spring security 的拦截器链上 和拦截规则
 * @Author zili.zong
 * @Date 2018/11/19 11:00
 * @Version 1.0
 **/

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthConfiguration extends WebSecurityConfigurerAdapter{
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
        List<String> pathsToSkip = Arrays.asList(
                "/user/login",
                "/user/reg",
                "/user/forgetPwd",
                "/business/login",
                "/appControl/getAppVersion",
                "/appProtocol/getProperties",
                "/common/**",
                "/product/**",
                "/cart/**",
                "/syn/**",
                "/omipay/**",
                "/customer/getNotices",
                "/customer/getNotice",
                "/swagger*//**",                   //swagger api json
                "/swagger-resources/configuration/ui",//用来获取支持的动作
                "/swagger-resources",                      //用来获取api-docs的URL
                "/swagger-resources/configuration/security",//安全选择
                "/swagger-ui.html",
                "/webjars/**",
                "/v2/**",
                "/configuration/**");               //跳过的验证的地址
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
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()// 使用redis 不用CSRF 自带的跨域处理，设置disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //设置不用Spring Security 创建session
                .and().authorizeRequests() //上个结束，and开始下一个
                .antMatchers("/user/login",
                		"/user/reg",
                		"/user/forgetPwd",
                        "/business/login",
                        "/appControl/getAppVersion",
                        "/appProtocol/getProperties",
                        "/product/**",
                        "/cart/**",
                        "/syn/**",
                        "/omipay/**", 
                        "/common/**",
                        "/customer/getNotices",
                        "/customer/getNotice",
                        "/swagger*//**",                   //swagger api json
                        "/swagger-resources/configuration/ui",//用来获取支持的动作
                        "/swagger-resources",                      //用来获取api-docs的URL
                        "/swagger-resources/configuration/security",//安全选择
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v2/**",
                        "/configuration/**").permitAll()
                .antMatchers(
                        "/**").authenticated() //设置加油站校验拦截
                /**
                 * json web token 权限控制的核心配置部分
                 * 在 Spring Security 开始判断本次会话是否有权限时的前一瞬间
                 * 通过添加过滤器将 token 解析，将用户所有的权限写入本次 Spring Security 的会话
                 */
                .and().addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.cors().configurationSource(corsConfigurationSource());//添加 CORS 支持
        httpSecurity.logout().disable(); //add logout
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