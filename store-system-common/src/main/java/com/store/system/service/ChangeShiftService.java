package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientChangeShift;
import com.store.system.model.User;
import com.store.system.model.attendance.ChangeShift;

import java.util.List;

public interface ChangeShiftService {

    /**
     * 调班申请
     */
    public ChangeShift add(ChangeShift changeShift) throws  Exception;

    /**
     * 调班详情
     * @param id
     * @return
     * @throws Exception
     */
    public ClientChangeShift load(long id) throws  Exception;

    /**
     * 个人调班历史记录
     * @param uid
     * @return
     * @throws Exception
     */
    public Pager getListByUid(long uid,Pager pager)throws Exception;

    /**
     * 个人被调班历史记录
     * @return
     * @throws Exception
     */
    public  List<ClientChangeShift> getListByReplaceUid(long id)throws Exception;

    /**
     * 审核不通过
     * @param id
     * @param reason
     * @return
     * @throws Exception
     */
    public  boolean nopass(long id, String reason)throws Exception;
    /**
     * 审核通过
     * @param id
     * @return
     * @throws Exception
     */
    public boolean pass(long id)throws Exception;

    /**
     * 被调班人拒绝
     * @param id
     * @return
     * @throws Exception
     */
    public boolean replaceNopass(long id)throws Exception;
    /**
     * 被调班人接受
     * @param id
     * @return
     * @throws Exception
     */
    public boolean replacePass(long id)throws Exception;

    /**
     * 根据条件获取调班列表
     *
     * @param subid
     * @param sid
     * @param userName
     * @param startTime
     * @param endTime
     * @param status
     * @param pager
     * @return
     * @throws Exception
     */
    public Pager getList(long subid, long sid, String userName, long startTime, long endTime, int status, Pager pager)throws Exception;
}
