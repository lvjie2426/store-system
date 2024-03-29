package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientChangeShift;
import com.store.system.dao.ApprovalLogDao;
import com.store.system.dao.ChangeShiftDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.ApprovalLog;
import com.store.system.model.attendance.ChangeShift;
import com.store.system.model.attendance.WorkOverTime;
import com.store.system.service.ChangeShiftService;
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
 * @create: 2019-09-16 13:58
 **/

@Service
public class ChangeShiftServiceImpl implements ChangeShiftService {

    @Autowired
    private ChangeShiftDao changeShiftDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ApprovalLogDao approvalLogDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<ChangeShift> rowMapper = new RowMapperHelp<>(ChangeShift.class);

    private void check(ChangeShift changeShift) {
        if (changeShift.getDate() == 0) throw new StoreSystemException("调班日期不能为空！");
        if (changeShift.getFlightStart() == 0) throw new StoreSystemException("班次开始时间不能为空");
        if (changeShift.getFlightEnd() == 0) throw new StoreSystemException("班次结束时间不能为空");
        if (changeShift.getChangeStart() == 0) throw new StoreSystemException("调班开始时间不能为空！");
        if (changeShift.getChangeEnd() == 0) throw new StoreSystemException("调班结束时间不能为空！");
        if (changeShift.getReplaceUid() == 0) throw new StoreSystemException("被调班人不能为空！");
        if (changeShift.getAskUid() == 0) throw new StoreSystemException("申请人不能为空！");
        if (changeShift.getCheckUid() == 0) throw new StoreSystemException("审核人不能为空！");
        if (changeShift.getCopyUid() == 0) throw new StoreSystemException("抄送人不能为空！");
        if (changeShift.getSid() == 0) throw new StoreSystemException("企业不能为空！");
        if (changeShift.getSubId() == 0) throw new StoreSystemException("门店不能为空！");
    }


    @Override
    public ChangeShift add(ChangeShift changeShift) throws Exception {
        check(changeShift);
        ChangeShift insert = changeShiftDao.insert(changeShift);
        if (insert != null && insert.getCheckUid() > 0) {
            ApprovalLog approvalLog = new ApprovalLog();
            approvalLog.setCheckUid(insert.getCheckUid());
            approvalLog.setSid(insert.getSid());
            approvalLog.setSubId(insert.getSubId());
            approvalLog.setType(ApprovalLog.type_change);
            approvalLog.setTypeId(insert.getId());
            approvalLogDao.insert(approvalLog);
        }
        return insert;
    }

    @Override
    public ClientChangeShift load(long id) throws Exception {
        return TransFormCliens(changeShiftDao.load(id));
    }

    @Override
    public Pager getListByUid(final long uid, Pager pager) throws Exception {
        return new PagerRequestService<ChangeShift>(pager, 0) {
            @Override
            public List<ChangeShift> step1GetPageResult(String s, int i) throws Exception {
                List<ChangeShift> listByUid = changeShiftDao.getListByUid(uid, Double.parseDouble(s), i);
                return listByUid;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<ChangeShift> step3FilterResult(List<ChangeShift> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<ChangeShift> list, PagerSession pagerSession) throws Exception {
                List<ClientChangeShift> clientChangeShifts = new ArrayList<>(list.size());
                for (ChangeShift changeShift : list) {
                    ClientChangeShift clientChangeShift = TransFormCliens(changeShift);
                    clientChangeShifts.add(clientChangeShift);
                }
                return clientChangeShifts;
            }
        }.getPager();

    }

    @Override
    public List<ClientChangeShift> getListByReplaceUid(long replaceUid) throws Exception {
        List<ChangeShift> listByUid = changeShiftDao.getListByReplaceUid(replaceUid);
        List<ClientChangeShift> clientChangeShifts = new ArrayList<>(listByUid.size());
        for (ChangeShift changeShift : listByUid) {
            ClientChangeShift clientChangeShift = TransFormCliens(changeShift);
            clientChangeShifts.add(clientChangeShift);
        }
        return clientChangeShifts;
    }

    @Override
    public boolean nopass(long id, String reason) throws Exception {
        ChangeShift load = changeShiftDao.load(id);
        if (load != null) {
            load.setStatus(ChangeShift.status_fail);
        }
        return changeShiftDao.update(load);
    }

    @Override
    public boolean pass(long id) throws Exception {
        ChangeShift load = changeShiftDao.load(id);
        if (load != null) {
            load.setStatus(ChangeShift.status_success);
        }
        return changeShiftDao.update(load);
    }

    @Override
    public boolean replaceNopass(long id) throws Exception {
        ChangeShift load = changeShiftDao.load(id);
        if (load != null) {
            load.setReplaceStatus(ChangeShift.replace_no);
        }
        return changeShiftDao.update(load);
    }

    @Override
    public boolean replacePass(long id) throws Exception {
        ChangeShift load = changeShiftDao.load(id);
        if (load != null) {
            load.setReplaceStatus(ChangeShift.replace_yes);
        }

        return changeShiftDao.update(load);
    }

    @Override
    public Pager getList(long subid, long sid, String userName, long startTime, long endTime, int status, Pager pager) throws Exception {
        String sql = "select wo.* from work_overtime wo, user u where u.id=wo.askUid ";
        String countSql = "select count(1) from work_overtime wo, user u where u.id=wo.askUid ";
        String limit = "  limit %d , %d ";
        if (status>-1) {
            sql = sql + " and status=" + status;
            countSql = countSql + " and status=" + status;
        }
        if (sid > 0 && subid > 0) {
            //门店
            sql = sql + " and subid=" + subid;
            countSql = countSql + " and subid=" + subid;
        }
        if (sid > 0 && subid == 0) {
            // 企业
            sql = sql + " and sid=" + sid + " and subid=0";
            countSql = countSql + " and sid=" + sid + " and subid=0";
        }

        if (StringUtils.isNotBlank(userName)) {
            sql = sql + " and u.`name` like '%" + userName + "%'";
            countSql = countSql + " and u.`name` like '%" + userName + "%'";
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
        List<ChangeShift> changeShifts = this.jdbcTemplate.query(sql, rowMapper);
        count = this.jdbcTemplate.queryForObject(countSql, Integer.class);

        List<ClientChangeShift> clientChangeShifts = new ArrayList<>(changeShifts.size());
        for (ChangeShift changeShift : changeShifts) {
            ClientChangeShift clientChangeShift = TransFormCliens(changeShift);
            clientChangeShifts.add(clientChangeShift);
        }

        pager.setData(clientChangeShifts);
        pager.setTotalCount(count);
        return pager;

    }

    public ClientChangeShift TransFormCliens(ChangeShift changeShift) {
        ClientChangeShift clientChangeShift = new ClientChangeShift(changeShift);
        User load = userDao.load(changeShift.getReplaceUid());
        if (load != null) {
            clientChangeShift.setReplaceName(load.getName());
            clientChangeShift.setReplaceCover(load.getCover());
        }
        User load1 = userDao.load(changeShift.getCopyUid());
        if (load1 != null) {
            clientChangeShift.setCopyName(load1.getName());
            clientChangeShift.setCopyCover(load1.getCover());
        }
        User load2 = userDao.load(changeShift.getCheckUid());
        if (load2 != null) {
            clientChangeShift.setCheckName(load2.getName());
            clientChangeShift.setCheckCover(load2.getCover());
        }
        User load3 = userDao.load(changeShift.getAskUid());
        if (load3 != null) {
            clientChangeShift.setAskName(load3.getName());
            clientChangeShift.setAskCover(load3.getCover());
        }
        return clientChangeShift;
    }

}
