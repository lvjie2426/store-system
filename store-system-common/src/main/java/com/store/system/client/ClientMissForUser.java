package com.store.system.client;

import com.store.system.model.User;
import lombok.Data;

import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-08-28 15:46
 **/
@Data
public class ClientMissForUser {


    private int tem;

    private List<ClientUser> list;
}
