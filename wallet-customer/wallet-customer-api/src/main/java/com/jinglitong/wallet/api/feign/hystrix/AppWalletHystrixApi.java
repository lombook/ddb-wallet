package com.jinglitong.wallet.api.feign.hystrix;

import com.jinglitong.wallet.api.feign.AppWalletFeignApi;
import com.jinglitong.wallet.common.utils.R;
import org.springframework.stereotype.Component;

@Component
public class AppWalletHystrixApi implements AppWalletFeignApi{

    @Override
    public Long getAppWalletIdByIden(String appIden) {
        return -1l;
    }
}
