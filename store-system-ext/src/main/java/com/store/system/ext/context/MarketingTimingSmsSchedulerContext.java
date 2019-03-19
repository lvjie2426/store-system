package com.store.system.ext.context;

import com.google.common.util.concurrent.Uninterruptibles;
import com.store.system.model.MarketingTimingSms;
import com.store.system.service.MarketingTimingSmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MarketingTimingSmsSchedulerContext implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(MarketingTimingSmsSchedulerContext.class);

    @Resource
    private MarketingTimingSmsService marketingTimingSmsService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new Processer());
        thread.start();
    }

    class Processer implements Runnable {
        @Override
        public void run() {
            while(true) {
                Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
                try {
                    long currentTime = System.currentTimeMillis();
                    List<MarketingTimingSms> smsList = marketingTimingSmsService.getNoSendList(5);
                    for(MarketingTimingSms sms : smsList) {
                        if(currentTime >= sms.getSendTime()) {
                            marketingTimingSmsService.sendSMS(sms);
                            sms.setSend(MarketingTimingSms.send_yes);
                            marketingTimingSmsService.update(sms);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
