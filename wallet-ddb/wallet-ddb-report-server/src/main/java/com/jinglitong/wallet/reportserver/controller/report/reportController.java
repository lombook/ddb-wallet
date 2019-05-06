package com.jinglitong.wallet.reportserver.controller.report;

import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.reportserver.service.ReportUserService;
import com.jinglitong.wallet.reportserver.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ddb/report")
@Slf4j
public class reportController {

    @Autowired
    private ReportUserService reportUserService;

    @Value("${ddb_app_id}")
    private String appId;

    @ResponseBody
    @RequestMapping(value = "/getUserInfo.json",method = RequestMethod.POST)
    public Map getCerUrl(@RequestBody Map<String,String> result) {
        result.put("appId",appId);
          Map userList = reportUserService.getUserInfo(result);
        return JsonUtil.toJsonSuccess("成功", userList);
    }


    @ResponseBody
    @RequestMapping(value = "/getOrderGive.json",method = RequestMethod.POST)
    public Map getOrderGive(@RequestBody Map<String,String> result) {

        Map userList = reportUserService.getOrderGive(result);

        return JsonUtil.toJsonSuccess("成功", userList);
    }

    @ResponseBody
    @RequestMapping(value = "/getGoodRules.json",method = RequestMethod.POST)
    public Map getGoodsRule() {

        Map<String,String> getrules = reportUserService.getrules();

        return JsonUtil.toJsonSuccess("成功", getrules);
    }
    
    @ResponseBody
    @RequestMapping(value = "/getteamActualDividends.json",method = RequestMethod.POST)
    public Map<?, ?> getteamActualDividends(@RequestBody Map<String,String> result) {

        Map<String, Object> getteamActualDividends = reportUserService.getteamActualDividends(result);

        return JsonUtil.toJsonSuccess("成功", getteamActualDividends);
    }
    
    @ResponseBody
    @RequestMapping(value = "/getRelationshipTree.json",method = RequestMethod.POST)
    public Map<?, ?> getRelationshipTree(@RequestBody Map<String,String> result) {

        Map<String, Object> getRelationshipTree = reportUserService.getRelationshipTree(result);

        return JsonUtil.toJsonSuccess("成功", getRelationshipTree);
    }
    @ResponseBody
    @RequestMapping(value = "/getallChildren.json",method = RequestMethod.POST)
    public List<Map<String, String>> getallChildren(@RequestBody Map<String,String> result) {

        List<Map<String, String>> childrenTree = reportUserService.getallChildren(result);

        return childrenTree;
    }
    
    @ResponseBody
    @RequestMapping(value = "/getlistChildren.json",method = RequestMethod.POST)
    public Map<String ,Object> getlistChildren(@RequestBody Map<String,String> result) {
        Map<String, Object> childrenTree = reportUserService.getListChildren(result);
        return childrenTree;
    }
    
    @ResponseBody
    @RequestMapping(value = "/gettreeChildren.json",method = RequestMethod.POST)
    public Map<String, Object> gettreeChildren(@RequestBody Map<String,String> result) {

    	Map<String ,Object> childrenTree = reportUserService.getTreeChildren(result);

        return childrenTree;
    }
    
    @ResponseBody
    @RequestMapping(value = "/getOrders.json",method = RequestMethod.POST)
    public Map<String, Object> getOrders(@RequestBody Map<String,String> result) {

    	return reportUserService.getOrders(result);

    }
}
