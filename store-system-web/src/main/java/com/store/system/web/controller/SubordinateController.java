package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.service.SubordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/subordinate")
public class SubordinateController extends BaseController {

    @Autowired
    private SubordinateService subordinateService;

    @RequestMapping("/getSubordinateStore")
    public ModelAndView     getSubordinateStore(HttpServletRequest request, HttpServletResponse response,
                                            long sid,
                                            Model model) throws Exception {
        try {
            List<ClientSubordinate> list= subordinateService.getTwoLevelAllList(sid);
            return this.viewNegotiating(request,response,new ResultClient(true,list));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }

    }


}
