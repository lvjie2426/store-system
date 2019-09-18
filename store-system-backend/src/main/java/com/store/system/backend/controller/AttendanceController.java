/*
package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.webframework.BaseController;
import com.xiuqiang.client.ResultClient;
import com.xiuqiang.client.attendance.ClientAttendanceInfo;
import com.xiuqiang.client.attendance.ClientAttendanceLog;
import com.xiuqiang.client.attendance.ClientAttendanceTemplate;
import com.xiuqiang.model.attendance.AttendanceTemplate;
import com.xiuqiang.service.attendance.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

*/
/**
 *
 *//*

@Controller
@RequestMapping("/attendance")
public class AttendanceController extends BaseController {

    @Autowired
    private AttendanceService attendanceService;

    @RequestMapping("/getStatisticsBySchool")
    public ModelAndView getStaticBySchool(HttpServletRequest request, HttpServletResponse response,
                                          Pager pager,
                                          @RequestParam(required = false, value = "type",defaultValue = "0") int type,
                                          @RequestParam(required = false, value = "schoolId",defaultValue = "0") long schoolId,
                                          @RequestParam(required = false, value = "startTime",defaultValue = "0") long startTime,
                                          @RequestParam(required = false, value = "endTime",defaultValue = "0") long endTime,
                                          Model model) throws Exception {
        Map<Long,ClientAttendanceInfo> map= attendanceService.getSchoolStatisticsAttendance(Lists.newArrayList(schoolId),startTime,endTime,type);
        ClientAttendanceInfo clientAttendanceInfo=map.get(schoolId);
        if(clientAttendanceInfo==null){
            clientAttendanceInfo=new ClientAttendanceInfo();
        }
        return this.viewNegotiating(request,response,clientAttendanceInfo);
    }


    @RequestMapping("/searchLog")
    public ModelAndView searchLog(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(required = false, value = "type",defaultValue = "0") int type,
                                  @RequestParam(required = false, value = "sid",defaultValue = "0") long sid,
                                  @RequestParam(required = false, value = "uid",defaultValue = "0") long uid,
                                  @RequestParam(required = false, value = "startTime",defaultValue = "0") long startTime,
                                  @RequestParam(required = false, value = "endTime",defaultValue = "0") long endTime,
                                  Model model) throws Exception {
        ClientAttendanceInfo clientAttendanceInfo= attendanceService.getUserAttendanceLogs(sid,uid,startTime,endTime,type);
        return this.viewNegotiating(request,response,new ResultClient(clientAttendanceInfo.getAttendanceLogs()));
    }

    */
/**
     * 获取一条考勤记录数据
     * @param request
     * @param response
     * @param sid
     * @param uid
     * @param type
     * @param day
     * @param model
     * @return
     * @throws Exception
     *//*

    @RequestMapping("/get")
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response,
                            long sid, long uid, int type, long day,
                            Model model) throws Exception {
        ClientAttendanceLog clientAttendanceLog = attendanceService.loadAttendanceLog(sid,uid,type,day);
        ResultClient resultClient=new ResultClient(true,clientAttendanceLog);
        return this.viewNegotiating(request,response,resultClient);
    }

    */
/**
     * 添加考勤模板
     * @param request
     * @param response
     * @param attendanceTemplate
     * @param model
     * @return
     * @throws Exception
     *//*

    @RequestMapping("/addAttendanceTemplate")
    public ModelAndView addAttendanceTemplate(HttpServletRequest request, HttpServletResponse response,
                                              AttendanceTemplate attendanceTemplate, Model model) throws Exception {
        AttendanceTemplate template = attendanceService.addAttendanceTemplate(attendanceTemplate);
        return this.viewNegotiating(request,response,new ResultClient(true,template));
    }

    */
/**
     * 获取考勤模板列表
     * @param request
     * @param response
     * @param sid
     * @param model
     * @return
     * @throws Exception
     *//*

    @RequestMapping("/getAttendanceTemplates")
    public ModelAndView getAttendanceTemplates(HttpServletRequest request, HttpServletResponse response,
                                               long sid, Model model) throws Exception {
        List<ClientAttendanceTemplate> attendanceTemplates= attendanceService.getAllAttendanceTemplate(sid);
        return this.viewNegotiating(request,response,new ResultClient(attendanceTemplates));
    }

    */
/**
     * 删除考勤模板
     * @param request
     * @param response
     * @param id
     * @param model
     * @return
     * @throws Exception
     *//*

    @RequestMapping("/deleteAttendanceTemplates")
    public ModelAndView deleteAttendanceTemplates(HttpServletRequest request, HttpServletResponse response,
                                                  long id, Model model) throws Exception {
        boolean b = attendanceService.deleteAttendanceTemplate(id);
        return this.viewNegotiating(request,response,new ResultClient(b));
    }

    */
/**
     * 修改考勤模板
     * @param request
     * @param response
     * @param attendanceTemplate
     * @param model
     * @return
     * @throws Exception
     *//*

    @RequestMapping("/updateAttendanceTemplates")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response,
                               AttendanceTemplate attendanceTemplate,
                               Model model) throws Exception {
        boolean b = attendanceService.updateAttendanceTemplate(attendanceTemplate);
        return this.viewNegotiating(request,response,new ResultClient(b));
    }

    */
/**
     * 获取考勤模板
     * @param request
     * @param response
     * @param aid
     * @param model
     * @return
     * @throws Exception
     *//*

    @RequestMapping("/getAttendanceTemplate")
    public ModelAndView getAttendanceTemplate(HttpServletRequest request, HttpServletResponse response,
                                              long aid, Model model) throws Exception {
        AttendanceTemplate attendanceTemplate = attendanceService.getAttendanceTemplate(aid);
        return this.viewNegotiating(request,response,new ResultClient(attendanceTemplate));
    }

    */
/**
     * 分配考勤模板给教师
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     *//*

    @RequestMapping("/addTemplatesToTearcher")
    public ModelAndView addTemplatesToTearcher(HttpServletRequest request, HttpServletResponse response,
                                               @RequestParam(required = false,value = "uids[]") List<Long> uids, long aid,
                                               Model model) throws Exception {
        attendanceService.setBackendUserAttendanceTemplate(aid,uids);
        return this.viewNegotiating(request,response,new ResultClient(true));
    }

    */
/**
     * 分配考勤模板给园所
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     *//*

    @RequestMapping("/addTemplatesToSchool")
    public ModelAndView addTemplatesToSchool(HttpServletRequest request, HttpServletResponse response,
                                             long sid, long aid, Model model) throws Exception {
        attendanceService.setSchoolAttendanceTemplate(aid,sid);
        return this.viewNegotiating(request,response,new ResultClient(true));
    }

    */
/**
     * 获取所有的节日
     *//*

    @RequestMapping("/getAllHoliday")
    public ModelAndView getAllHoliday(HttpServletRequest request, HttpServletResponse response,
                                      Model model) throws Exception {
        List<Long> result= attendanceService.getAllHoliday();
        return this.viewNegotiating(request,response,new ResultClient(result));
    }
    */
/**
     * 添加节日
     *//*

    @RequestMapping("/addAllHolidayInfo")
    public ModelAndView addAllHolidayInfo(HttpServletRequest request, HttpServletResponse response,
                                          long day, Model model) throws Exception {
        attendanceService.addAllHolidayInfo(day);
        return this.viewNegotiating(request,response,new ResultClient());
    }
    */
/**
     * 删除节日
     *//*

    @RequestMapping("/deleteAllHolidayInfo")
    public ModelAndView deleteAllHolidayInfo(HttpServletRequest request, HttpServletResponse response,
                                             long day, Model model) throws Exception {
        attendanceService.deleteAllHolidayInfo(day);
        return this.viewNegotiating(request,response,new ResultClient());
    }


}
*/
