package com.store.system.model.attendance;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AttendanceTimeItem
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/19 17:04
 * @Version 1.0
 **/
@Data
public class AttendanceTimeItem implements Serializable {

    private String startTime;
    /**
     * 从0点开始 计算分钟   100=1点30分   600=10点
     */
    private String endTime;
}
