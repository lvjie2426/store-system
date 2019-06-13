package com.store.system.service.impl;

import com.store.system.dao.CommissionDao;
import com.store.system.model.Commission;
import com.store.system.service.CommissionService;
import org.springframework.stereotype.Service;

/**
 * @ClassName CommissionServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/13 19:27
 * @Version 1.0
 **/
@Service
public class CommissionServiceImpl implements CommissionService {

    private CommissionDao commissionDao;

    @Override
    public Commission add(Commission commission) throws Exception {
        return commissionDao.insert(commission);
    }

    @Override
    public boolean update(Commission commission) throws Exception {
        return commissionDao.update(commission);
    }
}
