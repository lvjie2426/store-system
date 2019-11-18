package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.json.JsonUtils;
import com.quakoo.baseFramework.redis.JedisX;
import com.store.system.client.ClientAttendanceInfo;
import com.store.system.client.ClientAttendanceLog;
import com.store.system.dao.*;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.model.attendance.*;
import com.store.system.service.AttendanceLogService;
import com.store.system.service.HolidayService;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserService;
import com.store.system.util.TimeUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 节假日弄到后台输入所有节假日（包含周六日）
 *
 */
@Service
public class AttendanceLogServiceImpl implements AttendanceLogService {


	@Resource
	private UserService userService;

	@Resource
	private SubordinateService subordinateService;

	@Resource
	private AttendanceLogDao attendanceLogDao;

	@Resource
	private AttendanceTemplateDao attendanceTemplateDao;

	@Resource
	private PunchCardDao punchCardDao;

	@Resource
	private HolidayInfoDao holidayInfoDao;

	@Resource
	private SubordinateAttendanceStatisticsDao subordinateAttendanceStatisticsDao;


	@Resource
	private UserAttendanceStatisticsDao userAttendanceStatisticsDao;


	@Resource
	private HolidayService holidayService;

	@Resource
	private SubSettingsDao subSettingsDao;

	@Resource
	JdbcTemplate jdbcTemplate;

	@Autowired
	private SubordinateDao subordinateDao;


	@Autowired(required = true)
	@Qualifier("cachePool")
	protected JedisX cache;


	private AttendanceLog loadOrCreateLog(User user, long time)throws Exception{
		long uid=user.getId();
		long aid=user.getAid();
		AttendanceLog attendanceLog=new AttendanceLog();
		attendanceLog.setDay(TimeUtils.getDayFormTime(time));
		attendanceLog.setUid(uid);
		attendanceLog.setSid(user.getPsid());
		attendanceLog.setSubId(user.getSid());
		AttendanceLog dblog=attendanceLogDao.load(attendanceLog);
		if(dblog!=null){return dblog;}
		//考勤如果记录没有 ，那么需要获取他的模板。
		AttendanceTemplate attendanceTemplate=null;
		if(aid>0){
			attendanceTemplate=attendanceTemplateDao.load(aid);
			AttendanceItem attendanceItem=getAttendanceItem(TimeUtils.getDayFormTime(time),attendanceTemplate);
			attendanceLog.setStart(attendanceItem.getStart());
			attendanceLog.setEnd(attendanceItem.getEnd());
			attendanceLog.setFlexTime(attendanceTemplate.getFlextime());
		}
		attendanceLog.setMonth(TimeUtils.getMonthFormTime(time));
		attendanceLog.setWeek(TimeUtils.getWeekFormTime(time));
		attendanceLog= attendanceLogDao.insert(attendanceLog);
		createUserStatisticsAttendance(uid,time);
		return attendanceLog;
	}


/*	@Override
	public void insertAttendanceLog(long uid, long time, String img) throws Exception {
		User user = userService.load(uid);
		AttendanceLog dblog = loadOrCreateLog(user, time);
		if (dblog.getStartTime() > 0) {
			dblog.setEndImg(img);
			dblog.setEndTime(time);
			//Todo 推送打卡消息通知
*//*			String content ="您在" +format.format(time) +"成功打卡！";
			PushUtils.sendPush(uid,"考勤打卡",content,null);
			Notice notice = new Notice();
			notice.setTitle("考勤打卡");
			notice.setType(Notice.type_system);
			notice.setInfo(content);
			notice.setUid(uid);
			noticeDao.insert(notice);*//*
		} else {
			dblog.setStartTime(time);
			dblog.setStartImg(img);
			//Todo 推送打卡消息通知
		}
		attendanceLogDao.update(dblog);
		createUserStatisticsAttendance(uid, time);
	}*/

	@Override
	public ClientAttendanceLog insertAttendanceLog(long uid, long psid, long sid, long time, String img, int punchCardType, long card,
									String snCode, int no, String wifeNumber, String wifeName, String punchCardPlace, String mapData, String callName) throws Exception {
		AttendanceLog attendanceLog = new AttendanceLog();
		attendanceLog.setDay(TimeUtils.getDayFormTime(time));
		attendanceLog.setUid(uid);
		attendanceLog.setSid(psid);
		attendanceLog.setSubId(sid);
		long aid = 0;//考勤模板ID
		User user = null;
		user = userService.load(uid);
		if (null != user) {
			aid = user.getAid();
		}
		AttendanceLog dblog = attendanceLogDao.load(attendanceLog);
		if (dblog == null) {
			//考勤如果记录没有 ，那么需要获取他的模板。
			AttendanceTemplate attendanceTemplate = null;
			if (aid > 0) {
				attendanceTemplate = attendanceTemplateDao.load(aid);
				AttendanceItem attendanceItem = getAttendanceItem(TimeUtils.getDayFormTime(time), attendanceTemplate);
				attendanceLog.setStart(attendanceItem.getStart());
				attendanceLog.setEnd(attendanceItem.getEnd());
				attendanceLog.setFlexTime(attendanceTemplate.getFlextime());
			}
			attendanceLog.setMonth(TimeUtils.getMonthFormTime(time));
			attendanceLog.setWeek(TimeUtils.getWeekFormTime(time));
			dblog = attendanceLogDao.insert(attendanceLog);
		}

		PunchCardLog punchCardLog = punchCardLog(dblog, uid, time, img, punchCardType, card, snCode, no,
				wifeNumber, wifeName, punchCardPlace, mapData, callName);
		ClientAttendanceLog res = new ClientAttendanceLog(dblog);
		return res;

	}

	private PunchCardLog punchCardLog(AttendanceLog dblog, long uid, long time, String img, int punchCardType, long card,
							  String snCode, int no, String wifeNumber, String wifeName, String punchCardPlace, String mapData, String callName) throws Exception {
		if (dblog.getStartTime() > 0) {
			dblog.setEndImg(img);
			dblog.setEndTime(time);
		} else {
			dblog.setStartTime(time);
			dblog.setStartImg(img);
		}
		//存储打卡log记录
		PunchCardLog punchCardLog = new PunchCardLog();
		punchCardLog.setUid(uid);
		punchCardLog.setDay(TimeUtils.getDayFormTime(time));
		punchCardLog.setCard(card);
		punchCardLog.setPunchCardTime(time);
		punchCardLog.setPunchCardImg(img);
		punchCardLog.setPunchCardType(punchCardType);
		punchCardLog.setStatus(0);
		punchCardLog.setSnCode(snCode);
		punchCardLog.setNo(no);
		punchCardLog.setPunchCardPlace(punchCardPlace);
		punchCardLog.setMapData(mapData);
		punchCardLog.setWifeName(wifeName);
		punchCardLog.setWifeNumber(wifeNumber);
		punchCardLog.setCallName(callName);
		punchCardLog = punchCardDao.insert(punchCardLog);
		attendanceLogDao.update(dblog);
		return punchCardLog;
	}


	/**
	 * 获取一条数据
	 */
	public ClientAttendanceLog loadAttendanceLog(long uid, long day) throws Exception{
		User user = userService.load(uid);
		return transformClient(loadOrCreateLog(user,TimeUtils.getTimeFormDay(day)));
	}

	@Override
	public ClientAttendanceInfo getUserAttendanceByMonth(long sid, long subId, long uid, long month) throws Exception {
		List<AttendanceLog> logs = attendanceLogDao.getAllListByUserMonth(sid, subId, uid, month);
		ClientAttendanceInfo clientAttendanceInfo = new ClientAttendanceInfo();
		List<ClientAttendanceLog> clientAttendanceLogs = Lists.newArrayList();
		for(AttendanceLog attendanceLog : logs){
			ClientAttendanceLog clientAttendanceLog = transformClient(attendanceLog);
			clientAttendanceLogs.add(clientAttendanceLog);
		}
		clientAttendanceInfo.setAttendanceLogs(clientAttendanceLogs);
		UserAttendanceStatistics statistics = new UserAttendanceStatistics();
		statistics.setUid(uid);
		statistics.setMonth(month);
		statistics = userAttendanceStatisticsDao.load(statistics);
		if (statistics != null){
			clientAttendanceInfo.setAbsentNum(statistics.getAbsentNum());
			clientAttendanceInfo.setAttendanceNum(statistics.getAttendanceNum());
			clientAttendanceInfo.setAttendanceRate(String.valueOf(statistics.getAttendanceRate()));
			clientAttendanceInfo.setLateNum(statistics.getLateNum());
			clientAttendanceInfo.setLeaveEarlyNum(statistics.getLeaveEarlyNum());
			clientAttendanceInfo.setLeaveNum(statistics.getLeaveNum());
			clientAttendanceInfo.setNoCardNum(statistics.getNoCardNum());
			clientAttendanceInfo.setTotalNum(statistics.getTotalNum());
			clientAttendanceInfo.setUnsetNum(statistics.getUnsetNum());
			clientAttendanceInfo.setWeiPaibanNum(statistics.getWeiPaibanNum());
		}
		return clientAttendanceInfo;
	}

	@Override
	public List<ClientAttendanceLog> getAllListByDay(long sid, long subId, long uid, long day, boolean status) throws Exception {
		List<AttendanceLog> logs = attendanceLogDao.getAllListByUserDay(sid, subId, uid, day);
		List<ClientAttendanceLog> res = Lists.newArrayList();
		for (AttendanceLog log : logs) {
			ClientAttendanceLog client = transformClient(log);
			if(status) {
				if (client.getDayType()==ClientAttendanceLog.attendanceType_normal &&
						client.getStartType() == ClientAttendanceLog.attendanceType_normal) {
					res.add(client);
				}
			}else{
				if (client.getDayType()==ClientAttendanceLog.attendanceType_normal &&
						client.getStartType() == ClientAttendanceLog.attendanceType_late) {
					res.add(client);
				}
			}
		}
		return res;
	}

	@Override
	public List<ClientAttendanceLog> getAllListByDay(long sid, long subId, long uid, List<Long> days, boolean status) throws Exception {
		List<AttendanceLog> logs = Lists.newArrayList();
		for(Long day:days) {
			List<AttendanceLog> attendanceLogs = attendanceLogDao.getAllListByUserDay(sid, subId, uid, day);
			logs.addAll(attendanceLogs);
		}
		List<ClientAttendanceLog> res = Lists.newArrayList();
		for (AttendanceLog log : logs) {
			ClientAttendanceLog client = transformClient(log);
			if(status) {
				if (client.getDayType()==ClientAttendanceLog.attendanceType_normal &&
						client.getStartType() == ClientAttendanceLog.attendanceType_normal) {
					res.add(client);
				}
			}else{
				if (client.getDayType()==ClientAttendanceLog.attendanceType_normal &&
						client.getStartType() == ClientAttendanceLog.attendanceType_late) {
					res.add(client);
				}
			}
		}
		return res;
	}

	@Override
	public List<ClientAttendanceLog> getAllListByWeek(long sid, long subId, long uid, long week, boolean status) throws Exception {
		List<AttendanceLog> logs = attendanceLogDao.getAllListByUserWeek(sid, subId, uid, week);
		List<ClientAttendanceLog> res = Lists.newArrayList();
		for (AttendanceLog log : logs) {
			ClientAttendanceLog client = transformClient(log);
			if(status) {
				if (client.getDayType()==ClientAttendanceLog.attendanceType_normal &&
						client.getStartType() == ClientAttendanceLog.attendanceType_normal) {
					res.add(client);
				}
			}else{
				if (client.getDayType()==ClientAttendanceLog.attendanceType_late &&
						client.getStartType() == ClientAttendanceLog.attendanceType_late) {
					res.add(client);
				}
			}
		}
		return res;
	}

	@Override
	public List<ClientAttendanceLog> getAllListByMonth(long sid, long subId, long uid, long month, boolean status) throws Exception {
		List<AttendanceLog> logs = attendanceLogDao.getAllListByUserMonth(sid, subId, uid, month);
		List<ClientAttendanceLog> res = Lists.newArrayList();
		for (AttendanceLog log : logs) {
			ClientAttendanceLog client = transformClient(log);
			if(status) {
				if (client.getDayType()==ClientAttendanceLog.attendanceType_normal ||
						client.getStartType() == ClientAttendanceLog.attendanceType_normal) {
					res.add(client);
				}
			}else{
				if (client.getDayType()==ClientAttendanceLog.attendanceType_late ||
						client.getStartType() == ClientAttendanceLog.attendanceType_late) {
					res.add(client);
				}
			}
		}
		return res;
	}
	/**
	 *
	 * 补卡一条考勤记录
	 */
	public void fixAttendanceLog(long optUid,long uid,int type,long day,int fixType,String reason) throws Exception{
		long time=TimeUtils.getTimeFormDay(day);
		User user=userService.load(uid);
		AttendanceLog dblog=loadOrCreateLog(user,time);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date startDate=sdf.parse(""+day);
		Date endDate=sdf.parse(""+day);
		Map<String,String> orgInfoMap=new HashMap<>();
		orgInfoMap.put("optUid",""+optUid);
		orgInfoMap.put("startTime",""+dblog.getStartTime());
		orgInfoMap.put("endTime",""+dblog.getEndTime());
		dblog.setOrgInfo(JsonUtils.format(orgInfoMap));
		startDate.setMinutes(startDate.getMinutes()+dblog.getStart());
		endDate.setMinutes(endDate.getMinutes()+dblog.getEnd());

		if(fixType==AttendanceLog.fixType_all){
			dblog.setStartTime(startDate.getTime());
			dblog.setEndTime(endDate.getTime());
		}

		if(fixType==AttendanceLog.fixType_in){
			dblog.setStartTime(startDate.getTime());
		}
		if(fixType==AttendanceLog.fixType_out){
			dblog.setEndTime(endDate.getTime());
		}
		attendanceLogDao.update(dblog);

		createSubordinateAttendanceStatistics(user.getSid(),time);
		createUserStatisticsAttendance(uid,time);

	}

	public boolean update(AttendanceLog attendanceLog) throws Exception{
		AttendanceLog dbInfo = attendanceLogDao.load(attendanceLog);
		if(dbInfo != null){
			if(attendanceLog.getStartTime()>0){
				dbInfo.setStartTime(attendanceLog.getStartTime());
			}
			if(attendanceLog.getEndTime()>0){
				dbInfo.setEndTime(attendanceLog.getEndTime());
			}
		}
		return attendanceLogDao.update(dbInfo);
	}


	/**
	 *
	 * 修改为请假
	 */
	public void updateLeave(long uid,long day, int leaveType, long startTime, long endTime) throws Exception{
		long time=TimeUtils.getTimeFormDay(day);
		User user=userService.load(uid);
		AttendanceLog dblog=loadOrCreateLog(user,time);
		dblog.setLeave(AttendanceLog.leave_yes);

		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date startDate=sdf.parse(""+day);
		Date endDate=sdf.parse(""+day);
		Map<String,String> orgInfoMap=new HashMap<>();
		orgInfoMap.put("leaveType",String.valueOf(leaveType));
		orgInfoMap.put("startTime",""+startTime);
		orgInfoMap.put("endTime",""+endTime);
		dblog.setOrgInfo(JsonUtils.format(orgInfoMap));
		startDate.setMinutes(startDate.getMinutes()+dblog.getStart());
		endDate.setMinutes(endDate.getMinutes()+dblog.getEnd());

		createSubordinateAttendanceStatistics(user.getSid(),day);
		createUserStatisticsAttendance(uid,time);
		attendanceLogDao.update(dblog);
	}


	/**
	 * 根据时间段 获取一些学校的 考勤数据
	 */
	@Override
	public Map<Long, ClientAttendanceInfo> getSchoolStatisticsAttendance(List<Long> sids, long startTime, long endTime) throws Exception {
		long startDay= TimeUtils.getDayFormTime(startTime);
		long endDay= TimeUtils.getDayFormTime(endTime);
		List<SubordinateAttendanceStatistics> list=new ArrayList<>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
		for(long day=startDay;day<=endDay;day++){
			for(long sid:sids) {
				SubordinateAttendanceStatistics subordinateAttendanceStatistics = new SubordinateAttendanceStatistics();
				subordinateAttendanceStatistics.setSid(sid);
				subordinateAttendanceStatistics.setDay(day);
				subordinateAttendanceStatistics.setWeek(TimeUtils.getWeekFormTime(TimeUtils.getTimeFormDay(day)));
				subordinateAttendanceStatistics.setMonth(Long.parseLong(sdf.format(TimeUtils.getTimeFormDay(day))));
				list.add(subordinateAttendanceStatistics);
			}
		}
		list=subordinateAttendanceStatisticsDao.load(list);
		return calculateStatistics(list);
	}

	@Override
	public Map<Long, ClientAttendanceInfo> getSchoolStatisticsAttendance(List<Long> sids, long month) throws Exception {
		List<SubordinateAttendanceStatistics> list=new ArrayList<>();
		for(long sid:sids) {
			List<SubordinateAttendanceStatistics> statistics = subordinateAttendanceStatisticsDao.getAllList(sid,month);
			list.addAll(statistics);
		}
		return calculateStatistics(list);
	}

	private Map<Long,ClientAttendanceInfo> calculateStatistics(List<SubordinateAttendanceStatistics> list){
		Map<Long,ClientAttendanceInfo> result=new HashMap<>();
		if(list != null) {
			for (SubordinateAttendanceStatistics csa : list) {
				long schoolId = csa.getSid();
				ClientAttendanceInfo cai = result.get(schoolId);
				if (cai == null) {
					cai = new ClientAttendanceInfo();
					result.put(schoolId, cai);
				}
				Subordinate subordinate = subordinateDao.load(schoolId);
				cai.setName(subordinate.getName());
				cai.setAbsentNum(csa.getAbsentNum() + cai.getAbsentNum());
				cai.setAttendanceNum(csa.getAttendanceNum() + cai.getAttendanceNum());
				cai.setLateNum(csa.getLateNum() + cai.getLateNum());
				cai.setLeaveEarlyNum(csa.getLeaveEarlyNum() + cai.getLeaveEarlyNum());
				cai.setNoCardNum(csa.getNoCardNum() + cai.getNoCardNum());
				cai.setLeaveNum(csa.getLeaveNum() + cai.getLeaveNum());
				cai.setTotalNum(csa.getTotalNum() + cai.getTotalNum());
				cai.setUnsetNum(csa.getUnsetNum() + cai.getUnsetNum());
				cai.setWeiPaibanNum(csa.getWeiPaibanNum() + cai.getWeiPaibanNum());
				if (cai.getTotalNum() > 0) {
					cai.setAttendanceRate(new DecimalFormat("#.00").format(cai.getAttendanceNum() / cai.getTotalNum()));
				}
			}
		}
		return result;
	}

	/**
	 * 后台根据时间段 获取下属机构的所有用户的考勤统计(自行汇总统计)
	 */
	public Map<Long,ClientAttendanceInfo> getUserStatisticsAttendance(long sid, List<Long> uids, long startTime, long endTime) throws Exception{
		List<AttendanceLog> logs=new ArrayList<>();
		long startDay= TimeUtils.getDayFormTime(startTime);
		long endDay= TimeUtils.getDayFormTime(endTime);
		for(long uid:uids) {
			for(long day=startDay;day<=endDay;day++) {
				AttendanceLog attendanceLog = new AttendanceLog();
				attendanceLog.setSid(sid);
				attendanceLog.setUid(uid);
				attendanceLog.setDay(day);
				logs.add(attendanceLog);
			}
		}
		logs=attendanceLogDao.load(logs);
		return transformLogs(logs,-1);
	}

	public Map<Long,ClientAttendanceInfo> getUserStatisticsMonth(List<Long> subIds, long month, int type) throws Exception{
		List<AttendanceLog> logs=new ArrayList<>();
		for(Long subId:subIds) {
			List<AttendanceLog> attendanceLogs = attendanceLogDao.getAllListBySubMonth(subId,month);
			logs.addAll(attendanceLogs);
		}
		return transformLogs(logs,type);
	}

	public Map<Long,ClientAttendanceInfo> getUserStatisticsWeek(List<Long> subIds, long week, int type) throws Exception{
		List<AttendanceLog> logs=new ArrayList<>();
		for(Long subId:subIds) {
			List<AttendanceLog> attendanceLogs = attendanceLogDao.getAllListBySubWeek(subId,week);
			logs.addAll(attendanceLogs);
		}
		return transformLogs(logs,type);
	}

	public Map<Long,ClientAttendanceInfo> getUserStatisticsDay(List<Long> subIds, long day, int type) throws Exception{
		List<AttendanceLog> logs=new ArrayList<>();
		for(Long subId:subIds) {
			List<AttendanceLog> attendanceLogs = attendanceLogDao.getAllListBySubDay(subId,day);
			logs.addAll(attendanceLogs);
		}
		return transformLogs(logs,type);
	}

	public Map<Long,ClientAttendanceInfo> getUserStatisticsDay(List<Long> subIds, List<Long> days, int type) throws Exception{
		List<AttendanceLog> logs=new ArrayList<>();
		for(Long subId:subIds) {
			for(Long day:days) {
				List<AttendanceLog> attendanceLogs = attendanceLogDao.getAllListBySubDay(subId, day);
				logs.addAll(attendanceLogs);
			}
		}
		return transformLogs(logs,type);
	}

	private Map<Long,ClientAttendanceInfo> transformLogs(List<AttendanceLog> logs, int type) throws Exception{
		Map<Long,List<AttendanceLog>> userAttendanceLog=new HashMap<>();
		for(AttendanceLog attendanceLog:logs){
			long uid=attendanceLog.getUid();
			List<AttendanceLog> attendanceLogs=userAttendanceLog.get(uid);
			if(attendanceLogs==null){
				attendanceLogs=new ArrayList<>();
				userAttendanceLog.put(uid,attendanceLogs);
			}
			attendanceLogs.add(attendanceLog);
		}
		Map<Long,ClientAttendanceInfo> result=new HashMap<>();
		for(long uid:userAttendanceLog.keySet()){
			User clientUser=new User();
			clientUser.setId(uid);
			result.put(uid,createClientAttendanceInfo("",userAttendanceLog.get(uid), Lists.newArrayList(clientUser), type));
		}
		return result;
	}

	public ClientAttendanceInfo getUserAttendanceLogs(String timeTitle,long uid, long startTime, long endTime) throws Exception{
		User user=userService.load(uid);
		List<AttendanceLog> logs=new ArrayList<>();
		long startDay= TimeUtils.getDayFormTime(startTime);
		long endDay= TimeUtils.getDayFormTime(endTime);
		for(long day=startDay;day<=endDay;day++) {
			AttendanceLog attendanceLog = new AttendanceLog();
			attendanceLog.setSid(user.getSid());
			attendanceLog.setUid(uid);
			attendanceLog.setDay(day);
			logs.add(attendanceLog);
		}
		logs=attendanceLogDao.load(logs);
		List<User> users=new ArrayList<>();
		users.add(user);
		ClientAttendanceInfo clientAttendanceInfo=createClientAttendanceInfo(timeTitle,logs,users,-1);
		return clientAttendanceInfo;
	}


	//统计用户月份考勤(每次添加修改的时候 就统计一下当前月的)
	public void createUserStatisticsAttendance(long uid, long time) throws Exception {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date(time));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		long startTime=calendar.getTime().getTime();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		long endTime=calendar.getTime().getTime();
		ClientAttendanceInfo clientAttendanceInfo=getUserAttendanceLogs("",uid,startTime,endTime);

		UserAttendanceStatistics userAttendanceStatistics=new UserAttendanceStatistics();
		userAttendanceStatistics.setUid(uid);
		userAttendanceStatistics.setMonth(TimeUtils.getMonthFormTime(time));
		UserAttendanceStatistics dbInfo= userAttendanceStatisticsDao.load(userAttendanceStatistics);
		boolean update=true;
		if(dbInfo==null){
			update=false;
			dbInfo=userAttendanceStatistics;
		}
		dbInfo.setAbsentNum(clientAttendanceInfo.getAbsentNum());
		dbInfo.setAttendanceNum(clientAttendanceInfo.getAttendanceNum());
		dbInfo.setAttendanceRate(Double.parseDouble(clientAttendanceInfo.getAttendanceRate()));
		dbInfo.setLateNum(clientAttendanceInfo.getLateNum());
		dbInfo.setLeaveEarlyNum(clientAttendanceInfo.getLeaveEarlyNum());
		dbInfo.setLeaveNum(clientAttendanceInfo.getLeaveNum());
		dbInfo.setNoCardNum(clientAttendanceInfo.getNoCardNum());
		dbInfo.setTotalNum(clientAttendanceInfo.getTotalNum());
		dbInfo.setUnsetNum(clientAttendanceInfo.getUnsetNum());
		dbInfo.setWeiPaibanNum(clientAttendanceInfo.getWeiPaibanNum());
		if(update){
			userAttendanceStatisticsDao.update(dbInfo);
		}else{
			userAttendanceStatisticsDao.insert(dbInfo);
		}
	}


	@Override
	public ClientAttendanceInfo getAllListBySubordinateDay(long sid, long day) throws Exception {
		List<AttendanceLog> attendanceLogs=attendanceLogDao.getAllListBySubDay(sid, day);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String timeTitle=sdf.format(new Date(TimeUtils.getTimeFormDay(day)));
		ClientAttendanceInfo clientAttendanceInfo=null;
		List<User> users=userService.getAllUserBySid(sid);
		clientAttendanceInfo=createClientAttendanceInfo(timeTitle,attendanceLogs,users,-1);
		return clientAttendanceInfo;
	}


	//统计单位考勤（每次修改就统计一下，否则定时任务统计）
	public void createSubordinateAttendanceStatistics(long sid, long day) throws Exception {
		ClientAttendanceInfo clientAttendanceInfo= getAllListBySubordinateDay(sid, day);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
		SubordinateAttendanceStatistics subordinateAttendanceStatistics=new SubordinateAttendanceStatistics();
		subordinateAttendanceStatistics.setSid(sid);
		subordinateAttendanceStatistics.setDay(TimeUtils.getDayFormTime(day));
		subordinateAttendanceStatistics.setWeek(TimeUtils.getWeekFormTime(day));
		subordinateAttendanceStatistics.setMonth(Long.parseLong(sdf.format(day)));
		SubordinateAttendanceStatistics dbInfo= subordinateAttendanceStatisticsDao.load(subordinateAttendanceStatistics);
		boolean update=true;
		if(dbInfo==null){
			update=false;
			dbInfo=subordinateAttendanceStatistics;
		}
		dbInfo.setAbsentNum(clientAttendanceInfo.getAbsentNum());
		dbInfo.setAttendanceNum(clientAttendanceInfo.getAttendanceNum());
		dbInfo.setAttendanceRate(Double.parseDouble(clientAttendanceInfo.getAttendanceRate()));
		dbInfo.setLateNum(clientAttendanceInfo.getLateNum());
		dbInfo.setLeaveEarlyNum(clientAttendanceInfo.getLeaveEarlyNum());
		dbInfo.setLeaveNum(clientAttendanceInfo.getLeaveNum());
		dbInfo.setNoCardNum(clientAttendanceInfo.getNoCardNum());
		dbInfo.setTotalNum(clientAttendanceInfo.getTotalNum());
		dbInfo.setUnsetNum(clientAttendanceInfo.getUnsetNum());
		dbInfo.setWeiPaibanNum(clientAttendanceInfo.getWeiPaibanNum());
		if(update){
			subordinateAttendanceStatisticsDao.update(dbInfo);
		}else{
			subordinateAttendanceStatisticsDao.insert(dbInfo);
		}
	}



	/**
	 * 获取当天的考勤要求，早晚的时间点
	 * @param day
	 * @param attendanceTemplate
	 * @return
	 * @throws Exception
	 */
	@Override
	public AttendanceItem getAttendanceItem(long day,AttendanceTemplate attendanceTemplate)throws Exception{
		AttendanceItem item=new AttendanceItem();//默认的时间点都是-1
		//特殊的上班时间，特殊的放假时间
		Map<Long,SpecialDay> specialMap=attendanceTemplate.getSpecial();
		SpecialDay thisSpecialDay=specialMap.get(day);
		//轮换制度
		if(attendanceTemplate.getType()==AttendanceTemplate.type_turn){
			//判断今天是否要上班
			if(attendanceTemplate.getHolidayType()==AttendanceTemplate.holidayType_normal){
					//特殊时间点是否要上班
				if(thisSpecialDay!=null&&!thisSpecialDay.isWork()){
					return item;
					//如果是法定的。//判断今天是否要上班
				} else if(thisSpecialDay==null&&holidayService.getAllHoliday().contains(day)){
					return item;
				}else {
					//取出轮班表（跟日期对应上）(轮换的时候去掉节假日)
//					List<AttendanceItem> items = Lists.newArrayList();
//					for(Map.Entry<Long,AttendanceItem> entry:attendanceTemplate.getTurnMap().entrySet()){
//						items.add(entry.getValue());
//					}
					List<AttendanceItem> items = attendanceTemplate.getTurn();
					int workDay = 0;
					//当前时间 和轮换开始时间 其中的假期日 特殊的时间
					for(long oneDay=attendanceTemplate.getTurnStartDay();oneDay<=day;oneDay++){
						//循环 开始的天到现在的每一天，
						//如果这天需要上班(如果是特殊的先走特殊的，如果不是特殊的走普通的)
						SpecialDay specialDay=specialMap.get(oneDay);
						if(specialDay!=null){
							if(specialDay.isWork()){
								workDay=workDay+1;
							}
						}else if(!holidayService.getAllHoliday().contains(oneDay)){
							workDay=workDay+1;
						}
					}
					int s = (int) (workDay % items.size());
					item = items.get(s);
					return item;
				}
			}else{
				Date date=new Date(TimeUtils.getTimeFormDay(day));
				int weekDay=date.getDay();
				if(weekDay==0){weekDay=7;}
				//特殊时间点是否要上班
				if(thisSpecialDay!=null&&!thisSpecialDay.isWork()){
					return item;
					//判断今天是否要上班
				} else if(thisSpecialDay==null&&!attendanceTemplate.getWorkWeekDay().contains(weekDay)){
					return item;
				}else{
					//取出轮班表（跟日期对应上）(轮换的时候去掉节假日)
//					List<AttendanceItem> items = Lists.newArrayList();
//					for(Map.Entry<Long,AttendanceItem> entry:attendanceTemplate.getTurnMap().entrySet()){
//						items.add(entry.getValue());
//					}
					List<AttendanceItem> items = attendanceTemplate.getTurn();
					int workDay = 0;
					//当前时间 和轮换开始时间 其中的假期日 特殊的时间
					for(long oneDay=attendanceTemplate.getTurnStartDay();oneDay<=day;oneDay++){
						//循环 开始的天到现在的每一天，
						//如果这天需要上班(如果是特殊的先走特殊的，如果不是特殊的走普通的)
						SpecialDay specialDay=specialMap.get(oneDay);
						if(specialDay!=null){
							if(specialDay.isWork()){
								workDay=workDay+1;
							}
						}else if(attendanceTemplate.getWorkWeekDay().contains(TimeUtils.getWeekDayFormDay(oneDay))){
							workDay=workDay+1;
						}
					}
					int s = (int) (workDay % items.size());
					item = items.get(s);
					return item;
				}
			}
		} else {
			//固定制定
			//判断今天是否要上班
			//判断今天是否要上班
			if(attendanceTemplate.getHolidayType()==AttendanceTemplate.holidayType_normal){
				//特殊时间点是否要上班
				if(thisSpecialDay!=null&&!thisSpecialDay.isWork()){
					return item;
					//如果是法定的。//判断今天是否要上班
				} else if(thisSpecialDay==null&&holidayService.getAllHoliday().contains(day)){
					return item;
				}else {
					item.setStart(attendanceTemplate.getStart());
					item.setEnd(attendanceTemplate.getEnd());
					return item;
				}
			}else{
				Date date=new Date(TimeUtils.getTimeFormDay(day));
				int weekDay=date.getDay();
				if(weekDay==0){weekDay=7;}
				//特殊时间点是否要上班
				if(thisSpecialDay!=null&&!thisSpecialDay.isWork()){
					return item;
					//判断今天是否要上班
				} else if(thisSpecialDay==null&&!attendanceTemplate.getWorkWeekDay().contains(weekDay)){
					return item;
				}else{
					item.setStart(attendanceTemplate.getStart());
					item.setEnd(attendanceTemplate.getEnd());
					return item;
				}
			}
		}

	}

	@Override
	public List<ClientAttendanceLog> getUserAttendanceBuByMonth(long psid, long sid, long id, long month) throws Exception {
		List<AttendanceLog> logs = attendanceLogDao.getAllListByUserMonth(psid, sid, id, month);
		List<ClientAttendanceLog> list=new ArrayList<>(logs.size());
		List<ClientAttendanceLog> returnlist=new ArrayList<>(logs.size());
		for(AttendanceLog attendanceLog:logs){
			ClientAttendanceLog clientAttendanceLog=transformClient(attendanceLog);
			list.add(clientAttendanceLog);
		}

		for(ClientAttendanceLog clientAttendanceLog:list){
			if(clientAttendanceLog.getDayType()==ClientAttendanceLog.attendanceType_absent||clientAttendanceLog.getDayType()==ClientAttendanceLog.attendanceType_noCard){
				returnlist.add(clientAttendanceLog);
			}
		}
		return  returnlist;
	}


	/**
	 * 根据 考勤日志 生成统计数据
	 * @param timeTitle
	 * @param attendanceLogs
	 * @param users
	 * @return
	 * @throws Exception
	 */
	private ClientAttendanceInfo createClientAttendanceInfo(String timeTitle,List<AttendanceLog> attendanceLogs,List<User> users,int type) throws Exception {
		int late=0;//迟到
		int leaveEarly=0;//早退
		int absent=0;//旷工
		int leave=0;//请假
		int noCard=0;//缺卡
		int weiPaiban=0;//未排班
		int unset=0;//未设置
		int attendanceNum=0;//出勤人数
		int totalNum=0;//总人数
		int normalNum=0;//正常人数（学生不缺就算正常，教师迟到就算不正常）


		Map<Long,List<AttendanceLog>> map=new HashMap<>();
		for(AttendanceLog attendanceLog:attendanceLogs){
			List<AttendanceLog> list=map.get(attendanceLog.getUid());
			if(list==null){
				list=new ArrayList<>();
				map.put(attendanceLog.getUid(),list);
			}
			if(type>=0) {
				ClientAttendanceLog client = transformClient(attendanceLog);
				if (client.getDayType() == type ||
						client.getStartType() == type) {
					list.add(client);
				}
			}else {
				list.add(attendanceLog);
			}
		}

		List<ClientAttendanceLog> clientAttendanceLogs=new ArrayList<>();
		 {
			for(User user:users){
				List<AttendanceLog> attendanceLogUserList=map.get(user.getId());
				if(attendanceLogUserList==null){continue;}
				for(AttendanceLog attendanceLog:attendanceLogUserList) {
					ClientAttendanceLog clientAttendanceLog = transformClient(attendanceLog);
					clientAttendanceLog.setName(user.getName());
					clientAttendanceLogs.add(clientAttendanceLog);
					boolean normal = true;
					boolean attendance = true;
					//缺卡
					if (clientAttendanceLog.getDayType() == ClientAttendanceLog.attendanceType_noCard) {
						noCard = noCard + 1;
					}
					//旷工
					if (clientAttendanceLog.getDayType() == ClientAttendanceLog.attendanceType_absent) {
						absent = absent + 1;
						normal = false;
						attendance = false;

					}
					//未排班
					if (clientAttendanceLog.getDayType() == ClientAttendanceLog.attendanceType_weiPaiban) {
						weiPaiban = weiPaiban + 1;
					}
					//未设置
					if (clientAttendanceLog.getDayType() == ClientAttendanceLog.attendanceType_unSet) {
						unset = unset + 1;
					}
					//迟到
					if (clientAttendanceLog.getStartType() == ClientAttendanceLog.attendanceType_late) {
						late = late + 1;
					}
					//早退
					if (clientAttendanceLog.getEndType() == ClientAttendanceLog.attendanceType_leaveEarly) {
						leaveEarly = leaveEarly + 1;
					}
					//请假
					if (clientAttendanceLog.getDayType() == ClientAttendanceLog.attendanceType_leave) {
						leave = leave + 1;
						attendance = false;
					}
					if (normal) {
						normalNum = normalNum + 1;
					}
					if (attendance) {
						attendanceNum = attendanceNum + 1;
					}
					totalNum=totalNum+1;
				}

			}

		}
		ClientAttendanceInfo clientAttendanceInfo=new ClientAttendanceInfo();
		clientAttendanceInfo.setAbsentNum(absent);
		clientAttendanceInfo.setLateNum(late);
		clientAttendanceInfo.setLeaveEarlyNum(leaveEarly);
		clientAttendanceInfo.setLeaveNum(leave);
		clientAttendanceInfo.setNoCardNum(noCard);
		clientAttendanceInfo.setUnsetNum(unset);
		clientAttendanceInfo.setAttendanceNum(attendanceNum);
		clientAttendanceInfo.setWeiPaibanNum(weiPaiban);
		clientAttendanceInfo.setTotalNum(totalNum);
		DecimalFormat df = new DecimalFormat("0.00");
		if(totalNum>0) {
			clientAttendanceInfo.setAttendanceRate(df.format((double)normalNum / (double)totalNum));
		}
		clientAttendanceInfo.setAttendanceLogs(clientAttendanceLogs);
		clientAttendanceInfo.setTime(timeTitle);
		if(users.size()>0) {
			User user = userService.load(users.get(0).getId());
			if(user != null) {
				clientAttendanceInfo.setName(user.getName());
				clientAttendanceInfo.setIcon(user.getIcon());
			}
		}
		return clientAttendanceInfo;
	}


	private ClientAttendanceLog transformClient(AttendanceLog attendanceLog) throws Exception{
		ClientAttendanceLog clientAttendanceLog=new ClientAttendanceLog(attendanceLog);

		if(attendanceLog!=null){
			BeanUtils.copyProperties(clientAttendanceLog,attendanceLog);
		}
		if(attendanceLog==null){
			clientAttendanceLog.setDayType(ClientAttendanceLog.attendanceType_unSet);
			return clientAttendanceLog;
		}

		boolean late=false;//迟到
		boolean leaveEarly=false;//早退
		boolean absent=false;//旷工
		boolean leave=false;//请假
		boolean noCard=false;//缺卡
		boolean attendanceRate=false;//出勤率
		boolean weiPaiban=false;//未排班



		if(attendanceLog.getStart()<0&&attendanceLog.getEnd()<0){
			weiPaiban=true;
		}else {
			int cardRecodNum=0;
			if(attendanceLog.getStartTime()>0){
				cardRecodNum=cardRecodNum+1;
			}
			if(attendanceLog.getEndTime()>0){
				cardRecodNum=cardRecodNum+1;
			}
			if(cardRecodNum==1){
				noCard=true;
			}
			if(cardRecodNum==0){
				if(attendanceLog.getLeave()==AttendanceLog.leave_yes){
					leave=true;
				}else {
					absent = true;
				}
			}

			SubSettings subSettings = subSettingsDao.load(attendanceLog.getSubId());
			if(subSettings==null||subSettings.getHumanizedStatus()==SubSettings.status_off) {
				int useFlexTime = 0;//弹性时间（用过的）
				if (attendanceLog.getStartTime() > 0) {
					Date date = new Date(attendanceLog.getStartTime());
					int mins = date.getHours() * 60 + date.getMinutes();
					if (mins > (attendanceLog.getStart() + attendanceLog.getFlexTime())) {
						late = true;
						clientAttendanceLog.setStartType(ClientAttendanceLog.attendanceType_late);
					}
					if (mins > attendanceLog.getStart() && !late) {
						useFlexTime = mins - attendanceLog.getStart();
					}
				} else {
					clientAttendanceLog.setStartType(ClientAttendanceLog.attendanceType_noCard);
				}

				if (attendanceLog.getEndTime() > 0) {
					Date date = new Date(attendanceLog.getEndTime());
					int mins = date.getHours() * 60 + date.getMinutes();
					if (mins < (attendanceLog.getEnd() + useFlexTime)) {
						leaveEarly = true;
						clientAttendanceLog.setEndType(ClientAttendanceLog.attendanceType_leaveEarly);
					}
				} else {
					clientAttendanceLog.setEndType(ClientAttendanceLog.attendanceType_noCard);
				}
			}else if(subSettings.getHumanizedStatus()==SubSettings.status_on){
				//根据管理端人性化设置计算迟到、早退
				if (attendanceLog.getStartTime() > 0) {
					Date date = new Date(attendanceLog.getStartTime());
					int mins = date.getHours() * 60 + date.getMinutes();
					if (mins > (attendanceLog.getStart() + subSettings.getLateTime()*60)) {
						late = true;
						clientAttendanceLog.setStartType(ClientAttendanceLog.attendanceType_late);
					}
				} else {
					clientAttendanceLog.setStartType(ClientAttendanceLog.attendanceType_noCard);
				}

				if (attendanceLog.getEndTime() > 0) {
					Date date = new Date(attendanceLog.getEndTime());
					int mins = date.getHours() * 60 + date.getMinutes();
					if (mins < (attendanceLog.getEnd() - subSettings.getEarlyTime()*60)) {
						leaveEarly = true;
						clientAttendanceLog.setEndType(ClientAttendanceLog.attendanceType_leaveEarly);
					}
				} else {
					clientAttendanceLog.setEndType(ClientAttendanceLog.attendanceType_noCard);
				}
			}

		}


		if(late){
			clientAttendanceLog.setDayType(ClientAttendanceLog.attendanceType_late);
		}
		if(leaveEarly){
			clientAttendanceLog.setDayType(ClientAttendanceLog.attendanceType_leaveEarly);
		}
		if(absent){
			clientAttendanceLog.setDayType(ClientAttendanceLog.attendanceType_absent);
		}
		if(leave){
			clientAttendanceLog.setDayType(ClientAttendanceLog.attendanceType_leave);
		}
		if(noCard){
			clientAttendanceLog.setDayType(ClientAttendanceLog.attendanceType_noCard);
		}
		if(weiPaiban){
			clientAttendanceLog.setDayType(ClientAttendanceLog.attendanceType_weiPaiban);

		}
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

			clientAttendanceLog.setDayStr(sdf.format(new Date(TimeUtils.getTimeFormDay(attendanceLog.getDay()))));
			clientAttendanceLog.setStartStr(sdfTime.format(new Date(clientAttendanceLog.getStartTime())));
			clientAttendanceLog.setEndStr(sdfTime.format(new Date(clientAttendanceLog.getEndTime())));
			int startHour = clientAttendanceLog.getStart()/60;
			int startMinute = clientAttendanceLog.getStart()%60;
			int endHour = clientAttendanceLog.getEnd()/60;
			int endMinute = clientAttendanceLog.getEnd()%60;
			clientAttendanceLog.setUpStr(startHour+":"+(startMinute==0?String.valueOf("00"):startMinute));
			clientAttendanceLog.setDownStr(endHour+":"+(endMinute==0?String.valueOf("00"):endMinute));
			User user = userService.load(attendanceLog.getUid());
			if(user!=null) {
				clientAttendanceLog.setName(user.getName());
			}

			List<PunchCardLog> punchCardLogs = punchCardDao.getAllList(attendanceLog.getUid(),attendanceLog.getDay());
			clientAttendanceLog.setCardLogs(punchCardLogs);
		}
		return clientAttendanceLog;

	}




}
