package com.store.system.util;

public class NumberUtils {

    public static String getPhoneNum(String phone){
       return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }
}
