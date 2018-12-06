package com.store.system.service;



import com.store.system.model.Permission;
import com.store.system.model.Role;

import java.util.List;


public interface RoleService {


	public Role add(Role role) throws Exception;

	public boolean update(Role role) throws Exception;

	public boolean del(long id) throws Exception;

	public boolean updateRolePermission(long rid, List<Long> pids)throws Exception;

	/**
	 * 获取所有角色，0表示平台的角色，其他值表示平台下属单位的角色。
	 * @param sid 下级单位ID
	 * @return
	 * @throws Exception
	 */
	public List<Role> getAllList(long sid) throws Exception;

	public List<Permission> getRolePermissions(long rid) throws Exception;

}
