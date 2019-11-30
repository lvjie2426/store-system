package com.store.system.service.impl;

import com.store.system.dao.SpendCardSetDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.SpendCardSet;
import com.store.system.service.SpendCardSetService;
import com.store.system.util.Constant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SpendCardSetServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 10:46
 * @Version 1.0
 **/
@Service
public class SpendCardSetServiceImpl implements SpendCardSetService {

    @Resource
    private SpendCardSetDao spendCardSetDao;

    private void check(SpendCardSet spendCardSet) throws StoreSystemException {
        if (spendCardSet.getPsid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (spendCardSet.getCid() == 0) throw new StoreSystemException("类目ID不能为空");
        if(spendCardSet.getType() != SpendCardSet.TYPE_CATE &&
                spendCardSet.getType() != SpendCardSet.TYPE_SPU){
            throw new StoreSystemException("花卡类型参数不正确");
        }
        if (spendCardSet.getPrice() == 0) throw new StoreSystemException("消费满不能为空");
        if (spendCardSet.getNum() == 0) throw new StoreSystemException("获得花卡数量不能为空");
        if(spendCardSet.getType()==SpendCardSet.TYPE_CATE){
            int count = spendCardSetDao.getSpuCount(spendCardSet.getPsid(),spendCardSet.getCid());
            if(count>0){
                throw new StoreSystemException("该品类下的某系列商品已设置过花卡！");
            }
        }else if(spendCardSet.getType()==SpendCardSet.TYPE_SPU){
            int count = spendCardSetDao.getCateCount(spendCardSet.getPsid(),spendCardSet.getCid(),0,Constant.STATUS_NORMAL);
            if(count>0){
                throw new StoreSystemException("该品类已设置过花卡,不能再对单品设置花卡！");
            }
        }
    }

    @Override
    public SpendCardSet add(SpendCardSet spendCardSet) throws Exception {
        check(spendCardSet);
        return spendCardSetDao.insert(spendCardSet);
    }

    @Override
    public boolean delete(long id) throws Exception {
        SpendCardSet spendCardSet = spendCardSetDao.load(id);
        spendCardSet.setStatus(Constant.STATUS_DELETE);
        return spendCardSetDao.update(spendCardSet);
    }

    @Override
    public boolean update(SpendCardSet spendCardSet) throws Exception {
        return spendCardSetDao.update(spendCardSet);
    }

    @Override
    public List<SpendCardSet> getAllList(long psid, long cid, long spuId) throws Exception {
        return spendCardSetDao.getAllList(psid, cid, spuId, Constant.STATUS_NORMAL);
    }
}
