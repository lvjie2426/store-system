package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ClientWorkOverTime;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.model.attendance.WorkOverTime;
import com.store.system.service.SubordinateService;
import com.store.system.service.WorkOverTimeService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/workovertime")
public class WorkOverTimeController extends BaseController {

    @Resource
    private WorkOverTimeService workOverTimeService;

    @Resource
    private SubordinateService subordinateService;

    /**
     * 获取个人加班历史记录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getListByUser")
    public ModelAndView getListByUser(HttpServletRequest request, HttpServletResponse response, Pager pager) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            pager = workOverTimeService.getListByUid(user.getId(), pager);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 申请加班
     *
     * @param request
     * @param response
     * @param workOverTime
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, WorkOverTime workOverTime) throws Exception {
        try {
            WorkOverTime res = workOverTimeService.add(workOverTime);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 加班详情
     *
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/load")
    public ModelAndView load(HttpServletRequest request, HttpServletResponse response,
                             long id) throws Exception {
        try {
            ClientWorkOverTime res = workOverTimeService.load(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 审核加班通过
     *
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/pass")
    public ModelAndView check(HttpServletRequest request, HttpServletResponse response,
                              long id) throws Exception {
        try {
            boolean flag = workOverTimeService.pass(id);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 审核加班bu通过
     *
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/nopass")
    public ModelAndView nopass(HttpServletRequest request, HttpServletResponse response,
                               long id, String reason) throws Exception {
        try {
            boolean flag = workOverTimeService.nopass(id, reason);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 获取 加班列表  pc
     *
     * sid psid
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getList")
    public ModelAndView getList(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "subid",defaultValue = "0") long subid,
                                @RequestParam(value = "sid") long sid,
                                @RequestParam(value = "userName",defaultValue = "") String userName,
                                @RequestParam(value = "startTime",defaultValue = "0") long startTime,
                                @RequestParam(value = "endTime",defaultValue = "0") long endTime,
                                Pager pager) throws Exception {
        try {
            pager = workOverTimeService.getList(subid,sid,userName,startTime,endTime, pager);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 批量审核加班通过
     *
     * @param request
     * @param response
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/passMore")
    public ModelAndView passMore(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "ids[]") List<Long> ids) throws Exception {
        try {
            boolean flag = workOverTimeService.passMore(ids);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
