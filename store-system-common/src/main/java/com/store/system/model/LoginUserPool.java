package com.store.system.model;

import com.s7.baseFramework.model.pagination.PagerCursor;
import com.s7.space.annotation.domain.CombinationKey;
import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.ShardingKey;
import com.s7.space.annotation.domain.SortKey;
import com.s7.space.enums.HyperspaceDomainType;

import java.io.Serializable;

@HyperspaceDomain(domainType= HyperspaceDomainType.listDataStructure)
public class LoginUserPool implements Serializable {

	private static final long serialVersionUID = -1L;



	public static int loginType_phone = 1; // 手机号登陆

	public static int loginType_userName = 2; // 账号名密码登录

	public static int loginType_weixin = 3; // 微信
	public static int loginType_qq = 4; // QQ
	public static int loginType_weibo = 5; // 微博
	public static int loginType_alipay = 6; // 支付宝登录


	@ShardingKey
	@CombinationKey
	private int loginType;

    @CombinationKey
    private int userType;//用户类型（一般分为普通用户、后台用户）
	
	@CombinationKey
	private String account;//手机号登陆的时候 对应phone. 账号名密码登录的时候对应userName 微信登录的时候对应weixinId

	
	private long uid;
	

	@SortKey
	@PagerCursor
	private long ctime;
	
	private long utime;

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getUtime() {
		return utime;
	}

	public void setUtime(long utime) {
		this.utime = utime;
	}


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
