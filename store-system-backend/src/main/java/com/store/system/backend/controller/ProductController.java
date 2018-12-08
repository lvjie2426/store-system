package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.s7.baseFramework.jackson.JsonUtils;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.webframework.BaseController;
import com.store.system.client.ClientProductSPU;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.GlassesException;
import com.store.system.model.ProductSKU;
import com.store.system.model.ProductSPU;
import com.store.system.service.ProductService;
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
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Resource
    private ProductService productService;

    @RequestMapping("/addSPU")
    public ModelAndView addSPU(ProductSPU productSPU, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productSPU = productService.addSPU(productSPU);
            return this.viewNegotiating(request,response, new ResultClient(productSPU));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/addSKU")
    public ModelAndView addSKU(ProductSKU productSKU, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productSKU = productService.addSKU(productSKU);
            return this.viewNegotiating(request,response, new ResultClient(productSKU));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/add")
    public ModelAndView add(ProductSPU productSPU, @RequestParam(value = "skuJson") String skuJson,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ProductSKU> productSKUList = null;
            try {
                productSKUList = JsonUtils.fromJson(skuJson, new TypeReference<List<ProductSKU>>() {});
            } catch (Exception e) {
                throw new GlassesException("sku格式错误");
            }
            productService.add(productSPU, productSKUList);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/change")
    public ModelAndView change(ProductSPU productSPU, @RequestParam(value = "addSkuJson") String addSkuJson,
                               @RequestParam(value = "updateSkuJson") String updateSkuJson,
                               @RequestParam(value = "delSkuIdJson") String delSkuIdJson,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ProductSKU> addProductSKUList = null;
            try {
                addProductSKUList = JsonUtils.fromJson(addSkuJson, new TypeReference<List<ProductSKU>>() {});
            } catch (Exception e) {
                throw new GlassesException("新增的sku格式错误");
            }
            List<ProductSKU> updateProductSKUList = null;
            try {
                updateProductSKUList = JsonUtils.fromJson(updateSkuJson, new TypeReference<List<ProductSKU>>() {});
            } catch (Exception e) {
                throw new GlassesException("修改的sku格式错误");
            }
            List<Long> delSkuIds = null;
            try {
                delSkuIds = JsonUtils.fromJson(delSkuIdJson, new TypeReference<List<Long>>() {});
            } catch (Exception e) {
                throw new GlassesException("删除的sku格式错误");
            }
            productService.change(productSPU, addProductSKUList, updateProductSKUList, delSkuIds);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateSPU")
    public ModelAndView updateSPU(ProductSPU productSPU, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean sign = productService.updateSPU(productSPU);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateSKU")
    public ModelAndView updateSKU(ProductSKU productSKU, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean sign = productService.updateSKU(productSKU);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delSPU")
    public ModelAndView delSPU(@RequestParam(required = true, value = "id") long id,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean sign = productService.delSPU(id);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delSKU")
    public ModelAndView delSKU(@RequestParam(required = true, value = "id") long id,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean sign = productService.delSKU(id);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getSPUPager")
    public ModelAndView getSPUPager(@RequestParam(value = "subid", defaultValue = "0") long subid,
                                    @RequestParam(value = "cid", defaultValue = "0") long cid,
                                    @RequestParam(value = "pid", defaultValue = "0") long pid,
                                    @RequestParam(value = "bid", defaultValue = "0") long bid,
                                    @RequestParam(value = "sid", defaultValue = "0") long sid,
                                    Pager pager,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        pager = productService.getBackPager(pager, subid, cid, pid, bid, sid);
        return this.viewNegotiating(request, response, new PagerResult<>(pager));
    }

    @RequestMapping("/loadSPU")
    public ModelAndView loadSPU(@RequestParam(value = "id") long id,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        ClientProductSPU clientProductSPU = productService.loadSPU(id);
        return this.viewNegotiating(request,response, new ResultClient(true, clientProductSPU));
    }

}
