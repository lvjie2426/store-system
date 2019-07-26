package com.store.system.model;

import lombok.Data;

import java.io.Serializable;

/***
* 附加费用
* @Param:
* @return:
* @Author: LaoMa
* @Date: 2019/7/23
*/
@Data
public class Surcharge implements Serializable{

    private String name;

    private int price;//单位 分

}
