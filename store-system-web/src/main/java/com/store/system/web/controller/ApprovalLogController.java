package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientApprovalLog;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.service.ApprovalLogService;
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
 * @create: 2019-09-17 15:13
 **/
@Controller
@RequestMapping("approvalLog")
public class ApprovalLogController extends BaseController {

    @Autowired
    private ApprovalLogService approvalLogService;

    /**
     * 获取审批列表
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getList")
    public ModelAndView getList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            List<ClientApprovalLog> res = approvalLogService.getList(user.getId());
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
