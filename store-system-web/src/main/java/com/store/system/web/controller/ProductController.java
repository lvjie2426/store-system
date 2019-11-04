package com.store.system.web.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientProductSPU;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Subordinate;
import com.store.system.service.ProductService;
import com.store.system.service.SubordinateService;
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

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Resource
    private ProductService productService;
    @Resource
    private SubordinateService subordinateService;

    @RequestMapping("/getSaleSPUPager")
    public ModelAndView getSaleSPUPager(HttpServletRequest request, HttpServletResponse response, Model model,Pager pager,
                                  @RequestParam(value = "subid", defaultValue = "0") long subid,
                                  @RequestParam(value = "type", defaultValue = "-1") int type,
                                  String brandSeries) throws Exception {
        try {
            long pSubid = subid;
            Subordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) pSubid = subordinate.getPid();
            pager = productService.getSaleSPUPager(pager,pSubid,subid,type,brandSeries);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

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
}
