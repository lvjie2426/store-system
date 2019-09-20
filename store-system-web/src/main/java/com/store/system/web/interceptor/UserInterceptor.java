package com.store.system.web.interceptor;

import com.quakoo.baseFramework.json.JsonUtils;
import com.quakoo.baseFramework.secure.AESUtils;
import com.quakoo.baseFramework.secure.Base64Util;
import com.store.system.model.User;
import com.store.system.service.UserService;
import com.store.system.util.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserInterceptor extends HandlerInterceptorAdapter {
	private final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);


    private final static List<String> accessUrls = Arrays.asList("/login/in", "/login/verifyCode", "/main/exit",
            "/storage");


    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,X_Requested_With");
                String requestUri = request.getRequestURI();
        for(String accessUrl : accessUrls){
            if(requestUri.contains(accessUrl)) return true;
        }

        String token = request.getParameter("token");
        if(StringUtils.isBlank(token)){
            Cookie[] cookies=request.getCookies();
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    token=cookie.getValue();
                }
            }
        }

        if (StringUtils.isBlank(token)) {
            logger.info("login error token error!");
            response.setStatus(403);
            return false;
        }
        byte[] userJsonByte = null;
            userJsonByte = AESUtils.decrypt(Base64Util.decode(token), User.key);

        @SuppressWarnings("rawtypes")
        Map map = JsonUtils.parse(userJsonByte, Map.class);
        long id = Long.parseLong(map.get("id").toString());
        long rand = Long.parseLong(map.get("rand").toString());


        User user = userService.load(id);
        if (user == null || user.getRand() != rand || user.getStatus()==User.status_delete) {
            logger.info("login error!");
            response.setStatus(403);
            return false;
        }
        UserUtils.setUser(request, user);
        return true;
    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }
    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     *
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}
