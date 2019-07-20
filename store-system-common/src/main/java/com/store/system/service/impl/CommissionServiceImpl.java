package com.store.system.service.impl;

import com.store.system.dao.CommissionDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Commission;
import com.store.system.model.Subordinate;
import com.store.system.service.CommissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName CommissionServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/13 19:27
 * @Version 1.0
 **/
@Service
public class CommissionServiceImpl implements CommissionService {

    @Resource
    private CommissionDao commissionDao;

    @Resource
    private SubordinateDao subordinateDao;
    @Override
    public Commission add(Commission commission) throws Exception {
        check(commission);
        return commissionDao.insert(commission);
    }

    @Override
    public boolean update(Commission commission) throws Exception {
        return commissionDao.update(commission);
    }

    private void check(Commission commission)throws Exception{
        if(commission.getSubId()<=0){
            Subordinate subordinate = subordinateDao.load(commission.getSubId());
            if(subordinate.getPid()==0){ throw new StoreSystemException("门店ID错误!"); }
        }
        if(commission.getSpuId() ==0 ){ throw  new StoreSystemException("SPUid不能为空");}
    }

}
