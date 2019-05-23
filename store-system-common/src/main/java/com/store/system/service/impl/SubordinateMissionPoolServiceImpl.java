package com.store.system.service.impl;

import com.quakoo.ext.RowMapperHelp;
import com.store.system.dao.SubordinateMissionPoolDao;
import com.store.system.model.Mission;
import com.store.system.model.SubordinateMissionPool;
import com.store.system.service.SubordinateMissionPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SubordinateMissionPoolServiceImpl  implements SubordinateMissionPoolService{

    @Resource
    private SubordinateMissionPoolDao subordinateMissionPoolDao;
    @Override
    public SubordinateMissionPool load(long mid,long sid) throws Exception {
        return subordinateMissionPoolDao.load(mid,sid);
    }
}
