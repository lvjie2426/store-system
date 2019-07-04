package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientPayPassport;
import com.store.system.model.PayPassport;

import java.util.List;

public interface PayService {

    public PayPassport loadPayPassport(long id) throws Exception;

    public boolean updatePayPassport(PayPassport payPassport) throws Exception;

    public PayPassport insertPayPassport(PayPassport payPassport) throws Exception;

    public boolean deletePayPassport(long id) throws Exception;

    public Pager getBackPayPassportPager(Pager pager) throws Exception;

    public List<ClientPayPassport> getAllList() throws Exception;

    public List<ClientPayPassport> getAllList(long subId) throws Exception;

    public String authParam(long passportId) throws Exception;

    public boolean updateStatus(long id, int status) throws Exception;

}
