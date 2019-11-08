package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.store.system.client.ClientFillCard;
import com.store.system.dao.ApprovalLogDao;
import com.store.system.dao.FillCardDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.ApprovalLog;
import com.store.system.model.attendance.FillCard;
import com.store.system.service.FillCardService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private void check(FillCard fillCard) {
        if(fillCard.getAskUid()==0)throw  new StoreSystemException("申请人不能为空！");
        if(fillCard.getType()==0)throw  new StoreSystemException("补卡类型不能为空！");
        if(fillCard.getTime()==0)throw  new StoreSystemException("补卡时间不能为空！");
        if(fillCard.getSid()==0)throw  new StoreSystemException("企业id不能为空！");
        if(fillCard.getSubId()==0)throw  new StoreSystemException("门店id不能为空！");
        if(fillCard.getCopyUid()==0)throw  new StoreSystemException("抄送人不能为空！");
        if(StringUtils.isBlank(fillCard.getReason()))throw  new StoreSystemException("申请理由不能为空！");
    }

    @Override
    public boolean nopass(long id) throws Exception {
        FillCard load = fillCardDao.load(id);
        if(load!=null){
            load.setStatus(FillCard.status_fail);
        }
        return fillCardDao.update(load);
    }

    @Override
    public boolean pass(long id) throws Exception {
        FillCard load = fillCardDao.load(id);
        if(load!=null){
            load.setStatus(FillCard.status_success);
        }
        return fillCardDao.update(load);
    }

    @Override
    public ClientFillCard load(long id) throws Exception {
        FillCard load = fillCardDao.load(id);
        return  transFormClient(load);
    }

    private ClientFillCard transFormClient(FillCard load) {
        ClientFillCard clientFillCard=new ClientFillCard(load);

        if(load!=null){
            User user = userDao.load(load.getCheckUid());
            if(user!=null){
                clientFillCard.setCheckName(user.getName());
            }
        }
        return clientFillCard;
    }

    @Override
    public FillCard add(FillCard fillCard) throws Exception {
        check(fillCard);
        FillCard insert = fillCardDao.insert(fillCard);
        if(insert!=null&&insert.getCheckUid()>0){
            ApprovalLog approvalLog=new ApprovalLog();
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
               List<FillCard> list=  fillCardDao.getPager(id,Double.parseDouble(cursor),size);
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
}
