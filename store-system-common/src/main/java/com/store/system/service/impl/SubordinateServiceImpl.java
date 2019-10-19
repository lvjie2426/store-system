package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ClientUserOnLogin;
import com.store.system.dao.SubordinateDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.*;
import com.store.system.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubordinateServiceImpl implements SubordinateService {

	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private SubordinateDao subordinateDao;
	@Resource
	private UserDao userDao;

	@Resource
	private UserService userService;

	@Resource
	private PermissionService permissionService;

	@Resource
	private RoleService roleService;

	@Resource
	private RoleTemplateItemService roleTemplateItemService;

    private RowMapperHelp<Subordinate> rowMapper = new RowMapperHelp<>(Subordinate.class);


	@Override
	public Subordinate insert(Subordinate subordinate) throws Exception {
        subordinate.setAdminPassword(Subordinate.defaul_pwd);
		subordinate = subordinateDao.insert(subordinate);
		ClientUserOnLogin clientUserOnLogin=null;
		//为每个所生成指定的系统内部账号
		if(StringUtils.isNoneBlank(subordinate.getAdminPhone())||StringUtils.isNoneBlank(subordinate.getAdminUserName())) {
			User user = new User();
			user.setSid(subordinate.getId());
			user.setPhone(subordinate.getAdminPhone());
			user.setUserType(User.userType_backendUser);
			if(StringUtils.isNoneBlank(subordinate.getAdminPassword())) {
				user.setPassword(subordinate.getAdminPassword());
			}
			user.setUserName(subordinate.getAdminUserName() + "+admin");
			user.setName(subordinate.getName());
            clientUserOnLogin = userService.register(user);
			if (null == clientUserOnLogin) {
                throw new StoreSystemException("创建管理员账号错误");
			}
            long uid = clientUserOnLogin.getId();
			List<Permission> permissions = permissionService.getAllList(true);
			if(permissions!=null&&permissions.size()>0) {
				List<Long> pids = new ArrayList<>();
				for (Permission permission : permissions) {
					pids.add(permission.getId());
				}
				userService.updateUserPermission(uid, pids);
			}
			subordinate.setAdminId(uid);
			subordinateDao.update(subordinate);
		}
		if(subordinate.getRoleInitTemplateId()>0){
			List<RoleTemplateItem> roleTemplateItems=roleTemplateItemService.getAll(subordinate.getRoleInitTemplateId());
			for (RoleTemplateItem roleTemplateItem:roleTemplateItems){
				String name=roleTemplateItem.getRoleName();
				String remark=roleTemplateItem.getRemark();
				List<Long> pids=roleTemplateItem.getPids();
				Role role=new Role();
				role.setSid(subordinate.getId());
				role.setRoleName(name);
				role.setRemark(remark);
				role=roleService.add(role);
				if(clientUserOnLogin!=null) {
					userService.update(clientUserOnLogin, Lists.newArrayList(role.getId()));
				}
				roleService.updateRolePermission(role.getId(),pids);

			}
		}
		return subordinate;
	}

	@Override
	public boolean update(Subordinate subordinate) throws Exception {
            Subordinate db=subordinateDao.load(subordinate.getId());
        db.setIcon(subordinate.getIcon());
		db.setName(subordinate.getName());
		db.setProvince(subordinate.getProvince());
		db.setCity(subordinate.getCity());
		db.setArea(subordinate.getArea());
		db.setAddress(subordinate.getAddress());
		db.setContent(subordinate.getContent());
		db.setAdminUserName(subordinate.getAdminUserName());
		db.setAdminPhone(subordinate.getAdminPhone());
		return subordinateDao.update(db);
	}

	@Override
	public boolean updateStore(Subordinate subordinate) throws Exception {
		Subordinate db=subordinateDao.load(subordinate.getId());
		db.setName(subordinate.getName());
		db.setProvince(subordinate.getProvince());
		db.setCity(subordinate.getCity());
		db.setArea(subordinate.getArea());
		db.setPhone(subordinate.getPhone());
		db.setStoreCode(subordinate.getStoreCode());
		db.setStoreImg(subordinate.getStoreImg());
		db.setThreePolicy(subordinate.getThreePolicy());
		db.setStoredType(subordinate.getStoredType());
		db.setPayType(subordinate.getPayType());
		db.setStartTime(subordinate.getStartTime());
		db.setEndTime(subordinate.getEndTime());
		db.setProcessType(subordinate.getProcessType());
		db.setAddress(subordinate.getAddress());
		boolean res = subordinateDao.update(db);
		Subordinate parentSub = subordinateDao.load(subordinate.getPid());
		if(subordinate.getProcessType()==Subordinate.processType_per){
			if(!parentSub.getProcess().contains(subordinate.getName())) {
				parentSub.getProcess().add(subordinate.getName());
			}
		}
		subordinateDao.update(parentSub);
		return res;
	}

	@Override
	public Pager getSubordinateStoreByName(Pager pager,long sid, String name) throws Exception{
		String sql = "SELECT  *  FROM `subordinate`   where  1=1 ";
		String sqlCount = "SELECT  COUNT(*)  FROM `subordinate` where 1=1";
		String limit = "  limit %d , %d ";
		if(sid>0) {
			sql = sql + " and `pid` = " +sid;
			sqlCount = sqlCount + " and `pid` = " +sid;
		}
		if(!name.isEmpty()){
			sql = sql + " and `name`  like '%" + name + "%'" ;
			sqlCount = sqlCount + " and `name` like '%" + name + "%'" ;
		}

		sql = sql + " order  by ctime desc";
		int count = 0;
		sql = sql + String.format(limit, pager.getSize() * (pager.getPage() - 1), pager.getSize());
		List<Subordinate> subordinateList = jdbcTemplate.query(sql,new HyperspaceBeanPropertyRowMapper<Subordinate>(Subordinate.class));
		count=this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
		pager.setData(transformClient(subordinateList));
		pager.setTotalCount(count);;
		return pager;
	}

	@Override
	public boolean updateStatus(long id, int status) throws Exception {
		Subordinate subordinate=subordinateDao.load(id);
		subordinate.setStatus(status);
		return subordinateDao.update(subordinate);
	}

	@Override
	public List<Subordinate> getAllSubordinate() throws Exception {
		String sql = "SELECT  *  FROM `subordinate` where pid = 0" ;
		sql = sql + " order  by ctime desc";
		List<Subordinate> subordinateList = jdbcTemplate.query(sql,new HyperspaceBeanPropertyRowMapper<Subordinate>(Subordinate.class));
		return subordinateList;
	}

	@Override
	public List<Subordinate> getAllParentSubordinate() throws Exception {
		return subordinateDao.getAllList(0,Subordinate.status_online);
	}

	@Override
	public List<Subordinate> getAllList() throws Exception {
		return subordinateDao.getAllList(Subordinate.status_online);
	}

	@Override
	public Pager getWebTwoLevelAllList(final Pager pager,final long sid) throws Exception {
		return new PagerRequestService<Subordinate>(pager, 0) {

			@Override
			public List<Subordinate> step1GetPageResult(String s, int i) throws Exception {
				List<Subordinate> subordinates = subordinateDao.getAllList(sid,Subordinate.status_online,Double.parseDouble(s),i);
				return subordinates;
			}

			@Override
			public int step2GetTotalCount() throws Exception {
				return 0;
			}

			@Override
			public List<Subordinate> step3FilterResult(List<Subordinate> list, PagerSession pagerSession) throws Exception {
				return list;
			}

			@Override
			public List<?> step4TransformData(List<Subordinate> list, PagerSession pagerSession) throws Exception {
				return transformClient(list);
			}

		}.getPager();
	}


	@Override
	public boolean delete(long id) throws Exception {
		Subordinate subordinate=subordinateDao.load(id);
		subordinate.setStatus(Subordinate.status_delete);
		return subordinateDao.update(subordinate);
	}

	private ClientSubordinate transformClient(Subordinate subordinate) throws Exception{
		ClientSubordinate clientSubordinate = new ClientSubordinate(subordinate);
		return clientSubordinate;
	}

	@Override
	public ClientSubordinate load(long sid) throws Exception {
		return transformClient(subordinateDao.load(sid));
	}

	@Override
	public Pager getBackPage(Pager pager,  String name) throws Exception {
		String sql = "SELECT  *  FROM `subordinate` where 1=1 ";
		String sqlCount = "SELECT  COUNT(*)  FROM `subordinate` where 1=1";
		String limit = "  limit %d , %d ";
		sql = sql + " and `status` = " + Subordinate.status_online;
		sqlCount = sqlCount + " and `status` = " + Subordinate.status_online;
		if (StringUtils.isNotBlank(name)) {
			sql = sql + " and `name` like ?";
			sqlCount = sqlCount + " and `name` like ?";
		}
		sql = sql + " order  by ctime desc";
		sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
		List<Subordinate> subordinates = null;
		int count =0;
		if(StringUtils.isNotBlank(name)){
			subordinates=jdbcTemplate.query(sql,new BeanPropertyRowMapper(Subordinate.class),"%"+name+"%");
			count=this.jdbcTemplate.queryForObject(sqlCount, new Object[] {"%"+name+"%"}, Integer.class);
		}
		else{
			subordinates=jdbcTemplate.query(sql,new BeanPropertyRowMapper(Subordinate.class));
			count=this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
		}

		pager.setData(transformClient(subordinates));
		pager.setTotalCount(count);
		return pager;
	}

	private List<ClientSubordinate> transformClient(List<Subordinate> subordinatees) throws Exception{
		List<ClientSubordinate> result=new ArrayList<>();
		if(subordinatees!=null) {
			for (Subordinate subordinate : subordinatees) {
				ClientSubordinate clientSubordinate = new ClientSubordinate(subordinate);
				result.add(clientSubordinate);
			}
		}
		return result;
	}

    @Override
    public List<ClientSubordinate> getTwoLevelAllList(long pid) throws Exception {
	    List<Subordinate> subordinates = subordinateDao.getAllList(pid,Subordinate.status_online);
        return transformClient(subordinates);
    }

    @Override
    public Pager getOneLevelPager(Pager pager) throws Exception {
        String sql = "SELECT  *  FROM `subordinate` where pid = 0 and status = " + Subordinate.status_online;
        String sqlCount = "SELECT  COUNT(*)  FROM `subordinate` where pid = 0 and status = " + Subordinate.status_online;
        String limit = " limit %d , %d";
        sql = sql + " order  by ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<Subordinate> subordinates = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(transformClient(subordinates));
        pager.setTotalCount(count);
        return pager;
    }

	@Override
	public Subordinate insertStore(Subordinate subordinate)throws Exception {
		Subordinate parentSub = subordinateDao.load(subordinate.getPid());
		if(subordinate.getProcessType()==Subordinate.processType_per){
			if(!parentSub.getProcess().contains(subordinate.getName())) {
				parentSub.getProcess().add(subordinate.getName());
			}
		}
		subordinateDao.update(parentSub);
		subordinate = subordinateDao.insert(subordinate);
		return subordinate;
	}

}
