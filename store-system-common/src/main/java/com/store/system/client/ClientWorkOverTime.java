package com.store.system.client;

import com.store.system.model.User;
import com.store.system.model.attendance.WorkOverTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientWorkOverTime extends WorkOverTime{

    private String checkName; //审核人名称

    private String copyName; //抄送人名称


    public ClientWorkOverTime(WorkOverTime workOverTime) {
        try {
            BeanUtils.copyProperties(this, workOverTime);
        } catch (Exception e) {
            throw new IllegalStateException("WorkOverTime construction error!");
        }
    }


}