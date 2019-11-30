package com.store.system.service;

import com.store.system.model.SpendCardSet;

import java.util.List;

/**
 * @ClassName SpendCardSetService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 10:45
 * @Version 1.0
 **/
public interface SpendCardSetService {

    SpendCardSet add(SpendCardSet spendCardSet) throws Exception;

    boolean delete(long id) throws Exception;

    boolean update(SpendCardSet spendCardSet) throws Exception;

    List<SpendCardSet> getAllList(long psid, long cid, long spuId) throws Exception;

}
