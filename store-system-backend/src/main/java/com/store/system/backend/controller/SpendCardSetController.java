package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.SpendCardSet;
import com.store.system.service.SpendCardSetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName SpendCardSetController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 11:36
 * @Version 1.0
 **/
@Controller
@RequestMapping("/spendCardSet")
public class SpendCardSetController extends BaseController{

    @Resource
    private SpendCardSetService spendCardSetService;

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, SpendCardSet spendCardSet) throws Exception {
        try {
            SpendCardSet res = spendCardSetService.add(spendCardSet);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response, long id) throws Exception {
        try {
            boolean res = spendCardSetService.delete(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, SpendCardSet spendCardSet) throws Exception {
        try {
            boolean res = spendCardSetService.update(spendCardSet);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response,
                                   long psid, long cid, long spuId) throws Exception {
        try {
            List<SpendCardSet> res = spendCardSetService.getAllList(psid, cid, spuId);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
