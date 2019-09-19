package com.store.system.service.impl;


import com.google.common.collect.Lists;
import com.store.system.bean.SimpleUser;
import com.store.system.client.ClientAttendanceTemplate;
import com.store.system.dao.AttendanceTemplateDao;
import com.store.system.model.User;
import com.store.system.model.attendance.AttendanceTemplate;
import com.store.system.service.AttendanceTemplateService;
import com.store.system.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 节假日弄到后台输入所有节假日（包含周六日）
 */
@Service
public class AttendanceTemplateServiceImpl implements AttendanceTemplateService {

    @Resource
    private UserService userService;
    @Resource
    private AttendanceTemplateDao attendanceTemplateDao;

    public static final int fixType_all = 0;
    public static final int fixType_in = 1;
    public static final int fixType_out = 2;


    @Override
    public List<ClientAttendanceTemplate> getAllList(long subId) throws Exception {
        List<AttendanceTemplate> allList = attendanceTemplateDao.getAllList(subId);
        List<Long> uids = Lists.newArrayList();
        for (AttendanceTemplate t : allList) {
            long uid = t.getUid();
            uids.add(uid);
        }
        List<User> users = userService.load(uids);
        Map<Long, User> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getId(), user);
        }

        return trans(allList, userMap);
    }

    @Override
    public AttendanceTemplate add(AttendanceTemplate attendanceTemplate) throws Exception {
        attendanceTemplate = attendanceTemplateDao.insert(attendanceTemplate);
        User user = userService.load(attendanceTemplate.getUid());
        if(user!=null) {
            user.setAid(attendanceTemplate.getId());
            userService.updateUser(user);
        }
        return attendanceTemplate;
    }

    /**
     * 修改一个考勤模板
     *
     * @param attendanceTemplate
     */
    public boolean update(AttendanceTemplate attendanceTemplate) throws Exception {
        AttendanceTemplate template = attendanceTemplateDao.load(attendanceTemplate.getUid());
        if (template != null) {
/*            template.setName(attendanceTemplate.getName());
            template.setStart(attendanceTemplate.getStart());
            template.setEnd(attendanceTemplate.getEnd());
            template.setHolidayTyep(attendanceTemplate.getHolidayTyep());
//            template.setWorkWeekDay(attendanceTemplate.getWorkWeekDay());
            template.setTurnStartDay(attendanceTemplate.getTurnStartDay());
            template.setType(attendanceTemplate.getType());*/
        }
        boolean b = attendanceTemplateDao.update(template);
        return b;
    }


    @Override
    public boolean delete(long aid) throws Exception {
        boolean b = attendanceTemplateDao.delete(aid);
        return b;
    }

    @Override
    public void set(long aid, List<Long> buids) throws Exception {
        for (long buid : buids) {
            User user = userService.load(buid);
            user.setAid(aid);
            userService.update(user, null);
        }
    }


    private List<ClientAttendanceTemplate> trans(List<AttendanceTemplate> attendanceTemplates, Map<Long, User> userMap) throws InvocationTargetException, IllegalAccessException {
        List<ClientAttendanceTemplate> result = new ArrayList<>();
        for (AttendanceTemplate attendanceTemplate : attendanceTemplates) {
            ClientAttendanceTemplate clientAttendanceTemplate = new ClientAttendanceTemplate();
            BeanUtils.copyProperties(clientAttendanceTemplate, attendanceTemplate);
            clientAttendanceTemplate.setUser(new SimpleUser(userMap.get(clientAttendanceTemplate.getUid())));
            result.add(clientAttendanceTemplate);
        }
        return result;
    }


}
