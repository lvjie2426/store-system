package com.store.system.backend.controller;

import com.store.system.client.ResultClient;
import com.store.system.model.Permission;
import com.store.system.model.RoleTemplateItem;
import com.store.system.service.RoleTemplateItemService;
import com.quakoo.webframework.BaseController;
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
@RequestMapping("/roletemplateitem")
public class RoleTemplateItemController extends BaseController {

    @Resource
    private RoleTemplateItemService roleTemplateItemService;

    /**
     * 获取角色模板的所有角色
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAllRoleTemplateItems")
    public ModelAndView getAllRole(long roleInitTemplateId,
            HttpServletRequest request, HttpServletResponse response,
                                   Model model) throws Exception {
        List<RoleTemplateItem> roles = roleTemplateItemService.getAll(roleInitTemplateId);
        return this.viewNegotiating(request, response, new ResultClient(roles));
    }
    @RequestMapping("/getRolePermissions")
    public ModelAndView getRolePermissions(@RequestParam(required = true, value = "rid") long rid,
                                           HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<Permission> tempResult =roleTemplateItemService.getRolePermissions(rid);
        List<Permission> permissions =new ArrayList<>();
        for(Permission permission:tempResult){
            if(permission.getSubordinate()==Permission.subordinate_on){
                permissions.add(permission);
                //只给前端传递下属单位的权限
            }
        }
        return this.viewNegotiating(request, response, new ResultClient(permissions));
    }
    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(required = true, value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        boolean sign = roleTemplateItemService.del(id);
        return this.viewNegotiating(request, response, new ResultClient(sign));
    }

    @RequestMapping("/update")
    public ModelAndView update(RoleTemplateItem roleTemplateItem, 
                               HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        roleTemplateItem.setPids(roleTemplateItem.getPids());
        boolean sign = roleTemplateItemService.update(roleTemplateItem);
        return this.viewNegotiating(request, response, new ResultClient(sign));
    }

    @RequestMapping("/add")
    public ModelAndView add(RoleTemplateItem roleTemplateItem,
                            HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        roleTemplateItem = roleTemplateItemService.add(roleTemplateItem);
        return this.viewNegotiating(request, response, new ResultClient(roleTemplateItem));
    }

}


