package com.store.system.client;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-10-24 15:51
 **/
@Data
public class ClientProductCategory   {

    private Map<String,List<ClientProductSPU>> map;

}
