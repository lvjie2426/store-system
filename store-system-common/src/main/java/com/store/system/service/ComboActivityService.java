package com.store.system.service;

import com.store.system.client.ClientComboActivity;
import com.store.system.model.ComboActivity;

import java.util.List;

/**
 * @ClassName ComboActivityService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:38
 * @Version 1.0
 **/
public interface ComboActivityService {

    ComboActivity add(ComboActivity comboActivity) throws Exception;

    boolean delete(long id) throws Exception;

    boolean update(ComboActivity comboActivity) throws Exception;

    boolean updateOpen(long id, int open) throws Exception;

    List<ClientComboActivity>  getAllList(long psid) throws Exception;

}
