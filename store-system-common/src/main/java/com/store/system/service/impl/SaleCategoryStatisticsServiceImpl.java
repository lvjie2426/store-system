package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientCategoryStatistics;
import com.store.system.dao.SaleCategoryStatisticsDao;
import com.store.system.model.OrderSku;
import com.store.system.model.ProductCategory;
import com.store.system.model.SaleCategoryStatistics;
import com.store.system.model.Subordinate;
import com.store.system.service.ProductCategoryService;
import com.store.system.service.SaleCategoryStatisticsService;
import com.store.system.util.ArithUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SaleCategoryStatisticsServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/20 14:52
 * @Version 1.0
 **/
@Service
public class SaleCategoryStatisticsServiceImpl implements SaleCategoryStatisticsService{

    @Resource
    private SaleCategoryStatisticsDao saleCategoryStatisticsDao;
    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SubordinateServiceImpl subordinateService;



    @Override
    public Map<Long,List<ClientCategoryStatistics>>  getDayList(long subId, List<Long> days) throws Exception {
        List<SaleCategoryStatistics> list=Lists.newArrayList();
        for (Long day : days) {
            list.addAll(saleCategoryStatisticsDao.getSubList(subId, day));
        }
        return transformClientSale(list,subId);
    }

    @Override
    public Map<Long,List<ClientCategoryStatistics>> searchSale(long startTime, long endTime, long subId) throws Exception {
        String sql = "SELECT  *  FROM `sale_category_statistics` where  1=1 ";
        if (subId > 0) {
            sql = sql + " and `subId` = " + subId;
        }
        if (startTime > 0) {
            sql = sql + " and `ctime` > " + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `ctime` < " + endTime;
        }

        sql = sql + " order  by ctime desc";
        List<SaleCategoryStatistics> saleStatistics = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper(SaleCategoryStatistics.class));
        return transformClientSale(saleStatistics,subId);
    }

    private Map<Long,List<ClientCategoryStatistics>> transformClientSale(List<SaleCategoryStatistics> saleCategoryStatistics, long subId) throws Exception {
        List<ProductCategory> productCategories = productCategoryService.getAllList();
        Subordinate subordinate = subordinateService.load(subId);
        double total=0;
        for(ProductCategory category:productCategories) {
            List<SaleCategoryStatistics> list = Lists.newArrayList();
            for (SaleCategoryStatistics statistics : saleCategoryStatistics) {
                if (statistics.getCid() == category.getId()) {
                    list.add(statistics);
                }
            }
            ClientCategoryStatistics client = transformClientList(list);
            total += client.getSale();
        }

        Map<Long,List<ClientCategoryStatistics>> map=new HashMap<>();
        for(ProductCategory category:productCategories){
            List<ClientCategoryStatistics> clientList = Lists.newArrayList();
            List<SaleCategoryStatistics> list = Lists.newArrayList();
            List<OrderSku> skus = Lists.newArrayList();
            for(SaleCategoryStatistics statistics:saleCategoryStatistics){
                if(statistics.getCid()==category.getId()){
                    list.add(statistics);
                    skus.addAll(statistics.getSalesLog());
                }
            }
            ClientCategoryStatistics client = transformClientList(list);
            client.setCName(category.getName());
            client.setSubName(subordinate.getName());
            clientList.add(client);
            if(total>0) {
                double rate = ArithUtils.div(client.getSale(), total, 2);
                client.setRate(rate);
            }

            int rate_0to100=0;
            int rate_100to500=0;
            int rate_500to1000=0;
            int rate_1000to2000=0;
            int rate_2000=0;
            for(OrderSku sku:skus){
                if(sku.getLastSubtotal()>0&&sku.getLastSubtotal()<=100){
                    rate_0to100++;
                }
                if(sku.getLastSubtotal()>100&&sku.getLastSubtotal()<=500){
                    rate_100to500++;
                }
                if(sku.getLastSubtotal()>500&&sku.getLastSubtotal()<=1000){
                    rate_500to1000++;
                }
                if(sku.getLastSubtotal()>1000&&sku.getLastSubtotal()<=2000){
                    rate_1000to2000++;
                }
                if(sku.getLastSubtotal()>2000){
                    rate_2000++;
                }
            }
            if(client.getNum()>0) {
                client.setRate_0to100(ArithUtils.div(rate_0to100, client.getNum(), 2));
                client.setRate_100to500(ArithUtils.div(rate_100to500, client.getNum(), 2));
                client.setRate_500to1000(ArithUtils.div(rate_500to1000, client.getNum(), 2));
                client.setRate_1000to2000(ArithUtils.div(rate_1000to2000, client.getNum(), 2));
                client.setRate_2000(ArithUtils.div(rate_2000, client.getNum(), 2));
            }
            map.put(category.getId(),clientList);
        }
        return map;
    }


    private ClientCategoryStatistics transformClientList(List<SaleCategoryStatistics> saleCategoryStatistics) throws Exception {
        ClientCategoryStatistics client = new ClientCategoryStatistics();
        double perPrice = 0;
        int num = 0;
        double sale = 0;
        for(SaleCategoryStatistics statistics:saleCategoryStatistics){
            num += statistics.getNum();
            sale = ArithUtils.add(sale, statistics.getSale());
            perPrice = ArithUtils.add(perPrice, statistics.getPerPrice());
        }
        client.setNum(num);
        client.setSale(sale);
        client.setPerPrice(perPrice);
        client.setStatistics(saleCategoryStatistics);
        return client;
    }

}
