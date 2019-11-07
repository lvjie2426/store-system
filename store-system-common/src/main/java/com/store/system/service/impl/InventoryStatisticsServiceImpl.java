package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.space.JdbcBaseDao;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientCategoryStatistics;
import com.store.system.client.ClientInventoryStatistics;
import com.store.system.dao.InventoryStatisticsDao;
import com.store.system.model.*;
import com.store.system.service.InventoryStatisticsService;
import com.store.system.service.ProductCategoryService;
import com.store.system.util.ArithUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName InventoryStatisticsServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/27 16:35
 * @Version 1.0
 **/
@Service
public class InventoryStatisticsServiceImpl implements InventoryStatisticsService {


    @Resource
    private InventoryStatisticsDao inventoryStatisticsDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private ProductCategoryService productCategoryService;

    @Override
    public Map<Long,List<ClientInventoryStatistics>> getDayList(long subId, List<Long> days) throws Exception {
        List<InventoryStatistics> list = Lists.newArrayList();
        for(Long day:days) {
            list.addAll(inventoryStatisticsDao.getSubList(subId, day));
        }
        return transformClient(list);
    }

    @Override
    public Map<Long,List<ClientInventoryStatistics>> searchSale(long startTime, long endTime, long subId) throws Exception {
        String sql = "SELECT  *  FROM `inventory_statistics` where  1=1 ";
        if (subId > 0) {
            sql = sql + " and `subId` = " + subId;
        }
        if (startTime > 0) {
            sql = sql + " and `day` > " + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `day` < " + endTime;
        }

        sql = sql + " order  by `day` desc";
        List<InventoryStatistics> inventoryStatistics = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper(InventoryStatistics.class));
        return transformClient(inventoryStatistics);
    }

    private Map<Long,List<ClientInventoryStatistics>> transformClient(List<InventoryStatistics> inventoryStatistics) throws Exception {
        List<ProductCategory> productCategories = productCategoryService.getAllList();
        Map<Long,List<ClientInventoryStatistics>> map=new HashMap<>();
        for(ProductCategory category:productCategories) {
            List<InventoryStatistics> list = Lists.newArrayList();
            List<ClientInventoryStatistics> clientList = Lists.newArrayList();
            for (InventoryStatistics statistics : inventoryStatistics) {
                if (statistics.getCid() == category.getId()) {
                    list.add(statistics);
                }
            }
            ClientInventoryStatistics client = transformClientList(list);
            client.setCName(category.getName());
            clientList.add(client);
            map.put(category.getId(),clientList);
        }
        return map;
    }


    private ClientInventoryStatistics transformClientList(List<InventoryStatistics> inventoryStatistics) throws Exception {
        ClientInventoryStatistics client = new ClientInventoryStatistics();
        int num = 0;
        double price = 0;
        for(InventoryStatistics statistics:inventoryStatistics){
            num += statistics.getNum();
            price = ArithUtils.add(price, statistics.getPrice());
        }
        client.setNum(num);
        client.setPrice(price);
        return client;
    }
}
