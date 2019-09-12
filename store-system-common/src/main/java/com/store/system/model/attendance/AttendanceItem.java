package com.store.system.model.attendance;

import lombok.Data;

import java.io.Serializable;


/**
 * 考勤子项目
 */
@Data
public class AttendanceItem  implements Serializable{


    /***
    * 时间戳作为ID
    */
    private long timeId;
    /**
     * 从0点开始 计算分钟   100=1点30分   600=10点
     */
    private int start=-1;
    /**
     * 从0点开始 计算分钟   100=1点30分   600=10点
     */
    private int end=-1;

}
