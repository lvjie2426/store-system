package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.UserGradeCategoryDiscount;
import com.store.system.service.UserGradeCategoryDiscountService;
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
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-05-15 15:12
 **/
@Controller
@RequestMapping("/gradeDiscount")
public class UserGradeCategoryDiscountController  extends BaseController {

    @Resource
    private UserGradeCategoryDiscountService userGradeCategoryDiscountService;

    @RequestMapping("/addDiscount")
    public ModelAndView addDiscount(HttpServletRequest request, HttpServletResponse response,
                                   Pager pager,
                                   @RequestParam(value = "list", required = false, defaultValue = "")  List<UserGradeCategoryDiscount> list
                                   ,Model model) throws Exception {

//        userGradeCategoryDiscountService.addDiscount(list,null); 暂时用不到这个
        return this.viewNegotiating(request,response,new PagerResult<>(pager));
    }

    @RequestMapping("/load")
    public ModelAndView load(HttpServletRequest request, HttpServletResponse response, Model model,
                             long ugId, long spuId) throws Exception {
        try {
            UserGradeCategoryDiscount dbInfo = userGradeCategoryDiscountService.load(ugId, spuId);
            return this.viewNegotiating(request,response, new ResultClient(dbInfo));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
