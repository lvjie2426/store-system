package com.store.system.service.impl;


import com.store.system.dao.DictionaryDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Dictionary;
import com.store.system.service.DictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private DictionaryDao dictionaryDao;

    private List<Dictionary> dictionaries = null;
    private long lastRefTimes=0;

    @Override
    public List<Dictionary> getAllList() {
        List<Dictionary> dictionaries = dictionaryDao.getAllDictionary();
        return dictionaries;
    }

    @Override
    public String getString(String key, String defaultValue) throws Exception {
        if(dictionaries==null||(System.currentTimeMillis()-lastRefTimes > 1000 * 60)){
            dictionaries=dictionaryDao.getAllDictionary();
            lastRefTimes=System.currentTimeMillis();
        }
        String res = defaultValue;
        for(Dictionary dictionary : dictionaries) {
            if(key.trim().equals(dictionary.getName().trim()) && dictionary.getStatus() == Dictionary.status_online) {
                res = dictionary.getValue();
                break;
            }
        }
        return res;
    }
    @Override
    public String[] getStrings(String key, String defaultValue) throws Exception {
        if(dictionaries==null||(System.currentTimeMillis()-lastRefTimes > 1000 * 60)){
            dictionaries=dictionaryDao.getAllDictionary();
            lastRefTimes=System.currentTimeMillis();
        }
        String res = defaultValue;
        for(Dictionary dictionary : dictionaries) {
            if(key.trim().equals(dictionary.getName().trim()) && dictionary.getStatus() == Dictionary.status_online) {
                res = dictionary.getValue();
                break;
            }
        }
        return res.split(",");
    }

    @Override
    public long getLong(String key, String defaultValue) throws Exception {
        String value = getString(key, defaultValue);
        return Long.parseLong(value);
    }

    @Override
    public double getDouble(String key, String defaultValue) throws Exception {
        String value = getString(key, defaultValue);
        return Double.parseDouble(value);
    }

    @Override
    public int getInt(String key, String defaultValue) throws Exception {
        String value = getString(key, defaultValue);
        return Integer.parseInt(value);
    }

    @Override
    public Dictionary add(Dictionary dictionary) throws Exception {
        List<Dictionary> dictionaries = dictionaryDao.getAllDictionary();
        for(Dictionary one : dictionaries) {
            if(one.getName().trim().equals(dictionary.getName().trim())) {
                throw new StoreSystemException("此属性已添加");
            }
        }
        return dictionaryDao.insert(dictionary);
    }

    @Override
    public boolean update(Dictionary dictionary) throws Exception {
        Dictionary dbDictionary = dictionaryDao.load(dictionary.getId());
        if(null == dbDictionary) throw new StoreSystemException("字典为空");
        dbDictionary.setValue(dictionary.getValue());
        dbDictionary.setDesc(dictionary.getDesc());
        dbDictionary.setSort(dictionary.getSort());
        return dictionaryDao.update(dbDictionary);
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        Dictionary dbDictionary = dictionaryDao.load(id);
        if(null == dbDictionary) throw new StoreSystemException("字典为空");
        dbDictionary.setStatus(status);
        return dictionaryDao.update(dbDictionary);
    }

    @Override
    public boolean del(long id) throws Exception {
        return dictionaryDao.delete(id);
    }

}
