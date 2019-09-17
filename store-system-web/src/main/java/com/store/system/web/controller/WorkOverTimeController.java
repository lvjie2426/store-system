package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientProductSeries;
import com.store.system.client.ClientWorkOverTime;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductCategory;
import com.store.system.model.ProductProvider;
import com.store.system.model.User;
import com.store.system.model.attendance.WorkOverTime;
import com.store.system.service.ProductCategoryService;
import com.store.system.service.ProductProviderService;
import com.store.system.service.ProductSeriesService;
import com.store.system.service.WorkOverTimeService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**
     * 获取个人加班历史记录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getListByUser")
    public ModelAndView getListByUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            List<ClientWorkOverTime> res = workOverTimeService.getListByUid(user.getId());
            return this.viewNegotiating(request, response, new ResultClient(true, res));
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
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/nopass")
    public ModelAndView nopass(HttpServletRequest request, HttpServletResponse response,
                             long id,String reason) throws Exception {
        try {
            boolean flag = workOverTimeService.nopass(id,reason);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
