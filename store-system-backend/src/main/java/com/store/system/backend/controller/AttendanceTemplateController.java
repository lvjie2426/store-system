package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientAttendanceTemplate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.attendance.AttendanceTemplate;
import com.store.system.service.AttendanceTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName AttendanceTemplateController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/8/13 16:03
 * @Version 1.0
 **/
@Controller
@RequestMapping("/attendanceTemplate")
public class AttendanceTemplateController extends BaseController {

    @Autowired
    private AttendanceTemplateService attendanceTemplateService;

    /**
     * 添加考勤模板
     * @param request
     * @param response
     * @param attendanceTemplate
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView addAttendanceTemplate(HttpServletRequest request, HttpServletResponse response,
                                              AttendanceTemplate attendanceTemplate, Model model) throws Exception {
        try {
            AttendanceTemplate template = attendanceTemplateService.add(attendanceTemplate);
            return this.viewNegotiating(request, response, new ResultClient(true, template));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 获取考勤模板列表
     * @param request
     * @param response
     * @param sid
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAllList")
    public ModelAndView getAttendanceTemplates(HttpServletRequest request, HttpServletResponse response,
                                               long sid, Model model) throws Exception {
        List<ClientAttendanceTemplate> attendanceTemplates= attendanceTemplateService.getAllList(sid);
        return this.viewNegotiating(request,response,new ResultClient(attendanceTemplates));
    }

    /**
     * 删除考勤模板
     * @param request
     * @param response
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public ModelAndView deleteAttendanceTemplates(HttpServletRequest request, HttpServletResponse response,
                                                  long id, Model model) throws Exception {
        boolean b = attendanceTemplateService.delete(id);
        return this.viewNegotiating(request,response,new ResultClient(b));
    }

    /**
     * 修改考勤模板
     * @param request
     * @param response
     * @param attendanceTemplate
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response,
                               AttendanceTemplate attendanceTemplate,
                               Model model) throws Exception {
        boolean b = attendanceTemplateService.update(attendanceTemplate);
        return this.viewNegotiating(request,response,new ResultClient(b));
    }

}
