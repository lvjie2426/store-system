package com.store.system.client;

import com.store.system.model.PayPassport;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @ClassName ClientPayPassport
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/4 14:05
 * @Version 1.0
 **/
@Data
public class ClientPayPassport extends PayPassport{

    private String subName;

    public ClientPayPassport(PayPassport payPassport) {
        try {
            BeanUtils.copyProperties(this, payPassport);
        } catch (Exception e) {
            throw new IllegalStateException("ClientPayPassport construction error!");
        }
    }
}
