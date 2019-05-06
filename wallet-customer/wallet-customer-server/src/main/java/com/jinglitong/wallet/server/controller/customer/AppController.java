package com.jinglitong.wallet.server.controller.customer;

import com.alibaba.fastjson.JSONObject;
import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.logic.PageViewLog;
import com.jinglitong.wallet.api.model.view.AppBannerVO;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.AppBannerService;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.SessionUtil;
import com.jinglitong.wallet.api.model.view.AppHelpVO;
import com.jinglitong.wallet.api.model.view.AppNoticeVO;
import com.jinglitong.wallet.api.model.view.AppVO;
import com.jinglitong.wallet.api.model.view.ShareNoticeVo;
import com.jinglitong.wallet.server.service.AppControlService;
import java.nio.charset.StandardCharsets;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/customer/app/common")
public class AppController  extends CusBaseController{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppControlService appControlService;
    
    @Autowired
    private AppBannerService appBannerService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${isEnable}")
    private int isEnable;

    @Value("${web_ip}")
    private String webIp;

    @ResponseBody
    @RequestMapping(value = "/version.json", method = RequestMethod.POST)
    public Map getountries(@RequestBody AppVO vo) {
        setGlobalFromAppId(vo);
        List<AppControl> beans = appControlService.getAppVersionByType(vo.getType(),vo.getAppId());


        if (beans.size() == 0) {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21401);
        } else {
            return JsonUtil.toJsonSuccess("获取版本成功", beans.get(0));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/notices.json", method = RequestMethod.POST)
    public Map getNoices(@RequestBody AppNotice appNotice,@RequestHeader HttpHeaders headers) {
        appNotice.setAppId((String)headers.getFirst("appId"));
        setGlobalFromAppId(appNotice);
        return JsonUtil.toJsonSuccess("获取公告成功", appControlService.getAppNotices(appNotice));
    }

    @ResponseBody
    @RequestMapping(value = "/notice.json", method = RequestMethod.POST)
    public Map getNoice(@RequestBody AppNoticeVO vo) {
        //setGlobalCustomAppId(vo);
    	Customer cust =(Customer) SessionUtil.getUserInfo(); 
        Object appNotice = appControlService.getAppNotice(vo.getNoticeId(),cust == null?null:cust.getCustId());
        if(appNotice.equals(0)){
    	   return JsonUtil.toJsonError(ErrorEnum.ERROR_31421);
        }
        return JsonUtil.toJsonSuccess("获取公告成功",appNotice );
        /*if (isEnable == 1) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Customer customer = ((Customer) SecurityUtils.getSubject().getPrincipal());
                        PageViewLog pageViewLog = new PageViewLog();
                        pageViewLog.setAccount(customer.getAccount());
                        pageViewLog.setAppId(vo.getAppId());
                        pageViewLog.setViewId(appNotice.getNoticeId());
                        pageViewLog.setTitle(appNotice.getTitle());
                        String dateTime = DateUtils.getDateTime();
                        pageViewLog.setViewTime(dateTime);
                        pageViewLog.setCreateTime(dateTime);
                        pageViewLog.setViewType(0);
                        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                        Object json = JSONObject.toJSON(pageViewLog);
                        logger.info("公告记录数据收集参数:",json.toString());
                        ResponseEntity<String> postForEntity = restTemplate.postForEntity(webIp + "/pageViewLog/notice",json,String.class);//传递实体
                        String result = postForEntity.getBody();
                        if (result.equals("success")) {
                            logger.info("公告记录数据收集成功:",result);
                        } else {
                            logger.info("公告记录数据收集失败:",result);
                        }
                    } catch (Exception e) {
                        logger.info("公告记录数据收集失败:",e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            t.start();

        }*/

    }

    @ResponseBody
    @RequestMapping(value = "/helps.json", method = RequestMethod.POST)
    public Map getHelps(@RequestBody AppHelpVO vo,@RequestHeader HttpHeaders headers) {
        vo.setAppId((String)headers.getFirst("appId"));
        //setGlobalCustomAppId(vo);
        setGlobalFromAppId(vo);
        vo.setState(true);
        return JsonUtil.toJsonSuccess("获取帮助成功", appControlService.getAppHelpByType(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/help.json", method = RequestMethod.POST)
    public Map getHelp(@RequestBody AppHelpVO vo) {
        return JsonUtil.toJsonSuccess("获取帮助成功", appControlService.getAppHelp(vo.getAppHelpId()));
    }
    
    @ResponseBody
    @RequestMapping(value = "/banners.json", method = RequestMethod.POST)
    public Map getBanners(@RequestBody AppBannerVO vo) {
        setGlobalFromAppId(vo);
        AppBanner appBanner = new AppBanner();
        appBanner.setAppId(vo.getAppId());
        //setGlobalCustomAppId(appBanner);
        return JsonUtil.toJsonSuccess("获取banner成功", appBannerService.getAppBanners(appBanner));
    }
    
    @ResponseBody
    @RequestMapping(value = "/shareNotice.json", method = RequestMethod.POST)
    public Map shareNotice(@RequestBody ShareNoticeVo vo) {
    	 Customer customer = ((Customer) SecurityUtils.getSubject().getPrincipal());
         PageViewLog pageViewLog = new PageViewLog();
         pageViewLog.setAccount(customer.getAccount());
         pageViewLog.setAppId(vo.getAppId());
         if(vo.getShareType() == 2) {//邀请下载
        	 pageViewLog.setTitle("邀请");
        	 pageViewLog.setViewId(null);
         }else {//公告
        	 pageViewLog.setViewId(vo.getViewId());
        	 pageViewLog.setTitle(vo.getTitle());
         }
         String dateTime = DateUtils.getDateTime();
         pageViewLog.setViewTime(dateTime);
         pageViewLog.setCreateTime(dateTime);
         pageViewLog.setViewType(vo.getShareType());
         restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
         Object json = JSONObject.toJSON(pageViewLog);
         logger.info("分享记录数据收集参数:{}",json.toString());
         logger.info(webIp + "/pageViewLog/notice");
         ResponseEntity<String> postForEntity = restTemplate.postForEntity(webIp + "/pageViewLog/notice",json,String.class);//传递实体
         String result = postForEntity.getBody();
         if (result.equals("success")) {
             logger.info("分享记录数据收集成功:{}",result);
         } else {
             logger.info("分享记录数据收集失败:{}",result);
         }
         
         return JsonUtil.toJsonSuccess("success");
    }
}
