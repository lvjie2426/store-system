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

/**
 * 营销管理-定时短信
 * class_name: MarketingShotMsg
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/3/14
 * creat_time: 15:25
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
@Data
public class MarketingTimingSms implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    public static final String tag_user_name = "@var(user_name)"; //顾客姓名
    public static final String tag_sub_name = "@var(sub_name)"; //门店名称

    public static final int range_all = 0; //全部
    public static final int range_vip = 1; //会员
    public static final int range_no_vip = 2; //非会员

    public static final int sex_all = 0;//全部
    public static final int sex_man = 1;//男
    public static final int sex_woman = 2;//女

    public static final int send_no = 0; //未发送
    public static final int send_yes = 1; //已发送

    @PrimaryKey
    private long id;

    private long subid;

    private String content;
    private String job;

    @HyperspaceColumn(isJson = true)
    private List<String> tags;

    private int range;

    private int sex;

    private int startAge; //起始年龄

    private int endAge; //结束年龄

    @SortKey
    private long sendTime;

    private int status;

    private int send;

    private long ctime;

    private long utime;

    public static void main(String[] args) {
        String str = "@var(user_name)";
        System.out.println(str.replace("@var(","").replace(")",""));

    }
}
