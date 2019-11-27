package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientLeave;
import com.store.system.model.User;
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
    public Pager getListByUid(Pager pager, long uid)throws Exception;

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

    /**
     * 获取全部请假列表
     * @param pager
     * @param type
     * @param endTime
     * @param startTime
     * @return
     */
    public Pager getList(Pager pager, int type, long endTime, long startTime)throws Exception;

    /**
     * 编辑请假信息
     * @param leave
     * @return
     * @throws Exception
     */
    public Boolean update(Leave leave,User user)throws Exception;
}
