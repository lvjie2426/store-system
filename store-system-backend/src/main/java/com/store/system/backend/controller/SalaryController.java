package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Salary;
import com.store.system.model.SalaryRecord;
import com.store.system.model.User;
import com.store.system.service.ImportFileService;
import com.store.system.service.SalaryRecordService;
import com.store.system.service.SalaryService;
import com.store.system.util.DateUtils;
import com.store.system.util.FileDownload;
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 员工工资单管理
 * @Date: 2019/5/27 15:46
 * @Version: 1.0
 */
@Controller
@RequestMapping("/salary")
public class SalaryController extends BaseController {

    @Resource
    private FileDownload download;

    @Resource
    private SalaryRecordService salaryRecordService;
    @Resource
    private ImportFileService importFileService;

    @Resource
    private SalaryService salaryService;
    //查询某员工的工资单列表
    @RequestMapping("/loadSalaryByUser")
    public ModelAndView loadSalaryByUser(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(value = "uid") long uid)throws Exception{
        try {
            List<Salary> salaries = salaryService.loadSalaryByUser(uid);
            return this.viewNegotiating(request,response,new ResultClient(true,salaries));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }
    }

    /////////////////////导入工资单////////////////////////
    @RequestMapping("/importUserSalary")
    public ModelAndView importUserSalary(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(required = true,value = "file") MultipartFile file)throws Exception{
        try {
            User user = UserUtils.getUser(request);
            SalaryRecord salaryRecord = importFileService.importUserSalary(file,user);
            return this.viewNegotiating(request,response,new ResultClient(true,salaryRecord));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
    /////////////////////导入模板下载////////////////////////
    @RequestMapping("/downloadTemplatesExcel")
    public ModelAndView downloadTemplatesExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "工资单模板.xls";  //下载的文件名
        download.downloadFile(request, response, fileName);
        return this.viewNegotiating(request, response, new ResultClient(true));
    }
}
