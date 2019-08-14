package com.store.system.model;

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


/**
 * 医疗器械 企业生产商/供应商
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class Company implements Serializable {

    public static final int status_online = 0;//线上
    public static final int status_delete = 1;//删除

    public static final int checkStatus_no = 0;//未审核
    public static final int checkStatus_yes = 1;//审核通过

    public static final int mp_mf = 1;//生产商
    public static final int mp_pro = 2;//供应商

    @PrimaryKey
    private long id;
    //企业名称
    private String name;
    // 联系人
    private String userName;
    //联系电话
    private String phone;

    //经营范围
    @HyperspaceColumn(isJson = true)
    private List<String> range = new ArrayList<>();

    //企业营业执照照片
    @HyperspaceColumn(isJson = true)
    private List<String> storeImg = new ArrayList<>();

    //企业营业执照编号
    private String comBusinessNum;

    //企业营业执照发证日期
    private long comBusinessDate;

    //企业营业执照有效日期结束时间
    private long comBusinessDateEnd;

    //企业营业执照有效日期开始时间
    private long comBusinessDateStart;

    //生产商/供应商
    @HyperspaceColumn(isJson = true)
    private List<Long> manufacturerProvider = new ArrayList<>();

    //医疗器械注册证编号
    private String mfNum;

    //医疗器械注册证批准日期
    private long mfRegisterDate;

    //医疗器械注册证有效日期结束时间
    private long mfRegisterDateEnd;

    //医疗器械注册证有效日期开始时间
    private long mfRegisterDateStart;

    //医疗器械注册证照片、
    @HyperspaceColumn(isJson = true)
    private List<String> mfRegisterImg = new ArrayList<>();

    //生产许可证编号
    private String licenceNum;
    //生产许可证发证日期
    private long licenceDate;

    //生产许可证有效日期结束时间
    private long licenceDateEnd;

    //生产许可证有效日期开始时间
    private long licenceDateStart;

    //生产许可证照片
    @HyperspaceColumn(isJson = true)
    private List<String> licenceImg = new ArrayList<>();

    //生产许可证其他资料照片
    @HyperspaceColumn(isJson = true)
    private List<String> licenceDescImg = new ArrayList<>();

    //医疗器械经营许可证
    private String proNum;

    //供应商经营许可证批准日期
    private long proDate;

    //供应商经营许可证有效日期结束时间
    private long proDateEnd;

    //供应商经营许可证有效日期开始时间
    private long proDateStart;

    //经营许可证照片
    @HyperspaceColumn(isJson = true)
    private List<String> proImg = new ArrayList<>();

    //经营许可证其他资料照片
    @HyperspaceColumn(isJson = true)
    private List<String> proDescImg = new ArrayList<>();


    //详细地址
    private String address;

    private long province;//省

    private long city;//城市

    private long area;//区

    private long status;

    private long checkStatus;

    @SortKey
    private long ctime;

    private long utime;

}
