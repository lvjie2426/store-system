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
         // this.serverUrl = "http://39.107.247.82:20005";
        /**李浩杰*/
        // this.serverUrl = "http://192.168.1.30:20005";
        /**马文军*/
        this.serverUrl = "http://192.168.1.17:20005";
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
    /**图片上传地址*/
    _proto.getUrl_web_img_uploadUrl = function(){return this.serverUrl + '/storage/handle';};
    /**登陆*/
    _proto.getUrl_web_user_loginUrl = function(){return this.serverUrl + '/login/in';};
    /**登陆验证码*/
    _proto.getUrl_web_user_loginCodeUrl = function(){return this.serverUrl + '/login/verifyCode'};

    //=======================顾客管理 开始====================
    /**获取公司下门店列表*/
    _proto.getUrl_subordinate_getSubordinateStore = function(){return this.serverUrl + '/subordinate/getSubordinateStore';};
    /**获取门店下顾客的所有职业*/
    _proto.getUrl_user_getAllUserJob = function(){return this.serverUrl + '/user/getAllUserJob';};
    /**添加顾客(添加员工)*/
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

    //=======================会员等级 开始====================
    /**查询某个门店下的所有会员级别列表（查询会员列表）*/
    _proto.getUrl_usergrade_getAllList = function(){return this.serverUrl + '/usergrade/getAllList';};
    /**添加会员级别*/
    _proto.getUrl_usergrade_add = function(){return this.serverUrl + '/usergrade/add';};
    /**编辑会员级别信息*/
    _proto.getUrl_usergrade_update = function(){return this.serverUrl + '/usergrade/update';};
    /**删除当前会员级别*/
    _proto.getUrl_usergrade_del = function(){return this.serverUrl + '/usergrade/del';};
    //=======================会员等级 结束====================

    //=======================公司管理 开始====================
    /**根据名称搜索门店*/
    _proto.getUrl_subordinate_getSubordinateStoreByName = function(){return this.serverUrl + '/subordinate/getSubordinateStoreByName';};
    /**修改企业信息*/
    _proto.getUrl_subordinate_updateSubordinate = function(){return this.serverUrl + '/subordinate/updateSubordinate';};
    /**修改企业下门店信息*/
    _proto.getUrl_subordinate_updateSubordinateStore = function(){return this.serverUrl + '/subordinate/updateSubordinateStore';};
    /**企业下创建门店*/
    _proto.getUrl_subordinate_addSubordinateStore = function(){return this.serverUrl + '/subordinate/addSubordinateStore';};
    /**开启/关闭门店*/
    _proto.getUrl_subordinate_updateOpen = function(){return this.serverUrl + '/subordinate/updateOpen';};
    /**获取当前公司信息*/
    _proto.getUrl_subordinate_getSubordinate = function(){return this.serverUrl + '/subordinate/getSubordinate';};
    //=======================公司管理 结束====================

    //=======================员工管理 开始====================
    /**员工信息修改*/
    _proto.getUrl_user_updateUser = function(){return this.serverUrl + '/user/updateUser';};
    /**添加员工*/
    _proto.getUrl_user_addCustomer = function(){return this.serverUrl + '/user/addCustomer';};
    /**停用员工*/
    _proto.getUrl_user_updateStatus = function(){return this.serverUrl + '/user/updateStatus';};
    //=======================员工管理 结束====================

    //=======================企业端（营销管理） 开始====================
    /**新增短信群发*/
    _proto.getUrl_marketingtimingsms_add = function(){return this.serverUrl + '/marketingtimingsms/add';};
    /**获取短信群发列表*/
    _proto.getUrl_marketingtimingsms_getCheckPager = function(){return this.serverUrl + '/marketingtimingsms/getCheckPager';};
    /**编辑短信群发*/
    _proto.getUrl_marketingtimingsms_update = function(){return this.serverUrl + '/marketingtimingsms/update';};
    /**新增抵用卷*/
    _proto.getUrl_marketingcoupon_add = function(){return this.serverUrl + '/marketingcoupon/add';};
    /**启用/关闭抵用卷*/
    _proto.getUrl_marketingcoupon_updateOpen = function(){return this.serverUrl + '/marketingcoupon/updateOpen';};
    /**删除抵用卷*/
    _proto.getUrl_marketingcoupon_del = function(){return this.serverUrl + '/marketingcoupon/del';};
    /**获取抵用卷列表*/
    _proto.getUrl_marketingcoupon_getAllList = function(){return this.serverUrl + '/marketingcoupon/getAllList';};
    /**更新抵用卷排序*/
    _proto.getUrl_marketingcoupon_updateSort = function(){return this.serverUrl + '/marketingcoupon/updateSort';};
    /**设置短信营销（待定）*/
    //_proto.getUrl_subordinate_getSu = function(){return this.serverUrl + '/marketingtimingsms/add';};
    /**编辑短信营销(待定)*/
    //_proto.getUrl_subordinate_getSu = function(){return this.serverUrl + '/marketingtimingsms/add';};
    //=======================企业端（营销管理） 结束====================

    //=======================商品管理 开始====================
    /**获取商品的属性的值列表*/
    _proto.getUrl_productPropertyvalue_getAllList = function(){return this.serverUrl + '/productpropertyvalue/getAllList';};
    /**获取企业类目列表*/
    _proto.getUrl_productCategory_getSubAllList = function(){return this.serverUrl + '/productcategory/getSubAllList';};
    /**获取供应商*/
    _proto.getUrl_productProvider_getSubAllList = function(){return this.serverUrl + '/productprovider/getSubAllList';};
    /**获取门店下的品牌*/
    _proto.getUrl_productBrand_getSubAllList = function(){return this.serverUrl + '/productbrand/getSubAllList';};
    /**获取品牌下系列列表*/
    _proto.getUrl_productSeries_getSubAllList = function(){return this.serverUrl + '/productseries/getSubAllList';};
    /**获取商品列表*/
    _proto.getUrl_product_getSPUPager = function(){return this.serverUrl + '/product/getSPUPager';};
    /**增加商品*/
    _proto.getUrl_product_add = function(){return this.serverUrl + '/product/add';};
    /**删除商品spu*/
    _proto.getUrl_product_delSPU = function(){return this.serverUrl + '/product/delSPU';};
    /**根据spu的id加载商品信息*/
    _proto.getUrl_product_loadSPU = function(){return this.serverUrl + '/product/loadSPU';};
    /**开启关闭销售状态*/
    _proto.getUrl_product_updateSaleStatus = function(){return this.serverUrl + '/product/updateSaleStatus';};

    //=======================商品管理 结束====================



    Quakoo.class(Config,'Config',_super);


    return Config;
})(QuakooConfig);

