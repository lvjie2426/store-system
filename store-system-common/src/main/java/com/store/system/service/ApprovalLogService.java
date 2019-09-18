package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.bean.AfterSale;
import com.store.system.client.ClientApprovalLog;
import com.store.system.model.AfterSaleLog;
import com.store.system.model.attendance.ApprovalLog;

import java.util.List;

/**
 * @ClassName AfterSaleLogService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:25
 * @Version 1.0
 **/
public interface ApprovalLogService {

    /**
     * 获取审批列表
     * @param id
     * @return
     * @throws Exception
     */
    public Pager getList(long id,Pager pager) throws Exception;


}
