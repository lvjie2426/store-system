package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.UserLog;
import com.store.system.service.UserLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-08-14 10:20
 **/
@Controller
@RequestMapping("userLog")
public class UserLogController extends BaseController {
    @Resource
    private UserLogService userLogService;

    // 保存接口。保存前先查询。 有则更新，无则insert
    @RequestMapping("/add")
    public ModelAndView add(UserLog userLog, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            userLog = userLogService.add(userLog);
            return this.viewNegotiating(request, response, new ResultClient(userLog));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getInfo")
    public ModelAndView getInfo(
            @RequestParam(value = "uid") long uid,
            @RequestParam(value = "type") int type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<UserLog> userLog = userLogService.getInfoByUid(uid, type);
            return this.viewNegotiating(request, response, new ResultClient(userLog));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


}
