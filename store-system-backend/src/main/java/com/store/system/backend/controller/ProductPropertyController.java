package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientProductProperty;
import com.store.system.client.ResultClient;
import com.store.system.service.ProductPropertyService;
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
@RequestMapping("/productproperty")
public class ProductPropertyController extends BaseController {

    @Resource
    private ProductPropertyService productPropertyService;

    @RequestMapping("/getAllProperties")
    public ModelAndView getAllProperties(@RequestParam(value = "cid") long cid,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ClientProductProperty> properties = productPropertyService.getAllProperties(cid);
        return this.viewNegotiating(request,response,new ResultClient(properties));
    }

}
