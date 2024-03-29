package com.store.system.backend.controller;

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
     * 获取调班列表
     *
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
                                @RequestParam(value = "status",defaultValue = "-1") int status,

                                Pager pager) throws Exception {
        try {
            pager = changeShiftService.getList(subid,sid,userName,startTime,endTime,status,pager);
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
            User user=UserUtils.getUser(request);
           boolean flag = changeShiftService.pass(id);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 拒绝  审核人
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

    /**
     * 拒绝  被调班人
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/replaceNopass")
    public ModelAndView replaceNopass(HttpServletRequest request, HttpServletResponse response,
                             long id) throws Exception {
        try {
           boolean flag = changeShiftService.replaceNopass(id);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 接受  被调班人
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/replacePass")
    public ModelAndView replacePass(HttpServletRequest request, HttpServletResponse response,
                             long id) throws Exception {
        try {
           boolean flag = changeShiftService.replacePass(id);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
