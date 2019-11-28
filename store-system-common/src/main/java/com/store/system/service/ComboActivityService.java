package com.store.system.service;

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

    boolean updateStatus(long id, int status) throws Exception;

    List<ComboActivity> getAllList(long psid) throws Exception;

}
