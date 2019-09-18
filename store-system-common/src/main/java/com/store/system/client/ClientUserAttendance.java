package com.store.system.client;


import com.store.system.model.User;
import lombok.Data;

@Data
public class ClientUserAttendance extends ClientUser {
    public ClientUserAttendance(User user) {
        super(user);
    }

    private ClientAttendanceInfo clientAttendanceInfo;//考勤数据
}
