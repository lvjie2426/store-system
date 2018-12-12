package com.store.system.backend.controller;

import com.s7.baseFramework.model.pagination.Pager;
import com.s7.webframework.BaseController;
import com.store.system.client.*;
import com.store.system.exception.GlassesException;
import com.store.system.model.InventoryInBill;
import com.store.system.model.User;
import com.store.system.service.InventoryInBillService;
import com.store.system.service.ProductPropertyService;
import com.store.system.service.ProductService;
import com.store.system.util.UserUtils;
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
@RequestMapping("/inventoryinbill")
public class InventoryInBillController extends BaseController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductPropertyService productPropertyService;

    @Resource
    private InventoryInBillService inventoryInBillService;

    @RequestMapping("/select")
    public ModelAndView select(@RequestParam(value = "type") int type,
                               @RequestParam(value = "subid") long subid,
                               @RequestParam(value = "pid") long pid,
                               @RequestParam(value = "cid") long cid,
                               @RequestParam(value = "bid") long bid,
                               @RequestParam(value = "sid") long sid,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientInventoryInBillSelect res = null;
            ClientProductSPU productSPU = productService.selectSPU(type, subid, pid, cid, bid, sid);
            if(null != productSPU) {
                List<ClientProductProperty> properties = productPropertyService.getSKUProperties(cid);
                res = new ClientInventoryInBillSelect();
                res.setProductSPU(productSPU);
                res.setSkuProperties(properties);
            }
            return this.viewNegotiating(request,response, new ResultClient(res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/add")
    public ModelAndView add(InventoryInBill inventoryInBill, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            inventoryInBill = inventoryInBillService.add(inventoryInBill);
            return this.viewNegotiating(request,response, new ResultClient(inventoryInBill));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(InventoryInBill inventoryInBill, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryInBillService.update(inventoryInBill);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryInBillService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getCreatePager")
    public ModelAndView getCreatePager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        User user = UserUtils.getUser(request);
        long createUid = user.getId();
        pager = inventoryInBillService.getCreatePager(pager, createUid);
        return this.viewNegotiating(request,response, new PagerResult<>(pager));
    }

    @RequestMapping("/getCheckPager")
    public ModelAndView getCheckPager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        pager = inventoryInBillService.getCheckPager(pager);
        return this.viewNegotiating(request,response, new PagerResult<>(pager));
    }

    @RequestMapping("/pass")
    public ModelAndView pass(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long checkUid = user.getId();
            inventoryInBillService.pass(id, checkUid);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/noPass")
    public ModelAndView noPass(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long checkUid = user.getId();
            inventoryInBillService.noPass(id, checkUid);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (GlassesException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
