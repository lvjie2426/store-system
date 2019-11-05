package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.*;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.*;
import com.store.system.util.CodeUtil;
import com.store.system.util.Reflections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private TransformFieldSetUtils nameFieldSetUtils = new TransformFieldSetUtils(ProductPropertyName.class);

    private RowMapperHelp<ProductSPU> spuRowMapper = new RowMapperHelp<>(ProductSPU.class);

    private TransformMapUtils providerMapUtils = new TransformMapUtils(ProductProvider.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

    private TransformMapUtils subordinateMapUtils = new TransformMapUtils(Subordinate.class);

    private TransformMapUtils productSPUMapUtils = new TransformMapUtils(ProductSPU.class);

    private TransformMapUtils commissionMapUtils = new TransformMapUtils(Commission.class);

    private TransformMapUtils nameMapUtils = new TransformMapUtils(ProductPropertyName.class);

    private TransformMapUtils valueMapUtils = new TransformMapUtils(ProductPropertyValue.class);


    @Resource
    private ProductPropertyNameDao productPropertyNameDao;

    @Resource
    private ProductPropertyValueDao productPropertyValueDao;

    @Resource
    private ProductSPUDao productSPUDao;

    @Resource
    private ProductSKUDao productSKUDao;

    @Resource
    private ProductProviderDao productProviderDao;

    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductBrandDao productBrandDao;

    @Resource
    private ProductSeriesDao productSeriesDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private UserGradeDao userGradeDao;

    @Resource
    private InventoryDetailDao inventoryDetailDao;

    @Resource
    private InventoryWarehouseDao inventoryWarehouseDao;

    @Resource
    private UserGradeCategoryDiscountDao userGradeCategoryDiscountDao;

    @Resource
    private UserService userService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private UserGradeCategoryDiscountService userGradeCategoryDiscountService;

    @Resource
    private ProductBrandService productBrandService;

    @Resource
    private ProductSeriesService productSeriesService;

    @Resource
    private CommissionService commissionService;

    @Resource
    private CommissionDao commissionDao;


    private void checkSPU(ProductSPU productSPU) throws StoreSystemException {
        long subid = productSPU.getSubid();
        long pid = productSPU.getPid();
        long cid = productSPU.getCid();
        long bid = productSPU.getBid();
        long sid = productSPU.getSid();
        Map<Long, Object> properties = productSPU.getProperties();
        if (subid == 0) throw new StoreSystemException("SPU店铺不能为空");
        if (cid == 0) throw new StoreSystemException("SPU类目不能为空");
        if (bid == 0) throw new StoreSystemException("SPU品牌不能为空");

        int count = productSPUDao.getCount(subid, pid, cid, bid, sid);
        if (count > 0) throw new StoreSystemException("已添加此产品的SPU");


        List<ProductPropertyName> spuNames = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for (Iterator<ProductPropertyName> it = spuNames.iterator(); it.hasNext(); ) {
            ProductPropertyName name = it.next();
            if (name.getType() != ProductPropertyName.type_spu) it.remove();
        }
        Set<Long> nameIds = nameFieldSetUtils.fieldList(spuNames, "id");
        for (long nameId : nameIds) {
            if (!properties.keySet().contains(nameId)) throw new StoreSystemException("SPU缺少关键属性");
        }
    }

    @Override
    public boolean updateSPU(ProductSPU productSPU) throws Exception {
        checkSPU(productSPU);
        Subordinate subordinate = subordinateDao.load(productSPU.getSubid());
        if (subordinate.getPid() > 0) productSPU.setSubid(subordinate.getPid());
        boolean res = productSPUDao.update(productSPU);
        return res;
    }

    @Override
    public boolean updateSKU(ProductSKU productSKU) throws Exception {
        checkSKU(productSKU);
        boolean res = productSKUDao.update(productSKU);
        return res;
    }

    @Override
    public ProductSPU addSPU(ProductSPU productSPU) throws Exception {
        checkSPU(productSPU);
        Subordinate subordinate = subordinateDao.load(productSPU.getSubid());
        if (subordinate.getPid() > 0) productSPU.setSubid(subordinate.getPid());
        productSPU = productSPUDao.insert(productSPU);
        return productSPU;
    }

    private void checkSKU(ProductSKU productSKU) throws StoreSystemException {
        Map<Long, Object> properties = productSKU.getProperties();
        long spuid = productSKU.getSpuid();
        String code = productSKU.getCode();
        if (spuid == 0) throw new StoreSystemException("SPU不能为空");
        if (StringUtils.isBlank(code)) throw new StoreSystemException("产品编码不能为空");

        int count = productSKUDao.getCount(spuid, code);
        if (count > 0) throw new StoreSystemException("已添加此产品的SKU");

        ProductSPU productSPU = productSPUDao.load(productSKU.getSpuid());
        long cid = productSPU.getCid();
        List<ProductPropertyName> skuNames = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for (Iterator<ProductPropertyName> it = skuNames.iterator(); it.hasNext(); ) {
            ProductPropertyName name = it.next();
            if (name.getType() != ProductPropertyName.type_sku) it.remove();
        }
        Set<Long> nameIds = nameFieldSetUtils.fieldList(skuNames, "id");
        for (long nameId : nameIds) {
            if (!properties.keySet().contains(nameId)) throw new StoreSystemException("SKU缺少关键属性");
        }
    }

    @Override
    public ProductSKU addSKU(ProductSKU productSKU) throws Exception {
        checkSKU(productSKU);
        productSKU = productSKUDao.insert(productSKU);
        return productSKU;
    }

    @Override
    @Transactional
    public void change(ProductSPU productSPU, List<ProductSKU> addProductSKUList, List<ProductSKU> updateProductSKUList,
                       List<Long> delSkuids, String brandName, String seriesName, List<Commission> commissions) throws Exception {
        List<ProductSKU> productSKUList = Lists.newArrayList(addProductSKUList);
        productSKUList.addAll(updateProductSKUList);
        check(productSPU, productSKUList, null,null,false);
        if(StringUtils.isNotBlank(brandName)){
            ProductBrand brand = new ProductBrand();
            brand.setName(brandName);
            brand = productBrandService.add(brand);
            productSPU.setBid(brand.getId());
        }

        if(StringUtils.isNotBlank(seriesName)&&productSPU.getBid()>0){
            ProductSeries series = new ProductSeries();
            series.setName(seriesName);
            series.setBid(productSPU.getBid());
            series = productSeriesService.add(series);
            productSPU.setSid(series.getId());
        }

        if(StringUtils.isNotBlank(brandName)&&StringUtils.isNotBlank(seriesName)){
            ProductBrand brand = new ProductBrand();
            brand.setName(brandName);
            brand = productBrandService.add(brand);
            ProductSeries series = new ProductSeries();
            series.setName(seriesName);
            series.setBid(brand.getId());
            series = productSeriesService.add(series);
            productSPU.setBid(brand.getId());
            productSPU.setSid(series.getId());
        }
//        int count = productSPUDao.getCount(productSPU.getSubid(), productSPU.getPid(),
//                productSPU.getCid(), productSPU.getBid(), productSPU.getSid());
//        if(count==0) {
            productSPU.setNowRemind(productSPU.getNowRemind());
            productSPU.setTotalRemind(productSPU.getTotalRemind());
            productSPU.setUnderRemind(productSPU.getUnderRemind());
            productSPUDao.update(productSPU);
//        }

        long spuid = productSPU.getId();
        if (addProductSKUList.size() > 0) {
            for (ProductSKU productSKU : addProductSKUList) {
                productSKU.setSpuid(spuid);
                productSKUDao.insert(productSKU);
            }
        }

        if (updateProductSKUList.size() > 0) {
            for (ProductSKU productSKU : updateProductSKUList) {
                productSKUDao.update(productSKU);
            }
        }

        if (delSkuids.size() > 0) {
            List<ProductSKU> delProductSKUList = productSKUDao.load(delSkuids);
            for (ProductSKU productSKU : delProductSKUList) {
                if (null != productSKU) {
                    productSKU.setStatus(ProductSKU.status_delete);
                    productSKUDao.update(productSKU);
                }
            }
        }
        if(commissions.size()>0) {
            for (Commission commission : commissions) {
                commissionService.update(commission);
            }
        }
    }

    private void check(ProductSPU productSPU, List<ProductSKU> productSKUList, String brandName, String seriesName, boolean isAdd) throws Exception {
        Map<Long, Object> properties = productSPU.getProperties();
        if (productSPU.getSubid() == 0) throw new StoreSystemException("SPU店铺不能为空");
        if (productSPU.getCid() == 0) throw new StoreSystemException("SPU类目不能为空");

        if(isAdd) {
            if (productSPU.getSid() == 0 && StringUtils.isBlank(seriesName)) throw new StoreSystemException("SPU系列不能为空");
            if (productSPU.getBid() == 0 && StringUtils.isBlank(brandName)) throw new StoreSystemException("SPU品牌不能为空");
            int count = productSPUDao.getCount(productSPU.getSubid(), productSPU.getPid(),
                    productSPU.getCid(), productSPU.getBid(), productSPU.getSid());
            if (count > 0) throw new StoreSystemException("已存在此产品的SPU,请重新添加");
        }

        long cid = productSPU.getCid();
        List<ProductPropertyName> names = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        Set<Long> spuNames = Sets.newHashSet();
        Set<Long> skuNames = Sets.newHashSet();
        for (ProductPropertyName name : names) {
            if (name.getType() == ProductPropertyName.type_spu) spuNames.add(name.getId());
            else skuNames.add(name.getId());
        }

        for (long nameId : spuNames) {
            if (!properties.keySet().contains(nameId)) throw new StoreSystemException("SPU缺少关键属性");
        }

        if(productSKUList.size()>0) {
            for (ProductSKU productSKU : productSKUList) {
                properties = productSKU.getProperties();
                for (long nameId : skuNames) {
                    if (!properties.keySet().contains(nameId)) throw new StoreSystemException("SKU缺少关键属性");
                }
            }
        }
    }

    @Override
    @Transactional
    public void add(ProductSPU productSPU, List<ProductSKU> productSKUList, String brandName, String seriesName, List<Commission> commissions) throws Exception {
        check(productSPU, productSKUList, brandName, seriesName,true);
        if(StringUtils.isNotBlank(brandName)){
            ProductBrand brand = new ProductBrand();
            brand.setName(brandName);
            brand = productBrandService.add(brand);
            productSPU.setBid(brand.getId());
        }

        if(StringUtils.isNotBlank(seriesName)&&productSPU.getBid()>0){
            ProductSeries series = new ProductSeries();
            series.setName(seriesName);
            series.setBid(productSPU.getBid());
            series = productSeriesService.add(series);
            productSPU.setSid(series.getId());
        }

        if(StringUtils.isNotBlank(brandName)&&StringUtils.isNotBlank(seriesName)){
            ProductBrand brand = new ProductBrand();
            brand.setName(brandName);
            brand = productBrandService.add(brand);
            ProductSeries series = new ProductSeries();
            series.setName(seriesName);
            series.setBid(brand.getId());
            series = productSeriesService.add(series);
            productSPU.setBid(brand.getId());
            productSPU.setSid(series.getId());
        }
        productSPU = productSPUDao.insert(productSPU);
        if (null == productSPU) throw new StoreSystemException("SPU添加错误");
        long spuid = productSPU.getId();
        if (productSKUList.size() > 0) {
            for (ProductSKU productSKU : productSKUList) {
                productSKU.setSpuid(spuid);
                productSKU.setCode(String.valueOf(CodeUtil.getRandom(10)));
                productSKUDao.insert(productSKU);
            }
        }
        if(commissions.size()>0) {
            for (Commission commission : commissions) {
                commission.setSpuId(spuid);
                commissionService.add(commission);
            }
        }

    }

    @Override
    public ClientProductSPU loadSPU(long id) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        ClientProductSPU clientProductSPU = transformClient(productSPU);
        List<ProductSKU> productSKUList = productSKUDao.getAllList(id, ProductSKU.status_nomore);
        List<ClientProductSKU> skuList = Lists.newArrayList();
        for (ProductSKU one : productSKUList) {
            ClientProductSKU clientProductSKU = new ClientProductSKU(one);
            Map<Object, Object> map_value = Maps.newHashMap();
            map_value = getProperties(one,clientProductSKU,"p_properties_value");
            clientProductSKU.setP_properties_value(map_value);
            skuList.add(clientProductSKU);
        }

       List<UserGradeCategoryDiscount> userGradeCategoryDiscountList= userGradeCategoryDiscountService.getAllBySPUId(id);
        List<ClientUserGradeCategoryDiscount> clientUserGradeCategoryDiscounts=new ArrayList<>();
        for(UserGradeCategoryDiscount userGradeCategoryDiscount:userGradeCategoryDiscountList){
            ClientUserGradeCategoryDiscount clientUserGradeCategoryDiscount=new ClientUserGradeCategoryDiscount(userGradeCategoryDiscount);
            UserGrade load = userGradeDao.load(userGradeCategoryDiscount.getUgid());
            clientUserGradeCategoryDiscount.setUgName(load.getTitle());
            clientUserGradeCategoryDiscounts.add(clientUserGradeCategoryDiscount);

        }

        clientProductSPU.setSkuList(skuList);
        clientProductSPU.setUserGradeCategoryDiscountList(clientUserGradeCategoryDiscounts);
        return clientProductSPU;
    }

    private ClientProductSPU transformClientSPU(ClientProductSPU clientProductSPU) {
        ClientProductSPU clientProductSPU1=clientProductSPU;
        List<ClientProductSKU> properties = clientProductSPU1.getSkuList();
        // sku 属性。

        return  clientProductSPU ;
    }

    private List<ClientProductSPU> transformClients(List<ProductSPU> productSPUList,long subId) throws Exception {
        List<ClientProductSPU> res = Lists.newArrayList();
        if (productSPUList.size() == 0) return res;
        Set<Long> pids = Sets.newHashSet();
        Set<Long> cids = Sets.newHashSet();
        Set<Long> bids = Sets.newHashSet();
        Set<Long> sids = Sets.newHashSet();
        for (ProductSPU one : productSPUList) {
            pids.add(one.getPid());
            cids.add(one.getCid());
            bids.add(one.getBid());
            sids.add(one.getSid());
        }
        List<ProductProvider> providers = productProviderDao.load(Lists.newArrayList(pids));
        Map<Long, ProductProvider> providerMap = providerMapUtils.listToMap(providers, "id");
        List<ProductCategory> categories = productCategoryDao.load(Lists.newArrayList(cids));
        Map<Long, ProductCategory> categoryMap = categoryMapUtils.listToMap(categories, "id");
        List<ProductBrand> brands = productBrandDao.load(Lists.newArrayList(bids));
        Map<Long, ProductBrand> brandMap = brandMapUtils.listToMap(brands, "id");
        List<ProductSeries> seriesList = productSeriesDao.load(Lists.newArrayList(sids));
        Map<Long, ProductSeries> seriesMap = seriesMapUtils.listToMap(seriesList, "id");
        for (ProductSPU one : productSPUList) {
            ClientProductSPU client = new ClientProductSPU(one);
            ProductProvider provider = providerMap.get(client.getPid());
            if (null != provider) client.setProviderName(provider.getName());
            ProductCategory category = categoryMap.get(client.getCid());
            if (null != category) client.setCategoryName(category.getName());
            ProductBrand brand = brandMap.get(client.getBid());
            if (null != brand) client.setBrandName(brand.getName());
            ProductSeries series = seriesMap.get(client.getSid());
            if (null != series) client.setSeriesName(series.getName());

            List<Commission> commissions = commissionDao.getAllList(subId, one.getId());
            client.setCommissions(commissions);

            Map<Object, Object> map_value = Maps.newHashMap();
            map_value = getProperties(one,client,"spu_properties_value");
            client.setSpu_properties_value(map_value);
            res.add(client);
        }
        return res;
    }

    private ClientProductSPU transformClient(ProductSPU productSPU) throws Exception {
        ClientProductSPU clientProductSPU = new ClientProductSPU(productSPU);
        ProductProvider provider = productProviderDao.load(clientProductSPU.getPid());
        if (null != provider) clientProductSPU.setProviderName(provider.getName());
        ProductCategory category = productCategoryDao.load(clientProductSPU.getCid());
        if (null != category) clientProductSPU.setCategoryName(category.getName());
        ProductBrand brand = productBrandDao.load(clientProductSPU.getBid());
        if (null != brand) clientProductSPU.setBrandName(brand.getName());
        ProductSeries series = productSeriesDao.load(clientProductSPU.getSid());
        if (null != series) clientProductSPU.setSeriesName(series.getName());
        return clientProductSPU;
    }

    @Override
    public boolean delSKU(long id) throws Exception {
        ProductSKU productSKU = productSKUDao.load(id);
        if (null != productSKU) {
            productSKU.setStatus(ProductSKU.status_delete);
            return productSKUDao.update(productSKU);
        }
        return false;
    }

    @Override
    public boolean delSPU(long id) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        if (null != productSPU) {
            productSPU.setStatus(ProductSPU.status_delete);
            return productSPUDao.update(productSPU);
        }
        return false;
    }

    @Override
    public Pager getSPUBackPager(Pager pager, long subid, long cid, long pid, long bid, long sid, int type,String name,int saleStatus, long subId) throws Exception {
        String sql = "SELECT * FROM `product_spu` where `status` = " + ProductSPU.status_nomore;
        String sqlCount = "SELECT COUNT(id) FROM `product_spu` where `status` = " + ProductSPU.status_nomore;
        String limit = " limit %d , %d ";
        if (subid > 0) {
            sql += " and subid = " + subid;
            sqlCount += " and subid = " + subid;
        }
        if (cid > 0) {
            sql += " and cid = " + cid;
            sqlCount += " and cid = " + cid;
        }
        if (pid > 0) {
            sql += " and pid = " + pid;
            sqlCount += " and pid = " + pid;
        }
        if (bid > 0) {
            sql += " and bid = " + bid;
            sqlCount += " and bid = " + bid;
        }
        if (sid > 0) {
            sql += " and sid = " + sid;
            sqlCount += " and sid = " + sid;
        }
        if (type >= 0) {
            sql += " and type = " + type;
            sqlCount += " and type = " + type;
        }
        if (StringUtils.isNotBlank(name)) {
            sql += " and `name` like '%" + name + "%'";
            sqlCount += " and `name` like '%" + name + "%'";
        }
        if (saleStatus > -1) {
            sql += " and saleStatus = " + saleStatus;
            sqlCount += " and saleStatus = " + saleStatus;
        }

        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ProductSPU> productSPUList = this.jdbcTemplate.query(sql, spuRowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(transformClients(productSPUList,subId));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public ClientProductSPU selectSPU(long subid, long pid, long cid, long bid, long sid) throws Exception {
        int count = productSPUDao.getCount(subid, pid, cid, bid, sid);
        if (count == 0) throw new StoreSystemException("没有此类产品");
        List<ProductSPU> productSPUList = productSPUDao.getAllList(subid, pid, cid, bid, sid);
        ProductSPU productSPU = productSPUList.get(0);
        ClientProductSPU clientProductSPU = transformClient(productSPU);
        return clientProductSPU;
    }

    @Override
    public ClientProductSPU selectSPU(long subid,  long cid, long bid, long sid) throws Exception {
        List<ProductSPU> productSPUList = productSPUDao.getAllList(subid, cid, bid, sid);
        ProductSPU productSPU = productSPUList.get(0);
        ClientProductSPU clientProductSPU = transformClient(productSPU);
        return clientProductSPU;
    }

    @Override
    public Pager getSaleSPUBackPager(Pager pager, long pSubid, long subid, long cid, long bid, int type) throws Exception {
        String sql = "SELECT * FROM `product_spu` where `status` = " + ProductSPU.status_nomore + " and subid = " + pSubid;
        String sqlCount = "SELECT COUNT(id) FROM `product_spu` where `status` = " + ProductSPU.status_nomore + " and subid = " + pSubid;
        String limit = " limit %d , %d ";
        if (cid > 0) {
            sql += " and cid = " + cid;
            sqlCount += " and cid = " + cid;
        }
        if (bid > 0) {
            sql += " and bid = " + bid;
            sqlCount += " and bid = " + bid;
        }
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ProductSPU> productSPUList = this.jdbcTemplate.query(sql, spuRowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientProductSPU> data = transformClients(productSPUList,0);
        for (ClientProductSPU one : data) {
            int num = 0;
            List<InventoryDetail> details = inventoryDetailDao.getAllListBySubAndSPU(subid, one.getId());
            for (InventoryDetail detail : details) {
                num += detail.getNum();
            }
            one.setCanUseNum(num);
        }
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getSaleSPUPager(final Pager pager, final long pSubid, final long subid,
                                 final int type, final String brandSeries) throws Exception {
        return new PagerRequestService<ProductSPU>(pager, 0) {
            @Override
            public List<ProductSPU> step1GetPageResult(String cursor, int size) throws Exception {
                String sql = "select ps.* from `product_spu` as ps LEFT JOIN `product_brand` as pb ON ps.bid=pb.id " +
                        " LEFT JOIN `product_series` as s ON ps.sid=s.id where 1=1";
                {
                    sql = sql + " and ps.`status` = " + ProductSPU.status_nomore + " and ps.`subid` = " + pSubid;
                }
                {
                    sql = sql + " and ps.`type` = " + ProductSPU.type_common;
                }
                if (StringUtils.isNotBlank(brandSeries)) {
                    sql = sql + " and pb.`name` like '%" + brandSeries + "%'" + " or s.`name` like '%" + brandSeries + "%'";
                }
                long ctimeCursor = Long.parseLong(cursor);
                if (ctimeCursor == 0) ctimeCursor = Long.MAX_VALUE;
                sql = sql + " and ps.`ctime` <= " + ctimeCursor + " order by ps.ctime desc limit " + size;
                return jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<ProductSPU>(ProductSPU.class));
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<ProductSPU> step3FilterResult(List<ProductSPU> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }

            @Override
            public List<?> step4TransformData(List<ProductSPU> unTransformDatas, PagerSession session) throws Exception {
                List<ClientProductSPU> data = transformClients(unTransformDatas,subid);
                for (ClientProductSPU one : data) {
                    int num = 0;
                    List<InventoryDetail> details = inventoryDetailDao.getAllListBySubAndSPU(subid, one.getId());
                    for (InventoryDetail detail : details) {
                        num += detail.getNum();
                    }
                    one.setCanUseNum(num);
                }
                return data;
            }
        }.getPager();
    }

    @Override
    public List<ClientProductSKU> getSaleSKUAllList(long subid, long spuid, long uid) throws Exception {
        List<ProductSKU> list = productSKUDao.getAllList(spuid, ProductSKU.status_nomore);
        List<ClientProductSKU> res = Lists.newArrayList();
        Set<Long> wids = Sets.newHashSet();
        Set<Long> sids = Sets.newHashSet();
        UserGradeCategoryDiscount discount = new UserGradeCategoryDiscount();
        //商品的会员折扣
        if(uid>0) {
            User user = userService.load(uid);
            discount.setSpuid(spuid);
            discount.setUgid(user.getUserGradeId());
            discount = userGradeCategoryDiscountDao.load(discount);
        }
        for (ProductSKU one : list) {
            ClientProductSKU client = new ClientProductSKU(one);
            int num = 0;
            List<InventoryDetail> allDetails = inventoryDetailDao.getAllListBySKU(one.getId());
            List<ClientInventoryDetail> details = Lists.newArrayList();
            List<ClientInventoryDetail> otherDetails = Lists.newArrayList();
            for (InventoryDetail detail : allDetails) {
                wids.add(detail.getWid());
                sids.add(detail.getSubid());

                ClientInventoryDetail clientInventoryDetail = new ClientInventoryDetail(detail);
                if (detail.getSubid() == subid) {
                    details.add(clientInventoryDetail);
                    num += detail.getNum();
                } else {
                    otherDetails.add(clientInventoryDetail);
                }
            }
            Map<Object, Object> map_value = Maps.newHashMap();
            map_value = getProperties(one,client,"p_properties_value");
            client.setP_properties_value(map_value);
            client.setDetails(details);
            client.setOtherDetails(otherDetails);
            client.setCanUseNum(num);
            if(discount!=null)
                client.setDiscount(discount.getDiscount());
            res.add(client);
        }
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(sids));
        Map<Long, Subordinate> subordinateMap = subordinateMapUtils.listToMap(subordinates, "id");

        for (ClientProductSKU one : res) {
            List<ClientInventoryDetail> details = one.getDetails();
            for (ClientInventoryDetail detail : details) {
                InventoryWarehouse warehouse = warehouseMap.get(detail.getWid());
                if (null != warehouse) detail.setWarehouseName(warehouse.getName());
                Subordinate subordinate = subordinateMap.get(detail.getSubid());
                if (null != subordinate) detail.setSubName(subordinate.getName());
            }
            List<ClientInventoryDetail> otherDetails = one.getOtherDetails();
            for (ClientInventoryDetail otherDetail : otherDetails) {
                InventoryWarehouse warehouse = warehouseMap.get(otherDetail.getWid());
                if (null != warehouse) otherDetail.setWarehouseName(warehouse.getName());
                Subordinate subordinate = subordinateMap.get(otherDetail.getSubid());
                if (null != subordinate) otherDetail.setSubName(subordinate.getName());
            }
        }
        return res;
    }

    @Override
    public boolean updateSaleStatus(long id, int open) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        productSPU.setSaleStatus(open);
        return  productSPUDao.update(productSPU);
    }

    @Override
    public List<ProductSKU> getSkuBySubid(long subid, int type) throws Exception {
        String sql = " SELECT * FROM product_sku WHERE 1=1 AND spuid in (SELECT id FROM product_spu WHERE 1=1 AND subid = " + subid + " AND type = " + type + ")";
        return jdbcTemplate.query(sql,new HyperspaceBeanPropertyRowMapper<ProductSKU>(ProductSKU.class));
    }

    @Override
    public boolean checkStatus(List<Long> ids) throws Exception {

        List<ProductSPU> load = productSPUDao.load(ids);
        if(load.size()>0){
            for(ProductSPU productSPU:load){
                productSPU.setCheckStatus(ProductSPU.checkStatus_yes);
                productSPU.setCheckStatusDate(System.currentTimeMillis());
                productSPUDao.update(productSPU);
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Pager getSPUNoNirNumPager(Pager pager, long subid) throws Exception {

        String sql = "SELECT * FROM `product_spu` where `status` = " + ProductSPU.status_nomore + " and subid = " + subid;
        String sqlCount = "SELECT COUNT(id) FROM `product_spu` where `status` = " + ProductSPU.status_nomore + " and subid = " + subid;
        String limit = " limit %d , %d ";

        sql=sql+" and type="+ProductSPU.type_devices;
        sqlCount=sqlCount+" and type="+ProductSPU.type_devices;

        sql=sql+" and (nirNum=''";
        sqlCount=sqlCount+" and (nirNum=''";
        sql=sql+" or nirNumDate=0";
        sqlCount=sqlCount+" or nirNumDate=0";
        sql=sql+" or nirImg=[])";
        sqlCount=sqlCount+" or nirImg=[])";

        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ProductSPU> productSPUList = this.jdbcTemplate.query(sql, spuRowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);

        pager.setData(transformClients(productSPUList,0));
        pager.setTotalCount(count);
        return pager;
    }


    @Override
    public Pager getCommSpu(Pager pager, final long subid, final long cid) throws Exception {
        return  new PagerRequestService<Commission>(pager,0){

            @Override
            public List<Commission> step1GetPageResult(String cursor, int size) throws Exception {

                List<Commission> list = commissionDao.getAllList(subid,Double.parseDouble(cursor),size);
                return list;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<Commission> step3FilterResult(List<Commission> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<Commission> list, PagerSession pagerSession) throws Exception {
                Map<Long, List<Commission>> map = Maps.newHashMap();
                Set<Long> spuIds = new HashSet<>();
                for (Commission info : list) {
                    spuIds.add(info.getSpuId());
                }
                List<ProductSPU> spus = productSPUDao.load(Lists.newArrayList(spuIds));
                Map<Long, ProductSPU> spuMap = productSPUMapUtils.listToMap(spus, "id");
                for (Commission info : list) {
                    ProductSPU spu = spuMap.get(info.getSpuId());
                    if (spu != null) {
                        List<Commission> commissions = map.get(spu.getCid());
                        if (commissions == null) {
                            commissions = Lists.newArrayList();
                            map.put(spu.getCid(), commissions);
                        }
                        commissions.add(info);
                    }
                }
                return map.get(cid);
            }
        }.getPager();
    }

    @Override
    public ClientProductCategory searchSpu(String name) throws Exception {
        ClientProductCategory clientProductCategory=new ClientProductCategory();
        List<ProductCategory> allList = productCategoryDao.getAllList(ProductCategory.status_nomore);
        Map<String,List<ClientProductSPU>> map=new HashMap<>();
        for(ProductCategory li:allList){

            String sql="select * from product_spu where name like'%"+name+"%'";
            sql = sql+" and cid="+li.getId();
            List<ProductSPU> query = this.jdbcTemplate.query(sql, spuRowMapper);
            if(query.size()>0){
                map.put(li.getName(),transformClients(query,0));
            }

        }
        clientProductCategory.setMap(map);
        return clientProductCategory;
    }

    public List<ClientCommission> transformClients(List<Commission> list) {

        List<ClientCommission> clientCommissionList = new ArrayList<>();
        for (Commission commission : list) {

            ClientCommission clientCommission = new ClientCommission(commission);
            ProductSPU load = productSPUDao.load(commission.getSubId());
            clientCommission.setCode(load.getName());
            clientCommissionList.add(clientCommission);

        }

        return clientCommissionList;

    }

    public Map<Object, Object> getProperties(Object clazz, Object client, String property) throws Exception {
        Map<Object, Object> map_value = Maps.newHashMap();
        Map<Long, Object> map = (Map<Long, Object>) Reflections.invokeGetter(clazz, "properties");
        if (map != null) {
            Set<Long> keys = map.keySet();
            List<ProductPropertyName> names = productPropertyNameDao.load(Lists.newArrayList(keys));
            Map<Long, ProductPropertyName> nameMap = nameMapUtils.listToMap(names, "id");
            //如果是输入属性，则直接封装value返回
            //如果是非输入属性，则根据ID从value表中查找到content封装返回
            for (Map.Entry<Long, ProductPropertyName> entry : nameMap.entrySet()) {
                if (entry.getValue().getInput() == ProductPropertyName.input_no) {
                    Set<Long> values = Sets.newHashSet();
                    for (Object object : map.values()) {
                        if (StringUtils.isNotBlank(object.toString())) {
                            values.add(Long.parseLong(object.toString()));
                        }
                    }
                    List<ProductPropertyValue> valueList = productPropertyValueDao.load(Lists.newArrayList(values));
                    Map<Long, ProductPropertyValue> valueMap = valueMapUtils.listToMap(valueList, "id");

                    for (Map.Entry<Long, Object> skuEntry : map.entrySet()) {
                        if (nameMap.get(skuEntry.getKey()).getInput() == ProductPropertyName.input_no) {
                            map_value.put(nameMap.get(skuEntry.getKey()).getContent(), valueMap.get(Long.parseLong(skuEntry.getValue().toString())).getContent());
                        } else {
                            map_value.put(nameMap.get(skuEntry.getKey()).getContent(), skuEntry.getValue());
                        }
                    }
                    Reflections.invokeSetter(client, property, map_value);
                } else {
                    for (Map.Entry<Long, Object> skuEntry : map.entrySet()) {
                        map_value.put(nameMap.get(skuEntry.getKey()).getContent(), skuEntry.getValue());
                    }
                    Reflections.invokeSetter(client, property, map_value);
                }
            }
        }
        return map_value;
    }



}
