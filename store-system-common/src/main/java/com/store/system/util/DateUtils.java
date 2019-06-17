package com.store.system.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.util.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils{

	private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    
    


	public static long getTruncatedMinute() {
		return truncate(new Date(), Calendar.MINUTE).getTime();
	}

	public static long getTruncatedHour() {
		return truncate(new Date(), Calendar.HOUR).getTime();
	}

	public static long getTruncatedToday() {
		return truncate(new Date(), Calendar.DATE).getTime();
	}

	public static long truncateToMinute(long time) {
		return truncate(new Date(time), Calendar.MINUTE).getTime();
	}

	public static long truncateToHour(long time) {
		return truncate(new Date(time), Calendar.HOUR).getTime();
	}

	public static long truncateToToday(long time) {
		return truncate(new Date(time), Calendar.DATE).getTime();
	}

	/**
	 * 获取指定分钟后的时间点
	 */
	public static long getFutureTimeByMinute(int minute) {
		return addMinutes(new Date(), minute).getTime();
	}
	
	public static long getFutureTimeBySecond(int second) {
		return addSeconds(new Date(), second).getTime();
	}

	public static long getFutureTimeByMinute(long time, int minute) {
		return addMinutes(new Date(time), minute).getTime();
	}

	/**
	 * 获取指定天后的时间点
	 */
	public static long getFutureTimeByDay(int day) {
		return addDays(new Date(), day).getTime();
	}

	public static long getFutureTimeByDay(long time, int day) {
		return addDays(new Date(time), day).getTime();
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String dateFormat(long date) {
		FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static String dateFormat(Date date, String pattern) {
		FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
		return dateFormat.format(date);
	}

	public static String dateFormat(long date, String pattern) {
		FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
		return dateFormat.format(date);
	}
	
	public static String fromToday2(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        long time = date / 1000;
        long now = System.currentTimeMillis() / 1000;
        long ago = now - time;
        if (ago <= ONE_MINUTE) {
        	if(0 == ago) ago = 1;
        	return ago + "秒前";
        }
        else if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY) {
        	long minute = ago % ONE_HOUR / ONE_MINUTE;
        	if(minute == 0) {
        		return ago / ONE_HOUR + "小时前";
        	} else {
        		return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
                        + "分钟前";
        	}
        }
        else{
        	long num = ago / ONE_DAY;
        	if(num < 31) {
        		return num + "天前";
        	} else {
        		return DateUtils.dateFormat(date, "yyyy-MM");
        	}
        }
    }
	
	public static String fromToday(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        long time = date / 1000;
        long now = System.currentTimeMillis() / 1000;
        long ago = now - time;
        if (ago <= ONE_MINUTE) {
        	if(0 == ago) ago = 1;
        	return ago + "秒前";
        }
        else if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY) {
        	long minute = ago % ONE_HOUR / ONE_MINUTE;
        	if(minute == 0) {
        		return ago / ONE_HOUR + "小时前";
        	} else {
        		return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
                        + "分钟前";
        	}
        }
//        else if (ago <= ONE_DAY * 2) {
//        	int minute = calendar.get(Calendar.MINUTE);
//        	if(minute == 0) {
//        		return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点";
//        	} else {
//        		return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
//                        + calendar.get(Calendar.MINUTE) + "分";
//        	}
//        }
//        else if (ago <= ONE_DAY * 3) {
//        	int minute = calendar.get(Calendar.MINUTE);
//        	if(minute == 0) { 
//        		return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点";
//        	} else {
//        		return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
//                        + calendar.get(Calendar.MINUTE) + "分";
//        	}
//        }
        else
        	return DateUtils.dateFormat(date, "yyyy年MM月dd日");
    }

	public static String calculateTimeToHMS(long ss) {  
	    Integer mi = 60;  
	    Integer hh = mi * 60;  
	    Long hour = ss / hh;  
	    Long minute = (ss - hour * hh) / mi;  
	    Long second = (ss - hour * hh - minute * mi) ;  
	    StringBuffer sb = new StringBuffer();  
	    if(hour > 0) {  
	        sb.append(hour+"小时");  
	    }  
	    if(minute > 0) {  
	        sb.append(minute+"分");  
	    }  
	    if(second > 0) {  
	        sb.append(second+"秒");  
	    }  
	    return sb.toString();  
	}  
	
	public static String calculateTimeToDHMS(long ss) {  
	    Integer mi = 60;  
	    Integer hh = mi * 60;  
	    Integer dd = hh * 24;  
	    Long day = ss / dd;  
	    Long hour = (ss - day * dd) / hh;  
	    Long minute = (ss - day * dd - hour * hh) / mi;  
	    Long second = (ss - day * dd - hour * hh - minute * mi) ;  
	    StringBuffer sb = new StringBuffer();  
	    if(day > 0) {  
	        sb.append(day+"天");  
	    }  
	    if(hour > 0) {  
	        sb.append(hour+"小时");  
	    }  
	    if(minute > 0) {  
	        sb.append(minute+"分");  
	    }  
	    if(second > 0) {  
	        sb.append(second+"秒");  
	    }  
	    return sb.toString();  
	}  
	
	public static String simpleDateFormat(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String str = sdf.format(date);
		return str;
	}
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		System.out.println(sdf.format(new Date()));
	}

	//获得当前时间
	public static int getDate()throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String day = sdf.format(calendar.getTime());
		return Integer.valueOf(day);
	}

	//获取当天（按当前传入的时区）00:00:00点钟 所对应时刻的long型值
	public static long getStartTimeOfDay(long now, String timeZone) {
		String tz = TextUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
		TimeZone curTimeZone = TimeZone.getTimeZone(tz);
		Calendar calendar = Calendar.getInstance(curTimeZone);
		calendar.setTimeInMillis(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

}
