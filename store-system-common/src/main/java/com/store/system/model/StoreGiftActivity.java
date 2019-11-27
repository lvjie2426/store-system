package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**储值送礼活动
 * @ClassName StoreGiftActivity
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/25 17:04
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class StoreGiftActivity implements Serializable{

    public static final int STATUS_NORMAL = 0;//正常
    public static final int STATUS_DELETE = 1;//删除


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
     * 一次性储值金额(分)
     */
    private int storePrice;
    /***
     * 优惠券ID
     */
    private long couponId;
    /***
     * 活动开始时间
     */
    private long startTime;
    /***
     * 活动结束时间
     */
    private long endTime;
    /***
     * 状态
     */
    private int status;

    @SortKey
    private long ctime;

    private long utime;
}
