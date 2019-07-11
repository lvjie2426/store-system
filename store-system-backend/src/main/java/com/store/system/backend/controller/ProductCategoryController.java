package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductBrand;
import com.store.system.model.ProductBrandPool;
import com.store.system.model.ProductCategory;
import com.store.system.model.ProductCategoryPool;
import com.store.system.service.ProductCategoryService;
import com.store.system.service.SubordinateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/productcategory")
public class ProductCategoryController extends BaseController {

    @Resource
    private ProductCategoryService productCategoryService;

    @Resource
    private SubordinateService subordinateService;

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

    @RequestMapping("/getAllListByName")
    public ModelAndView getAllListByName(HttpServletRequest request, HttpServletResponse response, String name, Model model) throws Exception {
        List<ProductCategory> productCategories = productCategoryService.getAllList();
        List<ProductCategory> result=new ArrayList<>();
        for (ProductCategory productCategory:productCategories){
            if(StringUtils.isBlank(name)||productCategory.getName().contains(name)){
                result.add(productCategory);
            }
        }
        return this.viewNegotiating(request,response, new ResultClient(true, result));
    }

    @RequestMapping("/addPool")
    public ModelAndView addPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "cid") long cid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能添加");
            ProductCategoryPool productCategoryPool = new ProductCategoryPool();
            productCategoryPool.setSubid(subid);
            productCategoryPool.setCid(cid);
            boolean sign = productCategoryService.addPool(productCategoryPool);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delPool")
    public ModelAndView delPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "cid") long cid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能删除");
            ProductCategoryPool productCategoryPool = new ProductCategoryPool();
            productCategoryPool.setSubid(subid);
            productCategoryPool.setCid(cid);
            boolean res = productCategoryService.delPool(productCategoryPool);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getSubAllList")
    public ModelAndView getSubAllList(@RequestParam(value = "subid") long subid,
                                      HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) subid = subordinate.getPid();
            List<ProductCategory> res = productCategoryService.getSubAllList(subid);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }
}
