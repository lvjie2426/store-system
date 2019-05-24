package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.User;
import com.store.system.model.UserGradeCategoryDiscount;

import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-05-15 15:15
 **/

public interface UserGradeCategoryDiscountDao extends HDao<UserGradeCategoryDiscount> {
   public List<UserGradeCategoryDiscount> getAllBySPUId(long spuid)throws Exception;
}
