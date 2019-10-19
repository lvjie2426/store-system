package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryOutBillItem;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.client.ClientProductSPU;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryOutBill;
import com.store.system.service.InventoryDetailService;
import com.store.system.service.InventoryOutBillService;
import com.store.system.service.InventoryWarehouseService;
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

    @RequestMapping("/select")
    public ModelAndView select(@RequestParam(value = "subid") long subid,
                               @RequestParam(value = "pid") long pid,
                               @RequestParam(value = "cid") long cid,
                               @RequestParam(value = "bid") long bid,
                               @RequestParam(value = "sid") long sid,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ClientInventoryDetail> details = Lists.newArrayList();
            ClientProductSPU productSPU = productService.selectSPU(subid, pid, cid, bid, sid);
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




}
