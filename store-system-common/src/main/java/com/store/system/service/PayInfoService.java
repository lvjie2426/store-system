package com.store.system.service;

import com.store.system.model.PayInfo;

import java.util.List;

/**
 * @ClassName PayInfoService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/27 11:02
 * @Version 1.0
 **/
public interface PayInfoService {

    public PayInfo insert(PayInfo payInfo) throws Exception;

    public List<PayInfo> getAllList(long boId) throws Exception;

    public PayInfo load(long id) throws Exception;
}
