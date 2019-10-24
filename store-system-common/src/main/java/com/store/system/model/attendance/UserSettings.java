package com.store.system.model.attendance;

import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 用户菜单设置
 * @ClassName SubSettings
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/11 17:31
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class UserSettings implements Serializable{

    public static final int status_on = 1; //开启
    public static final int status_off = 0; //关闭

    @PrimaryKey
    private long uid;


    private int workOvertime_status;

    private int changeShift_status;

    private int leave_status;

    private int fillCard_status;

    @SortKey
    private long ctime;

    private long utime;

}
