package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.client.ClientAfterSaleDetail;
import com.store.system.client.ClientAfterSaleLog;
import com.store.system.dao.AfterSaleDetailDao;
import com.store.system.dao.OptometryInfoDao;
import com.store.system.dao.OrderDao;
import com.store.system.model.*;
import com.store.system.service.AfterSaleDetailService;
import com.store.system.service.OptometryInfoService;
import com.store.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AfterSaleDetailServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:26
 * @Version 1.0
 **/
@Service
public class AfterSaleDetailServiceImpl implements AfterSaleDetailService{

    @Resource
    private AfterSaleDetailDao afterSaleDetailDao;
    @Resource
    private UserService userService;
    @Resource
    private OrderDao orderDao;
    @Resource
    private OptometryInfoDao optometryInfoDao;


    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);
    private TransformMapUtils orderMapUtils = new TransformMapUtils(Order.class);


    @Override
    public List<ClientAfterSaleDetail> getAllList(long asId) throws Exception {
        return transformClient(afterSaleDetailDao.getAllList(asId));
    }

    @Override
    public List<ClientAfterSaleDetail> getAllListByOid(long oid) throws Exception {
        return transformClient(afterSaleDetailDao.getAllList(oid));
    }

    private List<ClientAfterSaleDetail> transformClient(List<AfterSaleDetail> afterSaleDetails) throws Exception{
        List<ClientAfterSaleDetail> clientAfterSaleDetails = Lists.newArrayList();
        Set<Long> uids = Sets.newHashSet();
        Set<Long> oids = Sets.newHashSet();
        for(AfterSaleDetail afterSaleDetail: afterSaleDetails){
            if(afterSaleDetail.getOptId()>0) {
                uids.add(afterSaleDetail.getOptId());
                oids.add(afterSaleDetail.getOid());
            }
        }

        List<User> users = userService.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        List<Order> orders = orderDao.load(Lists.newArrayList(oids));
        Map<Long, Order> orderMap = orderMapUtils.listToMap(orders, "id");

        for(AfterSaleDetail detail: afterSaleDetails){
            ClientAfterSaleDetail client = new ClientAfterSaleDetail();
            Order order = orderMap.get(detail.getOid());
            client.setUserName(userMap.get(order.getUid()).getName());
            client.setUserAge(userMap.get(order.getUid()).getAge());
            client.setPrice(String.valueOf(order.getPrice()*100));
            client.setDiscount(order.getDiscount());
            OptometryInfo optometryInfo = optometryInfoDao.load(order.getOiId());
            if(optometryInfo != null && optometryInfo.getOptUid()>0) {
                if(userMap.get(optometryInfo.getOptUid())!=null){
                    client.setOiName(userMap.get(optometryInfo.getOptUid()).getName());
                }
            }
            List<OptometryInfo> optometryInfos = optometryInfoDao.getList(order.getUid(),10);
            client.setOptometryInfos(optometryInfos);
            client.setSurcharges(order.getSurcharges());
            client.setTotalPrice(String.valueOf(order.getTotalPrice()*100));
            client.setSku(order.getSkuids());
            client.setReason(detail.getReason());
            client.setOptName(userMap.get(detail.getOptId()).getName());
            clientAfterSaleDetails.add(client);
        }
        return clientAfterSaleDetails;
    }

}
