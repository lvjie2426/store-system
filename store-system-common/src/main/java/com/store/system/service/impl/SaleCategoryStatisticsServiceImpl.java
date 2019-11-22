package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.*;
import com.store.system.dao.SaleCategoryStatisticsDao;
import com.store.system.model.*;
import com.store.system.service.BusinessOrderService;
import com.store.system.service.ProductCategoryService;
import com.store.system.service.SaleCategoryStatisticsService;
import com.store.system.util.ArithUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    private BusinessOrderService businessOrderService;
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
    public ClientCategoryStatistics getDayListSum(long subId, List<Long> days) throws Exception {
        List<SaleCategoryStatistics> list=Lists.newArrayList();
        for (Long day : days) {
            list.addAll(saleCategoryStatisticsDao.getSubList(subId, day));
        }
        Map<Long,List<ClientCategoryStatistics>> map = transformClientSale(list,subId);
        return transformClientSum(map,subId);
    }

    @Override
    public ClientCategoryStatistics searchSaleSum(long startTime, long endTime, long subId) throws Exception {
        String sql = "SELECT  *  FROM `sale_category_statistics` where  1=1 ";
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
        List<SaleCategoryStatistics> saleStatistics = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper(SaleCategoryStatistics.class));
        Map<Long,List<ClientCategoryStatistics>> map = transformClientSale(saleStatistics,subId);
        return transformClientSum(map,subId);
    }

    @Override
    public List<ClientOrderSku> getDetail(long startTime, long endTime, List<Long> subIds, long cid) throws Exception {
        List<SaleCategoryStatistics> saleStatistics = Lists.newArrayList();
        for(Long subId:subIds) {
            String sql = "SELECT  *  FROM `sale_category_statistics` where  1=1 ";
            if (subId > 0) {
                sql = sql + " and `subId` = " + subId;
            }
            if (cid > 0) {
                sql = sql + " and `cid` = " + cid;
            }
            if (startTime > 0) {
                sql = sql + " and `day` > " + startTime;
            }
            if (endTime > 0) {
                sql = sql + " and `day` < " + endTime;
            }

            sql = sql + " order  by `day` desc";
            saleStatistics.addAll(jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper(SaleCategoryStatistics.class)));

        }
        List<OrderSku> skuList = Lists.newArrayList();
        for (SaleCategoryStatistics statistics : saleStatistics) {
            skuList.addAll(statistics.getSalesLog());
        }
        List<ClientOrderSku> clientOrderSkus=businessOrderService.transformSKUClient(skuList);
        int total=0;
        for(ClientOrderSku sku:clientOrderSkus){
            total += sku.getNum();
        }
        List<ClientOrderSku> res = getNewList(clientOrderSkus,total);
        sort(res);
        return res;
    }

    private void sort(List<ClientOrderSku> list){
        Collections.sort(list, new Comparator<ClientOrderSku>(){
            public int compare(ClientOrderSku p1, ClientOrderSku p2) {
                if(p1.getLastSubtotal() < p2.getLastSubtotal()){
                    return 1;
                }
                if(p1.getLastSubtotal() == p2.getLastSubtotal()){
                    return 0;
                }
                return -1;
            }
        });
    }

    private List<ClientOrderSku> getNewList(List<ClientOrderSku> oldList,int total) throws IllegalAccessException {
        HashMap<Long,ClientOrderSku> tempMap = new HashMap<Long,ClientOrderSku>();
        //去掉重复的key
        for(ClientOrderSku sku : oldList){
            long spuid = sku.getSpuId();
            //containsKey(Object key)该方法判断Map集合中是否包含指定的键名，如果包含返回true，不包含返回false
            //containsValue(Object value)该方法判断Map集合中是否包含指定的键值，如果包含返回true，不包含返回false
            if(tempMap.containsKey(spuid)){
                ClientOrderSku newSku = new ClientOrderSku();
                newSku.setSkuId(sku.getSkuId());
                newSku.setSpuId(sku.getSpuId());
                newSku.setNum(tempMap.get(spuid).getNum()+sku.getNum());
                newSku.setCode(sku.getCode());
                newSku.setName(sku.getName());
                newSku.setPrice(sku.getPrice());
                newSku.setIntegralPrice(sku.getIntegralPrice());
                newSku.setDiscount(sku.getDiscount());
                newSku.setSubtotal(sku.getSubtotal());
                newSku.setLastSubtotal(tempMap.get(spuid).getLastSubtotal()+sku.getLastSubtotal());
                newSku.setQualityType(sku.getQualityType());
                newSku.setOptStatus(sku.getOptStatus());
                newSku.setP_properties_value(sku.getP_properties_value());
                newSku.setK_properties_value(sku.getK_properties_value());
                newSku.setProviderName(sku.getProviderName());
                newSku.setCategoryName(sku.getCategoryName());
                newSku.setBrandName(sku.getBrandName());
                newSku.setSeriesName(sku.getSeriesName());
                //占比计算 是按照数量计算的
                if(total>0) {
                    double rate = ArithUtils.div((double) sku.getNum(), (double) total, 3);
                    newSku.setRate(rate * 100);
                }
                tempMap.put(spuid,newSku);
            }else{
                tempMap.put(spuid,sku);
            }
        }
        //去除重复key的list
        List<ClientOrderSku> newList = new ArrayList<ClientOrderSku>();
        for(Long temp:tempMap.keySet()){
            newList.add(tempMap.get(temp));
        }
        return newList;
    }


    @Override
    public Map<Long,List<ClientCategoryStatistics>> searchSale(long startTime, long endTime, long subId) throws Exception {
        String sql = "SELECT  *  FROM `sale_category_statistics` where  1=1 ";
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
            //rate==一个分类的销售额占所有分类的销售额的占比
            if(total>0) {
                double rate = ArithUtils.div(client.getSale(), total, 2);
                client.setRate(rate);
            }

            int num_0to100=0;
            int num_100to500=0;
            int num_500to1000=0;
            int num_1000to2000=0;
            int num_2000=0;
            for(OrderSku sku:skus){
                if(sku.getLastSubtotal()>0&&sku.getLastSubtotal()<=100){
                    num_0to100++;
                }
                if(sku.getLastSubtotal()>100&&sku.getLastSubtotal()<=500){
                    num_100to500++;
                }
                if(sku.getLastSubtotal()>500&&sku.getLastSubtotal()<=1000){
                    num_500to1000++;
                }
                if(sku.getLastSubtotal()>1000&&sku.getLastSubtotal()<=2000){
                    num_1000to2000++;
                }
                if(sku.getLastSubtotal()>2000){
                    num_2000++;
                }
            }
            //销售额分类占比==分类销售区间内的个数占比所有分类销售的数量
            if(client.getNum()>0) {
                client.setRate_0to100(ArithUtils.div(num_0to100, client.getNum(), 2));
                client.setRate_100to500(ArithUtils.div(num_100to500, client.getNum(), 2));
                client.setRate_500to1000(ArithUtils.div(num_500to1000, client.getNum(), 2));
                client.setRate_1000to2000(ArithUtils.div(num_1000to2000, client.getNum(), 2));
                client.setRate_2000(ArithUtils.div(num_2000, client.getNum(), 2));
                client.setNum_0to100(num_0to100);
                client.setNum_100to500(num_100to500);
                client.setNum_500to1000(num_500to1000);
                client.setNum_1000to2000(num_1000to2000);
                client.setNum_2000(num_2000);
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

    private ClientCategoryStatistics transformClientSum(Map<Long,List<ClientCategoryStatistics>> map, long subId) throws Exception {
        Subordinate subordinate = subordinateService.load(subId);
        ClientCategoryStatistics res = new ClientCategoryStatistics();
        List<ClientCategoryStatistics> list = Lists.newArrayList();
        for(Map.Entry<Long,List<ClientCategoryStatistics>> entry:map.entrySet()){
            list.addAll(entry.getValue());
        }
        double sale=0;
        double perPrice=0;
        int num=0;
        double rate=0;
        double rate_0to100=0;
        double rate_100to500=0;
        double rate_500to1000=0;
        double rate_1000to2000=0;
        double rate_2000=0;
        int num_0to100=0;
        int num_100to500=0;
        int num_500to1000=0;
        int num_1000to2000=0;
        int num_2000=0;
        for(ClientCategoryStatistics statistics:list){
            sale += statistics.getSale();
            perPrice += statistics.getPerPrice();
            num += statistics.getNum();
            rate += statistics.getRate();
            rate_0to100 += statistics.getRate_0to100();
            rate_100to500 += statistics.getRate_100to500();
            rate_500to1000 += statistics.getRate_500to1000();
            rate_1000to2000 += statistics.getRate_1000to2000();
            rate_2000 += statistics.getRate_2000();
            num_0to100 += statistics.getNum_0to100();
            num_100to500 += statistics.getNum_100to500();
            num_500to1000 += statistics.getNum_500to1000();
            num_1000to2000 += statistics.getNum_1000to2000();
            num_2000 += statistics.getNum_2000();
        }
        res.setSale(sale);
        res.setPerPrice(perPrice);
        res.setNum(num);
        res.setRate(rate);
        res.setRate_0to100(rate_0to100);
        res.setRate_100to500(rate_100to500);
        res.setRate_500to1000(rate_500to1000);
        res.setRate_1000to2000(rate_1000to2000);
        res.setRate_2000(rate_2000);
        res.setNum_0to100(num_0to100);
        res.setNum_100to500(num_100to500);
        res.setNum_500to1000(num_500to1000);
        res.setNum_1000to2000(num_1000to2000);
        res.setNum_2000(num_2000);
        res.setSubName(subordinate.getName());
        return res;
    }

}
