package com.store.system.service;

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
    public List<ClientChangeShift> getListByUid(long uid)throws Exception;

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
}
