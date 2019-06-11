package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Dictionary;

import java.util.List;

public interface DictionaryDao extends HDao<Dictionary> {

    public List<Dictionary> getAllDictionary();

}
