package com.store.system.service.impl;

import com.quakoo.baseFramework.redis.JedisX;
import com.store.system.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CodeServiceImpl implements CodeService {

    @Autowired(required = true)
    @Qualifier("cachePool")
    protected JedisX cache;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    private static SimpleDateFormat dateFormat_2 = new SimpleDateFormat("yyMMddHHmm");


    private final static String spu_code_incr_key = "beite_spu_code_incr_%s";
    private final static String sku_code_incr_key = "beite_sku_code_incr_%s";
    private final static String recharge_code_incr_key = "recharge_code_incr_%s";

    @Override
    public String getSpuCode() throws Exception {
        Date date = new Date();
        String prefix = dateFormat.format(date);
        String key = String.format(spu_code_incr_key, prefix);
        long index = cache.incr(key);
        cache.expire(key, 60 * 60 * 24);
        String suffix = String.format("%07d", index);
        return prefix + suffix;
    }

    @Override
    public String getSkuCode() throws Exception {
        Date date = new Date();
        String prefix = "S" + dateFormat.format(date);
        String key = String.format(sku_code_incr_key, prefix);
        long index = cache.incr(key);
        cache.expire(key, 60 * 60 * 24);
        String suffix = String.format("%09d", index);
        return prefix + suffix;
    }

    @Override
    public String getOrderCode() throws Exception {
        Date date = new Date();
        String prefix = dateFormat_2.format(date);
        String key = String.format(sku_code_incr_key, prefix);
        long index = cache.incr(key);
        cache.expire(key, 60 * 60);
        String suffix = String.format("%06d", index);
        return prefix + suffix;
    }

    @Override
    public String getRechargeCode() throws Exception {
        Date date = new Date();
        String prefix = dateFormat_2.format(date);
        String key = String.format(recharge_code_incr_key, prefix);
        long index = cache.incr(key);
        cache.expire(key, 60 * 60);
        String suffix = String.format("%06d", index);
        return prefix + suffix;
    }

    @Override
    public String getT_orderCode() throws Exception {
        Date date = new Date();
        String prefix = "T" + dateFormat_2.format(date);
        String key = String.format(sku_code_incr_key, prefix);
        long index = cache.incr(key);
        cache.expire(key, 60 * 60);
        String suffix = String.format("%05d", index);
        return prefix + suffix;
    }

    @Override
    public String getB_orderCode() throws Exception {
        Date date = new Date();
        String prefix = "B" + dateFormat_2.format(date);
        String key = String.format(sku_code_incr_key, prefix);
        long index = cache.incr(key);
        cache.expire(key, 60 * 60);
        String suffix = String.format("%06d", index);
        return prefix + suffix;
    }

    @Override
    public String getJ_orderCode() throws Exception {
        Date date = new Date();
        String prefix = "J" + dateFormat_2.format(date);
        String key = String.format(sku_code_incr_key, prefix);
        long index = cache.incr(key);
        cache.expire(key, 60 * 60);
        String suffix = String.format("%06d", index);
        return prefix + suffix;
    }
}
