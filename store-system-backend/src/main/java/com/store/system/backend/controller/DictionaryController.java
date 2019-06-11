package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.model.Dictionary;
import com.store.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping("/add")
    public ModelAndView add(Dictionary dictionary, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        dictionary = dictionaryService.add(dictionary);
        return this.viewNegotiating(request, response, new ResultClient(dictionary));
    }

    @RequestMapping("/update")
    public ModelAndView update(Dictionary dictionary, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        boolean sign = dictionaryService.update(dictionary);
        return this.viewNegotiating(request, response, new ResultClient(sign));
    }

    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(@RequestParam(value = "id", required = true) long id,
                                     @RequestParam(value = "status", required = true) int status,
                                     HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        boolean sign = dictionaryService.updateStatus(id, status);
        return this.viewNegotiating(request, response, new ResultClient(sign));
    }

    @RequestMapping("/del")
    public ModelAndView del(long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        boolean sign = dictionaryService.del(id);
        return this.viewNegotiating(request,response,new ResultClient(sign));
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Dictionary> dictionaries= dictionaryService.getAllList();
        return this.viewNegotiating(request,response,dictionaries);
    }

}
