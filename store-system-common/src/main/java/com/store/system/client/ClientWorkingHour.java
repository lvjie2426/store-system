package com.store.system.client;

import com.store.system.bean.SimpleUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ClientWorkingHour
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/18 16:27
 * @Version 1.0
 **/
@Data
public class ClientWorkingHour implements Serializable{

    private SimpleUser user;

    private double hours;

    private int days;
}
