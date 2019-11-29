package com.store.system.client;

import com.store.system.exception.StoreSystemException;
import com.store.system.model.IntegralActivity;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ClientIntegralActivity
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/29 11:58
 * @Version 1.0
 **/
@Data
public class ClientIntegralActivity extends IntegralActivity{

    /***
    * 会员等级名称
    */
    private List<String> ugNames;

    private Map<Long,List<ClientProductSKU>> skuMap;

    public  ClientIntegralActivity(IntegralActivity integralActivity){
        try {
            BeanUtils.copyProperties(this,integralActivity);
        } catch (Exception e) {
            throw  new StoreSystemException("ClientIntegralActivity copy bean error");
        }
    }
}
