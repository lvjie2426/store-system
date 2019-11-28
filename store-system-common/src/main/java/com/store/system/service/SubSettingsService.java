package com.store.system.service;

import com.store.system.model.attendance.SubSettings;

/**
 * @ClassName SubSettingsService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/18 18:26
 * @Version 1.0
 **/
public interface SubSettingsService {

    public void update(SubSettings subSettings, String placeJson, String netJson) throws Exception;

    public SubSettings load(long subId) throws Exception;

    /**
     * pc 添加人性化设置打卡时间
     * @param subSettings
     * @return
     * @throws Exception
     */
    public SubSettings add(SubSettings subSettings)throws Exception;

    /**
     * 修改状态
     * @param subid
     * @param type
     * @return
     * @throws Exception
     */
    public  Boolean updateStatus(long subid, int type)throws Exception;
}
