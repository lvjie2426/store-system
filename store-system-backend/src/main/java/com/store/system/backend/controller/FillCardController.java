package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientFillCard;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.FillCard;
import com.store.system.service.FillCardService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-18 16:14
 **/

@Controller
@RequestMapping("fillCard")
public class FillCardController extends BaseController {

    @Resource
    private FillCardService fillCardService;

    /**
     * 获取个人补卡历史记录
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
            pager = fillCardService.getListByUid(user.getId(), pager);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 申请补卡
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, FillCard fillCard) throws Exception {
        try {
            FillCard res = fillCardService.add(fillCard);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 补卡详情
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
            ClientFillCard res = fillCardService.load(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 审核补卡通过
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
            boolean flag = fillCardService.pass(id);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 审核补卡bu通过
     *
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/nopass")
    public ModelAndView nopass(HttpServletRequest request, HttpServletResponse response,
                               long id) throws Exception {
        try {
            boolean flag = fillCardService.nopass(id);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 获取补卡列表
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "subid",defaultValue = "0") long subid,
                                   @RequestParam(value = "sid") long sid,
                                   @RequestParam(value = "startTime",defaultValue = "0") long startTime,
                                   @RequestParam(value = "endTime",defaultValue = "0") long endTime,
                                   @RequestParam(value = "status",defaultValue = "0") int status,
                                   @RequestParam(value = "userName",defaultValue = "") String userName,

                                   Pager pager) throws Exception {
        try {
            pager = fillCardService.getAllList(pager,subid,sid,startTime,endTime,status,userName);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 编辑补卡
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, FillCard fillCard) throws Exception {
        try {
            Boolean flag= fillCardService.update(fillCard);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
