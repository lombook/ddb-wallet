package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.server.service.PropertieTabService;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.PropertieTab;
import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.MainChainVO;
import com.jinglitong.wallet.server.service.MainChainService;
import com.jinglitong.wallet.server.service.SubChainService;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fan on 2018/5/22.
 */
@Controller
@RequestMapping(value = "/console")
public class MainChainController extends BaseController{


    @Autowired
    MainChainService mainChainService;

    @Autowired
    PropertieTabService propertieTabService;

    @Autowired
    SubChainService subChainService;


    /**
     * 获取列表
     * @param mainChainVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMainChains.json", method = RequestMethod.POST)
    public Map getMainChains(@RequestBody MainChainVO mainChainVO) {
        if(StringUtils.isEmpty(mainChainVO.getAppId())){
            setGlobalAdminAppId(mainChainVO);
        }
        HashMap<String, Object> map = mainChainService.getMainChains(mainChainVO);
        return JsonUtil.toJsonSuccess("获取链列表", map);
    }
    /**
     * 验证该链是否重复
     */
    @ResponseBody
    @RequestMapping(value = "/checksMainChain.json", method = RequestMethod.POST)
    public Boolean checksMainChain(@RequestBody MainChainVO mainChainVO) {
    	if(mainChainService.checksMainChain(mainChainVO)) {
    		return true;
    	}
    	return  false;
    }
    
    /**
     * 增加
     * @param mainChainVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createMainChain.json", method = RequestMethod.POST)
    public Map createMainChain(@RequestBody MainChainVO mainChainVO) {
        Integer insert = mainChainService.createMainChain(mainChainVO);
        if(insert > 0) {
            return JsonUtil.toJsonSuccess("添加链成功", insert);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31425);
        }
    }
    /**
     * 修改
     * @param mainChainVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateMainChain.json", method = RequestMethod.POST)
    public Map updateMainChain(@RequestBody MainChainVO mainChainVO) {
        int update = mainChainService.updateMainChain(mainChainVO);
        if(update > 0) {
            return JsonUtil.toJsonSuccess("修改链成功", update);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31424);
        }
    }
    /**
     * 修改链状态
     * @param mainChainVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateMainChainState.json", method = RequestMethod.POST)
    public Map updateMainChainState(@RequestBody MainChainVO mainChainVO) {
        int update = mainChainService.updateMainChainState(mainChainVO);
        if(update > 0)
            return JsonUtil.toJsonSuccess("修改链成功", update);
        else
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31424);
    }
    /**
     * 链类名下拉框
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/gethandleNames.json", method = RequestMethod.POST)
    public Map gethandleNames() {
        List<PropertieTab> propertieTab = propertieTabService.getpropertiesByGoup("handleName");
        return JsonUtil.toJsonSuccess("下拉列表", propertieTab);

    }

    /**
     * 链名和id下拉框链名和id下拉框
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMainChainMap.json", method = RequestMethod.POST)
    public Map getMainChainMap() {
        List<HashMap<String , Object>> list = mainChainService.gethandleNames();

            return JsonUtil.toJsonSuccess("获取列表成功", list);

    }
    @ResponseBody
    @RequestMapping("/getChainByAppId.json")
    public Map getMainByAppId(@RequestBody MainChainVO mainChainVO) {
    	List<HashMap<String, Object>> l = mainChainService.getmainChainByAppId(mainChainVO);
    	 return JsonUtil.toJsonSuccess("获取列表成功", l);
    }
    /**
     * 检查链名是否唯一
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkChainName.json", method = RequestMethod.POST)
    public Map checkChainName(@RequestBody MainChainVO mainChainVO) {
        MainChain mainChain = mainChainService.checkChainName(mainChainVO.getChainName());
        if(mainChain != null)
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31426);
        else
         return JsonUtil.toJsonSuccess("链名唯一");

    }
    /**
     *查询所有链
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/allMainChain.json", method = RequestMethod.POST)
    public Map getAllMainChain(@RequestBody MainChainVO  mainChainVO) {
        if(StringUtils.isEmpty(mainChainVO.getAppId())){
            setGlobalAdminAppId(mainChainVO);
        }
        List<MainChain> mainChainList = mainChainService.selectAllMainChain(mainChainVO);

        return JsonUtil.toJsonSuccess("成功",mainChainList);

    }

}
