package com.store.system.service.impl;


import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.dao.MarketingShotMsgDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.MarketingShotMsg;
import com.store.system.model.ProductSPU;
import com.store.system.model.Subordinate;
import com.store.system.service.MarketingShotMsgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MarketingShotMsgServiceImpl implements MarketingShotMsgService {

    @Resource
    private MarketingShotMsgDao marketingShotMsgDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private void check(MarketingShotMsg marketingShotMsg) throws StoreSystemException {
        if(StringUtils.isBlank(marketingShotMsg.getTitle())) throw new StoreSystemException("标题不能为空");
        Subordinate subordinate = subordinateDao.load(marketingShotMsg.getSubid());
        if(null == subordinate || subordinate.getPid() > 0) throw new StoreSystemException("公司ID错误");
    }

    @Override
    public MarketingShotMsg add(MarketingShotMsg marketingShotMsg) throws Exception {
        check(marketingShotMsg);
        marketingShotMsg = marketingShotMsgDao.insert(marketingShotMsg);
        return marketingShotMsg;
    }

    @Override
    public boolean update(MarketingShotMsg marketingShotMsg) throws Exception {
        check(marketingShotMsg);
        boolean res = marketingShotMsgDao.update(marketingShotMsg);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        MarketingShotMsg marketingShotMsg = marketingShotMsgDao.load(id);
        if(marketingShotMsg != null) {
            marketingShotMsg.setStatus(MarketingShotMsg.status_delete);
            return marketingShotMsgDao.update(marketingShotMsg);
        }
        return false;
    }

    @Override
    public Pager getBackPager(Pager pager, long subid) throws Exception {
        String sql = "SELECT * FROM `marketing_shot_msg` where `status` = " + MarketingShotMsg.status_nomore;
        String sqlCount = "SELECT COUNT(id) FROM `marketing_shot_msg` where `status` = " + MarketingShotMsg.status_nomore;
        return null;
    }
}
