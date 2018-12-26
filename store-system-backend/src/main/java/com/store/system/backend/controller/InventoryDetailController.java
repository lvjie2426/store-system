package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.service.InventoryDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/inventorydetail")
public class InventoryDetailController extends BaseController {

    @Resource
    private InventoryDetailService inventoryDetailService;

    @RequestMapping("/getCheckPager")
    public ModelAndView getCheckPager(@RequestParam(value = "wid") long wid,
                                      @RequestParam(value = "cid", defaultValue = "0") long cid,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        pager = inventoryDetailService.getPager(pager, wid, cid);
        return this.viewNegotiating(request,response, new PagerResult<>(pager));
    }

}
