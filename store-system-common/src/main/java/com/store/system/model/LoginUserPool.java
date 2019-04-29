package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import lombok.Data;

import java.io.Serializable;

@HyperspaceDomain(domainType= HyperspaceDomainType.listDataStructure)
@Data
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

}
