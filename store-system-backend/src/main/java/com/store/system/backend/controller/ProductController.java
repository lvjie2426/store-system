package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientProductSPU;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductSKU;
import com.store.system.model.ProductSPU;
import com.store.system.model.Subordinate;
import com.store.system.service.ProductService;
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
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Resource
    private ProductService productService;

    @Resource
    private SubordinateService subordinateService;

    @RequestMapping("/addSPU")
    public ModelAndView addSPU(ProductSPU productSPU, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productSPU = productService.addSPU(productSPU);
            return this.viewNegotiating(request,response, new ResultClient(productSPU));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/addSKU")
    public ModelAndView addSKU(ProductSKU productSKU, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productSKU = productService.addSKU(productSKU);
            return this.viewNegotiating(request,response, new ResultClient(productSKU));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 增加商品SPU和SKU
     * method_name: add
     * params: [productSPU, skuJson, request, response, model]
     * return: org.springframework.web.servlet.ModelAndView
     * creat_user: lihao
     * creat_date: 2019/3/2
     * creat_time: 14:49
     **/
    @RequestMapping("/add")
    public ModelAndView add(ProductSPU productSPU, @RequestParam(value = "skuJson") String skuJson,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ProductSKU> productSKUList = null;
            try {
                productSKUList = JsonUtils.fromJson(skuJson, new TypeReference<List<ProductSKU>>() {});
            } catch (Exception e) {
                throw new StoreSystemException("sku格式错误");
            }
            productService.add(productSPU, productSKUList);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 修改商品SPU和SKU
     * method_name: change
     * params: [productSPU, addSkuJson, updateSkuJson, delSkuIdJson, request, response, model]
     * return: org.springframework.web.servlet.ModelAndView
     * creat_user: lihao
     * creat_date: 2019/3/2
     * creat_time: 14:48
     **/
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
                throw new StoreSystemException("新增的sku格式错误");
            }
            List<ProductSKU> updateProductSKUList = null;
            try {
                updateProductSKUList = JsonUtils.fromJson(updateSkuJson, new TypeReference<List<ProductSKU>>() {});
            } catch (Exception e) {
                throw new StoreSystemException("修改的sku格式错误");
            }
            List<Long> delSkuIds = null;
            try {
                delSkuIds = JsonUtils.fromJson(delSkuIdJson, new TypeReference<List<Long>>() {});
            } catch (Exception e) {
                throw new StoreSystemException("删除的sku格式错误");
            }
            productService.change(productSPU, addProductSKUList, updateProductSKUList, delSkuIds);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateSPU")
    public ModelAndView updateSPU(ProductSPU productSPU, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean sign = productService.updateSPU(productSPU);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateSKU")
    public ModelAndView updateSKU(ProductSKU productSKU, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean sign = productService.updateSKU(productSKU);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delSPU")
    public ModelAndView delSPU(@RequestParam(required = true, value = "id") long id,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean sign = productService.delSPU(id);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delSKU")
    public ModelAndView delSKU(@RequestParam(required = true, value = "id") long id,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean sign = productService.delSKU(id);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 获取公司的所有商品SPU
     * method_name: getSPUPager
     * params: [subid, cid, pid, bid, sid, pager, request, response, model,name]
     * return: org.springframework.web.servlet.ModelAndView
     * creat_user: lihao
     * creat_date: 2019/3/2
     * creat_time: 14:39
     **/
    @RequestMapping("/getSPUPager")
    public ModelAndView getSPUPager(@RequestParam(value = "subid", defaultValue = "0") long subid,
                                    @RequestParam(value = "cid", defaultValue = "0") long cid,
                                    @RequestParam(value = "pid", defaultValue = "0") long pid,
                                    @RequestParam(value = "bid", defaultValue = "0") long bid,
                                    @RequestParam(value = "sid", defaultValue = "0") long sid,
                                    @RequestParam(value = "name", defaultValue = "") String name,
                                    @RequestParam(value = "saleStatus", defaultValue = "-1") int saleStatus,
                                    Pager pager,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) subid = subordinate.getPid();
            pager = productService.getSPUBackPager(pager, subid, cid, pid, bid, sid,name,saleStatus);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/loadSPU")
    public ModelAndView loadSPU(@RequestParam(value = "id") long id,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        ClientProductSPU clientProductSPU = productService.loadSPU(id);
        return this.viewNegotiating(request,response, new ResultClient(true, clientProductSPU));
    }

    /**
     * 销售开单 添加商品的SPU列表
     * method_name: getSaleSPUBackPager
     * params: [subid, cid, bid, pager, request, response, model]
     * return: org.springframework.web.servlet.ModelAndView
     * creat_user: lihao
     * creat_date: 2019/3/2
     * creat_time: 14:38
     **/
    @RequestMapping("/getSaleSPUBackPager")
    public ModelAndView getSaleSPUBackPager(@RequestParam(value = "subid", defaultValue = "0") long subid,
                                    @RequestParam(value = "cid", defaultValue = "0") long cid,
                                    @RequestParam(value = "bid", defaultValue = "0") long bid,
                                    Pager pager,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            long pSubid = subid;
            Subordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) pSubid = subordinate.getPid();
            pager = productService.getSaleSPUBackPager(pager, pSubid, subid, cid, bid);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 销售开单 添加商品的SKU列表
     * method_name: getSaleSKUAllList
     * params: [subid, spuid, request, response, model]
     * return: org.springframework.web.servlet.ModelAndView
     * creat_user: lihao
     * creat_date: 2019/3/2
     * creat_time: 15:34
     **/
    @RequestMapping("/getSaleSKUAllList")
    public ModelAndView getSaleSKUAllList(@RequestParam(value = "subid", defaultValue = "0") long subid,
                                            @RequestParam(value = "spuid") long spuid,
                                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ClientProductSKU> res = productService.getSaleSKUAllList(subid, spuid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateSaleStatus")
    public ModelAndView updateSaleStatus(@RequestParam(value = "id") long id,
                                   @RequestParam(value = "open") int open,
                                   HttpServletRequest request, HttpServletResponse response,
                                   Model model) throws Exception {
        try {
            boolean sign = productService.updateSaleStatus(id,open);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
