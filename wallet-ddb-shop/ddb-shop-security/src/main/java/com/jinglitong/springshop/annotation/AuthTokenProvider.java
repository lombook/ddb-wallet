package com.jinglitong.springshop.annotation;

import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.md5.MD5ShiroUtils;
import com.jinglitong.springshop.md5.Md5Util;
import com.jinglitong.springshop.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @ClassName AuthTokenProvider
 * @Description 自定义校验规则
 * @Author zili.zong
 * @Date 2018/11/19 10:16
 * @Version 1.0
 **/
@Component
public class AuthTokenProvider implements AuthenticationProvider {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected RedisService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        String clientId = null;
        if (authentication.getCredentials()!=null){
            clientId = (String) authentication.getCredentials();
        }
        if (token != null) {
            String md5TokenKey = MD5ShiroUtils.createRedisKey(clientId+token, IConstants.LOGIN_ENCRYPT_SALT);
            if (!tokenService.checkToken(md5TokenKey)) {
                throw new CredentialsExpiredException("您的登录已失效，请重新登录！");
            }
        } else {
            throw new BadCredentialsException("您还没有登录，请登录！");
        }
        logger.debug("Authenticated successfully.");
        return new UsernamePasswordAuthenticationToken(token, clientId, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
