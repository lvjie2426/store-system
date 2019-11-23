package com.store.system.client;

import com.store.system.model.attendance.Leave;
import com.store.system.model.attendance.WorkOverTime;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 16:52
 **/
@Data
public class ClientLeave extends Leave {

    private String checkName;
    private String checkCover;

    private String askName;

    private String copyName;
    private String copyCover;


    public ClientLeave(Leave leave) {
        try {
            BeanUtils.copyProperties(this, leave);
        } catch (Exception e) {
            throw new IllegalStateException("ClientLeave construction error!");
        }
    }
}
