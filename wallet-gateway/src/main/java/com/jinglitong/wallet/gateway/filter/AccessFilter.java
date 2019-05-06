package com.jinglitong.wallet.gateway.filter;

import com.jinglitong.wallet.api.feign.AppWalletFeignApi;
import com.jinglitong.wallet.common.utils.JSONUtils;
import com.jinglitong.wallet.common.utils.R;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.gateway.util.UserAgent;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import cz.mallat.uasparser.UserAgentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version V1.0
 * @author test
 */
@Component
@Slf4j
public class AccessFilter extends ZuulFilter {


    @Resource
    Environment environment;

    @Resource
    AppWalletFeignApi appWalletFeignApi;

    @Value("${wallet.ignori-url}")
    private String IgnoreUrl;

    @Value("${wallet.ddb-pre-url}")
    private String ddbPreUrl;

    @Value("${wallet.reject-url}")
    private String rejectUrl;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        UserAgentInfo uai = UserAgent.userAgent(request);
        log.info("  操作系统家族：" + uai.getOsFamily()+" "+"    操作系统详细名称：" + uai.getOsName()+""+"   浏览器名称和版本:" + uai.getUaName()+
                "   类型：" + uai.getType()+"浏览器名称：" + uai.getUaFamily()+" 浏览器版本：" + uai.getBrowserVersionInfo()+
                "   设备类型：" + uai.getDeviceType());


        return true;
    }


    @Override
    public Object run() {
        try{
            RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();
            final String requestUri = request.getRequestURI();
//
            log.info(requestUri);
            if(rejectUrl.indexOf(requestUri)>-1){
                setFailedRequest(R.error602(), 200);
                return null;
            }
            if(IgnoreUrl.indexOf(requestUri)>-1 || request.getMethod().equalsIgnoreCase("OPTIONS")){
                return null;
            }
            if(requestUri.indexOf(ddbPreUrl)>-1){
                return null;
            }
            String appKey = request.getHeader("appId");


            if(StringUtils.isNotBlank(appKey)){
                Long appId = appWalletFeignApi.getAppWalletIdByIden(appKey);
                if(appId==-1){
                    setFailedRequest(R.error401(), 200);
                    return null;
                }
            }else {
                setFailedRequest(R.error401(), 200);
                return null;
            }


            return null;
        }catch (Exception e){
            setFailedRequest(R.error("请求失败，请检查网络"),200);
            return null;
        }

//
    }

    private void setFailedRequest(Object body, int code) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        HttpServletResponse response = ctx.getResponse();
        PrintWriter out = null;
        try{
            response.setCharacterEncoding("utf-8");
            out = response.getWriter();
            out.write(JSONUtils.beanToJson(body));
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
        ctx.setSendZuulResponse(false);
    }



}
