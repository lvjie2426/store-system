package com.store.system.model.attendance;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**补卡申请
 * @ClassName FillCard
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/17 18:48
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class FillCard implements Serializable {

    public static final int status_ask=0;//申请中
    public static final int status_success=1;//审核通过
    public static final int status_fail=2;//审核不通过

    public static final int type_in=1;//上班打卡
    public static final int type_out=2;//下班打卡


    @PrimaryKey
    private long id;
    /***
    * 补卡类型
    */
    private int type;
    /***
     * 补卡时间
     */
    private long time;
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
     *  抄送UID
     */
    private long copyUid;

    private int status;

    private String reason;

    /**
     * 附带图片
     */
    @HyperspaceColumn(isJson = true)
    private List<String> imgs;

    @SortKey
    private long ctime;

    private long utime;


}
