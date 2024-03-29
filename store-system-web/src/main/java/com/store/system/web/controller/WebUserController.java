package com.store.system.web.controller;


import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.property.PropertyLoader;
import com.quakoo.baseFramework.redis.JedisX;
import com.quakoo.baseFramework.secure.MD5Utils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.OptometryInfoRes;
import com.store.system.client.ClientUser;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.dao.OptometryInfoDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.OptometryInfoService;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserService;
import com.store.system.util.UserUtils;
import com.store.system.util.WebPermissionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */

@Controller
@RequestMapping("/web/user")
public class WebUserController extends BaseController {

    @Autowired(required = true)
    @Qualifier("cachePool")
    protected JedisX cache;

    private static final long FIVE_MINUTES = 5*60*1000;
    
    @Autowired
    private UserService userService;
    @Autowired
    private OptometryInfoService optometryInfoService;
    @Autowired
    private SubordinateService subordinateService;
    PropertyLoader loader = PropertyLoader.getInstance("dao.properties");
    String templateRegisterCode = loader.getProperty("templateRegisterCode");
    String templateLoginCode = loader.getProperty("templateLoginCode");
	/**
	 * 登录
	 * @returnd
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(required = true, value = "phone") String phone,
                              @RequestParam(required = true, value = "authCode") String authCode) throws Exception {
//        String timeRes = cache.getString("login_code_time_" + (phone));
//        if(StringUtils.isNotBlank(timeRes)){
//            long time = Long.valueOf(timeRes);
//            if(System.currentTimeMillis() - time > FIVE_MINUTES){
//                return this.viewNegotiating(request, response, new ResultClient("验证码失效，请重新获取"));
//            }
//        }
        String res = userService.getLoginCodeAuthCode(phone);
        try{
            ClientUserOnLogin clientUserOnLogin = userService.loginForCode(phone);
            List<Permission> permissions = userService.getUserPermissions(clientUserOnLogin.getId());
            if (!WebPermissionUtil.hasSchoolLoginPermission(permissions)) {
                clientUserOnLogin.setAdmin(false);
            }else{
                clientUserOnLogin.setAdmin(true);
            }
            try{
                cache.delete("exists_login_code_" + (phone));
                cache.delete("login_code_" + (phone));
                cache.delete("login_code_time_" + phone);
            }catch (Exception e){

            }
            return this.viewNegotiating(request, response, new ResultClient(clientUserOnLogin));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response,new ResultClient(false,e.getMessage()));
        }
	}

	@RequestMapping("/createAuthCodeOnReg")
	public ModelAndView createAuthCodeOnReg(@RequestParam(required = true, value = "phone") String phone,
									   HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        try {
            long uid = userService.loadByAccount(LoginUserPool.loginType_phone, phone, User.userType_backendUser);
            if (uid > 0) throw new StoreSystemException("当前手机号已注册");
            return this.viewNegotiating(request, response, new ResultClient(true, userService.createAuthCode(String.valueOf(phone), templateRegisterCode)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, "", e.getMessage()));
        }
	}

    /**
     * 注册用户
     *
     * @param request
     * @param response
     * @param user
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping("/register")
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response,
                                 User user,
                                 @RequestParam(required = false, value = "code") String code) throws Exception {
        try {
//            User visitor = UserUtils.getUser(request); //有游客登陆则需要这行，没有游客登陆注释掉这行
            if (StringUtils.isNotBlank(code)) {
                String rightAuthCode = userService.getAuthCode(user.getPhone());
                if (StringUtils.isBlank(rightAuthCode)) {
                    return this.viewNegotiating(request, response, new ResultClient("验证码不正确"));
                }
                if (!rightAuthCode.equals(code)) {
                    return this.viewNegotiating(request, response, new ResultClient("验证码不正确"));
                }
            }
            ClientUserOnLogin register = userService.register(user);
            return super.viewNegotiating(request, response, new ResultClient(register));
        } catch (StoreSystemException e) {
            return super.viewNegotiating(request, response, new ResultClient(false, "", e.getMessage()));
        }
    }

    @RequestMapping("/createAuthCodeOnLogin")
    public ModelAndView createAuthCode(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(required = true, value = "phone") String phone) throws Exception {
        try {
            long uid = userService.loadByAccount(LoginUserPool.loginType_phone,phone, User.userType_backendUser);
            if (uid == 0) {
                throw new StoreSystemException("当前手机号未注册");
            }
            boolean res = userService.createLoginCodeAuthCode(phone);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


//    @RequestMapping("/createAuthCodeOnLogin")
//    public ModelAndView createAuthCode(@RequestParam(required = true, value = "phone") String phone,
//                                       HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
//        try {
//            return this.viewNegotiating(request, response, new ResultClient(true, userService.createAuthCode(String.valueOf(phone), SmsUtils.templateLoginCode)));
//        } catch (StoreSystemException e) {
//            return this.viewNegotiating(request, response, new ResultClient(false, "", e.getMessage()));
//        }
//    }

    /**
     * 获取手机修改密码验证码
     *
     * @param phone
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/createAuthCodeOnUpdatePassword")
    public ModelAndView createAuthCodeOnUpdatePassword(@RequestParam(required = true, value = "phone") String phone,
                                                       HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        try {
            long uid = userService.loadByAccount(LoginUserPool.loginType_phone, phone, User.userType_backendUser);
            if (uid == 0) throw new StoreSystemException("当前手机号未注册");
            return this.viewNegotiating(request, response, new ResultClient(userService.createAuthCode(String.valueOf(phone), templateLoginCode)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, "", e.getMessage()));
        }
    }


    /**
     * 修改密码
     *
     * @param phone
     * @param code
     * @param oldPassword
     * @param password
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/updatePasswordAndLogin")
    public ModelAndView updatePasswordAndLogin(
            @RequestParam(required = true, value = "phone") String phone,
            @RequestParam(required = false, value = "code") String code,
            @RequestParam(required = false, value = "oldPassword") String oldPassword,
            @RequestParam(required = true, value = "password") String password,
            HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        try {
            String res = userService.getAuthCode(phone);
            if (StringUtils.isNotBlank(res) || StringUtils.isNotBlank(code)) {
                if (!code.equals(res)) throw new StoreSystemException("验证码不正确");
            }
            long uid = userService.loadByAccount(LoginUserPool.loginType_phone, phone, User.userType_backendUser);
            if (uid == 0) throw new StoreSystemException("当前手机号未注册");
            User user = userService.load(uid);
            if (StringUtils.isNotBlank(oldPassword)) {
                if (!user.getPassword().equals(MD5Utils.md5ReStr(oldPassword.getBytes()))) {
                    return this.viewNegotiating(request, response, new ResultClient("原密码不正确"));
                }
            }
            user.setPassword(password);
            boolean sign = userService.update(user, null);
            return this.viewNegotiating(request, response, new ResultClient(sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, "", e.getMessage()));
        }
    }


    /**
     * 验证手机号是否已绑定三方账号
     *
     * @param request
     * @param response
     * @param weixinId
     * @param qqId
     * @param weiboId
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkThird")
    public ModelAndView checkThird(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(required = false, value = "weixinId") String weixinId,
                                   @RequestParam(required = false, value = "qqId") String qqId,
                                   @RequestParam(required = false, value = "weiboId") String weiboId) throws Exception {
        if (StringUtils.isNotBlank(weixinId)) {
            long uid = userService.loadByAccount(LoginUserPool.loginType_weixin, weixinId, 0);
            if (uid > 0) {
                return super.viewNegotiating(request, response, new ResultClient(true, false, "此微信已绑定手机号"));
            }
        } else if (StringUtils.isNotBlank(qqId)) {
            long uid = userService.loadByAccount(LoginUserPool.loginType_qq, qqId, 0);
            if (uid > 0) {
                return super.viewNegotiating(request, response, new ResultClient(true, false, "此QQ已绑定手机号"));
            }
        } else if (StringUtils.isNotBlank(weiboId)) {
            long uid = userService.loadByAccount(LoginUserPool.loginType_weibo, weiboId, 0);
            if (uid > 0) {
                return super.viewNegotiating(request, response, new ResultClient(true, false, "此微博已绑定手机号"));
            }
        }
        return super.viewNegotiating(request, response, new ResultClient(true, true));
    }

    // 管理端员工信息接口同时调用
    @RequestMapping("/load")
    public ModelAndView load(@RequestParam(value = "uid", required = false, defaultValue = "0") long uid,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if(uid == 0) {
                User user = UserUtils.getUser(request);
                uid = user.getId();
            }
            ClientUser res = userService.loadWithClient(uid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(e.getMessage()));
        }
    }

    /***
    * 完善个人资料 / 编辑员工
    * @Param: [request, response, user]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/8/22
    */
    @RequestMapping("/updateUser")
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response,
                                   User user) throws Exception {
        try {
            boolean res = userService.updateUser(user);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(e.getMessage()));
        }
    }

    /**
     * create by: zhangmeng
     * description: 获取门店下员工 获取正常状态员工。
     * create time: 2019/08/28 0028 10:32:56
     *
     * @Param: request
     * @Param: response
     * @Param: sid
     * @return
     */
    @RequestMapping("/getUserBySid")
    public ModelAndView getUserBySid(Pager pager,HttpServletRequest request, HttpServletResponse response,
                                   Long sid) throws Exception {

        try {
            pager = userService.getStaffUserBySid(pager,sid);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(e.getMessage()));
        }
    }

    /***
    * 公司下的意向顾客
    * @Param: [pager, request, response, model, name]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/8/30
    */
    @RequestMapping("/getIntentionUsers")
    public ModelAndView getIntentionUsers(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model,
                                          String name) throws Exception {
        try {
            long psid = 0;
            User user = UserUtils.getUser(request);
            if (user != null) {
                psid = user.getPsid();
            }
            pager = userService.getIntentionPager(pager, psid, User.userType_user, User.status_nomore, name);
            return this.viewNegotiating(request, response, (pager.toModelAttribute()));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 保存意向顾客  保存到user表，同时optometryinfo 验光表也保存
     * @param user
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/addCustomer")
    public ModelAndView addCustomer(User user, OptometryInfo optometryInfo,
                                    @RequestParam(required = false,value = "yuanYongResJson",defaultValue = "") String yuanYongResJson,
                                    @RequestParam(required = false,value = "yinXingResJson",defaultValue = "")  String yinXingResJson,
                                    @RequestParam(required = false,value = "jinYongResJson",defaultValue = "")  String jinYongResJson,
                                    @RequestParam(required = false,value = "jianJinDuoJiaoDianResJson",defaultValue = "")   String jianJinDuoJiaoDianResJson,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            user.setContactPhone(user.getPhone());
            user = userService.addCustomer(user);
            if(optometryInfo!=null){
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
                optometryInfo.setUid(user.getId());
                optometryInfoService.add(optometryInfo);
            }
            return this.viewNegotiating(request,response, new ResultClient(user));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 修改意向顾客  修改user表，同时optometryinfo 验光表也修改
     * @param user
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateCustomer")
    public ModelAndView updateCustomer(User user, OptometryInfo optometryInfo,long oid,
                                    @RequestParam(required = false,value = "yuanYongResJson",defaultValue = "") String yuanYongResJson,
                                    @RequestParam(required = false,value = "yinXingResJson",defaultValue = "")  String yinXingResJson,
                                    @RequestParam(required = false,value = "jinYongResJson",defaultValue = "")  String jinYongResJson,
                                    @RequestParam(required = false,value = "jianJinDuoJiaoDianResJson",defaultValue = "")   String jianJinDuoJiaoDianResJson,
                                    HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            optometryInfo.setId(oid);
            user.setContactPhone(user.getPhone());
           boolean falg= userService.updateCustomer(user);
            if(falg && optometryInfo.getId()>0){
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
                optometryInfo.setUid(user.getId());
                optometryInfoService.update(optometryInfo);
            }else{
                return this.viewNegotiating(request,response, new ResultClient("会员信息更新失败"));

            }
            return this.viewNegotiating(request,response, new ResultClient(user));

        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }





    // 管理端 创建新员工
    @RequestMapping("/addUser")
    public ModelAndView addUser(
            User user, HttpServletResponse response, HttpServletRequest request
    ) throws Exception {

        try {
            long uid = userService.loadByAccount(LoginUserPool.loginType_phone, user.getPhone(), User.userType_backendUser);
            if (uid > 0) throw new StoreSystemException("当前手机号已注册");
            user.setPassword("123456");
            return this.viewNegotiating(request, response, new ResultClient(userService.register(user)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, "", e.getMessage()));
        }
    }

    /**
     * create by: zhangmeng
     * description: 管理端  人员管理获取全部
     * create time: 2019/08/28 0028 10:32:56
     *
     * @Param: request
     * @Param: response
     * @Param: sid
     * @return
     */
    @RequestMapping("/getAllUserBySid")
    public ModelAndView getAllUserBySid(Pager pager,HttpServletRequest request, HttpServletResponse response,
                                     Long sid) throws Exception {

        try {
            pager= userService.getAllStaffUserBySid(pager,sid);
            return this.viewNegotiating(request, response,pager.toModelAttribute());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(e.getMessage()));
        }
    }

    /**
     * create by: zhangmeng
     * description:   获取门店下所有员工
     * create time: 2019/08/28 0028 10:32:56
     *
     * @Param: request
     * @Param: response
     * @Param: sid
     * @return
     */
    @RequestMapping("/getAllUserBySubid")
    public ModelAndView getAllUserBySubid(Pager pager,HttpServletRequest request, HttpServletResponse response,
                                        Long subid) throws Exception {

        try {

            return this.viewNegotiating(request, response,new ResultClient(userService.getAllUserBySid(subid)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(e.getMessage()));
        }
    }

    //获取所有 顾客/员工 的职业
    @RequestMapping("/getAllUserJob")
    public ModelAndView getAllUserJob(HttpServletRequest request,HttpServletResponse response,
                                      @RequestParam(value = "sid") long sid,
                                      @RequestParam(value = "userType",defaultValue = "0") int userType)throws Exception{
        try{
            Subordinate subordinate = subordinateService.load(sid);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("门店ID错误");
            return this.viewNegotiating(request,response,new ResultClient(true,userService.getAllUserJob(sid,userType)));
        }catch (StoreSystemException e){
            return this.viewNegotiating(request,response, new ResultClient(false,e.getMessage()));
        }
    }
    //根据职业获取员工列表
    @RequestMapping("/getUsersByJob")
    public ModelAndView getUsersByJob(HttpServletRequest request,HttpServletResponse response,
                                      @RequestParam(name = "job") String job,
                                      @RequestParam(name = "userType") int userType)throws Exception{
        try {
            User user = UserUtils.getUser(request);
            long sid = user.getSid();
            return this.viewNegotiating(request,response,new ResultClient(true,userService.getUserByJob(job,sid,userType)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

}

