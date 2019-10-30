package com.store.system.web.controller;

import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.OptometryInfoRes;
import com.store.system.client.ClientOptometryInfo;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.OptometryInfo;
import com.store.system.service.DictionaryService;
import com.store.system.service.OptometryInfoService;
import com.store.system.util.DictionaryUtils;
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

@Controller
@RequestMapping("/optometryinfo")
public class OptometryInfoController extends BaseController {

    @Resource
    private DictionaryService dictionaryService;

    @Resource
    private OptometryInfoService optometryInfoService;


    @RequestMapping("/update")
    public ModelAndView update(OptometryInfo optometryInfo, HttpServletRequest request, HttpServletResponse response, Model model,
                               @RequestParam(required = false,value = "yuanYongResJson",defaultValue = "") String yuanYongResJson,
                               @RequestParam(required = false,value = "yinXingResJson",defaultValue = "")  String yinXingResJson,
                               @RequestParam(required = false,value = "jinYongResJson",defaultValue = "")  String jinYongResJson,
                               @RequestParam(required = false,value = "jianJinDuoJiaoDianResJson",defaultValue = "")   String jianJinDuoJiaoDianResJson) throws Exception {
        try {
            if(StringUtils.isNotBlank(yuanYongResJson)){
                OptometryInfoRes yuanyongRes = JsonUtils.fromJson(yuanYongResJson, OptometryInfoRes.class);
                optometryInfo.setYuanYongRes(yuanyongRes);
            }if(StringUtils.isNotBlank(yinXingResJson)){
                OptometryInfoRes yinXingRes = JsonUtils.fromJson(yinXingResJson, OptometryInfoRes.class);
                optometryInfo.setYinXingRes(yinXingRes);
            }if(StringUtils.isNotBlank(jinYongResJson)){
                OptometryInfoRes jinYongRes = JsonUtils.fromJson(jinYongResJson, OptometryInfoRes.class);
                optometryInfo.setJinYongRes(jinYongRes);
            }if(StringUtils.isNotBlank(jianJinDuoJiaoDianResJson)){
                OptometryInfoRes jianJinDuoJiaoDianRes = JsonUtils.fromJson(jianJinDuoJiaoDianResJson, OptometryInfoRes.class);
                optometryInfo.setJianJinDuoJiaoDianRes(jianJinDuoJiaoDianRes);
            }
            boolean res = optometryInfoService.update(optometryInfo);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }


    @RequestMapping("/getUserList")
    public ModelAndView getUserList(@RequestParam(value = "uid") long uid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {

            return this.viewNegotiating(request,response, new ResultClient(true, optometryInfoService.getUserList(uid)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    // web
    //  获取验光 结果/建议 1/2
    @RequestMapping("/getResult")
    public ModelAndView getResult(HttpServletRequest request,HttpServletResponse response,
                                  @RequestParam(value = "type") int type)throws Exception{
        try {
            String res [] = {};
            if(type==1){
                res = dictionaryService.getStrings(DictionaryUtils.optometry_result,null);
            }else if(type==2){
                res = dictionaryService.getStrings(DictionaryUtils.optometry_support,null);
            }
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

}
