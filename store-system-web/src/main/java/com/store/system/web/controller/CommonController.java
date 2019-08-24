package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientProductSeries;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductCategory;
import com.store.system.service.ProductCategoryService;
import com.store.system.service.ProductSeriesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

    @Resource
    private ProductCategoryService productCategoryService;

    @Resource
    private ProductSeriesService productSeriesService;


    /***
    * 获取门店下的类目列表
    * @Param: [subid, request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/8/24
    */
    @RequestMapping("/getSubCategoryList")
    public ModelAndView getSubAllList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ProductCategory> res = productCategoryService.getAllList();
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    *  获取品牌下的列表
    * @Param: [request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/8/24
    */
    @RequestMapping("/getBrandSeriesList")
    public ModelAndView getBrandSeriesList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ClientProductSeries> res = productSeriesService.getAllList();
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }
}
