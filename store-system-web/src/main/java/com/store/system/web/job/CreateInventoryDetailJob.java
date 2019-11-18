package com.store.system.web.job;

import com.google.common.util.concurrent.Uninterruptibles;
import com.store.system.client.ClientSubordinate;
import com.store.system.dao.InventoryDetailDao;
import com.store.system.dao.ProductCategoryDao;
import com.store.system.dao.ProductSKUDao;
import com.store.system.dao.ProductSPUDao;
import com.store.system.model.*;
import com.store.system.service.SubordinateService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CreateInventoryDetailJob
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/16 16:29
 * @Version 1.0
 **/
@Log4j
@Component
public class CreateInventoryDetailJob implements InitializingBean {

    @Resource
    private SubordinateService subordinateService;

    @Resource
    private InventoryDetailDao inventoryDetailDao;

    @Resource
    private ProductSKUDao productSKUDao;

    @Resource
    private ProductSPUDao productSPUDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread receiveThread = new Thread(new PastDetailProcesser());
        receiveThread.start();
    }


    class PastDetailProcesser implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Uninterruptibles.sleepUninterruptibly(12, TimeUnit.HOURS);
                    long currentTime = System.currentTimeMillis();
                    List<Subordinate> subordinates = subordinateService.getAllParentSubordinate();
                    for (Subordinate subordinate : subordinates) {
                        List<ClientSubordinate> subs = subordinateService.getTwoLevelAllList(subordinate.getId());
                        for (Subordinate sub : subs) {
                            List<InventoryDetail> detailList = inventoryDetailDao.getAllListBySubId(sub.getId(), InventoryDetail.status_normal);
                            for (InventoryDetail detail : detailList) {
                                ProductSPU spu = productSPUDao.load(detail.getP_spuid());
                                ProductSKU sku = productSKUDao.load(detail.getP_skuid());
                                if (spu.getCid() == 3) {
                                    long endTime = (long) sku.getProperties().get(40L);
                                    if (endTime <= currentTime) {
                                        detail.setStatus(InventoryDetail.status_past);
                                        inventoryDetailDao.update(detail);
                                    }
                                }
                                if (spu.getCid() == 5) {
                                    long endTime = (long) sku.getProperties().get(33L);
                                    if (endTime <= currentTime) {
                                        detail.setStatus(InventoryDetail.status_past);
                                        inventoryDetailDao.update(detail);
                                    }
                                }

                            }

                        }

                    }

                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}


