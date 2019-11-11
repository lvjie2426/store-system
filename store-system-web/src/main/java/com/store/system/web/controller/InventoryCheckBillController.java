package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryCheckBillItem;
import com.store.system.client.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryCheckBill;
import com.store.system.model.InventoryDetail;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inventoryCheckBill")
public class InventoryCheckBillController extends BaseController {

    @Resource
    private ProductService productService;

    @Resource
    private InventoryDetailService inventoryDetailService;

    @Resource
    private InventoryCheckBillService inventoryCheckBillService;

    @Resource
    private SubordinateService subordinateService;

    @Resource
    private InventoryWarehouseService inventoryWarehouseService;

    @RequestMapping("/add")
    public ModelAndView add(InventoryCheckBill inventoryCheckBill,
                            @RequestParam(value = "subids[]", required = false, defaultValue = "") List<Long> subids,
                            @RequestParam(value = "itemsJson") String itemsJson,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<InventoryCheckBillItem> billItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                billItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryCheckBillItem>>() {});
            }
            List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(inventoryCheckBill.getSubid());
            if(warehouses.size()>0)
                inventoryCheckBill.setWid(warehouses.get(0).getId());
            inventoryCheckBill.setItems(billItems);
            inventoryCheckBillService.add(inventoryCheckBill,subids);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    //根据类目id 获取产品
    @RequestMapping("/selectDetailsByCid")
    public ModelAndView selectDetailsByCid(@RequestParam(value = "subid") long subid,
                                           @RequestParam(value = "cid") long cid,
                                           HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            long pSubid = subordinate.getPid();
            if (pSubid == 0) throw new StoreSystemException("店铺为空");
            List<ClientInventoryCheckBillSelect> res = Lists.newArrayList();
            List<ClientProductSPU> spuList = productService.selectSPU(pSubid, cid);
            for (ClientProductSPU productSPU : spuList) {
                ClientInventoryCheckBillSelect select = new ClientInventoryCheckBillSelect();
                if (null != productSPU) {
                    select = new ClientInventoryCheckBillSelect();
                    select.setProductSPU(productSPU);
                    int currentNum = 0;
                    List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(subid);
                    long wid = 0;
                    if (warehouses.size() > 0) {
                        wid = warehouses.get(0).getId();
                    }
                    List<ClientInventoryDetail> details = inventoryDetailService.getAllList(wid, productSPU.getId());
                    for (InventoryDetail detail : details) {
                        currentNum += detail.getNum();
                    }
                    select.setCurrentNum(currentNum);
                    select.setDetails(details);
                }
                res.add(select);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


    @RequestMapping("/update")
    public ModelAndView update(InventoryCheckBill inventoryCheckBill, HttpServletRequest request, HttpServletResponse response, Model model,
                               @RequestParam(value = "itemsJson") String itemsJson) throws Exception {
        try {
            List<InventoryCheckBillItem> billItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                billItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryCheckBillItem>>() {});
            }
            inventoryCheckBill.setItems(billItems);
            boolean res = inventoryCheckBillService.update(inventoryCheckBill);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }


    @RequestMapping("/submit")
    public ModelAndView submit(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryCheckBillService.submit(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/load")
    public ModelAndView load(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ClientInventoryCheckBill> list = inventoryCheckBillService.load(id);
            ClientInventoryCheckBill res = null;
            if(list.size()>0){
                res=list.get(0);
            }
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    //保存盘点
    @RequestMapping("/save")
    public ModelAndView save(InventoryCheckBill inventoryCheckBill, HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam(value = "itemsJson") String itemsJson) throws Exception {
        try {
            List<InventoryCheckBillItem> billItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                billItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryCheckBillItem>>() {});
            }
            inventoryCheckBill.setItems(billItems);
            List<ClientInventoryWarehouse> warehouses = inventoryWarehouseService.getAllList(inventoryCheckBill.getSubid());
            if(warehouses.size()>0)
                inventoryCheckBill.setWid(warehouses.get(0).getId());
            boolean res = inventoryCheckBillService.save(inventoryCheckBill);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * create by: zhangmeng
     * description: 员工端获取全部盘点任务 包括已完成的。
     * create time: 2019/09/02 0002 14:51:44
     *
     * @Param: subid
     * @Param: pager
     * @Param: request
     * @Param: response
     * @Param: model
     * @return
     */
    @RequestMapping("/getAll")
    public ModelAndView getCheckPager(@RequestParam(value = "subid") long subid,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            pager = inventoryCheckBillService.getWebCheckPager(pager, subid);
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getWebCreatePager")
    public ModelAndView getWebCreatePager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            pager = inventoryCheckBillService.getWebCheckPager(pager, user.getId());
            return this.viewNegotiating(request, response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }



}
