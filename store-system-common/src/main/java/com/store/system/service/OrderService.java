package com.store.system.service;


import com.quakoo.baseFramework.model.pagination.Pager;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    public boolean handleAliBarcodeOrder(long passportId, String authCode, int type, String typeInfo,
                                         String title, String desc, double price) throws Exception;

    public boolean handleWxBarcodeOrder(HttpServletRequest request, long passportId, String authCode, int type, String typeInfo,
                                        String title, String desc, double price, String ip) throws Exception;

    public  Pager getAll(Pager pager, long startTime, long endTime, long personnelid, int status,long uid,String name,int makeStatus,long subid) throws Exception;
}
