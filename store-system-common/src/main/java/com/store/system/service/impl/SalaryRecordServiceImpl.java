package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.dao.SalaryDao;
import com.store.system.dao.SalaryRecordDao;
import com.store.system.model.Salary;
import com.store.system.model.SalaryRecord;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.SalaryRecordService;
import com.store.system.service.SalaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.service.impl
 * @ClassName: SalaryRecordServiceImpl
 * @Author: LiHaoJie
 * @Description: 工资单
 * @Date: 2019/5/27 15:50
 * @Version: 1.0
 */
@Service
public class SalaryRecordServiceImpl implements SalaryRecordService{

    private RowMapperHelp<SalaryRecord> rowMapper = new RowMapperHelp<>(SalaryRecord.class);

    @Resource
    private SalaryDao salaryDao;

    @Resource
    private SalaryRecordDao salaryRecordDao;

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public SalaryRecord add(SalaryRecord salaryRecord) throws Exception {
        return salaryRecordDao.insert(salaryRecord);
    }

    @Override
    public Pager getAllByPager(Pager pager, long psid) throws Exception {
        String sql = "SELECT  *  FROM `salary_record` where 1=1 ";
        String sqlCount = "SELECT  COUNT(*)  FROM `salary_record` where 1=1";
        String limit = "  limit %d , %d ";
        {
            sql = sql + " and psid = " + psid;
            sqlCount = sqlCount + " and psid = " + psid;
        }
        sql = sql + " order  by ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<SalaryRecord> salaryRecords = null;
        int count =0;
        salaryRecords=jdbcTemplate.query(sql,rowMapper);
        count=this.jdbcTemplate.queryForObject(sqlCount, Integer.class);

        pager.setData(salaryRecords);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public boolean revoke(long id) throws Exception {
        SalaryRecord salaryRecord = salaryRecordDao.load(id);
        if(salaryRecord!=null){
            List<Long> sids = salaryRecord.getSids();
            for(Long sid:sids){
               Salary salary = salaryDao.load(sid);
               if(salary!=null){
                   salary.setStatus(Salary.status_delete);
               }
            }
            salaryRecord.setType(SalaryRecord.type_fail);
            return salaryRecordDao.update(salaryRecord);
        }
        return false;
    }
}
