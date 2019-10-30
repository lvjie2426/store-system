package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientUser;
import com.store.system.dao.UserDao;
import com.store.system.dao.UserGradeDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.UserGrade;
import com.store.system.service.UserGradeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserGradeServiceImpl implements UserGradeService {

    private RowMapperHelp<UserGrade> ugMapper = new RowMapperHelp<>(UserGrade.class);

    @Resource
    private UserGradeDao userGradeDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private UserDao userDao;
    @Resource
    private SubordinateDao subordinateDao;

    private void check(UserGrade userGrade, boolean isUpdate) throws StoreSystemException {
        String regex = "^[1-9]+(.[1-9]{1})?$";
        Pattern pattern = Pattern.compile(regex);
        Map<Long,Object> map = userGrade.getDiscount();
        for (Map.Entry<Long, Object> entry : map.entrySet()) {
            Matcher matcherUser = pattern.matcher(String.valueOf(entry.getValue()));
            if (!matcherUser.matches()) throw new StoreSystemException("类目为" + entry.getKey() + "的会员等级折扣输入格式有误！");
        }
        if (userGrade.getGainMoney() == 0) throw new StoreSystemException("积分获取金额不能为0");
        if (userGrade.getGainScore() == 0) throw new StoreSystemException("积分获取数值不能为0");
        if (userGrade.getSubstituteMoney() == 0) throw new StoreSystemException("积分抵现金额不能为0");
        if (userGrade.getSubstituteScore() == 0) throw new StoreSystemException("积分抵现数值不能为0");
        if (userGrade.getSubstituteRate() == 0) throw new StoreSystemException("积分抵现比率不能为0");

        long subid = userGrade.getSubid();

        if(!isUpdate) {
            if (userGrade.getId() == 0) throw new StoreSystemException("主键ID不能为空！");
            List<UserGrade> list = userGradeDao.getAllList(subid);
            long conditionScore = userGrade.getConditionScore();
            for (UserGrade one : list) {
                if (one.getConditionScore() == conditionScore) throw new StoreSystemException("等级条件重复");
            }
        }
    }

    @Override
    public UserGrade add(UserGrade userGrade) throws Exception {
        check(userGrade, true);
        userGrade = userGradeDao.insert(userGrade);
        return userGrade;
    }

    @Override
    public boolean update(UserGrade userGrade) throws Exception {
        check(userGrade, false);
        boolean res = userGradeDao.update(userGrade);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        return userGradeDao.delete(id);
    }

    @Override
    public UserGrade load(long id) throws Exception {
        return userGradeDao.load(id);
    }

    @Override
    public List<UserGrade> getAllList(long subid) throws Exception {
        List<UserGrade> list = userGradeDao.getAllList(subid);
        Collections.sort(list);
        return list;
    }

    @Override
    public Pager getByPager(Pager pager, long subid) throws Exception {
        String sql = "SELECT * FROM `user_grade` where subid = " + subid;
        String sqlCount = "SELECT COUNT(id) FROM `user_grade` where subid = " + subid;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = 0;
        List<UserGrade> userGrades = this.jdbcTemplate.query(sql, new RowMapperHelp<UserGrade>(UserGrade.class));
        count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(userGrades);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public UserGrade loadGrade(long subid, long score) throws Exception {
        List<UserGrade> list = userGradeDao.getAllList(subid);
        Collections.sort(list);
        UserGrade res = null;
        for (int i = 0; i < list.size(); i++) {
            UserGrade one = list.get(i);
            if (score >= one.getConditionScore()) {
                res = one;
                break;
            }
        }
        return res;
    }

    @Override
    public void checkUserScore(User user) throws Exception {
        int score = user.getScore();
        UserGrade userGrade = userGradeDao.load(user.getUserGradeId());//拿到现有的会员等级
        if(userGrade!=null){
            //判断积分
            if(score>userGrade.getConditionScore()){//等级改变
                String sql = " SELECT * FROM user_grade WHERE 1=1 AND subid = " + user.getSid() + " ORDER BY user_grade.conditionScore ";
                List<UserGrade> userGrades = jdbcTemplate.query(sql,ugMapper);
                int temp = 0;//临时保存上一次等级的条件
                for(UserGrade grade : userGrades){
                    if(score>=temp && score <=grade.getConditionScore()){
                        user.setUserGradeId(grade.getId());
                        userDao.update(user);
                        break;
                    }
                    temp = grade.getConditionScore();//用作下次比较
                }
            }
        }
    }
}
