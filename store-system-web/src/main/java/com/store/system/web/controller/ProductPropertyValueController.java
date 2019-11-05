package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientProductPropertyValue;
import com.store.system.client.ResultClient;
import com.store.system.service.ProductPropertyValueService;
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
@RequestMapping("/productpropertyvalue")
public class ProductPropertyValueController extends BaseController {

    @Resource
    private ProductPropertyValueService productPropertyValueService;


    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "pnid") long pnid, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ClientProductPropertyValue> res = productPropertyValueService.getAllList(pnid);
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }


}
