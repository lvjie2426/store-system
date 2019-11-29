package com.store.system.client;

import com.store.system.model.ComboActivity;
import com.store.system.model.Coupon;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * @ClassName ClientComboActivity
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/29 15:18
 * @Version 1.0
 **/
@Data
public class ClientComboActivity extends ComboActivity {

    private List<ClientProductSKU> skuList;

    private List<ClientComboItem> itemList;

    private ClientProductSKU sku;

    private Coupon coupon;

    public ClientComboActivity(ComboActivity comboActivity) {
        try {
            BeanUtils.copyProperties(this, comboActivity);
        } catch (Exception e) {
            throw new IllegalStateException("ClientComboActivity construction error!");
        }
    }
}
