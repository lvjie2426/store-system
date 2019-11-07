package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Mission;

import java.util.List;


public interface MissionDao extends HDao<Mission> {

    List<Mission> getAllList(long sid, int status)throws Exception;

}
