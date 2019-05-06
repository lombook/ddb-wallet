package com.jinglitong.springshop.auth;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.md5.MD5ShiroUtils;
import com.jinglitong.springshop.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AuthTokenProvider
 * @Description 自定义校验规则
 * @Author zili.zong
 * @Date 2018/11/19 10:16
 * @Version 1.0
 **/
@Slf4j
@Component
public class AuthTokenProvider implements AuthenticationProvider {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected RedisService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        String clientId = null;
        if (authentication.getCredentials() != null) {
            clientId = (String) authentication.getCredentials();
        }
        if (token != null) {
            String md5TokenKey = MD5ShiroUtils.createRedisKey(clientId+token, IConstants.LOGIN_ENCRYPT_SALT);
            if (!tokenService.checkToken(md5TokenKey)) {
                throw new CredentialsExpiredException(ErrorEnum.ERROR_208.getMsg());
            }
        } else {
            throw new BadCredentialsException(ErrorEnum.ERROR_205.getMsg());
        }
        log.debug("Authenticated successfully.");
        return new UsernamePasswordAuthenticationToken(token, clientId, authentication.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
