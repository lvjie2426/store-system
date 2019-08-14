package com.store.system.service;


import com.store.system.model.Mission;
import com.store.system.model.UserLog;
import com.store.system.model.UserMissionPool;

import java.util.List;

public interface UserLogService {

    public UserLog add(UserLog userLog)throws Exception;

    public List<UserLog> getInfoByUid(long uid, int type)throws Exception;
}
