package com.store.system.service;


import com.store.system.model.Mission;
import com.store.system.model.SubordinateMissionPool;

public interface SubordinateMissionPoolService {

    public SubordinateMissionPool update(SubordinateMissionPool subordinateMissionPool,Mission mission)throws Exception;
}
