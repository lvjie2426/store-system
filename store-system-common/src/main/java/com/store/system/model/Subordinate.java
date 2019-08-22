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
 * 公司，门店
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class Subordinate implements Serializable {
    public static final  String defaul_pwd="123456";

    public static final int status_online = 0;//线上
    public static final int status_delete = 1;//删除

    public static final int storedType_per = 0;//本店自己
    public static final int storedType_pub = 1;//与其他店公用

    public static final int payType_wx_per = 1;//微信个人
    public static final int payType_wx_pub = 2;//微信公共
    public static final int payType_ali_pub = 3;//支付宝公共
    public static final int payType_ali_per = 4;//支付宝个人

    public static final int processType_per = 0;//本店加工
    public static final int processType_pub = 1;//其他店加工

    public static final int isCheck_yes=0;//入库需要审核
    public static final int isCheck_no = 1;//入库不需要审核,默认店长审核通过


    @PrimaryKey
    private long id;

    private long pid; //父类ID(0：公司，非0：分店)

    private String name;
    // 门店简码
    private String storeCode;
    //门店照片
    @HyperspaceColumn(isJson = true)
    private List<String> storeImg=new ArrayList<>();

    private String cover;
    private String content;//介绍

    private String desc;
    private String phone; //联系电话
    private String icon;

    private String address;

    private long adminId;//管理员ID

    private String adminPhone;//管理员手机号，手机号+验证码登录时候用

    private String adminUserName;//管理员账号名 账号名+密码登录时候用

    private String adminPassword;//管理员密码 账号名+密码登录时候用

    private int status;

    private int isCheck;//是否要审核入库

    private long province;//省
    private long city;//城市
    private long area;//区
    private long roleInitTemplateId;//角色模板ID
    //营业开始时间
    private String startTime;
    //营业结束时间
    private String endTime;
    //支付方式
    private int payType;
    //储值方式
    private int storedType;
    //加工方式
    private int processType;
    //加工中心
    @HyperspaceColumn(isJson = true)
    private List<String> process=new ArrayList<>();
    //三包政策
    private String threePolicy;


    @SortKey
    private long ctime;

    private long utime;

}
