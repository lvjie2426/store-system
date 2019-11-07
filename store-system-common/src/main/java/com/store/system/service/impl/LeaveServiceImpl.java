package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.store.system.client.ClientLeave;
import com.store.system.dao.ApprovalLogDao;
import com.store.system.dao.LeaveDao;
import com.store.system.dao.UserDao;
import com.store.system.dao.UserLeavePoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.ApprovalLog;
import com.store.system.model.attendance.Leave;
import com.store.system.model.attendance.UserLeavePool;
import com.store.system.service.AttendanceLogService;
import com.store.system.service.LeaveService;
import com.store.system.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 14:53
 **/
@Service
public class LeaveServiceImpl implements LeaveService {

    private TransformFieldSetUtils UserLeave = new TransformFieldSetUtils(UserLeavePool.class);

    @Autowired
    private LeaveDao leaveDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserLeavePoolDao userLeavePoolDao;
    @Autowired
    private AttendanceLogService attendanceLogService;
    @Autowired
    private ApprovalLogDao approvalLogDao;



    private void check(Leave leave) {
        if(leave.getLeaveTime()==0)throw new StoreSystemException("请假时长不能为空！");
        if(leave.getSid()==0)throw new StoreSystemException("企业id不能为空！");
        if(leave.getSubId()==0)throw new StoreSystemException("门店id不能为空！");
        if(leave.getAskUid()==0)throw new StoreSystemException("请假人不能为空！");
        if(leave.getCheckUid()==0)throw new StoreSystemException("请假审批人不能为空！");
        if(leave.getCopyUid()==0)throw new StoreSystemException("请假抄送人不能为空！");
        if(leave.getStartTime()==0)throw new StoreSystemException("请假开始时间不能为空！");
        if(leave.getEndTime()==0)throw new StoreSystemException("请假结束时间不能为空！");
        if(StringUtils.isBlank(leave.getContent()))throw new StoreSystemException("请假内容不能为空！");
    }
    @Override
    public Leave add(Leave leave) throws Exception {
        check(leave);
        Leave insert = leaveDao.insert(leave);
        if(insert!=null&&insert.getCheckUid()>0){
            ApprovalLog approvalLog=new ApprovalLog();
            approvalLog.setCheckUid(insert.getCheckUid());
            approvalLog.setSid(insert.getSid());
            approvalLog.setSubId(insert.getSubId());
            approvalLog.setType(ApprovalLog.type_work);
            approvalLog.setTypeId(insert.getId());
            approvalLogDao.insert(approvalLog);


            UserLeavePool userLeavePool=new UserLeavePool(insert);
            userLeavePoolDao.insert(userLeavePool);
        }
        return insert;
    }

    @Override
    public ClientLeave load(long id) throws Exception {
        return  transformClients(leaveDao.load(id));
    }

    @Override
    public Pager getListByUid(Pager pager,final long uid) throws Exception {

        return new PagerRequestService<Leave>(pager,0) {
            @Override
            public List<Leave> step1GetPageResult(String cursor, int size) throws Exception {
                List<UserLeavePool> allList = userLeavePoolDao.getAllList(uid,Double.parseDouble(cursor),size);
                Set<Long> lid = UserLeave.fieldList(allList, "lid");
                List<Leave> load = leaveDao.load(Lists.newArrayList(lid));
                 return load;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<Leave> step3FilterResult(List<Leave> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<Leave> list, PagerSession pagerSession) throws Exception {

                List<ClientLeave> clientLeaveList=new ArrayList<>(list.size());

                for(Leave leave:list){
                    ClientLeave clientLeave=  transformClients(leave);
                    clientLeaveList.add(clientLeave);
                }
                return clientLeaveList;
            }
        }.getPager();

    }

    @Override
    public boolean pass(long id) throws Exception {
        Leave load = leaveDao.load(id);
        if (load != null) {
            load.setStatus(Leave.status_success);
            leaveDao.update(load);
            UserLeavePool userLeavePool = new UserLeavePool();
            userLeavePool.setLid(load.getId());
            userLeavePool.setStatus(load.getStatus());
            userLeavePool.setUid(load.getAskUid());
            userLeavePoolDao.update(userLeavePool);
            if (load.getStatus() == Leave.status_success) {
                long startDay = TimeUtils.getDayFormTime(load.getStartTime());
                long endDay = TimeUtils.getDayFormTime(load.getEndTime());
                for (long day = startDay; day <= endDay; day++) {
                    attendanceLogService.updateLeave(load.getAskUid(), day, load.getLeaveType(), load.getStartTime(), load.getEndTime());
                }
            }
        }
        return leaveDao.update(load);
    }

    @Override
    public boolean nopass(long id, String reason) throws Exception {
        Leave load = leaveDao.load(id);
        if(load!=null){
            load.setStatus(Leave.status_fail);
            load.setReason(reason);
        }
        return leaveDao.update(load);
    }

    public ClientLeave transformClients(Leave leave){
        ClientLeave clientLeave=new ClientLeave(leave);
        User load = userDao.load(leave.getCheckUid());
        if(load!=null){
            clientLeave.setCheckName(load.getName());
        }
        return clientLeave;
    }
}
