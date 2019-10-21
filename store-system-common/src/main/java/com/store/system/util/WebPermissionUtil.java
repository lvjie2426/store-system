package com.store.system.util;


import com.store.system.model.Permission;
import com.store.system.service.WebPermission;

import java.util.List;

public class WebPermissionUtil {


    public static boolean hasSchoolLoginPermission(List<Permission> permissions){
        if(!Permission.hasPermission(permissions, WebPermission.adminLogin)){
            return false;
        }
        return true;
    }


}
