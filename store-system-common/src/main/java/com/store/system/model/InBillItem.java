package com.store.system.model;

import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.PrimaryKey;
import com.s7.space.enums.HyperspaceDomainType;
import com.s7.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 入库单条目
 * class_name: InBillItem
 * package: com.glasses.model
 * creat_user: lihao
 * creat_date: 2018/12/3
 * creat_time: 12:05
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
public class InBillItem implements Serializable {

    public static final int type_lens = 1; //镜片
    public static final int type_bracket = 2; //镜架
    public static final int type_contact_lenses = 3; //隐形眼镜
    public static final int type_sun_glasses = 4; //太阳镜
    public static final int type_care_product = 5; //护理产品
    public static final int type_special_goods = 6; //特殊商品

    @PrimaryKey
    private long id;

    private long ibid; //入库单ID

    private int type; //入库单条目类型

    private long gbid; //品牌ID

    private long gsid; //系列ID

    private long gpid; //供应商ID

    private long gid; //商品ID

    private String name; //名称

    private int num;

    private String otherFields; //其他属性

    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIbid() {
        return ibid;
    }

    public void setIbid(long ibid) {
        this.ibid = ibid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getGbid() {
        return gbid;
    }

    public void setGbid(long gbid) {
        this.gbid = gbid;
    }

    public long getGsid() {
        return gsid;
    }

    public void setGsid(long gsid) {
        this.gsid = gsid;
    }

    public long getGpid() {
        return gpid;
    }

    public void setGpid(long gpid) {
        this.gpid = gpid;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getOtherFields() {
        return otherFields;
    }

    public void setOtherFields(String otherFields) {
        this.otherFields = otherFields;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
