package com.store.system.model;


import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 产品属性名
 * class_name: ProductPropertyName
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/6
 * creat_time: 11:05
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
public class ProductPropertyName implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    public static final int defaul_yes = 1; //默认 (关键属性，有单独的字段)
    public static final int defaul_no = 0; //非默认 (非关键的属性，json字段)

    public static final int multiple_yes = 1; //多值
    public static final int multiple_no = 0; //非多值

    public static final int input_yes = 1; //输入属性(选项)
    public static final int input_no = 0; //非输入属性

    public static final int type_spu = 1;  //SPU属性
    public static final int type_sku = 2;  //SKU属性

    @PrimaryKey
    private long id;

    private long cid; //类目id

    private int type; //属性类型 1-SPU属性 2-SKU属性

    private String content; //显示内容

    @SortKey
    private long sort;

    private int status;

    private int input; //是否输入属性 (输入的值非选项)

    private int defaul; //是否默认 (默认的不需要在页面上显示)

    private int multiple; //是否多值

    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getDefaul() {
        return defaul;
    }

    public void setDefaul(int defaul) {
        this.defaul = defaul;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
