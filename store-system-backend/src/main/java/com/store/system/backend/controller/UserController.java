package com.store.system.backend.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.store.system.bean.Result;
import com.store.system.client.ClientUser;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ExportUser;
import com.store.system.model.Permission;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.ImportFileService;
import com.store.system.service.PermissionService;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserService;
import com.store.system.util.FileDownload;
import com.store.system.util.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

    @Resource
    private SubordinateService subordinateService;

    @Resource
    private ImportFileService importFileService;

    @Resource
    private FileDownload download;

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
                                   Pager pager,
                                   @RequestParam(required = false,value = "startTime",defaultValue = "-1") long startTime,
                                   @RequestParam(required = false,value = "endTime",defaultValue = "-1") long endTime,
                                   @RequestParam(required = false, value = "phone",defaultValue = "") String phone,
                                   @RequestParam(required = false, value = "name",defaultValue = "") String name,
                                   @RequestParam(required = false, value = "userName",defaultValue = "") String userName,
                                   @RequestParam(required = false, value = "sid",defaultValue = "-1") long sid,
                                   @RequestParam(required = false, value = "subid",defaultValue = "-1") long subid,
                                   @RequestParam(required = false, value = "userType",defaultValue = "-1") int userType,
                                   @RequestParam(required = false, value = "rid",defaultValue = "0") long rid,
                                   @RequestParam(required = false, value = "status",defaultValue = "-1") int status,
                                   Model model) throws Exception {
        pager= userService.searchUser(pager,sid,subid,userType,name,phone,userName,rid,status,startTime,endTime);
        return this.viewNegotiating(request,response,new PagerResult<>(pager));
    }

    @RequestMapping("/searchBackendUser")
    public ModelAndView searchBackendUser(HttpServletRequest request, HttpServletResponse response,
                                   Pager pager,
                                   @RequestParam(required = false,value = "startTime",defaultValue = "-1") long startTime,
                                   @RequestParam(required = false,value = "endTime",defaultValue = "-1") long endTime,
                                   @RequestParam(required = false, value = "phone",defaultValue = "") String phone,
                                   @RequestParam(required = false, value = "name",defaultValue = "") String name,
                                   @RequestParam(required = false, value = "userName",defaultValue = "") String userName,
                                   @RequestParam(required = false, value = "sid",defaultValue = "-1") long sid,
                                   @RequestParam(required = false, value = "subid",defaultValue = "-1") long subid,
                                   @RequestParam(required = false, value = "userType",defaultValue = "-1") int userType,
                                   @RequestParam(required = false, value = "rid",defaultValue = "0") long rid,
                                   @RequestParam(required = false, value = "status",defaultValue = "-1") int status,
                                   Model model) throws Exception {
        pager= userService.searchUser(pager,sid,subid,userType,name,phone,userName,rid,status,startTime,endTime);
        return this.viewNegotiating(request,response,new PagerResult<>(pager));
    }

    @RequestMapping("/add")
    public ModelAndView add(User user, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            user.setPassword("123456");
            ClientUserOnLogin clientUserOnLogin = userService.register(user);
            return this.viewNegotiating(request, response, new ResultClient(clientUserOnLogin));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,"",e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(User user,
                               @RequestParam(value = "updateRids[]", required = false, defaultValue = "") List<Long> updateRids,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        boolean res = userService.update(user,updateRids);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

    @RequestMapping("/updateUserPermissions")
    public ModelAndView updateUserPermissions(@RequestParam(value = "uid", required = false) long uid,
                                              @RequestParam(value = "pids[]", required = false, defaultValue = "") List<Long> pids,
                                              HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        if(uid == 0) return super.viewNegotiating(request, response, new ResultClient("用户ID不能为空"));
        boolean res = userService.updateUserPermission(uid, pids);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }


    /////////////////////顾客相关////////////////////////

    @RequestMapping("/addCustomer")
    public ModelAndView addCustomer(User user, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            user = userService.addCustomer(user);
            return this.viewNegotiating(request,response, new ResultClient(user));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateCustomer")
    public ModelAndView updateCustomer(User user, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = userService.updateCustomer(user);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delCustomer")
    public ModelAndView delCustomer(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = userService.delCustomer(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getCustomerPager")
    public ModelAndView getCustomerPager(Pager pager, @RequestParam(value = "subid") long subid,
                                         @RequestParam(value = "userType") int userType,
                                         HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(subid);
            long pSubid = subordinate.getPid();
            if(pSubid != 0) throw new StoreSystemException("公司ID错误");
            pager = userService.getBackCustomerPager(pager, subid, userType);
            return this.viewNegotiating(request,response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getSubCustomerPager")
    public ModelAndView getSubCustomerPager(Pager pager, @RequestParam(value = "subid") long subid,
                                            @RequestParam(value = "userType") int userType,
                                            @RequestParam(required = false, value = "phone",defaultValue = "") String phone,
                                            @RequestParam(required = false, value = "phone1",defaultValue = "") String phone1,
                                            @RequestParam(required = false, value = "name",defaultValue = "") String name,
                                            @RequestParam(required = false, value = "name1",defaultValue = "") String name1,
                                            @RequestParam(required = false,value = "sex",defaultValue = "-1") int sex,
                                            @RequestParam(required = false,value = "job",defaultValue = "") String job,
                                            @RequestParam(required = false,value = "userGradeId",defaultValue = "0") long userGradeId,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(subid);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("分店ID错误");
            pager = userService.getBackSubCustomerPager(pager, subid,phone,phone1,name,name1,sex,userType,job,userGradeId);
            return this.viewNegotiating(request,response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    //获取所有 顾客/员工 的职业
    @RequestMapping("/getAllUserJob")
    public ModelAndView getAllUserJob(HttpServletRequest request,HttpServletResponse response,
                                      @RequestParam(value = "sid") long sid,
                                      @RequestParam(value = "userType",defaultValue = "0") int userType)throws Exception{
        try{
            Subordinate subordinate = subordinateService.load(sid);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("门店ID错误");
            return this.viewNegotiating(request,response,new ResultClient(true,userService.getAllUserJob(sid,userType)));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response, new ResultClient(false,e.getMessage()));
        }
    }

    //获取门店下所有 顾客/员工 下拉列表
    @RequestMapping("/getAllUser")
    public ModelAndView getAllUser(HttpServletRequest request,HttpServletResponse response,
                                  @RequestParam(value = "sid") long sid,
                                  @RequestParam(value = "userType") int userType)throws Exception{
        try {
            Subordinate subordiante = subordinateService.load(sid);
            long subid = subordiante.getPid();
            if(subid==0)throw new StoreSystemException("门店ID错误!");
            return this.viewNegotiating(request,response,new ResultClient(userService.getAllUser(sid,userType)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(s.getMessage()));
        }
    }

    /////////////////////员工相关////////////////////////
    @RequestMapping("/updateUser")
    public ModelAndView updateUser(HttpServletRequest request,HttpServletResponse response,
                                   User user)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,userService.updateUser(user)));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }
    }

    @RequestMapping("/updateStatus")
    public ModelAndView del(@RequestParam(required = true, value = "id") long id,
                            int status,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        boolean res = userService.updateStatus(id,status);
        return this.viewNegotiating(request, response, new ResultClient(res));
    }

    /////////////////////批量导入顾客信息////////////////////////
    @RequestMapping("/importUserInfo")
    public ModelAndView importUserInfo(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(required = false,value = "file") MultipartFile file)throws Exception{
        try {
            User user = UserUtils.getUser(request);
            ResultClient res = importFileService.importUserInFo(file,user);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }
    }
    /////////////////////导入模板下载////////////////////////
    @RequestMapping("/downloadTemplatesExcel")
    public ModelAndView downloadTemplatesExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "顾客信息模板.xls";  //下载的文件名
        download.downloadFile(request, response, fileName);
        return this.viewNegotiating(request, response, new ResultClient(true));
    }
    /////////////////////批量导出顾客信息////////////////////////
    @RequestMapping("/exportUserInfo")
    public ModelAndView exportUserInfo(HttpServletRequest request,HttpServletResponse response,
                                       @RequestParam(value = "sid") long sid,
                                       @RequestParam(required = false, value = "phone",defaultValue = "") String phone,
                                       @RequestParam(required = false,value = "sex",defaultValue = "-1") int sex,
                                       @RequestParam(required = false,value = "job",defaultValue = "") String job
                                        )throws Exception{
        try {
            List<ExportUser> users = userService.getExportUserInfo(sid,phone,sex,job);
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("顾客信息", "顾客信息"), ExportUser.class, users);
            if (workbook == null) { throw new StoreSystemException("无可查询的顾客信息的数据！"); }
            //导出excel
            download.exportInfoTemplate(request,response,workbook,"顾客信息");
            return this.viewNegotiating(request, response, new ResultClient("导出成功！"));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(s.getMessage()));
        }
    }

    /////////////////////日常任务和奖励////////////////////////
    @RequestMapping("/taskReward")
    public ModelAndView taskReward(HttpServletRequest request,HttpServletResponse response,
                                   @RequestParam(value = "date") String date,//date格式201905
                                   @RequestParam(value = "sid") long sid)throws Exception{
        try{
            Subordinate subordinate = subordinateService.load(sid);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("门店ID错误");
            Map<String,Object> res =  userService.taskReward(date,sid);
            return this.viewNegotiating(request,response,new ResultClient(res));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response, new ResultClient(false,e.getMessage()));
        }
    }

    //销售开单 -- 手机号精确查询
    @RequestMapping("/getUserByPhone")
    public ModelAndView getUserByPhone(HttpServletRequest request,HttpServletResponse response,
                                       @RequestParam("phone") String phone)throws Exception{
        try {
             ClientUser clientUser = userService.getUser(phone);
            return this.viewNegotiating(request,response,new ResultClient(true,clientUser));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
    //销售开单 -- 会员信息认证
    @RequestMapping("/becomeVip")
    public ModelAndView becomeVip(HttpServletRequest request,HttpServletResponse response,User user)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(userService.checkUserGradeInfo(user)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(s.getMessage()));
        }
    }

}

