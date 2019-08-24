package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.bean.CalculateOrder;
import com.store.system.client.ClientBusinessOrder;
import com.store.system.client.ClientOrder;
import com.store.system.client.ClientSettlementOrder;
import com.store.system.client.ResultClient;
import com.store.system.model.BusinessOrder;
import com.store.system.model.Order;
import com.store.system.model.PayInfo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BusinessOrderService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 17:19
 * @Version 1.0
 **/
public interface BusinessOrderService {

    public Pager getAllList(Pager pager, long startTime, long endTime, long staffId, int status,
                            long uid, String name, int makeStatus, long subId) throws Exception;

    public Pager getUnfinishedList(Pager pager, long startTime, long endTime, long staffId, int status,
                                   long uid, String name, long subId, int makeStatus) throws Exception;

    public Pager getBackPager(Pager pager, long subId, String name, String phone, String orderNo) throws Exception;

    public Pager getPager(Pager pager, long subId, long day, int status, int makeStatus) throws Exception;

    public Pager getPager(Pager pager, long subId, long startTime, long endTime, int status, int makeStatus) throws Exception;

    public ClientBusinessOrder add(BusinessOrder businessOrder) throws Exception;

    public ClientBusinessOrder load(long id) throws Exception;

    public List<ClientBusinessOrder> getAllList(long subId) throws Exception;

    public List<ClientBusinessOrder> getAllList(long subId, int status, int makeStatus) throws Exception;

    public List<BusinessOrder> getList(long subId, int status, int makeStatus, long time) throws Exception;

    public Map<String,Object> loadOrder(long id) throws Exception;

    public boolean update(BusinessOrder businessOrder) throws Exception;

    public boolean updateMakeStatus(long id, int makeStatus) throws Exception;

    public ResultClient currentCalculate(BusinessOrder businessOrder) throws Exception;

    public CalculateOrder calculateBusinessOrder(long subId, long startTime, long endTime) throws Exception;

    public Map<String,Integer> calculateSale(List<PayInfo> payInfos) throws Exception;

    public ClientSettlementOrder settlementPay(long boId, int cash, int stored, int otherStored) throws Exception;

    public ClientBusinessOrder settlementOrder(long boId, int cash, int stored, int otherStored, int score, int makeStatus,String desc) throws Exception;





}
