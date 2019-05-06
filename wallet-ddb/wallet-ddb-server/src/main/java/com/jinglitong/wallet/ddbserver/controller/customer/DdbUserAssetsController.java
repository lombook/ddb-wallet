package com.jinglitong.wallet.ddbserver.controller.customer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.logic.ReleaseCurrencyVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbUseAssetsRecordVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbUserAssetVo;
import com.jinglitong.wallet.ddbserver.service.DdbUserAssetsService;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;

@RestController
@RequestMapping("/customer/ddb")
public class DdbUserAssetsController {
protected Logger logger = LoggerFactory.getLogger(this.getClass());

	
    
    @Autowired
    private DdbUserAssetsService ddbUserAssetsService;

    @Autowired
    private RestTemplate restTemplate;
    
    


    @Value("${isEnable}")
    private int isEnable;

    @Value("${web_ip}")
    private String webIp;

    
    
    
    /**
     * 
     * 没有使用这个controller  直接调用了service   查询用户资产
     *
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "/getUserAssetAmount.json")
//    public Map getUserAssetAmount() {
//        Customer customer = (Customer) SessionUtil.getUserInfo();
//
//        DdbUserAssetVo ddbUserAssetVo = ddbUserAssetsService.getUserAssetAmount(customer);
//        return JsonUtil.toJsonSuccess("success", ddbUserAssetVo);
//    }
    
    /**
     *查询用户冻结余额======在冻结资产的接口上修改，添加查询用户资产，
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fz/freeAmount.json", method = RequestMethod.POST)
    public Map freeAmount(@RequestBody ReleaseCurrencyVo vo)  {
        Map<String,Object> map = ddbUserAssetsService.getReleaseAmount(vo);
        // 调用查询积分的方法
        Customer customer = (Customer) SessionUtil.getUserInfo();
        DdbUserAssetVo ddbUserAssetVo = ddbUserAssetsService.getUserAssetAmount(customer);
        Integer treeNum = ddbUserAssetsService.getTreeNum();
        ddbUserAssetVo.setTreeNum(treeNum.toString());
        ddbUserAssetVo.setTreeType("asset_tree");
        map.put("userAssets", ddbUserAssetVo);// 添加用户资产
        
        return JsonUtil.toJsonSuccess("查询成功",map);
    }
    
    
    /**
     * 获得用户资产交易记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRecords.json")
    public Map getRecords(@RequestBody DdbUseAssetsRecordVo vo) {
        return JsonUtil.toJsonSuccess("查询成功", ddbUserAssetsService.getRecords(vo));
    }
}
