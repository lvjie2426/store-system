package com.store.system.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quakoo.baseFramework.json.JsonUtils;
import com.quakoo.baseFramework.secure.AESUtils;
import com.quakoo.baseFramework.secure.Base64Util;
import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class User implements Serializable {

    public static String key = "glasses_user_key";


    public static final int userType_user=0;//前端用户
    public static final int userType_backendUser=1;//后台用户

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

    private String phone;//手机号，手机号+验证码登录时候用


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
    @HyperspaceColumn(isDbColumn = false)
    private List<Long> rids=new ArrayList<>();//角色ID
    private String ridsJson;//#v500# ^nn^角色ID

    private long sid; //下属单位ID，0表示平台

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

    private int age; // 年龄


    private String lastIp;//最后的ip


    private int score;

    private String plat;

    private String platVersion;

    private String ext;// 其他信息（）


    @SortKey
    private long ctime;

    private long utime;

    //========常用字段结束===========

    private int favourNum;  //收藏数量

    private int focousNum;  //关注数量

    private int fansNum;

    private String height;//身高

    private String weight;//体重

    private String waistline;//腰围

    private String hipline ;//臀围

    private String chest;//胸围

    private String leg;//腿长

    private String bar;//罩杯




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }




    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }



    public String createToken() throws Exception {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);
        map.put("rand", rand);
        return Base64Util.encode(AESUtils.encrypt(JsonUtils.objectMapper.writeValueAsBytes(map), key));
    }

    public long getRand() {
        return rand;
    }

    public void setRand(long rand) {
        this.rand = rand;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Long> getRids() {
        return rids;
    }

    public void setRids(List<Long> rids) {
        this.rids = rids;
    }

    public String getRidsJson() {
        return ridsJson;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getAlipayId() {
        return alipayId;
    }

    public void setAlipayId(String alipayId) {
        this.alipayId = alipayId;
    }

    public String getWeiboInfo() {
        return weiboInfo;
    }

    public void setWeiboInfo(String weiboInfo) {
        this.weiboInfo = weiboInfo;
    }

    public String getWeixinInfo() {
        return weixinInfo;
    }

    public void setWeixinInfo(String weixinInfo) {
        this.weixinInfo = weixinInfo;
    }

    public String getQqInfo() {
        return qqInfo;
    }

    public void setQqInfo(String qqInfo) {
        this.qqInfo = qqInfo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFavourNum() {
        return favourNum;
    }

    public void setFavourNum(int favourNum) {
        this.favourNum = favourNum;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public String getPlatVersion() {
        return platVersion;
    }

    public void setPlatVersion(String platVersion) {
        this.platVersion = platVersion;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setRidsJson(String ridsJson) throws IOException {
        this.ridsJson = ridsJson;
        if(StringUtils.isNotBlank(ridsJson))
            this.rids=JsonUtils.parse(ridsJson, new TypeReference<List<Long>>() {});

    }

    public void createJsonString() throws Exception {

        if(rids!=null){
            this.setRidsJson(JsonUtils.format(rids));
        }
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWaistline() {
        return waistline;
    }

    public void setWaistline(String waistline) {
        this.waistline = waistline;
    }

    public String getHipline() {
        return hipline;
    }

    public void setHipline(String hipline) {
        this.hipline = hipline;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getLeg() {
        return leg;
    }

    public void setLeg(String leg) {
        this.leg = leg;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public int getFocousNum() {
        return focousNum;
    }

    public void setFocousNum(int focousNum) {
        this.focousNum = focousNum;
    }
}
