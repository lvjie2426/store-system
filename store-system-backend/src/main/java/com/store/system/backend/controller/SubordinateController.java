package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.store.system.bean.InventoryCheckBillItem;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Payment;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.PaymentService;
import com.store.system.service.SubordinateService;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/subordinate")
public class SubordinateController extends BaseController {

    @Autowired
    private SubordinateService subordinateService;
    @Autowired
    private PaymentService paymentService;


    @RequestMapping("/getSubordinatePager")
    public ModelAndView getSubordinatePager(HttpServletRequest request, HttpServletResponse response,
                                       Pager pager, String name, Model model) throws Exception {
        pager = subordinateService.getBackPage(pager, name);
        return this.viewNegotiating(request, response, new PagerResult<>(pager));
    }

    @RequestMapping("/getSubordinate")
    public ModelAndView getSubordinatePager(HttpServletRequest request, HttpServletResponse response,
                                       long sid,
                                       Model model) throws Exception {
        try {
            ClientSubordinate clientSubordinate = subordinateService.load(sid);
            List<ClientSubordinate> list = Lists.newArrayList();
            list.add(clientSubordinate);
            return this.viewNegotiating(request,response,new ResultClient(true,list));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }

    @RequestMapping("/addSubordinate")
    public ModelAndView searchSubordinateCode(HttpServletRequest request, HttpServletResponse response,
                                         Subordinate subordinate,
                                         Model model) throws Exception {
        try {
            subordinate = subordinateService.insert(subordinate);
            return this.viewNegotiating(request,response, new ResultClient(subordinate));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }

    @RequestMapping("/updateSubordinate")
    public ModelAndView updateSubordinate(HttpServletRequest request, HttpServletResponse response,
                                     Subordinate subordinate,
                                     Model model) throws Exception {
        try {
            ResultClient resultClient = new ResultClient(subordinateService.update(subordinate));
            return this.viewNegotiating(request,response,resultClient);
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }

    @RequestMapping("/deleteSubordinate")
    public ModelAndView deleteSubordinate(HttpServletRequest request, HttpServletResponse response,
                                     long id,
                                     Model model) throws Exception {
        try {
            ResultClient resultClient = new ResultClient(subordinateService.delete(id));
            return this.viewNegotiating(request,response,resultClient);
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
    @RequestMapping("/addSubordinateStore")
    public ModelAndView addSubordinateStore(HttpServletRequest request, HttpServletResponse response,
                                              Subordinate subordinate, @RequestParam(value = "storeImgJson") String storeImgJson,
                                              Model model) throws Exception {
        try {
            List<String> images = Lists.newArrayList();
            if(StringUtils.isNotBlank(storeImgJson)) {
                images = JsonUtils.fromJson(storeImgJson, new TypeReference<List<String>>() {});
            }
            subordinate.setStoreImg(images);
            subordinate = subordinateService.insertStore(subordinate);
            return this.viewNegotiating(request,response, new ResultClient(subordinate));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
    @RequestMapping("/updateSubordinateStore")
    public ModelAndView updateSubordinateStore(HttpServletRequest request, HttpServletResponse response,
                                          Subordinate subordinate, @RequestParam(value = "storeImgJson") String storeImgJson,
                                          Model model) throws Exception {
        try {
            List<String> images = Lists.newArrayList();
            if(StringUtils.isNotBlank(storeImgJson)) {
                images = JsonUtils.fromJson(storeImgJson, new TypeReference<List<String>>() {});
            }
            subordinate.setStoreImg(images);
            ResultClient resultClient = new ResultClient(subordinateService.updateStore(subordinate));
            return this.viewNegotiating(request,response,resultClient);
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
    @RequestMapping("/getSubordinateStore")
    public ModelAndView getSubordinateStore(HttpServletRequest request, HttpServletResponse response,
                                            long sid,
                                            Model model) throws Exception {
        try {
            List<ClientSubordinate> list= subordinateService.getTwoLevelAllList(sid);
            return this.viewNegotiating(request,response,new ResultClient(true,list));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
    @RequestMapping("/getSubordinateStoreByName")
    public ModelAndView getSubordinateStoreByName(HttpServletRequest request, HttpServletResponse response,
                                           Pager pager, long sid,String name,
                                            Model model) throws Exception {
        try {
            pager = subordinateService.getSubordinateStoreByName(pager,sid,name);
            return this.viewNegotiating(request,response,new ResultClient(true,pager.getData()));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
    /**
     * create by: zhangmeng
     * description: 企业下门店 开启/关闭
     * create time: 2019/05/15 0015 17:14:50
     *
     * @Param: id
     * @Param: status
     * @Param: request
     * @Param: response
     * @Param: model
     * @return
     */
    @RequestMapping("/updateOpen")
    public ModelAndView updateOpen(@RequestParam(value = "id") long id,
                                   @RequestParam(value = "status") int status, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        try {
            boolean res = subordinateService.updateStatus(id, status);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllSubordinate")
    public ModelAndView getAllSubordinate(HttpServletRequest request,HttpServletResponse response)throws Exception{
        try{
            return this.viewNegotiating(request,response,subordinateService.getAllSubordinate());
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }

    }

    @RequestMapping("/getSubordinateStoreBySid")
    public ModelAndView getSubordinateStoreBySid(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam(value = "sid") long sid, Model model) throws Exception {
        try {
            List<ClientSubordinate> list= subordinateService.getTwoLevelAllList(sid);
            return this.viewNegotiating(request,response,list);
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }

    /*** 
    * 获取当前的加工中心
    * @Param: [request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/5
    */ 
    @RequestMapping("/getProcessList")
    public ModelAndView getProcessList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long psid=0;
            if(user!=null){
                psid=user.getPsid();
            }
            ClientSubordinate subordinate = subordinateService.load(psid);
            return this.viewNegotiating(request,response,new ResultClient(subordinate.getProcess()));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }
}
