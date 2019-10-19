package com.store.system.model.attendance;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;
import java.io.Serializable;
import java.util.List;


/**
 * 请假
 */
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class Leave implements Serializable {


    public static final int leaveType_shijia=0;//事假
    public static final int leaveType_bingjia=1;//病假
    public static final int leaveType_daoxiu=2;//倒休
    public static final int leaveType_nianjia=3;//年假
    public static final int leaveType_yuejia=4;//月假
    public static final int leaveType_hunjia=5;//婚假
    public static final int leaveType_chanjianjia=6;//产检假
    public static final int leaveType_chajia=7;//产假
    public static final int leaveType_peichanjia=8;//陪产假
    public static final int leaveType_sangjia=9;//丧家
    public static final int leaveType_ext=10;//其他

    public static final int status_ask=0;//请假中
    public static final int status_success=1;//审核通过
    public static final int status_fail=2;//审核不通过

    @PrimaryKey
    private long id;
    /**
     * 请假类型
     */
    private int leaveType;
    /**
     * 请假时长(小时)
     */
    private double leaveTime;
    /**
     * 请假状态
     */
    private int status;
    /**
     * 公司ID
     */
    private long sid;
    /**
     * 门店ID
     */
    private long subId;
    /**
     * 请假人UID
     */
    private long askUid;
    /**
     * 审批人UID
     */
    private long checkUid;
    /**
    *  抄送UID
    */
    private long copyUid;
    /**
     * 请假开始
     */
    private long startTime;
    /**
     * 请假结束
     */
    private long endTime;
    /**
     * 附带图片
     */
    @HyperspaceColumn(isJson = true)
    private List<String> imgs;
    /**
     * 内容
     */
    private String content;
    /**
     * 审核理由
     */
    private String reason;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;


}
