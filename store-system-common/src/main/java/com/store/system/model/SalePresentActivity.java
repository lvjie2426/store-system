package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import com.store.system.bean.SalePresentItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**买赠活动
 * @ClassName SalePresentActivity
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/25 17:44
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class SalePresentActivity implements Serializable {

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
     * 购买商品
     */
    private long skuId;
    /***
     * 购买数量
     */
    private int num;
    /***
     * 优惠券ID
     */
    @HyperspaceColumn(isJson = true)
    private List<SalePresentItem> items;
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
