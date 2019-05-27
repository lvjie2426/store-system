package com.store.system.service.impl;

import com.quakoo.ext.RowMapperHelp;
import com.store.system.dao.SalaryDao;
import com.store.system.model.Salary;
import com.store.system.model.SalaryRecord;
import com.store.system.service.SalaryService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.service.impl
 * @ClassName: SalaryServiceImpl
 * @Author: LiHaoJie
 * @Description: 工资单
 * @Date: 2019/5/27 15:50
 * @Version: 1.0
 */
@Service
public class SalaryServiceImpl implements SalaryService{

    private RowMapperHelp<Salary> rowMapper = new RowMapperHelp<>(Salary.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private SalaryDao salaryDao;
    @Override
    public Salary add(Salary salary) throws Exception {
        return salaryDao.insert(salary);
    }

    @Override
    public List<Salary> loadSalaryByUser(long uid) throws Exception {
        String sql = "SELECT  *  FROM `salary` where 1=1 and 'status' = " + Salary.status_nomore;
        {
            sql = sql + " and uid = " + uid;
        }
        sql = sql + " order  by ctime desc";
        List<Salary> salaries = jdbcTemplate.query(sql,rowMapper);
        return salaries;
    }
}
