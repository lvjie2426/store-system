package com.store.system.service.impl;

import com.store.system.dao.UserGradeCategoryDiscountDao;
import com.store.system.model.UserGradeCategoryDiscount;
import com.store.system.service.UserGradeCategoryDiscountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void addDiscount(List<UserGradeCategoryDiscount> list,long spuid) throws Exception {

        for(UserGradeCategoryDiscount li:list){
            UserGradeCategoryDiscount userGradeCategoryDiscount=new UserGradeCategoryDiscount();
            userGradeCategoryDiscount.setSpuid(spuid);
            userGradeCategoryDiscount.setDiscount(li.getDiscount());
            userGradeCategoryDiscount.setUgid(li.getUgid());
            userGradeCategoryDiscountDao.insert(userGradeCategoryDiscount);
        }

    }
}
