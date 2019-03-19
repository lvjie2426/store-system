package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductCustom;
import com.store.system.model.Subordinate;
import com.store.system.service.ProductCustomService;
import com.store.system.service.SubordinateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/productcustom")
public class ProductCustomController extends BaseController {

    @Resource
    private ProductCustomService productCustomService;

    @Resource
    private SubordinateService subordinateService;

    @RequestMapping("/add")
    public ModelAndView add(ProductCustom productCustom, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productCustom = productCustomService.add(productCustom);
            return this.viewNegotiating(request,response, new ResultClient(productCustom));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(@RequestParam(value = "id", required = true) long id,
                                     @RequestParam(value = "status", required = true) int status, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productCustomService.updateStatus(id, status);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getPager")
    public ModelAndView getPager(@RequestParam(value = "subid") long subid, @RequestParam(value = "status", required = true) int status,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(subid);
            pager = productCustomService.getBackPager(pager, subid, status);
            return this.viewNegotiating(request,response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }



}
