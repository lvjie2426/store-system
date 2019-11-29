package com.store.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.store.system.client.ClientSubordinate;
import com.store.system.dao.SubSettingsDao;
import com.store.system.model.Subordinate;
import com.store.system.model.attendance.PunchCardPlace;
import com.store.system.model.attendance.SubSettings;
import com.store.system.model.attendance.WirelessNetwork;
import com.store.system.service.SubSettingsService;
import com.store.system.service.SubordinateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    @Resource
    private SubordinateService subordinateService;


    @Override
    public void update(SubSettings subSettings, String placeJson, String netJson) throws Exception {
        List<PunchCardPlace> placeList = Lists.newArrayList();
        if (StringUtils.isNotBlank(placeJson)) {
            if(placeJson.contains("-1")){
                placeList = Lists.newArrayList();
            }else {
                placeList = JsonUtils.fromJson(placeJson, new TypeReference<List<PunchCardPlace>>() {
                });
            }
        }
        subSettings.setPunchCardPlaces(placeList);
        List<WirelessNetwork> netList = Lists.newArrayList();
        if (StringUtils.isNotBlank(netJson) && !netJson.contains("-1")) {
            if(netJson.contains("-1")){
                netList = Lists.newArrayList();
            }else {
                netList = JsonUtils.fromJson(netJson, new TypeReference<List<WirelessNetwork>>() {
                });
            }
        }
        subSettings.setWirelessNetworks(netList);

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
            } else if (StringUtils.isNotBlank(placeJson) && netJson.contains("-1")) {
                dbInfo.setPunchCardPlaces(Lists.newArrayList());
            }
            if (subSettings.getWirelessNetworks().size() > 0) {
                dbInfo.setWirelessNetworks(subSettings.getWirelessNetworks());
            } else if (StringUtils.isNotBlank(netJson) && netJson.contains("-1")) {
                dbInfo.setWirelessNetworks(Lists.newArrayList());
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

    @Override
    public SubSettings add(SubSettings subSettings) throws Exception {
        SubSettings insert = subSettingsDao.insert(subSettings);
        return insert;
    }

    @Override
    public Boolean updateStatus(long subid, int type) throws Exception {
        SubSettings load = subSettingsDao.load(subid);
        if(type== SubSettings.status_off){
            load.setHumanizedStatus(SubSettings.status_off);
        }else{
            load.setHumanizedStatus(SubSettings.status_on);
        }
        return subSettingsDao.update(load);
    }

    @Override
    public List<SubSettings> getAllList(long subId,long psid) throws Exception {
        List<SubSettings> list=new ArrayList<>();
        if(subId>0){
            list.add(subSettingsDao.load(subId));
            return list;
        }else{
            List<ClientSubordinate> twoLevelAllList = subordinateService.getTwoLevelAllList(psid);
           List<Long> ids= twoLevelAllList.stream().map(ClientSubordinate::getId).collect(Collectors.toList());
           return subSettingsDao.load(ids);
        }
    }
}
