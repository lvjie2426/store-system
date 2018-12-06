package com.store.system.service.impl;

import com.store.system.dao.GoodsBrandDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.GoodsBrand;
import com.store.system.service.GoodsBrandService;
import com.s7.baseFramework.transform.TransformFieldSetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class GoodsBrandServiceImpl implements GoodsBrandService {

    private TransformFieldSetUtils goodsBrandFieldSetUtils = new TransformFieldSetUtils(GoodsBrand.class);

    @Resource
    private GoodsBrandDao goodsBrandDao;

    private void check(GoodsBrand goodsBrand) throws GlassesException {
        if(StringUtils.isBlank(goodsBrand.getName()))
            throw new GlassesException("名称不能为空");
        List<GoodsBrand> list = goodsBrandDao.getAllList(GoodsBrand.status_nomore);
        Set<String> names = goodsBrandFieldSetUtils.fieldList(list, "name");
        if(names.contains(goodsBrand.getName()))
            throw new GlassesException("名称存在");
    }


    @Override
    public GoodsBrand add(GoodsBrand goodsBrand) throws Exception {
        check(goodsBrand);
        GoodsBrand res = goodsBrandDao.insert(goodsBrand);
        return res;
    }

    @Override
    public boolean update(GoodsBrand goodsBrand) throws Exception {
        check(goodsBrand);
        boolean res = goodsBrandDao.update(goodsBrand);
        return  res;
    }

    @Override
    public boolean del(long id) throws Exception {
        GoodsBrand goodsBrand = goodsBrandDao.load(id);
        if(null != goodsBrand) {
            goodsBrand.setStatus(GoodsBrand.status_delete);
            return goodsBrandDao.update(goodsBrand);
        }
        return false;
    }

    @Override
    public List<GoodsBrand> getAllList() throws Exception {
        List<GoodsBrand> res = goodsBrandDao.getAllList(GoodsBrand.status_nomore);
        return res;
    }

}
