package com.jinglitong.springshop.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.ShopRespose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @ClassName AuthTokenFilter
 * @Description  token 拦截器 通过重写springsecurity AbstractAuthenticationProcessingFilter
 * @Author zili.zong
 * @Date 2018/11/19 10:03
 * @Version 1.0
 **/
public class AuthTokenFilter extends AbstractAuthenticationProcessingFilter{
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment env;

    public AuthTokenFilter(RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String authToken = request.getHeader("X-Auth-Token");
        String client = request.getHeader("clientId");
        //TODO 在这里获取agent信息
//        String userAgentString = request.getHeader("user-agent");
//        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
//        //操作系统
//        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
//        //browser信息
//        Browser browser= userAgent.getBrowser();
//        logger.debug("start to check token:{}"+userAgent.toString());
        if (authToken == null) {
            throw new AuthenticationCredentialsNotFoundException("您还没有登录，请登录！");
        }
        //消费者在登录时有非空校验，管理端不需要clientId 这里不需要校验clientId
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authToken, client);
        return getAuthenticationManager().authenticate(authentication);
    }

    /**
     * desc 验证成功
     * @param request
     * @param response
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    /**
     * desc 验证失败
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException authException) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;
        if (authException.getCause() != null) {
            message = authException.getCause().getMessage();
        } else {
            message = authException.getMessage();
        }
        ShopRespose baseResponse = new ShopRespose(ErrorEnum.ERROR_205);
        byte[] body = new ObjectMapper().writeValueAsBytes(baseResponse);
        response.getOutputStream().write(body);
    }
}