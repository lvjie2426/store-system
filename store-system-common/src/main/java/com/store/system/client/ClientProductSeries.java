package com.store.system.client;

import com.store.system.model.ProductSeries;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @ClassName ClientProductSeries
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/8/24 17:59
 * @Version 1.0
 **/
@Data
public class ClientProductSeries extends ProductSeries {

    private String brandName;

    public ClientProductSeries(ProductSeries productSeries) {
        try {
            BeanUtils.copyProperties(this, productSeries);
        } catch (Exception e) {
            throw new IllegalStateException("clientProductSeries construction error!");
        }
    }

}
