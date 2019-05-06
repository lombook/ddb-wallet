package com.jinglitong.wallet.gateway.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.io.CharStreams;
import com.jinglitong.wallet.common.utils.R;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @version V1.0
 * @author test
 */
//@Component
//@Slf4j
public class PostFilter extends ZuulFilter {




    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {


        return true;
    }


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        ctx.setResponseBody("Overriding Zuul Exception Body");
//        ctx.getResponse().setContentType("application/json");
        ctx.setResponseStatusCode(500);//Can set any error code as excepted
//        ctx.setResponseBody(R.error("登录超时").toString());
        try (InputStream responseDataStream = ctx.getResponseDataStream()) {
            final String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));
            int responseStatusCode = ctx.getResponseStatusCode();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseData);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            ctx.setResponseBody(mapper.writeValueAsString(R.error("登录超时")));
        } catch (IOException e) {
//            log.warn("Error reading body", e);
        }
        return null;
//
    }


}
