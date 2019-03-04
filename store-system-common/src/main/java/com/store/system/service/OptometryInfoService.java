package com.store.system.service;

import com.store.system.client.ClientOptometryInfo;
import com.store.system.model.OptometryInfo;

import java.util.List;

public interface OptometryInfoService {

    public OptometryInfo add(OptometryInfo optometryInfo) throws Exception;

    public boolean update(OptometryInfo optometryInfo) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ClientOptometryInfo> getList(long cid, int size) throws Exception;

}
