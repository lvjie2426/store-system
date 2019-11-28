package com.store.system.service;

import com.store.system.client.ClientSalePresentActivity;
import com.store.system.model.SalePresentActivity;

import java.util.List;

/**
 * @ClassName SalePresentActivityService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 11:09
 * @Version 1.0
 **/
public interface SalePresentActivityService {

    SalePresentActivity add(SalePresentActivity salePresentActivity) throws Exception;

    boolean delete(long id) throws Exception;

    boolean update(SalePresentActivity salePresentActivity) throws Exception;

    boolean updateStatus(long id, int status) throws Exception;

    List<SalePresentActivity> getAllList(long psid) throws Exception;

    List<ClientSalePresentActivity> getIngList(long psid) throws Exception;

    List<ClientSalePresentActivity> getHistoryList(long psid) throws Exception;

}
