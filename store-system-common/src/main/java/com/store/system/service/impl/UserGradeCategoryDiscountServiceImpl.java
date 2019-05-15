package com.store.system.service.impl;

import com.store.system.dao.UserGradeCategoryDiscountDao;
import com.store.system.model.UserGradeCategoryDiscount;
import com.store.system.service.UserGradeCategoryDiscountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-05-15 15:14
 **/
@Service
public class UserGradeCategoryDiscountServiceImpl implements UserGradeCategoryDiscountService {

    @Resource
   private UserGradeCategoryDiscountDao userGradeCategoryDiscountDao;

    @Override
    public void addDiscount(List<UserGradeCategoryDiscount> list) throws Exception {

        for(UserGradeCategoryDiscount li:list){
            UserGradeCategoryDiscount userGradeCategoryDiscount=new UserGradeCategoryDiscount();
            userGradeCategoryDiscount.setCId(li.getCId());
            userGradeCategoryDiscount.setDiscount(li.getDiscount());
            userGradeCategoryDiscount.setUgId(li.getUgId());
            userGradeCategoryDiscountDao.insert(userGradeCategoryDiscount);
        }

    }
}
