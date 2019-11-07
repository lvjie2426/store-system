package com.store.system.web.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientLeave;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.Leave;
import com.store.system.model.attendance.WorkOverTime;
import com.store.system.service.LeaveService;
import com.store.system.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 15:04
 **/

@Controller
@RequestMapping("leave")
public class LeaveController extends BaseController {

    @Autowired
    private LeaveService leaveService;

    /**
     * 获取个人请假历史记录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getListByUid")
    public ModelAndView getListByUid(HttpServletRequest request, HttpServletResponse response,Pager pager) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            pager = leaveService.getListByUid(pager,user.getId());
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 申请请假
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Leave leave) throws Exception {
        try {
            Leave res = leaveService.add(leave);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 请假详情
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
            ClientLeave res = leaveService.load(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/pass")
    public ModelAndView pass(HttpServletRequest request, HttpServletResponse response,
                             long id) throws Exception {
        try {
            boolean flag = leaveService.pass(id);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/nopass")
    public ModelAndView nopass(HttpServletRequest request, HttpServletResponse response,
                             long id,String reason) throws Exception {
        try {
           boolean flag = leaveService.nopass(id,reason);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
