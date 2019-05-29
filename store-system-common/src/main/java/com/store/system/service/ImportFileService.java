package com.store.system.service;

import com.store.system.client.ResultClient;
import com.store.system.model.Salary;
import com.store.system.model.SalaryRecord;
import com.store.system.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface ImportFileService {
    /**
     * 导入顾客信息
     * @param file
     * @param user
     * @author lihaojie
     * @date 2019/5/21 14:00
     * @return
     */
    public ResultClient importUserInFo(MultipartFile file,User user) throws Exception;
    /**
     * 导入工资单
     * @param file
     * @param user
     * @author lihaojie
     * @date 2019/5/27 15:59
     * @return
     */
    public String importUserSalary(MultipartFile file, User user) throws Exception;
}
