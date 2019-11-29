package com.store.system.service;

import com.store.system.model.StoreGiftActivity;

import java.util.List;

/**
 * @ClassName StoreGiftActivityService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/26 16:50
 * @Version 1.0
 **/
public interface StoreGiftActivityService {

    StoreGiftActivity add(StoreGiftActivity storeGiftActivity) throws Exception;

    boolean delete(long id) throws Exception;

    boolean update(StoreGiftActivity storeGiftActivity) throws Exception;

    boolean updateOpen(long id, int open) throws Exception;

    List<StoreGiftActivity> getAllList(long psid) throws Exception;

    List<StoreGiftActivity> getIngList(long psid) throws Exception;

    List<StoreGiftActivity> getHistoryList(long psid) throws Exception;

}
