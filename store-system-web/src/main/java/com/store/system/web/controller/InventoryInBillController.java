package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryInBillItem;
import com.store.system.client.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryInBill;
import com.store.system.model.User;
import com.store.system.service.*;
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
import java.util.List;

@Controller
@RequestMapping("/inventoryinbill")
public class InventoryInBillController extends BaseController {

    @Resource
    private ProductService productService;

    @Resource
    private SubordinateService subordinateService;

    @Resource
    private InventoryInBillService inventoryInBillService;

    @Resource
    private InventoryWarehouseService inventoryWarehouseService;

    /**
     * 获取一个商品的SPU，返回需要确定的所有SKU属性
     * method_name: select
     * params: [type, subid, pid, cid, bid, sid, request, response, model]
     * return: org.springframework.web.servlet.ModelAndView
     * creat_user: lihao
     * creat_date: 2019/3/2
     * creat_time: 14:54
     **/
    @RequestMapping("/select")
    public ModelAndView select(@RequestParam(value = "subid") long subid,
                               @RequestParam(value = "pid") long pid,
                               @RequestParam(value = "cid") long cid,
                               @RequestParam(value = "bid") long bid,
                               @RequestParam(value = "sid") long sid,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("店铺为空");
            ClientInventoryInBillSelect res = new ClientInventoryInBillSelect();
            ClientProductSPU productSPU = productService.selectSPU(pSubid, pid, cid, bid, sid);
            List<ClientProductSKU> skuList =  productService.getSaleSKUAllList(subid,productSPU.getId(),0);
            if(skuList.size()>0) res.setSkuList(skuList);
            return this.viewNegotiating(request,response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 添加一个入库单(item属性是sku相关参数对象的JSON结构)
     * method_name: add
     * params: [inventoryInBill, request, response, model]
     * return: org.springframework.web.servlet.ModelAndView
     * creat_user: lihao
     * creat_date: 2019/3/2
     * creat_time: 14:56
     **/
    @RequestMapping("/add")
    public ModelAndView add(InventoryInBill inventoryInBill, HttpServletRequest request, HttpServletResponse response, Model model,
                            @RequestParam(value = "itemsJson") String itemsJson) throws Exception {
        try {
            List<InventoryInBillItem> billItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                billItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryInBillItem>>() {});
            }
            List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(inventoryInBill.getSubid());
            if(warehouses.size()>0)
                inventoryInBill.setWid(warehouses.get(0).getId());
            inventoryInBill.setItems(billItems);
            inventoryInBill = inventoryInBillService.add(inventoryInBill);
            return this.viewNegotiating(request,response, new ResultClient(inventoryInBill));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(InventoryInBill inventoryInBill, HttpServletRequest request, HttpServletResponse response, Model model,
                               @RequestParam(value = "itemsJson") String itemsJson) throws Exception {
        try {
            List<InventoryInBillItem> billItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                billItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryInBillItem>>() {});
            }
            inventoryInBill.setItems(billItems);
            boolean res = inventoryInBillService.update(inventoryInBill);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryInBillService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/submit")
    public ModelAndView submit(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryInBillService.submit(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 员工端入库单列表
    * @Param: [pager, request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/8/27
    */
    @RequestMapping("/getCreatePager")
    public ModelAndView getCreateWebPager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long createUid = user.getId();
            pager = inventoryInBillService.getCreateWebPager(pager, createUid);
            return this.viewNegotiating(request,response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getCheckPager")
    public ModelAndView getCheckWebPager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long subid = user.getSid();
            pager = inventoryInBillService.getCheckWebPager(pager, subid);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/pass")
    public ModelAndView pass(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long checkUid = user.getId();
            inventoryInBillService.pass(id, checkUid);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (StoreSystemException e) {
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
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }



}
