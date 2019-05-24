package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.SubordinateMissionPool;

import java.util.List;

public interface SubordinateMissionPoolDao extends HDao<SubordinateMissionPool>{

    public List<SubordinateMissionPool > getList(long mid, long sid);
}
