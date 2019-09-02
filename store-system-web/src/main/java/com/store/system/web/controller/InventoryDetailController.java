package com.store.system.web.controller;

import com.google.common.collect.Maps;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductCategory;
import com.store.system.model.User;
import com.store.system.service.InventoryDetailService;
import com.store.system.service.InventoryWarehouseService;
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
import java.util.Map;

@Controller
@RequestMapping("/inventorydetail")
public class InventoryDetailController extends BaseController {

    @Resource
    private InventoryDetailService inventoryDetailService;
    @Resource
    private InventoryWarehouseService inventoryWarehouseService;

    @RequestMapping("/getPager")
    public ModelAndView getCheckPager(@RequestParam(value = "cid", defaultValue = "0") long cid,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Map<String,Object> res = Maps.newHashMap();
            long wid = 0;
            long subid = 0;
            User user = UserUtils.getUser(request);
            if (user != null) {
                subid = user.getSid();
            }
            List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(subid);
            if (warehouses.size() > 0)
                wid = warehouses.get(0).getId();
            pager = inventoryDetailService.getPager(pager, wid, cid);
            List<ClientInventoryDetail> details = inventoryDetailService.getWaringList(wid, cid);
            res.put("pager",new PagerResult<>(pager));
            res.put("warning",details.size());
            return this.viewNegotiating(request, response, res);
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


    /***
    *  低库存预警列表
    * @Param: [cid, request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/2
    */
    @RequestMapping("/getWaringList")
    public ModelAndView getWaringList(@RequestParam(value = "cid", defaultValue = "0") long cid,
                                      HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            long wid = 0;
            long subid = 0;
            User user = UserUtils.getUser(request);
            if (user != null) {
                subid = user.getSid();
            }
            List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(subid);
            if (warehouses.size() > 0)
                wid = warehouses.get(0).getId();
            List<ClientInventoryDetail> res = inventoryDetailService.getWaringList(wid, cid);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


}
