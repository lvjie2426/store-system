package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.client.ClientIntegralActivity;
import com.store.system.client.ClientProductSKU;
import com.store.system.dao.IntegralActivityDao;
import com.store.system.dao.ProductSKUDao;
import com.store.system.dao.UserGradeDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.IntegralActivity;
import com.store.system.model.ProductSKU;
import com.store.system.model.UserGrade;
import com.store.system.service.IntegralActivityService;
import com.store.system.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName IntegralActivityServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:10
 * @Version 1.0
 **/
@Service
public class IntegralActivityServiceImpl implements IntegralActivityService{

    @Resource
    private IntegralActivityDao integralActivityDao;
    @Resource
    private ProductSKUDao productSKUDao;
    @Resource
    private UserGradeDao userGradeDao;
    @Resource
    private ProductService productService;

    private TransformMapUtils clientSkuMapUtils = new TransformMapUtils(ClientProductSKU.class);

    private void check(IntegralActivity integralActivity) throws StoreSystemException {
        if (integralActivity.getPsid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (StringUtils.isBlank(integralActivity.getTitle())) throw new StoreSystemException("标题不能为空");
        if (integralActivity.getSkuIds().size() == 0) throw new StoreSystemException("活动商品IDs不能为空");
        if (integralActivity.getType() != IntegralActivity.TYPE_MONEY
                && integralActivity.getType() != IntegralActivity.TYPE_RATE )
            throw new StoreSystemException("赠送积分类型有误");
        if (integralActivity.getDescSubtract() == 0) throw new StoreSystemException("金额或折扣不能为空");
        if (integralActivity.getStartTime() == 0) throw new StoreSystemException("活动开始时间不能为空");
        if (integralActivity.getEndTime() == 0) throw new StoreSystemException("活动结束时间不能为空");
    }

    @Override
    public IntegralActivity add(IntegralActivity integralActivity) throws Exception {
        check(integralActivity);
        return integralActivityDao.insert(integralActivity);
    }

    @Override
    public boolean delete(long id) throws Exception {
        return integralActivityDao.delete(id);
    }

    @Override
    public boolean update(IntegralActivity integralActivity) throws Exception {
        return integralActivityDao.update(integralActivity);
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        IntegralActivity integralActivity = integralActivityDao.load(id);
        integralActivity.setStatus(status);
        return integralActivityDao.update(integralActivity);
    }

    @Override
    public List<IntegralActivity> getAllList(long psid) throws Exception {
        return integralActivityDao.getAllList(psid);
    }

    @Override
    public List<ClientIntegralActivity> getIngList(long psid) throws Exception {
        List<IntegralActivity> res = Lists.newArrayList();
        List<IntegralActivity> list = integralActivityDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (IntegralActivity one : list) {
            if (currentTime >= one.getStartTime() && currentTime <= one.getEndTime()) {
                res.add(one);
            }
        }
        return transformClient(res);
    }

    @Override
    public List<ClientIntegralActivity> getHistoryList(long psid) throws Exception {
        List<IntegralActivity> res = Lists.newArrayList();
        List<IntegralActivity> list = integralActivityDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (IntegralActivity one : list) {
            if (currentTime > one.getEndTime()) {
                res.add(one);
            }
        }
        return transformClient(res);
    }

    private List<ClientIntegralActivity> transformClient(List<IntegralActivity> integralActivities) throws Exception {
        List<ClientIntegralActivity> res = Lists.newArrayList();
        Set<Long> skuIds = Sets.newHashSet();
        for(IntegralActivity one:integralActivities){
            skuIds.addAll(one.getSkuIds());
        }
        List<ProductSKU> skuList = productSKUDao.load(Lists.newArrayList(skuIds));
        List<ClientProductSKU> clientProductSKUS = productService.transformSKUClient(skuList);
        Map<Long, ClientProductSKU> clientSkuMap = clientSkuMapUtils.listToMap(clientProductSKUS, "id");

        for(IntegralActivity one:integralActivities){
            ClientIntegralActivity client = new ClientIntegralActivity(one);
            List<ClientProductSKU> skus = Lists.newArrayList();
            for(Long skuId:one.getSkuIds()){
                skus.add(clientSkuMap.get(skuId));
            }
            client.setSkuList(skus);
            List<String> ugNames = Lists.newArrayList();
            if(one.getUgIds().size()>0){
                List<UserGrade> userGrades = userGradeDao.load(Lists.newArrayList(one.getUgIds()));
                for(UserGrade userGrade:userGrades){
                    ugNames.add(userGrade.getTitle());
                }
            }
            client.setUgNames(ugNames);
            res.add(client);
        }
        return res;
    }

}
