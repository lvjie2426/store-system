package com.store.system.service;


import com.store.system.client.ClientUser;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.model.ExportUser;
import com.store.system.model.Permission;
import com.store.system.model.User;
import com.quakoo.baseFramework.model.pagination.Pager;

import java.util.List;
import java.util.Map;
import java.util.Set;


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
    public Pager searchUser(Pager pager, long sid, long subid, int userType, String name,
                            String phone, String userName, long rid, int status, long startTime, long endTime) throws Exception;


    public Pager searchBackendUser(Pager pager, long sid, long psid, int userType, String name,
                            String phone, String userName, long rid, int status, long startTime, long endTime) throws Exception;

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
    public ClientUserOnLogin login(User user, String code) throws Exception;


    public User load(long uid) throws Exception;

    public long loadByAccount(int loginType, String account, int userType);


    public ClientUser loadWithClient(long uid) throws Exception;


    public List<User> load(List<Long> uids) throws Exception;

    public boolean update(User user, List<Long> updateRids) throws Exception;

    public boolean updateStatus(long id, int status) throws Exception;

    public boolean updateUserPermission(long uid, List<Long> pids)throws Exception;



    public boolean createAuthCode(String phone, String template)throws Exception;

    public String getAuthCode(String phone) throws Exception ;

    public void addScore(long id, int num) throws Exception;

    /**
     * 获取下属单位的所有用户
     * @param sid
     * @return
     * @throws Exception
     */
    public List<User> getAllUserBySid(long sid)throws Exception;

    /**
     * 修改基本信息
     * @param user
     * @return
     */
    public boolean updateUser(User user) throws Exception;



    ///////////////////////////顾客相关//////////////////////////

    public User addCustomer(User user) throws Exception;

    public boolean updateCustomer(User user) throws Exception;

    public boolean delCustomer(long id) throws Exception;

    public Pager getBackCustomerPager(Pager pager, long pSubid, int userType) throws Exception; //获取公司下的顾客

    public Pager getBackSubCustomerPager(Pager pager, long subid, String phone, String phone1, String name, String name1, int sex,int userType, String job,long userGradeId) throws Exception; //获取分店下的顾客

    public Set<String> getAllUserJob(long sid,int userType)throws Exception;

    public List<ClientUser> getAllUser(long sid,int userType)throws Exception;

    public ClientUser getUser(String phone)throws Exception;

    //会员信息认证
    public ClientUser checkUserGradeInfo(User user)throws Exception;

    //根据职业获取列表
    public List<ClientUser> getUserByJob(String job,long sid, int userType)throws Exception;

    ///////////////////////////导出顾客信息//////////////////////////
    public List<ExportUser> getExportUserInfo(long sid, String phone, int sex, String job)throws Exception;

    public Map<String,Object> taskReward(String date, long sid)throws Exception;
}
