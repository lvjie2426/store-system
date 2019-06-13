package com.store.system.service;

import com.store.system.client.ClientAfterSaleDetail;

import java.util.List;

/**
 * @ClassName AfterSaleDetailServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:26
 * @Version 1.0
 **/
public interface AfterSaleDetailService {

    public List<ClientAfterSaleDetail> getAllList(long asId) throws Exception;

}
