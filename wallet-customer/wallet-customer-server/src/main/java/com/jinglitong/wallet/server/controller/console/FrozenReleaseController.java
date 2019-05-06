package com.jinglitong.wallet.server.controller.console;


import com.jinglitong.wallet.api.model.FrozenReleaseRule;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.api.model.walletVo.BatchFinishVo;
import com.jinglitong.wallet.api.model.walletVo.FrozenReleaseVO;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.FrozenReleaseService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.kvn.poi.imp.PoiImporter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: FrozenReleaseController.java
 * 作        者: yxl 2018年7月26日
 * 创建时间: 2018年7月26日
 * 功能说明:凍結釋放
 */
@Controller
@RequestMapping(value = "/console")
public class FrozenReleaseController extends BaseController{

    @Autowired
    private FrozenReleaseService frozenReleaseService;

    /**
     * 
     * 功能说明:添加凍結釋放
     * @param file
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fz/createrule.json", method = RequestMethod.POST)
    public Map readExcel(@RequestParam("file") MultipartFile file, FrozenReleaseVO vo)  {
        List<List<Object>> allExcel = null;
        try {
            allExcel = PoiImporter.importFirstSheetFrom(file.getInputStream()).getContent();
            List<Object> excelHadList = allExcel.get(0);
            if(excelHadList.get(0).equals("注册账号") && excelHadList.get(1).equals("冻结金额")){

            } else {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_35101);
            }
            if(StringUtils.isBlank(vo.getChainId()) || StringUtils.isBlank(vo.getCoinId())){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_35104);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(allExcel.size()>1) {
            setGlobalAdminAppId(vo);
            return frozenReleaseService.prossesExcel(allExcel.subList(1,allExcel.size()),vo);
        }
        return null;
    }

    /**
     * 
     * 功能说明:列表
     * @param pageVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fz/list.json", method = RequestMethod.POST)
    public Map ruleAllList(@RequestBody FrozenReleaseVO pageVO)  {
        setGlobalAdminAppId(pageVO);
        Map map = frozenReleaseService.ruleList(pageVO);
        return JsonUtil.toJsonSuccess("获用户列表成功", map);
    }

    /**
     *
     * 功能说明:去修改頁面
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fz/get.json", method = RequestMethod.POST)
    public Map ruleist(@RequestBody FrozenReleaseVO vo)  {
        setGlobalAdminAppId(vo);
        FrozenReleaseRule rule = frozenReleaseService.getById(vo);
        return JsonUtil.toJsonSuccess("获用户成功", rule);
    }


    /**
     * 
     * 功能说明:修改
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fz/update.json", method = RequestMethod.POST)
    public Map update(@RequestBody FrozenReleaseVO vo)  {
    	setGlobalAdminAppId(vo);
        return frozenReleaseService.update(vo);
    }


    /**
     *用户信息
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fz/selectSource.json", method = RequestMethod.POST)
    public Map selectSource(@RequestBody FrozenReleaseVO vo)  {
    	setGlobalAdminAppId(vo);
        Map<String,Object> map = frozenReleaseService.selectByRuleID(vo);
        return JsonUtil.toJsonSuccess("查询成功",map);
    }

    /**
     *终止释放
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fz/batchFinish.json", method = RequestMethod.POST)
    public Map batchFinish(@RequestBody BatchFinishVo vo)  {
        Map<String,Object> map = frozenReleaseService.batchFinish(vo);
        return JsonUtil.toJsonSuccess("终止成功",map);
    }
    
    /**
    *
    * 根据标题查询规则
    */
   @ResponseBody
   @RequestMapping(value = "/fz/qryRule.json", method = RequestMethod.POST)
   public Map qryRule(@RequestBody FrozenReleaseVO vo)  {
	   Map map = frozenReleaseService.qryRule(vo);
       return JsonUtil.toJsonSuccess("获用户列表成功", map);
   }

}
