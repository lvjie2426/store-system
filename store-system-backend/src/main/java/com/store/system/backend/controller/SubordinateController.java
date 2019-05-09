package com.store.system.backend.controller;

import com.store.system.client.ClientSubordinate;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Subordinate;
import com.store.system.service.SubordinateService;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/subordinate")
public class SubordinateController extends BaseController {

    @Autowired
    private SubordinateService subordinateService;


//    @RequestMapping("/getSubordinatePager")
//    public ModelAndView getSubordinatePager(HttpServletRequest request, HttpServletResponse response,
//                                       Pager pager, String name, Model model) throws Exception {
//        pager = subordinateService.getBackPage(pager, name);
//        return this.viewNegotiating(request, response, new PagerResult<>(pager));
//    }

    @RequestMapping("/getSubordinate")
    public ModelAndView getSubordinatePager(HttpServletRequest request, HttpServletResponse response,
                                       long sid,
                                       Model model) throws Exception {
        try {
            ClientSubordinate clientSubordinate = subordinateService.load(sid);
            List<ClientSubordinate> list = Lists.newArrayList();
            list.add(clientSubordinate);
            return this.viewNegotiating(request,response,new ResultClient(true,list));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }

    @RequestMapping("/addSubordinate")
    public ModelAndView searchSubordinateCode(HttpServletRequest request, HttpServletResponse response,
                                         Subordinate subordinate,
                                         Model model) throws Exception {
        try {
            subordinate = subordinateService.insert(subordinate);
            return this.viewNegotiating(request,response, new ResultClient(subordinate));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }

    @RequestMapping("/updateSubordinate")
    public ModelAndView updateSubordinate(HttpServletRequest request, HttpServletResponse response,
                                     Subordinate subordinate,
                                     Model model) throws Exception {
        try {
            ResultClient resultClient = new ResultClient(subordinateService.update(subordinate));
            return this.viewNegotiating(request,response,resultClient);
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }

    @RequestMapping("/deleteSubordinate")
    public ModelAndView deleteSubordinate(HttpServletRequest request, HttpServletResponse response,
                                     long id,
                                     Model model) throws Exception {
        try {
            ResultClient resultClient = new ResultClient(subordinateService.delete(id));
            return this.viewNegotiating(request,response,resultClient);
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
    @RequestMapping("/addSubordinateStore")
    public ModelAndView addSubordinateStore(HttpServletRequest request, HttpServletResponse response,
                                              Subordinate subordinate,
                                              Model model) throws Exception {
        try {
            subordinate = subordinateService.insertStore(subordinate);
            return this.viewNegotiating(request,response, new ResultClient(subordinate));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
    @RequestMapping("/updateSubordinateStore")
    public ModelAndView updateSubordinateStore(HttpServletRequest request, HttpServletResponse response,
                                          Subordinate subordinate,
                                          Model model) throws Exception {
        try {
            ResultClient resultClient = new ResultClient(subordinateService.updateStore(subordinate));
            return this.viewNegotiating(request,response,resultClient);
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
    @RequestMapping("/getSubordinateStore")
    public ModelAndView getSubordinateStore(HttpServletRequest request, HttpServletResponse response,
                                            long sid,
                                            Model model) throws Exception {
        try {
            List<ClientSubordinate> list= subordinateService.getTwoLevelAllList(sid);
            return this.viewNegotiating(request,response,new ResultClient(true,list));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
}
