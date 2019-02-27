package com.store.system.backend;

import com.store.system.service.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

    public static void main(String[] args) throws Exception{
        ApplicationContext ct=new ClassPathXmlApplicationContext("glasses-servlet.xml");
        OrderService orderService = ct.getBean(OrderService.class);
        System.out.println("==========");
        String authCode = "134651638008190285";
//        boolean sign = orderService.handleAliBarcodeOrder(1,authCode,1,
//                "test","test","test", 0.01);
//        boolean sign =  orderService.handleWxBarcodeOrder("/Users/lihao/Desktop/project/store-system/store-system-backend/src/main/resources",
//                1, authCode, 1, "test", "test", "test", 0.01, "0.0.0.0");
//        System.out.println(sign);
        System.exit(1);
    }

}
