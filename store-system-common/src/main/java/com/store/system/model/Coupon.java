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

/**优惠券
 * @ClassName Coupon
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/26 16:17
 * @Version 1.0
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity,
        domainType = HyperspaceDomainType.mainDataStructure)
@Data
public class Coupon implements Serializable {

    public static final int STATUS_NORMAL = 0;//正常
    public static final int STATUS_DELETE = 1;//删除

    public static final int TYPE_FULL = 0;//满减
    public static final int TYPE_DIRECTIONAL = 1;//定向
    public static final int TYPE_DISCOUNT = 2;//折扣

    @PrimaryKey
    private long id;
    /***
     * 公司ID
     */
    private long sid;
    /***
     * 标题
     */
    private String title;
    /***
     * 消费满(分)
     */
    private int descFull;
    /***
     * 减(分) 折扣:8.8折
     */
    private double descSubtract;
    /***
     * 使用商品IDs
     */
    @HyperspaceColumn(isJson = true)
    private List<Long> skuIds;
    /***
     * 有效期开始时间
     */
    private long startTime;
    /***
     * 有效期结束时间
     */
    private long endTime;
    /***
     * 类型
     */
    private int type;
    /***
     * 状态
     */
    private int status;

    @SortKey
    private long ctime;

    private long utime;
}
