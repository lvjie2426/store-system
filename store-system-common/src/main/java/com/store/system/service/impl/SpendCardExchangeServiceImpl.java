package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.bean.SalePresentItem;
import com.store.system.client.*;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.ProductService;
import com.store.system.service.SpendCardExchangeService;
import com.store.system.service.SubordinateService;
import com.store.system.util.Constant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SpendCardExchangeServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 10:48
 * @Version 1.0
 **/
@Service
public class SpendCardExchangeServiceImpl implements SpendCardExchangeService {

    @Resource
    private SpendCardExchangeDao spendCardExchangeDao;
    @Resource
    private ProductSKUDao productSKUDao;
    @Resource
    private ProductSPUDao productSPUDao;
    @Resource
    private ProductService productService;
    @Resource
    private SubordinateDao subordinateDao;

    private TransformMapUtils clientSkuMapUtils = new TransformMapUtils(ClientProductSKU.class);
    private TransformMapUtils clientSpuMapUtils = new TransformMapUtils(ClientProductSPU.class);

    private void check(SpendCardExchange exchange) throws StoreSystemException {
        if (exchange.getPsid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (exchange.getSpuId() == 0) throw new StoreSystemException("SPU不能为空");
        if (exchange.getSkuId() == 0) throw new StoreSystemException("可兑换商品SKU不能为空");
        if (exchange.getCardNum() == 0) throw new StoreSystemException("兑换所需花卡不能为空");
        if (exchange.getNum() == 0) throw new StoreSystemException("可兑换数量不能为空");
        if (exchange.getSubIds().size() == 0) throw new StoreSystemException("可兑换门店不能为空");
        int count = spendCardExchangeDao.getCount(exchange.getPsid(),exchange.getSpuId(),Constant.STATUS_NORMAL);
        if(count >= 8){
            throw new StoreSystemException("可兑换商品最多设置8个");
        }
    }

    @Override
    public SpendCardExchange add(SpendCardExchange spendCardExchange) throws Exception {
        check(spendCardExchange);
        return spendCardExchangeDao.insert(spendCardExchange);
    }

    @Override
    public boolean delete(long id) throws Exception {
        SpendCardExchange dbInfo = spendCardExchangeDao.load(id);
        dbInfo.setStatus(Constant.STATUS_DELETE);
        return spendCardExchangeDao.update(dbInfo);
    }

    @Override
    public boolean update(SpendCardExchange spendCardExchange) throws Exception {
        return spendCardExchangeDao.update(spendCardExchange);
    }

    @Override
    public List<ClientSpendCardExchange> getAllList(long psid, long spuId) throws Exception {
        return transformClient(spendCardExchangeDao.getAllList(psid,spuId,Constant.STATUS_NORMAL));
    }

    private List<ClientSpendCardExchange> transformClient(List<SpendCardExchange> spendCardExchanges) throws Exception {
        List<ClientSpendCardExchange> res = Lists.newArrayList();
        Set<Long> skuIds = Sets.newHashSet();
        Set<Long> spuIds = Sets.newHashSet();
        for(SpendCardExchange one:spendCardExchanges){
            skuIds.add(one.getSkuId());
            spuIds.add(one.getSpuId());
        }
        List<ProductSKU> skuList = productSKUDao.load(Lists.newArrayList(skuIds));
        List<ClientProductSKU> clientProductSKUS = productService.transformSKUClient(skuList);
        Map<Long, ClientProductSKU> clientSkuMap = clientSkuMapUtils.listToMap(clientProductSKUS, "id");

        List<ProductSPU> spuList = productSPUDao.load(Lists.newArrayList(spuIds));
        List<ClientProductSPU> clientProductSPUS = productService.transformClients(spuList,0);
        Map<Long, ClientProductSPU> clientSpuMap = clientSpuMapUtils.listToMap(clientProductSPUS, "id");


        for(SpendCardExchange one:spendCardExchanges){
            ClientSpendCardExchange client = new ClientSpendCardExchange(one);
            client.setSku(clientSkuMap.get(one.getSkuId()));
            client.setSpu(clientSpuMap.get(one.getSpuId()));
            List<String> subNames = Lists.newArrayList();
            List<Subordinate> subordinates = subordinateDao.load(one.getSubIds());
            for(Subordinate sub:subordinates){
                subNames.add(sub.getName());
            }
            client.setSubNames(subNames);
            res.add(client);
        }
        return res;
    }
}
