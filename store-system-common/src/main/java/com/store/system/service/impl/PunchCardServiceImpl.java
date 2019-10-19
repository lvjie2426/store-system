package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.store.system.client.ClientPunchCardLog;
import com.store.system.dao.PunchCardDao;
import com.store.system.model.User;
import com.store.system.model.attendance.PunchCardLog;
import com.store.system.service.PunchCardService;
import com.store.system.service.UserService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class PunchCardServiceImpl implements PunchCardService {

    @Resource
    private PunchCardDao punchCardDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private UserService userService;

    @Override
    public Pager getBackendLogs(Pager pager, long uid, long day) throws Exception {
        final String selectData = "SELECT * FROM `punchcard_log` WHERE 1=1";
        final String selectCount = "SELECT count(*) FROM `punchcard_log` WHERE 1=1";
        final String limit = "  limit %d , %d ";
        String sql = selectData;
        String countSql = selectCount;
        if (uid > 0) {
            sql = sql + " and `uid` = " + uid;
            countSql = countSql + " and `uid` = " + uid;
        }
        if (day > 0) {
            sql = sql + " and `day` = " + day;
            countSql = countSql + " and `day` = " + day;
        }

        sql = sql + " order  by `ctime` desc";
        System.err.println(sql);
        sql = sql + String.format(limit, pager.getSize() * (pager.getPage() - 1), pager.getSize());
        List<PunchCardLog> punchCardLogs = null;
        int count = 0;
        punchCardLogs = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PunchCardLog.class));
        count = this.jdbcTemplate.queryForObject(countSql, Integer.class);
        pager.setData(transformClientPunchCard(punchCardLogs));
        pager.setTotalCount(count);
        return pager;

    }

    @Override
    public Pager getWebLogsPager(final Pager pager, final long uid) throws Exception {
        return new PagerRequestService<PunchCardLog>(pager, 0) {
            @Override
            public List<PunchCardLog> step1GetPageResult(String cursor, int size) throws Exception {
                return punchCardDao.getAllList(uid, Double.parseDouble(cursor), size);

            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return punchCardDao.getCount(uid);

            }

            @Override
            public List<PunchCardLog> step3FilterResult(List<PunchCardLog> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }

            @Override
            public List<?> step4TransformData(List<PunchCardLog> punchCardLogList, PagerSession session) throws Exception {
                return punchCardLogList;
            }

        }.getPager();

    }

    /**
     * 获取一条数据
     */
    public ClientPunchCardLog loadPunchCardLog(long id) throws Exception {
        return transformClientPunchCard(punchCardDao.load(id));
    }


    private List<ClientPunchCardLog> transformClientPunchCard(List<PunchCardLog> punchCardLogs) throws Exception {
        List<ClientPunchCardLog> result = new ArrayList<>();
        if (punchCardLogs != null) {
            for (PunchCardLog punchCardLog : punchCardLogs) {
                ClientPunchCardLog clientPunchCardLog = transformClientPunchCard(punchCardLog);
                result.add(clientPunchCardLog);
            }
        }
        return result;
    }

    private ClientPunchCardLog transformClientPunchCard(PunchCardLog punchCardLog) throws Exception {
        ClientPunchCardLog clientPunchCardLog = new ClientPunchCardLog(punchCardLog);
        User user = userService.load(punchCardLog.getUid());
        if (user != null) {
            clientPunchCardLog.setUserName(user.getName());
        }
        return clientPunchCardLog;
    }
}
