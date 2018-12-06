package com.store.system.service;


import com.store.system.client.ClientUser;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.model.Permission;
import com.store.system.model.User;
import com.s7.baseFramework.model.pagination.Pager;

import java.util.List;


public interface UserService {

    /**
     * 获取用户所有权限
     * @param uid
     * @return
     * @throws Exception
     */
    public List<Permission> getUserPermissions(long uid) throws Exception;

    /**
     * 后台 搜索用户
     * @param pager 分页数据
     * @param sid 下级单位ID
     * @param userType 用户类型，前端用户，后台用户？
     * @param name
     * @param phone
     * @param userName
     * @param rid
     * @param status
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public Pager searchUser(Pager pager, long sid, int userType, String name, String phone, String userName, long rid, int status, long startTime, long endTime) throws Exception;


    /**
     * 注册
     * @param user
     * @return
     * @throws Exception
     */
    public ClientUserOnLogin register(User user) throws Exception;

    /**
     * 登录(支持手机号登录，支持账号密码登录，支持第三方账号登录)
     * @param user
     * @return
     * @throws Exception
     */
    public ClientUserOnLogin login(User user) throws Exception;


    public User load(long uid) throws Exception;

    public long loadByAccount(int loginType, String account, int userType);


    public ClientUser loadWithClient(long uid) throws Exception;


    public List<User> load(List<Long> uids) throws Exception;

    public List<ClientUser> loadWithClient(List<Long> uids) throws Exception;




    public boolean update(User user, List<Long> updateRids) throws Exception;

    public boolean updateStatus(long id, int status) throws Exception;

    public boolean updateUserPermission(long uid, List<Long> pids)throws Exception;



    public boolean createAuthCode(String phone, String template)throws Exception;

    public String getAuthCode(String phone) throws Exception ;

    public void addScore(long id, int num) throws Exception;


    public String getAlipayUid(long uid) throws Exception ;
    public boolean updateAlipayUid(long uid, String alipayUid) throws Exception;
    public String getWxpayOpenId(long uid) throws Exception ;
    public boolean updateWxpayOpenId(long uid, String wxpayOpenId) throws Exception;


    /**
     * 获取下属单位的所有用户
     * @param sid
     * @return
     * @throws Exception
     */
    public List<User> getAllUserBySid(long sid)throws Exception;

    public boolean addfavourNum(long uid, String filed, int num) throws Exception;

    /**
     * 修改基本信息
     * @param user
     * @return
     */
    public boolean updateUser(User user) throws Exception;


}
