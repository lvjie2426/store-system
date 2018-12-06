package com.store.system.backend.controller;

import com.store.system.client.ResultClient;
import com.store.system.model.Permission;
import com.store.system.service.PermissionService;
import com.google.common.collect.Lists;
import com.s7.baseFramework.jackson.JsonUtils;
import com.s7.webframework.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */

@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取所有权限
     * @param request
     * @param response
     * @param sid   下级单位的ID（代理ID，学校ID等）
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAll")
    public ModelAndView getAll(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestParam(required = false, value = "sid",defaultValue = "0") long sid,
                                         Model model) throws Exception {
        List<Permission> permissionList = permissionService.getAllList(sid>0?true:false);
        return this.viewNegotiating(request,response,new ResultClient(permissionList));
    }

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response,
                            Permission permission, Model model) throws Exception {
        if(StringUtils.isBlank(permission.getText()))
            return this.viewNegotiating(request, response, new ResultClient("标题不能为空"));
        String hrefList = permission.getHref();
        List<String> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(hrefList)){
            for(String href :  hrefList.split(",")){
                list.add(href);
            }
        }
        permission.setHref(JsonUtils.toJson(list));
        permission = permissionService.add(permission);
        return this.viewNegotiating(request, response, new ResultClient(permission));
    }

    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response,
                               Permission permission, Model model) throws Exception {
        if(permission.getId() == 0) return super.viewNegotiating(request, response, new ResultClient("id不能为空"));
        if(StringUtils.isBlank(permission.getText()))
            return this.viewNegotiating(request, response, new ResultClient("标题不能为空"));
        String hrefList = permission.getHref();
        List<String> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(hrefList)){
            for(String href :  hrefList.split(",")){
                list.add(href);
            }
        }
        permission.setHref(JsonUtils.toJson(list));
        boolean res = permissionService.update(permission);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(required = true, value = "id") long id, Model model) throws Exception {
        if(id == 0) return super.viewNegotiating(request, response, new ResultClient("id不能为空"));
        boolean res = permissionService.del(id);
        return super.viewNegotiating(request, response, new ResultClient(res));
    }

}
