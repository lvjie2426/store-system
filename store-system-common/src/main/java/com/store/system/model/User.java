package com.store.system.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quakoo.baseFramework.json.JsonUtils;
import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.baseFramework.secure.AESUtils;
import com.quakoo.baseFramework.secure.Base64Util;
import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import com.store.system.util.ArithUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class User implements Serializable {

    public static String key = "glasses_user_key";


    public static final int userType_user=0;//前端用户 顾客
    public static final int userType_backendUser=1;//后台用户 员工

    public static final int sex_nomore=0;//未知
    public static final int sex_mai=1;//男
    public static final int sex_woman=2;//女

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除


    //========基本登录用字段===========

    @PrimaryKey
    private long id;

    private long rand;

    private int userType;//用户类型（一般分为普通用户、后台用户）

    private String job;//职业

    private String phone;//手机号，手机号+验证码登录时候用

    private String contactPhone;//联系人

    private String userName;//账号名 账号名+密码登录时候用

    private String password;//密码 账号名+密码登录时候用


    private String weiboId;// 微博的唯一标识，通过微博登陆使用

    private String weixinId;// 微信的唯一标识，通过微信登陆使用

    private String qqId;// qq的唯一标识，通过QQ登陆使用

    private String alipayId; //支付宝的唯一标示


    // ==包含token等信息===
    private String weiboInfo;// 微博信息

    private String weixinInfo;// 微信信息

    private String qqInfo;// qq信息

    /**
     * 角色
     */
    @HyperspaceColumn(isJson = true)
    private List<Long> rids=new ArrayList<>();//角色ID

    private long psid; //上级单位ID
    private long sid;  //下级单位ID，0表示平台

    private int status;


    //========基本信息字段===========

    private String name;

    private String icon;

    private String mail;


    /**
     * 身份证
     */
    private String idCard;

    private String desc;


    /**
     * 居住地
     */
    private String place;
    /**
     * 用户封面图
     */
    private String cover;

    private int sex;// 0未知，1男，2女

    private long birthdate;//出生日期

    private long workingDate;//入职日期

    private int age; // 年龄


    private String lastIp;//最后的ip


    private int score; //积分

    private int money; //储值金额

    private long aid;//考勤ID

    private String plat;

    private String platVersion;

    private String ext;// 其他信息（）

    private long userGradeId;//用户会员等级

    private long recommender;//推荐人

    private int cardNumber;//会员卡

    @HyperspaceColumn(isJson = true)
    private Map<String, String> bankCard=new HashMap<String, String>(); //银行卡号

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

    //========常用字段结束===========

    public String createToken() throws Exception {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);
        map.put("rand", rand);
        return Base64Util.encode(AESUtils.encrypt(JsonUtils.objectMapper.writeValueAsBytes(map), key));
    }
    public static void main(String[] agrs) throws Exception {


        Map<String, Long> map = new HashMap<>();
        map.put("id", 49L);
        map.put("rand", 356482L);
        System.out.println(URLEncoder.encode(Base64Util.encode(AESUtils.encrypt(JsonUtils.objectMapper.writeValueAsBytes(map), key))));

    }
}
