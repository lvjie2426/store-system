package com.store.system.client;

import com.store.system.bean.ComboItem;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @ClassName ClientComboItem
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/29 15:20
 * @Version 1.0
 **/
@Data
public class ClientComboItem extends ComboItem {

    private ClientProductSKU itemSku;

    public ClientComboItem(ComboItem comboItem) {
        try {
            BeanUtils.copyProperties(this, comboItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientComboItem construction error!");
        }
    }
}
