package com.store.system.service.impl;

import com.store.system.dao.PayInfoDao;
import com.store.system.model.PayInfo;
import com.store.system.service.PayInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PayInfoServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/27 11:03
 * @Version 1.0
 **/
@Service
public class PayInfoServiceImpl implements PayInfoService {


    @Resource
    private PayInfoDao payInfoDao;

    @Override
    public PayInfo insert(PayInfo payInfo) throws Exception {
        return payInfoDao.insert(payInfo);
    }

    @Override
    public List<PayInfo> getAllList(long boId, int status) throws Exception {
        return payInfoDao.getAllList(boId,PayInfo.status_pay);
    }

    @Override
    public PayInfo load(long id) throws Exception {
        return payInfoDao.load(id);
    }

    @Override
    public boolean update(PayInfo payInfo) throws Exception {
        return payInfoDao.update(payInfo);
    }
}
