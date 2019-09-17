package com.store.system.service.impl;

import com.store.system.client.ClientApprovalLog;
import com.store.system.dao.*;
import com.store.system.model.User;
import com.store.system.model.attendance.ApprovalLog;
import com.store.system.model.attendance.ChangeShift;
import com.store.system.model.attendance.Leave;
import com.store.system.model.attendance.WorkOverTime;
import com.store.system.service.ApprovalLogService;
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
    private WorkOverTimeDao workOverTimeDao;
    @Autowired
    private LeaveDao leaveDao;
    @Autowired
    private UserDao userDao;

    public String getName(long id){
        User load = userDao.load(id);
        if(load!=null){
            return load.getName();
        }
        return "";
    }

    @Override
    public List<ClientApprovalLog> getList(long id) throws Exception {

        List<ApprovalLog> list = approvalLogDao.getList(id);
        List<ClientApprovalLog> clientApprovalLogList=new ArrayList<>(list.size());
        for(ApprovalLog approvalLog:list){
            ClientApprovalLog clientApprovalLog=new ClientApprovalLog(approvalLog);

            if(approvalLog.getType()==ApprovalLog.type_leave){
                //请假
                Leave leave = leaveDao.load(approvalLog.getTypeId());
                if(leave!=null){
                    clientApprovalLog.setData(leave.getStartTime());
                    clientApprovalLog.setCheckName(getName(leave.getCheckUid()));
                    clientApprovalLog.setStatus(leave.getStatus());
                }
            }else if(approvalLog.getType()==ApprovalLog.type_card){
                //补卡
            }else if(approvalLog.getType()==ApprovalLog.type_change){
                //调班
                ChangeShift changeShift = changeShiftDao.load(approvalLog.getTypeId());
                if(changeShift!=null){
                    clientApprovalLog.setData(changeShift.getDate());
                    clientApprovalLog.setCheckName(getName(changeShift.getCheckUid()));
                    clientApprovalLog.setStatus(changeShift.getStatus());
                }
            }else{
                //加班
                WorkOverTime workOverTime = workOverTimeDao.load(approvalLog.getTypeId());
                if(workOverTime!=null){
                    clientApprovalLog.setData(workOverTime.getApplyTime());
                    clientApprovalLog.setCheckName(getName(workOverTime.getCheckUid()));
                    clientApprovalLog.setStatus(workOverTime.getStatus());
                }
            }
            clientApprovalLogList.add(clientApprovalLog);
        }


        return clientApprovalLogList;
    }
}
