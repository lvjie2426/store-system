package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.dao.InventoryWarehouseDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryWarehouse;
import com.store.system.model.User;
import com.store.system.service.InventoryWarehouseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class InventoryWarehouseServiceImpl implements InventoryWarehouseService {

    @Resource
    private UserDao userDao;

    @Resource
    private InventoryWarehouseDao inventoryWarehouseDao;

    private TransformFieldSetUtils fieldSetUtils = new TransformFieldSetUtils(InventoryWarehouse.class);

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    private void check(InventoryWarehouse inventoryWarehouse) throws StoreSystemException {
        if(inventoryWarehouse.getSubid() == 0) throw new StoreSystemException("店铺不能为空");
        if(StringUtils.isBlank(inventoryWarehouse.getName())) throw new StoreSystemException("店铺名称不能为空");
    }

    @Override
    public InventoryWarehouse add(InventoryWarehouse inventoryWarehouse) throws Exception {
        check(inventoryWarehouse);
        inventoryWarehouse = inventoryWarehouseDao.insert(inventoryWarehouse);
        return inventoryWarehouse;
    }

    @Override
    public boolean update(InventoryWarehouse inventoryWarehouse) throws Exception {
        check(inventoryWarehouse);
        boolean res = inventoryWarehouseDao.update(inventoryWarehouse);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        InventoryWarehouse inventoryWarehouse = inventoryWarehouseDao.load(id);
        if(null != inventoryWarehouse) {
            inventoryWarehouse.setStatus(InventoryWarehouse.status_delete);
            return inventoryWarehouseDao.update(inventoryWarehouse);
        }
        return false;
    }

    @Override
    public List<ClientInventoryWarehouse> getAllList(long subid) throws Exception {
        List<InventoryWarehouse> list = inventoryWarehouseDao.getAllList(subid, InventoryWarehouse.status_nomore);
        Set<Long> adminids = fieldSetUtils.fieldList(list, "adminid");
        for(Iterator<Long> it = adminids.iterator(); it.hasNext();) {
            if(it.next() == 0) it.remove();
        }
        Map<Long, User> userMap = Maps.newHashMap();
        if(adminids.size() > 0) {
            List<User> users = userDao.load(Lists.newArrayList(adminids));
            userMap = userMapUtils.listToMap(users, "id");
        }
        List<ClientInventoryWarehouse> res = Lists.newArrayList();
        for(InventoryWarehouse one : list) {
            ClientInventoryWarehouse client = new ClientInventoryWarehouse(one);
            User user = userMap.get(client.getAdminid());
            if(null != user) client.setAdminName(user.getName());
            res.add(client);
        }
        return res;
    }
}
