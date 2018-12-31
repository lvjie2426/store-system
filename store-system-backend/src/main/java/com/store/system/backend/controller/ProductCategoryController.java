package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductCategory;
import com.store.system.service.ProductCategoryService;
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
@RequestMapping("/productcategory")
public class ProductCategoryController extends BaseController {

    @Resource
    private ProductCategoryService productCategoryService;

    @RequestMapping("/add")
    public ModelAndView add(ProductCategory productCategory, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productCategory = productCategoryService.add(productCategory);
            return this.viewNegotiating(request,response, new ResultClient(productCategory));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(ProductCategory productCategory, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productCategoryService.update(productCategory);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productCategoryService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ProductCategory> res = productCategoryService.getAllList();
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

}
