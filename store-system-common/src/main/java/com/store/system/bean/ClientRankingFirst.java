package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ClientRankingFirst
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/16 14:43
 * @Version 1.0
 **/
@Data
public class ClientRankingFirst implements Serializable{

    private SimpleUser user;

    /***
    * 累计获得早起鸟次数
    */
    private int times;

}
