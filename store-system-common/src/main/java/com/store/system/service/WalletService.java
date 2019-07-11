package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.WalletStatistics;

import java.util.List;

public interface WalletService {

    /**
     * 记录钱包明细
     * @param uid
     * @param type
     * @param money
     * @param detail
     * @param desc
     * @param async true:异步 false:同步
     * @throws Exception
     */
    public void recordDetail(long uid, int type, double money, String detail, String desc, boolean async) throws Exception;

    /**
     * 获取钱包金额
     * @param uid
     * @return
     * @throws Exception
     */
    public double loadMoney(long uid) throws Exception;

    /**
     * 钱包金额变动 (+ -)金额
     * @param uid
     * @param money
     * @throws Exception
     */
    public void refresh(long uid, double money) throws Exception;

    /**
     * 获取钱包明细分页
     * @param uid
     * @param pager
     * @return
     * @throws Exception
     */
    public Pager getPager(long uid, Pager pager) throws Exception;

    /**
     * 获取支出/收入钱包明细分页
     * @param uid
     * @param type
     * @param pager
     * @return
     * @throws Exception
     */
    public Pager getPager(long uid, int type, Pager pager) throws Exception;


    public List<WalletStatistics> getStatistics(int startDate, int endDate) throws Exception;


}
