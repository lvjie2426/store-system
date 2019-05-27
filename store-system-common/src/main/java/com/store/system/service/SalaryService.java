package com.store.system.service;

import com.store.system.model.Salary;

import java.util.List;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.service
 * @ClassName: SalaryService
 * @Author: LiHaoJie
 * @Description: 工资单
 * @Date: 2019/5/27 15:49
 * @Version: 1.0
 */
public interface SalaryService {

    public Salary add(Salary salary)throws Exception;

    public List<Salary> loadSalaryByUser(long uid)throws Exception;
}
