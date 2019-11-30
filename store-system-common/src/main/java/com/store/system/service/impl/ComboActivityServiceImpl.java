package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.bean.ComboItem;
import com.store.system.client.ClientComboActivity;
import com.store.system.client.ClientComboItem;
import com.store.system.client.ClientProductSKU;
import com.store.system.dao.ComboActivityDao;
import com.store.system.dao.CouponDao;
import com.store.system.dao.ProductSKUDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ComboActivity;
import com.store.system.model.Coupon;
import com.store.system.model.ProductSKU;
import com.store.system.service.ComboActivityService;
import com.store.system.service.ProductService;
import com.store.system.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ComboActivityServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:38
 * @Version 1.0
 **/
@Service
public class ComboActivityServiceImpl implements ComboActivityService{

    @Resource
    private ComboActivityDao comboActivityDao;
    @Resource
    private ProductSKUDao productSKUDao;
    @Resource
    private CouponDao couponDao;
    @Resource
    private ProductService productService;

    private TransformMapUtils couponMapUtils = new TransformMapUtils(Coupon.class);
    private TransformMapUtils clientSkuMapUtils = new TransformMapUtils(ClientProductSKU.class);

    private void check(ComboActivity comboActivity) throws StoreSystemException {
        if (comboActivity.getPsid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (StringUtils.isBlank(comboActivity.getTitle())) throw new StoreSystemException("标题不能为空");
        if (comboActivity.getSkuIds().size() == 0) throw new StoreSystemException("活动商品IDs不能为空");
        if (comboActivity.getType() != ComboActivity.TYPE_ORIGINAL
                && comboActivity.getType() != ComboActivity.TYPE_VIP )
            throw new StoreSystemException("套餐价格类型有误");
        for(ComboItem item:comboActivity.getItems()){
            if (item.getPrice()== 0) throw new StoreSystemException("套餐详情的加价不能为空");
            if (item.getItemSkuId()== 0) throw new StoreSystemException("套餐详情的SKU不能为空");
        }
        if (comboActivity.getSkuId() == 0) throw new StoreSystemException("赠品不能为空");
        if (comboActivity.getCouponId() == 0) throw new StoreSystemException("优惠券ID不能为空");
    }

    @Override
    public ComboActivity add(ComboActivity comboActivity) throws Exception {
        check(comboActivity);
        return comboActivityDao.insert(comboActivity);
    }

    @Override
    public boolean delete(long id) throws Exception {
        ComboActivity comboActivity = comboActivityDao.load(id);
        comboActivity.setStatus(Constant.STATUS_DELETE);
        return comboActivityDao.update(comboActivity);
    }

    @Override
    public boolean update(ComboActivity comboActivity) throws Exception {
        return comboActivityDao.update(comboActivity);
    }

    @Override
    public boolean updateOpen(long id, int open) throws Exception {
        ComboActivity comboActivity = comboActivityDao.load(id);
        comboActivity.setOpen(open);
        return comboActivityDao.update(comboActivity);
    }

    @Override
    public List<ClientComboActivity> getAllList(long psid) throws Exception {
        return transformClient(comboActivityDao.getAllList(psid, Constant.STATUS_NORMAL, Constant.OPEN_ON));
    }

    private List<ClientComboActivity> transformClient(List<ComboActivity> comboActivities) throws Exception {
        List<ClientComboActivity> res = Lists.newArrayList();
        Set<Long> skuIds = Sets.newHashSet();
        Set<Long> couponIds = Sets.newHashSet();
        for(ComboActivity one:comboActivities){
            skuIds.add(one.getSkuId());
            couponIds.add(one.getCouponId());
            for(ComboItem item:one.getItems()){
                skuIds.add(item.getItemSkuId());
            }
        }
        List<ProductSKU> skuList = productSKUDao.load(Lists.newArrayList(skuIds));
        List<ClientProductSKU> clientProductSKUS = productService.transformSKUClient(skuList,0);
        Map<Long, ClientProductSKU> clientSkuMap = clientSkuMapUtils.listToMap(clientProductSKUS, "id");

        List<Coupon> couponList = couponDao.load(Lists.newArrayList(couponIds));
        Map<Long, Coupon> couponMap = couponMapUtils.listToMap(couponList, "id");
        for(ComboActivity one:comboActivities){
            ClientComboActivity client = new ClientComboActivity(one);
            List<ClientComboItem> items = Lists.newArrayList();
            for(ComboItem item:one.getItems()){
                ClientComboItem clientItem = new ClientComboItem(item);
                clientItem.setItemSku(clientSkuMap.get(item.getItemSkuId()));
                items.add(clientItem);
            }
            List<ProductSKU> skus = productSKUDao.load(Lists.newArrayList(one.getSkuIds()));
            List<ClientProductSKU> clientSkus = productService.transformSKUClient(skus,0);
            client.setSkuList(clientSkus);
            client.setItemList(items);
            client.setSku(clientSkuMap.get(one.getSkuId()));
            client.setCoupon(couponMap.get(one.getCouponId()));
            res.add(client);
        }
        return res;
    }

}
