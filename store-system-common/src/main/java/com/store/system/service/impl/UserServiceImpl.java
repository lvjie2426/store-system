package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.baseFramework.property.PropertyLoader;
import com.quakoo.baseFramework.redis.JedisX;
import com.quakoo.baseFramework.secure.MD5Utils;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.bean.StatisticsOrderUser;
import com.store.system.client.ClientMissForUser;
import com.store.system.client.ClientUser;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.BusinessOrderService;
import com.store.system.service.OrderService;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserService;
import com.store.system.util.DateUtils;
import com.store.system.util.NumberUtils;
import com.store.system.util.SmsUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserGradeDao userGradeDao;
    @Resource
    private UserDao userDao;
    @Resource
    private SubordinateService subordinateService;

    @Resource
    private LoginUserPoolDao loginUserPoolDao;

    @Resource
    private RolePermissionPoolDao rolePermissionPoolDao;

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private UserPermissionPoolDao userPermissionPoolDao;

    @Resource
    private SubordinateUserPoolDao subordinateUserPoolDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    protected JdbcTemplate jdbcTemplate;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderDao orderDao;

    @Resource
    private BusinessOrderDao businessOrderDao;

    @Resource
    private BusinessOrderService businessOrderService;

    @Resource
    private UserMissionPoolDao userMissionPoolDao;

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    private RowMapperHelp<User> rowMapper = new RowMapperHelp<>(User.class);

    private RowMapperHelp<UserGrade> ugMapper = new RowMapperHelp<>(UserGrade.class);


    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    PropertyLoader loader = PropertyLoader.getInstance("dao.properties");
    String messageAccessKeyId = loader.getProperty("accessKeyId");
    String messageAccessKeySecret = loader.getProperty("accessKeySecret");
    String company = loader.getProperty("company");
    String templateId = loader.getProperty("templateId");

    String appId = loader.getProperty("appId");
    String appsecret = loader.getProperty("appsecret");

    String QrCodeAppId = loader.getProperty("QrCodeAppId", "");
    String QrCodeAppsecret = loader.getProperty("QrCodeAppsecret", "");

    @Autowired(required = true)
    @Qualifier("cachePool")
    protected JedisX cache;

    private String auth_code_key(String phone) {
        return "auth_common_" + phone;
    }


    @Override
    public List<Permission> getUserPermissions(long uid) throws Exception {
        List<Long> pids = getUserPermissionIds(uid);
        List<Permission> permissions = permissionDao.load(pids);

        User user = userDao.load(uid);
        if (user.getSid() > 0) {
            for (Iterator<Permission> it = permissions.iterator(); it.hasNext(); ) {
                Permission permission = it.next();
                if (permission.getPid() > 0 && permission.getSubordinate() == Permission.subordinate_off) it.remove();
            }
        }
        Collections.sort(permissions);
        return permissions;
    }

    private List<Long> getUserPermissionIds(long uid) {
        Set<Long> res = Sets.newHashSet();
        User user = userDao.load(uid);
        for (long rid : user.getRids()) {
            List<RolePermissionPool> rolePermissionPoolPools = rolePermissionPoolDao.getAllList(rid);
            for (RolePermissionPool one : rolePermissionPoolPools) {
                res.add(one.getPid());
            }
        }
        List<UserPermissionPool> userPermissionPools = userPermissionPoolDao.getAllList(uid);
        for (UserPermissionPool one : userPermissionPools) {
            if (one.getType() == UserPermissionPool.type_on) {
                res.add(one.getPid());
            } else {
                res.remove(one.getPid());
            }
        }
        return Lists.newArrayList(res);
    }


    @Override
    public Pager searchUser(Pager pager, long sid, long subid, int userType, String name, String phone, String userName, long rid, int status, long startTime, long endTime) throws Exception {
        final String selectCount = "select count(*) from `user` where 1=1  ";
        final String selectData = "select * from `user` where 1=1  ";
        final String limit = "  limit %d , %d ";
        String sql = selectData;
        String countSql = selectCount;
        if (sid > -1) {
            sql = sql + " and `sid` = " + sid;
            countSql = countSql + " and `sid` = " + sid;
        }
        if (status > -1) {
            sql = sql + " and `status` = " + status;
            countSql = countSql + " and `status` = " + status;
        }
        if (userType > -1) {
            sql = sql + " and `userType` = " + userType;
            countSql = countSql + " and `userType` = " + userType;
        }
        if (StringUtils.isNotBlank(name)) {
            sql = sql + " and `name` like ?";
            countSql = countSql + " and `name` like ?";
        }
        if (StringUtils.isNotBlank(phone)) {
            sql = sql + " and `phone` like ?";
            countSql = countSql + " and `phone` like ?";
        }
        if (StringUtils.isNotBlank(userName)) {
            sql = sql + " and `userName` like ?";
            countSql = countSql + " and `userName` like ?";
        }
        if (rid > 0) {
            sql = sql + " and (`ridsJson` like '[" + rid + "]'  or `ridsJson` like '%," + rid + ",%'  or `ridsJson` like '[" + rid + ",%'   or `ridsJson` like '%," + rid + "]')";
            countSql = countSql + " and (`ridsJson` like '[" + rid + "]'  or `ridsJson` like '%," + rid + ",%'  or `ridsJson` like '[" + rid + ",%'   or `ridsJson` like '%," + rid + "]')";
        }

        if (startTime > 0) {
            sql = sql + " and `ctime` > " + startTime;
            countSql = countSql + " and `ctime` > " + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `ctime` < " + endTime;
            countSql = countSql + " and `ctime` < " + endTime;
        }
        if (subid > -1) {
            sql = sql + " and `psid` = " + subid;
            countSql = countSql + " and `psid` = " + subid;
        }
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, pager.getSize() * (pager.getPage() - 1), pager.getSize());
        List<User> users = null;
        int count = 0;
        List<Object> objects = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            objects.add("%" + name + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            objects.add("%" + phone + "%");
        }
        if (StringUtils.isNotBlank(userName)) {
            objects.add("%" + userName + "%");
        }
        if (objects.size() > 0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            users = jdbcTemplate.query(sql, new RowMapperHelp(User.class), args);
            count = this.jdbcTemplate.queryForObject(countSql, args, Integer.class);
        } else {
            users = jdbcTemplate.query(sql, new RowMapperHelp(User.class));
            count = this.jdbcTemplate.queryForObject(countSql, Integer.class);
        }
        pager.setData(transformClient(users));
        pager.setTotalCount(count);
        return pager;

    }

    @Override
    public Pager searchBackendUser(Pager pager, long sid, long psid, int userType, String name, String phone, String userName, long rid, int status, long startTime, long endTime) throws Exception {
        final String selectCount = "select count(*) from `user` where 1=1  ";
        final String selectData = "select * from `user` where 1=1  ";
        final String limit = "  limit %d , %d ";
        String sql = selectData;
        String countSql = selectCount;
        if (sid > -1 && psid > -1) {
            sql = sql + " and `psid` = " + psid + " and `sid` = " + sid;
            countSql = countSql + " and `psid` = " + psid + " and `sid` = " + sid;
        }
        if (psid > -1 && sid <= -1) {
            sql = sql + " and `psid` = " + psid + " and `sid` = 0 ";
            countSql = countSql + " and `psid` = " + psid + " and `sid` = 0 ";
        }
        if (sid <= -1 && psid <= -1) {
            sql = sql + " and `psid` != 0 ";
            countSql = countSql + " and `psid` != 0 ";
        }
        if (status > -1) {
            sql = sql + " and `status` = " + status;
            countSql = countSql + " and `status` = " + status;
        }
        if (userType > -1) {
            sql = sql + " and `userType` = " + userType;
            countSql = countSql + " and `userType` = " + userType;
        }
        if (StringUtils.isNotBlank(name)) {
            sql = sql + " and `name` like ?";
            countSql = countSql + " and `name` like ?";
        }
        if (StringUtils.isNotBlank(phone)) {
            sql = sql + " and `phone` like ?";
            countSql = countSql + " and `phone` like ?";
        }
        if (StringUtils.isNotBlank(userName)) {
            sql = sql + " and `userName` like ?";
            countSql = countSql + " and `userName` like ?";
        }
        if (rid > 0) {
            sql = sql + " and (`ridsJson` like '[" + rid + "]'  or `ridsJson` like '%," + rid + ",%'  or `ridsJson` like '[" + rid + ",%'   or `ridsJson` like '%," + rid + "]')";
            countSql = countSql + " and (`ridsJson` like '[" + rid + "]'  or `ridsJson` like '%," + rid + ",%'  or `ridsJson` like '[" + rid + ",%'   or `ridsJson` like '%," + rid + "]')";
        }

        if (startTime > 0) {
            sql = sql + " and `ctime` > " + startTime;
            countSql = countSql + " and `ctime` > " + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `ctime` < " + endTime;
            countSql = countSql + " and `ctime` < " + endTime;
        }


        sql = sql + " order by ctime desc";
        System.err.println(sql);
        System.err.println(countSql);
        sql = sql + String.format(limit, pager.getSize() * (pager.getPage() - 1), pager.getSize());
        List<User> users = null;
        int count = 0;
        List<Object> objects = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            objects.add("%" + name + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            objects.add("%" + phone + "%");
        }
        if (StringUtils.isNotBlank(userName)) {
            objects.add("%" + userName + "%");
        }
        if (objects.size() > 0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            users = jdbcTemplate.query(sql, new RowMapperHelp(User.class), args);
            count = this.jdbcTemplate.queryForObject(countSql, args, Integer.class);
        } else {
            users = jdbcTemplate.query(sql, new RowMapperHelp(User.class));
            count = this.jdbcTemplate.queryForObject(countSql, Integer.class);
        }
        pager.setData(transformClient(users));
        pager.setTotalCount(count);
        return pager;

    }


    @Override
    public ClientUserOnLogin register(User user) throws Exception {
        boolean thirdSign = false;
        if (StringUtils.isNotBlank(user.getWeiboId()) || StringUtils.isNotBlank(user.getWeixinId()) || StringUtils.isNotBlank(user.getQqId())
                || StringUtils.isNotBlank(user.getAlipayId())) {
            thirdSign = true;
        }
        if (thirdSign) {
            if (StringUtils.isBlank(user.getPhone())) throw new StoreSystemException("手机号不能为空");
            LoginUserPool loginUserPool = new LoginUserPool();
            loginUserPool.setUserType(user.getUserType());
            if (StringUtils.isNotBlank(user.getWeiboId())) {
                loginUserPool.setAccount(user.getWeiboId());
                loginUserPool.setLoginType(LoginUserPool.loginType_weibo);
            }
            if (StringUtils.isNotBlank(user.getWeixinId())) {
                loginUserPool.setAccount(user.getWeixinId());
                loginUserPool.setLoginType(LoginUserPool.loginType_weixin);
            }
            if (StringUtils.isNotBlank(user.getQqId())) {
                loginUserPool.setAccount(user.getQqId());
                loginUserPool.setLoginType(LoginUserPool.loginType_qq);
            }
            if (StringUtils.isNotBlank(user.getAlipayId())) {
                loginUserPool.setAccount(user.getAlipayId());
                loginUserPool.setLoginType(LoginUserPool.loginType_alipay);
            }
            LoginUserPool dbLoginUserPool = loginUserPoolDao.load(loginUserPool);
            if (dbLoginUserPool != null) throw new StoreSystemException("该账号已经存在");

            LoginUserPool phonePool = new LoginUserPool();
            phonePool.setUserType(user.getUserType());
            phonePool.setLoginType(LoginUserPool.loginType_phone);
            phonePool.setAccount(user.getPhone());
            phonePool = loginUserPoolDao.load(phonePool);
            if (null != phonePool) {
                long uid = phonePool.getUid();
                User dbUser = userDao.load(uid);
                if (null == dbUser) throw new StoreSystemException("用户错误");
                if (StringUtils.isNotBlank(user.getWeiboId())) {
                    if (StringUtils.isNotBlank(dbUser.getWeiboId())) throw new StoreSystemException("手机号已绑定微博");
                    dbUser.setWeiboId(user.getWeiboId());
                }
                if (StringUtils.isNotBlank(user.getWeixinId())) {
                    if (StringUtils.isNotBlank(dbUser.getWeixinId())) throw new StoreSystemException("手机号已绑定微信");
                    dbUser.setWeixinId(user.getWeixinId());
                }
                if (StringUtils.isNotBlank(user.getQqId())) {
                    if (StringUtils.isNotBlank(dbUser.getQqId())) throw new StoreSystemException("手机号已绑定QQ");
                    dbUser.setQqId(user.getQqId());
                }
                if (StringUtils.isNotBlank(user.getAlipayId())) {
                    if (StringUtils.isNotBlank(dbUser.getAlipayId())) throw new StoreSystemException("手机号已绑定支付宝");
                    dbUser.setAlipayId(user.getAlipayId());
                }
                userDao.update(dbUser);
                loginUserPool.setUid(dbUser.getId());
                loginUserPoolDao.insert(loginUserPool);
                user = dbUser;
            } else {
                Random rand = new Random();
                user.setRand(rand.nextInt(100000000));
                if (StringUtils.isNotBlank(user.getWeiboId())) {
                    user.setWeiboId(user.getWeiboId());
                }
                if (StringUtils.isNotBlank(user.getWeixinId())) {
                    user.setWeixinId(user.getWeixinId());
                }
                if (StringUtils.isNotBlank(user.getQqId())) {
                    user.setQqId(user.getQqId());
                }
                if (StringUtils.isNotBlank(user.getAlipayId())) {
                    user.setAlipayId(user.getAlipayId());
                }
                user.setPassword(MD5Utils.md5ReStr(user.getPassword().getBytes()));
                if (user.getSid() > 0) {
                    Subordinate subordinate = subordinateDao.load(user.getSid());
                    long psid = subordinate.getPid();
                    if (psid == 0) psid = subordinate.getId();
                    user.setPsid(psid);
                }
                user = userDao.insert(user);
                phonePool = new LoginUserPool();
                phonePool.setUserType(user.getUserType());
                phonePool.setLoginType(LoginUserPool.loginType_phone);
                phonePool.setAccount(user.getPhone());
                phonePool.setUid(user.getId());
                loginUserPoolDao.insert(phonePool);
                loginUserPool.setUid(user.getId());
                loginUserPoolDao.insert(loginUserPool);
                if (user.getSid() > 0) {
                    SubordinateUserPool subordinateUserPool = new SubordinateUserPool();
                    subordinateUserPool.setUid(user.getId());
                    subordinateUserPool.setSid(user.getSid());
                    subordinateUserPool.setStatus(user.getStatus());
                    subordinateUserPoolDao.insert(subordinateUserPool);
                }
            }
        } else {
            if (StringUtils.isBlank(user.getPassword())) throw new StoreSystemException("密码不能为空");
            LoginUserPool loginUserPool = new LoginUserPool();
            loginUserPool.setUserType(user.getUserType());
            if (StringUtils.isNotBlank(user.getPhone())) {
                loginUserPool.setAccount(user.getPhone());
                loginUserPool.setLoginType(LoginUserPool.loginType_phone);
            }
            if (StringUtils.isNotBlank(user.getUserName())) {
                loginUserPool.setAccount(user.getUserName());
                loginUserPool.setLoginType(LoginUserPool.loginType_userName);
            }
            LoginUserPool dbLoginUserPool = loginUserPoolDao.load(loginUserPool);
            if (dbLoginUserPool != null) throw new StoreSystemException("该账号已经存在");
            Random rand = new Random();
            String name = user.getName();
            if (StringUtils.isBlank(name)) name = "" + rand.nextInt(100000000);
            user.setName(name);
            user.setRand(rand.nextInt(100000000));
            user.setPassword(MD5Utils.md5ReStr(user.getPassword().getBytes()));
            if (user.getSid() > 0) {
                Subordinate subordinate = subordinateDao.load(user.getSid());
                long psid = subordinate.getPid();
                if (psid == 0) psid = subordinate.getId();
                user.setPsid(psid);
            }
            user = userDao.insert(user);
            loginUserPool.setUid(user.getId());
            loginUserPoolDao.insert(loginUserPool);
            if (user.getSid() > 0) {
                SubordinateUserPool subordinateUserPool = new SubordinateUserPool();
                subordinateUserPool.setUid(user.getId());
                subordinateUserPool.setSid(user.getSid());
                subordinateUserPool.setStatus(user.getStatus());
                subordinateUserPoolDao.insert(subordinateUserPool);
            }
        }
        return new ClientUserOnLogin(user);
    }


    @Override
    public ClientUserOnLogin login(User user, String code) throws Exception {
        LoginUserPool loginUserPool = new LoginUserPool();
        loginUserPool.setUserType(user.getUserType());
        if (StringUtils.isNotBlank(user.getPhone())) {
            loginUserPool.setAccount(user.getPhone());
            loginUserPool.setLoginType(LoginUserPool.loginType_phone);
        } else if (StringUtils.isNotBlank(user.getUserName())) {
            loginUserPool.setAccount(user.getUserName());
            loginUserPool.setLoginType(LoginUserPool.loginType_userName);
        } else if (StringUtils.isNotBlank(user.getWeiboId())) {
            loginUserPool.setAccount(user.getWeiboId());
            loginUserPool.setLoginType(LoginUserPool.loginType_weibo);
        } else if (StringUtils.isNotBlank(user.getWeixinId())) {
            loginUserPool.setAccount(user.getWeixinId());
            loginUserPool.setLoginType(LoginUserPool.loginType_weixin);
        } else if (StringUtils.isNotBlank(user.getQqId())) {
            loginUserPool.setAccount(user.getQqId());
            loginUserPool.setLoginType(LoginUserPool.loginType_qq);
        } else if (StringUtils.isNotBlank(user.getAlipayId())) {
            loginUserPool.setAccount(user.getAlipayId());
            loginUserPool.setLoginType(LoginUserPool.loginType_alipay);
        } else {
            throw new StoreSystemException("用户不存在");
        }
        loginUserPool = loginUserPoolDao.load(loginUserPool);
        if (loginUserPool == null) {
            throw new StoreSystemException("用户不存在");
        }

        User dbUser = userDao.load(loginUserPool.getUid());
        if (null == dbUser || dbUser.getStatus() == User.status_delete) {
            throw new StoreSystemException("用户不存在");
        }
//        if (StringUtils.isBlank(user.getQqId()) && StringUtils.isBlank(user.getWeixinId()) && StringUtils.isBlank(user.getWeiboId())) {
//            if (StringUtils.isBlank(code)) {
//                if (StringUtils.isNotBlank(dbUser.getPassword()) &&
//                        !MD5Utils.md5ReStr(user.getPassword().getBytes()).equalsIgnoreCase(dbUser.getPassword())) {
//                    throw new StoreSystemException("密码不正确");
//                }
//
//            }
//        }
        ClientUserOnLogin clientUserOnLogin = new ClientUserOnLogin(dbUser);
        Subordinate subordinate = subordinateService.load(clientUserOnLogin.getSid());
        Subordinate pSubordinate = subordinateService.load(clientUserOnLogin.getPsid());
        if (subordinate != null) {
            if (subordinate.getName() != null) {
                clientUserOnLogin.setSName(subordinate.getName());//公司名称
            }
        }
        if (pSubordinate != null) {
            if (pSubordinate.getName() != null) {
                clientUserOnLogin.setSubName(pSubordinate.getName());//门店名称
            }
        }
        if (clientUserOnLogin.getBankCard().size() == 0) {
            clientUserOnLogin.setBankCard(null);
        }
        return clientUserOnLogin;
    }

    @Override
    public User load(long id) throws Exception {
        return userDao.load(id);
    }

    @Override
    public long loadByAccount(int loginType, String account, int userType) {
        LoginUserPool loginUserPool = new LoginUserPool();
        loginUserPool.setAccount(account);
        loginUserPool.setLoginType(loginType);
        loginUserPool.setUserType(userType);
        loginUserPool = loginUserPoolDao.load(loginUserPool);
        if (loginUserPool == null) {
            return 0;
        }
        return loginUserPool.getUid();
    }

    @Override
    public ClientUser loadWithClient(long id) throws Exception {
        return transformClient(userDao.load(id));
    }


    @Override
    public List<User> load(List<Long> ids) throws Exception {
        return userDao.load(ids);
    }

    @Override
    public boolean update(User user, List<Long> updateRids) throws Exception {
        User currentUser = userDao.load(user.getId());
        if (null == currentUser) throw new StoreSystemException("用户不存在");
        LoginUserPool oldLoginUserPool = null;//旧的
        LoginUserPool loginUserPool = null;//新的
        if (StringUtils.isNotBlank(user.getUserName())
                && !user.getUserName().equals(currentUser.getUserName())) {
            loginUserPool = new LoginUserPool();
            loginUserPool.setAccount(user.getUserName());
            loginUserPool.setLoginType(LoginUserPool.loginType_userName);
            loginUserPool.setUserType(user.getUserType());
            loginUserPool.setUid(user.getId());
            LoginUserPool dbLoginUserPool = loginUserPoolDao.load(loginUserPool);
            if (dbLoginUserPool != null) {
                throw new StoreSystemException("账号已存在");
            }
            oldLoginUserPool = new LoginUserPool();
            oldLoginUserPool.setAccount(currentUser.getUserName());
            oldLoginUserPool.setLoginType(LoginUserPool.loginType_userName);
            oldLoginUserPool.setUserType(currentUser.getUserType());
            oldLoginUserPool.setUid(currentUser.getId());

            currentUser.setUserName(user.getUserName());
        }

        if (StringUtils.isNotBlank(user.getMail())
                && !user.getMail().equals(currentUser.getMail())) {
            currentUser.setMail(user.getMail());
        }
        if (StringUtils.isNotBlank(user.getName())
                && !user.getName().equals(currentUser.getName())) {
            currentUser.setName(user.getName());
        }
        if (StringUtils.isNotBlank(user.getPhone())
                && !user.getPhone().equals(currentUser.getPhone())) {
            loginUserPool = new LoginUserPool();
            loginUserPool.setAccount(user.getPhone());
            loginUserPool.setLoginType(LoginUserPool.loginType_phone);
            loginUserPool.setUserType(user.getUserType());
            loginUserPool.setUid(user.getId());
            LoginUserPool dbLoginUserPool = loginUserPoolDao.load(loginUserPool);
            if (dbLoginUserPool != null) {
                throw new StoreSystemException("账号已存在");
            }

            oldLoginUserPool = new LoginUserPool();
            oldLoginUserPool.setAccount(currentUser.getPhone());
            oldLoginUserPool.setLoginType(LoginUserPool.loginType_phone);
            oldLoginUserPool.setUserType(currentUser.getUserType());
            oldLoginUserPool.setUid(currentUser.getId());

            currentUser.setPhone(user.getPhone());
        }
        if (StringUtils.isNotBlank(user.getDesc())
                && !user.getDesc().equals(currentUser.getDesc())) {
            currentUser.setDesc(user.getDesc());
        }
        if (updateRids != null) {
            currentUser.setRids(updateRids);
        }
        if (StringUtils.isNotBlank(user.getPassword())
                && !MD5Utils.md5ReStr(user.getPassword().getBytes()).equals(
                MD5Utils.md5ReStr(currentUser.getPassword().getBytes()))) {
            currentUser.setPassword(MD5Utils.md5ReStr(user.getPassword().getBytes()));
            Random rand = new Random();
            currentUser.setRand(rand.nextInt(100000000));
        }

        boolean sign = userDao.update(currentUser);
        if (oldLoginUserPool != null) {
            loginUserPoolDao.delete(oldLoginUserPool);
            loginUserPoolDao.insert(loginUserPool);
        }
        return sign;
    }


    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        User user = userDao.load(id);
        if (null == user) throw new StoreSystemException("用户不存在");
        user.setStatus(status);
        boolean sign = userDao.update(user);
        LoginUserPool loginUserPool = new LoginUserPool();
        loginUserPool.setUserType(User.userType_user);
        loginUserPool.setLoginType(LoginUserPool.loginType_phone);
        loginUserPool.setAccount(user.getPhone());
        if (user.getStatus() == User.status_delete) {
            loginUserPoolDao.delete(loginUserPool);
        } else if (user.getStatus() == User.status_nomore) {
            loginUserPoolDao.insert(loginUserPool);
        }
        return sign;
    }


    @Override
    public boolean updateUserPermission(long uid, List<Long> pids) throws Exception {
        List<Long> nowPids = getUserPermissionIds(uid);
        List<Long> newPids = getTreePids(pids);

        List<Long> addPids = Lists.newArrayList();
        List<Long> deletePids = Lists.newArrayList();

        for (long pid : newPids) {
            if (!nowPids.contains(pid)) {
                addPids.add(pid);
            }
        }

        for (long pid : nowPids) {
            if (!newPids.contains(pid)) {
                deletePids.add(pid);
            }
        }

        //增加
        for (long pid : addPids) {
            UserPermissionPool pool = new UserPermissionPool();
            pool.setUid(uid);
            pool.setPid(pid);
            pool.setType(UserPermissionPool.type_on);
            pool = userPermissionPoolDao.load(pool);
            if (pool == null) {
                pool = new UserPermissionPool();
                pool.setUid(uid);
                pool.setPid(pid);
                pool.setType(UserPermissionPool.type_on);
                userPermissionPoolDao.insert(pool);
            } else {
                pool.setType(UserPermissionPool.type_on);
                userPermissionPoolDao.update(pool);
            }
        }

        //减少
        for (long pid : deletePids) {
            UserPermissionPool pool = new UserPermissionPool();
            pool.setUid(uid);
            pool.setPid(pid);
            pool.setType(UserPermissionPool.type_off);
            pool = userPermissionPoolDao.load(pool);
            if (pool == null) {
                pool = new UserPermissionPool();
                pool.setUid(uid);
                pool.setPid(pid);
                pool.setType(UserPermissionPool.type_off);
                userPermissionPoolDao.insert(pool);
            } else {
                pool.setType(UserPermissionPool.type_off);
                userPermissionPoolDao.update(pool);
            }
        }
        return true;
    }

    private List<Long> getTreePids(List<Long> pids) throws Exception {
        Set<Long> treePids = Sets.newHashSet();
        loopTreePids(treePids, pids);
        return Lists.newArrayList(treePids);
    }

    private void loopTreePids(Set<Long> treePids, List<Long> pids) throws Exception {
        List<Permission> permissions = permissionDao.load(pids);
        List<Long> parentPids = Lists.newArrayList();
        for (Permission permission : permissions) {
            if (null != permission) {
                treePids.add(permission.getId());
                if (permission.getPid() > 0) parentPids.add(permission.getPid());
            }
        }
        if (parentPids.size() > 0) loopTreePids(treePids, parentPids);
    }

    @Override
    public boolean createLoginCodeAuthCode(String phone) throws Exception {
        String sent = cache.getString("exists_login_code_" + auth_code_key(phone));
        if (StringUtils.isNotBlank(sent)) {
            throw new StoreSystemException("验证码已经发送");
        }
        Random random = new Random();
        int num = random.nextInt(9999);
        String code = String.format("%04d", num);
        String temCode = "{\"code\":\"" + code + "\"}";
        boolean sign = SmsUtils.sendConfigMessge(phone, templateId, temCode, messageAccessKeyId, messageAccessKeySecret, company);
        if (sign) {
            cache.setString("exists_login_code_" + auth_code_key(phone), 58, "true");
            cache.setString("login_code_" + auth_code_key(phone), 5 * 60, code);
            cache.setString("login_code_time_" + phone, 10 * 60, System.currentTimeMillis() + "");

        }
        return true;
    }

    @Override
    public String getLoginCodeAuthCode(String phone) throws Exception {
        return cache.getString("login_code_" + auth_code_key(phone));
    }

    @Override
    public ClientUserOnLogin loginForCode(String phone) throws Exception {
        LoginUserPool loginUserPool = new LoginUserPool();
        loginUserPool.setAccount(phone);
        loginUserPool.setLoginType(LoginUserPool.loginType_phone);
        loginUserPool.setUserType(User.userType_backendUser);
        LoginUserPool load = loginUserPoolDao.load(loginUserPool);

        //验证码登录：两种情况
        //一:提示未注册  这个一般在发送登录验证码时就进行了判断,这里第二次验证
        if (load == null) {
            throw new StoreSystemException("你还没有注册账号");
        }

        User dbUser = userDao.load(load.getUid());
        if (null == dbUser || dbUser.getStatus() == User.status_delete) {
            throw new StoreSystemException("用户不存在");
        }

        if (dbUser.getStatus() == User.status_delete) {
            throw new StoreSystemException("用户已被冻结");
        }
        Random rand = new Random();
        dbUser.setRand(rand.nextInt(100000000));
        userDao.update(dbUser);
        return transformUser(dbUser);
    }

    @Override
    public boolean createAuthCode(String phone, String template) throws Exception {
        String sent = cache.getString("exists_auth_common_user_" + (phone));
        if (StringUtils.isNotBlank(sent)) {
            throw new StoreSystemException("验证码已经发送");
        }
        Random random = new Random();
        int num = random.nextInt(9999);
        String code = String.format("%04d", num);
        boolean sign = SmsUtils.sendConfigMessge(phone, template, code, messageAccessKeyId, messageAccessKeySecret, company);
//        boolean sign = SmsUtils.sendSms(phone, template, code);
        if (sign) {
            cache.setString("exists_auth_common_user_" + (phone), 58, "true");
            cache.setString("auth_common_user_" + (phone), 5 * 60, code);
        } else {
            if (StringUtils.isNotBlank(sent)) {
                throw new StoreSystemException("当前手机号已被注册");
            }
        }
        return true;
    }

    @Override
    public String getAuthCode(String phone) throws Exception {
        String res = cache.getString("auth_common_user_" + (phone));
        return res;
    }


    public void addScore(long id, int num) throws Exception {
        userDao.increment(userDao.load(id), "score", num);
    }

    private ClientUserOnLogin transformUser(User user) throws Exception {
        ClientUserOnLogin clientUserOnLogin = new ClientUserOnLogin(user);
        Subordinate subordinate = subordinateDao.load(clientUserOnLogin.getSid());
        Subordinate load = subordinateDao.load(clientUserOnLogin.getPsid());
        clientUserOnLogin.setSubName(subordinate != null ? subordinate.getName() : "");
        clientUserOnLogin.setSName(load != null ? load.getName() : "");

        return clientUserOnLogin;
    }

    public List<ClientUser> transformClient(List<User> users) throws Exception {
        Set<Long> sids = Sets.newHashSet();
        for (User user : users) {
            if (user.getPsid() > 0) sids.add(user.getPsid());
            if (user.getSid() > 0) sids.add(user.getSid());
        }
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(sids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        List<ClientUser> res = Lists.newArrayList();
        for (User user : users) {
            ClientUser clientUser = transformClient(user);
            res.add(clientUser);
        }
        return res;
    }

    private ClientUser transformClient(User user) throws Exception {
        ClientUser clientUser = new ClientUser(user);

        Set<Long> sids = Sets.newHashSet();
        if (user.getPsid() > 0) sids.add(user.getPsid());
        if (user.getSid() > 0) sids.add(user.getSid());
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(sids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        if (clientUser.getPsid() > 0) {
            Subordinate subordinate = subordinateMap.get(clientUser.getPsid());
            if (subordinate != null) clientUser.setPSubName(subordinate.getName());
        }
        if (clientUser.getSid() > 0) {
            Subordinate subordinate = subordinateMap.get(clientUser.getSid());
            if (subordinate != null) clientUser.setSubName(subordinate.getName());
        }
        //会员等级 && 会员折扣
        if (user != null && user.getUserGradeId() > 0) {
            UserGrade userGrade = userGradeDao.load(user.getUserGradeId());
            if (userGrade != null) {
                clientUser.setMember(userGrade.getTitle());
                // TODO: 2019/7/10
//                clientUser.setDiscount(userGrade.getDiscount());
            }
        }
        //消费次数
        List<BusinessOrder> orders = businessOrderDao.getUserList(user.getId(), BusinessOrder.status_pay, BusinessOrder.makeStatus_qu_yes);
        if (orders.size() > 0) {
            clientUser.setPayCount(orders.size());
            // TODO: 2019/7/24
//            Map<String,Integer> map = businessOrderService.calculateSale(Lists.newArrayList(orders.get(0)));
//            clientUser.setLastMoney(map.get("sale"));//上次消费金额
        }
        //推荐人
        if (user.getRecommender() > 0) {
            long uid = user.getRecommender();
            User recommender = userDao.load(uid);
            if (recommender != null) {
                clientUser.setTname(recommender.getName());
                clientUser.setTphone(recommender.getPhone());
            }
        }

        return clientUser;
    }

    @Override
    public List<User> getAllUserBySid(long sid) throws Exception {
        List<SubordinateUserPool> subordinateUserPools = subordinateUserPoolDao.getAllList(sid, User.status_nomore);
        List<Long> uids = new ArrayList<>();
        for (SubordinateUserPool subordinateUserPool : subordinateUserPools) {
            uids.add(subordinateUserPool.getUid());
        }
        return userDao.load(uids);
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        //查找用户
        User olduser = userDao.load(user.getId());
        //姓名
        if (StringUtils.isNotBlank(user.getName())) {
            olduser.setName(user.getName());
        }
        //性别
        if (user.getSex() != 0) {
            olduser.setSex(user.getSex());
        }
        //生日
        if (user.getBirthdate() != 0) {
            olduser.setBirthdate(user.getBirthdate());
        }
        //年龄
        if (user.getAge() != 0) {
            olduser.setAge(user.getAge());
        }
        //入职时间
        if (user.getWorkingDate() != 0) {
            olduser.setWorkingDate(user.getWorkingDate());
        }
        //封面图
        if (StringUtils.isNotBlank(user.getCover())) {
            olduser.setCover(user.getCover());
        }
        //职位
        if (StringUtils.isNotBlank(user.getJob())) {
            olduser.setJob(user.getJob());
        }
        //邮件
        if (StringUtils.isNotBlank(user.getMail())) {
            olduser.setMail(user.getMail());
        }
        //身份证号
        if (StringUtils.isNotBlank(user.getIdCard())) {
            olduser.setIdCard(user.getIdCard());
        }
        //银行卡号
        if (user.getBankCard() != null && user.getBankCard().size() > 0) {
            olduser.setBankCard(user.getBankCard());
        }
        //手机号
        if (StringUtils.isNotBlank(user.getPhone())) {
            olduser.setPhone(user.getPhone());
        }
        //储值金额(分)
        if (user.getMoney() != 0) {
            olduser.setMoney(user.getMoney() * 100);
        }
        if (user.getAid() != 0) {
            olduser.setAid(user.getAid());
        }
        if (user.getSid() != 0) {
            SubordinateUserPool subordinateUserPool = new SubordinateUserPool();
            subordinateUserPool.setUid(olduser.getId());
            subordinateUserPool.setSid(olduser.getSid());
            subordinateUserPool = subordinateUserPoolDao.load(subordinateUserPool);
            if (subordinateUserPool != null) {
                subordinateUserPool.setStatus(User.status_delete);
                subordinateUserPoolDao.update(subordinateUserPool);
            }
            SubordinateUserPool newPool = new SubordinateUserPool();
            newPool.setUid(user.getId());
            newPool.setSid(user.getSid());
            SubordinateUserPool dbInfo = subordinateUserPoolDao.load(newPool);
            if (dbInfo != null) {
                newPool.setStatus(User.status_delete);
                subordinateUserPoolDao.update(dbInfo);
            } else {
                newPool.setStatus(User.status_nomore);
                subordinateUserPoolDao.insert(newPool);
            }
            olduser.setSid(user.getSid());
        }
        // 禁用开启
        if (user.getStatus() != olduser.getStatus()) {
            olduser.setStatus(user.getStatus());
        }
        boolean res = userDao.update(olduser);
        if (res && !olduser.getPhone().equals(user.getPhone())) {
            LoginUserPool loginUserPool = new LoginUserPool();
            loginUserPool.setUserType(User.userType_backendUser);//员工
            loginUserPool.setLoginType(LoginUserPool.loginType_phone);
            loginUserPool.setAccount(olduser.getPhone());
            loginUserPoolDao.delete(loginUserPool);
            loginUserPool.setUid(olduser.getId());
            loginUserPoolDao.insert(loginUserPool);
            return res;
        }
        return res;
    }

    private void check(User user) throws StoreSystemException {
        if (user.getUserType() != User.userType_user) throw new StoreSystemException("用户类型错误");
        String name = user.getName();
        if (StringUtils.isBlank(name)) throw new StoreSystemException("姓名不能为空");
        String phone = user.getPhone();
        if (StringUtils.isBlank(phone)) throw new StoreSystemException("手机号不能为空");
        if (user.getSid() == 0) throw new StoreSystemException("店铺ID错误");

    }

    @Override
    public User addCustomer(User user) throws Exception {
        check(user);
        long subid = user.getSid();
        Subordinate subordinate = subordinateDao.load(subid);
        long pSubid = subordinate.getPid();
        if (subordinate.getPid() == 0) pSubid = subid;
        user.setPsid(pSubid);
        user = userDao.insert(user);
        LoginUserPool loginUserPool = new LoginUserPool();
        loginUserPool.setUserType(User.userType_user);
        loginUserPool.setLoginType(LoginUserPool.loginType_phone);
        loginUserPool.setAccount(user.getPhone());
        loginUserPool.setUid(user.getId());
        LoginUserPool dbInfo = loginUserPoolDao.load(loginUserPool);
        if (dbInfo == null) {
            loginUserPoolDao.insert(loginUserPool);
        }
        return user;
    }

    @Override
    public boolean updateCustomer(User user) throws Exception {
        check(user);
        User dbUser = userDao.load(user.getId());
        long subid = user.getSid();
        Subordinate subordinate = subordinateDao.load(subid);
        long pSubid = subordinate.getPid();
        if (subordinate.getPid() == 0) pSubid = subid;
        user.setPsid(pSubid);
        boolean res = userDao.update(user);
        if (res && !dbUser.getPhone().equals(user.getPhone())) {
            LoginUserPool loginUserPool = new LoginUserPool();
            loginUserPool.setUserType(User.userType_user);
            loginUserPool.setLoginType(LoginUserPool.loginType_phone);
            loginUserPool.setAccount(dbUser.getPhone());
            loginUserPoolDao.delete(loginUserPool);
            loginUserPool = new LoginUserPool();
            loginUserPool.setUserType(User.userType_user);
            loginUserPool.setLoginType(LoginUserPool.loginType_phone);
            loginUserPool.setAccount(user.getPhone());
            loginUserPool.setUid(user.getId());
            loginUserPoolDao.insert(loginUserPool);
        }
        return res;
    }

    @Override
    public boolean delCustomer(long id) throws Exception {
        User user = userDao.load(id);
        if (user != null) {
            if (user.getUserType() != User.userType_user) throw new StoreSystemException("用户类型错误");
            user.setStatus(User.status_delete);
            boolean res = userDao.update(user);
            if (res) {
                LoginUserPool loginUserPool = new LoginUserPool();
                loginUserPool.setUserType(User.userType_user);
                loginUserPool.setLoginType(LoginUserPool.loginType_phone);
                loginUserPool.setAccount(user.getPhone());
                loginUserPoolDao.delete(loginUserPool);
            }
            return res;
        }
        return false;
    }

    @Override
    public Pager getBackCustomerPager(Pager pager, long pSubid, int userType) throws Exception {
        String sql = "SELECT * FROM `user` where psid = " + pSubid + " and `status` = " + User.status_nomore;
        String sqlCount = "SELECT COUNT(id) FROM `user` where psid = " + pSubid + " and `status` = " + User.status_nomore;
        if (userType > -1) {
            sql = sql + " AND userType =" + userType;
            sqlCount = sqlCount + " AND userType =" + userType;
        }
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<User> users = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientUser> data = transformClient(users);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    //name 是精确查询
    //name1 是模糊查询
    @Override
    public Pager getBackSubCustomerPager(Pager pager, long subid, String phone, String phone1, String name, String name1, int sex, int userType, String job, long userGradeId, int cardNumber) throws Exception {
        String sql = "SELECT * FROM `user` where sid = " + subid;
        String sqlCount = "SELECT COUNT(id) FROM `user` where sid = " + subid;
        String limit = " limit %d , %d ";
        List<Object> objects = new ArrayList<>();

        if (userType > -1) {
            sql = sql + " AND userType =" + userType;
            sqlCount = sqlCount + " AND userType =" + userType;
        }
        if (cardNumber > -1) {
            sql = sql + " AND cardNumber LIKE ?";
            sqlCount = sqlCount + " AND cardNumber LIKE ?";
            objects.add("%" + cardNumber + "%");
        }
        if (StringUtils.isNotBlank(job)) {
            sql = sql + " and `job` =" + job;
            sqlCount = sqlCount + " and `job` =" + job;
        }

        if (StringUtils.isNotBlank(name) && StringUtils.isBlank(name1)) {//精确查询t
            sql = sql + " AND `name` = '" + name + "'";
            sqlCount = sqlCount + " AND `name` = '" + name + "'";
        }
        if (StringUtils.isNotBlank(name1) && StringUtils.isBlank(name)) {//模糊查询
            sql = sql + " and `name` like ?";
            sqlCount = sqlCount + " and `name` like ?";
            objects.add("%" + name1 + "%");
        }
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(name1)) {
            if (name.equals(name1)) {//相等精确查询
                sql = sql + " AND `name` = '" + name + "'";
                sqlCount = sqlCount + " and `name` = '" + name + "'";
            } else if (name.indexOf(name1) >= 0) {//如果 姓名.indexOf(姓/名) 按照姓/名模糊查询
                sql = sql + " and `name` like ?";
                sqlCount = sqlCount + " and `name` like ?";
                objects.add("%" + name1 + "%");
            } else {//输入的两个值不能指向同一个用户
                return pager;
            }
        }
        if (StringUtils.isNotBlank(phone) && StringUtils.isBlank(phone1)) {
            sql = sql + " and `phone` = '" + phone + "'";
            sqlCount = sqlCount + " and `phone` = '" + phone + "'";
        }
        if (StringUtils.isNotBlank(phone1) && StringUtils.isBlank(phone)) {
            sql = sql + " and `phone` like ?";
            sqlCount = sqlCount + " and `phone` like ?";
            objects.add("%" + phone1 + "%");
        }
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(phone1)) {
            if (phone.equals(phone1)) {
                sql = sql + " and `phone` = '" + phone + "'";
                sqlCount = sqlCount + " and `phone` = '" + phone + "'";
            } else if (phone.indexOf(phone1) >= 0) {
                sql = sql + " and `phone` like ?";
                sqlCount = sqlCount + " and `phone` like ?";
                objects.add("%" + phone1 + "%");
            } else {//输入的两个值不能指向同一个用户
                return pager;
            }
        }
        if (sex > -1) {
            sql = sql + " AND sex =" + sex;
            sqlCount = sqlCount + " AND sex =" + sex;
        }
        if (userGradeId > -0) {
            sql = sql + " AND userGradeId =" + sex;
            sqlCount = sqlCount + " AND userGradeId =" + sex;
        }
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<User> users = null;
        int count = 0;
        if (objects.size() > 0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            users = this.jdbcTemplate.query(sql, rowMapper, args);
            count = this.jdbcTemplate.queryForObject(sqlCount, args, Integer.class);
        } else {
            users = this.jdbcTemplate.query(sql, rowMapper);
            count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        }
        List<ClientUser> data = transformClient(users);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getPager(final Pager pager, final long psid, final int userType, final int status, final String name) throws Exception {
        return new PagerRequestService<User>(pager, 0) {
            @Override
            public List<User> step1GetPageResult(String cursor, int size) throws Exception {
                String sql = "select * from `user` where 1=1  ";
                if (psid > 0) {
                    sql = sql + " and `psid` = " + psid;
                }
                if (status > -1) {
                    sql = sql + " and `status` = " + status;
                }
                if (userType > -1) {
                    sql = sql + " and `userType` = " + userType;
                }
                if (StringUtils.isNotBlank(name)) {
                    sql = sql + " and `name` like '%" + name + "%'";
                }
                long ctimeCursor = Long.parseLong(cursor);
                if (ctimeCursor == 0) ctimeCursor = Long.MAX_VALUE;
                sql = sql + " and `ctime` <= " + ctimeCursor + " order by ctime desc limit " + size;
                return jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<User>(User.class));
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<User> step3FilterResult(List<User> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }

            @Override
            public List<?> step4TransformData(List<User> unTransformDatas, PagerSession session) throws Exception {

                return transformClient(unTransformDatas);
            }
        }.getPager();
    }

    @Override
    public Pager getIntentionPager(final Pager pager, final long psid, final int userType, final int status, final String name) throws Exception {
        return new PagerRequestService<User>(pager, 0) {
            @Override
            public List<User> step1GetPageResult(String cursor, int size) throws Exception {
                String sql = "SELECT DISTINCT u.* FROM `user` AS u JOIN `optometry_info` AS o ON u.id = o.uid WHERE 1 = 1";
                if (psid > 0) {
                    sql = sql + " and u.`psid` = " + psid;
                }
                if (status > -1) {
                    sql = sql + " and u.`status` = " + status;
                }
                if (userType > -1) {
                    sql = sql + " and u.`userType` = " + userType;
                }
                if (StringUtils.isNotBlank(name)) {
                    sql = sql + " and u.`name` like '%" + name + "%'";
                }
                long ctimeCursor = Long.parseLong(cursor);
                if (ctimeCursor == 0) ctimeCursor = Long.MAX_VALUE;
                sql = sql + " and u.`ctime` <= " + ctimeCursor + " order by u.ctime desc limit " + size;
                return jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<User>(User.class));
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<User> step3FilterResult(List<User> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }

            @Override
            public List<?> step4TransformData(List<User> unTransformDatas, PagerSession session) throws Exception {

                return transformClient(unTransformDatas);
            }
        }.getPager();
    }

    @Override
    public Set<String> getAllUserJob(long sid, int userType) throws Exception {
        TransformFieldSetUtils fieldSetUtils = new TransformFieldSetUtils(User.class);
        List<User> users = userDao.getAllList(sid, userType, User.status_nomore);
        return fieldSetUtils.fieldList(users, "job");
    }

    @Override
    public List<ClientUser> getAllUser(long sid, int userType) throws Exception {
        String sql = " SELECT * FROM `user` WHERE 1=1 ";

        if (sid > 0) {
            sql = sql + " AND sid =" + sid;
        }
        if (userType > 0) {
            sql = sql + " AND userType =" + userType;
        }
        sql = sql + " order  by `ctime` desc";
        List<User> users = jdbcTemplate.query(sql, rowMapper);
        return transformClient(users);
    }

    @Override
    public List<ClientUser> getAllUser(long sid, int userType, long aid) throws Exception {
        String sql = " SELECT * FROM `user` WHERE 1=1 ";

        if (sid > 0) {
            sql = sql + " AND sid =" + sid;
        }
        if (userType > 0) {
            sql = sql + " AND userType =" + userType;
        }
        {
            sql = sql + " AND aid =" + aid;
        }
        sql = sql + " order  by `ctime` desc";
        List<User> users = jdbcTemplate.query(sql, rowMapper);
        return transformClient(users);
    }

    @Override
    public ClientUser getUser(String phone) throws Exception {
        User user = userDao.getUserByPhone(User.userType_user, User.status_nomore, phone);
        return transformClient(user);
    }

    @Override
    public ClientUser checkUserGradeInfo(User user) throws Exception {
        //根据手机号查询user
        List<User> users = userDao.getAllLists(User.userType_user, User.status_nomore, user.getContactPhone());
        if (users.size() <= 0) {
            //生成会员卡号
            user.setCardNumber(new Random().nextInt(1000000));
            user.setUserType(User.userType_user);
            //设置一个最低等级的会员
            String sql = " select * from user_grade where 1=1 AND subid = " + user.getPsid() + " order by conditionScore";
            List<UserGrade> userGrades = jdbcTemplate.query(sql, ugMapper);
            if (userGrades.size() > 0) {
                user.setUserGradeId(userGrades.get(0).getId());
            } else {
                throw new StoreSystemException("请先在本公司下设置会员等级!");
            }
            check(user);
            long subid = user.getSid();
            Subordinate subordinate = subordinateDao.load(subid);
            long pSubid = subordinate.getPid();
            if (subordinate.getPid() == 0) pSubid = subid;
            user.setPsid(pSubid);
            user.setRand(new Random().nextInt(100000000));
            user.setPassword(MD5Utils.md5ReStr("123456".getBytes()));
            user.setContactPhone(user.getPhone());
            user = userDao.insert(user);
            return transformClient(user);
        }
        return transformClient(users.get(0));
    }

    @Override
    public List<ClientUser> getUserByJob(String job, long sid, int userType) throws Exception {
        return transformClient(userDao.getAllListByJob(userType, User.status_nomore, job, sid));
    }

    @Override
    public List<ExportUser> getExportUserInfo(long sid, String phone, int sex, String job) throws Exception {
        String sql = " SELECT * FROM `user` WHERE status = " + User.status_nomore + " AND `userType` = " + User.userType_user;
        if (sid > 0) {
            sql = sql + " AND sid = " + sid;
        }
        if (StringUtils.isNotBlank(phone)) {
            sql = sql + " AND `phone` LIKE ?";
        }
        if (sex > -1) {
            sql = sql + " AND sex = " + sex;
        }
        if (StringUtils.isNotBlank(job)) {
            sql = sql + " AND `job` LIKE ?";
        }
        sql = sql + " ORDER  BY `ctime` DESC ";
        List<Object> objects = new ArrayList<>();
        List<User> users = null;
        int count = 0;
        if (StringUtils.isNotBlank(job)) {
            objects.add("%" + job + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            objects.add("%" + phone + "%");
        }
        if (objects.size() > 0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            users = this.jdbcTemplate.query(sql, rowMapper, args);
        } else {
            users = this.jdbcTemplate.query(sql, rowMapper);
        }

        if (users.size() == 0 || users == null) {
            throw new StoreSystemException("暂未查询到可导出的信息!");
        }
        List<ExportUser> exportUsers = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int index = 1;
        for (User user : users) {
            ExportUser exportUser = new ExportUser();
            exportUser.setName(user.getName());
            exportUser.setAddress(user.getPlace());
            exportUser.setBirthday(sdf.format(new Date(user.getBirthdate())));
            exportUser.setDesc(user.getDesc());
            exportUser.setEmail(user.getMail());
            exportUser.setJob(user.getJob());
            exportUser.setMoney(String.valueOf(user.getMoney()));
            exportUser.setPhone(user.getPhone());
            exportUser.setScore(String.valueOf(user.getScore()));
            exportUser.setUserName(user.getUserName());
            exportUser.setWeChat(user.getWeixinId());
            exportUser.setAge(String.valueOf(user.getAge()));
            if (user.getSex() == 0) {
                exportUser.setSex("未知");
            } else if (user.getSex() == 1) {
                exportUser.setSex("男");
            } else {
                exportUser.setSex("女");
            }
            exportUser.setIdCard(user.getIdCard());
            exportUser.setNumber(String.valueOf(index++));
            exportUsers.add(exportUser);
        }
        return exportUsers;
    }

    @Override
    public Map<String, Object> taskReward(String date, long sid) throws Exception {
        Map<String, Object> map = Maps.newHashMap();
        //查询门店内的员工
        List<User> users = userDao.getAllList(sid, User.userType_backendUser, User.status_nomore);
        List<ClientUser> clientUsers = Lists.newArrayList();
        int personal = 0;//个人销售额(分)
        int tem = 0;//总店销售额(分)
        //查询员工完成的所有订单的金额 已完成订单的销售量 保存下来
        for (User user : users) {
            List<UserMissionPool> userMissionPools = userMissionPoolDao.getAllList(user.getId());
            for (UserMissionPool userMissionPool : userMissionPools) {
                List<Long> oids = userMissionPool.getOids();
                List<BusinessOrder> orders = Lists.newArrayList();
                for (Long id : oids) {
                    // TODO: 2019/7/24
//                    BusinessOrder order = orderDao.load(id);
//                    if(order!=null){ orders.add(order); }
                }
                for (BusinessOrder order : orders) {
                    //判断订单是否当前月份
                    // TODO: 2019/7/24
//                    if(inExistence(date,order.getPayTime())){
//                        Map<String,Integer> saleMap = orderService.calculateSale(Lists.newArrayList(order));
//                        personal += saleMap.get("sale")/100;
//                    }
                }
                //判断是否有完成任务
                if (userMissionPool.getUid() == user.getId() && userMissionPool.getProgress() >= 100) {
                    personal += userMissionPool.getPrice() / 100;
                }
            }
            tem += personal;
            ClientUser clientUser = transformClient(user);
            clientUser.setSale(personal);
            clientUsers.add(clientUser);
            personal = 0;
        }
        //sale数值大的排前面
        Collections.sort(clientUsers);
        map.put("tem", tem);
        map.put("clientUsers", clientUsers);
        return map;
    }

    @Override
    public StatisticsOrderUser statisticsOrderUser(long sid, int status, long startTime, long endTime) throws Exception {
        String sql = " SELECT * FROM `business_order` WHERE 1=1 AND subId = " + sid;
        if (startTime > 0) {
            sql = sql + " AND ctime >= " + startTime;
        }
        if (endTime > 0) {
            sql = sql + " AND  ctime <= " + endTime;
        }
        if (status > -1 && status != BusinessOrder.makeStatus_no && status != BusinessOrder.status_no_pay && status != BusinessOrder.makeStatus_qu_no) {
            sql = sql + " AND makestatus = " + status;
        } else {
            sql = sql + " and (`makeStatus` = " + BusinessOrder.makeStatus_no + " OR `makeStatus` = " + BusinessOrder.status_no_pay + " OR `makeStatus` = " + BusinessOrder.makeStatus_qu_no + " ) ";
        }
        List<BusinessOrder> orders = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<BusinessOrder>(BusinessOrder.class));
        List<Long> uids = Lists.newArrayList();
        int pNumber = 0;//总顾客人数
        if (orders.size() > 0) {
            for (BusinessOrder order : orders) {
                uids.add(order.getUid());
            }
        }
        int man = 0;//男人
        int woman = 0;//女人
        int oldNumber = 0;//老顾客人数
        int vxNumber = 0; //微信人数
        int phoneNumber = 0;//手机号认证人数
        int moneyNumber = 0;//充值人数
        List<Long> res = Lists.newArrayList();
        for (Long id : uids) {
            User user = userDao.load(id);
            if (user != null) {
                //判断 性别
                if (user.getSex() == User.sex_mai) {
                    man++;
                } else if (user.getSex() == User.sex_woman) {
                    woman++;
                }
                //判断是否老顾客

                //判断是否认证微信
                if (StringUtils.isNotBlank(user.getWeixinId())) {
                    vxNumber++;
                }
                //判断是否认证手机号
                if (StringUtils.isNotBlank(user.getPhone())) {
                    phoneNumber++;
                }
            }
        }
        StatisticsOrderUser statisticsOrderUser = new StatisticsOrderUser();
        statisticsOrderUser.setPNumber(man + woman);//总人数
        statisticsOrderUser.setOldNumber(cale(statisticsOrderUser.getPNumber(), oldNumber));//老顾客占比
        statisticsOrderUser.setMan(cale(statisticsOrderUser.getPNumber(), man));//男比例
        statisticsOrderUser.setWoman(cale(statisticsOrderUser.getPNumber(), woman));//女比例
        statisticsOrderUser.setVxNumber(vxNumber);//微信
        statisticsOrderUser.setPhoneNumber(phoneNumber);//手机号
        statisticsOrderUser.setMoneyNumber(moneyNumber);//储值
        return statisticsOrderUser;
    }

    @Override
    public List<ClientUser> getUserListByPhone(long sid, int userType, String phone, String name) throws Exception {
        String sql = "SELECT * FROM `user` where sid = " + sid;
        if (userType > 0) {
            sql = sql + " AND userType =" + userType;
        }
        if (StringUtils.isNotBlank(phone)) {
            sql = sql + " and `contactPhone` like ?";
        }
        if (StringUtils.isNotBlank(name)) {
            sql = sql + " and `name` like ?";
        }
        List<User> users = null;
        List<Object> objects = Lists.newArrayList();
        if (StringUtils.isNotBlank(phone)) {
            objects.add("%" + phone + "%");
        }
        if (StringUtils.isNotBlank(name)) {
            objects.add("%" + name + "%");
        }
        if (objects.size() > 0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            users = jdbcTemplate.query(sql, rowMapper, args);
        } else {
            users = jdbcTemplate.query(sql, rowMapper);
        }
        return transformClient(users);
    }

    @Override
    public List<ClientUser> searchUserList(long sid, int userType, String phone, String name) throws Exception {
        String sql = "SELECT * FROM `user` where sid = " + sid;
        if (userType > -1) {
            sql = sql + " AND userType =" + userType;
        }
        if (StringUtils.isNotBlank(phone)) {
            sql = sql + " AND `contactPhone` = '" + phone + "'";
        }
        if (StringUtils.isNotBlank(name)) {
            sql = sql + " AND `name` = '" + name + "'";
        }

        List<User> users = null;
        List<Object> objects = Lists.newArrayList();
        if (objects.size() > 0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            System.err.println(sql);
            users = jdbcTemplate.query(sql, rowMapper, args);
        } else {
            System.err.println(sql);
            users = jdbcTemplate.query(sql, rowMapper);
        }
        return transformClient(users);
    }

    @Override
    public ClientMissForUser taskRewardApp(String date, long sid) throws Exception {
        ClientMissForUser clientMissForUser = new ClientMissForUser();
        List<User> users = userDao.getAllList(sid, User.userType_backendUser, User.status_nomore);
        List<ClientUser> clientUsers = Lists.newArrayList();
        int personal = 0;//个人销售额(分)
        int tem = 0;//总店销售额(分)
        //查询员工完成的所有订单的金额 已完成订单的销售量 保存下来
        for (User user : users) {
            List<UserMissionPool> userMissionPools = userMissionPoolDao.getAllList(user.getId());
            for (UserMissionPool userMissionPool : userMissionPools) {
                long ctime = userMissionPool.getCtime();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(ctime);
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);
                String o = "";
                if (month < 10) {
                    o = year + "0" + month;
                } else {
                    o = year + "" + month;
                }
                if (o.equals(date)) {
                    //判断是否有完成任务
                    if (userMissionPool.getUid() == user.getId() && userMissionPool.getProgress() >= 100) {
                        personal += userMissionPool.getPrice() / 100;
                    }
                }

            }
            tem += personal;
            ClientUser clientUser = new ClientUser(user);
            clientUser.setSale(personal);
            if(clientUser.getSale()>0){
                clientUsers.add(clientUser);
            }
        }
        Collections.sort(clientUsers);
        clientMissForUser.setTem(tem);
        clientMissForUser.setList(clientUsers);
        return clientMissForUser;


    }

    @Override
    public Pager getStaffUserBySid(Pager pager, final Long sid) throws Exception {
        return new PagerRequestService<User>(pager, 0) {
            @Override
            public List<User> step1GetPageResult(String s, int i) throws Exception {
                List<User> allList = userDao.getPagerAllList(sid, User.userType_backendUser, User.status_nomore, Double.parseDouble(s), i);
                return allList;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<User> step3FilterResult(List<User> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<User> list, PagerSession pagerSession) throws Exception {
                List<ClientUser> clientUserList = Lists.newArrayList();
                for (User user : list) {
                    ClientUser clientUser = new ClientUser(user);
                    clientUserList.add(clientUser);
                }
                return clientUserList;
            }
        }.getPager();

    }

    @Override
    public Pager getAllStaffUserBySid(Pager pager, final Long sid) throws Exception {
        return new PagerRequestService<User>(pager, 0) {

            @Override
            public List<User> step1GetPageResult(String s, int i) throws Exception {
                List<User> allList = userDao.getAllUserList(sid, User.userType_backendUser, Double.parseDouble(s), i);
                return allList;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<User> step3FilterResult(List<User> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<User> list, PagerSession pagerSession) throws Exception {
                List<ClientUser> clientUserList = Lists.newArrayList();

                for (User user : list) {
                    ClientUser clientUser = new ClientUser(user);
                    clientUserList.add(clientUser);
                }
                return clientUserList;
            }
        }.getPager();
    }

    @Override
    public Map<Long, List<ClientUser>> getAllUserByPsid(long psid) throws Exception {
        Map<Long, List<ClientUser>> map = new HashedMap();
        List<User> allListByPsid = userDao.getAllListByPsid(psid, User.userType_backendUser, User.status_nomore);
        for (User li : allListByPsid) {
            ClientUser clientUser = new ClientUser(li);
            Subordinate subordinates = subordinateDao.load(clientUser.getSid());
            if (subordinates != null) clientUser.setSubName(subordinates.getName());
            if (!map.containsKey(li.getSid())) {
                List<ClientUser> clientUserList = new ArrayList<>();
                clientUserList.add(clientUser);
                map.put(clientUser.getSid(), clientUserList);
            } else {
                map.get(li.getSid()).add(clientUser);
            }
        }
        return map;
    }

    //传入年月 判断是否当前月
    private boolean inExistence(String date, long time) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String mon = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (String.valueOf(mon).length() == 1) {
            mon = 0 + mon;
        }
        String res = year + mon;
        return res.equals(date);
    }

    //计算比例
    private int cale(int all, int count) throws Exception {
        float aNum = all;
        float cNum = count;
        return (int) (cNum / aNum * 100);
    }

}
