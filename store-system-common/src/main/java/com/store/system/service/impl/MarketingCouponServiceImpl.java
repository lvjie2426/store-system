package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.dao.MarketingCouponDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.MarketingCoupon;
import com.store.system.model.MarketingTimingSms;
import com.store.system.model.Subordinate;
import com.store.system.service.MarketingCouponService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Service
public class MarketingCouponServiceImpl implements MarketingCouponService {

    @Resource
    private MarketingCouponDao marketingCouponDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<MarketingCoupon> rowMapper = new RowMapperHelp<>(MarketingCoupon.class);

    private void check(MarketingCoupon marketingCoupon) throws StoreSystemException {
        if(StringUtils.isBlank(marketingCoupon.getTitle())) throw new StoreSystemException("标题不能为空");
        if(marketingCoupon.getDescFull() == 0 || marketingCoupon.getDescSubtract() == 0) throw new StoreSystemException("描述数值不能为0");
        if(marketingCoupon.getConditionType() == MarketingCoupon.condition_type_expense_money
                || marketingCoupon.getConditionType() == MarketingCoupon.condition_type_expense_num) {
            if(marketingCoupon.getConditionFull() == 0) throw new StoreSystemException("获取条件不能为0");
        }
        if(marketingCoupon.getStartTime() == 0 || marketingCoupon.getEndTime() == 0) throw new StoreSystemException("有效期不能为0");
        if(marketingCoupon.getEndTime() < marketingCoupon.getStartTime()) throw new StoreSystemException("有效期错误");

        Subordinate subordinate = subordinateDao.load(marketingCoupon.getSubid());
        if(null == subordinate || subordinate.getPid() > 0) throw new StoreSystemException("公司ID错误");
    }

    @Override
    public MarketingCoupon add(MarketingCoupon marketingCoupon) throws Exception {
        check(marketingCoupon);
        marketingCoupon = marketingCouponDao.insert(marketingCoupon);
        return marketingCoupon;
    }

    @Override
    public boolean updateOpen(long id, int open) throws Exception {
        MarketingCoupon marketingCoupon = marketingCouponDao.load(id);
        if(marketingCoupon != null) {
            marketingCoupon.setOpen(open);
            return marketingCouponDao.update(marketingCoupon);
        }
        return false;
    }

    @Override
    public boolean updateSort(long id, long sort) throws Exception {
        MarketingCoupon marketingCoupon = marketingCouponDao.load(id);
        if(marketingCoupon != null) {
            marketingCoupon.setSort(sort);
            return marketingCouponDao.update(marketingCoupon);
        }
        return false;
    }

    @Override
    public boolean del(long id) throws Exception {
        MarketingCoupon marketingCoupon = marketingCouponDao.load(id);
        if(marketingCoupon != null) {
            marketingCoupon.setStatus(MarketingCoupon.status_delete);
            return marketingCouponDao.update(marketingCoupon);
        }
        return false;
    }

    @Override
    public List<MarketingCoupon> getAllList(long subid) throws Exception {
        List<MarketingCoupon> res = marketingCouponDao.getAllList(subid, MarketingCoupon.status_nomore);
        return res;
    }

    @Override
    public Pager getBackPager(Pager pager, long subid) throws Exception {
        String sql = "SELECT * FROM `marketing_coupon` where `status` = " + MarketingCoupon.status_nomore
                + " and subid = " + subid;
        String sqlCount = "SELECT COUNT(id) FROM `marketing_coupon` where `status` = " + MarketingCoupon.status_nomore
                + " and subid = " + subid;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `sort` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<MarketingCoupon> list = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(list);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public List<MarketingCoupon> getCanUseList(long subid, int money, int num, long time) throws Exception {
        List<MarketingCoupon> res = marketingCouponDao.getAllList(subid, MarketingCoupon.status_nomore, MarketingCoupon.open_yes);
        for(Iterator<MarketingCoupon> it = res.iterator(); it.hasNext();) {
            MarketingCoupon marketingCoupon = it.next();
            long startTime = marketingCoupon.getStartTime();
            long endTime = marketingCoupon.getEndTime();
            if(time < startTime ||  time > endTime) {
                it.remove();
                continue;
            }
            int conditionFull = marketingCoupon.getConditionFull();
            int conditionType = marketingCoupon.getConditionType();
            if(conditionType == MarketingCoupon.condition_type_expense_num) {
                if(num < conditionFull) {
                    it.remove();
                    continue;
                }
            }
            if(conditionType == MarketingCoupon.condition_type_expense_money) {
                if(money < conditionFull) {
                    it.remove();
                }
            }
        }
        return res;
    }

    @Override
    public int calculateMoney(long mcid, int num, int money) throws Exception {
        int res = money;
        MarketingCoupon marketingCoupon = marketingCouponDao.load(mcid);
        if(marketingCoupon != null) {
            int descFullType = marketingCoupon.getDescFullType();
            int descSubtractType = marketingCoupon.getDescSubtractType();
            int descFull = marketingCoupon.getDescFull();
            double descSubtract = marketingCoupon.getDescSubtract();
            boolean sign = false;
            if(descFullType == MarketingCoupon.desc_full_type_num) {
                if(num >= descFull) sign = true;
            } else if(descFullType == MarketingCoupon.desc_full_type_money) {
                if(money >= descFull) sign = true;
            }
            if(sign) {
                if(descSubtractType == MarketingCoupon.desc_subtract_type_money) {
                    res = money - (int)descSubtract;
                } else if(descSubtractType == MarketingCoupon.desc_subtract_type_rate) {
                    BigDecimal bigDecimal = new BigDecimal(res * descSubtract);
                    res = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                }
            }
        }
        return res;
    }

}
