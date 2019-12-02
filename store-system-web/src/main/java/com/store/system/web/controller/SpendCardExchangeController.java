package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSpendCardExchange;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.service.SpendCardExchangeService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName SpendCardExchangeController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 11:40
 * @Version 1.0
 **/
@Controller
@RequestMapping("/spendCardExchange")
public class SpendCardExchangeController extends BaseController{

    @Resource
    private SpendCardExchangeService spendCardExchangeService;

    @RequestMapping("/getAllListBySid")
    public ModelAndView getAllListBySid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            List<ClientSpendCardExchange> res = spendCardExchangeService.getAllList(user.getPsid(),user.getSid());
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
