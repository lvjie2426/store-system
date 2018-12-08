package com.store.system.backend.controller;

import com.s7.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.GlassesException;
import com.store.system.model.ProductBrand;
import com.store.system.service.ProductBrandService;
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
@RequestMapping("/productbrand")
public class ProductBrandController extends BaseController {

    @Resource
    private ProductBrandService productBrandService;

    @RequestMapping("/add")
    public ModelAndView add(ProductBrand productBrand, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productBrand = productBrandService.add(productBrand);
            return this.viewNegotiating(request,response, new ResultClient(productBrand));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(ProductBrand productBrand, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productBrandService.update(productBrand);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productBrandService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ProductBrand> res = productBrandService.getAllList();
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

}
