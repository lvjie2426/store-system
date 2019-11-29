package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.bean.SalePresentItem;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientCoupon;
import com.store.system.client.ClientSalePresentItem;
import com.store.system.dao.CouponDao;
import com.store.system.dao.ProductSKUDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Coupon;
import com.store.system.model.ProductSKU;
import com.store.system.model.SalePresentActivity;
import com.store.system.service.CouponService;
import com.store.system.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName CouponServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 15:32
 * @Version 1.0
 **/
@Service
public class CouponServiceImpl implements CouponService{

    @Resource
    private CouponDao couponDao;
    @Resource
    private ProductSKUDao productSKUDao;
    @Resource
    private ProductService productService;

    private TransformMapUtils clientSkuMapUtils = new TransformMapUtils(ClientProductSKU.class);

    private void check(Coupon coupon) throws StoreSystemException {
        if (coupon.getPsid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (StringUtils.isBlank(coupon.getTitle())) throw new StoreSystemException("标题不能为空");
        if (coupon.getType() != Coupon.TYPE_FULL
                && coupon.getType() != Coupon.TYPE_DIRECTIONAL
                && coupon.getType() != Coupon.TYPE_DISCOUNT ) {
            throw new StoreSystemException("优惠券类型有误");
        }
        if (coupon.getStartTime() == 0) throw new StoreSystemException("活动开始时间不能为空");
        if (coupon.getEndTime() == 0) throw new StoreSystemException("活动结束时间不能为空");
    }

    @Override
    public Coupon add(Coupon coupon) throws Exception {
        check(coupon);
        return couponDao.insert(coupon);
    }

    @Override
    public boolean delete(long id) throws Exception {
        return couponDao.delete(id);
    }

    @Override
    public boolean update(Coupon coupon) throws Exception {
        return couponDao.update(coupon);
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        Coupon integralActivity = couponDao.load(id);
        integralActivity.setStatus(status);
        return couponDao.update(integralActivity);
    }

    @Override
    public List<Coupon> getAllList(long psid) throws Exception {
        return couponDao.getAllList(psid);
    }

    @Override
    public List<ClientCoupon> getIngList(long psid) throws Exception {
        List<Coupon> res = Lists.newArrayList();
        List<Coupon> list = couponDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (Coupon one : list) {
            if (currentTime >= one.getStartTime() && currentTime <= one.getEndTime()) {
                res.add(one);
            }
        }
        return transformClient(res);
    }

    @Override
    public List<ClientCoupon> getHistoryList(long psid) throws Exception {
        List<Coupon> res = Lists.newArrayList();
        List<Coupon> list = couponDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (Coupon one : list) {
            if (currentTime > one.getEndTime()) {
                res.add(one);
            }
        }
        return transformClient(res);
    }

    private List<ClientCoupon> transformClient(List<Coupon> coupons) throws Exception {
        List<ClientCoupon> res = Lists.newArrayList();
        Set<Long> skuIds = Sets.newHashSet();
        for(Coupon one:coupons){
            skuIds.addAll(one.getSkuIds());
        }
        List<ProductSKU> skuList = productSKUDao.load(Lists.newArrayList(skuIds));
        List<ClientProductSKU> clientProductSKUS = productService.transformSKUClient(skuList);
        Map<Long, ClientProductSKU> clientSkuMap = clientSkuMapUtils.listToMap(clientProductSKUS, "id");

        for(Coupon one:coupons){
            ClientCoupon client = new ClientCoupon(one);
            List<ClientProductSKU> skus = Lists.newArrayList();
            for(Long skuId:one.getSkuIds()){
                skus.add(clientSkuMap.get(skuId));
            }
            client.setSkuList(skus);
            res.add(client);
        }
        return res;
    }
}
