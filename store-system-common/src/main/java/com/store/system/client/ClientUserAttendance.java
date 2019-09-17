package com.store.system.client;


import com.store.system.model.User;

public class ClientUserAttendance extends ClientUser {
    public ClientUserAttendance(User user) {
        super(user);
    }

    private ClientAttendanceInfo clientAttendanceInfo;//考勤数据

    public ClientAttendanceInfo getClientAttendanceInfo() {
        return clientAttendanceInfo;
    }

    public void setClientAttendanceInfo(ClientAttendanceInfo clientAttendanceInfo) {
        this.clientAttendanceInfo = clientAttendanceInfo;
    }
}
