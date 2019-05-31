package com.store.system.client;

import com.store.system.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientUser extends User {

    private String pSubName; //公司名称

    private String subName; //分店名称

    private String member;//会员等级称号

    private int payCount;//消费次数

    private int sale;//销售额(分)

    public ClientUser(User user) {
        try {
            BeanUtils.copyProperties(this, user);
            this.setPassword(null);
            this.setRand(-1);
        } catch (Exception e) {
            throw new IllegalStateException("ClientUser construction error!");
        }
    }

}
