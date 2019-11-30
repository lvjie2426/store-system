package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.SalePresentItem;
import com.store.system.client.ClientSpendCardExchange;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.SpendCardExchange;
import com.store.system.service.SpendCardExchangeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName SpendCardExchangeController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 11:40
 * @Version 1.0
 **/
@Controller
@RequestMapping("/spendCardExchange")
public class SpendCardExchangeController extends BaseController{

    @Resource
    private SpendCardExchangeService spendCardExchangeService;

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, SpendCardExchange exchange,
                            String subIdsJson) throws Exception {
        try {
            List<Long> subIds = Lists.newArrayList();
            if(StringUtils.isNotBlank(subIdsJson)) {
                subIds = JsonUtils.fromJson(subIdsJson, new TypeReference<List<Long>>() {});
            }
            exchange.setSubIds(subIds);
            SpendCardExchange res = spendCardExchangeService.add(exchange);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response, long id) throws Exception {
        try {
            boolean res = spendCardExchangeService.delete(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, SpendCardExchange exchange,
                               String subIdsJson) throws Exception {
        try {
            List<Long> subIds = Lists.newArrayList();
            if(StringUtils.isNotBlank(subIdsJson)) {
                subIds = JsonUtils.fromJson(subIdsJson, new TypeReference<List<Long>>() {});
            }
            exchange.setSubIds(subIds);
            boolean res = spendCardExchangeService.update(exchange);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response,
                                   long psid, long spuId) throws Exception {
        try {
            List<ClientSpendCardExchange> res = spendCardExchangeService.getAllList(psid, spuId);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
