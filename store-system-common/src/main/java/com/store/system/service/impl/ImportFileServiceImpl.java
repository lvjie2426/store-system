package com.store.system.service.impl;


import ch.qos.logback.core.util.TimeUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.secure.MD5Utils;
import com.store.system.client.ResultClient;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ImportUser;
import com.store.system.model.User;
import com.store.system.service.ImportFileService;
import com.store.system.util.FileUtils;
import com.store.system.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ImportFileServiceImpl implements ImportFileService {
    @Resource
    private UserDao userDao;

    Logger logger = LoggerFactory.getLogger(ImportFileService.class);


    @Override
    @Transactional
    public ResultClient importUserInFo(MultipartFile file, User user) throws Exception {
        ResultClient res = new ResultClient();
        long sid = user.getSid();//店铺ID
        long psid = user.getPsid();//公司ID
            File usersFile = FileUtils.multipartToFile(file);
            String filename = file.getOriginalFilename();
            if(StringUtils.isBlank(filename)){
                return new ResultClient("读取失败");
            }
            ImportParams importParams = new ImportParams();
            importParams.setTitleRows(2);
            List<ImportUser> list = ExcelImportUtil.importExcel(usersFile,ImportUser.class,importParams);
            handleImportUser(list,sid,psid);
        res.setMsg("导入成功!");
        return res;
    }
    //保存user
    private void handleImportUser(List<ImportUser> list, long sid, long psid)throws Exception{
        for(ImportUser importUser:list){
            User user = getImportUser(importUser,sid,psid);
            user = userDao.insert(user);
            logger.info("@@@@@@@@@@"+ JsonUtils.toJson(user));
        }
    }

    //importUser转换User
    private User getImportUser(ImportUser importUser,long sid,long psid)throws Exception{
        User user = new User();

        /**顾客姓名**/
        if (StringUtils.isNotBlank(importUser.getName())) {
            user.setName(importUser.getName().trim());
        }else{
            throw new StoreSystemException("导入信息出错,第"+importUser.getNumber()+"行 顾客姓名没有填写!");
        }

        /**性别**/
        if(StringUtils.isNotBlank(importUser.getSex())){
            int sex = getUserSex(importUser.getSex().trim());
            user.setSex(sex);
        }

        /**出生日期**/
        if(StringUtils.isNotBlank(importUser.getBirthday())){
            try{
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(importUser.getBirthday()));
                long birthday = calendar.getTime().getTime();
                int age = getAge(birthday);
                user.setBirthdate(birthday);
                user.setAge(age);
            }catch (StoreSystemException s){
                throw new StoreSystemException("导入信息出错,第"+importUser.getNumber()+"行 出生日期格式有误!");
            }
        }else{
            throw new StoreSystemException("导入信息出错,第"+importUser.getNumber()+"行 出生日期没有填写!");
        }

        /**职业**/
        if(StringUtils.isNotBlank(importUser.getJob())){
            user.setJob(importUser.getJob().trim());
        }else{
            user.setExt("导入信息出错,第"+importUser.getNumber()+"行 职业没有填写!");
            return user;
        }

        /**手机号**/
        if(StringUtils.isNotBlank(importUser.getPhone())){
            user.setPhone(importUser.getPhone().trim());
        }else{
            throw new StoreSystemException("导入信息出错,第"+importUser.getNumber()+"行 手机号没有填写!");
        }

        /**用户名**/
        if(StringUtils.isNotBlank(importUser.getUserName())){
            user.setUserName(importUser.getUserName().trim());
        }

        /**微信号**/
        if(StringUtils.isNotBlank(importUser.getWeChat())){
            user.setWeixinId(importUser.getWeChat().trim());
        }

        /**身份证号**/
        if(StringUtils.isNotBlank(importUser.getIdCard())){
            user.setIdCard(importUser.getIdCard().trim().toUpperCase());
        }else{
            throw new StoreSystemException("导入信息出错,第"+importUser.getNumber()+"行 身份证号没有填写!");
        }

        /**邮箱**/
        if(StringUtils.isNotBlank(importUser.getEmail())){
            user.setMail(importUser.getEmail().trim());
        }else{
            throw new StoreSystemException("导入信息出错,第"+importUser.getNumber()+"行 邮箱没有填写!");
        }

        /**居住地**/
        if(StringUtils.isNotBlank(importUser.getAddress())){
            user.setPlace(importUser.getAddress());
        }else{
            throw new StoreSystemException("导入信息出错,第"+importUser.getNumber()+"行 居住地没有填写!");
        }

        /**积分**/
        if(StringUtils.isNotBlank(importUser.getScore())){
            int score = Integer.valueOf(importUser.getScore());
            user.setScore(score);
        }

        /**储值金额**/
        if(StringUtils.isNotBlank(importUser.getMoney())){
            long money = Long.valueOf(importUser.getMoney());
            user.setMoney(money);
        }

        /**备注**/
        if(StringUtils.isNotBlank(importUser.getDesc())){
            user.setDesc(importUser.getDesc().trim());
        }

        user.setSid(sid);
        user.setPsid(psid);
        user.setRand(new Random().nextInt(100000000));
        user.setUserType(User.userType_user);
        user.setPassword(MD5Utils.md5ReStr("1234".getBytes()));
        return user;
    }

    private int getUserSex(String sex)throws Exception{
        if("男".equals(sex)){
            return 1;
        }else if("女".equals(sex)){
            return 2;
        }else{
            return 0;
        }
    }

    public int getAge(long birthday)throws Exception{
        Calendar calendar = Calendar.getInstance();
        int yearr = calendar.get(Calendar.YEAR);//获取当前年份
        int monthh = calendar.get(Calendar.MONTH);//获取当前月份
        int dayy = calendar.get(Calendar.DATE);//获取天

        calendar.setTime(new Date(birthday));
        int year = calendar.get(Calendar.YEAR);//获取出生年份
        int month = calendar.get(Calendar.MONTH);//获取出生月份
        int day = calendar.get(Calendar.DATE);//获取天

        int age =yearr-year;
        //如果出生月份小于当前月份,年龄-1
        if(month<monthh){
            age--;
        }else if(month==monthh){
            if(day>dayy){
                age--;
            }
        }
        return age;
    }
}
