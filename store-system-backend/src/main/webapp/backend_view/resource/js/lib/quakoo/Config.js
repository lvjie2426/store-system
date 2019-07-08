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
        this.hotVersion = "201901020001";






        /**测试服务器地址*/
        this.serverUrl = "http://39.107.247.82:20005";
        /**李浩杰*/
        // this.serverUrl = "http://192.168.1.29:20005";
        /**张猛*/
        // this.serverUrl = "http://192.168.1.7:20005";
        /**马文军*/
         this.serverUrl = "http://192.168.1.30:20005";
        /**聊天服务地址*/
        this.chatNativeUrl = "";
        /**图片服务器地址*/
        this.uploadImageUrl = "http://39.107.247.82:20005/storage/handle";
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
    /**登陆*/
    _proto.getUrl_web_user_getTree = function(){return this.serverUrl + '/user/getTree';};
    /**登陆验证码*/
    _proto.getUrl_web_user_loginCodeUrl = function(){return this.serverUrl + '/login/verifyCode'};


    //=======================销售开单 开始====================
    /**获取临时订单*/
    _proto.getUrl_order_getTemporaryOrder = function(){return this.serverUrl + '/order/getTemporaryOrder';};
    /**保存验光信息*/
    _proto.getUrl_optometryinfo_add = function(){return this.serverUrl + '/optometryinfo/add';};
    /**获取某个用户的验光记录*/
    _proto.getUrl_optometryinfo_getList = function(){return this.serverUrl + '/optometryinfo/getList';};
    /**获取门店历史消费记录*/
    _proto.getUrl_order_getOrderBySubid = function(){return this.serverUrl + '/order/getOrderBySubid';};
    /**计算订单金额*/
    _proto.getUrl_order_countPrice = function(){return this.serverUrl + '/order/countPrice';};
    /**保存订单*/
    _proto.getUrl_order_saveOrder = function(){return this.serverUrl + '/order/saveOrder';};
    /**修改验光信息*/
    _proto.getUrl_optometryinfo_update = function(){return this.serverUrl + '/optometryinfo/update';};
    /**更多验光记录*/
    _proto.getUrl_optometryinfo_getList = function(){return this.serverUrl + '/optometryinfo/getList';};
    /**根据手机号查询用户信息*/
    _proto.getUrl_user_getUserByPhone = function(){return this.serverUrl + '/user/getUserByPhone';};
    /**会员信息认证 或 注册会员*/
    _proto.getUrl_user_becomeVip = function(){return this.serverUrl + '/user/becomeVip';};
    /**检查手机号验证码*/
    _proto.getUrl_user_checkCode = function(){return this.serverUrl + '/user/checkCode';};
    /**发送验证码*/
    _proto.getUrl_user_createAuthCodeOnReg = function(){return this.serverUrl + '/user/createAuthCodeOnReg';};
    /**根据职位获取用户列表*/
    _proto.getUrl_user_getUsersByJob = function(){return this.serverUrl + '/user/getUsersByJob';};
    /**获取 验光结果建议*/
    _proto.getUrl_optometryinfo_getResult = function(){return this.serverUrl + '/optometryinfo/getResult';};
    /**促销方式*/
    _proto.getUrl_marketingcoupon_getCanUseList = function(){return this.serverUrl + '/marketingcoupon/getCanUseList';};
    /**添加商品的SPU列表*/
    _proto.getUrl_product_getSaleSPUBackPager = function(){return this.serverUrl + '/product/getSaleSPUBackPager';};
    /**添加商品的SKU列表*/
    _proto.getUrl_product_getSaleSKUAllList = function(){return this.serverUrl + '/product/getSaleSKUAllList';};
    /**创建微信退款订单*/
    _proto.getUrl_order_createWxRefundOrder = function(){return this.serverUrl + '/order/createWxRefundOrder';};
    /**微信退款*/
    _proto.getUrl_order_handleWxRefundOrder = function(){return this.serverUrl + '/order/handleWxRefundOrder';};
    /**微信条形码*/
    _proto.getUrl_order_handleWxBarcodeOrder = function(){return this.serverUrl + '/order/handleWxBarcodeOrder';};
    /**创建支付宝退款订单*/
    _proto.getUrl_order_createAliRefundOrder = function(){return this.serverUrl + '/order/createAliRefundOrder';};
    /**支付宝退款*/
    _proto.getUrl_order_handleAliRefundOrder = function(){return this.serverUrl + '/order/handleAliRefundOrder';};
    /**支付宝条形码*/
    _proto.getUrl_order_handleAliBarcodeOrder = function(){return this.serverUrl + '/order/handleAliBarcodeOrder';};
    //=======================销售开单 结束====================
    //=======================实时工作台 开始====================
    /**获取日常任务和奖励*/
    _proto.getUrl_user_taskReward = function(){return this.serverUrl + '/user/taskReward';};
    /**任务奖励*/
    _proto.getUrl_mission_getAllMission = function(){return this.serverUrl + '/mission/getAllMission';};
    /**实时工作台 -- 销售奖励*/
    _proto.getUrl_product_saleReward = function(){return this.serverUrl + '/product/saleReward';};
    /**实时工作台 -- 昨日销售额统计*/
    _proto.getUrl_statisticsSale_saleYesterday = function(){return this.serverUrl + '/statisticsSale/saleYesterday';};
    /**实时工作台 -- 今日销售额统计*/
    _proto.getUrl_statisticsSale_saleToday = function(){return this.serverUrl + '/statisticsSale/saleToday';};
    /**实时工作台 -- 本周销售额统计*/
    _proto.getUrl_statisticsSale_saleWeek = function(){return this.serverUrl + '/statisticsSale/saleWeek';};
    /**实时工作台 -- 本月销售额统计*/
    _proto.getUrl_statisticsSale_saleMonth = function(){return this.serverUrl + '/statisticsSale/saleMonth';};
    /**实时工作台 -- 查询销售额统计*/
    _proto.getUrl_statisticsSale_searchSale = function(){return this.serverUrl + '/statisticsSale/searchSale';};

    //=======================实时工作台 结束====================

    //=======================顾客管理 开始====================
    /**获取公司下门店列表*/
    _proto.getUrl_subordinate_getSubordinateStore = function(){return this.serverUrl + '/subordinate/getSubordinateStore';};
    /**获取门店下顾客的所有职业（公司下员工所有职位）*/
    _proto.getUrl_user_getAllUserJob = function(){return this.serverUrl + '/user/getAllUserJob';};
    /**顾客人数统计*/
    _proto.getUrl_user_statisticsOrderUser = function(){return this.serverUrl + '/user/statisticsOrderUser';};
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
    /**创建企业*/
    _proto.getUrl_subordinate_addSubordinate = function(){return this.serverUrl + '/subordinate/addSubordinate';};
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
    /**获取当前公司信息*/
    _proto.getUrl_subordinate_getProcessList = function(){return this.serverUrl + '/subordinate/getProcessList';};
    //=======================公司管理 结束====================

    //=======================支付设置 开始====================
    /**添加门店支付方式*/
    _proto.getUrl_pay_addPay = function(){return this.serverUrl + '/pay/addPay';};
    /**门店获取支付方式*/
    _proto.getUrl_pay_getAllListBySub = function(){return this.serverUrl + '/pay/getAllListBySub';};
    /**修改支付方式*/
    _proto.getUrl_pay_updatePay = function(){return this.serverUrl + '/pay/updatePay';};

    //=======================支付设置 结束====================

    //=======================员工管理 开始====================
    /**员工信息修改*/
    _proto.getUrl_user_updateUser = function(){return this.serverUrl + '/user/updateUser';};
    /**添加员工*/
    _proto.getUrl_user_add = function(){return this.serverUrl + '/user/add';};
    /**停用员工*/
    _proto.getUrl_user_updateStatus = function(){return this.serverUrl + '/user/updateStatus';};
    /**获取所有员工名字*/
    _proto.getUrl_user_getAllUser = function(){return this.serverUrl + '/user/getAllUser';};
    //=======================员工管理 结束====================

    //=======================工资模块 开始====================
    /**查询某公司工资单导入记录*/
    _proto.getUrl_salaryrecord_getAllList = function(){return this.serverUrl + '/salaryrecord/getAllList';};
    /**撤销导入*/
    _proto.getUrl_salaryrecord_revoke = function(){return this.serverUrl + '/salaryrecord/revoke';};
    /**查询某员工 工资单列表*/
    _proto.getUrl_salary_loadSalaryByUser = function(){return this.serverUrl + '/salary/loadSalaryByUser';};
    //=======================工资模块 结束====================

    //=======================企业端（营销管理） 开始====================
    /**新增短信群发*/
    _proto.getUrl_marketingtimingsms_add = function(){return this.serverUrl + '/marketingtimingsms/add';};
    /**获取短信群发列表*/
    _proto.getUrl_marketingtimingsms_getCheckPager = function(){return this.serverUrl + '/marketingtimingsms/getCheckPager';};
    /**编辑短信群发*/
    _proto.getUrl_marketingtimingsms_update = function(){return this.serverUrl + '/marketingtimingsms/update';};
    /**新增抵用卷*/
    _proto.getUrl_marketingcoupon_add = function(){return this.serverUrl + '/marketingcoupon/add';};
    /**编辑抵用卷*/
    _proto.getUrl_marketingcoupon_updateMarketing = function(){return this.serverUrl + '/marketingcoupon/updateMarketing';};
    /**启用/关闭抵用卷*/
    _proto.getUrl_marketingcoupon_updateOpen = function(){return this.serverUrl + '/marketingcoupon/updateOpen';};
    /**删除抵用卷*/
    _proto.getUrl_marketingcoupon_del = function(){return this.serverUrl + '/marketingcoupon/del';};
    /**获取抵用卷列表*/
    _proto.getUrl_marketingcoupon_getAllList = function(){return this.serverUrl + '/marketingcoupon/getAllList';};
    /**创建任务*/
    _proto.getUrl_mission_add = function(){return this.serverUrl + '/mission/add';};
    /**查询任务列表*/
    _proto.getUrl_mission_getAllList = function(){return this.serverUrl + '/mission/getAllList';};
    /**删除任务*/
    _proto.getUrl_mission_del = function(){return this.serverUrl + '/mission/del';};
    /**获取抵用卷列表*/
    _proto.getUrl_marketingcoupon_getAllList = function(){return this.serverUrl + '/marketingcoupon/getAllList';};
    /**获取抵用卷列表*/
    _proto.getUrl_mission_update = function(){return this.serverUrl + '/mission/update';};
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
    /**修改商品spu*/
    _proto.getUrl_product_change = function(){return this.serverUrl + '/product/change';};
    /**删除商品spu*/
    _proto.getUrl_product_delSPU = function(){return this.serverUrl + '/product/delSPU';};
    /**根据spu的id加载商品信息*/
    _proto.getUrl_product_loadSPU = function(){return this.serverUrl + '/product/loadSPU';};
    /**开启关闭销售状态*/
    _proto.getUrl_product_updateSaleStatus = function(){return this.serverUrl + '/product/updateSaleStatus';};

    /**获取所有出库单*/
    _proto.getUrl_inventoryOutBill_getCheckPager = function(){return this.serverUrl + '/inventoryOutBill/getCheckPager';};
    /**获取当前登陆用户创建的出库单*/
    _proto.getUrl_inventoryOutBill_getCreatePager = function(){return this.serverUrl + '/inventoryOutBill/getCreatePager';};
    /**获取一个商品的SPU，返回需要确定的所有SKU属性   出库*/
    _proto.getUrl_inventoryOutBill_select = function(){return this.serverUrl + '/inventoryOutBill/select';};
    /**添加一个出库单*/
    _proto.getUrl_inventoryOutBill_add = function(){return this.serverUrl + '/inventoryOutBill/add';};
    /**编辑出库单*/
    _proto.getUrl_inventoryOutBill_update = function(){return this.serverUrl + '/inventoryOutBill/update';};
    /**审核通过出库单*/
    _proto.getUrl_inventoryOutBill_pass = function(){return this.serverUrl + '/inventoryOutBill/pass';};
    /**删除出库单*/
    _proto.getUrl_inventoryOutBill_del = function(){return this.serverUrl + '/inventoryOutBill/del';};

    /**获取一个商品的SPU，返回需要确定的所有SKU属性   入库*/
    _proto.getUrl_inventoryInBill_select = function(){return this.serverUrl + '/inventoryinbill/select';};
    /**获取门店下的编辑状态的入库单列表*/
    _proto.getUrl_inventoryInBill_getCheckPager = function(){return this.serverUrl + '/inventoryinbill/getCheckPager';};
    /**获取当前登陆用户创建的入库单*/
    _proto.getUrl_inventoryInBill_getCreatePager = function(){return this.serverUrl + '/inventoryinbill/getCreatePager';};
    /**添加一个入库单*/
    _proto.getUrl_inventoryInBill_add = function(){return this.serverUrl + '/inventoryinbill/add';};
    /**修改入库单*/
    _proto.getUrl_inventoryInBill_update = function(){return this.serverUrl + '/inventoryinbill/update';};
    /**审核通过入库单*/
    _proto.getUrl_inventoryInBill_pass = function(){return this.serverUrl + '/inventoryinbill/pass';};
    /**审核通过入库单*/
    _proto.getUrl_productprovider_add = function(){return this.serverUrl + '/productprovider/add';};

    //=======================商品管理 结束====================


    //=======================盘点 开始====================
    /**获取门店盘点列表**/
    _proto.getUrl_inventoryCheckBill_getCheckPager = function(){return this.serverUrl + '/inventoryCheckBill/getCheckPager';};
    /**获取当前人创建的盘点列表**/
    _proto.getUrl_inventoryCheckBill_getCreatePager = function(){return this.serverUrl + '/inventoryCheckBill/getCreatePager';};
    /**查询盘点单**/
    _proto.getUrl_inventoryCheckBill_select = function(){return this.serverUrl + '/inventoryCheckBill/select';};
    /**新增盘点**/
    _proto.getUrl_inventoryCheckBill_add = function(){return this.serverUrl + '/inventoryCheckBill/add';};
    /**编辑盘点**/
    _proto.getUrl_inventoryCheckBill_update = function(){return this.serverUrl + '/inventoryCheckBill/update';};
    /**删除盘点**/
    _proto.getUrl_inventoryCheckBill_del = function(){return this.serverUrl + '/inventoryCheckBill/del';};
    /**完成盘点**/
    _proto.getUrl_inventoryCheckBill_end = function(){return this.serverUrl + '/inventoryCheckBill/end';};
    /**保存盘点**/
    _proto.getUrl_inventoryCheckBill_save = function(){return this.serverUrl + '/inventoryCheckBill/save';};

    //=======================盘点 结束====================
    //=======================调货 开始====================
    /**获取编辑状态下的调货单**/
    _proto.getUrl_inventoryInvokeBill_getCheckPager = function(){return this.serverUrl + '/inventoryInvokeBill/getCheckPager';};
    /**获取某个员工创建的调货单**/
    _proto.getUrl_inventoryInvokeBill_getCreatePager = function(){return this.serverUrl + '/inventoryInvokeBill/getCreatePager';};
    /**查询调货单**/
    _proto.getUrl_inventoryInvokeBill_select = function(){return this.serverUrl + '/inventoryInvokeBill/select';};
    /**新增调货单**/
    _proto.getUrl_inventoryInvokeBill_add = function(){return this.serverUrl + '/inventoryInvokeBill/add';};
    /**修改调货单**/
    _proto.getUrl_inventoryInvokeBill_update = function(){return this.serverUrl + '/inventoryInvokeBill/update';};
    /**审核通过调货单**/
    _proto.getUrl_inventoryInvokeBill_pass = function(){return this.serverUrl + '/inventoryInvokeBill/pass';};
    /**删除调货单**/
    _proto.getUrl_inventoryInvokeBill_del = function(){return this.serverUrl + '/inventoryInvokeBill/del';};

    //=======================调货 结束====================


    //=======================订单管理 开始====================
    //获取全部||作废订单
    _proto.getUrl_order_getAllOrder = function(){return this.serverUrl + '/order/getAllOrder';};
    //获取未完成订单
    _proto.getUrl_order_getIncomplete = function(){return this.serverUrl + '/order/getIncomplete';};
    //根据职位获取用户列表
    _proto.getUrl_user_getUsersByJob = function(){return this.serverUrl + '/user/getUsersByJob';};
    //获取订单详情
    _proto.getUrl_order_loadOrder = function(){return this.serverUrl + '/order/loadOrder';};
    //=======================订单管理 结束====================

    //=======================售后 开始====================
    /**获取售后记录*/
    _proto.getUrl_afterSale_getLogPager = function(){return this.serverUrl + '/afterSale/getLogPager';};
    /**查看售后*/
    _proto.getUrl_afterSale_loadDetails = function(){return this.serverUrl + '/afterSale/loadDetails';};
    /**进行售后*/
    _proto.getUrl_afterSale_add = function(){return this.serverUrl + '/afterSale/add';};
    //=======================售后 结束====================

    //=======================统计 开始====================
    /**顾客统计--时间范围*/
    _proto.getUrl_statisticsCustomer_statisticsByTime = function(){return this.serverUrl + '/statisticsCustomer/statisticsByTime';};
    /**顾客统计--本周*/
    _proto.getUrl_statisticsCustomer_statisticsByWeek = function(){return this.serverUrl + '/statisticsCustomer/statisticsByWeek';};
    /**顾客统计--本月*/
    _proto.getUrl_statisticsCustomer_statisticsByMonth = function(){return this.serverUrl + '/statisticsCustomer/statisticsByMonth';};
    /**顾客统计--近半年*/
    _proto.getUrl_statisticsCustomer_statisticsByHalfYear = function(){return this.serverUrl + '/statisticsCustomer/statisticsByHalfYear';};
    /**顾客统计--多店统计时间区间*/
    _proto.getUrl_statisticsCustomer_timeInterval = function(){return this.serverUrl + '/statisticsCustomer/timeInterval';};
    /**顾客统计--多店对比*/
    _proto.getUrl_statisticsCustomer_statisticsBySubordinates = function(){return this.serverUrl + '/statisticsCustomer/statisticsBySubordinates';};


    /**客单价-查询客单价占比*/
    _proto.getUrl_statisticsSaleCategory_saleSubordinates = function(){return this.serverUrl + '/statisticsSaleCategory/saleSubordinates';};

    /**销售额-多店对比查询销售额*/
    _proto.getUrl_statisticsSale_saleSubordinates = function(){return this.serverUrl + '/statisticsSale/saleSubordinates';};

    /**库存统计*/
    _proto.getUrl_statisticsInventory_saleSubordinates = function(){return this.serverUrl + '/statisticsInventory/saleSubordinates';};
    //=======================统计 结束====================



    Quakoo.class(Config,'Config',_super);


    return Config;
})(QuakooConfig);

