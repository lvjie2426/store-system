package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.model.InventoryDetail;

import java.util.List;
import java.util.Map;

public interface InventoryDetailService {

    public Pager getBackendPager(Pager pager, long wid, long cid) throws Exception;

    public Pager getPager(Pager pager, long wid, long cid) throws Exception;

    /**
     * 库存详情
     * method_name: getAllList
     * params: [wid, p_spuid]
     * return: java.util.List<com.store.system.client.ClientInventoryDetail>
     * creat_user: lihao
     * creat_date: 2019/2/22
     * creat_time: 14:38
     **/
    public List<ClientInventoryDetail> getAllList(long wid, long p_spuid) throws Exception;

    public List<InventoryDetail> getAllOriginList(long wid, long p_spuid) throws Exception;

    public List<ClientInventoryDetail> getAllList(long subid) throws Exception;

    public List<ClientInventoryDetail> getWaringList(long wid, long cid) throws Exception;

    public List<ClientInventoryDetail> getExpireList(long wid, long cid) throws Exception;

    public Map<Long,List<ClientInventoryDetail>> selectDetails(long wid, String search) throws Exception;

    public boolean updateStatus(long id) throws Exception;

}
