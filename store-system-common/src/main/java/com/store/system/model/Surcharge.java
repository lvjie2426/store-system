package com.store.system.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class Surcharge implements Serializable{

    private  String name;
    private  double price;


}
