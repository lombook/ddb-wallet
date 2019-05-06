package com.jinglitong.wallet.server.filter;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.server.util.RSASignature;
import com.jinglitong.wallet.api.model.Seller;
import com.jinglitong.wallet.server.service.SellerService;
import com.jinglitong.wallet.server.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenApiFilter extends HandlerInterceptorAdapter{

    /**
     * 访问key
     */
    private static String APP_KEY ="appkey";

    /**
     * 时间戳
     */
    private static String TIMESTAMP ="timestamp";

    /**
     * 签名
     */
    private static String SIGN ="sign";

    /**
     * 请求超时
     */
    private static Long REQUEST_TIME_OUT = 10*60*1000l;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String appkey = request.getHeader(APP_KEY);
        String timstamp = request.getHeader(TIMESTAMP);
        String sign = request.getHeader(SIGN);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        //参数不对
        if(StringUtils.isEmpty(appkey) || StringUtils.isEmpty(timstamp)|| StringUtils.isEmpty(sign)){
            response.getWriter().write(JSON.toJSONString(JsonUtil.toJsonError(ErrorEnum.ERROR_31447)));
            return false;
        }

        //时间超过10分钟
        if((System.currentTimeMillis()-Long.valueOf(timstamp))>REQUEST_TIME_OUT){
            response.getWriter().write(JSON.toJSONString(JsonUtil.toJsonError(ErrorEnum.ERROR_31449)));
            return false;
        }


        //商家不存在
        SellerService sellerService =  (SellerService) ApplicationContextHelper.getBean("sellerService");
        Seller seller = new Seller();
        seller.setAccessKey(appkey);
        seller = sellerService.queryOneSeller(seller);
        if(null == seller || null == seller.getId()){
            response.getWriter().write(JSON.toJSONString(JsonUtil.toJsonError(ErrorEnum.ERROR_31448)));
            return false;
        }

        //签名不对
        String signData = "appkey="+appkey+";timestamp="+timstamp;
        try {
            boolean signOk = RSASignature.verify(signData.getBytes(),seller.getPublicKey(),sign);
            if(!signOk){
                response.getWriter().write(JSON.toJSONString(JsonUtil.toJsonError(ErrorEnum.ERROR_31450)));
                return false;
            }
        }catch (Exception e){
            response.getWriter().write(JSON.toJSONString(JsonUtil.toJsonError(ErrorEnum.ERROR_31450)));
            return false;
        }


        return true;
    }

}
