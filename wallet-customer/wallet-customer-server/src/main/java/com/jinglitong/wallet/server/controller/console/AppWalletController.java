package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.AppWallet;
import com.jinglitong.wallet.api.model.view.AppWalletVo;
import com.jinglitong.wallet.api.model.view.PropertieVO;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.AppWalletService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("console/wallet")
@Slf4j
public class AppWalletController  extends BaseController{

    @Resource
    AppWalletService appWalletService;

    /**
     * 钱包列表
     * @param appWalletVo
     * @return
     */
    @RequestMapping("list")
    public Map queryAppWalletByPage(@RequestBody AppWalletVo appWalletVo){
        return JsonUtil.toJsonSuccess("钱包列表", appWalletService.queryAppWalletByPage(appWalletVo));

    }


    /**
     * 新增钱包信息
     * @param appWallet
     * @return
     */
    @RequestMapping(value ="/add", method = RequestMethod.POST)
    public Map addAppWallet(@RequestBody  AppWallet appWallet){
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        appWallet.setCreatedUser(admin.getUsername());
        appWallet.setUpdatedUser(admin.getUsername());
        appWallet.setZid(UuidUtil.getUUID()); //唯一标识
        int m = appWalletService.insertAppWallet(appWallet);
        if(m > 0){
            return JsonUtil.toJsonSuccess("新增成功", m);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_36003);
        }

    }


    /**
     * 更新钱包信息
     * @param appWallet
     * @return
     */
    @RequestMapping("update")
    public Map updateAppWallet(@RequestBody AppWallet appWallet){
        AppWallet appWallet1 = checkWalletZid(appWallet.getZid());
        if(appWallet1 != null) {
            if (!(appWallet.getId()+"").equals(appWallet1.getId()+"")) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_36006);
            }
        }
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        appWallet.setUpdatedUser(admin.getUsername());
        int m = appWalletService.updateAppWallet(appWallet);
        if(m > 0){
            return JsonUtil.toJsonSuccess("更新成功", m);

        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_36003);
        }

    }

    /**
     * 获得钱包信息
     * @param appWallet
     * @return
     */
    @RequestMapping("getAppWallet")
    public Map getAppWallet(@RequestBody AppWallet appWallet){
        return JsonUtil.toJsonSuccess("获得钱包信息成功", appWalletService.getOneAppWallet(appWallet));
    }

    /**
     * 修改钱包状态信息
     * @param appWallet
     * @return
     */
    @RequestMapping("deleteAppWallet")
    public Map deleteAppWallet(@RequestBody AppWallet appWallet){
        int m = appWalletService.updateStateAppWallet(appWallet);
        if(m > 0){
            return JsonUtil.toJsonSuccess("修改成功", m);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_36003);
        }
    }



    /**
     * 生成唯一访问标识
     * @return
     */
    private  String generateCode(){
        return Long.toHexString(System.currentTimeMillis()+166688815001376681l);
    }

    public static void main(String[] args){
        System.out.println(Long.toHexString(System.currentTimeMillis()+166688815001376681l));
    }


    /**
     * appwallet列表
     *
     * @return
     */
    @RequestMapping("getWalletName")
    public Map getWalletName(){
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        setGlobalAdminAppId(admin,true);
        return JsonUtil.toJsonSuccess("钱包列表", appWalletService.getWalletName(admin));

    }
    public AppWallet checkWalletZid(String zid){
        return appWalletService.checkWalletZid(zid);
    }

}
