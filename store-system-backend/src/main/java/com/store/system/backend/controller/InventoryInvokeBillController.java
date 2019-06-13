package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryInvokeBillItem;
import com.store.system.bean.InventoryOutBillItem;
import com.store.system.client.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryInvokeBill;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.model.UserGradeCategoryDiscount;
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

/**
 * @ProjectName: store-system
 * @Package: com.store.system.backend.controller
 * @ClassName: InventoryInvokeBillController
 * @Author: LiHaoJie
 * @Description: 调货模块
 * @Date: 2019/6/5 15:06
 * @Version: 1.0
 */
@Controller
@RequestMapping("/inventoryInvokeBill")
public class InventoryInvokeBillController extends BaseController{

    @Resource
    private SubordinateService subordinateService;

    @Resource
    private InventoryInvokeBillService inventoryInvokeBillService;

    @Resource
    private ProductService productService;

    @Resource
    private InventoryWarehouseService inventoryWarehouseService;

    @Resource
    private InventoryDetailService inventoryDetailService;

    /***
    * 获取其他门店得一个商品的SPU，返回需要确定的所有SKU属性
    * @Param: [type, subid, pid, cid, bid, sid, request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/6/13
    */
    @RequestMapping("/select")
    public ModelAndView select(@RequestParam(value = "type") int type,
                               @RequestParam(value = "subid") long subid,
                               @RequestParam(value = "pid") long pid,
                               @RequestParam(value = "cid") long cid,
                               @RequestParam(value = "bid") long bid,
                               @RequestParam(value = "sid") long sid,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ClientInventoryDetail> details = Lists.newArrayList();
            Subordinate subotdinate = subordinateService.load(subid);
            long psid = subotdinate.getPid();
            if(psid==0){ throw new StoreSystemException("门店ID错误!"); }
            ClientProductSPU productSPU = productService.selectSPU(type, psid, pid, cid, bid, sid);
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


    //新增调货单
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request,HttpServletResponse response,
                            @RequestParam(value = "itemsJson") String itemsJson,
                               InventoryInvokeBill inventoryInvokeBill)throws Exception{
        try {
            List<InventoryInvokeBillItem> billItems = Lists.newArrayList();
            try {
                if(StringUtils.isNotBlank(itemsJson)) {
                    billItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryInvokeBillItem>>() {});
                    inventoryInvokeBill.setItems(billItems);
                }
            } catch (Exception e) {
                throw new StoreSystemException("子条目格式错误！");
            }

            return this.viewNegotiating(request,response,new ResultClient(true,inventoryInvokeBillService.add(inventoryInvokeBill)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //编辑调货单
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request,HttpServletResponse response,
                            InventoryInvokeBill inventoryInvokeBill)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,inventoryInvokeBillService.update(inventoryInvokeBill)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //删除调货单
    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request,HttpServletResponse response,
                            @RequestParam(value = "id") long id)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,inventoryInvokeBillService.del(id)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //提交进入审核状态
    @RequestMapping("/submit")
    public ModelAndView submit(HttpServletRequest request,HttpServletResponse response,
                                @RequestParam(value = "id") long id)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,inventoryInvokeBillService.submit(id)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //审核通过调货单
    @RequestMapping("/pass")
    public ModelAndView pass(HttpServletRequest request,HttpServletResponse response,
                             @RequestParam(value = "id") long id,
                             @RequestParam(value = "outUid") long outUid)throws Exception{
        try {
            User user = UserUtils.getUser(request);
            long checkUid = user.getId();
            inventoryInvokeBillService.pass(id,checkUid,outUid);
            return this.viewNegotiating(request,response,new ResultClient(true));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(true,s.getMessage()));
        }
    }

    //审核不通过调货单
    @RequestMapping("/noPass")
    public ModelAndView noPass(HttpServletRequest request,HttpServletResponse response,
                             @RequestParam(value = "id") long id)throws Exception{
        try {
            User user = UserUtils.getUser(request);
            long checkUid = user.getId();
            inventoryInvokeBillService.noPass(id,checkUid);
            return this.viewNegotiating(request,response,new ResultClient(true));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(true,s.getMessage()));
        }
    }


    //获取某个用户创建的调货单
    @RequestMapping("/getCreatePager")
    public ModelAndView getCreatePager(HttpServletRequest request,HttpServletResponse response, Pager pager,
                                       @RequestParam(value = "createUid") long createUid)throws Exception{
        try {
            pager = inventoryInvokeBillService.getCreatePager(pager,createUid);
            return this.viewNegotiating(request,response,new PagerResult<>(pager));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //获取某个门店下 编辑状态的调货单
    @RequestMapping("/getCheckPager")
    public ModelAndView getCheckPager(HttpServletRequest request, HttpServletResponse response, Pager pager,
                                      @RequestParam(value = "subid") long subid)throws Exception{
        try {
            Subordinate subotdinate = subordinateService.load(subid);
            long pid = subotdinate.getPid();
            if(pid==0){ throw new StoreSystemException("门店ID错误!"); }
            pager = inventoryInvokeBillService.getCheckPager(pager,subid);
            return this.viewNegotiating(request,response,new PagerResult<>(pager));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
}
