package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.GlassesException;
import com.store.system.model.ProductSeries;
import com.store.system.service.ProductSeriesService;
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

    @RequestMapping("/add")
    public ModelAndView add(ProductSeries productSeries, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productSeries = productSeriesService.add(productSeries);
            return this.viewNegotiating(request,response, new ResultClient(productSeries));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(ProductSeries productSeries, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productSeriesService.update(productSeries);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productSeriesService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "bid") long bid,
                                   HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ProductSeries> res = productSeriesService.getAllList(bid);
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

}
