package com.store.system.service.impl;

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

	@Override
	public Subordinate insert(Subordinate subordinate) throws Exception {

		subordinate = subordinateDao.insert(subordinate);
		//为每个所生成指定的系统内部账号
		if(StringUtils.isNoneBlank(subordinate.getAdminPhone())||StringUtils.isNoneBlank(subordinate.getAdminUserName())) {
			User user = new User();
			user.setSid(subordinate.getId());
			user.setPhone(subordinate.getAdminPhone());
			user.setUserType(User.userType_backendUser);
			if(StringUtils.isNoneBlank(subordinate.getAdminPassword())) {
				user.setPassword(subordinate.getAdminPassword());
			}
			user.setUserName(subordinate.getAdminUserName());
			user.setName(subordinate.getName() + "+admin");
            ClientUserOnLogin clientUserOnLogin = userService.register(user);
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
				roleService.updateRolePermission(role.getId(),pids);
			}
		}
		return subordinate;
	}

	@Override
	public boolean update(Subordinate subordinate) throws Exception {
		Subordinate db=subordinateDao.load(subordinate.getId());
		db.setProvince(subordinate.getProvince());
		db.setCity(subordinate.getCity());
		db.setName(subordinate.getName());
		db.setContent(subordinate.getContent());
		db.setIcon(subordinate.getIcon());
		db.setPhone(subordinate.getPhone());
		db.setDesc(subordinate.getDesc());
		return subordinateDao.update(db);
	}

	@Override
	public boolean delete(long id) throws Exception {
		Subordinate subordinate=subordinateDao.load(id);
		subordinate.setStatus(Subordinate.status_delete);
		return subordinateDao.update(subordinate);
	}

	private List<ClientSubordinate> transformClientByProvince(List<Subordinate> subordinatees) throws Exception{
		List<ClientSubordinate> result=new ArrayList<>();
		if(subordinatees!=null) {
			for (Subordinate subordinate : subordinatees) {
				ClientSubordinate clientSubordinate = transformClient(subordinate);
				result.add(clientSubordinate);
			}
		}
		return result;
	}

	private ClientSubordinate transformClient(Subordinate subordinate) throws Exception{
		ClientSubordinate clientSubordinate = new ClientSubordinate();
		BeanUtils.copyProperties(clientSubordinate, subordinate);
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
				ClientSubordinate clientSubordinate = transformClient(subordinate);
				result.add(clientSubordinate);
			}
		}
		return result;
	}


}
