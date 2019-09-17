package com.store.system.service;

import com.store.system.client.ClientLeave;
import com.store.system.model.attendance.Leave;
import com.store.system.model.attendance.WorkOverTime;

import java.util.List;

public interface LeaveService {

    /**
     * 申请请假
     */
    public Leave add(Leave leave) throws  Exception;

    /**
     * 请假详情
     * @param id
     * @return
     * @throws Exception
     */
    public ClientLeave load(long id) throws  Exception;

    /**
     * 个人请假历史记录
     * @param uid
     * @return
     * @throws Exception
     */
    public List<ClientLeave> getListByUid(long uid)throws Exception;

    /**
     * 审核通过
     * @param id
     * @return
     * @throws Exception
     */
    public boolean pass(long id)throws Exception;
    /**
     * 审核bu通过
     * @param id
     * @return
     * @throws Exception
     */
    public boolean nopass(long id, String reason)throws Exception;
}
