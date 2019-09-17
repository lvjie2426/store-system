package com.store.system.service;

import com.store.system.client.ClientAttendanceTemplate;
import com.store.system.model.attendance.AttendanceTemplate;

import java.util.List;

public interface AttendanceTemplateService {

	public List<ClientAttendanceTemplate> getAllList(long sid) throws Exception;

	public AttendanceTemplate add(AttendanceTemplate attendanceTemplate) throws Exception;

	public boolean update(AttendanceTemplate attendanceTemplate) throws Exception;

	public boolean delete(long aid) throws Exception;

	public void set(long aid, List<Long> buid) throws Exception;

}
