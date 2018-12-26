package com.store.system.backend.controller;

import com.store.system.client.ResultClient;
import com.store.system.model.Permission;
import com.store.system.model.Role;
import com.store.system.service.RoleService;
import com.quakoo.webframework.BaseController;
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
 *
 */

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 获取所有角色
     * @param request
     * @param response
     * @param sid 下级单位ID（代理ID，学校ID等）
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAllRoles")
    public ModelAndView getAllRole(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(required = false, value = "sid",defaultValue = "0") long sid,
                                   Model model) throws Exception {
        List<Role> roles = roleService.getAllList(sid);
        return this.viewNegotiating(request, response, new ResultClient(roles));
    }

    @RequestMapping("/getRolePermissions")
    public ModelAndView getRolePermissions(@RequestParam(required = true, value = "rid") long rid,
                                           HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<Permission> permissions = roleService.getRolePermissions(rid);
        return this.viewNegotiating(request, response, new ResultClient(permissions));
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(required = true, value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        boolean res = roleService.del(id);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

    @RequestMapping("/update")
    public ModelAndView update(Role role, @RequestParam(value = "pids[]", required = false, defaultValue = "") List<Long> pids,
                               HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        if(StringUtils.isBlank(role.getRoleName()))
            return super.viewNegotiating(request, response, new ResultClient("角色名不能为空"));
        boolean res = roleService.update(role);
        if(res) roleService.updateRolePermission(role.getId(), pids);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

    @RequestMapping("/add")
    public ModelAndView add(Role role, @RequestParam(value = "pids[]", required = false, defaultValue = "") List<Long> pids,
                            HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        if(StringUtils.isBlank(role.getRoleName()))
            return super.viewNegotiating(request, response, new ResultClient("角色名不能为空"));
        role = roleService.add(role);
        if(null != role) {
            long rid = role.getId();
            roleService.updateRolePermission(rid, pids);
        }
        return this.viewNegotiating(request, response, new ResultClient(role));
    }

}


