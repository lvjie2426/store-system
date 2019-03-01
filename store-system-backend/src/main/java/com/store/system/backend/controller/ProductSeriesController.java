package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductProvider;
import com.store.system.model.ProductProviderPool;
import com.store.system.model.ProductSeries;
import com.store.system.model.ProductSeriesPool;
import com.store.system.service.ProductSeriesService;
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
@RequestMapping("/productseries")
public class ProductSeriesController extends BaseController {

    @Resource
    private ProductSeriesService productSeriesService;

    @Resource
    private SubordinateService subordinateService;

    @RequestMapping("/add")
    public ModelAndView add(ProductSeries productSeries, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productSeries = productSeriesService.add(productSeries);
            return this.viewNegotiating(request,response, new ResultClient(productSeries));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(ProductSeries productSeries, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productSeriesService.update(productSeries);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productSeriesService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "bid") long bid,
                                   HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ProductSeries> res = productSeriesService.getAllList(bid);
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }


    @RequestMapping("/addPool")
    public ModelAndView addPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "sid") long sid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能添加");
            ProductSeries series = productSeriesService.load(sid);
            ProductSeriesPool productSeriesPool = new ProductSeriesPool();
            productSeriesPool.setSubid(subid);
            productSeriesPool.setBid(series.getBid());
            productSeriesPool.setSid(sid);
            boolean sign = productSeriesService.addPool(productSeriesPool);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delPool")
    public ModelAndView delPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "sid") long sid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能删除");
            ProductSeries series = productSeriesService.load(sid);
            ProductSeriesPool productSeriesPool = new ProductSeriesPool();
            productSeriesPool.setSubid(subid);
            productSeriesPool.setBid(series.getBid());
            productSeriesPool.setSid(sid);
            boolean res = productSeriesService.delPool(productSeriesPool);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getSubAllList")
    public ModelAndView getSubAllList(@RequestParam(value = "subid") long subid,
                                      @RequestParam(value = "bid") long bid,
                                      HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) subid = subordinate.getPid();
            List<ProductSeries> res = productSeriesService.getSubAllList(subid, bid);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
