package com.jinglitong.wallet.job.common.spring;

import com.jinglitong.wallet.job.service.GrowInteService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper  implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public ApplicationContextHelper() {
    }

    public static Object getIntegralAccountBean(Class<GrowInteService> growInteServiceClass) {
        return applicationContext != null?applicationContext.getBean(growInteServiceClass):null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return applicationContext != null?applicationContext.getBean(beanName):null;
    }
}