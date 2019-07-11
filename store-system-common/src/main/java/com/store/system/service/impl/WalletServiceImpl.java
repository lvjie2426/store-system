package com.store.system.service.impl;

import com.quakoo.baseFramework.lock.ZkLock;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.baseFramework.property.PropertyLoader;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.dao.*;
import com.store.system.model.User;
import com.store.system.model.Wallet;
import com.store.system.model.WalletDetail;
import com.store.system.model.WalletStatistics;
import com.store.system.service.WalletService;
import com.store.system.util.ArithUtils;
import com.store.system.util.Constant;
import com.store.system.util.PropertyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService, InitializingBean {

    private PropertyUtil propertyUtil = PropertyUtil.getInstance("dao.properties");

    PropertyLoader loader = PropertyLoader.getInstance("dao.properties");

    @Resource
    private WalletDao walletDao;

    @Resource
    private WalletDetailDao walletDetailDao;

    @Resource
    private UserDao userDao;

    @Resource
    private WalletStatisticsDao walletStatisticsDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private String projectName;
    private String lockZkAddress;

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        this.lockZkAddress = propertyUtil.getProperty("zookeeper.address");
        this.projectName = propertyUtil.getProperty("project.name");
    }


    private void _recordDetail(long uid, int type, double money, String detail, String desc) {
        WalletDetail walletDetail = new WalletDetail();
        walletDetail.setUid(uid);
        walletDetail.setType(type);
        walletDetail.setMoney(money);
        walletDetail.setDetail(detail);
        walletDetail.setDesc(desc);
        walletDetailDao.insert(walletDetail);
    }

    @Override
    public void recordDetail(final long uid, final int type, final double money, final String detail, final String desc, final boolean async) throws Exception {
        if(async) {
            Constant.sync_executor.submit(new Runnable() {
                @Override
                public void run() {
                    _recordDetail(uid, type, money, detail, desc);
                }
            });
        } else {
            _recordDetail(uid, type, money, detail, desc);
        }
    }

    @Override
    public double loadMoney(long uid) throws Exception {
        double res = 0;
        Wallet wallet = walletDao.load(uid);
        if(null != wallet) res = wallet.getMoney();
        return res;
    }

    @Override
    public void refresh(long uid, double money) throws Exception {
        ZkLock lock = null;
        try {
            lock = ZkLock.getAndLock(lockZkAddress, projectName, projectName + "_wallet_" + uid + "_lock",
                    true, 5000, 10000);
            Wallet wallet = walletDao.load(uid);
            if(null == wallet) {
                wallet = new Wallet();
                wallet.setUid(uid);
                wallet.setMoney(money);
                walletDao.insert(wallet);
            } else {
                wallet.setMoney(ArithUtils.add(wallet.getMoney() , money));
                walletDao.update(wallet);
            }
        } finally {
            if(null != lock) lock.release();
        }
    }

    @Override
    public Pager getPager(final long uid, Pager pager) throws Exception {
        return new PagerRequestService<WalletDetail>(pager, 0) {
            @Override
            public List<WalletDetail> step1GetPageResult(String cursor, int size) throws Exception {
                return walletDetailDao.getPageList(uid, Double.parseDouble(cursor), size);
            }
            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }
            @Override
            public List<WalletDetail> step3FilterResult(List<WalletDetail> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }
            @Override
            public List<?> step4TransformData(List<WalletDetail> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }
        }.getPager();
    }

    @Override
    public Pager getPager(final long uid, final int type, Pager pager) throws Exception {
        return new PagerRequestService<WalletDetail>(pager, 0) {
            @Override
            public List<WalletDetail> step1GetPageResult(String cursor, int size) throws Exception {
                return walletDetailDao.getPageList(uid, type, Double.parseDouble(cursor), size);
            }
            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }
            @Override
            public List<WalletDetail> step3FilterResult(List<WalletDetail> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }
            @Override
            public List<?> step4TransformData(List<WalletDetail> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }
        }.getPager();
    }

    @Override
    public List<WalletStatistics> getStatistics(int startDate, int endDate) throws Exception {
        return walletStatisticsDao.getBetweenDateList(startDate, endDate);
    }

}
