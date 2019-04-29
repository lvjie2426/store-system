package com.store.system.client;

import com.store.system.model.OptometryInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientOptometryInfo extends OptometryInfo {

    private String pSubName; //公司名称

    private String subName; //分店名称

    private String customerName;

    private String optUserName;

    public ClientOptometryInfo(OptometryInfo optometryInfo) {
        try {
            BeanUtils.copyProperties(this, optometryInfo);
        } catch (Exception e) {
            throw new IllegalStateException("ClientOptometryInfo construction error!");
        }
    }


}
