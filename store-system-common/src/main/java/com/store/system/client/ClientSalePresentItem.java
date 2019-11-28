package com.store.system.client;

import com.store.system.bean.SalePresentItem;
import com.store.system.model.Coupon;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @ClassName ClientSalePresentItem
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 17:04
 * @Version 1.0
 **/
@Data
public class ClientSalePresentItem extends SalePresentItem{

    private ClientProductSKU itemSku;

    private Coupon coupon;

    public ClientSalePresentItem(SalePresentItem salePresentItem) {
        try {
            BeanUtils.copyProperties(this, salePresentItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientSalePresentItem construction error!");
        }
    }
}
