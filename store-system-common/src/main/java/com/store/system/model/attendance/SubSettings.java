package com.store.system.model.attendance;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 门店/公司考勤设置
 * @ClassName SubSettings
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/11 17:31
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class SubSettings implements Serializable{

    public static final int status_on = 2; //开启
    public static final int status_off = 1; //关闭

    @PrimaryKey
    private long subId;

    /***
    * 签到开始时间
    */
    private long signTime;
    /***
     *人性化考勤状态
     */
    private int humanizedStatus;
    /***
     * 迟到时间(分钟)
     */
    private int lateTime;
    /***
     * 早退时间(分钟)
     */
    private int earlyTime;
    /***
     * 上班前提醒时间(分钟)
     */
    private int beforeTime;
    /***
     * 下班前提醒时间(分钟)
     */
    private int afterTime;


    @HyperspaceColumn(isJson = true)
    private List<PunchCardPlace> punchCardPlaces=new ArrayList<>();

    @HyperspaceColumn(isJson = true)
    private List<WirelessNetwork> wirelessNetworks=new ArrayList<>();

    private int placeStatus; //位置开启/关闭

    private int netStatus;  //网络设置开启/关闭

    @SortKey
    private long ctime;

    private long utime;

}
