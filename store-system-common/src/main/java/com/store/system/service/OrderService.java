package com.store.system.service;


import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    public boolean handleAliBarcodeOrder(long passportId, String authCode, int type, String typeInfo,
                                         String title, String desc, double price) throws Exception;

    public boolean handleWxBarcodeOrder(HttpServletRequest request, long passportId, String authCode, int type, String typeInfo,
                                        String title, String desc, double price, String ip) throws Exception;

}
