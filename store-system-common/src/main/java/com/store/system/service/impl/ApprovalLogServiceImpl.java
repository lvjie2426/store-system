package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.store.system.client.ClientApprovalLog;
import com.store.system.client.ClientFillCard;
import com.store.system.dao.*;
import com.store.system.model.User;
import com.store.system.model.attendance.*;
import com.store.system.service.ApprovalLogService;
import com.store.system.service.FillCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-17 14:58
 **/
@Service
public class ApprovalLogServiceImpl implements ApprovalLogService {

    @Autowired
    private ApprovalLogDao approvalLogDao;
    @Autowired
    private ChangeShiftDao changeShiftDao;
    @Autowired
    private FillCardService fillCardService;
    @Autowired
    private WorkOverTimeDao workOverTimeDao;
    @Autowired
    private LeaveDao leaveDao;
    @Autowired
    private UserDao userDao;

    public String getName(long id) {
        User load = userDao.load(id);
        if (load != null) {
            return load.getName();
        }
        return "";
    }

    @Override
    public Pager getList(final long id, Pager pager) throws Exception {
        return new PagerRequestService<ApprovalLog>(pager, 0) {
            @Override
            public List<ApprovalLog> step1GetPageResult(String cursor, int size) throws Exception {
                List<ApprovalLog> list = approvalLogDao.getList(id, Double.parseDouble(cursor), size);
                return list;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<ApprovalLog> step3FilterResult(List<ApprovalLog> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<ApprovalLog> list, PagerSession pagerSession) throws Exception {

                List<ClientApprovalLog> clientApprovalLogList = new ArrayList<>(list.size());
                for (ApprovalLog approvalLog : list) {
                    ClientApprovalLog clientApprovalLog = new ClientApprovalLog(approvalLog);

                    if (approvalLog.getType() == ApprovalLog.type_leave) {
                        //请假
                        Leave leave = leaveDao.load(approvalLog.getTypeId());
                        if (leave != null) {
                            clientApprovalLog.setData(leave.getStartTime());
                            clientApprovalLog.setCheckName(getName(leave.getCheckUid()));
                            clientApprovalLog.setStatus(leave.getStatus());
                        }
                    } else if (approvalLog.getType() == ApprovalLog.type_card) {
                        //补卡
                        ClientFillCard load = fillCardService.load(approvalLog.getTypeId());
                        if(load!=null){
                            clientApprovalLog.setData(load.getCtime());
                            clientApprovalLog.setCheckName(load.getCheckName());
                            clientApprovalLog.setStatus(load.getStatus());
                        }

                    } else if (approvalLog.getType() == ApprovalLog.type_change) {
                        //调班
                        ChangeShift changeShift = changeShiftDao.load(approvalLog.getTypeId());
                        if (changeShift != null) {
                            clientApprovalLog.setData(changeShift.getDate());
                            clientApprovalLog.setCheckName(getName(changeShift.getCheckUid()));
                            clientApprovalLog.setStatus(changeShift.getStatus());
                            if(changeShift.getReplaceStatus()==ChangeShift.replace_no){
                                clientApprovalLog.setStatus(ChangeShift.status_fail);
                            }
                            clientApprovalLog.setReplaceStatus(changeShift.getReplaceStatus());

                        }
                    } else {
                        //加班
                        WorkOverTime workOverTime = workOverTimeDao.load(approvalLog.getTypeId());
                        if (workOverTime != null) {
                            clientApprovalLog.setData(workOverTime.getApplyTime());
                            clientApprovalLog.setCheckName(getName(workOverTime.getCheckUid()));
                            clientApprovalLog.setStatus(workOverTime.getStatus());
                        }
                    }
                    clientApprovalLogList.add(clientApprovalLog);
                }
                return clientApprovalLogList;
            }
        }.

                getPager();


    }
}
