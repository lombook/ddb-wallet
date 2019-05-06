package com.jinglitong.wallet.api.feign.hystrix;

import com.jinglitong.wallet.api.feign.FrozenReleaseFeignApi;
import com.jinglitong.wallet.api.feign.LockCoinRecordFeignApi;
import org.springframework.stereotype.Component;

@Component
public class FrozenReleaseHystrixApi implements FrozenReleaseFeignApi {

    @Override
    public void release() {

    }

    @Override
    public void check() {

    }
}
