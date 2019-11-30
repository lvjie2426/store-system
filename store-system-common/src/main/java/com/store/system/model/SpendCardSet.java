package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**花卡获得设置
 * @ClassName SpendCardSet
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/29 17:13
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class SpendCardSet implements Serializable {

    public static final int TYPE_CATE = 1;//类目
    public static final int TYPE_SPU = 2;//spu

    @PrimaryKey
    private long id;
    /***
     * 公司ID
     */
    private long psid;
    /***
     * 类目ID
     */
    private long cid;
    /***
     * SpuID
     */
    private long spuId;

    private int type;
    /***
     * 消费满 (分)
     */
    private int price;
    /***
     * 花卡数量
     */
    private int num;
    /***
     * 状态
     */
    private int status;


    @SortKey
    private long ctime;

    private long utime;
}
