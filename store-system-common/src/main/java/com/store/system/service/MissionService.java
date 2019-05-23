package com.store.system.service;


import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.Mission;


public interface MissionService {

    public Mission insert(Mission mission)throws Exception;

    public boolean update(Mission mission)throws Exception;

    public boolean del(long id)throws Exception;

    public Pager getByPager(Pager pager,long sid)throws Exception;
}
