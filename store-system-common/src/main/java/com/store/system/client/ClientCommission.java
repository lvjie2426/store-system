package com.store.system.client;

import com.store.system.model.Commission;
import com.store.system.model.FinanceLog;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-08-31 11:52
 **/
@Data
public class ClientCommission extends Commission{

    private String code;

    public ClientCommission(Commission commission) {
        try {
            BeanUtils.copyProperties(this, commission);
        } catch (Exception e) {
            throw new IllegalStateException("commission construction is error!");
        }
    }
}