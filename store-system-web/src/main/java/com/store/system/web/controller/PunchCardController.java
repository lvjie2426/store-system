package com.store.system.web.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientPunchCardLog;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.service.PunchCardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/punchCard")
public class PunchCardController extends BaseController {

    @Resource
    private PunchCardService punchCardService;

    /**
     * 获取个人的打卡log记录
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/getUserLogsPager")
    public ModelAndView getPunchCardLogs(HttpServletRequest request, HttpServletResponse response,
                                         Pager pager, long uid, Model model) throws Exception {
        try {
            pager = punchCardService.getWebLogsPager(pager, uid);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


    /**
     * 查询用户的打卡记录
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/load")
    public ModelAndView getUserAttendanceByDay(HttpServletRequest request, HttpServletResponse response,
                                               long id, Model model) throws Exception {
        try {
            ClientPunchCardLog clientPunchCardLog = punchCardService.loadPunchCardLog(id);
            return this.viewNegotiating(request, response, new ResultClient(clientPunchCardLog));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
