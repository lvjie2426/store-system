package com.store.system.backend.controller;

import com.store.system.client.ClientUserOnLogin;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.service.UserService;
import com.store.system.util.VerifyCodeUtils;
import com.quakoo.baseFramework.redis.JedisX;
import com.quakoo.webframework.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;


@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    private static final String verifyCode = "user_verifyCode_";

    @Resource
    UserService userService;

    @Autowired(required = true)
    @Qualifier("cachePool")
    protected JedisX cache;


    @RequestMapping("/in")
    public ModelAndView in(HttpServletRequest request, HttpServletResponse response, final Model model)
            throws Exception {
        try {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            String userTypeStr = request.getParameter("userType");
            String code = request.getParameter("code1");
            String uuid = request.getParameter("uuid");
            int userType = User.userType_backendUser;
            if(StringUtils.isNotBlank(userTypeStr)){
                userType=Integer.parseInt(userTypeStr);
            }
            if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
                cache.delete(verifyCode + uuid);
                return this.viewNegotiating(request, response, new ResultClient("用户名密码错误"));
            }
            if (StringUtils.isBlank(uuid) || StringUtils.isBlank(code)) {
                cache.delete(verifyCode + uuid);
                return this.viewNegotiating(request, response, new ResultClient("验证码不正确"));
            }
            String oldCode = cache.getString(verifyCode + uuid);
            if (!code.equalsIgnoreCase(oldCode)) {
                cache.delete(verifyCode + uuid);
                return this.viewNegotiating(request, response, new ResultClient("验证码不正确"));
            }
            User user=new User();
            user.setUserName(userName);
            user.setPassword(password);
            user.setUserType(userType);
            ClientUserOnLogin clientUserOnLogin = userService.login(user, code);
            cache.delete(verifyCode + uuid);
            return this.viewNegotiating(request, response, new ResultClient(clientUserOnLogin));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/verifyCode")
    public void verifyCode(
            HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        VerifyCodeUtils vCode = new VerifyCodeUtils(120, 40, 4, 30);
        String code = vCode.getCode();
        String uuid = request.getParameter("uuid");
        cache.setString(verifyCode + uuid, 2 * 60, code);
        OutputStream out = response.getOutputStream();
        vCode.write(out);
    }


}
