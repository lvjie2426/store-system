package com.store.system.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterStringUtil {

    /**
     * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("[　*| *| *|//s*\\[\\] |\r|\n]*", "");
        }
        return result;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        boolean res = true;
        if (StringUtils.isNotBlank(str)) {
            res = pattern.matcher(str).matches();
        }
        return res;
    }

    public static boolean checkDiscount(String discount) {
        boolean res = false;
        if(StringUtils.isNotBlank(discount)) {
            String regex = "^(\\d(\\.\\d)?|10)$";
            if(discount.equals("10")){
                return true;
            }
            res = Pattern.matches(regex,discount);
        }
        return res;
    }
}
