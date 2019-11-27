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

/**双倍积分活动
 * @ClassName IntegralActivity
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/25 18:14
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class IntegralActivity implements Serializable{

    public static final int STATUS_NORMAL = 0;//正常
    public static final int STATUS_DELETE = 1;//删除

    public static final int TYPE_MONEY = 1; //金额
    public static final int TYPE_RATE = 2; //百分比

    @PrimaryKey
    private long id;
    /***
     * 门店ID
     */
    private long subId;
    /***
     * 标题
     */
    private String title;
    /***
     * 活动商品IDs
     */
    @HyperspaceColumn(isJson = true)
    private List<Long> skuIds;
    /***
     * 活动开始时间 长期有效=0
     */
    private long startTime;
    /***
     * 活动结束时间 长期有效=50之后的时间戳
     */
    private long endTime;
    /***
     * 类型
     */
    private int type;
    /***
     * 折扣代表：减折(8.8折)
     * 数额代表：分
     */
    private double descSubtract;

    /***
     * 为空代表所有人
     */
    @HyperspaceColumn(isJson = true)
    private List<Long> ugIds;

    /***
     * 状态
     */
    private int status;

    @SortKey
    private long ctime;

    private long utime;


}
