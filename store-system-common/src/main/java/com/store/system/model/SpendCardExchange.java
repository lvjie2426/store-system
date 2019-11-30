package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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

    @PrimaryKey
    private long id;
    /***
     * 公司ID
     */
    private long psid;
    /***
     * SPU
     */
    private long spuId;
    /***
    * 可兑换商品
    */
    private long skuId;
    /***
     * 兑换所需花卡
     */
    private int cardNum;
    /***
     * 可兑换数量
     */
    private int num;
    /***
     * 可兑换门店
     */
    @HyperspaceColumn(isJson = true)
    private List<Long> subIds;
    /***
     * 状态
     */
    private int status;


    @SortKey
    private long ctime;

    private long utime;
}
