package com.store.system.bean;

import com.store.system.model.Commission;
import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 销售奖励表 返回值用
 * @Date: 2019/6/14 11:15
 * @Version: 1.0
 */
@Data
public class SaleReward implements Serializable{

    private String productName ;//商品名称

    private String bName ;//品牌

    private String sName ;//系列

    private int number ;//完成量

    private Commission commission;

    private int royaltyPersonal;//个人奖励(分)

    private int royaltyTeam;//团队奖励(分)

}
