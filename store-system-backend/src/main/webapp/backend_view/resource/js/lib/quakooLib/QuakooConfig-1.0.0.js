/**
 *  雀科科技- http://www.quakoo.com
 *
 *  配置类
 *
 *
 *  跟后端积木工程对应的接口请保持所有项目一致
 *  积木工程接口，请查看积木工程接口文档
 *
 */


var QuakooConfig = (function () {
    function QuakooConfig() {
        this.isTest=false;
        /**
         * js部分使用的版本号
         * @type {string}
         */
        this.version = "1.0.0";
        this.rootWindowName = "root";
        //头部高度
        this.headHeight = 44;
        //底部高度
        this.bottomHeight = 48;


        //win窗口中打开的FRAME距离顶部的高度（win窗口头部高度）
        this.winHeadHeight = 45;
        //win窗口中打开的FRAME距离底部的高度（win窗口底部高度）
        this.winBottomHeight = 0;

        //是否初始化
        this.isInit = "isInit";
        this.lastTime = 'lastTime';
        //是否播放引导视频
        this.isShowGuide = false;
        //是否需要游客登录
        this.isNeedCustomerUser = false;
        //短信验证码发送时间
        this.Storage_Sms_Time = "smsTime";
        //聊天未读消息数
        this.Storage_chat_num = "chat_num";
        //当前地址
        this.curAddress = 'curAddress';
        //当前经度
        this.curLon = 'curLon';
        //当前纬度
        this.curLat = 'curLat';
        //购物车
        this.cartsKey = 'carts';




        //=======================Quakoo Data配置 开始====================
        this.quakooPage_downNum=12;
        this.quakooDate_upNum=6;
        this.quakooDate_threshold=100;
        this.quakooDate_openUpAction=true;//开始上拉事件效果
        this.quakooDate_customRefreshHeaderInfo=false;        //使用自定义下拉组件


        //使用自定义下拉刷新组件之前，需要在config.xml里面配置要使用的自定义下拉刷新模块名称，如：
        //<preference name="customRefreshHeader" value="UIPullRefresh"/>


        //=======================Quakoo Data配置 开始====================







        //=======================项目所用API 开始====================

        /**api服务地址*/
        this.serverUrl = "";
        /**聊天服务地址*/
        this.chatNativeUrl = "";
        /**图片服务器地址*/
        this.uploadImageUrl = "";




    }



    var _proto = QuakooConfig.prototype;
    Quakoo.class(QuakooConfig,'QuakooConfig');
    _proto.getServerUrl=function () {
        return this.serverUrl;
    }

    _proto.getUploadImageUrl=function () {
        return this.uploadImageUrl;
    }
    //=======================积木工程系统接口 开始====================
    _proto.getUrl_web_version_hotUpdate = function(){return this.serverUrl + '/version/hotUpdate';}

    //=======================积木工程系统接口 结束====================




    //=======================积木工程用户接口 开始====================
    /**游客登录*/
    _proto.getUrl_web_user_loadCustomer = function(){return this.serverUrl + '/user/loadCustomer';}
    /** 登录*/
    _proto.getUrl_web_user_loginUrl = function(){return this.serverUrl + '/user/login';} //phone password
    /**注册获取验证码*/
    _proto.getUrl_web_user_getCode = function(){return this.serverUrl + '/user/createAuthCodeOnReg';} //phone
    /**登陆获取验证码*/
    _proto.createAuthCodeOnLogin = function(){return this.serverUrl + '/user/createAuthCodeOnLogin';} //phone
    /**修改密码获取验证码*/
    _proto.getUrl_web_user_createAuthCodeOnUpdatePassword = function(){return this.serverUrl + '/user/createAuthCodeOnUpdatePassword';} //phone
    /**修改密码*/
    _proto.getUrl_web_user_updatePasswordAndLogin = function(){return this.serverUrl + '/user/updatePasswordAndLogin';} //phone,code,password
    /**注册*/
    _proto.getUrl_web_user_regUrl = function(){return this.serverUrl + '/user/register';}
    /**验证三方账号是否注册*/
    _proto.getUrl_web_user_checkThird = function(){return this.serverUrl + '/user/checkThird';}
    /**我的信息*/
    _proto.getUrl_web_user_myMsg = function(){return this.serverUrl + '/user/load';}
    /**修改我的信息*/
    _proto.getUrl_web_user_updataMyMsg = function(){return this.serverUrl + '/user/updateUser';}
    /**设置支付密码*/
    _proto.getUrl_web_user_setPaypassWord = function(){return this.serverUrl + '/user/updatePayPassword';}
    /**检查支付密码*/
    _proto.getUrl_web_user_checkloadPayPwd = function(){return this.serverUrl + '/user/loadPay';}
    /**修改支付密码*/
    _proto.getUrl_web_user_editPayPwd = function(){return this.serverUrl + '/user/updatePayPasswordByCode';}
    /**获得微信openid*/
    _proto.getUrl_web_user_getWxpayOpenId = function(){return this.serverUrl + '/user/getWxpayOpenId';}
    /**获得支付宝openid*/
    _proto.getUrl_web_user_getAlipayUid = function(){return this.serverUrl + '/user/getAlipayUid';}

    /**绑定第三方key*/
    _proto.getUrl_web_user_bangKey = function(){return this.serverUrl + '/user/bangKey';}
    /**解绑第三方key*/
    _proto.getUrl_web_user_untiedAuthKey = function(){return this.serverUrl + '/user/untiedAuthKey';}


    /**我的钱包*/
    _proto.getUrl_web_user_cashMoneyUrl = function(){return this.serverUrl + '/wallet/cashMoney';}
    _proto.getUrl_web_record_getRecordPagerUrl = function(){return this.serverUrl + '/record/getRecordPager';}
    _proto.getUrl_web_pay_orderPayParamUrl = function(){return this.serverUrl + '/pay/orderPayParam';}
    _proto.getUrl_web_wallet_chargeWalletUrl = function(){return this.serverUrl + '/wallet/chargeWallet';}
    _proto.getUrl_web_pay_aliAuthParamUrl = function(){return this.serverUrl + '/pay/aliAuthParam';}
    _proto.getUrl_web_pay_updateUserAliCountUrl = function(){return this.serverUrl + '/pay/updateUserAliCount';}
    _proto.getUrl_web_pay_updateUserWxCountUrl = function(){return this.serverUrl + '/pay/updateUserWxCount';}
    _proto.getUrl_web_wallet_loadTaxRateUrl = function(){return this.serverUrl + '/wallet/loadTaxRate';}
    _proto.getUrl_web_wallet_loadSystemInfoUrl = function(){return this.serverUrl + '/wallet/loadSystemInfo';}
    _proto.getUrl_web_coupon_getCouponPagerUrl = function(){return this.serverUrl + '/coupon/getCouponPager';}
    _proto.getUrl_web_coupon_getCanUseCouponCountUrl = function(){return this.serverUrl + '/coupon/getCanUseCouponCount';}
    _proto.getUrl_web_user_createUpdatePayInfoAuthCodeUrl = function(){return this.serverUrl + '/user/createUpdatePayInfoAuthCode';}
    _proto.getUrl_web_user_updatePayPasswordUrl = function(){return this.serverUrl + '/user/updatePayPassword';}
    _proto.getUrl_web_user_createUpdatePasswordAuthCodeUrl = function(){return this.serverUrl + '/user/createUpdatePasswordAuthCode';}



    /**搜索好友，模糊搜索*/
    _proto.getUrl_web_user_userSearch = function(){return this.serverUrl + '/user/search';} //name
    /**获得所有好友*/
    _proto.getUrl_web_user_friendPager = function(){return this.serverUrl + '/friend/pager';}
    /**查询好友是否已被添加*/
    _proto.getUrl_web_user_friendLoad = function(){return this.serverUrl + '/friend/load';}
    /**添加好友*/
    _proto.getUrl_web_user_friendAdd = function(){return this.serverUrl + '/friend/add';}
    /**删除好友*/
    _proto.getUrl_web_user_friendDel = function(){return this.serverUrl + '/friend/del';}

    /**获得个人会员等级*/
    _proto.getUrl_web_expense_level = function(){return this.serverUrl + '/expense/level';}
    /**会员等级详情*/
    _proto.getUrl_web_expense_getList = function(){return this.serverUrl + '/expense/getList';}
    //=======================积木工程用户接口 结束====================



    //=======================积木工程系统接口 开始====================
    /**收藏*/
    _proto.getUrl_web_system_favoriteInfo = function(){return this.serverUrl + '/favorite/pager';} //收藏列表
    _proto.getUrl_web_system_favoriteAdd = function(){return this.serverUrl + '/favorite/add';} //添加收藏
    _proto.getUrl_web_system_favoriteDelete = function(){return this.serverUrl + '/favorite/delete';} //取消收藏
    /**关注/粉丝*/
    _proto.getUrl_web_system_focusfansFocusInfo = function(){return this.serverUrl + '/focusfans/focusPager';} //关注列表
    _proto.getUrl_web_system_focusfansFansInfo = function(){return this.serverUrl + '/focusfans/fansPager';} //粉丝列表
    _proto.getUrl_web_system_focusfansAdd = function(){return this.serverUrl + '/focusfans/add';} //添加关注
    _proto.getUrl_web_system_focusfansDelete = function(){return this.serverUrl + '/focusfans/delete';} //取消关注
    /**点赞*/
    _proto.getUrl_web_system_supportAdd = function(){return this.serverUrl + '/support/add';} //添加点赞
    _proto.getUrl_web_system_supportDelete = function(){return this.serverUrl + '/support/delete';} //取消点赞
    /**评论*/
    _proto.getUrl_web_system_commentPager = function(){return this.serverUrl + '/comment/pager';}//获取评论 typeId：当前商品的id;}type类型
    _proto.getUrl_web_system_commentAdd = function(){return this.serverUrl + '/comment/add';}//添加评论 typeId：当前商品的id;}type类型

    _proto.getUrl_web_system_comment = function(){return this.serverUrl + '/comment/addAll';}//发布动态评论
    _proto.getUrl_web_system_commentDel = function(){return this.serverUrl + '/comment/del';}//删除评论

    /**轮播图*/
    _proto.getUrl_web_system_getAllBanner = function(){return this.serverUrl + '/banner/getAll';}
    /**提交反馈*/
    _proto.getUrl_web_system_complaintAdd = function(){return this.serverUrl + '/complaint/add';}
    /**关于我们和使用帮助*/
    _proto.getUrl_web_system_dictionaryConfig = function(){return this.serverUrl + '/dictionary/config';}
    //=======================积木工程系统接口 结束====================




    //=======================积木工程商城接口 开始====================
    /**首页*/
    /**获取通知*/
    _proto.getUrl_web_mall_remindAll = function(){return this.serverUrl + '/remind/all';} //type
    /**获取推荐*/
    _proto.getUrl_web_mall_getRecommends = function(){return this.serverUrl + '/recommend/getAll';} //type
    /**首页搜索*/
    _proto.getUrl_web_mall_goodsSearch = function(){return this.serverUrl + '/goods/search';}
    /**按类型搜索*/
    _proto.getUrl_web_mall_getSearchByType = function(){return this.serverUrl + '/goods/getSearchByType';}
    /**在店铺中按店铺id和类型搜索*/
    _proto.getUrl_web_mall_getPagerBySidAndGoodTypeId = function(){return this.serverUrl + '/goods/getPagerBySidAndGoodTypeId';}
    /**在店铺中按关键字搜索*/
    _proto.getUrl_web_mall_getSearchBySid = function(){return this.serverUrl + '/goods/getSearchBySid';}
    /**热门搜索*/
    _proto.getUrl_web_mall_hotSearchAll = function(){return this.serverUrl + '/hotsearch/getAll';}

    /**时尚专区*/
    _proto.getUrl_web_mall_zoneGetAll = function(){return this.serverUrl + '/zone/getAll';} // 首页获取专区文章
    _proto.getUrl_web_mall_getArticle = function(){return this.serverUrl + '/zone/getArticle';} // 获取专区文章
    _proto.getUrl_web_mall_getZone = function(){return this.serverUrl + '/zone/getZone';} // 获取专区分类
    _proto.getUrl_web_mall_getZoneLoad = function(){return this.serverUrl + '/zone/load';} // 获取专区文章详情



    _proto.getUrl_web_mall_style = function(){return this.serverUrl + '/dynamic/getStyle';} // 获取时尚圈风格列表

    /**获取商品分类*/
    _proto.getUrl_web_mall_getGoodsClass = function(){return this.serverUrl + '/goodtype/getAll';} //id
    _proto.getUrl_web_mall_getGoodsClassList = function(){return this.serverUrl + '/goods/getPagerByGoodTypeId';}
    _proto.getUrl_web_mall_getGoodsIndexs = function(){return this.serverUrl + '/goods/load';} //id
    _proto.getUrl_web_mall_getRe = function(){return this.serverUrl + '/goods/getReCommend';} // goodsId
    _proto.getUrl_web_mall_getPagerByType = function(){return this.serverUrl + '/goods/getPagerByType';} // type，sort
    _proto.getUrl_web_mall_getPagerByTypeAndGoodTypeId = function(){return this.serverUrl + '/goods/getPagerByTypeAndGoodTypeId';} // type，sort goodTypeId
    //获得商品规格skuid,
    _proto.getUrl_web_mall_selectSKU = function(){return this.serverUrl + '/goods/selectSKU';} // gid,color,size

    _proto.getUrl_web_mall_getAllStore = function(){return this.serverUrl + '/subordinate/getAllByType';} //type:设计师 2    品牌 3
    /**店铺*/
    /**获取店铺风格*/
    _proto.getUrl_web_mall_getStyles = function(){return this.serverUrl + '/subordinate/getStyle';}
    /**根据风格获取所有店铺*/
    _proto.getUrl_web_mall_getStyleAll = function(){return this.serverUrl + '/subordinate/getAll';} //styleId
    /**获取推荐店铺*/
    _proto.getUrl_web_mall_getRecommendBrand = function(){return this.serverUrl + '/subordinate/getRecommend';} //type 1自营 2设计师 3品牌
    /**获取全部店铺*/
    _proto.getUrl_web_mall_getBrand = function(){return this.serverUrl + '/subordinate/getPager';} //type 1自营 2设计师 3品牌
    /**获取店铺详情*/
    _proto.getUrl_web_mall_getBrandIndex = function(){return this.serverUrl + '/subordinate/load';}//id
    /**查询客服id*/
    _proto.getUrl_web_mall_subordinateLoadChatUid = function(){return this.serverUrl + '/subordinate/loadChatUid';} //店铺id
    /**在订单中查询客服id*/
    _proto.getUrl_web_mall_subordinateLoadChatUidByOrder = function(){return this.serverUrl + '/subordinate/loadChatUidByOrder';} //订单id
    /**根据店铺获取商品*/
    _proto.getUrl_web_mall_getStoreOfGoods = function(){return this.serverUrl + '/goods/getPagerBySid';} //sid,sort
    /**搜索店铺*/
    _proto.getUrl_web_mall_subordinateSearch = function(){return this.serverUrl + '/subordinate/search';} //name(店铺名)

    /**获取全部二级分类*/
    _proto.getUrl_web_mall_getTwoClass = function(){return this.serverUrl + '/goodtype/getTwo';}

    /**购物车*/
    /**创建预订单*/
    _proto.getUrl_web_mall_createIndent = function(){return this.serverUrl + '/suborder/createPreOrder';}
    /**根据订单中的店铺金额，得到运费*/
    _proto.getUrl_web_mall_postageGetPrice = function(){return this.serverUrl + '/postage/getPrice';} //sid，totalPrice
    /**提交订单*/
    _proto.getUrl_web_mall_postIndent = function(){return this.serverUrl + '/suborder/createSubOrder';}
    /**取消订单*/
    _proto.getUrl_web_mall_cancelIndent = function(){return this.serverUrl + '/suborder/cancelSubOrder';}
    /**删除订单*/
    _proto.getUrl_web_mall_delIndent = function(){return this.serverUrl + '/suborder/delSubOrder';}
    /**生成立即购买订单*/
    _proto.getUrl_web_mall_createImmIndent = function(){return this.serverUrl + '/suborder/buyNoCart';}
    /**添加到购物车*/
    _proto.getUrl_web_mall_addToCar = function(){return this.serverUrl + '/shopcart/addGoods';}
    /**购物车列表*/
    _proto.getUrl_web_mall_getShopCarList = function(){return this.serverUrl + '/shopcart/getShopCarts';}
    /**添加商品数量*/
    _proto.getUrl_web_mall_addGoodsNum = function(){return this.serverUrl + '/shopcart/incrNum';}
    /**减少商品数量*/
    _proto.getUrl_web_mall_minusGoodsNum = function(){return this.serverUrl + '/shopcart/decrNum';}
    /**删除商品*/
    _proto.getUrl_web_mall_delGood = function(){return this.serverUrl + '/shopcart/delete';}
    /**修改商品颜色 大小*/
    _proto.getUrl_web_mall_updataCS = function(){return this.serverUrl + '/shopcart/updateGoods';}

    /** 地址*/
    /** 默认地址*/
    _proto.getUrl_web_mall_getdefAddress = function(){return this.serverUrl + '/address/getDefault';}
    /**设为收货地址*/
    _proto.getUrl_web_mall_setDefAddress = function(){return this.serverUrl + '/address/setDefault';}
    /**修改收货地址*/
    _proto.getUrl_web_mall_updataAddress = function(){return this.serverUrl + '/address/update';}
    /**删除收货地址*/
    _proto.getUrl_web_mall_delAddress = function(){return this.serverUrl + '/address/del';}
    /**我的收货地址*/
    _proto.getUrl_web_mall_myAddress = function(){return this.serverUrl + '/address/getAll';}
    /**添加收货地址*/
    _proto.getUrl_web_mall_addAddr = function(){return this.serverUrl + '/address/add';}

    //我的订单*/
    /** 获取我的全部订单*/
    _proto.getUrl_web_mall_getMyAllIndent = function(){return this.serverUrl + '/suborder/getPager';}
    /**获取我的待付款订单*/
    _proto.getUrl_web_mall_getMyNoPayIndent = function(){return this.serverUrl + '/suborder/getWaitPayPager';}
    /**获取我的待发货订单*/
    _proto.getUrl_web_mall_getNoDeliverIndent = function(){return this.serverUrl + '/suborder/getWaitSendPager';}
    /** 获取我的待收货订单*/
    _proto.getUrl_web_mall_getMyTakeIndent = function(){return this.serverUrl + '/suborder/getWaitReceivePager';}
    /** 获取我的待评价订单*/
    _proto.getUrl_web_mall_getMyNoAppraiseIndent = function(){return this.serverUrl + '/suborder/getWaitCommentPager';}
    /**确认收货*/
    _proto.getUrl_web_mall_receiveSubOrder = function(){return this.serverUrl + '/suborder/receiveSubOrder';}
    /**快递信息*/
    _proto.getUrl_web_mall_Infos = function(){return this.serverUrl + '/logistics/getInfos';}

    //售后
    /**添加售后*/
    _proto.getUrl_web_mall_addSales = function(){return this.serverUrl + '/aftersale/add';}
    /**取消售后*/
    _proto.getUrl_web_mall_cancelSales = function(){return this.serverUrl + '/aftersale/cancel';}
    /**售后列表*/
    _proto.getUrl_web_mall_salesList = function(){return this.serverUrl + '/aftersale/getPager';}
    /**售后详情*/
    _proto.getUrl_web_mall_salesLoad = function(){return this.serverUrl + '/aftersale/load';}

    /**优惠券*/
    /**获取我的优惠券*/
    _proto.getUrl_web_mall_getMyCoupons = function(){return this.serverUrl + '/coupon/mine';} //1可用 0已使用 2过期
    /**获取可领优惠券*/
    _proto.getUrl_web_mall_getCanGet = function(){return this.serverUrl + '/coupon/canGet';}
    /**领取优惠券*/
    _proto.getUrl_web_mall_getCoupon = function(){return this.serverUrl + '/coupon/get';} //领取优惠券
    /**当前店铺优惠券*/
    _proto.getUrl_web_mall_getYesCoupon = function(){return this.serverUrl + '/coupon/canUse';}//handle  0可用   1不可用
    /**获取店铺优惠券*/
    _proto.getUrl_web_mall_getStoreCoupon = function(){return this.serverUrl + '/coupon/getSub';}

    /**积分*/
    /**获取总积分*/
    _proto.getUrl_web_mall_getTotalIntegral = function(){return this.serverUrl + '/score/load';}
    /**获取积分列表*/
    _proto.getUrl_web_mall_getIntegralList = function(){return this.serverUrl + '/score/getPager';}

    /**获取全部动态*/
    _proto.getUrl_web_mall_Dynamic = function(){return this.serverUrl + '/dynamic/getAllDynamic';}
    /**获取某用户或店铺的动态*/
    _proto.getUrl_web_mall_getThirdDynamic = function(){return this.serverUrl + '/dynamic/getThirdDynamic';}
    /**根据类型获取动态*/
    _proto.getUrl_web_mall_getStyleDynamic = function(){return this.serverUrl + '/dynamic/getStyleDynamic';}
    /**动态详情*/
    _proto.getUrl_web_mall_Load = function(){return this.serverUrl + '/dynamic/load';}
    /**添加动态*/
    _proto.getUrl_web_mall_dynamicAdd = function(){return this.serverUrl + '/dynamic/add';}
    /**删除动态*/
    _proto.getUrl_web_mall_dynamicDel = function(){return this.serverUrl + '/dynamic/del';}
    /**获取关注人的所有动态*/
    _proto.getUrl_web_mall_dynamicFocusPager = function(){return this.serverUrl + '/dynamic/focusPager';}
    /**获取附近的所有动态*/
    _proto.getUrl_web_mall_dynamicNear = function(){return this.serverUrl + '/dynamic/near';}
    //=======================积木工程商城接口 结束====================




    //=======================积木工程钱包和支付接口 开始====================
    /**获取钱数*/
    _proto.getUrl_web_pay_getMyMoney = function(){return this.serverUrl + '/wallet/loadMoney';}
    /**获取钱数*/
    _proto.getUrl_web_pay_getConfig = function(){return this.serverUrl + '/dictionary/config';}
    /**获取收入列表*/
    _proto.getUrl_web_pay_getIncomeList = function(){return this.serverUrl + '/wallet/getInPager';}
    /**获取支出列表*/
    _proto.getUrl_web_pay_getPayList = function(){return this.serverUrl + '/wallet/getOutPager';}
    /**获取佣金列表*/
    _proto.getUrl_web_pay_getCommission = function(){return this.serverUrl + '/wallet/getCommission';}
    /**获取全部列表*/
    _proto.getUrl_web_pay_getAllList = function(){return this.serverUrl + '/wallet/getPager';}
    //支付
    /**比对支付密码*/
    _proto.getUrl_web_pay_checkPayPassword = function(){return this.serverUrl + '/user/checkPayPassword';}
    /**获取ali oid*/
    _proto.getUrl_web_pay_getAliOid = function(){return this.serverUrl + '/order/createAliOrderAboutSub';}//subOids
    /**修改ali oid*/
    _proto.getUrl_web_pay_updateAlipayUid = function(){return this.serverUrl + '/user/updateAlipayUid';}//alipayUid
    /**获取阿里授权参数*/
    _proto.getUrl_web_pay_aliAuthParam = function(){return this.serverUrl + '/user/aliAuthParam';}
    /**获取微信oid*/
    _proto.getUrl_web_pay_getWxOid = function(){return this.serverUrl + '/order/createWxOrderAboutSub';}//subOids
    /**修改微信oid*/
    _proto.getUrl_web_pay_updateWxpayOpenId = function(){return this.serverUrl + '/user/updateWxpayOpenId';} //wxpayOpenId
    /**获取支付宝支付参数*/
    _proto.getUrl_web_pay_aliParam = function(){return this.serverUrl + '/order/aliOrderPayParam';}//oid
    /** 获取微信支付参数*/
    _proto.getUrl_web_pay_wxPay = function(){return this.serverUrl + '/order/wxOrderPayParam';} //oid
    /** 使用钱包支付*/
    _proto.getUrl_web_pay_walletPay = function(){return this.serverUrl + '/wallet/payAboutSub';} //subOids
    /**充值*/
    /**微信充值*/
    _proto.getUrl_web_pay_wxRecharge = function(){return this.serverUrl + '/order/createWxOrderAboutWallet';} //money 元
    /**支付宝充值*/
    _proto.getUrl_web_pay_zfbRecharge = function(){return this.serverUrl + '/order/createAliOrderAboutWallet';}//money 元
    /**提现*/
    _proto.getUrl_web_pay_withdraw = function(){return this.serverUrl + '/wallet/submitTransItem';}//1支付宝 2微信
    //=======================积木工程钱包和支付接口 结束====================

    //=======================积木工程直播接口 开始====================
    /**开始直播列表*/
    _proto.getUrl_web_live_getBeginPager = function(){return this.serverUrl + '/liveroom/getBeginPager';}
    /**获取所有直播记录*/
    _proto.getUrl_web_live_getAllRecords = function(){return this.serverUrl + '/liveroom/getAllRecords';}
    /**获取某一店铺的直播记录*/
    _proto.getUrl_web_live_getRecords = function(){return this.serverUrl + '/liveroom/getRecords';} //id
    /**直播预告*/
    _proto.getUrl_web_live_getLiveNotice = function(){return this.serverUrl + '/liveroom/getLiveNotice';} //id
    /**直播详情*/
    _proto.getUrl_web_live_liveRoomLoad = function(){return this.serverUrl + '/liveroom/load';} //id
    /**获取直播推荐商品*/
    _proto.getUrl_web_live_getLiveRecommend = function(){return this.serverUrl + '/goods/getLiveRecommend';} //liveId
    //=======================积木工程钱包和支付接口 结束====================


    /**意见建议*/
    _proto.getUrl_web_about_feedback = function(){return this.serverUrl + "/suggestion/addSuggestion";} //content:内容、手机号
    /**问题详情*/
    _proto.getUrl_web_about_lookDetail= function(){return this.serverUrl + "/helpCenter/lookDetail";} //帮助详情


    return QuakooConfig;
})();

