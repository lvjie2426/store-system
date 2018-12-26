package com.store.system.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quakoo.baseFramework.json.JsonUtils;
import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * 权限初始化模板(子项)。
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class RoleTemplateItem implements Serializable {

    @PrimaryKey
    private long id;

    private String roleName;

    private String remark;

    @HyperspaceColumn(isDbColumn = false)
    private List<Long> pids;//权限

    private String pidsJson;//#v500# ^nn^权限ID

    private long roleInitTemplateId;

    private long utime;

    @PagerCursor
    @SortKey
    private long ctime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Long> getPids() {
        return pids;
    }

    public void setPids(List<Long> pids) throws IOException {
        this.pids = pids;
        if(pids!=null)
            this.pidsJson=JsonUtils.format(pids);
    }

    public String getPidsJson() {
        return pidsJson;
    }

    public void setPidsJson(String pidsJson) throws IOException {
        this.pidsJson = pidsJson;
        if(StringUtils.isNotBlank(pidsJson))
            this.pids=JsonUtils.parse(pidsJson, new TypeReference<List<Long>>() {});
    }

    public long getRoleInitTemplateId() {
        return roleInitTemplateId;
    }

    public void setRoleInitTemplateId(long roleInitTemplateId) {
        this.roleInitTemplateId = roleInitTemplateId;
    }
}
