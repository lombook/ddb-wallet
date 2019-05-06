package com.jinglitong.springshop.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
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
import com.jinglitong.springshop.system.Role;
import com.jinglitong.springshop.system.SysUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName com.jinglitong.springshop.com.jinglitong.springshop.filter.AuthTokenFilter
 * @Description  token 拦截器 通过重写springsecurity AbstractAuthenticationProcessingFilter
 * @Author zili.zong
 * @Date 2018/11/19 10:03
 * @Version 1.0
 **/
@Slf4j
public class AuthTokenFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    private Environment env;

    public AuthTokenFilter(RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String authToken = request.getHeader("token");
        String client = request.getHeader("clientId");
        if (authToken == null) {
            throw new AuthenticationCredentialsNotFoundException(ErrorEnum.ERROR_205.getMsg());
        }
        String roleName = JwtTokenUtils.getUserRole(authToken);
        Role role = new Role();
        role.setName(roleName);
        role.setRole(roleName);
        List<Role> list = new ArrayList<>();
        list.add(role);
        SysUser user = new SysUser();
        user.setRoles(list);
        //消费者在登录时有非空校验，管理端不需要clientId 这里不需要校验clientId
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authToken, client,user.getAuthorities());
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
        ShopRespose msgCode = new ShopRespose(ErrorEnum.ERROR_205);
        byte[] body = new ObjectMapper().writeValueAsBytes(msgCode);
        response.getOutputStream().write(body);
    }
}