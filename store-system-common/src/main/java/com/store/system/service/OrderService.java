package com.store.system.service;


import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.bean.CalculateOrder;
import com.store.system.client.ClientOrder;
import com.store.system.client.ResultClient;
import com.store.system.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    //阿里
    public ResultClient handleAliBarcodeOrder(String authCode, int type, String title, String desc, int price, long subId, long boId) throws Exception;

    public boolean aliOrderNotify(Map<String, String> params) throws Exception;

    public RefundOrder createAliRefundOrder(long oid) throws Exception;

    public boolean handleAliRefundOrder(long roid) throws Exception;


    //微信
    public ResultClient handleWxBarcodeOrder(HttpServletRequest request, String authCode, int type, String title, String desc,
                                             int price, long subId, long boId) throws Exception;

    public boolean wxOrderNotify(String xmlStr) throws Exception;

    public RefundOrder createWxRefundOrder(long oid) throws Exception;

    public boolean handleWxRefundOrder(HttpServletRequest request, long roid) throws Exception;


    public ResultClient handleOtherPay(int type, int price, long boId) throws Exception;


    public Pager getAll(Pager pager, long startTime, long endTime, long personnelid, int status, long uid, String name, int makeStatus, long subid) throws Exception;

    public Pager getBackPager(Pager pager, long subid, String name, String phone, String orderNo) throws Exception;

    public ResultClient saveOrder(Order order) throws Exception;

//    public List<ClientOrder> getAllBySubid(long subid)throws Exception;

    public ClientOrder countPrice(Order order) throws Exception;

    public Map<Object, Object> countSkuPrice(long uid, List<OrderSku> orderSkuList, long couponid, List<Surcharge> surchargeList, long sid) throws Exception;

//    public List<ClientOrder> getTemporaryOrder(long subid)throws Exception;

    public Pager getAllIncomplete(Pager pager, long startTime, long endTime, long personnelid, int status, long uid, String name, long subid, int makeStatus) throws Exception;

    public Map<String, Object> loadOrder(long id) throws Exception;

    public boolean updateOrder(Order order) throws Exception;

    public Map<String, Object> saleReward(long subid) throws Exception;

    public CalculateOrder calculateOrders(long subId, long startTime, long endTime) throws Exception;

    public Map<String, Integer> calculateSale(List<Order> orders) throws Exception;

    public Pager saleRewardApp(Pager pager,long subid) throws Exception;
}
