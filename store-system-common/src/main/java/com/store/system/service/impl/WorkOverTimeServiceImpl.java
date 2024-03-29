package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientWorkOverTime;
import com.store.system.dao.ApprovalLogDao;
import com.store.system.dao.UserDao;
import com.store.system.dao.WorkOverTimeDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.ApprovalLog;
import com.store.system.model.attendance.Leave;
import com.store.system.model.attendance.WorkOverTime;
import com.store.system.service.WorkOverTimeService;
import com.store.system.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 10:50
 **/
@Service
public class WorkOverTimeServiceImpl implements WorkOverTimeService {

    @Autowired
    private WorkOverTimeDao workOverTimeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ApprovalLogDao approvalLogDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<WorkOverTime> rowMapper = new RowMapperHelp<>(WorkOverTime.class);

    private void check(WorkOverTime workOverTime) {
        if(workOverTime.getStartTime()==0)throw  new StoreSystemException("加班开始时间不能为空！");
        if(workOverTime.getEndTime()==0)throw  new StoreSystemException("加班结束时间不能为空！");
        if(workOverTime.getApplyTime()==0)throw  new StoreSystemException("加班申请时间不能为空！");
        if(workOverTime.getWorkTime()==0)throw  new StoreSystemException("加班时长不能为空！");
        if(workOverTime.getSid()==0)throw  new StoreSystemException("企业id不能为空！");
        if(workOverTime.getSubId()==0)throw  new StoreSystemException("门店id不能为空！");
        if(workOverTime.getAskUid()==0)throw  new StoreSystemException("申请人不能为空！");
        if(workOverTime.getCheckUid()==0)throw  new StoreSystemException("审核人不能为空！");
        if(workOverTime.getCopyUid()==0)throw  new StoreSystemException("抄送人不能为空！");
        if(StringUtils.isBlank(workOverTime.getContent()))throw  new StoreSystemException("加班原因内容不能为空！");
    }

    @Override
    public WorkOverTime add(WorkOverTime workOverTime) throws Exception {
        check(workOverTime);
        workOverTime.setDay(TimeUtils.getDayFormTime(workOverTime.getApplyTime()));
        workOverTime.setMonth(TimeUtils.getMonthFormTime(workOverTime.getApplyTime()));
        workOverTime.setWeek(TimeUtils.getWeekFormTime(workOverTime.getApplyTime()));
        workOverTime.setYear(TimeUtils.getYearFormTime(workOverTime.getApplyTime()));
        WorkOverTime insert = workOverTimeDao.insert(workOverTime);
        if(insert!=null&&insert.getCheckUid()>0){
            ApprovalLog approvalLog=new ApprovalLog();
            approvalLog.setCheckUid(insert.getCheckUid());
            approvalLog.setSid(insert.getSid());
            approvalLog.setSubId(insert.getSubId());
            approvalLog.setType(ApprovalLog.type_work);
            approvalLog.setTypeId(insert.getId());
            approvalLogDao.insert(approvalLog);
        }

        return insert;
    }

    @Override
    public ClientWorkOverTime load(long id) throws Exception {
        return  transformClients(workOverTimeDao.load(id));
    }

    @Override
    public Pager getListByUid(final long askUid,Pager pager) throws Exception {
        return new PagerRequestService<WorkOverTime>(pager,0) {
            @Override
            public List<WorkOverTime> step1GetPageResult(String cursor, int size) throws Exception {
                List<WorkOverTime> listByUid = workOverTimeDao.getListByUid(askUid,Double.parseDouble(cursor),size);
                return listByUid;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<WorkOverTime> step3FilterResult(List<WorkOverTime> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<WorkOverTime> list, PagerSession pagerSession) throws Exception {
                List<ClientWorkOverTime> clientWorkOverTimeList=new ArrayList<>(list.size());
                for(WorkOverTime workOverTime:list){
                    ClientWorkOverTime clientWorkOverTime=transformClients(workOverTime);
                    clientWorkOverTimeList.add(clientWorkOverTime);
                }
                return clientWorkOverTimeList;
            }
        }.getPager();




    }

    @Override
    public boolean pass(long id) throws Exception {
        WorkOverTime load = workOverTimeDao.load(id);
        if(load!=null){
            load.setStatus(WorkOverTime.status_success);
        }
        return workOverTimeDao.update(load);
    }

    @Override
    public boolean nopass(long id, String reason) throws Exception {

        WorkOverTime load = workOverTimeDao.load(id);
        if(load!=null){
            load.setStatus(WorkOverTime.status_fail);
            load.setReason(reason);
        }
        return workOverTimeDao.update(load);
    }

    @Override
    public Pager getList(long subid,long sid, String userName, long startTime, long endTime, Pager pager) throws Exception {
        String sql = "select wo.* from work_overtime wo, user u where u.id=wo.askUid ";
        String countSql = "select count(1) from work_overtime wo, user u where u.id=wo.askUid ";
        String limit = "  limit %d , %d ";

        if(sid>0&&subid>0){
            //门店
            sql=sql+" and subid="+subid;
            countSql=countSql+" and subid="+subid;
        }
        if(sid>0&&subid==0){
            // 企业
            sql=sql+" and sid="+sid+" and subid=0";
            countSql=countSql+" and sid="+sid+" and subid=0";
        }

        if (StringUtils.isNotBlank(userName)) {
            sql = sql + " and u.`name` like '%"+userName+"%'";
            countSql = countSql + " and u.`name` like '%"+userName+"%'";
        }
        if (startTime > 0) {
            sql = sql + " and ctime>=" + startTime;
            countSql = countSql + " and ctime>=" + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and ctime<=" + endTime;
            countSql = countSql + " and ctime<=" + endTime;
        }


        sql = sql + " order  by ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = 0;
        List<WorkOverTime> workOverTimes = this.jdbcTemplate.query(sql, rowMapper);
        count = this.jdbcTemplate.queryForObject(countSql, Integer.class);

        List<ClientWorkOverTime> clientWorkOverTimeList=new ArrayList<>(workOverTimes.size());
        for(WorkOverTime workOverTime:workOverTimes){
            ClientWorkOverTime clientWorkOverTime=transformClients(workOverTime);
            clientWorkOverTimeList.add(clientWorkOverTime);
        }

        pager.setData(clientWorkOverTimeList);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public boolean passMore(List<Long> ids) throws Exception {
        for(Long id:ids){
            pass(id);
        }
        return true;
    }

    public ClientWorkOverTime transformClients(WorkOverTime workOverTime){
        ClientWorkOverTime clientWorkOverTime=new ClientWorkOverTime(workOverTime);
        User load = userDao.load(workOverTime.getCheckUid());
        if(load!=null){
            clientWorkOverTime.setCheckName(load.getName());
            clientWorkOverTime.setCheckCover(load.getCover());
        }
        User load1 = userDao.load(workOverTime.getCopyUid());
        if(load1!=null){
            clientWorkOverTime.setCopyName(load1.getName());
            clientWorkOverTime.setCopyCover(load1.getCover());
        }User load2 = userDao.load(workOverTime.getAskUid());
        if(load1!=null){
            clientWorkOverTime.setAskName(load2.getName());
            clientWorkOverTime.setAskCover(load2.getCover());
        }
        return clientWorkOverTime;
    }
}
