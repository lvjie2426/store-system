/**
 *  雀科科技- http://www.quakoo.com
 *
 *  业务配置类（继承父类：QuakooConfig）
 *
 *  放本业务相关的配置
 *
 */
var Config = (function(_super){
    function Config(){
        Config.__super.call(this);

        this.isTest=false;
        /**
         * js部分使用的版本号，项目如果持续发布版本，记得更换版本号
         * @type {string}
         */
        this.version = "1.0.0";
        this.hotVersion = "201901020001"






        /**测试服务器地址*/
        this.serverUrl = "http://39.107.247.82:10004";
        /**李浩杰*/
        // this.serverUrl = "http://192.168.1.48:10004";
        /**聊天服务地址*/
        this.chatNativeUrl = "";
        /**图片服务器地址*/
        this.uploadImageUrl = "http://39.107.247.82:19996/storage/handle";
        /**聊天地址*/
        this.chatServerUrl = '39.107.247.82';
        /**聊天端口*/
        this.chatPort = '23333';
        /**推送地址*/
        this.pushServerUrl = '39.107.247.82';
        /**推送端口*/
        this.pushPort  = '23333';



    }
    var _proto = _super.prototype;
    /**登陆*/
    _proto.getUrl_web_user_loginUrl = function(){return this.serverUrl + '/login/in';};
    /**登陆验证码*/
    _proto.getUrl_web_user_loginCodeUrl = function(){return this.serverUrl + '/login/verifyCode'};

    //=======================顾客管理 开始====================
    /**获取公司下门店列表*/
    _proto.getUrl_subordinate_getSubordinateStore = function(){return this.serverUrl + '/subordinate/getSubordinateStore';};
    /**获取门店下顾客的所有职业*/
    _proto.getUrl_user_getAllUserJob = function(){return this.serverUrl + '/user/getAllUserJob';};
    /**添加顾客*/
    _proto.getUrl_user_addCustomer = function(){return this.serverUrl + '/user/addCustomer';};
    /**获取所属公司的所有顾客*/
    _proto.getUrl_user_getCustomerPager = function(){return this.serverUrl + '/user/getCustomerPager';};
    /**查询某个门店下的所有会员级别列表*/
    _proto.getUrl_usergrade_getAllList = function(){return this.serverUrl + '/usergrade/getAllList';};
    /**获取所属门店的所有顾客*/
    _proto.getUrl_user_getSubCustomerPager = function(){return this.serverUrl + '/user/getSubCustomerPager';};
    /**修改顾客信息*/
    _proto.getUrl_user_updateCustomer = function(){return this.serverUrl + '/user/updateCustomer';};

    //=======================顾客管理 结束====================

    //=======================公司管理 开始====================
    /**获取公司下门店列表*/
    _proto.getUrl_subordinate_getSub = function(){return this.serverUrl + '/subordinate/getSubordinateStore';};

    //=======================公司管理 结束====================

    //=======================企业端（营销管理） 开始====================
    /**获取公司下门店列表*/
    _proto.getUrl_subordinate_getSu = function(){return this.serverUrl + '/subordinate/getSubordinateStore';};

    //=======================企业端（营销管理） 结束====================

    //=======================商品管理 开始====================
    /**获取公司下门店列表*/
    _proto.getUrl_subordinate_ge = function(){return this.serverUrl + '/subordinate/getSubordinateStore';};

    //=======================商品管理 结束====================



    Quakoo.class(Config,'Config',_super);


    return Config;
})(QuakooConfig);

