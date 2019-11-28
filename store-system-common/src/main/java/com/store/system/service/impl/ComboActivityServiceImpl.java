package com.store.system.service.impl;

import com.store.system.bean.ComboItem;
import com.store.system.dao.ComboActivityDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ComboActivity;
import com.store.system.service.ComboActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    private void check(ComboActivity comboActivity) throws StoreSystemException {
        if (comboActivity.getSid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (StringUtils.isBlank(comboActivity.getTitle())) throw new StoreSystemException("标题不能为空");
        if (comboActivity.getSkuIds().size() == 0) throw new StoreSystemException("活动商品IDs不能为空");
        if (comboActivity.getType() != ComboActivity.TYPE_ORIGINAL
                && comboActivity.getType() != ComboActivity.TYPE_VIP )
            throw new StoreSystemException("套餐价格类型有误");
        for(ComboItem item:comboActivity.getItems()){
            if (item.getPrice()== 0) throw new StoreSystemException("套餐详情的加价不能为空");
            if (item.getSkuId()== 0) throw new StoreSystemException("套餐详情的SKU不能为空");
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
        comboActivity.setStatus(ComboActivity.STATUS_DELETE);
        return comboActivityDao.update(comboActivity);
    }

    @Override
    public boolean update(ComboActivity comboActivity) throws Exception {
        return comboActivityDao.update(comboActivity);
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        ComboActivity comboActivity = comboActivityDao.load(id);
        comboActivity.setStatus(status);
        return comboActivityDao.update(comboActivity);
    }

    @Override
    public List<ComboActivity> getAllList(long psid) throws Exception {
        return comboActivityDao.getAllList(psid);
    }

}
