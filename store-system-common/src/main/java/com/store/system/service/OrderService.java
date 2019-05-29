package com.store.system.service;


import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientOrder;
import com.store.system.model.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {

    public boolean handleAliBarcodeOrder(long passportId, String authCode, int type, String typeInfo,
                                         String title, String desc, double price) throws Exception;

    public boolean handleWxBarcodeOrder(HttpServletRequest request, long passportId, String authCode, int type, String typeInfo,
                                        String title, String desc, double price, String ip) throws Exception;

    public  Pager getAll(Pager pager, long startTime, long endTime, long personnelid, int status,long uid,String name,int makeStatus,long subid) throws Exception;

    public Order saveOrder(Order order)throws Exception;

    public  List<ClientOrder> getAllBySubid(long subid)throws Exception;

    public Order countPrice(Order order)throws Exception;

    public  List<ClientOrder> getTemporaryOrder(long subid)throws Exception;

    public   Pager getAllIncomplete(Pager pager, long startTime, long endTime, long personnelid, int status, long uid, String name, long subid,int makeStatus)throws Exception;
}
