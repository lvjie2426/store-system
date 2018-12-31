package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryWarehouse;
import com.store.system.service.InventoryWarehouseService;
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
@RequestMapping("/inventorywarehouse")
public class InventoryWarehouseController extends BaseController {

    @Resource
    private InventoryWarehouseService inventoryWarehouseService;

    @RequestMapping("/add")
    public ModelAndView add(InventoryWarehouse inventoryWarehouse, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            inventoryWarehouse = inventoryWarehouseService.add(inventoryWarehouse);
            return this.viewNegotiating(request,response, new ResultClient(inventoryWarehouse));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(InventoryWarehouse inventoryWarehouse, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryWarehouseService.update(inventoryWarehouse);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryWarehouseService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "subid") long subid,
                                   HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ClientInventoryWarehouse> res = inventoryWarehouseService.getAllList(subid);
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

}
