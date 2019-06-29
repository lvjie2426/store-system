package com.store.system.backend;


import com.store.system.util.ArithUtils;
import com.store.system.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName Demo
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/5/17 17:57
 * @Version 1.0
 **/
public class Demo {

    public static void main(String[] args) throws IllegalAccessException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        GregorianCalendar calendar = new GregorianCalendar();
//        calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - 1);
//        String currentYearAndMonth = sdf.format(calendar.getTime());

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.WEEK_OF_YEAR));//20190615 获得15

        String regex = "^[1-9]+(.[1-9]{1})?$";
//        String regex = "[1-9](\.[1-9])?|0\.[1-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("5.5");
        boolean flag = matcher.matches();
            System.err.println(flag);
    }
}
