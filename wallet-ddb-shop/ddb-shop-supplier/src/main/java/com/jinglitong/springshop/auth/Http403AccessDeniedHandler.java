package com.jinglitong.springshop.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.ShopRespose;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName com.jinglitong.springshop.auth.Http403AccessDeniedHandler
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/24 17:42
 * @Version 1.0
 **/
public class Http403AccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;
        if (e.getCause() != null) {
            message = e.getCause().getMessage();
        } else {
            message = e.getMessage();
        }
        ShopRespose resultInfo = new ShopRespose(ErrorEnum.ERROR_207.getCode(),ErrorEnum.ERROR_207.getMsg(),message);
        byte[] body = new ObjectMapper().writeValueAsBytes(resultInfo);
        httpServletResponse.getOutputStream().write(body);
    }
}
