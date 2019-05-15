package com.store.system.service.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.dao.MarketingTimingSmsDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.MarketingTimingSms;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.MarketingTimingSmsService;
import com.store.system.util.SubmailMultiItem;
import com.store.system.util.SubmailUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MarketingTimingSmsServiceImpl implements MarketingTimingSmsService {

    @Resource
    private MarketingTimingSmsDao marketingTimingSmsDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private UserDao userDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<MarketingTimingSms> rowMapper = new RowMapperHelp<>(MarketingTimingSms.class);

    private void check(MarketingTimingSms marketingTimingSms) throws StoreSystemException {
        if (StringUtils.isBlank(marketingTimingSms.getContent())) throw new StoreSystemException("内容不能为空");
        Subordinate subordinate = subordinateDao.load(marketingTimingSms.getSubid());
        if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
        if (marketingTimingSms.getSendTime() == 0) throw new StoreSystemException("发送时间错误");

        Pattern pattern = Pattern.compile("@var\\([a-zA-Z_]+\\)");
        Matcher matcher = pattern.matcher(marketingTimingSms.getContent());
        List<String> list = Lists.newArrayList();
        while(matcher.find()) {
            list.add(matcher.group());
        }
        List<String> tags = marketingTimingSms.getTags();
        if(list.size() != tags.size()) throw new StoreSystemException("标签错误");
        for(String one : list) {
            if(!tags.contains(one)) throw new StoreSystemException("标签错误");
        }
    }

    @Override
    public MarketingTimingSms add(MarketingTimingSms marketingTimingSms) throws Exception {
        check(marketingTimingSms);
        marketingTimingSms = marketingTimingSmsDao.insert(marketingTimingSms);
        return marketingTimingSms;
    }

    @Override
    public boolean update(MarketingTimingSms marketingTimingSms) throws Exception {
        check(marketingTimingSms);
        boolean res = marketingTimingSmsDao.update(marketingTimingSms);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        MarketingTimingSms marketingTimingSms = marketingTimingSmsDao.load(id);
        if (marketingTimingSms != null) {
            marketingTimingSms.setStatus(MarketingTimingSms.status_delete);
            return marketingTimingSmsDao.update(marketingTimingSms);
        }
        return false;
    }

    @Override
    public Pager getBackPager(Pager pager, long subid) throws Exception {
        String sql = "SELECT * FROM `marketing_timing_sms` where `status` = " + MarketingTimingSms.status_nomore
                + " and subid = " + subid;
        String sqlCount = "SELECT COUNT(id) FROM `marketing_timing_sms` where `status` = " + MarketingTimingSms.status_nomore
                + " and subid = " + subid;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `sendTime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<MarketingTimingSms> list = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(list);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public List<MarketingTimingSms> getNoSendList(int size) throws Exception {
        List<MarketingTimingSms> res = marketingTimingSmsDao.getList(MarketingTimingSms.status_nomore,
                MarketingTimingSms.send_no, size);
        return res;
    }

    @Override
    public void sendSMS(MarketingTimingSms marketingTimingSms) throws Exception {
        long sid = marketingTimingSms.getSubid();
        int sex = marketingTimingSms.getSex();
        int startAge = marketingTimingSms.getStartAge();
        int endAge = marketingTimingSms.getEndAge();
        List<User> users = userDao.getAllList(sid, User.userType_user, User.status_nomore);
        for (Iterator<User> it = users.iterator(); it.hasNext(); ) {
            User user = it.next();
            if (sex != MarketingTimingSms.sex_all && user.getSex() != sex) {
                it.remove();
                continue;
            }
            if(user.getAge() < startAge || user.getAge() > endAge) {
                it.remove();
            }
        }
        List<String> tags = marketingTimingSms.getTags();
        Subordinate subordinate = subordinateDao.load(sid);
        List<SubmailMultiItem> multiItems = Lists.newArrayList();
        for(User user : users) {
            if(StringUtils.isNotBlank(user.getPhone())) {
                String to = user.getPhone();
                Map<String, String> vars = Maps.newHashMap();
                if(tags.contains(MarketingTimingSms.tag_user_name)) {
                    String key = MarketingTimingSms.tag_user_name.replace("@var(", "")
                            .replace(")", "");
                    vars.put(key, user.getName());
                }
                if(tags.contains(MarketingTimingSms.tag_sub_name)) {
                    String key = MarketingTimingSms.tag_sub_name.replace("@var(", "")
                            .replace(")", "");
                    vars.put(key, subordinate.getName());
                }
                SubmailMultiItem multiItem = new SubmailMultiItem();
                multiItem.setTo(to);
                multiItem.setVars(vars);
                multiItems.add(multiItem);
            }
        }
        SubmailUtils.multiSend(multiItems, marketingTimingSms.getContent());
    }

    @Override
    public boolean updateById(MarketingTimingSms marketingTimingSms) throws Exception {
        check(marketingTimingSms);
        MarketingTimingSms load = marketingTimingSmsDao.load(marketingTimingSms.getId());
        load.setContent(marketingTimingSms.getContent());
        load.setTags(marketingTimingSms.getTags());
        boolean res = marketingTimingSmsDao.update(load);
        return res;
    }


}
