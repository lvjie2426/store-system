package com.store.system.service;


import com.store.system.model.SubordinateMissionPool;

public interface SubordinateMissionPoolService {

    public SubordinateMissionPool load(long mid,long sid)throws Exception;

    public SubordinateMissionPool update(SubordinateMissionPool subordinateMissionPool)throws Exception;
}
