package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.GlassesException;
import com.store.system.model.ProductCategory;
import com.store.system.model.ProductProvider;
import com.store.system.service.ProductProviderService;
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
@RequestMapping("/productprovider")
public class ProductProviderController extends BaseController {

    @Resource
    private ProductProviderService productProviderService;

    @RequestMapping("/add")
    public ModelAndView add(ProductProvider productProvider, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productProvider = productProviderService.add(productProvider);
            return this.viewNegotiating(request,response, new ResultClient(productProvider));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(ProductProvider productProvider, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productProviderService.update(productProvider);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productProviderService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ProductProvider> res = productProviderService.getAllList();
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

}
