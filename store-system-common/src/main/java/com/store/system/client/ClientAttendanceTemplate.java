package com.store.system.client;

import com.store.system.bean.SimpleUser;
import com.store.system.model.attendance.AttendanceTemplate;
import lombok.Data;

@Data
public class ClientAttendanceTemplate extends AttendanceTemplate {

    private SimpleUser creater;

}