package com.store.system.service.impl;

import com.store.system.dao.LoginUserPoolDao;
import com.store.system.model.LoginUserPool;
import com.store.system.service.LoginUserPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginUserPoolServiceImpl implements LoginUserPoolService {

    @Resource
    private LoginUserPoolDao loginUserPoolDao;

    @Override
    public LoginUserPool load(LoginUserPool loginUserPool) throws Exception {
        return loginUserPoolDao.load(loginUserPool);
    }
}
