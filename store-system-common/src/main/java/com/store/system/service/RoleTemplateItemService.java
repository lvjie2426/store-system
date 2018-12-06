package com.store.system.service;



import com.store.system.model.Permission;
import com.store.system.model.RoleTemplateItem;

import java.util.List;

/**
 * 角色模板
 */
public interface RoleTemplateItemService {

	/**
	 * 角色模板内添加一个角色
	 */
	public RoleTemplateItem add(RoleTemplateItem roleTemplateItem) throws Exception;

	/**
	 * 角色模板内修改某个角色
	 */
	public boolean update(RoleTemplateItem roleTemplateItem) throws Exception;

	/**
	 * 角色模板内删除某个角色
	 */
	public boolean del(long id) throws Exception;

	/**
	 * 查出角色模板内的所有角色
	 */
	public List<RoleTemplateItem> getAll(long id) throws Exception;


    public List<Permission> getRolePermissions(long id) throws Exception;

}
