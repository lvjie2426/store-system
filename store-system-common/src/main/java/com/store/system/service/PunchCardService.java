package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientPunchCardLog;

public interface PunchCardService {

	public Pager getBackendLogs(Pager pager, long uid, long day) throws Exception;

	public Pager getWebLogsPager(Pager pager, long uid) throws Exception;

	/**
	 * 获取个人的一条打卡数据
	 */
	public ClientPunchCardLog loadPunchCardLog(long id) throws Exception;
}
