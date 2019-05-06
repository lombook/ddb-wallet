package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import com.jinglitong.wallet.api.model.SellerCoinInfo;
import com.jinglitong.wallet.api.model.view.SellerCoinInfoVo;
import com.jinglitong.wallet.server.service.SellerCoinInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("console/sellerCoinInfo")
public class SellerCoinInfoController extends BaseController{


    @Resource
    private SellerCoinInfoService coinInfoService;


    /**
     * 商家币信息 分页信息
     * @param coinInfo
     * @return
     */
    @RequestMapping("infos")
    public Map<String,Object> getSellerCoinInfosByPage(@RequestBody  SellerCoinInfoVo coinInfo){
    	setGlobalAdminAppId(coinInfo);
        if(coinInfo.getId() == null){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_35002);
        }
        Map<String, Object> stringObjectMap = coinInfoService.querySellerCoinInfoByPage(coinInfo);
        return JsonUtil.toJsonSuccess("获取列表成功",stringObjectMap);
    }


    /**
     * 商家币信息 添加信息
     * @param coinInfo
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Map<String,Object> addSellerCoinInfo(@RequestBody SellerCoinInfo coinInfo){
    	setGlobalAdminAppId(coinInfo);
        coinInfo.setCoinCode(generateCode());
        coinInfo.setzId(UuidUtil.getUUID());
        if(coinInfoService.addSellerCoinInfo(coinInfo)){
            return JsonUtil.toJsonSuccess("添加商家币种信息成功",1);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31437);
        }
    }

    /**
     * 商家币信息 修改信息
     * @param coinInfo
     * @return
     */
    @RequestMapping("update")
    public Map<String,Object> updateSeller(@RequestBody SellerCoinInfo coinInfo){
        if(coinInfoService.updateSellerCoinInfo(coinInfo)){
            return JsonUtil.toJsonSuccess("修改商家信息成功",1);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31438);
        }
    }


    /**
     * 商家币信息 修改信息
     * @param coinInfo
     * @return
     */
    @RequestMapping("delete")
    public Map<String,Object> deleteSeller(@RequestBody SellerCoinInfo coinInfo){
        if(coinInfoService.deleteSellerCoinInfo(coinInfo)){
            return JsonUtil.toJsonSuccess("删除商家信息成功",1);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31439);
        }
    }

    /**
     * 生成唯一访问标识
     * @return
     */
    private  synchronized String generateCode(){
        return Long.toHexString(System.currentTimeMillis()+66688815001376681l);
    }

}
