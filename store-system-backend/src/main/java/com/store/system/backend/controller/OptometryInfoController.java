package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientOptometryInfo;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.OptometryInfo;
import com.store.system.service.OptometryInfoService;
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
@RequestMapping("/optometryinfo")
public class OptometryInfoController extends BaseController {

    @Resource
    private OptometryInfoService optometryInfoService;

    @RequestMapping("/add")
    public ModelAndView add(OptometryInfo optometryInfo, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            optometryInfo = optometryInfoService.add(optometryInfo);
            return this.viewNegotiating(request,response, new ResultClient(optometryInfo));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(OptometryInfo optometryInfo, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = optometryInfoService.update(optometryInfo);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = optometryInfoService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getList")
    public ModelAndView getList(@RequestParam(value = "cid") long cid,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ClientOptometryInfo> res = optometryInfoService.getList(cid, size);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }



}
