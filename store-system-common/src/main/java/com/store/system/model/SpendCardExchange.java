package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**花卡兑换设置
 * @ClassName SpendCardExchange
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/29 17:19
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class SpendCardExchange implements Serializable {
}
