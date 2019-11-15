package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Company;
import com.store.system.service.CompanyService;
import com.store.system.service.DictionaryService;
import com.store.system.util.DictionaryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @create: 2019-08-12 11:41
 **/
@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {



    @Autowired
    private DictionaryService dictionaryService;

    @Resource
    private CompanyService companyService;
    // 新增医疗器械企业
    @RequestMapping("/saveCompany")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response,
                                   Company company) throws Exception {
        try {

            return this.viewNegotiating(request, response, new ResultClient(companyService.insert(company)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    // 编辑医疗器械企业
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response,
                                  Company company) throws Exception {
        try {

            return this.viewNegotiating(request, response, new ResultClient(companyService.update(company)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    // 获取全部 医疗器械企业
    @RequestMapping("/search")
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response,Pager pager) throws Exception {
        try {
            pager=companyService.getAll(pager);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    // 获取到期 医疗器械企业
    @RequestMapping("/searchOverdue")
    public ModelAndView searchOverdue(HttpServletRequest request, HttpServletResponse response,Pager pager) throws Exception {
        try {
            pager=companyService.getOverdue(pager);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    //获取供应商/生产商 用于添加商品处
    @RequestMapping("/getMp")
    public ModelAndView getMp(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "mpId") int mpId) throws Exception {
        try {

           List<Company> companyList= companyService.getMp(mpId);
            return this.viewNegotiating(request, response, new ResultClient(companyList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    //审核供应商
    @RequestMapping("/checkStatus")
    public ModelAndView checkStatus(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(required = true, value = "ids[]", defaultValue = "") List<Long> ids) throws Exception {
        try {

          boolean flag= companyService.checkStatus(ids);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }
    //获取经营范围
    @RequestMapping("/getRange")
    public ModelAndView getRange(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String res [] = {};
            res = dictionaryService.getStrings(DictionaryUtils.range,null);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

}
