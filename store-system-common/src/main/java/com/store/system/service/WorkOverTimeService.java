package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientWorkOverTime;
import com.store.system.model.WalletStatistics;
import com.store.system.model.attendance.WorkOverTime;

import java.util.List;

public interface WorkOverTimeService {

    /**
     * 申请加班
     */
    public WorkOverTime add(WorkOverTime workOverTime) throws  Exception;

    /**
     * 加班详情
     * @param id
     * @return
     * @throws Exception
     */
    public ClientWorkOverTime load(long id) throws  Exception;

    /**
     * 个人加班历史记录
     * @param uid
     * @return
     * @throws Exception
     */
    public  Pager  getListByUid(long uid,Pager pager)throws Exception;

    /**
     * 审核加班通过
     * @param id
     * @return
     */
    public boolean pass(long id)throws Exception;

    /**
     * 审核加班不通过
     * @param id
     * @return
     * @throws Exception
     */
    public boolean nopass(long id,String reason)throws Exception;

    /**
     * pc 获取加班申请 列表
     * @param userName
     * @param startTime
     * @param endTime
     * @param pager
     * @return
     * @throws Exception
     */
    public Pager getList(long subid,long sid, String userName, long startTime, long endTime, Pager pager)throws Exception;

    public boolean passMore(List<Long> ids)throws Exception;

}
