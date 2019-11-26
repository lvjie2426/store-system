package com.store.system.client;

import com.store.system.model.attendance.ChangeShift;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;


/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 16:59
 **/
@Data
public class ClientChangeShift extends ChangeShift {

    private String copyName;
    private String copyCover;

    private String replaceName;
    private String replaceCover;

    private String checkName;
    private String checkCover;

    private String askName;
    private String askCover;


    public ClientChangeShift(ChangeShift changeShift){
        try {
            BeanUtils.copyProperties(this, changeShift);
        } catch (Exception e) {
            throw new IllegalStateException("ChangeShift construction error!");
        }
    }
}
