package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.api.model.AppHelp;
import com.jinglitong.wallet.api.model.view.AppHelpCreateVO;
import com.jinglitong.wallet.api.model.view.AppHelpSelVO;
import com.jinglitong.wallet.server.common.AppTypeEnum;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.AppHelpService;
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
public class AppHelpController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AppHelpService appHelpService;
    /**
     * 获取appHelp列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/apphelps.json", method = RequestMethod.POST)
    public Map applists(@RequestBody AppHelpSelVO appHelpSelVO) {
        setGlobalAdminAppId(appHelpSelVO);
        HashMap<String, Object> map = appHelpService.getapplists(appHelpSelVO);
        return JsonUtil.toJsonSuccess("获取app列表", map);
    }
    /**
     * 创建appHelp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createApphelp.json", method = RequestMethod.POST)
    public Map createApphelp(@RequestBody AppHelpCreateVO appHelpCreateVO) {
        setGlobalAdminAppId(appHelpCreateVO);
         int insert = appHelpService.createApphelp(appHelpCreateVO);
         if(insert > 0){
             return JsonUtil.toJsonSuccess("创建apphelp成功", insert);
         }else{
             return JsonUtil.toJsonError(ErrorEnum.ERROR_31404);
         }
    }
    /**
     * 修改appHelp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateapphelp.json", method = RequestMethod.POST)
    public Map updateapphelp(@RequestBody AppHelpCreateVO appHelpCreateVO) {
         int update = appHelpService.updateapphelp(appHelpCreateVO);
         if(update > 0){
             return JsonUtil.toJsonSuccess("修改appHelp成功", update);
         }else{
             return JsonUtil.toJsonError(ErrorEnum.ERROR_31405);
         }

    }
    /**
     * 获取appHelp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getapphelp.json", method = RequestMethod.POST)
    public Map getapphelp(@RequestBody AppHelpCreateVO appHelpVo) {
        if(StringUtils.isEmpty(appHelpVo.getAppHelpId()))
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31405);
         AppHelp appHelp = appHelpService.getapphelp(appHelpVo.getAppHelpId());
        return JsonUtil.toJsonSuccess("获取appHelp成功", appHelp);
    }

    /**
     * 启用或冻结Help
     * @param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/updateHelpState.json", method = RequestMethod.POST)
    public Map memberState(@RequestBody AppHelpCreateVO appHelpCreateVO) {
        if(StringUtils.isEmpty(appHelpCreateVO.getAppHelpId())|| appHelpCreateVO.getState() == null){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31406);
        }
        int i = appHelpService.updateHelpState(appHelpCreateVO.getAppHelpId(), appHelpCreateVO.getState());
        if(i < 0){
            return JsonUtil.toJsonSuccess("修改状态成功", i);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31407);
        }
    }

    /**
     * 启用或冻结Help
     * @param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getAppType.json", method = RequestMethod.POST)
    public Map getAppType() {
        AppTypeEnum[] values = AppTypeEnum.values();
        Map<String,String> Datas = new HashMap<>();
        for (AppTypeEnum enm : values){
            Datas.put(enm.getName(),enm.getValue());
        }
        return JsonUtil.toJsonSuccess("成功", Datas);
    }


}