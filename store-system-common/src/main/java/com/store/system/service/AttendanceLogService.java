package com.store.system.service;

import com.store.system.client.ClientAttendanceInfo;
import com.store.system.client.ClientAttendanceLog;
import com.store.system.model.attendance.AttendanceItem;
import com.store.system.model.attendance.AttendanceLog;
import com.store.system.model.attendance.AttendanceTemplate;

import java.util.List;
import java.util.Map;

public interface AttendanceLogService {
	/**
	 * 打卡
	 * 插入一条考勤记录
	 * @param uid
	 * @param time 毫秒级时间戳
	 * @param img
	 * @throws Exception
	 */
	public ClientAttendanceLog insertAttendanceLog(long uid, long psid, long sid, long time, String img, int punchCardType, long card,
									String snCode,int no,String wifeNumber, String wifeName, String punchCardPlace,String mapData,String callName) throws Exception;

	/**
	 * 获取用户一天的考勤数据
	 * @param uid
	 * @param day 日期(20180801)
	 * @return
	 * @throws Exception
	 */
	public ClientAttendanceLog loadAttendanceLog(long uid, long day) throws Exception;

	/**
	 * 获取用户一月的考勤日志数据和统计数据
	 * @param sid
	 * @param uid
	 * @param month 月份（201808）
	 * @return
	 * @throws Exception
	 */
	public ClientAttendanceInfo getUserAttendanceByMonth(long sid, long subId, long uid, long month) throws Exception;

	public List<ClientAttendanceLog> getAllListByDay(long sid, long subId, long uid, long day, boolean status) throws Exception;

	public List<ClientAttendanceLog> getAllListByDay(long sid, long subId, long uid, List<Long> days, boolean status) throws Exception;

	public List<ClientAttendanceLog> getAllListByWeek(long sid, long subId, long uid, long day, boolean status) throws Exception;

	public List<ClientAttendanceLog> getAllListByMonth(long sid, long subId, long uid, long month, boolean status) throws Exception;

	/**
	 *
	 * 补卡
	 */
	public void fixAttendanceLog(long optUid, long uid, long day, int fixType, String reason) throws Exception;


	public boolean update(AttendanceLog attendanceLog) throws Exception;

	/**
	 * 修改为请假
	 * @param uid
	 * @param day（日期：20180801)
	 * @throws Exception
	 */
	public void updateLeave(long uid, long day, int leaveType, long startTime, long endTime) throws Exception;

	/**
	 * 手动统计用户月份考勤
	 * @param uid
	 * @param month(毫秒级时间戳)
	 * @throws Exception
	 */
	public void createUserStatisticsAttendance(long uid, long month) throws Exception ;

	/**
	 * 手动统计单位考勤
	 * @param sid
	 * @param day（日期：20180801)
	 * @throws Exception
	 */
	public void createSubordinateAttendanceStatistics(long sid, long day) throws Exception ;

	/**
	 * 根据时间段 获取一些学校的 考勤数据(汇总子统计)
	 */
	public Map<Long, ClientAttendanceInfo> getSchoolStatisticsAttendance(List<Long> sids, long startTime, long endTime) throws Exception;


	public Map<Long, ClientAttendanceInfo> getSchoolStatisticsAttendance(List<Long> sids, long month) throws Exception;

	/**
	 * 后台根据时间段 获取下属机构的所有用户的考勤统计(自行汇总统计)
	 *
	 * @param uids 用户ID
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public Map<Long,ClientAttendanceInfo> getUserStatisticsAttendance(long sid, List<Long> uids, long startTime, long endTime) throws Exception;

	public Map<Long, ClientAttendanceInfo> getUserStatisticsMonth(List<Long> subIds, long month, int type) throws Exception;

	public Map<Long, ClientAttendanceInfo> getUserStatisticsWeek(List<Long> subIds, long week, int type) throws Exception;

	public Map<Long, ClientAttendanceInfo> getUserStatisticsDay(List<Long> subIds, long day, int type) throws Exception;

	public Map<Long, ClientAttendanceInfo> getUserStatisticsDay(List<Long> subIds, List<Long> days, int type) throws Exception;

	/**
	 * 根据时间段 获取用户的 考勤日志合计（包含自行统计结果，考勤日志）
	 *
	 * @param uid 用户ID
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public ClientAttendanceInfo getUserAttendanceLogs(String timeTitle, long uid, long startTime, long endTime) throws Exception;

	/**
	 * 根据时间 获取单位所有用户的 考勤日志合计（包含自行统计结果，考勤日志）
	 *
	 * 比如一个页面 上图是统计 下面是每条日志
	 * @return
	 * @throws Exception
	 */
	public ClientAttendanceInfo getAllListBySubordinateDay(long sid, long day) throws Exception ;

	public AttendanceItem getAttendanceItem(long day, AttendanceTemplate attendanceTemplate) throws Exception;


	List<ClientAttendanceLog> getUserAttendanceBuByMonth(long psid, long sid, long id, long month) throws Exception;
}
