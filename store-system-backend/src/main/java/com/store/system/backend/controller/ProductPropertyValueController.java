package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientProductPropertyValue;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductPropertyValue;
import com.store.system.model.ProductPropertyValuePool;
import com.store.system.service.ProductPropertyValueService;
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
@RequestMapping("/productpropertyvalue")
public class ProductPropertyValueController extends BaseController {

    @Resource
    private ProductPropertyValueService productPropertyValueService;

    @Resource
    private SubordinateService subordinateService;

    @RequestMapping("/add")
    public ModelAndView add(ProductPropertyValue productPropertyValue, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productPropertyValue = productPropertyValueService.add(productPropertyValue);
            return this.viewNegotiating(request,response, new ResultClient(productPropertyValue));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(ProductPropertyValue productPropertyValue, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productPropertyValueService.update(productPropertyValue);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productPropertyValueService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "pnid") long pnid, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ClientProductPropertyValue> res = productPropertyValueService.getAllList(pnid);
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

    @RequestMapping("/addPool")
    public ModelAndView addPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "pvid") long pvid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能添加");
            ProductPropertyValue propertyValue = productPropertyValueService.load(pvid);
            ProductPropertyValuePool productPropertyValuePool = new ProductPropertyValuePool();
            productPropertyValuePool.setSubid(subid);
            productPropertyValuePool.setPnid(propertyValue.getPnid());
            productPropertyValuePool.setPvid(pvid);
            boolean sign = productPropertyValueService.addPool(productPropertyValuePool);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delPool")
    public ModelAndView delPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "pvid") long pvid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能删除");
            ProductPropertyValue propertyValue = productPropertyValueService.load(pvid);
            ProductPropertyValuePool productPropertyValuePool = new ProductPropertyValuePool();
            productPropertyValuePool.setSubid(subid);
            productPropertyValuePool.setPnid(propertyValue.getPnid());
            productPropertyValuePool.setPvid(pvid);
            boolean res = productPropertyValueService.delPool(productPropertyValuePool);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getSubAllList")
    public ModelAndView getSubAllList(@RequestParam(value = "subid") long subid,
                                      @RequestParam(value = "pnid") long pnid,
                                      HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) subid = subordinate.getPid();
            List<ProductPropertyValue> res = productPropertyValueService.getSubAllList(subid, pnid);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
