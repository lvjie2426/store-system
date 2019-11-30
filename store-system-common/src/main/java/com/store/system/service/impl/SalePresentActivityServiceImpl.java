package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.bean.SalePresentItem;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientSalePresentActivity;
import com.store.system.client.ClientSalePresentItem;
import com.store.system.dao.CouponDao;
import com.store.system.dao.ProductSKUDao;
import com.store.system.dao.SalePresentActivityDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Coupon;
import com.store.system.model.ProductSKU;
import com.store.system.model.SalePresentActivity;
import com.store.system.service.ProductService;
import com.store.system.service.SalePresentActivityService;
import com.store.system.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SalePresentActivityServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 11:10
 * @Version 1.0
 **/
@Service
public class SalePresentActivityServiceImpl implements SalePresentActivityService{

    @Resource
    private SalePresentActivityDao salePresentActivityDao;
    @Resource
    private ProductSKUDao productSKUDao;
    @Resource
    private CouponDao couponDao;
    @Resource
    private ProductService productService;

    private TransformMapUtils couponMapUtils = new TransformMapUtils(Coupon.class);
    private TransformMapUtils clientSkuMapUtils = new TransformMapUtils(ClientProductSKU.class);

    private void check(SalePresentActivity salePresentActivity) throws StoreSystemException {
        if (salePresentActivity.getPsid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (StringUtils.isBlank(salePresentActivity.getTitle())) throw new StoreSystemException("标题不能为空");
        if (salePresentActivity.getSkuId() == 0) throw new StoreSystemException("购买商品不能为空");
        if (salePresentActivity.getNum() <= 0) throw new StoreSystemException("购买数量有误");
        if (salePresentActivity.getItems().size() == 0) throw new StoreSystemException("赠送的商品或优惠券不能为空");
        for(SalePresentItem item:salePresentActivity.getItems()){
            if(item.getType() != SalePresentItem.TYPE_COUPON &&
                    item.getType() != SalePresentItem.TYPE_GOODS){
               throw new StoreSystemException("赠送的商品或优惠券的类型不正确");
            }
            if(item.getTypeId()==0) throw new StoreSystemException("赠送的商品或优惠券的ID不能为空");
            if(item.getItemNum()==0) throw new StoreSystemException("赠送的商品或优惠券的数量不能为空");
        }
        if (salePresentActivity.getStartTime() == 0) throw new StoreSystemException("活动开始时间不能为空");
        if (salePresentActivity.getEndTime() == 0) throw new StoreSystemException("活动结束时间不能为空");
    }

    @Override
    public SalePresentActivity add(SalePresentActivity salePresentActivity) throws Exception {
        check(salePresentActivity);
        return salePresentActivityDao.insert(salePresentActivity);
    }

    @Override
    public boolean delete(long id) throws Exception {
        SalePresentActivity dbInfo = salePresentActivityDao.load(id);
        dbInfo.setStatus(Constant.STATUS_DELETE);
        return salePresentActivityDao.update(dbInfo);
    }

    @Override
    public boolean update(SalePresentActivity salePresentActivity) throws Exception {
        return salePresentActivityDao.update(salePresentActivity);
    }

    @Override
    public boolean updateOpen(long id, int open) throws Exception {
        SalePresentActivity storeGiftActivity = salePresentActivityDao.load(id);
        storeGiftActivity.setOpen(open);
        return salePresentActivityDao.update(storeGiftActivity);
    }

    @Override
    public List<SalePresentActivity> getAllList(long psid) throws Exception {
        return salePresentActivityDao.getAllList(psid, Constant.STATUS_NORMAL, Constant.OPEN_ON);
    }

    @Override
    public List<ClientSalePresentActivity> getIngList(long psid) throws Exception {
        List<SalePresentActivity> res = Lists.newArrayList();
        List<SalePresentActivity> list = salePresentActivityDao.getAllList(psid, Constant.STATUS_NORMAL, Constant.OPEN_ON);
        long currentTime = System.currentTimeMillis();
        for (SalePresentActivity one : list) {
            if (currentTime >= one.getStartTime() && currentTime <= one.getEndTime()) {
                res.add(one);
            }
        }
        return transformClient(res);
    }

    @Override
    public List<ClientSalePresentActivity> getHistoryList(long psid) throws Exception {
        List<SalePresentActivity> res = Lists.newArrayList();
        List<SalePresentActivity> list = salePresentActivityDao.getAllList(psid, Constant.STATUS_NORMAL, Constant.OPEN_ON);
        long currentTime = System.currentTimeMillis();
        for (SalePresentActivity one : list) {
            if (currentTime > one.getEndTime()) {
                res.add(one);
            }
        }
        return transformClient(res);
    }

    private List<ClientSalePresentActivity> transformClient(List<SalePresentActivity> salePresentActivities) throws Exception {
       List<ClientSalePresentActivity> res = Lists.newArrayList();
        Set<Long> skuIds = Sets.newHashSet();
        Set<Long> couponIds = Sets.newHashSet();
        for(SalePresentActivity one:salePresentActivities){
            skuIds.add(one.getSkuId());
            for(SalePresentItem item:one.getItems()){
                if(item.getType()==SalePresentItem.TYPE_GOODS){
                    skuIds.add(item.getTypeId());
                }else if(item.getType()==SalePresentItem.TYPE_COUPON){
                    couponIds.add(item.getTypeId());
                }
            }

        }
        List<ProductSKU> skuList = productSKUDao.load(Lists.newArrayList(skuIds));
        List<ClientProductSKU> clientProductSKUS = productService.transformSKUClient(skuList,0);
        Map<Long, ClientProductSKU> clientSkuMap = clientSkuMapUtils.listToMap(clientProductSKUS, "id");

        List<Coupon> couponList = couponDao.load(Lists.newArrayList(couponIds));
        Map<Long, Coupon> couponMap = couponMapUtils.listToMap(couponList, "id");
        for(SalePresentActivity one:salePresentActivities){
            ClientSalePresentActivity client = new ClientSalePresentActivity(one);
            client.setSku(clientSkuMap.get(one.getSkuId()));

            List<ClientSalePresentItem> items = Lists.newArrayList();
            for(SalePresentItem item:one.getItems()){
                ClientSalePresentItem clientItem = new ClientSalePresentItem(item);
                if(item.getType()==SalePresentItem.TYPE_GOODS) {
                    clientItem.setItemSku(clientSkuMap.get(item.getTypeId()));
                }else if(item.getType()==SalePresentItem.TYPE_COUPON){
                    clientItem.setCoupon(couponMap.get(item.getTypeId()));
                }
                items.add(clientItem);
            }
            client.setClientItems(items);
            res.add(client);
        }
        return res;
    }

}
