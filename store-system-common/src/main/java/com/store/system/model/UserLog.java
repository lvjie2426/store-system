package com.store.system.model;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import lombok.Data;

import java.io.Serializable;

@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure)
@Data
public class UserLog implements Serializable {

    public static final int type_examination=1;//体检
    public static final int type_train=2;//培训

    @ShardingKey
    @CombinationKey
    private long subid;

    @CombinationKey
    private long uid;

    private int type;// 1 体检 2培训

    private String num;// 编号

    private String job;// 职位


    private String examinationDate;//体检日期
    private String workYear; //从业年限
    private String examinationOrder; //体检结果

    private String education; //文化程度

    private String lecturer; //授课人
    private String trainForm; //培训形式
    private String trainUnit;//培训单位/体检医院
    private String trainContent;//培训内容/体检内容
    private double tests; //考试成绩

    private String desc; //备注



    @SortKey
    private long ctime;

    private long utime;

}
