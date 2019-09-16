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
    public  List<ClientWorkOverTime> getListByUid(long uid)throws Exception;
}
