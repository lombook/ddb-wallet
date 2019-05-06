//package com.jinglitong.wallet.gateway.filter;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.ReflectionUtils;
//
//@Configuration
//@Slf4j
//public class CustomErrorFilter extends ZuulFilter {
//
//    @Override
//    public String filterType() {
//        return "error";
//    }
//
//    @Override
//    public int filterOrder() {
//        return -1; // Needs to run before SendErrorFilter which has filterOrder == 0
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        // only forward to errorPath if it hasn't been forwarded to already
//        return RequestContext.getCurrentContext().containsKey("error.status_code");
//    }
//
//    @Override
//    public Object run() {
//        try {
//            RequestContext ctx = RequestContext.getCurrentContext();
//            Object e = ctx.get("error.exception");
//
//            if (e != null && e instanceof ZuulException) {
//                ZuulException zuulException = (ZuulException)e;
//                log.error("Zuul failure detected: " + zuulException.getMessage(), zuulException);
//
//                // Remove error code to prevent further error handling in follow up filters
//                ctx.remove("error.status_code");
//
//                // Populate context with new response values
//                ctx.setResponseBody("Overriding Zuul Exception Body");
//                ctx.getResponse().setContentType("application/json");
//                ctx.setResponseStatusCode(500); //Can set any error code as excepted
//            }
//        }
//        catch (Exception ex) {
//            log.error("Exception filtering in custom error filter", ex);
//            ReflectionUtils.rethrowRuntimeException(ex);
//        }
//        return null;
//    }
//}
