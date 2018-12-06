package com.store.system.bean;

import java.io.Serializable;

public class GlassesInformation implements Serializable {

    private String ball; //球

    private String column; //柱

    private long value; //价格分 备货片 积分分数

    public String getBall() {
        return ball;
    }

    public void setBall(String ball) {
        this.ball = ball;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GlassesInformation{" +
                "ball='" + ball + '\'' +
                ", column='" + column + '\'' +
                ", value=" + value +
                '}';
    }
}
