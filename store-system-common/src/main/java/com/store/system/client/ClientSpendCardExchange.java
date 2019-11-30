package com.store.system.client;

import com.store.system.model.SpendCardExchange;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * @ClassName ClientSpendCardExchange
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 16:20
 * @Version 1.0
 **/
@Data
public class ClientSpendCardExchange extends SpendCardExchange{

    private ClientProductSKU sku;

    private ClientProductSPU spu;

    private List<String> subNames;

    public ClientSpendCardExchange(SpendCardExchange spendCardExchange) {
        try {
            BeanUtils.copyProperties(this, spendCardExchange);
        } catch (Exception e) {
            throw new IllegalStateException("ClientSpendCardExchange construction error!");
        }
    }
}
