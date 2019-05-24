package com.store.system.service;

import com.store.system.model.UserGradeCategoryDiscount;

import java.util.List; /**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-05-15 15:14
 **/

public interface UserGradeCategoryDiscountService {
  public   void addDiscount(List<UserGradeCategoryDiscount> list,long spuid) throws Exception;

  public List<UserGradeCategoryDiscount> getAllBySPUId(long id)throws Exception;
}
