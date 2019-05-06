package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.PropertieTabService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.common.ProGroupEnum;
import com.jinglitong.wallet.api.model.PropertieTab;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.api.model.view.PropertieTabVO;

import java.util.HashMap;
import java.util.Map;
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
public class PropertieController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PropertieTabService propertieTabService;


    /**
     * 获取关于列表
     */
    @ResponseBody
    @RequestMapping(value = "/aboutManagementList.json", method = RequestMethod.POST)
    public Map aboutManagementList(@RequestBody PropertieTabVO propertieTabVO) {
        if(StringUtils.isEmpty(propertieTabVO.getAppId())){
            setGlobalAdminAppId(propertieTabVO);
        }
        return propertieTabService.getAboutManagementList(propertieTabVO);
    }

    /**
     * 新增关于配置
     */
    @ResponseBody
    @RequestMapping(value = "/addAboutManagement.json", method = RequestMethod.POST)
    public Map addAboutManagement(@RequestBody PropertieTabVO propertieTabVO) {
        return propertieTabService.addAboutManagement(propertieTabVO);
    }

    /**
     * 删除关于配置
     */
    @ResponseBody
    @RequestMapping(value = "/delAboutManagement.json", method = RequestMethod.POST)
    public Map delAboutManagement(@RequestBody PropertieTabVO propertieTabVO) {
        return propertieTabService.delAboutManagement(propertieTabVO);
    }

    /**
     * 回显关于配置
     */
    @ResponseBody
    @RequestMapping(value = "/getAboutManagementById.json", method = RequestMethod.POST)
    public Map getAboutManagementById(@RequestBody PropertieTabVO propertieTabVO) {
        return propertieTabService.getAboutManagementById(propertieTabVO);
    }

    /**
     * 修改关于配置
     */
    @ResponseBody
    @RequestMapping(value = "/updateAboutManagement.json", method = RequestMethod.POST)
    public Map updateAboutManagement(@RequestBody PropertieTab propertieTab) {
        return propertieTabService.updateAboutManagement(propertieTab);
    }


    /* *//**
     * 获取配置列表
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "/propertieList.json", method = RequestMethod.POST)
    public Map propertieList(@RequestBody PageVO page) {
        HashMap<String, Object> map = propertieTabService.getPropertiesList(page);
        return JsonUtil.toJsonSuccess("获取配置列表", map);
    }
    *//**
     * 创建配置
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "/createPropertie.json", method = RequestMethod.POST)
    public Map createPropertie(@RequestBody PropertieTabVO propertieVO) {
        String groupKey = propertieVO.getGroupKey();
        String groupName = propertieVO.getGroupName();
        PropertieTab propertieTab = checkPropertie(groupKey, groupName);
        if(propertieTab != null){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31430);
        }
        int insert = propertieTabService.createPropertie(propertieVO);
        if(insert>0){
            return JsonUtil.toJsonSuccess("成功", insert);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31430);
        }
    }
    private PropertieTab  checkPropertie(String groupKey,String groupName){
       PropertieTab propertieTab = propertieTabService.checkPropertie(groupKey,groupName);
       return propertieTab;
    }
    *//**
     * 修改配置
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "/updaePropertie.json", method = RequestMethod.POST)
    public Map updaePropertie(@RequestBody PropertieTabVO propertieVO) {
        String id = propertieVO.getId();
        PropertieTab p = propertieTabService.selectById(id);
        if(p != null){
            if(!propertieVO.getGroupKey().equals(p.getGroupKey())){
                String groupKey = propertieVO.getGroupKey();
                String groupName = propertieVO.getGroupName();
                PropertieTab propertieTab = checkPropertie(groupKey, groupName);
                if(propertieTab != null){
                    return JsonUtil.toJsonError(ErrorEnum.ERROR_31430);
                }
            }
        }

        int updete = propertieTabService.updaePropertie(propertieVO);
        if(updete>0){
            return JsonUtil.toJsonSuccess("成功", updete);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21103);
        }
    }
    *//**
     * 删除配置
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "/deletePropertie.json", method = RequestMethod.POST)
    public Map deletePropertie(@RequestBody Map map) {
        String id = (String)map.get("id");
        int delete = propertieTabService.deletePropertie(id);
        if(delete>0){
            return JsonUtil.toJsonSuccess("成功", delete);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21114);
        }
    }*/
    /**
     * 枚举下拉框
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/groupEnmus.json", method = RequestMethod.POST)
    public Map enmus() {
        ProGroupEnum[] values = ProGroupEnum.values();
        Map<String,String> groups = new HashMap<>();
        for (ProGroupEnum enm : values){
            groups.put(enm.getName(),enm.getValue());
        }
        return JsonUtil.toJsonSuccess("成功", groups);
    }
}