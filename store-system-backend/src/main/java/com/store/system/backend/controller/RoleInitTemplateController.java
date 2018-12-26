package com.store.system.backend.controller;

import com.store.system.client.ResultClient;
import com.store.system.model.RoleInitTemplate;
import com.store.system.service.RoleInitTemplateService;
import com.quakoo.webframework.BaseController;
import org.apache.commons.lang.StringUtils;
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

/**
 *
 */

@Controller
@RequestMapping("/roleinittemplate")
public class RoleInitTemplateController extends BaseController {

    @Resource
    private RoleInitTemplateService roleInitTemplateService;

    /**
     * 获取所有角色模板
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAllRoleInitTemplates")
    public ModelAndView getAllRole(HttpServletRequest request, HttpServletResponse response,
                                   Model model) throws Exception {
        List<RoleInitTemplate> roles = roleInitTemplateService.getAll();
        return this.viewNegotiating(request, response, new ResultClient(roles));
    }

    @RequestMapping("/getAllRoleInitTemplatesByName")
    public ModelAndView getAllRoleInitTemplatesByName(HttpServletRequest request, HttpServletResponse response,String name) throws Exception {
        List<RoleInitTemplate> roles = roleInitTemplateService.getAll();
        List<RoleInitTemplate> result=new ArrayList<>();
        for (RoleInitTemplate role:roles){
            if(StringUtils.isBlank(name)||role.getName().contains(name)){
                result.add(role);
            }
        }
        return this.viewNegotiating(request,response,result);
    }


    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(required = true, value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        boolean sign = roleInitTemplateService.del(id);
        return this.viewNegotiating(request, response, new ResultClient(sign));
    }

    @RequestMapping("/update")
    public ModelAndView update(RoleInitTemplate roleInitTemplate,
                               HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        boolean sign = roleInitTemplateService.update(roleInitTemplate);
        return this.viewNegotiating(request, response, new ResultClient(sign));
    }

    @RequestMapping("/add")
    public ModelAndView add(RoleInitTemplate roleInitTemplate,
                            HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        roleInitTemplate = roleInitTemplateService.add(roleInitTemplate);
        return this.viewNegotiating(request, response, new ResultClient(roleInitTemplate));
    }


}


