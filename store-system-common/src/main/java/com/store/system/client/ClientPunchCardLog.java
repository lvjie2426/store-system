package com.store.system.client;


import com.store.system.model.attendance.PunchCardLog;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;


@Data
public class ClientPunchCardLog extends PunchCardLog {

    private String userName;

    public ClientPunchCardLog(PunchCardLog punchCardLog) {
        try {
            BeanUtils.copyProperties(this, punchCardLog);
        } catch (Exception e) {
            throw new IllegalStateException("ClientPunchCardLog construction error!");
        }
    }
}
