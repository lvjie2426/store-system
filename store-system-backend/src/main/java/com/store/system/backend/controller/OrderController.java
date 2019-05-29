package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientOrder;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Order;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.OrderService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;
    @Resource
    private SubordinateDao subordinateDao;

    /**
     * create by: zhangmeng
     * description: 获取全部订单
     * @Param: request
     * @Param: response
     * @Param: pager
     * @Param: startTime
     * @Param: endTime
     * @Param: status
     * @Param: personnelid
     * @return
     */
    @RequestMapping("/getAllOrder")
    public ModelAndView getSubOrder(HttpServletRequest request, HttpServletResponse response,
                                    Pager pager,
                                    @RequestParam(required = false,value = "startTime",defaultValue = "0")long startTime,
                                    @RequestParam(required = false,value = "subid",defaultValue = "0")long subid,
                                    @RequestParam(required = false,value = "endTime",defaultValue = "0")long endTime,
                                    @RequestParam(required = false, value = "personnelid",defaultValue = "0") long personnelid,
                                    @RequestParam(required = false, value = "status",defaultValue = "0") int status,
                                    @RequestParam(required = false, value = "uid",defaultValue = "0") long uid,
                                    @RequestParam(required = false, value = "name",defaultValue = "") String name,
                                    @RequestParam(required = false, value = "makeStatus",defaultValue = "0")  int makeStatus,
                                                   Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateDao.load(subid);
            if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
            pager= orderService.getAll(pager,startTime,endTime,personnelid,status,uid,name, makeStatus,subid);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }

    /**
     * create by: zhangmeng
     * description: 计算订单金额
     * create time: 2019/05/28 0028 13:54:43
     * @return
     */
    @RequestMapping("/countPrice")
    public ModelAndView countPrice(HttpServletRequest request, HttpServletResponse response,
                                  Order order) throws Exception {

        try {
            order = orderService.countPrice(order);
            return this.viewNegotiating(request, response, new ResultClient(order));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }


    /**
     * create by: zhangmeng
     * description: 创建订单（临时订单）。
     * create time: 2019/05/28 0028 13:54:43
     * @return
     */
    @RequestMapping("/saveOrder")
    public ModelAndView saveOrder(HttpServletRequest request, HttpServletResponse response,
                                      Order order) throws Exception {

        try {
            order = orderService.saveOrder(order);
            return this.viewNegotiating(request, response, new ResultClient(order));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }

    /**
     * create by: zhangmeng
     * description: 获取门店历史订单
     * create time: 2019/05/28 0028 13:54:43
     * @return
     */
    @RequestMapping("/getOrderBySubid")
    public ModelAndView getOrderBySubid(HttpServletRequest request, HttpServletResponse response,
                                  long subid) throws Exception {

        try {
           List<ClientOrder> orderList = orderService.getAllBySubid(subid);
            return this.viewNegotiating(request, response, new ResultClient(orderList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }
    /**
     * create by: zhangmeng
     * description: 获取临时订单
     * create time: 2019/05/28 0028 13:54:43
     * @return
     */
    @RequestMapping("/getTemporaryOrder")
    public ModelAndView getTemporaryOrder(HttpServletRequest request, HttpServletResponse response,
                                        long subid) throws Exception {

        try {
            List<ClientOrder> orderList = orderService.getTemporaryOrder(subid);
            return this.viewNegotiating(request, response, new ResultClient(orderList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }





}
