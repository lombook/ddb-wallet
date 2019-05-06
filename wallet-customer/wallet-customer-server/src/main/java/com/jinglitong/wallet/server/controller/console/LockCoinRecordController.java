package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.service.ConsoleAPI;
import com.jinglitong.wallet.server.service.WalletService;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.storage.AliCloudStorageService;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.view.LockCoinRecordVo;
import com.jinglitong.wallet.api.model.view.LockCoinToltalVo;
import com.jinglitong.wallet.api.model.view.LockSelCoinRecordVO;
import com.jinglitong.wallet.server.service.LockCoinRecordService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.SessionUtil;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("console/lockCoinRecord")
public class LockCoinRecordController extends BaseController{

    @Resource
    private LockCoinRecordService lockCoinRecordService;
    @Autowired
    private ConsoleAPI consoleAPI;

    @Autowired
    WalletService walletService;

    @Autowired
    private AliCloudStorageService aliCloudStorageService;

    @ResponseBody
    @RequestMapping(value = "/upload.json")
    public Map upload(@RequestParam("file") MultipartFile file) {
//        logger.debug("开始上传图片");
        Map map = new HashMap();
        try {
            String url = aliCloudStorageService.uploadFile(file);
//            logger.debug("上传图片URL" + url);
            map.put("url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("ok", file.getName());
//        logger.debug("图片上传成功");
        return JsonUtil.toJsonSuccess("成功", map);
    }


    /**
     * 获取列表
     * @param coinRecordVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/records", method = RequestMethod.POST)
    public Map getLockCoinRecords(@RequestBody LockCoinRecordVo coinRecordVo) {
        Customer customer = (Customer) SessionUtil.getUserInfo();
        if(null == customer){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21011);
        }
        coinRecordVo.setCustId(customer.getCustId());
        HashMap<String, Object> map = lockCoinRecordService.getLockCoinRecords(coinRecordVo);
        return JsonUtil.toJsonSuccess("获取列表", map);
    }
    /**
     * 锁仓配置-查看详情 后台获取列表
     * @param coinRecordVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/recordListByChainCoin", method = RequestMethod.POST)
    public Map recordListByChainCoin(@RequestBody LockSelCoinRecordVO coinRecordVo) {
        setGlobalAdminAppId(coinRecordVo);
        HashMap<String, Object> map = lockCoinRecordService.recordListByChainCoin(coinRecordVo);
//        Map<String, Object> balances = consoleAPI.balances(coinRecordVo);
        map.put("adress",coinRecordVo.getWalletAddress());
        return JsonUtil.toJsonSuccess("获取列表", map);
    }
    /**
     * 
     * 功能说明:锁仓页面-到期解仓数据    获取已到期用户信息统计
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/recordToltal", method = RequestMethod.POST)
    public Map recordToltal(@RequestBody LockCoinToltalVo vo){
        setGlobalAdminAppId(vo);
    	HashMap<String, Object> map =lockCoinRecordService.recordToltal(vo);
    	return JsonUtil.toJsonSuccess("获取列表", map);
    }
    /**
     * 
     * 功能说明:锁仓页面-到期解仓数据-点击用户数量       已到期用户详情信息
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/recordToltalDetail", method = RequestMethod.POST)
    public Map recordToltalDetail(@RequestBody LockCoinToltalVo vo){
        setGlobalAdminAppId(vo);
    	HashMap<String, Object> map = lockCoinRecordService.recordToltalDetail(vo);
    	return JsonUtil.toJsonSuccess("获取列表", map);
    }

}
