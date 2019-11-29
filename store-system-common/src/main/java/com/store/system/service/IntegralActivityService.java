package com.store.system.service;

import com.store.system.client.ClientIntegralActivity;
import com.store.system.model.IntegralActivity;

import java.util.List;

/**
 * @ClassName IntegralActivityService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:10
 * @Version 1.0
 **/
public interface IntegralActivityService {

    IntegralActivity add(IntegralActivity integralActivity) throws Exception;

    boolean delete(long id) throws Exception;

    boolean update(IntegralActivity integralActivity) throws Exception;

    boolean updateStatus(long id, int status) throws Exception;

    List<IntegralActivity> getAllList(long psid) throws Exception;

    List<ClientIntegralActivity> getIngList(long psid) throws Exception;

    List<ClientIntegralActivity> getHistoryList(long psid) throws Exception;
    
}
