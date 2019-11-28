package com.store.system.client;

import com.store.system.model.SalePresentActivity;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * @ClassName ClientSalePresentActivity
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 17:01
 * @Version 1.0
 **/
@Data
public class ClientSalePresentActivity extends SalePresentActivity {

    private ClientProductSKU sku;

    private List<ClientSalePresentItem> clientItems;

    public ClientSalePresentActivity(SalePresentActivity salePresentActivity) {
        try {
            BeanUtils.copyProperties(this, salePresentActivity);
        } catch (Exception e) {
            throw new IllegalStateException("ClientSalePresentActivity construction error!");
        }
    }
}
