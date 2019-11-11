package com.store.system.service.impl;

import com.store.system.dao.SubSettingsDao;
import com.store.system.model.attendance.SubSettings;
import com.store.system.service.SubSettingsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName SubSettingsServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/18 18:27
 * @Version 1.0
 **/
@Service
public class SubSettingsServiceImpl implements SubSettingsService {

    @Resource
    private SubSettingsDao subSettingsDao;


    @Override
    public void update(SubSettings subSettings) throws Exception {
        SubSettings dbInfo = subSettingsDao.load(subSettings.getSubId());
        if (dbInfo == null) {
            subSettings.setSubId(subSettings.getSubId());
            subSettingsDao.insert(subSettings);
        } else {
            if (subSettings.getHumanizedStatus() > 0) {
                dbInfo.setHumanizedStatus(subSettings.getHumanizedStatus());
            }
            if (subSettings.getSignTime() > 0) {
                dbInfo.setSignTime(subSettings.getSignTime());
            } else if (subSettings.getSignTime() == -1) {
                dbInfo.setSignTime(0);
            }
            if (subSettings.getLateTime() > 0) {
                dbInfo.setLateTime(subSettings.getLateTime());
            } else if (subSettings.getLateTime() == -1) {
                dbInfo.setLateTime(0);
            }
            if (subSettings.getEarlyTime() > 0) {
                dbInfo.setEarlyTime(subSettings.getEarlyTime());
            } else if (subSettings.getEarlyTime() == -1) {
                dbInfo.setEarlyTime(0);
            }
            if (subSettings.getBeforeTime() > 0) {
                dbInfo.setBeforeTime(subSettings.getBeforeTime());
            } else if (subSettings.getBeforeTime() == -1) {
                dbInfo.setBeforeTime(0);
            }
            if (subSettings.getAfterTime() > 0) {
                dbInfo.setAfterTime(subSettings.getAfterTime());
            } else if (subSettings.getAfterTime() == -1) {
                dbInfo.setAfterTime(0);
            }
            if (subSettings.getPunchCardPlaces().size() > 0) {
                dbInfo.setPunchCardPlaces(subSettings.getPunchCardPlaces());
            }
            if (subSettings.getWirelessNetworks().size() > 0) {
                dbInfo.setWirelessNetworks(subSettings.getWirelessNetworks());
            }
            if (subSettings.getPlaceStatus() > 0) {
                dbInfo.setPlaceStatus(subSettings.getPlaceStatus());
            }
            if (subSettings.getNetStatus() > 0) {
                dbInfo.setNetStatus(subSettings.getNetStatus());
            }
            subSettingsDao.update(dbInfo);
        }
    }

    @Override
    public SubSettings load(long subId) throws Exception {
        SubSettings dbInfo = subSettingsDao.load(subId);
        SubSettings subSettings = new SubSettings();
        if (dbInfo == null) {
            subSettings.setSubId(subId);
            subSettings.setHumanizedStatus(SubSettings.status_off);
            subSettings.setPlaceStatus(SubSettings.status_off);
            subSettings.setNetStatus(SubSettings.status_off);
            subSettings = subSettingsDao.insert(subSettings);
        } else {
            return dbInfo;
        }
        return subSettings;
    }
}
