package com.store.system.service;

import com.store.system.model.Commission;
import org.springframework.stereotype.Service;

/**
 * @ClassName CommissionService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/13 19:26
 * @Version 1.0
 **/
@Service
public interface CommissionService {

    public Commission add(Commission commission) throws Exception;

    public boolean update(Commission commission) throws Exception;
}
