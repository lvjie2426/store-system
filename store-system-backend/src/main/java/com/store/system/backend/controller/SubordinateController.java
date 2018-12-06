package com.store.system.backend.controller;

import com.store.system.client.ClientSubordinate;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.model.Subordinate;
import com.store.system.service.SubordinateService;
import com.google.common.collect.Lists;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.webframework.BaseController;
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


    @RequestMapping("/getSubordinatePager")
    public ModelAndView getSubordinatePager(HttpServletRequest request, HttpServletResponse response,
                                       Pager pager, String name,
                                            Model model) throws Exception {
        pager = subordinateService.getBackPage(pager, name);
        return this.viewNegotiating(request, response, new PagerResult<>(pager));
    }

    @RequestMapping("/getSubordinate")
    public ModelAndView getSubordinatePager(HttpServletRequest request, HttpServletResponse response,
                                       long sid,
                                       Model model) throws Exception {
        ClientSubordinate clientSubordinate = subordinateService.load(sid);
        List<ClientSubordinate> list = Lists.newArrayList();
        list.add(clientSubordinate);
        return this.viewNegotiating(request,response,new ResultClient(true,list));
    }

    @RequestMapping("/addSubordinate")
    public ModelAndView searchSubordinateCode(HttpServletRequest request, HttpServletResponse response,
                                         Subordinate subordinate,
                                         Model model) throws Exception {
        subordinate = subordinateService.insert(subordinate);
        return this.viewNegotiating(request,response, new ResultClient(subordinate));
    }

    @RequestMapping("/updateSubordinate")
    public ModelAndView updateSubordinate(HttpServletRequest request, HttpServletResponse response,
                                     Subordinate subordinate,
                                     Model model) throws Exception {
        ResultClient resultClient = new ResultClient(subordinateService.update(subordinate));
        return this.viewNegotiating(request,response,resultClient);
    }

    @RequestMapping("/deleteSubordinate")
    public ModelAndView deleteSubordinate(HttpServletRequest request, HttpServletResponse response,
                                     long id,
                                     Model model) throws Exception {
        ResultClient resultClient = new ResultClient(subordinateService.delete(id));
        return this.viewNegotiating(request,response,resultClient);
    }

    @RequestMapping("/getAllListByName")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response,String name) throws Exception {
        List<Subordinate> subordinates = subordinateService.getAllList();
        List<Subordinate> result=new ArrayList<>();
        for (Subordinate subordinate:subordinates){
            if(StringUtils.isBlank(name)||subordinate.getName().contains(name)){
                result.add(subordinate);
            }
        }
        return this.viewNegotiating(request,response,result);
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Subordinate> subordinates= subordinateService.getAllList();
        return this.viewNegotiating(request,response,subordinates);
    }
}
