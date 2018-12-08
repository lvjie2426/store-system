package com.store.system.service;


import com.store.system.client.ClientProductProperty;

import java.util.List;

public interface ProductPropertyService {

    public List<ClientProductProperty> getAllProperties(long cid) throws Exception;

}
