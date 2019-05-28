package com.store.system.service.impl;

import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.client.ClientUser;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserService;
import com.store.system.util.SmsUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.redis.JedisX;
import com.quakoo.baseFramework.secure.MD5Utils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    private RowMapperHelp<User> rowMapper = new RowMapperHelp<>(User.class);

    private RowMapperHelp<UserGrade> ugMapper = new RowMapperHelp<>(UserGrade.class);

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
        if(user.getSid() > 0) {
            for(Iterator<Permission> it = permissions.iterator(); it.hasNext();) {
                Permission permission = it.next();
                if(permission.getPid() > 0 && permission.getSubordinate() == Permission.subordinate_off) it.remove();
            }
        }
        Collections.sort(permissions);
        return permissions;
    }

    private List<Long> getUserPermissionIds(long uid) {
        Set<Long> res = Sets.newHashSet();
        User user = userDao.load(uid);
        for(long rid: user.getRids()){
            List<RolePermissionPool> rolePermissionPoolPools =rolePermissionPoolDao.getAllList(rid);
            for(RolePermissionPool one : rolePermissionPoolPools) {
                res.add(one.getPid());
            }
        }
        List<UserPermissionPool> userPermissionPools = userPermissionPoolDao.getAllList(uid);
        for(UserPermissionPool one : userPermissionPools) {
            if(one.getType()==UserPermissionPool.type_on) {
                res.add(one.getPid());
            }else{
                res.remove(one.getPid());
            }
        }
        return Lists.newArrayList(res);
    }



    @Override
    public Pager searchUser(Pager pager,long sid, long subid,int userType,String name,String phone,String userName,long rid,int status,long startTime,long endTime) throws Exception {
        final String selectCount = "select count(*) from `user` where 1=1  ";
        final String selectData = "select * from `user` where 1=1  ";
        final String limit = "  limit %d , %d ";
        String sql = selectData;
        String countSql = selectCount;
        if(sid>-1){
            sql = sql + " and `sid` = " + sid;
            countSql = countSql + " and `sid` = " + sid;
        }
        if(status>-1){
            sql = sql + " and `status` = " + status;
            countSql = countSql + " and `status` = " + status;
        }
        if(userType>-1){
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
        if(rid>0){
            sql = sql + " and (`ridsJson` like '[" + rid+"]'  or `ridsJson` like '%," + rid+",%'  or `ridsJson` like '[" + rid+",%'   or `ridsJson` like '%," + rid+"]')";
            countSql = countSql + " and (`ridsJson` like '[" + rid+"]'  or `ridsJson` like '%," + rid+",%'  or `ridsJson` like '[" + rid+",%'   or `ridsJson` like '%," + rid+"]')";
        }

        if(startTime>0){
            sql = sql + " and `ctime` > " + startTime;
            countSql = countSql + " and `ctime` > " + startTime;
        }
        if(endTime>0){
            sql = sql + " and `ctime` < " + endTime;
            countSql = countSql + " and `ctime` < " + endTime;
        }
        if(subid>-1){
            sql = sql + " and `psid` = " + subid;
            countSql = countSql + " and `psid` = " + subid;
        }
        sql = sql + String.format(limit,pager.getSize()*(pager.getPage()-1),pager.getSize());
        List<User> users =null;
        int count=0;
        List<Object> objects=new ArrayList<>();
        if(StringUtils.isNotBlank(name)){objects.add("%"+name+"%");}
        if(StringUtils.isNotBlank(phone)){objects.add("%"+phone+"%");}
        if(StringUtils.isNotBlank(userName)){objects.add("%"+userName+"%");}
        if(objects.size()>0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            users = jdbcTemplate.query(sql, new RowMapperHelp(User.class),args);
            count = this.jdbcTemplate.queryForObject(countSql,args, Integer.class);
        }else{
            users = jdbcTemplate.query(sql, new RowMapperHelp(User.class));
            count = this.jdbcTemplate.queryForObject(countSql,Integer.class);
        }
        pager.setData(transformClient(users));
        pager.setTotalCount(count);
        return pager;

    }


    @Override
    public ClientUserOnLogin register(User user) throws Exception {
        boolean thirdSign = false;
        if(StringUtils.isNotBlank(user.getWeiboId()) || StringUtils.isNotBlank(user.getWeixinId()) || StringUtils.isNotBlank(user.getQqId())
                || StringUtils.isNotBlank(user.getAlipayId())) {
            thirdSign = true;
        }
        if(thirdSign) {
            if(StringUtils.isBlank(user.getPhone())) throw new StoreSystemException("手机号不能为空");
            LoginUserPool loginUserPool = new LoginUserPool();
            loginUserPool.setUserType(user.getUserType());
            if(StringUtils.isNotBlank(user.getWeiboId())) {
                loginUserPool.setAccount(user.getWeiboId());
                loginUserPool.setLoginType(LoginUserPool.loginType_weibo);
            }
            if(StringUtils.isNotBlank(user.getWeixinId())) {
                loginUserPool.setAccount(user.getWeixinId());
                loginUserPool.setLoginType(LoginUserPool.loginType_weixin);
            }
            if(StringUtils.isNotBlank(user.getQqId())) {
                loginUserPool.setAccount(user.getQqId());
                loginUserPool.setLoginType(LoginUserPool.loginType_qq);
            }
            if(StringUtils.isNotBlank(user.getAlipayId())) {
                loginUserPool.setAccount(user.getAlipayId());
                loginUserPool.setLoginType(LoginUserPool.loginType_alipay);
            }
            LoginUserPool dbLoginUserPool=loginUserPoolDao.load(loginUserPool);
            if(dbLoginUserPool != null) throw new StoreSystemException("该账号已经存在");

            LoginUserPool phonePool = new LoginUserPool();
            phonePool.setUserType(user.getUserType());
            phonePool.setLoginType(LoginUserPool.loginType_phone);
            phonePool.setAccount(user.getPhone());
            phonePool = loginUserPoolDao.load(phonePool);
            if(null != phonePool) {
                long uid = phonePool.getUid();
                User dbUser = userDao.load(uid);
                if(null == dbUser) throw new StoreSystemException("用户错误");
                if(StringUtils.isNotBlank(user.getWeiboId())) {
                    if(StringUtils.isNotBlank(dbUser.getWeiboId())) throw new StoreSystemException("手机号已绑定微博");
                    dbUser.setWeiboId(user.getWeiboId());
                }
                if(StringUtils.isNotBlank(user.getWeixinId())) {
                    if(StringUtils.isNotBlank(dbUser.getWeixinId())) throw new StoreSystemException("手机号已绑定微信");
                    dbUser.setWeixinId(user.getWeixinId());
                }
                if(StringUtils.isNotBlank(user.getQqId())) {
                    if(StringUtils.isNotBlank(dbUser.getQqId())) throw new StoreSystemException("手机号已绑定QQ");
                    dbUser.setQqId(user.getQqId());
                }
                if(StringUtils.isNotBlank(user.getAlipayId())) {
                    if(StringUtils.isNotBlank(dbUser.getAlipayId())) throw new StoreSystemException("手机号已绑定支付宝");
                    dbUser.setAlipayId(user.getAlipayId());
                }
                userDao.update(dbUser);
                loginUserPool.setUid(dbUser.getId());
                loginUserPoolDao.insert(loginUserPool);
                user = dbUser;
            } else {
                Random rand = new Random();
                user.setRand(rand.nextInt(100000000));
                if(StringUtils.isNotBlank(user.getWeiboId())) {
                    user.setWeiboId(user.getWeiboId());
                }
                if(StringUtils.isNotBlank(user.getWeixinId())) {
                    user.setWeixinId(user.getWeixinId());
                }
                if(StringUtils.isNotBlank(user.getQqId())) {
                    user.setQqId(user.getQqId());
                }
                if(StringUtils.isNotBlank(user.getAlipayId())) {
                    user.setAlipayId(user.getAlipayId());
                }
                user.setPassword(MD5Utils.md5ReStr(user.getPassword().getBytes()));
                if(user.getSid()>0){
                    Subordinate subordinate = subordinateDao.load(user.getSid());
                    long psid = subordinate.getPid();
                    if(psid == 0) psid = subordinate.getId();
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
                if(user.getSid()>0){
                    SubordinateUserPool subordinateUserPool=new SubordinateUserPool();
                    subordinateUserPool.setUid(user.getId());
                    subordinateUserPool.setSid(user.getSid());
                    subordinateUserPool.setStatus(user.getStatus());
                    subordinateUserPoolDao.insert(subordinateUserPool);
                }
            }
        } else {
            if(StringUtils.isBlank(user.getPassword())) throw new StoreSystemException("密码不能为空");
            LoginUserPool loginUserPool = new LoginUserPool();
            loginUserPool.setUserType(user.getUserType());
            if(StringUtils.isNotBlank(user.getPhone())) {
                loginUserPool.setAccount(user.getPhone());
                loginUserPool.setLoginType(LoginUserPool.loginType_phone);
            }
            if(StringUtils.isNotBlank(user.getUserName())) {
                loginUserPool.setAccount(user.getUserName());
                loginUserPool.setLoginType(LoginUserPool.loginType_userName);
            }
            LoginUserPool dbLoginUserPool=loginUserPoolDao.load(loginUserPool);
            if(dbLoginUserPool != null) throw new StoreSystemException("该账号已经存在");
            Random rand = new Random();
            String name = user.getName();
            if(StringUtils.isBlank(name)) name = ""+rand.nextInt(100000000);
            user.setName(name);
            user.setRand(rand.nextInt(100000000));
            user.setPassword(MD5Utils.md5ReStr(user.getPassword().getBytes()));
            if(user.getSid()>0){
                Subordinate subordinate = subordinateDao.load(user.getSid());
                long psid = subordinate.getPid();
                if(psid == 0) psid = subordinate.getId();
                user.setPsid(psid);
            }
            user = userDao.insert(user);
            loginUserPool.setUid(user.getId());
            loginUserPoolDao.insert(loginUserPool);
            if(user.getSid()>0){
                SubordinateUserPool subordinateUserPool=new SubordinateUserPool();
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
        if(StringUtils.isNotBlank(user.getPhone())) {
            loginUserPool.setAccount(user.getPhone());
            loginUserPool.setLoginType(LoginUserPool.loginType_phone);
        } else if(StringUtils.isNotBlank(user.getUserName())) {
            loginUserPool.setAccount(user.getUserName());
            loginUserPool.setLoginType(LoginUserPool.loginType_userName);
        } else if(StringUtils.isNotBlank(user.getWeiboId())) {
            loginUserPool.setAccount(user.getWeiboId());
            loginUserPool.setLoginType(LoginUserPool.loginType_weibo);
        } else if(StringUtils.isNotBlank(user.getWeixinId())) {
            loginUserPool.setAccount(user.getWeixinId());
            loginUserPool.setLoginType(LoginUserPool.loginType_weixin);
        } else if(StringUtils.isNotBlank(user.getQqId())) {
            loginUserPool.setAccount(user.getQqId());
            loginUserPool.setLoginType(LoginUserPool.loginType_qq);
        } else if(StringUtils.isNotBlank(user.getAlipayId())) {
            loginUserPool.setAccount(user.getAlipayId());
            loginUserPool.setLoginType(LoginUserPool.loginType_alipay);
        }else{
            throw new StoreSystemException("用户不存在");
        }
        loginUserPool=loginUserPoolDao.load(loginUserPool);
        if(loginUserPool==null){
            throw new StoreSystemException("用户不存在");
        }

        User dbUser=userDao.load(loginUserPool.getUid());
        if(null == dbUser||dbUser.getStatus()==User.status_delete){
            throw new StoreSystemException("用户不存在");
        }
        if(StringUtils.isBlank(user.getQqId())&&StringUtils.isBlank(user.getWeixinId())&&StringUtils.isBlank(user.getWeiboId())){
            if(StringUtils.isBlank(code)){
                if(StringUtils.isNotBlank(dbUser.getPassword())&&
                        !MD5Utils.md5ReStr(user.getPassword().getBytes()).equalsIgnoreCase(dbUser.getPassword())) {
                    throw new StoreSystemException("密码不正确");
                }

            }
        }
        ClientUserOnLogin clientUserOnLogin = new ClientUserOnLogin(dbUser);
        Subordinate subordinate = subordinateService.load(clientUserOnLogin.getSid());
        Subordinate pSubordinate = subordinateService.load(clientUserOnLogin.getPsid());
        if(subordinate!=null){
            if(subordinate.getName()!=null){
                clientUserOnLogin.setSName(subordinate.getName());//公司名称
            }
        }
        if(pSubordinate!=null){
            if(pSubordinate.getName()!=null){
                clientUserOnLogin.setSubName(pSubordinate.getName());//门店名称
            }
        }
        return clientUserOnLogin;
    }

    @Override
    public User load(long id)throws Exception {
        return userDao.load(id);
    }

    @Override
    public long loadByAccount(int loginType, String account,int userType){
        LoginUserPool loginUserPool=new LoginUserPool();
        loginUserPool.setAccount(account);
        loginUserPool.setLoginType(loginType);
        loginUserPool.setUserType(userType);
        loginUserPool= loginUserPoolDao.load(loginUserPool);
        if(loginUserPool==null){return 0;}
        return loginUserPool.getUid();
    }

    @Override
    public ClientUser loadWithClient(long id)throws Exception {
        return transformClient(userDao.load(id));
    }



    @Override
    public List<User> load(List<Long> ids) throws Exception {
        return userDao.load(ids);
    }

    @Override
    public boolean update(User user, List<Long> updateRids) throws Exception {
        User currentUser = userDao.load(user.getId());
        if(null == currentUser) throw new StoreSystemException("用户不存在");
        LoginUserPool oldLoginUserPool=null;//旧的
        LoginUserPool loginUserPool=null;//新的
        if(StringUtils.isNotBlank(user.getUserName())
                &&!user.getUserName().equals(currentUser.getUserName())){
            loginUserPool=new LoginUserPool();
            loginUserPool.setAccount(user.getUserName());
            loginUserPool.setLoginType(LoginUserPool.loginType_userName);
            loginUserPool.setUserType(user.getUserType());
            loginUserPool.setUid(user.getId());
            LoginUserPool dbLoginUserPool=loginUserPoolDao.load(loginUserPool);
            if(dbLoginUserPool!=null) {
                throw new StoreSystemException("账号已存在");
            }
            oldLoginUserPool=new LoginUserPool();
            oldLoginUserPool.setAccount(currentUser.getUserName());
            oldLoginUserPool.setLoginType(LoginUserPool.loginType_userName);
            oldLoginUserPool.setUserType(currentUser.getUserType());
            oldLoginUserPool.setUid(currentUser.getId());

            currentUser.setUserName(user.getUserName());
        }

        if(StringUtils.isNotBlank(user.getMail())
                &&!user.getMail().equals(currentUser.getMail())) {
            currentUser.setMail(user.getMail());
        }
        if(StringUtils.isNotBlank(user.getName())
                &&!user.getName().equals(currentUser.getName())) {
            currentUser.setName(user.getName());
        }
        if(StringUtils.isNotBlank(user.getPhone())
                &&!user.getPhone().equals(currentUser.getPhone())) {
            loginUserPool=new LoginUserPool();
            loginUserPool.setAccount(user.getPhone());
            loginUserPool.setLoginType(LoginUserPool.loginType_phone);
            loginUserPool.setUserType(user.getUserType());
            loginUserPool.setUid(user.getId());
            LoginUserPool dbLoginUserPool=loginUserPoolDao.load(loginUserPool);
            if(dbLoginUserPool!=null) {
                throw new StoreSystemException("账号已存在");
            }

            oldLoginUserPool=new LoginUserPool();
            oldLoginUserPool.setAccount(currentUser.getPhone());
            oldLoginUserPool.setLoginType(LoginUserPool.loginType_phone);
            oldLoginUserPool.setUserType(currentUser.getUserType());
            oldLoginUserPool.setUid(currentUser.getId());

            currentUser.setPhone(user.getPhone());
        }
        if(StringUtils.isNotBlank(user.getDesc())
                &&!user.getDesc().equals(currentUser.getDesc())) {
            currentUser.setDesc(user.getDesc());
        }
        if(updateRids!=null) {
            currentUser.setRids(updateRids);
        }
        if(StringUtils.isNotBlank(user.getPassword())
                &&!MD5Utils.md5ReStr(user.getPassword().getBytes()).equals(
                MD5Utils.md5ReStr(currentUser.getPassword().getBytes()))) {
            currentUser.setPassword(MD5Utils.md5ReStr(user.getPassword().getBytes()));
            Random rand = new Random();
            currentUser.setRand(rand.nextInt(100000000));
        }

        boolean sign = userDao.update(currentUser);
        if(oldLoginUserPool!=null){
            loginUserPoolDao.delete(oldLoginUserPool);
            loginUserPoolDao.insert(loginUserPool);
        }
        return sign;
    }


    @Override
    public boolean updateStatus(long id,int status) throws Exception {
        User user = userDao.load(id);
        if(null == user) throw new StoreSystemException("用户不存在");
        user.setStatus(status);
        boolean sign = userDao.update(user);
        LoginUserPool loginUserPool = new LoginUserPool();
        loginUserPool.setUserType(User.userType_user);
        loginUserPool.setLoginType(LoginUserPool.loginType_phone);
        loginUserPool.setAccount(user.getPhone());
        if(user.getStatus()==User.status_delete){
            loginUserPoolDao.delete(loginUserPool);
        }else if(user.getStatus()==User.status_nomore){
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

        for (long pid : newPids){
            if(!nowPids.contains(pid)){
                addPids.add(pid);
            }
        }

        for(long pid : nowPids) {
            if (!newPids.contains(pid)) {
                deletePids.add(pid);
            }
        }

        //增加
        for(long pid : addPids){
            UserPermissionPool pool = new UserPermissionPool();
            pool.setUid(uid);
            pool.setPid(pid);
            pool.setType(UserPermissionPool.type_on);
            pool = userPermissionPoolDao.load(pool);
            if(pool == null) {
                pool = new UserPermissionPool();
                pool.setUid(uid);
                pool.setPid(pid);
                pool.setType(UserPermissionPool.type_on);
                userPermissionPoolDao.insert(pool);
            }else{
                pool.setType(UserPermissionPool.type_on);
                userPermissionPoolDao.update(pool);
            }
        }

        //减少
        for(long pid : deletePids){
            UserPermissionPool pool = new UserPermissionPool();
            pool.setUid(uid);
            pool.setPid(pid);
            pool.setType(UserPermissionPool.type_off);
            pool = userPermissionPoolDao.load(pool);
            if(pool == null) {
                pool = new UserPermissionPool();
                pool.setUid(uid);
                pool.setPid(pid);
                pool.setType(UserPermissionPool.type_off);
                userPermissionPoolDao.insert(pool);
            }else{
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
        for(Permission permission : permissions) {
            if(null != permission) {
                treePids.add(permission.getId());
                if(permission.getPid() > 0) parentPids.add(permission.getPid());
            }
        }
        if(parentPids.size() > 0) loopTreePids(treePids, parentPids);
    }

    @Override
    public boolean createAuthCode(String phone, String template)throws Exception{
        String sent = cache.getString("exists_auth_common_user_" + (phone));
        if (StringUtils.isNotBlank(sent)) {
            throw new StoreSystemException("验证码已经发送");
        }
        Random random = new Random();
        int num = random.nextInt(9999);
        String code = String.format("%04d", num);
        boolean sign = SmsUtils.sendSms(phone, template, code);
        if (sign) {
            cache.setString("exists_auth_common_user_"+(phone), 58, "true");
            cache.setString("auth_common_user_"+(phone), 5 * 60, code);
        } else {
            if (StringUtils.isNotBlank(sent)) {
                throw new StoreSystemException("当前手机号已被注册");
            }
        }
        return true;
    }

    @Override
    public String getAuthCode(String phone) throws Exception {
        String res = cache.getString("auth_common_user_"+(phone));
        return res;
    }



    public void addScore(long id,int num) throws Exception {
        userDao.increment(userDao.load(id), "score", num);
    }


    public List<ClientUser> transformClient(List<User> users) throws Exception{
        Set<Long> sids = Sets.newHashSet();
        for(User user : users) {
            if(user.getPsid() > 0) sids.add(user.getPsid());
            if(user.getSid() > 0) sids.add(user.getSid());
        }
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(sids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        List<ClientUser> res = Lists.newArrayList();
        for(User user : users) {
            ClientUser clientUser = transformClient(user);
            res.add(clientUser);
        }
        return res;
    }

    private ClientUser transformClient(User user) throws Exception{
        ClientUser clientUser = new ClientUser(user);
        Set<Long> sids = Sets.newHashSet();
        if(user.getPsid() > 0) sids.add(user.getPsid());
        if(user.getSid() > 0) sids.add(user.getSid());
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(sids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        if(clientUser.getPsid() > 0) {
            Subordinate subordinate = subordinateMap.get(clientUser.getPsid());
            if(subordinate != null) clientUser.setPSubName(subordinate.getName());
        }
        if(clientUser.getSid() > 0) {
            Subordinate subordinate = subordinateMap.get(clientUser.getSid());
            if(subordinate != null) clientUser.setSubName(subordinate.getName());
        }
        //会员等级
        if(user!=null&&user.getUserGradeId()>0){
            UserGrade userGrade = userGradeDao.load(user.getUserGradeId());
            if(userGrade!=null){
                clientUser.setMember(userGrade.getTitle());
            }
        }
        //消费次数
        return clientUser;
    }

    public List<User> getAllUserBySid(long sid)throws Exception{
        List<SubordinateUserPool> subordinateUserPools=subordinateUserPoolDao.getAllList(sid,User.status_nomore);
        List<Long> uids=new ArrayList<>();
        for (SubordinateUserPool subordinateUserPool:subordinateUserPools){
            uids.add(subordinateUserPool.getUid());
        }
        return userDao.load(uids);
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        //查找用户
        User olduser=userDao.load(user.getId());
        //姓名
        if(user.getName()!=null && StringUtils.isNotBlank(user.getName())){ olduser.setName(user.getName()); }
        //性别
        if(user.getSex() != 0){ olduser.setSex(user.getSex()); }
        //年龄
        if(user.getAge() != 0){ olduser.setAge(user.getAge()); }
        //入职时间
        if(user.getWorkingDate() !=0){ olduser.setWorkingDate(user.getWorkingDate());}
        //封面图
        if(user.getCover()!=null && StringUtils.isNotBlank(user.getCover())){ olduser.setCover(user.getCover()); }
        //生日
        if(user.getBirthdate() != 0){ olduser.setBirthdate(user.getBirthdate()); }
        //职位
        if(user.getJob()!=null&&StringUtils.isNotBlank(user.getJob())){ olduser.setJob(user.getJob()); }
        //邮件
        if(user.getMail()!=null&&StringUtils.isNotBlank(user.getMail())){ olduser.setMail(user.getMail()); }
        //手机号
        if(user.getPhone()!=null&&StringUtils.isNotBlank(user.getPhone())){ olduser.setPhone(user.getPhone()); }
        boolean res = userDao.update(olduser);
        if(res && !olduser.getPhone().equals(user.getPhone())) {
            LoginUserPool loginUserPool = new LoginUserPool();
            loginUserPool.setUserType(User.userType_backendUser);//员工
            loginUserPool.setLoginType(LoginUserPool.loginType_phone);
            loginUserPool.setAccount(olduser.getPhone());
            loginUserPoolDao.delete(loginUserPool);
            loginUserPool.setUid(olduser.getId());
            loginUserPoolDao.insert(loginUserPool);
        }
        boolean result = userDao.update(olduser);
        return result;
    }

    private void check(User user) throws StoreSystemException {
        if(user.getUserType() != User.userType_user) throw new StoreSystemException("用户类型错误");
        String name = user.getName();
        if(StringUtils.isBlank(name)) throw new StoreSystemException("姓名不能为空");
        String phone = user.getPhone();
        if(StringUtils.isBlank(phone)) throw new StoreSystemException("手机号不能为空");
        if(user.getSid() == 0) throw new StoreSystemException("店铺ID错误");

    }

    @Override
    public User addCustomer(User user) throws Exception {
        check(user);
        long subid = user.getSid();
        Subordinate subordinate = subordinateDao.load(subid);
        long pSubid = subordinate.getPid();
        if(subordinate.getPid() == 0) pSubid = subid;
        user.setPsid(pSubid);
        user = userDao.insert(user);
        LoginUserPool loginUserPool = new LoginUserPool();
        loginUserPool.setUserType(User.userType_user);
        loginUserPool.setLoginType(LoginUserPool.loginType_phone);
        loginUserPool.setAccount(user.getPhone());
        loginUserPool.setUid(user.getId());
        loginUserPoolDao.insert(loginUserPool);
        return user;
    }

    @Override
    public boolean updateCustomer(User user) throws Exception {
        check(user);
        User dbUser = userDao.load(user.getId());
        long subid = user.getSid();
        Subordinate subordinate = subordinateDao.load(subid);
        long pSubid = subordinate.getPid();
        if(subordinate.getPid() == 0) pSubid = subid;
        user.setPsid(pSubid);
        boolean res = userDao.update(user);
        if(res && !dbUser.getPhone().equals(user.getPhone())) {
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
        if(user != null) {
            if(user.getUserType() != User.userType_user) throw new StoreSystemException("用户类型错误");
            user.setStatus(User.status_delete);
            boolean res = userDao.update(user);
            if(res) {
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
        if(userType>-1){
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
    public Pager getBackSubCustomerPager(Pager pager, long subid, String phone, String phone1, String name, String name1, int sex, int userType, String job, long userGradeId) throws Exception {
        String sql = "SELECT * FROM `user` where sid = " + subid + " and `status` = " + User.status_nomore;
        String sqlCount = "SELECT COUNT(id) FROM `user` where sid = " + subid + " and `status` = " + User.status_nomore;
        String limit = " limit %d , %d ";
        List<Object> objects=new ArrayList<>();

        if(userType>-1){
            sql = sql + " AND userType =" + userType;
            sqlCount = sqlCount + " AND userType =" + userType;
        }
        if(StringUtils.isNotBlank(job)){
            sql = sql + " and `job` =" + job;
            sqlCount = sqlCount + " and `job` =" + job;
        }

        if(StringUtils.isNotBlank(name)&&StringUtils.isBlank(name1)){//精确查询
            sql = sql + " AND `name` = '" + name + "'";
            sqlCount = sqlCount + " AND `name` = '" + name + "'";
        }
        if (StringUtils.isNotBlank(name1)&&StringUtils.isBlank(name)) {//模糊查询
            sql = sql + " and `name` like ?";
            sqlCount = sqlCount + " and `name` like ?";
            objects.add("%"+name1+"%");
        }
        if (StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(name1)) {
            if(name.equals(name1)){//相等精确查询
                sql = sql + " AND `name` = '" + name + "'";
                sqlCount = sqlCount + " and `name` = '" + name + "'";
            }else if(name.indexOf(name1)>=0){//如果 姓名.indexOf(姓/名) 按照姓/名模糊查询
                sql = sql + " and `name` like ?";
                sqlCount = sqlCount + " and `name` like ?";
                objects.add("%"+name1+"%");
            }else{//输入的两个值不能指向同一个用户
                return pager;
            }
        }
        if (StringUtils.isNotBlank(phone)&&StringUtils.isBlank(phone1)) {
            sql = sql + " and `phone` = '" + phone + "'";
            sqlCount = sqlCount + " and `phone` = '" + phone + "'";
        }
        if (StringUtils.isNotBlank(phone1)&&StringUtils.isBlank(phone)) {
            sql = sql + " and `phone` like ?";
            sqlCount = sqlCount + " and `phone` like ?";
            objects.add("%"+phone1+"%");
        }
        if(StringUtils.isNotBlank(phone)&&StringUtils.isNotBlank(phone1)){
            if(phone.equals(phone1)){
                sql = sql + " and `phone` = '" + phone + "'";
                sqlCount = sqlCount + " and `phone` = '"+ phone + "'";
            }else if(phone.indexOf(phone1)>=0){
                sql = sql + " and `phone` like ?";
                sqlCount = sqlCount + " and `phone` like ?";
                objects.add("%"+phone1+"%");
            }else{//输入的两个值不能指向同一个用户
                return pager;
            }
        }
        if (sex>-1){
            sql = sql + " AND sex =" + sex;
            sqlCount = sqlCount + " AND sex =" + sex;
        }
        if (userGradeId>-0){
            sql = sql + " AND userGradeId =" + sex;
            sqlCount = sqlCount + " AND userGradeId =" + sex;
        }
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<User> users = null;
        int count = 0;
        if(objects.size()>0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            users = this.jdbcTemplate.query(sql, rowMapper,args);
            count = this.jdbcTemplate.queryForObject(sqlCount, args,Integer.class);
        }else{
            users = this.jdbcTemplate.query(sql, rowMapper);
            count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        }
        List<ClientUser> data = transformClient(users);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Set<String> getAllUserJob(long subid,int userType) throws Exception {
        TransformFieldSetUtils fieldSetUtils = new TransformFieldSetUtils(User.class);
        List<User> users = userDao.getAllLists(subid,userType,User.status_nomore);
        return fieldSetUtils.fieldList(users,"job");
    }

    @Override
    public List<ClientUser> getAllUser(long sid, int userType) throws Exception {
        String sql = " SELECT * FROM `user` WHERE status = "+ User.status_nomore + " AND `userType` = " + userType + " and sid = " + sid;
        List<User> users = jdbcTemplate.query(sql,rowMapper);
        return transformClient(users);
    }

    @Override
    public ClientUser getUser(String phone) throws Exception {
        //根据手机号查询user
        List<User> users = userDao.getUsers(phone);
        if (users!=null){
            User user = users.get(0);
            if(user.getCardNumber()==0){
                //生成会员卡号
                user.setCardNumber(new Random().nextInt(1000000));
                //给一个最低等级的会员
                String sql = " select * from user_grade where 1=1 and subid = " + user.getSid() + " order by conditionScore asc";
                List<UserGrade> userGrades = jdbcTemplate.query(sql,ugMapper);
                if(userGrades.size()>0){
                    user.setUserGradeId(userGrades.get(0).getId());
                }else{
                    throw new StoreSystemException("请先在本公司下设置会员等级!");
                }
                userDao.update(user);
                return transformClient(user);
            }else{
                return transformClient(user);
            }
        }
        return null;
    }

    @Override
    public List<ExportUser> getExportUserInfo(long sid, String phone, int sex, String job) throws Exception {
        String sql = " SELECT * FROM `user` WHERE status = "+ User.status_nomore + " AND `userType` = " + User.userType_user;
        if(sid>0){
            sql = sql + " AND sid = " + sid;
        }
        if(StringUtils.isNotBlank(phone)){
            sql = sql + " AND `phone` LIKE ?";
        }
        if(sex>-1){
            sql = sql + " AND sex = " + sex;
        }
        if(StringUtils.isNotBlank(job)){
            sql = sql + " AND `job` LIKE ?";
        }
        sql = sql + " ORDER  BY `ctime` DESC ";
        List<Object> objects=new ArrayList<>();
        List<User> users = null;
        int count = 0;
        if(StringUtils.isNotBlank(job)){objects.add("%"+job+"%");}
        if(StringUtils.isNotBlank(phone)){objects.add("%"+phone+"%");}
        if(objects.size()>0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            users = this.jdbcTemplate.query(sql, rowMapper,args);
        }else{
            users = this.jdbcTemplate.query(sql, rowMapper);
        }

        if(users.size()==0||users==null){ throw new StoreSystemException("暂未查询到可导出的信息!"); }
        List<ExportUser> exportUsers = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int index = 1;
        for (User user:users){
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
            if(user.getSex()==0){
                exportUser.setSex("未知");
            }else if(user.getSex()==1){
                exportUser.setSex("男");
            }else {
                exportUser.setSex("女");
            }
            exportUser.setIdCard(user.getIdCard());
            exportUser.setNumber(String.valueOf(index++));
            exportUsers.add(exportUser);
        }
        return exportUsers;
    }
}
