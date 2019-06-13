package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.bean.AfterSale;
import com.store.system.model.AfterSaleLog;

/**
 * @ClassName AfterSaleLogService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:25
 * @Version 1.0
 **/
public interface AfterSaleLogService  {

    public AfterSaleLog add(AfterSale afterSale) throws Exception;

    public Pager getBackPager(Pager pager, long subId, String userName, String phone) throws Exception;
}
