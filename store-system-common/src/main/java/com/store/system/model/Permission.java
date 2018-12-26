package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;
import java.util.List;


@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class Permission implements Comparable<Permission>, Serializable {

    public static final int menu_yes=1;
    public static final int menu_no=0;

    public static final int subordinate_on=1;
    public static final int subordinate_off=0;
    @PrimaryKey
    private long id;

    //节点名称
    private String text;

    //权限请求路径
    private String href;

    //父类节点Id
    private long pid;

    private String cssName;



    @PagerCursor
    @SortKey
    private long sort;


    //应用到下级单位（学校、代理等） 0否，1是
    private int subordinate;

    //应用到后台菜单 0否，1是
    private int menu;

    private long ctime;

    private long utime;

    @Override
    public int compareTo(Permission other) {
        if(this.getSort() < other.getSort()) return 1;
        else if(this.getSort() > other.getSort()) return -1;
        else return this.getText().compareTo(other.getText());
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public String getCssName() {
        return cssName;
    }

    public void setCssName(String cssName) {
        this.cssName = cssName;
    }

    public int getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(int subordinate) {
        this.subordinate = subordinate;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }


    /**
     * 是否有该权限
     * 根据路径判断，不能根据名字判断
     * @param permissions
     * @param href  路径
     * @return
     */
    public static boolean hasPermission(List<Permission> permissions, String href){
        for(Permission permission : permissions){
            if(permission.getHref().contains(href)){
                return true;
            }
        }
        return false;
    }


}
