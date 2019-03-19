package com.store.system.util;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.baseFramework.http.HttpParam;
import com.quakoo.baseFramework.http.HttpResult;
import com.quakoo.baseFramework.http.HttpUtils;
import com.quakoo.baseFramework.jackson.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubmailUtils {

    private static Logger logger = LoggerFactory.getLogger(SubmailUtils.class);

    private static final int appid = 33127;
    private static final String appkey = "041b0a50467758b72f04faf0587818ce";

    private static final String multi_send_url = "https://api.mysubmail.com/message/multisend.json";
    private static final String send_url = "https://api.mysubmail.com/message/send.json";

    private static final String content_signature = "【雀科科技】";

    private static HttpParam httpParam = new HttpParam(2000, 2000, 1);

    public static void send(String phone, String content) {
        try {
            Map<String, Object> postParams = Maps.newHashMap();
            postParams.put("to", phone);
            postParams.put("content", content_signature + content);
            postParams.put("appid", appid);
            postParams.put("signature", appkey);
            HttpResult httpResult = HttpUtils.httpQuery(httpParam, send_url, null, "post",
                    null, postParams, null);
            System.out.println(httpResult.getResult());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    //@var(name)
    public static void multiSend(List<SubmailMultiItem> multiItems, String content) {
        try {
            List<List<SubmailMultiItem>> multiItemsList = Lists.partition(multiItems, 50);
            for(List<SubmailMultiItem> one : multiItemsList) {
                String multi = JsonUtils.toJson(multiItems);
                Map<String, Object> postParams = Maps.newHashMap();
                postParams.put("multi", multi);
                postParams.put("content", content_signature + content);
                postParams.put("appid", appid);
                postParams.put("signature", appkey);
                HttpResult httpResult = HttpUtils.httpQuery(httpParam, multi_send_url, null, "post",
                        null, postParams, null);
                System.out.println(httpResult.getResult());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
//        send("15011226135", "12345");
        String content = "尊敬的@var(user_name)你好,@var(sub_name)全体员工祝您周末快乐!";
        List<SubmailMultiItem> multiItems = Lists.newArrayList();
        SubmailMultiItem one = new SubmailMultiItem();
        one.setTo("15011226135");
        Map<String, String> oneVars = Maps.newHashMap();
        oneVars.put("user_name", "李昊");
        oneVars.put("sub_name", "马家堡分店");
        one.setVars(oneVars);
        multiItems.add(one);

        SubmailMultiItem two = new SubmailMultiItem();
        two.setTo("17191073809");
        Map<String, String> twoVars = Maps.newHashMap();
        twoVars.put("user_name", "Aa");
        twoVars.put("sub_name", "马家堡分店");
        two.setVars(twoVars);
        multiItems.add(two);

        content = "aaaa";
//        multiSend(multiItems, content);
        Pattern pattern = Pattern.compile("@var\\([a-zA-Z_]+\\)");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) {
            System.out.println(matcher.group());
        }
    }


}
