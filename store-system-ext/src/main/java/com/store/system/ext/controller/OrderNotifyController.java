package com.store.system.ext.controller;


import com.quakoo.webframework.BaseController;
import com.store.system.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/ordernotify")
public class OrderNotifyController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(OrderNotifyController.class);

    private static String wx_notify_success = "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
    private static String wx_notify_fail = "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";

    @Resource
    private OrderService orderService;

    @RequestMapping("/ali")
    @ResponseBody
    public String orderNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> params = this.getParams(request);
        logger.info("======== " + params.toString());
        boolean result = orderService.aliOrderNotify(params);
        if (result)
            return "success";
        else
            return "fail";
    }

    @RequestMapping("/wx")
    @ResponseBody
    public String wx(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean result = orderService.wxOrderNotify(body);

        logger.info("%%%%%%%%%%%%%%%%%%%"+request);
        if (result)
            return wx_notify_success;
        else
            return wx_notify_fail;
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter
                .hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

}
