package com.store.system.util;

import com.google.common.collect.Lists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {

    public static long getDayFormTime(long time){
       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
       String timeStr=simpleDateFormat.format(time);
       return  Long.parseLong(timeStr);
    }
    public static long getTimeFormDay(long day){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        Date time= null;
        try {
            time = simpleDateFormat.parse(""+day);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return time.getTime();
    }

    public static int getWeekDayFormDay(long day){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        Date time= null;
        try {
            time = simpleDateFormat.parse(""+day);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int result = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return result;
    }

    public static long getMonthFormTime(long time){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMM");
        String timeStr=simpleDateFormat.format(time);
        return  Long.parseLong(timeStr);
    }

    public static long getTimeFormMonth(long month) throws Exception {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMM");
        Date time= null;
        try {
            time = simpleDateFormat.parse(""+month);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return time.getTime();
    }

    public static long getWeekFormTime(long time){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyww");
        String timeStr=simpleDateFormat.format(time);
        return  Long.parseLong(timeStr);
    }


    public static long getTimeFormWeek(long week){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyww");
        Date time= null;
        try {
            time = simpleDateFormat.parse(""+week);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return time.getTime();
    }

    public static List<Integer> getWeekDays(int year, int week) {
        List<Integer> res = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year); // 2016年
        cal.set(Calendar.WEEK_OF_YEAR, week); // 设置为2016年的第10周
        cal.set(Calendar.DAY_OF_WEEK, 2); // 1表示周日，2表示周一，7表示周六
        int one = Integer.parseInt(sdf.format(cal.getTime()));
        res.add(one);
        for(int i = 1; i <= 6; i++) {
            cal.add(Calendar.DAY_OF_WEEK, 1);
            one = Integer.parseInt(sdf.format(cal.getTime()));
            res.add(one);
        }
        return res;
    }

    //由出生日期获得年龄
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }

    /**
     * 获取时间段内随机时间点
     * @param start
     * @param end
     * @return
     */
    public static Date getMiddleTime(Date start,Date end){
        Long startL = start.getTime();
        Long endL = end.getTime();
        double middleL = (long)startL + (endL-startL)*Math.random();
        return new Date((long) middleL);
    }

    /**
     * 根据指定时间周期和指定时间获取随机时间戳
     * @return
     * @throws ParseException
     */
    public static List<Long> getTimeByformat(SimpleDateFormat formatStart,SimpleDateFormat formatEnd,Date dStart, Date dEnd) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Date> dateList = findDates(dStart, dEnd);
        List<Long> res = Lists.newArrayList();
        for(Date date:dateList) {
            Date dateSystem = new Date(date.getTime());
            Date dateStart = simpleDateFormat.parse(formatStart.format(dateSystem));
            Date dateEnd = simpleDateFormat.parse(formatEnd.format(dateSystem));
            Date randomDate = getMiddleTime(dateStart,dateEnd);
            res.add(randomDate.getTime());
        }
        return res;
    }

    //获取某段时间内的所有日期
    public static List<Date> findDates(Date dStart, Date dEnd) {
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);
        List<Date> dateList = Lists.newArrayList();
        //别忘了，把起始日期加上
        dateList.add(dStart);
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(cStart.getTime());
        }
        return dateList;
    }

    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return  a.get(Calendar.DATE);
    }
    public static void main(String[] sdf){
    }

    /**
     * 获取当前系统时间最近12月的年月（含当月）
     */
    public static String[]  getLatest8Month(){
        String[] latest12Months = new String[8];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1); //要先+1,才能把本月的算进去
        for(int i=0; i<8; i++){
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月
            latest12Months[7-i] = cal.get(Calendar.YEAR)+fillZero(cal.get(Calendar.MONTH)+1);
        }
        return latest12Months;
    }

    /**
     * 获取当前系统时间最近12月的年月（含当月）
     */
    public static String[]  getLatest12Month(){
        String[] latest12Months = new String[12];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1); //要先+1,才能把本月的算进去
        for(int i=0; i<12; i++){
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月
            latest12Months[11-i] = cal.get(Calendar.YEAR)+fillZero(cal.get(Calendar.MONTH)+1);
        }
        return latest12Months;
    }
    /**
     * 格式化月份
     */
    public static String fillZero(int i){
        String month = "";
        if(i<10){
            month = "0" + i;
        }else{
            month = String.valueOf(i);
        }
        return month;
    }

    public static long getBirthDayFormTime(long time){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMdd");
        String timeStr=simpleDateFormat.format(time);
        return  Long.parseLong(timeStr);
    }

    public static long getBirthMonthFormTime(long time){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM");
        String timeStr=simpleDateFormat.format(time);
        return  Long.parseLong(timeStr);
    }

    /**
     * 取得当月天数
     * */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        return a.get(Calendar.DATE);
    }
}
