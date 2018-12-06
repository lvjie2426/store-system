package com.store.system.service;

import com.store.system.model.InBill;
import com.s7.baseFramework.model.pagination.Pager;

public interface InBillService {

    public InBill add(InBill inBill) throws Exception;

    public boolean del(long id, long createUid) throws Exception;

    /**
     * 管理员获取等待审核和审核结束的入库单
     * method_name: getBackPager
     * params: [pager]
     * return: com.s7.baseFramework.model.pagination.Pager
     * creat_user: lihao
     * creat_date: 2018/12/5
     * creat_time: 15:20
     **/
    public Pager getBackPager(Pager pager) throws Exception;

    /**
     * 管理员获取等待审核的入库单
     * method_name: getBackWaitCheckPager
     * params: [pager]
     * return: com.s7.baseFramework.model.pagination.Pager
     * creat_user: lihao
     * creat_date: 2018/12/5
     * creat_time: 15:21
     **/
    public Pager getBackWaitCheckPager(Pager pager) throws Exception;

    /**
     * 管理员获取审核结束的入库单
     * method_name: getBackEndPager
     * params: [pager]
     * return: com.s7.baseFramework.model.pagination.Pager
     * creat_user: lihao
     * creat_date: 2018/12/5
     * creat_time: 15:21
     **/
    public Pager getBackEndPager(Pager pager) throws Exception;

    /**
     * 创建人获取创建的所有入库单
     * method_name: getBackPagerByCreateUid
     * params: [pager, createUid]
     * return: com.s7.baseFramework.model.pagination.Pager
     * creat_user: lihao
     * creat_date: 2018/12/5
     * creat_time: 15:22
     **/
    public Pager getBackPagerByCreateUid(Pager pager, long createUid) throws Exception;

    /**
     * 创建人获取可编辑的入库单
     * method_name: getBackEditPagerByCreateUid
     * params: [pager, createUid]
     * return: com.s7.baseFramework.model.pagination.Pager
     * creat_user: lihao
     * creat_date: 2018/12/5
     * creat_time: 15:22
     **/
    public Pager getBackEditPagerByCreateUid(Pager pager, long createUid) throws Exception;

    /**
     * 创建人获取等待审核的入库单
     * method_name: getBackWaitCheckPagerByCreateUid
     * params: [pager, createUid]
     * return: com.s7.baseFramework.model.pagination.Pager
     * creat_user: lihao
     * creat_date: 2018/12/5
     * creat_time: 15:23
     **/
    public Pager getBackWaitCheckPagerByCreateUid(Pager pager, long createUid) throws Exception;

    /**
     * 创建人获取审核结束的入库单
     * method_name: getBackEndPagerByCreateUid
     * params: [pager, createUid]
     * return: com.s7.baseFramework.model.pagination.Pager
     * creat_user: lihao
     * creat_date: 2018/12/5
     * creat_time: 15:23
     **/
    public Pager getBackEndPagerByCreateUid(Pager pager, long createUid) throws Exception;

}
