package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryCheckBillItem;
import com.store.system.client.ClientMission;
import com.store.system.client.ClientUser;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.MissionService;
import com.store.system.service.SubordinateMissionPoolService;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserMissionPoolService;
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
    public ModelAndView add(Mission mission, String executorJson, String skuIdsJson,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<Long> ids = Lists.newArrayList();
            List<Long> skuIds = Lists.newArrayList();
            if(StringUtils.isNotBlank(executorJson)) {
                ids = JsonUtils.fromJson(executorJson, new TypeReference<List<Long>>() {});
            }
            if(StringUtils.isNotBlank(skuIdsJson)) {
                skuIds = JsonUtils.fromJson(skuIdsJson, new TypeReference<List<Long>>() {});
            }
            mission.setExecutor(ids);
            mission.setSkuIds(skuIds);
            Mission missionEntity = missionService.insert(mission);
            return this.viewNegotiating(request,response, new ResultClient(true, missionEntity));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(Mission mission, String executorJson, String skuIdsJson, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<Long> ids = Lists.newArrayList();
            List<Long> skuIds = Lists.newArrayList();
            if(StringUtils.isNotBlank(executorJson)) {
                ids = JsonUtils.fromJson(executorJson, new TypeReference<List<Long>>() {});
            }
            if(StringUtils.isNotBlank(skuIdsJson)) {
                skuIds = JsonUtils.fromJson(skuIdsJson, new TypeReference<List<Long>>() {});
            }
            mission.setExecutor(ids);
            mission.setSkuIds(skuIds);
            boolean res = missionService.update(mission);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView update(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = missionService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
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
            return this.viewNegotiating(request,response, new PagerResult<>( pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllMission")
    public ModelAndView getAllMission(@RequestParam(value = "sid") long sid, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(sid);
            long psid = subordinate.getPid();
            if(psid==0){ throw new StoreSystemException("门店ID错误");}
            return this.viewNegotiating(request,response, new ResultClient( missionService.getAllMission(psid)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }



}
