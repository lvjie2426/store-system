package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.AfterSale;
import com.store.system.bean.InventoryCheckBillItem;
import com.store.system.bean.InventoryOutBillItem;
import com.store.system.client.ClientAfterSaleDetail;
import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.AfterSaleLog;
import com.store.system.model.InventoryOutBill;
import com.store.system.model.OrderSku;
import com.store.system.model.User;
import com.store.system.service.AfterSaleDetailService;
import com.store.system.service.AfterSaleLogService;
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
 * @ClassName AfterSaleController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/13 15:17
 * @Version 1.0
 **/
@Controller
@RequestMapping("/afterSale")
public class AfterSaleController extends BaseController {

    @Resource
    private AfterSaleLogService afterSaleLogService;
    @Resource
    private AfterSaleDetailService afterSaleDetailService;

    /***
    * 获取售后记录
    * @Param: [subid, pager, request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/6/13
    */
    @RequestMapping("/getLogPager")
    public ModelAndView getCheckPager(@RequestParam(value = "subId") long subId,
                                      @RequestParam(required = false,value = "userName",defaultValue = "") String userName,
                                      @RequestParam(required = false,value = "phone",defaultValue = "") String phone,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            pager = afterSaleLogService.getBackPager(pager, subId, userName, phone);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /*** 
    * 进行售后
    * @Param: [afterSale, request, response, model, skuJson]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/6/13
    */ 
    @RequestMapping("/add")
    public ModelAndView add(AfterSale afterSale, HttpServletRequest request, HttpServletResponse response,
                            Model model, @RequestParam(value = "skuJson") String skuJson) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long optId = user.getId();
            List<OrderSku> skus = Lists.newArrayList();
            if(StringUtils.isNotBlank(skuJson)) {
                skus = JsonUtils.fromJson(skuJson, new TypeReference<List<OrderSku>>() {});
            }
            afterSale.setSku(skus);
            afterSale.setOid(optId);
            AfterSaleLog afterSaleLog = afterSaleLogService.add(afterSale);
            return this.viewNegotiating(request,response, new ResultClient(afterSaleLog));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 查看售后
    * @Param: [afterSale, request, response, model, asId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/6/13
    */
    @RequestMapping("/loadDetails")
    public ModelAndView loadDetails(HttpServletRequest request, HttpServletResponse response, Model model,
                                    long asId) throws Exception {
        try {
            List<ClientAfterSaleDetail> res = afterSaleDetailService.getAllList(asId);
            return this.viewNegotiating(request,response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }
}
