package com.store.system.service;



import com.store.system.model.RoleInitTemplate;

import java.util.List;

/**
 * 角色模板
 */
public interface RoleInitTemplateService {

	/**
	 * 增加一个角色模板
	 */
	public RoleInitTemplate add(RoleInitTemplate roleInitTemplate) throws Exception;

	/**
	 * 修改一个角色模板
	 */
	public boolean update(RoleInitTemplate roleInitTemplate) throws Exception;

	/**
	 * 删除一个角色模板
	 */
	public boolean del(long id) throws Exception;

	/**
	 * 查出所有一个角色模板
	 */
	public List<RoleInitTemplate> getAll() throws Exception;

}
