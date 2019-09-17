package com.store.system.service;

import com.quakoo.space.interfaces.HService;
import com.store.system.model.attendance.HolidayInfo;

import java.util.List;

public interface HolidayService extends HService<HolidayInfo> {


	public List<HolidayInfo> getAll()throws Exception;

	/**
	 * 获取所有的节日
	 */
	public List<Long> getAllHoliday()throws Exception;
	/**
	 * 添加节日
	 */
	public void addAllHolidayInfo(long day)throws Exception;
	/**
	 * 删除节日
	 */
	public void deleteAllHolidayInfo(long day)throws Exception;




}
