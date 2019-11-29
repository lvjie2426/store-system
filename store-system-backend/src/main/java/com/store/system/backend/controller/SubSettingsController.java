package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.SubSettings;
import com.store.system.service.SubSettingsService;
import com.store.system.service.SubordinateService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName SubSettingsController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/18 18:25
 * @Version 1.0
 **/
@Controller
@RequestMapping("/subSettings")
public class SubSettingsController extends BaseController {

    @Resource
    private SubSettingsService subSettingsService;
    @Resource
    private SubordinateService subordinateService;

    /***
    * 获取门店设置详情
    * @Param: [request, response, model, subId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/18
    */
    @RequestMapping("/load")
    public ModelAndView load(HttpServletRequest request, HttpServletResponse response, Model model,
                             long subId) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subId);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("公司为空！");
            SubSettings subSettings = subSettingsService.load(subId);
            return this.viewNegotiating(request, response, new ResultClient(subSettings));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }



    /***
     * 添加
     * @Param: [request, response, model, subSettings]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/18
     */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Model model,
                               SubSettings subSettings) throws Exception {
        try {
            User user= UserUtils.getUser(request);
            subSettings.setCheckUid(user.getId());
            subSettings= subSettingsService.add(subSettings);
            return this.viewNegotiating(request, response, new ResultClient(subSettings));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 编辑
     * @Param: [request, response, model, subSettings]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/18
     */
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, Model model,
                            SubSettings subSettings) throws Exception {
        try {
            User user= UserUtils.getUser(request);
            subSettings.setCheckUid(user.getId());
            subSettingsService.update(subSettings,"","");
            return this.viewNegotiating(request, response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
    /***
     * 开启/关闭
     * @Param: [request, response, model, subSettings]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/18
     */
    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(HttpServletRequest request, HttpServletResponse response, Model model,
                               long subid,int type) throws Exception {
        try {
           Boolean flag= subSettingsService.updateStatus(subid,type);
            return this.viewNegotiating(request, response, new ResultClient(flag));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 获取全部门店设置列表
     * @Param: [request, response, model, subId]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Date: 2019/9/18
     */
    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, Model model,
                             long subId) throws Exception {
        try {
           User user=UserUtils.getUser(request);
            List<SubSettings> subSettings = subSettingsService.getAllList(subId,user.getPsid());
            return this.viewNegotiating(request, response, new ResultClient(subSettings));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
