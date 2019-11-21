package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryInBillItem;
import com.store.system.bean.SaleReward;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientProductSPU;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.CommissionService;
import com.store.system.service.OrderService;
import com.store.system.service.ProductService;
import com.store.system.service.SubordinateService;
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Resource
    private ProductService productService;
    @Resource
    private CommissionService commissionService;
    @Resource
    private SubordinateService subordinateService;
    @Resource
    private OrderService orderService;

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
    public ModelAndView add(ProductSPU productSPU, String brandName, String seriesName,
                            @RequestParam(required = false, value = "skuJson") String skuJson,
                            @RequestParam(required = false, value = "commissionJson") String commissionJson,
                            @RequestParam(required = false, value = "rangesJson") String rangesJson,
                            @RequestParam(required = false, value = "nowRangesJson") String nowRangesJson,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ProductSKU> productSKUList = Lists.newArrayList();
            try {
                if (StringUtils.isNotBlank(skuJson)) {
                    productSKUList = JsonUtils.fromJson(skuJson, new TypeReference<List<ProductSKU>>() {
                    });
                }
            } catch (Exception e) {
                throw new StoreSystemException("sku格式错误");
            }
//            List<UserGradeCategoryDiscount> ugDiscountList = Lists.newArrayList();
//            try {
//                if (StringUtils.isNotBlank(ugDiscount)) {
//                    ugDiscountList = JsonUtils.fromJson(ugDiscount, new TypeReference<List<UserGradeCategoryDiscount>>() {});
//                }
//                for (UserGradeCategoryDiscount info : ugDiscountList) {
//                    String regex = "^[1-9]+(.[1-9]{1})?$";
//                    Pattern pattern = Pattern.compile(regex);
//                    Matcher matcherUser = pattern.matcher(String.valueOf(info.getDiscount()));
//                    if (!matcherUser.matches()) throw new StoreSystemException("会员折扣输入格式有误！");
//                }
//            } catch (Exception e) {
//                throw new StoreSystemException("会员折扣格式错误");
//            }
            List<Commission> commissions = Lists.newArrayList();
            try {
                if (StringUtils.isNotBlank(commissionJson)) {
                    commissions = JsonUtils.fromJson(commissionJson, new TypeReference<List<Commission>>() {});
                }
            } catch (Exception e) {
                throw new StoreSystemException("commission提成格式错误");
            }
            List<ProductCustomRange> ranges = Lists.newArrayList();
            List<ProductCustomRange> nowRanges = Lists.newArrayList();
            if (StringUtils.isNotBlank(rangesJson)) {
                ranges = JsonUtils.fromJson(rangesJson, new TypeReference<List<ProductCustomRange>>() {});
            }
            if (StringUtils.isNotBlank(nowRangesJson)) {
                nowRanges = JsonUtils.fromJson(nowRangesJson, new TypeReference<List<ProductCustomRange>>() {});
            }
            productSPU.setRanges(ranges);
            productSPU.setNowRanges(nowRanges);
            productService.add(productSPU, productSKUList, brandName, seriesName, commissions);
            return this.viewNegotiating(request, response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
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
    public ModelAndView change(ProductSPU productSPU, String brandName, String seriesName, @RequestParam(value = "addSkuJson") String addSkuJson,
                               @RequestParam(value = "updateSkuJson") String updateSkuJson,
                               @RequestParam(value = "delSkuIdJson") String delSkuIdJson,
                               @RequestParam(required = false,value = "commissionJson") String commissionJson,
                               @RequestParam(required = false,value = "rangesJson") String rangesJson,
                               @RequestParam(required = false,value = "nowRangesJson") String nowRangesJson,
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
            List<Commission> commissions = Lists.newArrayList();
            try {
                if(StringUtils.isNotBlank(commissionJson)) {
                    commissions = JsonUtils.fromJson(commissionJson, new TypeReference<List<Commission>>() {});
                }
            } catch (Exception e) {
                throw new StoreSystemException("修改的commission提成格式错误");
            }

            List<ProductCustomRange> ranges = Lists.newArrayList();
            List<ProductCustomRange> nowRanges = Lists.newArrayList();
            if(StringUtils.isNotBlank(rangesJson)) {
                ranges = JsonUtils.fromJson(rangesJson, new TypeReference<List<ProductCustomRange>>() {});
            }
            if(StringUtils.isNotBlank(nowRangesJson)) {
                nowRanges = JsonUtils.fromJson(nowRangesJson, new TypeReference<List<ProductCustomRange>>() {});
            }
            productSPU.setRanges(ranges);
            productSPU.setNowRanges(nowRanges);
            productService.change(productSPU, addProductSKUList, updateProductSKUList, delSkuIds, brandName, seriesName, commissions);
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
     * params: [subid(门店ID), cid, pid, bid, sid, pager, request, response, model,name]
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
                                    @RequestParam(value = "type", defaultValue = "0") int type,
                                    @RequestParam(value = "name", defaultValue = "") String name,
                                    @RequestParam(value = "saleStatus", defaultValue = "-1") int saleStatus,
                                    Pager pager,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) subid = subordinate.getPid();
            pager = productService.getSPUBackPager(pager, subid, cid, pid, bid, sid,type,name,saleStatus, subordinate.getId());
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
                                            @RequestParam(value = "type", defaultValue = "-1") int type,
                                            Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            long pSubid = subid;
            Subordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) pSubid = subordinate.getPid();
            pager = productService.getSaleSPUBackPager(pager, pSubid, subid, cid, bid, type);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 销售开单 添加商品的SKU列表 uid为顾客
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
                                          @RequestParam(value = "uid", defaultValue = "0") long uid,
                                          HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ClientProductSKU> res = productService.getSaleSKUAllList(subid, spuid, uid);
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


    //实施工作台--销售奖励
    @RequestMapping("/saleReward")
    public ModelAndView saleReward(HttpServletRequest request,HttpServletResponse response,
                                   @RequestParam(name = "subid") long subid)throws Exception{
        try {
            Subordinate subordinate = subordinateService.load(subid);
            long sid = subordinate.getPid();
            if(sid==0){ throw new StoreSystemException("门店ID有误"); }
            Map<String,Object> res = orderService.saleReward(subid);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response, new ResultClient(false, s.getMessage()));
        }
    }

    //医疗器械商品 审核通过 变成已验收状态。
    @RequestMapping("/checkStatus")
    public ModelAndView checkStatus(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(required = true, value = "ids[]", defaultValue = "") List<Long> ids) throws Exception {
        try {
            User user= UserUtils.getUser(request);
            boolean flag= productService.checkStatus(ids,user);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    //获取未补充证明的医疗器械的商品
    @RequestMapping("/getNoNirNum")
    public ModelAndView getNoNirNum(@RequestParam(value = "subid", defaultValue = "0") long subid,
                                    Pager pager,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) subid = subordinate.getPid();
            pager = productService.getSPUNoNirNumPager(pager, subid);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    // 搜索商品  按照spu的name 分类返回
    @RequestMapping("/searchSpu")
    public ModelAndView searchSpu(@RequestParam(value = "name") String name,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            if(StringUtils.isBlank(name)){
                throw new StoreSystemException("搜索字段不能为空");
            }

            return this.viewNegotiating(request, response, new ResultClient(productService.searchSpu(name)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
