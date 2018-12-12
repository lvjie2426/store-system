package com.store.system.service.impl;

import com.s7.baseFramework.model.pagination.Pager;
import com.s7.ext.RowMapperHelp;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.service.InventoryDetailService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InventoryDetailServiceImpl implements InventoryDetailService {

    private RowMapperHelp<ClientInventoryDetail> rowMapper = new RowMapperHelp<>(ClientInventoryDetail.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Pager getPager(Pager pager, long wid, long cid) throws Exception {
        String sql = "select d.id, d.wid, spu.type as p_type, spu.pid as p_pid, spu.cid as p_cid, spu.bid as p_bid, spu.sid as p_sid, " +
                     "spu.name as p_name, spu.id as p_spuid, sku.id as p_skuid, sku.code as p_code, sku.retailPrice as p_retailPrice, " +
                     "sku.costPrice as p_costPrice, sku.integralPrice as p_integralPrice, d.num, d.ctime from " +
                     "inventory_detail as d, product_sku as sku, product_spu as spu where d.p_skuid = sku.id and sku.spuid = spu.id";
        String sqlCount = "select count(d.id) from inventory_detail as d, product_sku as sku," +
                          "product_spu as spu where d.p_skuid = sku.id and sku.spuid = spu.id ";
        if(wid > 0) {
            sql = sql + " and d.wid = " + wid;
            sqlCount = sqlCount + " and d.wid = " + wid;
        }
        if(cid > 0) {
            sql = sql + " and spu.cid = " + cid;
            sqlCount = sqlCount + " and spu.cid = " + cid;
        }
        String limit = " limit %d , %d ";
        sql = sql + " order by d.ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ClientInventoryDetail> details = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForInt(sqlCount);
        pager.setData(details);
        pager.setTotalCount(count);
        return pager;
    }

}
