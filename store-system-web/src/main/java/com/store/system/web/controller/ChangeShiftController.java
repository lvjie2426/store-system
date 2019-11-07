package com.store.system.web.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientChangeShift;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.ChangeShift;
import com.store.system.service.ChangeShiftService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 14:08
 **/
@Controller()
@RequestMapping("changeShift")
public class ChangeShiftController extends BaseController {

    @Resource
    private ChangeShiftService changeShiftService;

    /**
     * 获取个人调班历史记录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getListByUser")
    public ModelAndView getListByUser(HttpServletRequest request, HttpServletResponse response,Pager pager) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            pager = changeShiftService.getListByUid(user.getId(),pager);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 获取被调班列表
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getListByReplaceUid")
    public ModelAndView getListByReplaceUid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            List<ClientChangeShift> res = changeShiftService.getListByReplaceUid(user.getId());
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 申请调班
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, ChangeShift changeShift) throws Exception {
        try {
            ChangeShift res = changeShiftService.add(changeShift);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 调班详情
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
            ClientChangeShift res = changeShiftService.load(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 通过
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/pass")
    public ModelAndView pass(HttpServletRequest request, HttpServletResponse response,
                             long id) throws Exception {
        try {
           boolean flag = changeShiftService.pass(id);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 拒绝
     * @param request
     * @param response
     * @param id
     * @param reason   拒绝理由，暂时没作用，假如后期需要用直接set值
     * @return
     * @throws Exception
     */
    @RequestMapping("/nopass")
    public ModelAndView nopass(HttpServletRequest request, HttpServletResponse response,
                             long id,
                               @RequestParam(value = "reason",defaultValue = "") String reason) throws Exception {
        try {
           boolean flag = changeShiftService.nopass(id,reason);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
