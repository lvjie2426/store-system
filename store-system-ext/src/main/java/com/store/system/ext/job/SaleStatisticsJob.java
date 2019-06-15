package com.store.system.ext.job;

import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName SaleStatisticsJob
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/15 11:09
 * @Version 1.0
 **/
public class SaleStatisticsJob implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(SaleStatisticsJob.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new handle());
        thread.start();
    }

    class handle implements Runnable {
        @Override
        public void run() {
            while(true) {
                Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
                try {
                    long currentTime = System.currentTimeMillis();

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
