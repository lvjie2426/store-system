package com.store.system.service;

import com.store.system.client.ClientSpendCardExchange;
import com.store.system.model.SpendCardExchange;

import java.util.List;

/**
 * @ClassName SpendCardExchangeService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 10:46
 * @Version 1.0
 **/
public interface SpendCardExchangeService {

    SpendCardExchange add(SpendCardExchange spendCardExchange) throws Exception;

    boolean delete(long id) throws Exception;

    boolean update(SpendCardExchange spendCardExchange) throws Exception;

    List<ClientSpendCardExchange> getAllList(long psid, long spuId) throws Exception;
}
