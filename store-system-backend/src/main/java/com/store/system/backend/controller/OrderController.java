package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Subordinate;
import com.store.system.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

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
                                    @RequestParam(required = false,value = "endTime",defaultValue = "0")long endTime,
                                    @RequestParam(required = false, value = "personnelid",defaultValue = "0") long personnelid,
                                    @RequestParam(required = false, value = "status",defaultValue = "0") int status,
                                    @RequestParam(required = false, value = "uid",defaultValue = "0") long uid,
                                    @RequestParam(required = false, value = "name",defaultValue = "") String name,
                                    @RequestParam(required = false, value = "makeStatus",defaultValue = "0")  int makeStatus,
                                                   Model model) throws Exception {
        try {
            pager= orderService.getAll(pager,startTime,endTime,personnelid,status,uid,name, makeStatus);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }


}
