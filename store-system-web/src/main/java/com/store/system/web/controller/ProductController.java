package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientProductSPU;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-10-24 15:39
 **/
@Controller
@RequestMapping("/web/product")
public class ProductController extends BaseController {

    @Resource
    private ProductService productService;

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
