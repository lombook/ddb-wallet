package com.jinglitong.wallet.api.feign.hystrix;

import com.jinglitong.wallet.api.feign.LockCoinRecordFeignApi;
import org.springframework.stereotype.Component;

@Component
public class LockCoinRecordHystrixApi implements LockCoinRecordFeignApi {
    @Override
    public void confirmPayStatus() {

    }
}
