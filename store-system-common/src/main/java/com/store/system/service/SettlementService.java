package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientSettlement;
import com.store.system.model.Settlement;

/**
 * @ClassName SettlementService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/8 16:06
 * @Version 1.0
 **/
public interface SettlementService {

    public Settlement insert(Settlement settlement) throws Exception;

    public ClientSettlement loadClient(long subId) throws Exception;

    public Pager getPager(Pager pager, long subId) throws Exception;
}
