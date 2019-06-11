package com.store.system.service;



import com.store.system.model.Dictionary;

import java.util.List;

public interface DictionaryService {

    public List<Dictionary> getAllList() throws Exception;

    public String getString(String key, String defaultValue) throws Exception;

    public String[] getStrings(String key, String defaultValue) throws Exception;

    public long getLong(String key, String defaultValue) throws Exception;

    public double getDouble(String key, String defaultValue) throws Exception;

    public int getInt(String key, String defaultValue) throws Exception;

    public Dictionary add(Dictionary dictionary) throws Exception;

    public boolean update(Dictionary dictionary) throws Exception;

    public boolean updateStatus(long id, int status) throws Exception;

    public boolean del(long id) throws Exception;

}
