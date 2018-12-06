package com.store.system.service.impl;

import com.store.system.client.ClientUser;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.dao.*;
import com.store.system.exception.GlassesException;
import com.store.system.model.*;
import com.store.system.service.UserService;
import com.store.system.util.SmsUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.baseFramework.redis.JedisX;
import com.s7.baseFramework.secure.MD5Utils;
import com.s7.ext.RowMapperHelp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

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
    protected JdbcTemplate jdbcTemplate;

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
    public Pager searchUser(Pager pager,long sid,int userType,String name,String phone,String userName,long rid,int status,long startTime,long endTime) throws Exception {
        final String selectCount = "select count(*) from `user` where 1=1  ";
        final String selectData = "select * from `user` where 1=1  ";
        final String limit = "  limit %d , %d ";
        String sql = selectData;
        String countSql = selectCount;
        {
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
            count = this.jdbcTemplate.queryForInt(countSql,args);
        }else{
            users = jdbcTemplate.query(sql, new RowMapperHelp(User.class));
            count = this.jdbcTemplate.queryForInt(countSql);
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
            if(StringUtils.isBlank(user.getPhone())) throw new GlassesException("手机号不能为空");
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
            if(dbLoginUserPool != null) throw new GlassesException("该账号已经存在");

            LoginUserPool phonePool = new LoginUserPool();
            phonePool.setUserType(user.getUserType());
            phonePool.setLoginType(LoginUserPool.loginType_phone);
            phonePool.setAccount(user.getPhone());
            phonePool = loginUserPoolDao.load(phonePool);
            if(null != phonePool) {
                long uid = phonePool.getUid();
                User dbUser = userDao.load(uid);
                if(null == dbUser) throw new GlassesException("用户错误");
                if(StringUtils.isNotBlank(user.getWeiboId())) {
                    if(StringUtils.isNotBlank(dbUser.getWeiboId())) throw new GlassesException("手机号已绑定微博");
                    dbUser.setWeiboId(user.getWeiboId());
                }
                if(StringUtils.isNotBlank(user.getWeixinId())) {
                    if(StringUtils.isNotBlank(dbUser.getWeixinId())) throw new GlassesException("手机号已绑定微信");
                    dbUser.setWeixinId(user.getWeixinId());
                }
                if(StringUtils.isNotBlank(user.getQqId())) {
                    if(StringUtils.isNotBlank(dbUser.getQqId())) throw new GlassesException("手机号已绑定QQ");
                    dbUser.setQqId(user.getQqId());
                }
                if(StringUtils.isNotBlank(user.getAlipayId())) {
                    if(StringUtils.isNotBlank(dbUser.getAlipayId())) throw new GlassesException("手机号已绑定支付宝");
                    dbUser.setAlipayId(user.getAlipayId());
                }
                userDao.update(dbUser);
                loginUserPool.setUid(dbUser.getId());
                loginUserPoolDao.insert(loginUserPool);
                user = dbUser;
            } else {
                user.createJsonString();
                Random rand = new Random();
                user.setRand(rand.nextInt(100000000));
                user = userDao.insert(user);
                phonePool = new LoginUserPool();
                phonePool.setUserType(user.getUserType());
                phonePool.setLoginType(LoginUserPool.loginType_phone);
                phonePool.setAccount(user.getPhone());
                phonePool = loginUserPoolDao.load(phonePool);
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
            if(StringUtils.isBlank(user.getPassword())) throw new GlassesException("密码不能为空");
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
            if(dbLoginUserPool != null) throw new GlassesException("该账号已经存在");
            user.createJsonString();
            Random rand = new Random();
            user.setRand(rand.nextInt(100000000));
            user.setPassword(MD5Utils.md5ReStr(user.getPassword().getBytes()));
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
    public ClientUserOnLogin login(User user) throws Exception {
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
            throw new GlassesException("用户不存在");
        }
        loginUserPool=loginUserPoolDao.load(loginUserPool);
        if(loginUserPool==null){
            throw new GlassesException("用户不存在");
        }

        User dbUser=userDao.load(loginUserPool.getUid());
        if(null == dbUser||dbUser.getStatus()==User.status_delete){
            throw new GlassesException("用户不存在");
        }

        if(StringUtils.isNotBlank(dbUser.getPassword())&&
                !MD5Utils.md5ReStr(user.getPassword().getBytes()).equalsIgnoreCase(dbUser.getPassword()))
            throw new GlassesException("密码不正确");
        ClientUserOnLogin clientUserOnLogin = new ClientUserOnLogin(dbUser);
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
    public List<ClientUser> loadWithClient(List<Long> ids) throws Exception {
        return transformClient(userDao.load(ids));
    }

    @Override
    public boolean update(User user, List<Long> updateRids) throws Exception {
        User currentUser = userDao.load(user.getId());
        if(null == currentUser) throw new GlassesException("用户不存在");
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
                throw new GlassesException("账号已存在");
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
                throw new GlassesException("账号已存在");
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
        currentUser.createJsonString();


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
        if(null == user) throw new GlassesException("用户不存在");
        user.setStatus(status);
        boolean sign = userDao.update(user);
        if(user.getSid()>0){
            SubordinateUserPool subordinateUserPool=new SubordinateUserPool();
            subordinateUserPool.setUid(user.getId());
            subordinateUserPool.setSid(user.getSid());
            subordinateUserPool.setStatus(user.getStatus());
            subordinateUserPoolDao.update(subordinateUserPool);
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
            throw new GlassesException("验证码已经发送");
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
                throw new GlassesException("当前手机号已被注册");
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
        List<ClientUser> result=new ArrayList<>();
        if(users!=null) {
            for (User user : users) {
                ClientUser clientUser = transformClient(user);
                result.add(clientUser);
            }
        }
        return result;
    }

    private ClientUser transformClient(User user) throws Exception{
        ClientUser clientUser = new ClientUser(user);
        return clientUser;
    }


    @Override
    public String getAlipayUid(long uid) throws Exception {
        User user = userDao.load(uid);
        if (null == user) throw new GlassesException("user is null!");
        return user.getAlipayId();
    }

    @Override
    public boolean updateAlipayUid(long uid, String alipayUid) throws Exception {
        User user = userDao.load(uid);
        if (null == user) throw new GlassesException("user is null!");
        user.setAlipayId(alipayUid);
        boolean sign = userDao.update(user);
        return sign;
    }

    @Override
    public String getWxpayOpenId(long uid) throws Exception {
        User user = userDao.load(uid);
        if (null == user) throw new GlassesException("user is null!");
        return user.getWeixinId();
    }

    @Override
    public boolean updateWxpayOpenId(long uid, String wxpayOpenId) throws Exception {
        User user = userDao.load(uid);
        if (null == user) throw new GlassesException("user is null!");
        user.setWeixinId(wxpayOpenId);
        boolean sign = userDao.update(user);
        return sign;
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
    public boolean addfavourNum(long uid,String filed, int num) throws Exception {
        User user = userDao.increment(userDao.load(uid), filed, num);
        if(user!=null){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        //查找用户
        User olduser=userDao.load(user.getId());
        //姓名
        if(user.getName()!=null && !"".equals(user.getName())){
            olduser.setName(user.getName());
        }
        //性别
        if(user.getSex() != 0){
            olduser.setSex(user.getSex());
        }
        //年龄
        if(user.getAge() != 0){
            olduser.setAge(user.getAge());
        }
        //身高
        if (user.getHeight()!=null && !"".equals(user.getHeight())){
            olduser.setHeight(user.getHeight());
        }
        //体重
        if (user.getWeight()!=null && !"".equals(user.getWeight())){
            olduser.setWeight(user.getWeight());
        }
        //头像
        if(user.getIcon()!=null && !"".equals(user.getIcon())){
            olduser.setIcon(user.getIcon());
        }
        //罩杯
        if(user.getBar()!=null && !"".equals(user.getBar())){
            olduser.setBar(user.getBar());
        }
        //腰围
        if(user.getWaistline()!=null && !"".equals(user.getWaistline())){
            olduser.setWaistline(user.getWaistline());
        }
        //臀围
        if(user.getHipline()!=null && !"".equals(user.getHipline())){
            olduser.setHipline(user.getHipline());
        }
        //胸围
        if(user.getChest()!=null && !"".equals(user.getChest())){
            olduser.setChest(user.getChest());
        }
        //腿长
        if(user.getLeg()!=null && !"".equals(user.getLeg())){
            olduser.setLeg(user.getLeg());
        }
        //封面图
        if(user.getCover()!=null && !"".equals(user.getCover())){
            olduser.setCover(user.getCover());
        }
        boolean res = userDao.update(olduser);
        return res;
    }


}
