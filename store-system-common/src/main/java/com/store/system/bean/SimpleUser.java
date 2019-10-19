package com.store.system.bean;


import com.store.system.model.User;
import lombok.Data;

@Data
public class SimpleUser {

    private long id;
    private String name;
    private String icon;
    private String phone;

    public SimpleUser(){
    }

    public SimpleUser(User user){
        if(user!=null) {
            this.id = user.getId();
            this.name = user.getName();
            this.icon = user.getIcon();
            this.phone = user.getPhone();
        }
    }

}
