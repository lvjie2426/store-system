package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientPayPassport;
import com.store.system.client.ClientPayment;
import com.store.system.client.ClientSubordinate;
import com.store.system.dao.PayPassportDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.PayPassport;
import com.store.system.model.Payment;
import com.store.system.model.Subordinate;
import com.store.system.service.PayService;
import com.store.system.util.Constant;
import com.store.system.util.PayUtils;
import com.store.system.util.RSA;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.*;

@Service
public class PayServiceImpl implements PayService {

    @Resource
    private PayPassportDao payPassportDao;
    @Resource
    private SubordinateDao subordinateDao;
    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<PayPassport> rowMapper = new RowMapperHelp<>(PayPassport.class);
    private TransformMapUtils subordinateMapUtils = new TransformMapUtils(Subordinate.class);

    @Override
    public PayPassport loadPayPassport(long id) throws Exception {
        return payPassportDao.load(id);
    }

    @Override
    public boolean updatePayPassport(PayPassport payPassport) throws Exception {
        return payPassportDao.update(payPassport);
    }

    @Override
    public PayPassport insertPayPassport(PayPassport payPassport) throws Exception {

        return payPassportDao.insert(payPassport);
    }

    @Override
    public boolean deletePayPassport(long id) throws Exception {
        PayPassport payPassport = payPassportDao.load(id);
        payPassport.setStatus(PayPassport.status_off);
        return payPassportDao.update(payPassport);
    }

    @Override
    public Pager getBackPayPassportPager(Pager pager) throws Exception {
        String sql = "SELECT  *  FROM `pay_passport` order by ctime desc ";
        String sqlCount = "SELECT  COUNT(sid)  FROM `pay_passport`";
        String limit = " limit %d, %d ";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<PayPassport> passports = jdbcTemplate.query(sql, rowMapper);
        pager.setData(passports);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public List<ClientPayPassport> getAllList() throws Exception {
        return transformClients(payPassportDao.getAllList(PayPassport.status_on));
    }

    @Override
    public List<ClientPayPassport> getAllList(long subId) throws Exception {
        return transformClients(payPassportDao.getAllList(subId, PayPassport.status_on));
    }

    @Override
    public String authParam(long passportId) throws Exception {
        PayPassport payPassport = payPassportDao.load(passportId);
        if(null == payPassport) throw new StoreSystemException("passport is null!");
        String aliAppid = payPassport.getAliAppid();
        String aliPid = payPassport.getAliPid();
        String aliPrivateKey = payPassport.getAliPrivateKey();
        if(StringUtils.isBlank(aliAppid)) throw new StoreSystemException("aliAppid is null!");
        if(StringUtils.isBlank(aliPid)) throw new StoreSystemException("aliPid is null!");
        if(StringUtils.isBlank(aliPrivateKey)) throw new StoreSystemException("aliPrivateKey is null!");
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("apiname", "com.alipay.account.auth");
        sParaTemp.put("method", "alipay.open.auth.sdk.code.get");
        sParaTemp.put("app_id", aliAppid);
        sParaTemp.put("app_name", "mc");
        sParaTemp.put("biz_type", "openservice");
        sParaTemp.put("pid", aliPid);
        sParaTemp.put("product_id", "APP_FAST_LOGIN");
        sParaTemp.put("scope", "kuaijie");
        sParaTemp.put("target_id", String.valueOf(System.currentTimeMillis()));
        sParaTemp.put("auth_type", "AUTHACCOUNT");
        sParaTemp.put("sign_type", "RSA2");
        String prestr = PayUtils.createLinkString(sParaTemp);
        String mysign = RSA.sign256(prestr, aliPrivateKey, Constant.defaultCharset);
        sParaTemp.put("sign", mysign);

        List<String> keys = new ArrayList<String>(sParaTemp.keySet());
        Collections.sort(keys);
        String params = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sParaTemp.get(key);
            value = URLEncoder.encode(value, Constant.defaultCharset);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                params += key + "=" + value;
            } else {
                params += key + "=" + value + "&";
            }
        }
        return params;
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        PayPassport payPassport = payPassportDao.load(id);
        payPassport.setStatus(status);
        return payPassportDao.update(payPassport);
    }

    private List<ClientPayPassport> transformClients(List<PayPassport> payPassports) throws Exception {
        List<ClientPayPassport> res = Lists.newArrayList();
        if (payPassports.size() == 0) return res;
        Set<Long> subIds = Sets.newHashSet();
        for (PayPassport one : payPassports) {
            subIds.add(one.getSubId());
        }
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subIds));
        Map<Long, Subordinate> subordinateMap = subordinateMapUtils.listToMap(subordinates, "id");

        for (PayPassport one : payPassports) {
            ClientPayPassport client = new ClientPayPassport(one);
            Subordinate subordinate = subordinateMap.get(client.getSubId());
            if (null != subordinate) {
                client.setSubName(subordinate.getName());
            }
            res.add(client);
        }
        return res;
    }
}
