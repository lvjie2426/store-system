package com.store.system.service.impl;

import com.store.system.bean.*;
import com.store.system.dao.GoodsDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.Goods;
import com.store.system.service.GoodsService;
import com.google.common.collect.Lists;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.ext.RowMapperHelp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsDao goodsDao;

    private RowMapperHelp<Goods> rowMapper = new RowMapperHelp<>(Goods.class);

    @Resource
    protected JdbcTemplate jdbcTemplate;

    private void checkBracket(Bracket bracket) throws GlassesException {
        if(bracket.getGbid() == 0) throw new GlassesException("品牌不能为空");
        if(bracket.getGsid() == 0) throw new GlassesException("系列不能为空");
        if(bracket.getGpid() == 0) throw new GlassesException("供应商不能为空");
    }

    @Override
    public Bracket addBracket(Bracket bracket) throws Exception {
        checkBracket(bracket);
        Goods goods = Bracket.toGoods(bracket);
        goods = goodsDao.insert(goods);
        Bracket res = Bracket.fromGoods(goods);
        return res;
    }

    @Override
    public boolean updateBracket(Bracket bracket) throws Exception {
        checkBracket(bracket);
        Goods goods = Bracket.toGoods(bracket);
        boolean res = goodsDao.update(goods);
        return res;
    }

    @Override
    public Pager getBackBracketPager(Pager pager, long gpid) throws Exception {
        pager = this.getBackGoodsPager(pager, Goods.type_bracket, gpid);
        List<Bracket> brackets = Lists.newArrayList();
        for(Object obj : pager.getData()) {
            Goods goods = (Goods) obj;
            Bracket bracket = Bracket.fromGoods(goods);
            brackets.add(bracket);
        }
        pager.setData(brackets);
        return pager;
    }

    private void checkCareProduct(CareProduct careProduct) throws GlassesException {
        if(careProduct.getGbid() == 0) throw new GlassesException("品牌不能为空");
        if(careProduct.getGsid() == 0) throw new GlassesException("系列不能为空");
        if(careProduct.getGpid() == 0) throw new GlassesException("供应商不能为空");
    }

    @Override
    public CareProduct addCareProduct(CareProduct careProduct) throws Exception {
        checkCareProduct(careProduct);
        Goods goods = CareProduct.toGoods(careProduct);
        goods = goodsDao.insert(goods);
        CareProduct res = CareProduct.fromGoods(goods);
        return res;
    }

    @Override
    public boolean updateCareProduct(CareProduct careProduct) throws Exception {
        checkCareProduct(careProduct);
        Goods goods = CareProduct.toGoods(careProduct);
        boolean res = goodsDao.update(goods);
        return res;
    }

    @Override
    public Pager getBackCareProductPager(Pager pager, long gpid) throws Exception {
        pager = this.getBackGoodsPager(pager, Goods.type_care_product, gpid);
        List<CareProduct> careProducts = Lists.newArrayList();
        for(Object obj : pager.getData()) {
            Goods goods = (Goods) obj;
            CareProduct careProduct = CareProduct.fromGoods(goods);
            careProducts.add(careProduct);
        }
        pager.setData(careProducts);
        return pager;
    }

    private void checkContactLenses(ContactLenses contactLenses) throws GlassesException {
        if(contactLenses.getGbid() == 0) throw new GlassesException("品牌不能为空");
        if(contactLenses.getGsid() == 0) throw new GlassesException("系列不能为空");
        if(contactLenses.getGpid() == 0) throw new GlassesException("供应商不能为空");
    }

    @Override
    public ContactLenses addContactLenses(ContactLenses contactLenses) throws Exception {
        checkContactLenses(contactLenses);
        Goods goods = ContactLenses.toGoods(contactLenses);
        goods = goodsDao.insert(goods);
        ContactLenses res = ContactLenses.fromGoods(goods);
        return res;
    }

    @Override
    public boolean updateContactLenses(ContactLenses contactLenses) throws Exception {
        checkContactLenses(contactLenses);
        Goods goods = ContactLenses.toGoods(contactLenses);
        boolean res = goodsDao.update(goods);
        return res;
    }

    @Override
    public Pager getBackContactLensesPager(Pager pager, long gpid) throws Exception {
        pager = this.getBackGoodsPager(pager, Goods.type_contact_lenses, gpid);
        List<ContactLenses> contactLensesList = Lists.newArrayList();
        for(Object obj : pager.getData()) {
            Goods goods = (Goods) obj;
            ContactLenses contactLenses = ContactLenses.fromGoods(goods);
            contactLensesList.add(contactLenses);
        }
        pager.setData(contactLensesList);
        return pager;
    }

    private void checkLens(Lens lens) throws GlassesException {
        if(lens.getGbid() == 0) throw new GlassesException("品牌不能为空");
        if(lens.getGsid() == 0) throw new GlassesException("系列不能为空");
        if(lens.getGpid() == 0) throw new GlassesException("供应商不能为空");
    }

    @Override
    public Lens addLens(Lens lens) throws Exception {
        checkLens(lens);
        Goods goods = Lens.toGoods(lens);
        goods = goodsDao.insert(goods);
        Lens res = Lens.fromGoods(goods);
        return res;
    }

    @Override
    public boolean updateLens(Lens lens) throws Exception {
        checkLens(lens);
        Goods goods = Lens.toGoods(lens);
        boolean res = goodsDao.update(goods);
        return res;
    }

    @Override
    public Pager getBackLensPager(Pager pager, long gpid) throws Exception {
        pager = this.getBackGoodsPager(pager, Goods.type_lens, gpid);
        List<Lens> lensList = Lists.newArrayList();
        for(Object obj : pager.getData()) {
            Goods goods = (Goods) obj;
            Lens lens = Lens.fromGoods(goods);
            lensList.add(lens);
        }
        pager.setData(lensList);
        return pager;
    }

    private void checkSpecialGoods(SpecialGoods specialGoods) throws GlassesException {
        if(specialGoods.getGbid() == 0) throw new GlassesException("品牌不能为空");
        if(specialGoods.getGsid() == 0) throw new GlassesException("系列不能为空");
        if(specialGoods.getGpid() == 0) throw new GlassesException("供应商不能为空");
    }

    @Override
    public SpecialGoods addSpecialGoods(SpecialGoods specialGoods) throws Exception {
        checkSpecialGoods(specialGoods);
        Goods goods = SpecialGoods.toGoods(specialGoods);
        goods = goodsDao.insert(goods);
        SpecialGoods res = SpecialGoods.fromGoods(goods);
        return res;
    }

    @Override
    public boolean updateSpecialGoods(SpecialGoods specialGoods) throws Exception {
        checkSpecialGoods(specialGoods);
        Goods goods = SpecialGoods.toGoods(specialGoods);
        boolean res = goodsDao.update(goods);
        return res;
    }

    @Override
    public Pager getBackSpecialGoodsPager(Pager pager, long gpid) throws Exception {
        pager = this.getBackGoodsPager(pager, Goods.type_special_goods, gpid);
        List<SpecialGoods> specialGoodsList = Lists.newArrayList();
        for(Object obj : pager.getData()) {
            Goods goods = (Goods) obj;
            SpecialGoods specialGoods = SpecialGoods.fromGoods(goods);
            specialGoodsList.add(specialGoods);
        }
        pager.setData(specialGoodsList);
        return pager;
    }

    private void checkSunGlasses(SunGlasses sunGlasses) throws GlassesException {
        if(sunGlasses.getGbid() == 0) throw new GlassesException("品牌不能为空");
        if(sunGlasses.getGsid() == 0) throw new GlassesException("系列不能为空");
        if(sunGlasses.getGpid() == 0) throw new GlassesException("供应商不能为空");
    }

    @Override
    public SunGlasses addSunGlasses(SunGlasses sunGlasses) throws Exception {
        checkSunGlasses(sunGlasses);
        Goods goods = SunGlasses.toGoods(sunGlasses);
        goods = goodsDao.insert(goods);
        SunGlasses res = SunGlasses.fromGoods(goods);
        return res;
    }

    @Override
    public boolean updateSunGlasses(SunGlasses sunGlasses) throws Exception {
        checkSunGlasses(sunGlasses);
        Goods goods = SunGlasses.toGoods(sunGlasses);
        boolean res = goodsDao.update(goods);
        return res;
    }

    @Override
    public Pager getBackSunGlassesPager(Pager pager, long gpid) throws Exception {
        pager = this.getBackGoodsPager(pager, Goods.type_sun_glasses, gpid);
        List<SunGlasses> sunGlassesList = Lists.newArrayList();
        for(Object obj : pager.getData()) {
            Goods goods = (Goods) obj;
            SunGlasses sunGlasses = SunGlasses.fromGoods(goods);
            sunGlassesList.add(sunGlasses);
        }
        pager.setData(sunGlassesList);
        return pager;
    }




    @Override
    public boolean del(long id) throws Exception {
        Goods goods = goodsDao.load(id);
        if(null != goods) {
            goods.setStatus(Goods.status_delete);
            return goodsDao.update(goods);
        }
        return false;
    }

    private Pager getBackGoodsPager(Pager pager, int type, long gpid) throws Exception {
        String sql = "select * from goods where `type` = " + type ;
        String countSql = "select count(id) from goods where `type` = " + type;
        String limit = "  limit %d , %d ";
        if(gpid > 0){
            sql += " and gpid = " + gpid;
            countSql += " and gpid = " + gpid;
        }
        sql += " order by ctime desc ";
        sql = sql + String.format(limit,pager.getSize() * (pager.getPage()-1), pager.getSize());
        List<Goods> goodsList = jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForInt(countSql);
        pager.setData(goodsList);
        pager.setTotalCount(count);
        return pager;
    }
}
