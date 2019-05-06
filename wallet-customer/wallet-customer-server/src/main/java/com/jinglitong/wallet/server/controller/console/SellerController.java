package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.SellerService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.SessionUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.Seller;
import com.jinglitong.wallet.api.model.view.SellerVo;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("console/seller")
public class SellerController extends BaseController{


    @Resource
    private SellerService sellerService;


    /**
     * 商家信息 分页信息
     * @param sellerVo
     * @return
     */
    @RequestMapping(value = "infos",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getSellersByPage(@RequestBody SellerVo sellerVo){
    	setGlobalAdminAppId(sellerVo);
        Map<String, Object> stringObjectMap = sellerService.querySellerByPage(sellerVo);
        return JsonUtil.toJsonSuccess("获取列表成功",stringObjectMap);
    }


    /**
     * 商家信息 添加信息
     * @param seller
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addSeller(@RequestBody Seller seller){
    	setGlobalAdminAppId(seller);
        //唯一标识
        seller.setAccessKey(generateAccesskey());
        //app访问标识
        seller.setAppAccessInfo(generateAccesskey());
        seller.setSellerId(UuidUtil.getUUID());
        Admin userInfo = (Admin) SessionUtil.getUserInfo();
        if(userInfo!=null)
            seller.setCreateBy(userInfo.getUsername());
        if(sellerService.addSeller(seller)){
            return JsonUtil.toJsonSuccess("添加商家信息成功",1);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31434);
        }
    }

    /**
     * 商家信息 修改信息
     * @param seller
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateSeller(@RequestBody Seller seller){
    	setGlobalAdminAppId(seller);
        Admin userInfo = (Admin)SessionUtil.getUserInfo();
        if(userInfo!=null)
            seller.setCreateBy(userInfo.getUsername());
        if(sellerService.updateSeller(seller)){
            return JsonUtil.toJsonSuccess("修改商家信息成功",1);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31435);
        }
    }


    /**
     * 商家信息 删除信息
     * @param seller
     * @return
     */
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteSeller(@RequestBody Seller seller){
        if(sellerService.deleteSeller(seller)){
            return JsonUtil.toJsonSuccess("删除商家信息成功",1);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31436);
        }
    }
    /**
     * 商家信息 状态修改
     * @param
     * @return
     */
    @RequestMapping(value = "/updateState",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateState(@RequestBody Seller seller){
    	setGlobalAdminAppId(seller);
        if(sellerService.updateSellerState(seller)){
            return JsonUtil.toJsonSuccess("修改商家状态成功",1);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31436);
        }
    }

    /**
     * 生成唯一访问标识
     * @return
     */
    private  synchronized String generateAccesskey(){
        return Long.toHexString(System.currentTimeMillis()+1008666615001376681l);
    }


    public static void main(String[] args){
//        System.out.println(generateAccesskey());
    }


}
