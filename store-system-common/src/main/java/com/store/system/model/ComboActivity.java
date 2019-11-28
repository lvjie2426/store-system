package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import com.store.system.bean.ComboItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ComboActivity
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/25 19:06
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class ComboActivity implements Serializable {

    public static final int STATUS_NORMAL = 0;//正常
    public static final int STATUS_DELETE = 1;//删除

    public static final int TYPE_ORIGINAL = 0;//原价
    public static final int TYPE_VIP = 1;//会员价

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
     * 原/会员价
     */
    private int type;
    /***
     * 活动商品IDs
     */
    @HyperspaceColumn(isJson = true)
    private List<Long> skuIds;

    @HyperspaceColumn(isJson = true)
    private List<ComboItem> items;

    /***
     * 赠品
     */
    private long skuId;
    /***
     * 优惠券ID
     */
    private long couponId;


    /***
     * 状态
     */
    private int status;

    @SortKey
    private long ctime;

    private long utime;
}