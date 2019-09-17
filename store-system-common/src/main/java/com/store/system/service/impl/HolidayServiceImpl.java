package com.store.system.service.impl;

import com.store.system.dao.HolidayInfoDao;
import com.store.system.model.attendance.HolidayInfo;
import com.store.system.service.HolidayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class HolidayServiceImpl implements HolidayService {

	
	@Resource
	HolidayInfoDao holidayInfoDao;

	
	@Override
	public List<String> getCacheByType(String type) {
		return null;
	}


	@Override
	public HolidayInfo load(Object id) throws Exception {
		return holidayInfoDao.load(id);
	}

	@Override
	public List<HolidayInfo> load(List objs) throws Exception {
		return holidayInfoDao.load(objs);
	}

	@Override
	public HolidayInfo insert(HolidayInfo model) throws Exception {
		model= holidayInfoDao.insert(model);
		return model;
	}

	@Override
	public boolean update(HolidayInfo model) throws Exception {
		boolean result= holidayInfoDao.update(model);
		return result;
	}

	@Override
	public boolean delete(Object id) throws Exception {
		return holidayInfoDao.delete(id);
	}


	@Override
	public List<HolidayInfo> getAll() throws Exception {
		return holidayInfoDao.getAll();
	}

	static List<Long> holidaySet;
	static long holidayMapRefreshTime;//刷新时间


	public List<Long> getAllHoliday() throws Exception {
		if (holidayMapRefreshTime == 0 ||
				(System.currentTimeMillis() - holidayMapRefreshTime > 600000)) {
			holidayMapRefreshTime = System.currentTimeMillis();
			List<HolidayInfo> holidayInfos=holidayInfoDao.getAll();
			List<Long> tholidaySet = new ArrayList<>();
			for (HolidayInfo holidayInfo:holidayInfos) {
				tholidaySet.add(holidayInfo.getDay());
			}
			holidaySet = tholidaySet;
		}
		return holidaySet;
	}
	/**
	 * 添加节日
	 */
	public void addAllHolidayInfo(long day)throws Exception{
		HolidayInfo holidayInfo=new HolidayInfo();
		holidayInfo.setDay(day);
		holidayInfoDao.insert(holidayInfo);
	}
	/**
	 * 删除节日
	 */
	public void deleteAllHolidayInfo(long day)throws Exception{
		HolidayInfo holidayInfo=new HolidayInfo();
		holidayInfo.setDay(day);
		holidayInfoDao.delete(holidayInfo);
	}
}
