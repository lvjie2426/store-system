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


    public  List<ClientChangeShift> getListByReplaceUid(long id)throws Exception;
}
