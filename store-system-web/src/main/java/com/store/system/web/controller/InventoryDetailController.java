package com.store.system.web.controller;

import com.google.common.collect.Maps;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
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

    @RequestMapping("/getSPUDetailPager")
    public ModelAndView getSPUDetailPager(@RequestParam(value = "cid", defaultValue = "0") long cid,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model,
                                      long subid) throws Exception {
        try {
            long wid = 0;
            List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(subid);
            if (warehouses.size() > 0)
                wid = warehouses.get(0).getId();
            pager = inventoryDetailService.getPager(pager, wid, cid);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getSKUDetailList")
    public ModelAndView getSKUDetailList(HttpServletRequest request, HttpServletResponse response, Model model,
                                         @RequestParam(value = "spuid", defaultValue = "0") long spuid, long subid) throws Exception {
        try {
            long wid = 0;
            List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(subid);
            if (warehouses.size() > 0)
                wid = warehouses.get(0).getId();
            List<ClientInventoryDetail> res = inventoryDetailService.getAllList(wid, spuid);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 获取到快过期产品。
     * @param cid
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/getDatedGoods")
    public ModelAndView getDatedGoods(@RequestParam(value = "cid", defaultValue = "0") long cid,
                                      HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
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
            List<ClientInventoryDetail> details = inventoryDetailService.getWaringList(wid, cid);
            List<ClientInventoryDetail> expireList = inventoryDetailService.getExpireList(wid, cid);
            res.put("warning",details.size());
            res.put("expire",expireList.size());
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

    /***
    * 到期预警列表
     * 若距离到期时间在三天之内，则为到期产品
    * @Param: [cid, request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/3
    */
    @RequestMapping("/getExpireList")
    public ModelAndView getExpireList(@RequestParam(value = "cid", defaultValue = "0") long cid,
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
            List<ClientInventoryDetail> res = inventoryDetailService.getExpireList(wid, cid);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }



}
