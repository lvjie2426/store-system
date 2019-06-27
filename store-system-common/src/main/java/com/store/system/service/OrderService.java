package com.store.system.service;


import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.bean.SaleReward;
import com.store.system.client.ClientOrder;
import com.store.system.client.ResultClient;
import com.store.system.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface OrderService {

    //阿里
    public ResultClient handleAliBarcodeOrder(long passportId, String authCode, int type, String typeInfo,
                                              String title, String desc, int price, Order order) throws Exception;

    public boolean aliOrderNotify(Map<String, String> params) throws Exception;

    public RefundOrder createAliRefundOrder(long oid) throws Exception;

    public boolean handleAliRefundOrder(long roid) throws Exception;


    //微信
    public ResultClient handleWxBarcodeOrder(HttpServletRequest request, long passportId, String authCode, int type, String typeInfo,
                                        String title, String desc, int price,  Order order) throws Exception;

    public boolean wxOrderNotify(String xmlStr) throws Exception;

    public RefundOrder createWxRefundOrder(long oid) throws Exception;

    public boolean handleWxRefundOrder(HttpServletRequest request, long roid) throws Exception;


    public  Pager getAll(Pager pager, long startTime, long endTime, long personnelid, int status,long uid,String name,int makeStatus,long subid) throws Exception;

    public  Pager getBackPager(Pager pager, long subid, String name, String phone, String orderNo) throws Exception;

    public ClientOrder saveOrder(Order order)throws Exception;

    public  List<ClientOrder> getAllBySubid(long subid)throws Exception;

    public ClientOrder countPrice(Order order)throws Exception;

    public  Map<Object,Object> countSkuPrice(long uid, List<OrderSku> orderSkuList, long couponid, List<Surcharge> surchargeList) throws Exception;

    public  List<ClientOrder> getTemporaryOrder(long subid)throws Exception;

    public   Pager getAllIncomplete(Pager pager, long startTime, long endTime, long personnelid, int status, long uid, String name, long subid,int makeStatus)throws Exception;

    public Map<String,Object> loadOrder(long id) throws Exception;

    public Map<String,Object> saleReward(long subid)throws Exception;

}
