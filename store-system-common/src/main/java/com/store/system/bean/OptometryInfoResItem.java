package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class OptometryInfoResItem implements Serializable {

    private String nakedEyesight; //裸眼视力

    private String s; //球镜

    private String c; //柱镜

    private String a; //轴位

    private String correctEyesight; //矫正视力

    private String pd; //瞳距

    private String v; //棱镜

    private String sc; //基底

}
