package com.store.system.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constant {

    public static final String defaultCharset = "utf-8";

    public static final long defaultPassportId = 1;

    public static final int pay_type_wallet = 1;

    public static ExecutorService sync_executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //异步执行器

}
