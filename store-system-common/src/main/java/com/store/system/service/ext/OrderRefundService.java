package com.store.system.service.ext;

public interface OrderRefundService {

    public void successHandleBusiness(int type, String typeInfo);

    public void failHandleBusiness(int type, String typeInfo);

}