package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryInvokeBill;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.InventoryInvokeBillService;
import com.store.system.service.SubordinateService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


    //新增调货单
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request,HttpServletResponse response,
                               InventoryInvokeBill inventoryInvokeBill)throws Exception{
        try {
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
