package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientMission;
import com.store.system.client.ClientUser;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.MissionService;
import com.store.system.service.SubordinateMissionPoolService;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserMissionPoolService;
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
 * Description:营销管理--任务模块  用于给团队或个人创建任务,任务目标可以是 销售额、门店总销售额、销售量，完成任务可获得奖励。
 * MissionController
 * package: com.store.system.backend.controller
 * creat_user: lihaojie
 * creat_date: 2019/5/23
 * creat_time: 10：50
 **/
@Controller
@RequestMapping("/mission")
public class MissionController extends BaseController {

    @Resource
    private MissionService missionService;

    @Resource
    private SubordinateService subordinateService;

    @RequestMapping("/add")
    public ModelAndView add(Mission mission, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Mission missionEntity = missionService.insert(mission);
            return this.viewNegotiating(request,response, new ResultClient(true, new ResultClient(missionEntity)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(Mission mission, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = missionService.update(mission);
            return this.viewNegotiating(request,response, new ResultClient(true, new ResultClient(res)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView update(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = missionService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, new ResultClient(res)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "sid") long sid, HttpServletRequest request, HttpServletResponse response, Model model, Pager pager) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(sid);
            long psid = subordinate.getPid();
            if(psid==0){ throw new StoreSystemException("门店ID错误");}
            pager = missionService.getByPager(pager,psid);
            return this.viewNegotiating(request,response, new ResultClient(true, pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }



}
