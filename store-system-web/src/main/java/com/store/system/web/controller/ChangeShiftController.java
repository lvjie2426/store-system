package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientChangeShift;
import com.store.system.client.ResultClient;
import com.store.system.model.User;
import com.store.system.model.attendance.ChangeShift;
import com.store.system.service.ChangeShiftService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getListByUser")
    public ModelAndView getListByUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = UserUtils.getUser(request);
        List<ClientChangeShift> res = changeShiftService.getListByUid(user.getId());
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

    /**
     * 获取被调班列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getListByReplaceUid")
    public ModelAndView getListByReplaceUid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = UserUtils.getUser(request);
        List<ClientChangeShift> res = changeShiftService.getListByReplaceUid(user.getId());
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

    /**
     * 申请调班
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response,ChangeShift changeShift) throws Exception {
        ChangeShift res = changeShiftService.add(changeShift);
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

    /**
     * 调班详情
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/load")
    public ModelAndView load(HttpServletRequest request, HttpServletResponse response,
                             long id) throws Exception {
        ClientChangeShift res = changeShiftService.load(id);
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

}
