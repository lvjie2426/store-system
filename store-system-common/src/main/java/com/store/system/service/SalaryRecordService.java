package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.SalaryRecord;
import com.sun.jimi.core.util.P;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.service
 * @ClassName: SalaryRecordService
 * @Author: LiHaoJie
 * @Description: 工资单
 * @Date: 2019/5/27 15:49
 * @Version: 1.0
 */
public interface SalaryRecordService {

    public SalaryRecord add(SalaryRecord salaryRecord)throws Exception;

    public Pager getAllByPager(Pager pager,long psid)throws Exception;

    public boolean revoke(long id)throws Exception;
}
