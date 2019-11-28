package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientLeave;
import com.store.system.dao.ApprovalLogDao;
import com.store.system.dao.LeaveDao;
import com.store.system.dao.UserDao;
import com.store.system.dao.UserLeavePoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Order;
import com.store.system.model.User;
import com.store.system.model.attendance.ApprovalLog;
import com.store.system.model.attendance.Leave;
import com.store.system.model.attendance.UserLeavePool;
import com.store.system.service.AttendanceLogService;
import com.store.system.service.LeaveService;
import com.store.system.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<Leave> rowMapper = new RowMapperHelp<>(Leave.class);

    private void check(Leave leave) {
        if (leave.getLeaveTime() == 0) throw new StoreSystemException("请假时长不能为空！");
        if (leave.getSid() == 0) throw new StoreSystemException("企业id不能为空！");
        if (leave.getSubId() == 0) throw new StoreSystemException("门店id不能为空！");
        if (leave.getAskUid() == 0) throw new StoreSystemException("请假人不能为空！");
        if (leave.getCheckUid() == 0) throw new StoreSystemException("请假审批人不能为空！");
        if (leave.getCopyUid() == 0) throw new StoreSystemException("请假抄送人不能为空！");
        if (leave.getStartTime() == 0) throw new StoreSystemException("请假开始时间不能为空！");
        if (leave.getEndTime() == 0) throw new StoreSystemException("请假结束时间不能为空！");
        if (StringUtils.isBlank(leave.getContent())) throw new StoreSystemException("请假内容不能为空！");
    }

    @Override
    public Leave add(Leave leave) throws Exception {
        check(leave);
        Leave insert = leaveDao.insert(leave);
        if (insert != null && insert.getCheckUid() > 0) {
            ApprovalLog approvalLog = new ApprovalLog();
            approvalLog.setCheckUid(insert.getCheckUid());
            approvalLog.setSid(insert.getSid());
            approvalLog.setSubId(insert.getSubId());
            approvalLog.setType(ApprovalLog.type_leave);
            approvalLog.setTypeId(insert.getId());
            approvalLogDao.insert(approvalLog);


            UserLeavePool userLeavePool = new UserLeavePool(insert);
            userLeavePoolDao.insert(userLeavePool);
        }
        return insert;
    }

    @Override
    public ClientLeave load(long id) throws Exception {
        return transformClients(leaveDao.load(id));
    }

    @Override
    public Pager getListByUid(Pager pager, final long uid) throws Exception {

        return new PagerRequestService<Leave>(pager, 0) {
            @Override
            public List<Leave> step1GetPageResult(String cursor, int size) throws Exception {
                List<UserLeavePool> allList = userLeavePoolDao.getAllList(uid, Double.parseDouble(cursor), size);
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

                List<ClientLeave> clientLeaveList = new ArrayList<>(list.size());

                for (Leave leave : list) {
                    ClientLeave clientLeave = transformClients(leave);
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
        if (load != null) {
            load.setStatus(Leave.status_fail);
            load.setReason(reason);
        }
        return leaveDao.update(load);
    }

    @Override
    public Pager getList(long subid, long sid, String userName, Pager pager, int type, long endTime, long startTime) throws Exception {
        String sql = "select le.* from leave le,user u where 1=1 and u.id=le.askUid";
        String countSql = "select count(*) from leave where 1=1 and u.id=le.askUid";
        String limit = "  limit %d , %d ";

        if (sid > 0 && subid > 0) {
            //门店
            sql = sql + " and le.subid=" + subid;
            countSql = countSql + " and le.subid=" + subid;
        }
        if (sid > 0 && subid == 0) {
            // 企业
            sql = sql + " and le.sid=" + sid + " and le.subid=0";
            countSql = countSql + " and le.sid=" + sid + " and le.subid=0";
        }

        if (StringUtils.isNotBlank(userName)) {
            sql = sql + " and u.`name` like '%" + userName + "%'";
            countSql = countSql + " and u.`name` like '%" + userName + "%'";
        }
        if (type > -1) {
            sql = sql + " and TYPE=" + type;
            countSql = countSql + " and TYPE=" + type;
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
        List<Leave> leaveList = this.jdbcTemplate.query(sql, rowMapper);
        count = this.jdbcTemplate.queryForObject(countSql, Integer.class);
        pager.setData(transformClients(leaveList));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Boolean update(Leave leave, User user) throws Exception {
        check(leave);
        Leave load = leaveDao.load(leave.getId());
        if (load != null) {
            load.setLeaveType(leave.getLeaveType());
            load.setLeaveTime(leave.getLeaveTime());
            load.setStatus(leave.getStatus());
            load.setSid(leave.getSid());
            load.setSubId(leave.getSubId());
            load.setAskUid(leave.getAskUid());
            load.setCheckUid(user.getId());
            load.setCopyUid(leave.getCopyUid());
            load.setStartTime(leave.getStartTime());
            load.setEndTime(leave.getEndTime());
            load.setImgs(leave.getImgs());
            load.setContent(leave.getContent());
        }
        if (leave.getStatus() == Leave.status_success) {
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
    public boolean passMore(List<Long> ids) throws Exception {
        for(Long  id:ids) {

            pass(id);

        }
        return true;
    }

    public ClientLeave transformClients(Leave leave) {
        ClientLeave clientLeave = new ClientLeave(leave);
        User load = userDao.load(leave.getCheckUid());
        User ask = userDao.load(leave.getAskUid());
        User copy = userDao.load(leave.getCopyUid());
        clientLeave.setCheckName(load != null ? load.getName() : "");
        clientLeave.setCheckCover(load != null ? load.getCover() : "");
        clientLeave.setAskName(ask != null ? ask.getName() : "");
        clientLeave.setAskCover(ask != null ? ask.getCover() : "");
        clientLeave.setCopyName(copy != null ? copy.getName() : "");
        clientLeave.setCopyCover(copy != null ? copy.getCover() : "");
        return clientLeave;
    }

    public List<ClientLeave> transformClients(List<Leave> leave) {
        List<ClientLeave> list = new ArrayList<>(leave.size());
        for (Leave le : leave) {
            ClientLeave clientLeave = transformClients(le);
            list.add(clientLeave);
        }
        return list;
    }
}
