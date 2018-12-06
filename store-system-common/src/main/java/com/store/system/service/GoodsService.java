package com.store.system.service;

import com.s7.baseFramework.model.pagination.Pager;
import com.store.system.bean.*;

public interface GoodsService {

    public Bracket addBracket(Bracket bracket) throws Exception;

    public boolean updateBracket(Bracket bracket) throws Exception;

    public Pager getBackBracketPager(Pager pager, long gpid) throws Exception;


    public CareProduct addCareProduct(CareProduct careProduct) throws Exception;

    public boolean updateCareProduct(CareProduct careProduct) throws Exception;

    public Pager getBackCareProductPager(Pager pager, long gpid) throws Exception;


    public ContactLenses addContactLenses(ContactLenses contactLenses) throws Exception;

    public boolean updateContactLenses(ContactLenses contactLenses) throws Exception;

    public Pager getBackContactLensesPager(Pager pager, long gpid) throws Exception;


    public Lens addLens(Lens lens) throws Exception;

    public boolean updateLens(Lens lens) throws Exception;

    public Pager getBackLensPager(Pager pager, long gpid) throws Exception;


    public SpecialGoods addSpecialGoods(SpecialGoods specialGoods) throws Exception;

    public boolean updateSpecialGoods(SpecialGoods specialGoods) throws Exception;

    public Pager getBackSpecialGoodsPager(Pager pager, long gpid) throws Exception;


    public SunGlasses addSunGlasses(SunGlasses sunGlasses) throws Exception;

    public boolean updateSunGlasses(SunGlasses sunGlasses) throws Exception;

    public Pager getBackSunGlassesPager(Pager pager, long gpid) throws Exception;


    public boolean del(long id) throws Exception;

}
