package com.store.system.client;

import com.store.system.bean.SimpleUser;
import com.store.system.model.attendance.AttendanceRanking;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;


/**
 * @ClassName ClientAttendanceRanking
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/19 15:43
 * @Version 1.0
 **/
@Data
public class ClientAttendanceRanking extends AttendanceRanking {

    private SimpleUser user;

    private String subName;

    public ClientAttendanceRanking(AttendanceRanking attendanceRanking) {
        try {
            BeanUtils.copyProperties(this, attendanceRanking);
        } catch (Exception e) {
            throw new IllegalStateException("ClientAttendanceRanking construction error!");
        }
    }

}
