package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientMissForUser;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Mission;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.*;
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private UserService userService;

    @Resource
    private MissionService missionService;

    @Resource
    private SubordinateService subordinateService;

    @Resource
    private ProductService productService;

    @Resource
    private OrderService orderService;

    @RequestMapping("/add")
    public ModelAndView add(Mission mission,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
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

    /**
     * create by: zhangmeng
     * description: 管理端获取任务列表
     * create time: 2019/08/23 0023 15:41:55
     * @Param: sid
     * @Param: missionStatus 1 进行中 2 完成
     * @Param: type 1 团队 2个人
     * @Param: isNow  0 进行中 1 结束
     * @Param: request
     * @Param: response
     * @Param: model
     * @Param: pager
     * @return
     */
    @RequestMapping("/getWebByPager")
    public ModelAndView getWebByPager(@RequestParam(value = "sid") long sid,
                                   @RequestParam(value = "missionStatus",defaultValue = "0") int missionStatus,
                                   @RequestParam(value = "type",defaultValue = "0") int type,
                                   @RequestParam(value = "isNow",defaultValue = "0") int isNow,
                                   HttpServletRequest request, HttpServletResponse response, Model model, Pager pager) throws Exception {

        try {
            Subordinate subordinate = subordinateService.load(sid);
            long psid = subordinate.getPid();
            if(psid==0){ throw new StoreSystemException("门店ID错误");}
            pager = missionService.getWebByPager(pager,psid,missionStatus,type,isNow);
            return this.viewNegotiating(request,response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * create by: zhangmeng
     * description: 员工端获取销售任务列表
     * create time: 2019/08/23 0023 16:30:13
     *
     * @Param: sid
     * @Param: missionStatus
     * @Param: type
     * @Param: isNow
     * @Param: request
     * @Param: response
     * @Param: model
     * @Param: pager
     * @return
     */
    @RequestMapping("/getUserWebByPager")
    public ModelAndView getUserWebByPager(@RequestParam(value = "sid") long sid,
                                      @RequestParam(value = "missionStatus",defaultValue = "0") int missionStatus,
                                      @RequestParam(value = "type",defaultValue = "0") int type,
                                      @RequestParam(value = "isNow",defaultValue = "0") int isNow,
                                      HttpServletRequest request, HttpServletResponse response, Model model, Pager pager) throws Exception {

        try {
            User user= UserUtils.getUser(request);
            Subordinate subordinate = subordinateService.load(sid);
            long psid = subordinate.getPid();
            if(psid==0){ throw new StoreSystemException("门店ID错误");}
            pager = missionService.getUserWebByPager(pager,psid,missionStatus,type,isNow,user);
            return this.viewNegotiating(request,response, pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * create by: zhangmeng
     * description: app奖励榜
     * create time: 2019/08/24 0024 15:43:51
     *
     * @Param: request
     * @Param: response
     * @Param: date  201908
     * @Param: sid
     * @return
     */
    @RequestMapping("/taskReward")
    public ModelAndView taskReward(HttpServletRequest request,HttpServletResponse response,
                                   @RequestParam(value = "date") String date,//date格式201905
                                   @RequestParam(value = "sid") long sid)throws Exception{

        try{
            Subordinate subordinate = subordinateService.load(sid);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("门店ID错误");
            ClientMissForUser clientMissForUser =  userService.taskRewardApp(date,sid);
            return this.viewNegotiating(request,response,new ResultClient(clientMissForUser));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response, new ResultClient(false,e.getMessage()));
        }
    }

    //任务奖励 app
    //todo 客户需要确认修改一下页面。
    @RequestMapping("/getAllMission")
    public ModelAndView getAllMission(@RequestParam(value = "sid") long sid,
                                      Date date,HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(sid);
            long psid = subordinate.getPid();
            if(psid==0){ throw new StoreSystemException("门店ID错误");}
            return this.viewNegotiating(request,response, new ResultClient( missionService.getAllMissionApp(psid,date)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }


    //app--销售奖励
    //todo 客户需要确认修改一下页面。
/*    @RequestMapping("/saleReward")
    public ModelAndView saleReward(HttpServletRequest request,HttpServletResponse response, Date date,
                                   @RequestParam(name = "subid") long subid)throws Exception{
        try {
            Subordinate subordinate = subordinateService.load(subid);
            long sid = subordinate.getPid();
            if(sid==0){ throw new StoreSystemException("门店ID有误"); }
            Map<String,Object> res = orderService.saleRewardApp(subid,date);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response, new ResultClient(false, s.getMessage()));
        }
    }*/


    // 获取提成商品清单
    @RequestMapping("/getCommSpu")
    public ModelAndView getCommSpu(HttpServletRequest request,HttpServletResponse response,Pager pager, long subid,long cid)throws Exception{


        try{
            Subordinate subordinate=subordinateService.load(subid);
            long sid=subordinate.getPid();
            if(sid==0){
                throw  new StoreSystemException("门店id错误!");
            }
            pager=   productService.getCommSpu(pager,sid,cid);
            return this.viewNegotiating(request,response,pager.toModelAttribute());
        }catch (StoreSystemException s){
            return  this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
}
