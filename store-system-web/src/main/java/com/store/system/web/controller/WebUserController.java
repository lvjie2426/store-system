package com.store.system.web.controller;


import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.secure.MD5Utils;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientUser;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.LoginUserPool;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.OptometryInfoService;
import com.store.system.service.PayService;
import com.store.system.service.UserService;
import com.store.system.util.SmsUtils;
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */

@Controller
@RequestMapping("/web/user")
public class WebUserController extends BaseController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private OptometryInfoService optometryInfoService;

	/**
	 * 登录
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request,
											 HttpServletResponse response,
											 User loginUser,@RequestParam(required = false, value = "authCode") String authCode,
											 Model model) throws Exception {
        try {
            if (StringUtils.isNotBlank(loginUser.getPhone())) {
                String rightAuthCode = userService.getAuthCode(loginUser.getPhone());
                if (StringUtils.isBlank(authCode) && StringUtils.isBlank(loginUser.getPassword()))
                    throw new StoreSystemException("请输入密码或验证码");
                if (StringUtils.isNotBlank(authCode) && !authCode.equals(rightAuthCode)) {
                    throw new StoreSystemException("验证码不正确");
                }
            }
            ClientUserOnLogin clientUserOnLogin = userService.login(loginUser, authCode);
            return this.viewNegotiating(request, response, new ResultClient(clientUserOnLogin));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, "", e.getMessage()));
        }
	}


	@RequestMapping("/createAuthCodeOnReg")
	public ModelAndView createAuthCodeOnReg(@RequestParam(required = true, value = "phone") String phone,
									   HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        try {
            long uid = userService.loadByAccount(LoginUserPool.loginType_phone, phone, User.userType_user);
            if (uid > 0) throw new StoreSystemException("当前手机号已注册");
            return this.viewNegotiating(request, response, new ResultClient(true, userService.createAuthCode(String.valueOf(phone), SmsUtils.templateLoginCode)));
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
    public ModelAndView createAuthCode(@RequestParam(required = true, value = "phone") String phone,
                                       HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        try {
            return this.viewNegotiating(request, response, new ResultClient(true, userService.createAuthCode(String.valueOf(phone), SmsUtils.templateLoginCode)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, "", e.getMessage()));
        }
    }

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
            long uid = userService.loadByAccount(LoginUserPool.loginType_phone, phone, User.userType_user);
            if (uid == 0) throw new StoreSystemException("当前手机号未注册");
            return this.viewNegotiating(request, response, new ResultClient(userService.createAuthCode(String.valueOf(phone), SmsUtils.templateUpdatePasswordCode)));
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
            long uid = userService.loadByAccount(LoginUserPool.loginType_phone, phone, User.userType_user);
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
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
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
            return this.viewNegotiating(request, response,new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(e.getMessage()));
        }
    }



}

