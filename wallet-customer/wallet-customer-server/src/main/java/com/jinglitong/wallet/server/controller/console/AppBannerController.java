package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.service.AppBannerService;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.api.model.AppBanner;
import com.jinglitong.wallet.api.model.view.AppBannerVO;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import static com.jinglitong.wallet.server.common.ClickTypeEnum.*;


@Controller
@RequestMapping(value = "/console")
public class AppBannerController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AppBannerService appBannerService;
    /**
     * 获取banner列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBannerList.json", method = RequestMethod.POST)
    public Map getBannerList(@RequestBody AppBannerVO vo) {
        if(StringUtils.isEmpty(vo.getAppId())){
            setGlobalAdminAppId(vo);
        }
    	HashMap<String, Object> list = appBannerService.getBannerList(vo);
        return JsonUtil.toJsonSuccess("获取banner列表成功", list);
    }
    /**
     * 新增banner
     * @return
     * @throws Exception 
     */
    @ResponseBody
    @RequestMapping(value = "/addBanner.json", method = RequestMethod.POST)
    public Map addBanner(@RequestBody AppBannerVO vo) throws Exception {
        int insert = appBannerService.addBanner(vo);
        if(insert > 0){
            return JsonUtil.toJsonSuccess("新增banner数据成功", insert);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31433);
        }
    }
    /**
     * 修改banner
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updBanner.json", method = RequestMethod.POST)
    public Map updBanner(@RequestBody AppBannerVO vo) {
        int update = appBannerService.updBanner(vo);
        if(update > 0){
            return JsonUtil.toJsonSuccess("更新banner数据成功", update);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31432);
        }
    }
    /**
     * 查看banner详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bannerDetail.json", method = RequestMethod.POST)
    public Map bannerDetail(@RequestBody  Map<String, Object> map) {
        
    	AppBanner appBanner= appBannerService.bannerDetail(map);
            return JsonUtil.toJsonSuccess("获取成功", appBanner);
    }
    
    
    /**
     * 获取banner点击类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getClickType.json", method = RequestMethod.POST)
    public Map<String , Object> getClickType() {
        Map<String , Object> map=new HashMap<>();
        map.put(NOTICE.getCode(), NOTICE.getValue());
        map.put(JUMP.getCode(), JUMP.getValue());
        return JsonUtil.toJsonSuccess("获取banner点击类型成功", map);
    }
    
    @ResponseBody
    @RequestMapping(value = "/getNotices.json", method = RequestMethod.POST)
    public Map getNoices() {
        return JsonUtil.toJsonSuccess("获取公告成功", appBannerService.getAppNotices());
    }

}