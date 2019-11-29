package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientFillCard;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.*;
import com.store.system.service.AttendanceLogService;
import com.store.system.service.FillCardService;
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
 * @create: 2019-09-18 16:15
 **/
@Service
public class FillCardServiceImpl implements FillCardService {

    @Autowired
    private FillCardDao fillCardDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ApprovalLogDao approvalLogDao;
    @Resource
    private AttendanceLogService attendanceLogService;
    @Resource
    private PunchCardDao punchCardDao;
    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<FillCard> rowMapper = new RowMapperHelp<>(FillCard.class);

    private void check(FillCard fillCard) {
        if (fillCard.getAskUid() == 0) throw new StoreSystemException("申请人不能为空！");
        if (fillCard.getType() == 0) throw new StoreSystemException("补卡类型不能为空！");
        if (fillCard.getTime() == 0) throw new StoreSystemException("补卡时间不能为空！");
        if (fillCard.getSid() == 0) throw new StoreSystemException("企业id不能为空！");
        if (fillCard.getSubId() == 0) throw new StoreSystemException("门店id不能为空！");
        if (fillCard.getCopyUid() == 0) throw new StoreSystemException("抄送人不能为空！");
        if (StringUtils.isBlank(fillCard.getReason())) throw new StoreSystemException("申请理由不能为空！");
    }

    @Override
    public boolean nopass(long id) throws Exception {
        FillCard load = fillCardDao.load(id);
        if (load != null) {
            load.setStatus(FillCard.status_fail);
        }
        return fillCardDao.update(load);
    }

    @Override
    public boolean pass(long id) throws Exception {
        boolean res = false;
        FillCard load = fillCardDao.load(id);
        if (load != null) {
            load.setStatus(FillCard.status_success);
            res = fillCardDao.update(load);
            attendanceLogService.fixAttendanceLog(load.getCheckUid(), load.getAskUid(), TimeUtils.getDayFormTime(load.getTime()),
                    load.getType(), load.getReason());
        }
        return res;
    }

    @Override
    public ClientFillCard load(long id) throws Exception {
        FillCard load = fillCardDao.load(id);
        return transFormClient(load);
    }

    private ClientFillCard transFormClient(FillCard load) {
        ClientFillCard clientFillCard = new ClientFillCard(load);

        if (load != null) {
            User user = userDao.load(load.getCheckUid());
            if (user != null) {
                clientFillCard.setCheckName(user.getName());
                clientFillCard.setCheckCover(user.getCover());
            }
            User load1 = userDao.load(load.getAskUid());
            if (load1 != null) {
                clientFillCard.setAskName(load1.getName());
                clientFillCard.setAskCover(load1.getCover());
            }
        }
        // 拿到当天打卡记录表 信息取第一次打卡和最后一次打卡
        List<PunchCardLog> punchCardLogs = punchCardDao.getAllList(load.getAskUid(),TimeUtils.getDayFormTime(load.getTime()));
        if(punchCardLogs.size()>0){
            clientFillCard.setComeTime(punchCardLogs.get(0).getPunchCardTime());
            clientFillCard.setLeaveTime(punchCardLogs.get(punchCardLogs.size()-1).getPunchCardTime());
        }

        return clientFillCard;
    }

    @Override
    public FillCard add(FillCard fillCard) throws Exception {
        check(fillCard);
        FillCard insert = fillCardDao.insert(fillCard);
        if (insert != null && insert.getCheckUid() > 0) {
            ApprovalLog approvalLog = new ApprovalLog();
            approvalLog.setCheckUid(insert.getCheckUid());
            approvalLog.setSid(insert.getSid());
            approvalLog.setSubId(insert.getSubId());
            approvalLog.setType(ApprovalLog.type_card);
            approvalLog.setTypeId(insert.getId());
            approvalLogDao.insert(approvalLog);
        }
        return insert;
    }


    @Override
    public Pager getListByUid(final long id, Pager pager) throws Exception {
        return new PagerRequestService<FillCard>(pager, 0) {
            @Override
            public List<FillCard> step1GetPageResult(String cursor, int size) throws Exception {
                List<FillCard> list = fillCardDao.getPager(id, Double.parseDouble(cursor), size);
                return list;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<FillCard> step3FilterResult(List<FillCard> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<FillCard> list, PagerSession pagerSession) throws Exception {
                return list;
            }
        }.getPager();
    }

    @Override
    public Boolean update(FillCard fillCard) throws Exception {
        check(fillCard);
        FillCard load = fillCardDao.load(fillCard.getId());
        if (load != null) {
            load.setStatus(fillCard.getStatus());
            load.setType(fillCard.getType());
            load.setTime(fillCard.getTime());
            load.setImgs(fillCard.getImgs());
            load.setReason(fillCard.getReason());
            if (load.getStatus() == FillCard.status_success) {
                attendanceLogService.fixAttendanceLog(load.getCheckUid(), load.getAskUid(), TimeUtils.getDayFormTime(load.getTime()),
                        load.getType(), load.getReason());
            }
        }
        return fillCardDao.update(load);
    }

    @Override
    public Pager getAllList(Pager pager, long subid, long sid, long startTime, long endTime, int status, String userName) throws Exception {
        String sql = "select fill.* from fill_card fill, user u where u.id=fill.askUid ";
        String countSql = "select count(1) from fill_card fill, user u where u.id=fill.askUid ";
        String limit = "  limit %d , %d ";

        if (sid > 0 && subid > 0) {
            //门店
            sql = sql + " and fill.subid=" + subid;
            countSql = countSql + " and fill.subid=" + subid;
        }
        if (sid > 0 && subid == 0) {
            // 企业
            sql = sql + " and fill.sid=" + sid + " and fill.subid=0";
            countSql = countSql + " and fill.sid=" + sid + " and fill.subid=0";
        }

        if (StringUtils.isNotBlank(userName)) {
            sql = sql + " and u.`name` like '%" + userName + "%'";
            countSql = countSql + " and u.`name` like '%" + userName + "%'";
        }
        if (startTime > 0) {
            sql = sql + " and fill.ctime>=" + startTime;
            countSql = countSql + " and fill.ctime>=" + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and fill.ctime<=" + endTime;
            countSql = countSql + " and fill.ctime<=" + endTime;
        }
        if (status > 0) {
            sql = sql + " and fill.status =" + status;
            countSql = countSql + " and fill.status =" + status;
        }
        sql = sql + " order  by ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = 0;
        List<FillCard> fillCards = this.jdbcTemplate.query(sql, rowMapper);
        count = this.jdbcTemplate.queryForObject(countSql, Integer.class);
        pager.setData(transFormClients(fillCards));
        pager.setTotalCount(count);
        return pager;
    }

    private List<ClientFillCard> transFormClients(List<FillCard> fillCards) {
        List<ClientFillCard> list = new ArrayList<>(fillCards.size());
        for (FillCard fillCard : fillCards) {
            list.add(transFormClient(fillCard));
        }
        return list;
    }
}
