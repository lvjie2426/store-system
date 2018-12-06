package com.store.system.util;



import com.store.system.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class UserUtils {

    public static void setUser(HttpServletRequest request, User user){
        request.setAttribute("user", user);
    }

    public static User getUser(HttpServletRequest request){
        return (User)request.getAttribute("user");

    }
}
