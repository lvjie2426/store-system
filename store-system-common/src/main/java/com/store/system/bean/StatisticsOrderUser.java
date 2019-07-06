package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 顾客管理--人数统计
 * @Date: 2019/7/6 11:18
 * @Version: 1.0
 */
@Data
public class StatisticsOrderUser implements Serializable{

    private int pNumber;//顾客人数

    private int man;//男比例

    private int woman;//女比例

    private int oldNumber;//老顾客占比

    private int vxNumber;//微信认证顾客占比

    private int phoneNumber;//手机号认证占比

    private int moneyNumber;//充值客户占比

}
