package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Salary;

import java.util.List;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.dao
 * @ClassName: SalaryService
 * @Author: LiHaoJie
 * @Description: 工资单
 * @Date: 2019/5/27 15:46
 * @Version: 1.0
 */
public interface SalaryDao extends HDao<Salary>{

    public List<Salary> getAll(long uid)throws Exception;
}
