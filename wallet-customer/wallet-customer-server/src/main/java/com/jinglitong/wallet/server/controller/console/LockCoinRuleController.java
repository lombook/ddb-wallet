package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.api.model.LockCoinRule;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.view.LockCoinRuleVo;
import com.jinglitong.wallet.api.model.view.LockConRuleVO;
import com.jinglitong.wallet.api.model.view.LockCreateConRuleVO;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.LockCoinRuleService;
import com.jinglitong.wallet.server.service.MainChainService;
import com.jinglitong.wallet.server.service.SubChainService;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("console/lockCoinRule")
public class LockCoinRuleController extends BaseController{

    @Resource
    private LockCoinRuleService lockCoinRuleService;

    @Resource
    private MainChainService mainChainService;

    @Resource
    private SubChainService subChainService;


    /**
     * 获取列表
     * @param lockCoinRuleVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rules", method = RequestMethod.POST)
    public Map getLockCoinRules(@RequestBody LockCoinRuleVo lockCoinRuleVo) {
//        lockCoinRuleVo.setRuleStatus(true);
        HashMap<String, Object> map = lockCoinRuleService.getLockCoinRules(lockCoinRuleVo);
        List<LockCoinRule> lockCoinRules = new ArrayList<>();
        for(LockCoinRule rule:(List<LockCoinRule>)map.get("rules")){
            MainChain mainChain = new MainChain();
            mainChain.setChainId(rule.getChainId());
            mainChain = mainChainService.getOneMainChain(mainChain);
            rule.setChainId(mainChain.getChainName());
            SubChain subChain = subChainService.selectByCoinId(rule.getCoinId());
            rule.setCoinId(subChain.getCoinName());
            lockCoinRules.add(rule);
        }
        map.put("rules",lockCoinRules);
        return JsonUtil.toJsonSuccess("获取列表", map);
    }

    /**
     * 获取后台列表
     * @param lockCoinRuleVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRulesList", method = RequestMethod.POST)
    public Map getRulesList(@RequestBody LockConRuleVO lockCoinRuleVo) {
    	setGlobalAdminAppId(lockCoinRuleVo);
        return JsonUtil.toJsonSuccess("获取列表成功", lockCoinRuleService.getLockCoinRuleList(lockCoinRuleVo));
    }

    /**
     * 创建规则
     * @param lockCoinRuleVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createRule", method = RequestMethod.POST)
    public Map createRule(@RequestBody LockCreateConRuleVO lockCoinRuleVo) {
    	setGlobalAdminAppId(lockCoinRuleVo);
        Boolean flag = lockCoinRuleService.createLockCoinRuleList(lockCoinRuleVo);
        if(!flag)
            return JsonUtil.toJsonError(ErrorEnum.ERROR_35003);
        return JsonUtil.toJsonSuccess("创建成功", flag);
    }
    /**
     * 修改显示状态
     * @param lockCoinRuleVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateRuleState", method = RequestMethod.POST)
    public Map updateRuleState(@RequestBody LockConRuleVO lockCoinRuleVo) {
    	setGlobalAdminAppId(lockCoinRuleVo);
        if(lockCoinRuleVo.getRuleStatus() == null)
            return JsonUtil.toJsonError(ErrorEnum.ERROR_35001);
        if(StringUtils.isEmpty(lockCoinRuleVo.getLockRuleId()))
            return JsonUtil.toJsonError(ErrorEnum.ERROR_35002);
        Boolean flag = lockCoinRuleService.updateRuleState(lockCoinRuleVo);
        return JsonUtil.toJsonSuccess("修改成功", flag);
    }

}
