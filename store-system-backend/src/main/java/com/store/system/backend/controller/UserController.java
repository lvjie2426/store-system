package com.store.system.backend.controller;


import com.store.system.client.ClientUserOnLogin;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.GlassesException;
import com.store.system.model.Permission;
import com.store.system.model.User;
import com.store.system.service.PermissionService;
import com.store.system.service.UserService;
import com.store.system.util.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.webframework.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @RequestMapping("/getTree")
    public ModelAndView getTree(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        User user = UserUtils.getUser(request);
        long uid = user.getId();
        List<Permission> permissions = userService.getUserPermissions(uid);
        Map<Long, List<Permission>> res = Maps.newHashMap();
        for(Permission permission : permissions) {
            if(permission.getMenu()==Permission.menu_no){
                continue;
            }
            long pid = permission.getPid();
            List<Permission>  subPermissions = res.get(pid);
            if(null == subPermissions) {
                subPermissions = Lists.newArrayList();
                res.put(pid, subPermissions);
            }
            subPermissions.add(permission);
        }
        List<Permission> rootPermissions = res.get(0l);
        if(null != rootPermissions) {
            for(Iterator<Permission> it = rootPermissions.iterator(); it.hasNext();) {
                if(!res.keySet().contains(it.next().getId())) it.remove();
            }
        }
        for(List<Permission> one : res.values()) {
            Collections.sort(one);
        }
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

    @RequestMapping("/getUserPermissions")
    public ModelAndView getUserPermissions(@RequestParam(required = true, value = "uid") long uid,
                                           HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<Permission> permissions = userService.getUserPermissions(uid);
        return this.viewNegotiating(request, response, new ResultClient(permissions));
    }

    @RequestMapping("/searchUser")
    public ModelAndView searchUser(HttpServletRequest request, HttpServletResponse response,
                                   Pager pager, long startTime, long endTime,
                                   @RequestParam(required = false, value = "phone",defaultValue = "") String phone,
                                   @RequestParam(required = false, value = "name",defaultValue = "") String name,
                                   @RequestParam(required = false, value = "userName",defaultValue = "") String userName,
                                   @RequestParam(required = false, value = "sid",defaultValue = "0") long sid,
                                   @RequestParam(required = false, value = "userType",defaultValue = "0") int userType,
                                   @RequestParam(required = false, value = "rid",defaultValue = "0") long rid,
                                   @RequestParam(required = false, value = "status",defaultValue = "-1") int status,
                                   Model model) throws Exception {
        pager= userService.searchUser(pager,sid,userType,name,phone,userName,rid,status,startTime,endTime);
        return this.viewNegotiating(request,response,new PagerResult<>(pager));
    }

    @RequestMapping("/add")
    public ModelAndView add(User user, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientUserOnLogin clientUserOnLogin = userService.register(user);
            return this.viewNegotiating(request, response, new ResultClient(clientUserOnLogin));
        }catch (GlassesException e){
            return this.viewNegotiating(request,response,new ResultClient(false,"",e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(User user,
                               @RequestParam(value = "updateRids", required = false, defaultValue = "") List<Long> updateRids,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        boolean res = userService.update(user,updateRids);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

    @RequestMapping("/updateStatus")
    public ModelAndView del(@RequestParam(required = true, value = "id") long id,
                            int status,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        boolean res = userService.updateStatus(id,status);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

    @RequestMapping("/updateUserPermissions")
    public ModelAndView updateUserPermissions(@RequestParam(value = "uid", required = false) long uid,
                                              @RequestParam(value = "pids", required = false, defaultValue = "") List<Long> pids,
                                              HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        if(uid == 0) return super.viewNegotiating(request, response, new ResultClient("用户ID不能为空"));
        boolean res = userService.updateUserPermission(uid, pids);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

}

