package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.SubordinateMissionPool;

public interface SubordinateMissionPoolDao extends HDao<SubordinateMissionPool>{

    public SubordinateMissionPool load(long mid,long sid)throws Exception;
}
