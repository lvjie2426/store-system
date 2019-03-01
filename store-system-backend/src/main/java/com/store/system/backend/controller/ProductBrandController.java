package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductBrand;
import com.store.system.model.ProductBrandPool;
import com.store.system.service.ProductBrandService;
import com.store.system.service.SubordinateService;
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

    @Resource
    private SubordinateService subordinateService;

    @RequestMapping("/add")
    public ModelAndView add(ProductBrand productBrand, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productBrand = productBrandService.add(productBrand);
            return this.viewNegotiating(request,response, new ResultClient(productBrand));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(ProductBrand productBrand, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productBrandService.update(productBrand);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productBrandService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ProductBrand> res = productBrandService.getAllList();
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }




    @RequestMapping("/addPool")
    public ModelAndView addPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "bid") long bid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能添加");
            ProductBrandPool productBrandPool = new ProductBrandPool();
            productBrandPool.setSubid(subid);
            productBrandPool.setBid(bid);
            boolean sign = productBrandService.addPool(productBrandPool);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delPool")
    public ModelAndView delPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "bid") long bid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能删除");
            ProductBrandPool productBrandPool = new ProductBrandPool();
            productBrandPool.setSubid(subid);
            productBrandPool.setBid(bid);
            boolean res = productBrandService.delPool(productBrandPool);
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
            List<ProductBrand> res = productBrandService.getSubAllList(subid);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
