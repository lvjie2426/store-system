package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.store.system.client.ClientChangeShift;
import com.store.system.dao.ApprovalLogDao;
import com.store.system.dao.ChangeShiftDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.ApprovalLog;
import com.store.system.model.attendance.ChangeShift;
import com.store.system.service.ChangeShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserDao  userDao;
    @Autowired
    private ApprovalLogDao approvalLogDao;

    private void check(ChangeShift changeShift) {
        if(changeShift.getDate()==0)throw new StoreSystemException("调班日期不能为空！");
        if(changeShift.getFlightStart()==0)throw new StoreSystemException("班次开始时间不能为空");
        if(changeShift.getFlightEnd()==0)throw new StoreSystemException("班次结束时间不能为空");
        if(changeShift.getChangeStart()==0)throw new StoreSystemException("调班开始时间不能为空！");
        if(changeShift.getChangeEnd()==0)throw new StoreSystemException("调班结束时间不能为空！");
        if(changeShift.getReplaceUid()==0)throw new StoreSystemException("被调班人不能为空！");
        if(changeShift.getAskUid()==0)throw new StoreSystemException("申请人不能为空！");
        if(changeShift.getCheckUid()==0)throw new StoreSystemException("审核人不能为空！");
        if(changeShift.getCopyUid()==0)throw new StoreSystemException("抄送人不能为空！");
        if(changeShift.getSid()==0)throw new StoreSystemException("企业不能为空！");
        if(changeShift.getSubId()==0)throw new StoreSystemException("门店不能为空！");
    }


    @Override
    public ChangeShift add(ChangeShift changeShift) throws Exception {
        check(changeShift);
        ChangeShift insert = changeShiftDao.insert(changeShift);
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
    public ClientChangeShift load(long id) throws Exception {
        return TransFormCliens(changeShiftDao.load(id));
    }

    @Override
    public Pager getListByUid(final long uid, Pager pager) throws Exception {
        return new PagerRequestService<ChangeShift>(pager, 0) {
            @Override
            public List<ChangeShift> step1GetPageResult(String s, int i) throws Exception {
                List<ChangeShift> listByUid = changeShiftDao.getListByUid(uid,Double.parseDouble(s),i);
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
                List<ClientChangeShift> clientChangeShifts=new ArrayList<>(list.size());
                for(ChangeShift changeShift:list){
                    ClientChangeShift clientChangeShift=TransFormCliens(changeShift);
                    clientChangeShifts.add(clientChangeShift);
                }
                return clientChangeShifts;
            }
        }.getPager();

    }

    @Override
    public List<ClientChangeShift> getListByReplaceUid(long replaceUid) throws Exception {
        List<ChangeShift> listByUid = changeShiftDao.getListByReplaceUid(replaceUid);
        List<ClientChangeShift> clientChangeShifts=new ArrayList<>(listByUid.size());
        for(ChangeShift changeShift:listByUid){
            ClientChangeShift clientChangeShift=TransFormCliens(changeShift);
            clientChangeShifts.add(clientChangeShift);
        }
        return clientChangeShifts;
    }

    @Override
    public boolean nopass(long id, String reason) throws Exception {
        ChangeShift load = changeShiftDao.load(id);
        if(load!=null){
            load.setStatus(ChangeShift.status_fail);
        }
        return changeShiftDao.update(load);
    }

    @Override
    public boolean pass(long id) throws Exception {
        ChangeShift load = changeShiftDao.load(id);
        if(load!=null){
            load.setStatus(ChangeShift.status_success);
        }
        return changeShiftDao.update(load);
    }

    public ClientChangeShift TransFormCliens(ChangeShift changeShift){
        ClientChangeShift clientChangeShift=new ClientChangeShift(changeShift);
        User load = userDao.load(changeShift.getReplaceUid());
        if(load!=null){
            clientChangeShift.setReplaceName(load.getName());
        }
        User load1 = userDao.load(changeShift.getCopyUid());
        if(load1!=null){
            clientChangeShift.setCopyName(load1.getName());
        }
        return clientChangeShift;
    }

}
