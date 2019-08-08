package com.store.system.service.ext;

public interface OrderRefundService {

    public void successHandleBusiness(int payType, String typeInfo) throws Exception;

    public void failHandleBusiness(int payType, String typeInfo);

}
