package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.service.ImportFileService;
import com.store.system.service.SalaryRecordService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 工资单明细管理
 * @Date: 2019/5/27 15:46
 * @Version: 1.0
 */
@Controller
@RequestMapping("/salaryrecord")
public class SalaryRecordController extends BaseController {

    @Resource
    private SalaryRecordService salaryRecordService;

    /////////////////////查询工资单列表////////////////////////
    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "sid") long sid, Pager pager)throws Exception{
        try{
            pager = salaryRecordService.getAllByPager(pager,sid);
            return this.viewNegotiating(request,response,pager);
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
    /////////////////////撤销导入////////////////////////
    @RequestMapping("/revoke")
    public ModelAndView revoke(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "id") long id)throws Exception{
        try{
            boolean res = salaryRecordService.revoke(id);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
}
