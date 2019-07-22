package com.store.system.model;

import com.google.common.collect.Lists;
import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品SKU
 * class_name: ProductSKU
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/6
 * creat_time: 16:29
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
@Data
public class ProductSKU implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    public static final int eyeType_left=1;//左眼
    public static final int eyeType_right=2;//右眼

    @PrimaryKey
    private long id;

    private long spuid; //SPU的ID

    private String code; //产品编码

    private String name; //sku名称

    @HyperspaceColumn(isJson = true)
    private Map<Long, Object> properties=new HashMap<Long,Object>(); //属性json

    private int retailPrice; //零售价(分)

    private int costPrice; //成本价(分)

    private int integralPrice; //积分价

    private int num; //备货量

    private int eyeType;

    private String other; //附加属性

    private int status;

    @SortKey
    private long sort;

    private long ctime;

    private long utime;

    public Map<Long, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<Long, Object> properties) {
        this.properties = properties;
    }

}
