package com.jinglitong.springshop.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
	protected Logger logger = LoggerFactory.getLogger(CorsFilter.class);
    /*跨域请求配置*/
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	long start = System.currentTimeMillis();
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest reqs = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", reqs.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "5000");
        response.setHeader("Access-Control-Allow-Headers", "appId,Content-Type, Access-Control-Allow-Headers, Authorization, token,clientId");
        chain.doFilter(req, res);
        long end = System.currentTimeMillis();
        logger.info("URL:"+reqs.getRequestURL()+" , cost time:"+(end-start)+"ms");
        
    }
    public void init(FilterConfig filterConfig) {}
    public void destroy() {}
}
