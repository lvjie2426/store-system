package com.store.system.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constant {

    public static final String defaultCharset = "utf-8";

    public static final long defaultPassportId = 1;

    public static final int pay_type_wallet = 1;

    public static ExecutorService sync_executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //异步执行器

    public static final int type_today = 1; //今日
    public static final int type_yesterday = 2; //昨日
    public static final int type_week = 3; //本周
    public static final int type_month = 4; //本月
    public static final int type_halfYear = 5; //近半年
    public static final int type_search = 6; //按时间查询
}
