package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientFillCard;
import com.store.system.model.attendance.FillCard;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-18 16:15
 **/

public interface FillCardService {

   public boolean nopass(long id)throws Exception;

    public boolean pass(long id)throws Exception;

    public ClientFillCard load(long id)throws Exception;

    public FillCard add(FillCard fillCard)throws Exception;

    public Pager getListByUid(long id, Pager pager)throws Exception;
}
