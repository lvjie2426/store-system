package com.store.system.model.attendance;


import lombok.Data;

import java.io.Serializable;

@Data
public class SpecialDay implements Serializable {

    /**
     * 特殊天命名
     */
    private String name;

    /**
     * 是否上班
     */
    private boolean work;
    /**
     * 特殊的具体哪一天
     */
    private long day;
    /**
     * 开始时间
     */
    private int start;
    /**
     * 结束时间
     */
    private int end;

}
