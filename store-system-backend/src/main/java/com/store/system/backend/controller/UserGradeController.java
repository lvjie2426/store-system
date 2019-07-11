package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.UserGrade;
import com.store.system.service.UserGradeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/usergrade")
public class UserGradeController extends BaseController {

    @Resource
    private UserGradeService userGradeService;

    @RequestMapping("/add")
    public ModelAndView add(UserGrade userGrade, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            userGrade = userGradeService.add(userGrade);
            return this.viewNegotiating(request,response, new ResultClient(userGrade));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(UserGrade userGrade, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = userGradeService.update(userGrade);
            return this.viewNegotiating(request,response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = userGradeService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "subid") long subid,
                                   HttpServletRequest request, HttpServletResponse response, Model model, Pager pager) throws Exception {
        try {
            pager = userGradeService.getByPager(pager,subid);
            return this.viewNegotiating(request,response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
