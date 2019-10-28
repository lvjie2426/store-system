package com.store.system.service;

public interface CodeService {

    public String getSpuCode() throws Exception;

    public String getSkuCode() throws Exception;

    public String getOrderCode() throws Exception;

    public String getRechargeCode() throws Exception;
    //提货单
    public String getT_orderCode() throws Exception;

    //补货单
    public String getB_orderCode() throws Exception;

    //结算单
    public String getJ_orderCode() throws Exception;
}
