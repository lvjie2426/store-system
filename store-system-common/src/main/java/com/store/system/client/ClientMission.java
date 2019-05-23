package com.store.system.client;

import com.store.system.model.Mission;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
@Data
public class ClientMission extends Mission{

    private int allProgress;//任务总完成度

    private int allAmount;//任务总量

    public ClientMission(Mission mission) {
        try {
            BeanUtils.copyProperties(this, mission  );
        } catch (Exception e) {
            throw new IllegalStateException("ClientMission construction error!");
        }
    }
}
