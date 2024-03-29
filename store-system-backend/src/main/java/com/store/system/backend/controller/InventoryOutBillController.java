package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryOutBillItem;
import com.store.system.client.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryOutBill;
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
@RequestMapping("/inventoryOutBill")
public class InventoryOutBillController extends BaseController {

    @Resource
    private ProductService productService;

    @Resource
    private InventoryDetailService inventoryDetailService;

    @Resource
    private InventoryOutBillService inventoryOutBillService;

    @Resource
    private InventoryWarehouseService inventoryWarehouseService;

    @Resource
    private SubordinateService subordinateService;

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
            List<ClientInventoryDetail> details = Lists.newArrayList();
            ClientProductSPU productSPU = productService.selectSPU(pSubid, pid, cid, bid, sid);
            if(null != productSPU) {
                List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(subid);
                if(warehouses.size()>0){
                    long wid = warehouses.get(0).getId();
                    details = inventoryDetailService.getAllList(wid, productSPU.getId());
                }
            }
            return this.viewNegotiating(request,response, new ResultClient(true, details));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/add")
    public ModelAndView add(InventoryOutBill inventoryOutBill, HttpServletRequest request, HttpServletResponse response,
                            Model model, @RequestParam(value = "itemsJson") String itemsJson) throws Exception {
        try {
            List<InventoryOutBillItem> billItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                billItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryOutBillItem>>() {});
            }
            List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(inventoryOutBill.getSubid());
            if(warehouses.size()>0)
                inventoryOutBill.setWid(warehouses.get(0).getId());
            inventoryOutBill.setItems(billItems);
            inventoryOutBill = inventoryOutBillService.add(inventoryOutBill);
            return this.viewNegotiating(request,response, new ResultClient(inventoryOutBill));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(InventoryOutBill inventoryOutBill, HttpServletRequest request, HttpServletResponse response,
                               Model model, @RequestParam(value = "itemsJson") String itemsJson) throws Exception {
        try {
            List<InventoryOutBillItem> billItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                billItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryOutBillItem>>() {});
            }
            inventoryOutBill.setItems(billItems);
            boolean res = inventoryOutBillService.update(inventoryOutBill);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryOutBillService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/submit")
    public ModelAndView submit(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryOutBillService.submit(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getCreatePager")
    public ModelAndView getCreatePager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        User user = UserUtils.getUser(request);
        long createUid = user.getId();
        pager = inventoryOutBillService.getCreatePager(pager, createUid);
        return this.viewNegotiating(request,response, new PagerResult<>(pager));
    }

    @RequestMapping("/getCheckPager")
    public ModelAndView getCheckPager(@RequestParam(value = "subid") long subid,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        pager = inventoryOutBillService.getCheckPager(pager, subid);
        return this.viewNegotiating(request,response, new PagerResult<>(pager));
    }

    @RequestMapping("/pass")
    public ModelAndView pass(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long checkUid = user.getId();
            inventoryOutBillService.pass(id, checkUid);
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
            inventoryOutBillService.noPass(id, checkUid);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    // 获取医疗器械 所有出库单
    @RequestMapping("/getAllPager")
    public ModelAndView getAllPager(@RequestParam(value = "subid") long subid,
                                    @RequestParam(value = "type") int type,
                                    String licenceNum,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        pager = inventoryOutBillService.getAllPager(pager, subid,type,licenceNum);
        return this.viewNegotiating(request,response, new PagerResult<>(pager));
    }

}
