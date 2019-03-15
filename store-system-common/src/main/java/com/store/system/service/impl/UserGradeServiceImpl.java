package com.store.system.service.impl;

import com.store.system.dao.UserGradeDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.UserGrade;
import com.store.system.service.UserGradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class UserGradeServiceImpl implements UserGradeService {

    @Resource
    private UserGradeDao userGradeDao;

    @Resource
    private SubordinateDao subordinateDao;

    private void check(UserGrade userGrade) throws StoreSystemException {
        if(userGrade.getDiscount() == 0) throw new StoreSystemException("折扣不能为0");
        if(userGrade.getGainMoney() == 0) throw new StoreSystemException("积分获取金额不能为0");
        if(userGrade.getGainScore() == 0) throw new StoreSystemException("积分获取数值不能为0");
        if(userGrade.getSubstituteMoney() == 0) throw new StoreSystemException("积分抵现金额不能为0");
        if(userGrade.getSubstituteScore() == 0) throw new StoreSystemException("积分抵现数值不能为0");
        if(userGrade.getSubstituteRate() == 0) throw new StoreSystemException("积分抵现比率不能为0");

        long subid = userGrade.getSubid();

        List<UserGrade> list = userGradeDao.getAllList(subid);
        long conditionScore = userGrade.getConditionScore();
        for(UserGrade one : list) {
            if(one.getConditionScore() == conditionScore) throw new StoreSystemException("等级条件重复");
        }
    }

    @Override
    public UserGrade add(UserGrade userGrade) throws Exception {
        check(userGrade);
        userGrade = userGradeDao.insert(userGrade);
        return userGrade;
    }

    @Override
    public boolean update(UserGrade userGrade) throws Exception {
        check(userGrade);
        boolean res = userGradeDao.update(userGrade);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        return userGradeDao.delete(id);
    }

    @Override
    public List<UserGrade> getAllList(long subid) throws Exception {
        List<UserGrade> list = userGradeDao.getAllList(subid);
        Collections.sort(list);
        return list;
    }

    @Override
    public UserGrade loadGrade(long subid, long score) throws Exception {
        List<UserGrade> list = userGradeDao.getAllList(subid);
        Collections.sort(list);
        UserGrade res = null;
        for(int i = 0; i < list.size(); i++) {
            UserGrade one = list.get(i);
            if(score >= one.getConditionScore()) {
                res = one;
                break;
            }
        }
        return res;
    }

}
