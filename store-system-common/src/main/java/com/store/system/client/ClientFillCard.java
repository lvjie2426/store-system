package com.store.system.client;

import com.store.system.exception.StoreSystemException;
import com.store.system.model.attendance.FillCard;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-18 16:24
 **/
@Data
public class ClientFillCard extends FillCard {

    private String checkName;
    private String checkCover;

    private String askName;
    private String askCover;

    private long comeTime;
    private long leaveTime;


    public  ClientFillCard(FillCard fillCard){
        try {
            BeanUtils.copyProperties(this,fillCard);
        } catch (Exception e) {
            throw  new StoreSystemException("ClientFillCard copybean error");
        }
    }
}
