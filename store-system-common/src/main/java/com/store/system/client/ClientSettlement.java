package com.store.system.client;

import com.store.system.model.Settlement;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @ClassName ClientSettlement
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/8 16:39
 * @Version 1.0
 **/
@Data
public class ClientSettlement extends Settlement {

    private String subName; //门店名称

    private String userName; //结算人

    private Settlement last; //上次结算

    private String lastTime; //上次结算时间

    public ClientSettlement(Settlement settlement) {
        try {
            BeanUtils.copyProperties(this, settlement);
        } catch (Exception e) {
            throw new IllegalStateException("ClientSettlement construction error!");
        }
    }
}
