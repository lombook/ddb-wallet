package com.jinglitong.wallet.server.controller.feign;

import com.jinglitong.wallet.api.feign.AppWalletFeignApi;
import com.jinglitong.wallet.api.model.AppWallet;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.server.service.AppWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("appWalletFeign")
@Slf4j
public class AppWalletFeignController implements AppWalletFeignApi {


    @Resource
    AppWalletService appWalletService;


    @PostMapping("checkAppIdExistFeign")
    @Override
    public Long getAppWalletIdByIden(@RequestParam("appIden") String appIden) {
        try {
            if (StringUtils.isEmpty(appIden))
                throw new RuntimeException("appIden不能为空！");
            AppWallet appWallet = new AppWallet();
            appWallet.setZid(appIden);
            appWallet = appWalletService.getOneAppWallet(appWallet);
            return Long.parseLong( appWallet.getId().toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1l;
        }

    }

}