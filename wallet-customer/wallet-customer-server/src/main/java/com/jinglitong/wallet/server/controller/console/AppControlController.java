package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.api.model.AppControl;
import com.jinglitong.wallet.api.model.view.AppConSelVO;
import com.jinglitong.wallet.api.model.view.AppControlVO;
import com.jinglitong.wallet.server.service.AppControlService;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/console")
public class AppControlController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AppControlService appControlService;
    /**
     * 获取app版本列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/appContoollists.json", method = RequestMethod.POST)
    public Map applists(@RequestBody AppConSelVO appConSelVO) {
        if(org.springframework.util.StringUtils.isEmpty(appConSelVO.getAppId())){
            setGlobalAdminAppId(appConSelVO);
        }
        HashMap<String, Object> map = appControlService.applists(appConSelVO);
        return JsonUtil.toJsonSuccess("获取app版本列表", map);
    }
    /**
     * 新增app版本
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createappControl.json", method = RequestMethod.POST)
    public Map createappControl(@RequestBody AppControlVO appControlVO) {
        int insert = appControlService.createappControl(appControlVO);
        if(insert > 0){
            return JsonUtil.toJsonSuccess("新增成功", insert);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31401);
        }
    }
    /**
     * 修改app版本
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateAappControl.json", method = RequestMethod.POST)
    public Map updateAappControl(@RequestBody AppControlVO appControlVO) {
        int update = appControlService.updateAappControl(appControlVO);
        if(update > 0){
            return JsonUtil.toJsonSuccess("修改成功", update);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31402);
        }
    }
    /**
     * 查看app版本
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAppControl.json", method = RequestMethod.POST)
    public Map getAppControl(@RequestBody  AppControlVO appControlVo) {
        if(appControlVo.getId() == null)
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31403);
        AppControl appControl= appControlService.getAppControl(appControlVo.getId());
            return JsonUtil.toJsonSuccess("获取成功", appControl);
    }

}