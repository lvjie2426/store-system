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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AttendanceTemplate
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/8/14 16:08
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class AttendanceTemplate implements Serializable{

    public static final int type_fix=0;// 固定
    public static final int type_turn=1; //轮换

    public static final int holidayType_normal=0;//国家法定
    public static final int holidayType_custom=1;//自定义


    @PrimaryKey
    private long id;

    private String name;//考勤模板名称

    private long sid;//公司ID

    private long uid;//创建人id

    //轮换制,固定制度
    private int type;
    /**
     * 节假日类型
     * 1.国家法定节假日
     * 2.自定义
     */
    private int holidayType;


    //自定义
    //上班时间 (1,2,3,4,5,6,7代表一周7天上班)
    @HyperspaceColumn(isJson = true)
    private List<Integer> workWeekDay=new ArrayList<>();


    //弹性工作制(十分钟 表示可以迟到十分钟)
    private int flextime;


    //特殊日期（手动录入，比如添加某天为不考勤，,XX号 早晚   XX号早晚）
    //key:day-value:详细信息
    @HyperspaceColumn(isJson = true)
    private Map<Long,SpecialDay> special = new HashMap<>();


    //轮换详细（第一条：早，晚，第二条：早晚，第三条：早晚）
    @HyperspaceColumn(isJson = true)
    private List<AttendanceItem> turn = new ArrayList<>();


    @HyperspaceColumn(isJson = true)
    private Map<Long,AttendanceItem> turnMap = new HashMap<>();
    /**
     * 轮换开始时间
     * 天，time/1000/60/60/24 从1970年以来的天数
     */
    private long turnStartDay;

    /**
     * 从0点开始 计算分钟   100=1点30分   600=10点
     */
    private int start=-1;

    /**
     * 从0点开始 计算分钟   100=1点30分   600=10点
     */
    private int end=-1;

    @SortKey
    private long ctime;

    private long utime;

}
