/**
 *  雀科科技- http://www.quakoo.com
 *
 *  业务配置类（继承父类：QuakooConfig）
 *
 *  放本业务相关的配置
 *
 */
var Config = (function(_super) {
    function Config() {
        Config.__super.call(this);

        this.isTest = false;
        /**
         * js部分使用的版本号，项目如果持续发布版本，记得更换版本号
         * @type {string}
         */
        this.version = "1.0.0";
        this.hotVersion = "201901020001";






        /**测试服务器地址*/
        // this.serverUrl = "http://39.107.247.82:20005";
        // this.serverUrl = "http://192.168.1.20:20005";
        /**李浩杰*/
        // this.serverUrl = "http://192.168.1.29:20005";
        /**张猛*/
        this.serverUrl = "http://192.168.1.5:20005";
        /**马文军*/
        // this.serverUrl = "http://127.0.0.1:20005";
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
        this.pushPort = '23333';
        /**用户信息存储的本地变量名*/
        this.userInfoName = 'user_info';
        /**用户信息存储时间的本地变量名*/
        this.userInfoTime = 'user_info_time';

        //=======================商品的属性的值列表 开始====================
        /**镜片-焦点*/
        this.jiaodianId = 1;
        /**镜片-折射率*/
        this.zheshelvId = 2;
        /**镜片-功能*/
        this.gongnengId = 3;
        /**镜架-质保时长*/
        this.jingjiaTimeId = 4;
        /**镜架-零售价*/
        this.jingjiaRealPriceId = 19;
        /**镜架-成本价*/
        this.jingjiaCostPriceId = 20;
        /**隐形眼镜-类型*/
        this.yinxingLeixingId = 5;
        /**更换周期*/
        this.zhoouqiId = 6;
        /**隐形眼镜-包装*/
        this.yinxingBaozhuangId = 7;
        /**隐形眼镜-质保时长*/
        this.yinxingTimeId = 8;
        /**太阳镜-质保时长*/
        this.taiyangTimeId = 9;
        /**太阳镜-零售价*/
        this.taiyangjingRealPriceId = 21;
        /**太阳镜-成本价*/
        this.taiyangjingCostPriceId = 22;
        /**护理产品-类型*/
        this.huliLeixingId = 10;
        /**护理产品-包装*/
        this.huliBaozhuang = 11;
        /**护理产品-开瓶寿命/天*/
        this.shoumingId = 12;
        /**护理产品-质保时长*/
        this.huliTimeId = 13;
        /**护理产品-零售价*/
        this.hulishangpinRealPriceId = 23;
        /**护理产品-成本价*/
        this.hulishangpinCostPriceId = 24;
        /**其他商品-质保时长*/
        this.qitaTimeId = 14;
        /**其他商品-零售价*/
        this.qitashangpinRealPriceId = 27;
        /**其他商品-成本价*/
        this.qitashangpinCostPriceId = 28;
        /**特殊商品-成本价*/
        this.teshushangpinCostPriceId = 30;
        /**特殊商品-名称*/
        this.teshushangpinNameId = 29;
        /**球*/
        this.qiuId = 15;
        /**柱*/
        this.zhuId = 16;
        //=======================商品的属性的值列表 结束====================

    }
    var _proto = _super.prototype;
    /**登陆*/
    _proto.getUrl_web_user_loginUrl = function() { return this.serverUrl + '/login/in'; };
    /**登陆*/
    _proto.getUrl_web_user_getTree = function() { return this.serverUrl + '/user/getTree'; };
    /**登陆验证码*/
    _proto.getUrl_web_user_loginCodeUrl = function() { return this.serverUrl + '/login/verifyCode' };


    //=======================销售开单 开始====================
    /**顾客姓名和手机号精确查询*/
    _proto.getUrl_user_searchUserList = function() { return this.serverUrl + '/user/searchUserList'; };
    /**获取临时订单*/
    _proto.getUrl_order_getTemporaryOrder = function() { return this.serverUrl + '/businessOrder/getTemporaryOrder'; };
    /**根据手机和姓名 查找门店下的推荐人*/
    _proto.getUrl_user_getUserByNamePhone = function() { return this.serverUrl + '/user/getUserByNamePhone'; };
    /**保存验光信息*/
    _proto.getUrl_optometryinfo_add = function() { return this.serverUrl + '/optometryinfo/add'; };
    /**获取某个用户的验光记录*/
    _proto.getUrl_optometryinfo_getList = function() { return this.serverUrl + '/optometryinfo/getList'; };
    /**获取门店历史消费记录*/
    _proto.getUrl_order_getOrderBySubid = function() { return this.serverUrl + '/order/getOrderBySubid'; };
    /**计算订单金额*/
    _proto.getUrl_businessOrder_currentCalculate = function() { return this.serverUrl + '/businessOrder/currentCalculate'; };
    /**保存订单*/
    _proto.getUrl_businessOrder_saveOrder = function() { return this.serverUrl + '/businessOrder/saveOrder'; };
    /**修改验光信息*/
    _proto.getUrl_optometryinfo_update = function() { return this.serverUrl + '/optometryinfo/update'; };
    /**更多验光记录*/
    _proto.getUrl_optometryinfo_getList = function() { return this.serverUrl + '/optometryinfo/getList'; };
    /**根据手机号查询用户信息*/
    _proto.getUrl_user_getUserByPhone = function() { return this.serverUrl + '/user/getUserByPhone'; };
    /**会员信息认证 或 注册会员*/
    _proto.getUrl_user_becomeVip = function() { return this.serverUrl + '/user/becomeVip'; };
    /**检查手机号验证码*/
    _proto.getUrl_user_checkCode = function() { return this.serverUrl + '/user/checkCode'; };
    /**发送验证码*/
    _proto.getUrl_user_createAuthCodeOnReg = function() { return this.serverUrl + '/user/createAuthCodeOnReg'; };
    /**根据职位获取用户列表*/
    _proto.getUrl_user_getUsersByJob = function() { return this.serverUrl + '/user/getUsersByJob'; };
    /**获取 验光结果建议*/
    _proto.getUrl_optometryinfo_getResult = function() { return this.serverUrl + '/optometryinfo/getResult'; };
    /**附加费用*/
    _proto.getUrl_order_getSurcharge = function() { return this.serverUrl + '/businessOrder/getSurcharge'; };
    /**促销方式*/
    _proto.getUrl_marketingcoupon_getCanUseList = function() { return this.serverUrl + '/marketingcoupon/getCanUseList'; };
    /**添加商品的SPU列表*/
    _proto.getUrl_product_getSaleSPUBackPager = function() { return this.serverUrl + '/product/getSaleSPUBackPager'; };
    /**添加商品的SKU列表*/
    _proto.getUrl_product_getSaleSKUAllList = function() { return this.serverUrl + '/product/getSaleSKUAllList'; };
    /**计算优惠金额*/
    _proto.getUrl_marketingcoupon_calculateMoney = function() { return this.serverUrl + '/marketingcoupon/calculateMoney'; };
    /**获取商品会员折扣*/
    _proto.getUrl_gradeDiscount_load = function() { return this.serverUrl + '/gradeDiscount/load'; };
    /**获取会员详情*/
    _proto.getUrl_usergrade_load = function() { return this.serverUrl + '/usergrade/load'; };
    /**创建微信退款订单*/
    _proto.getUrl_order_createWxRefundOrder = function() { return this.serverUrl + '/order/createWxRefundOrder'; };
    /**微信退款*/
    _proto.getUrl_order_handleWxRefundOrder = function() { return this.serverUrl + '/order/handleWxRefundOrder'; };
    /**微信条形码*/
    _proto.getUrl_order_handleWxBarcodeOrder = function() { return this.serverUrl + '/order/handleWxBarcodeOrder'; };
    /**创建支付宝退款订单*/
    _proto.getUrl_order_createAliRefundOrder = function() { return this.serverUrl + '/order/createAliRefundOrder'; };
    /**支付宝退款*/
    _proto.getUrl_order_handleAliRefundOrder = function() { return this.serverUrl + '/order/handleAliRefundOrder'; };
    /**支付宝条形码*/
    _proto.getUrl_order_handleAliBarcodeOrder = function() { return this.serverUrl + '/order/handleAliBarcodeOrder'; };
    /**支付结算*/
    _proto.getUrl_businessOrder_settlementPay = function() { return this.serverUrl + '/businessOrder/settlementPay'; };
    /**订单结算*/
    _proto.getUrl_businessOrder_settlementOrder = function() { return this.serverUrl + '/businessOrder/settlementOrder'; };
    //=======================销售开单 结束====================
    //=======================实时工作台 开始====================
    /**获取日常任务和奖励*/
    _proto.getUrl_user_taskReward = function() { return this.serverUrl + '/user/taskReward'; };
    /**任务奖励*/
    _proto.getUrl_mission_getAllMission = function() { return this.serverUrl + '/mission/getAllMission'; };
    /**实时工作台 -- 销售奖励*/
    _proto.getUrl_product_saleReward = function() { return this.serverUrl + '/product/saleReward'; };
    /**实时工作台 -- 昨日销售额统计*/
    _proto.getUrl_statisticsSale_saleYesterday = function() { return this.serverUrl + '/statisticsSale/saleYesterday'; };
    /**实时工作台 -- 今日销售额统计*/
    _proto.getUrl_statisticsSale_saleToday = function() { return this.serverUrl + '/statisticsSale/saleToday'; };
    /**实时工作台 -- 本周销售额统计*/
    _proto.getUrl_statisticsSale_saleWeek = function() { return this.serverUrl + '/statisticsSale/saleWeek'; };
    /**实时工作台 -- 本月销售额统计*/
    _proto.getUrl_statisticsSale_saleMonth = function() { return this.serverUrl + '/statisticsSale/saleMonth'; };
    /**实时工作台 -- 查询销售额统计*/
    _proto.getUrl_statisticsSale_searchSale = function() { return this.serverUrl + '/statisticsSale/searchSale'; };

    //=======================实时工作台 结束====================
    //=======================资金管理 开始====================
    /**资金管理 -- 获取结算记录*/
    _proto.getUrl_settlement_getPagerLog = function() { return this.serverUrl + '/settlement/getPagerLog'; };
    /**资金管理 -- 获取最新的一次结算*/
    _proto.getUrl_settlement_loadClient = function() { return this.serverUrl + '/settlement/loadClient'; };
    /**资金管理 -- 进行结算*/
    _proto.getUrl_settlement_add = function() { return this.serverUrl + '/settlement/add'; };
    /**资金管理 -- 获取金额*/
    _proto.getUrl_businessOrder_calculateOrders = function() { return this.serverUrl + '/businessOrder/calculateOrders'; };
    /**资金管理 -- 获取金额*/
    _proto.getUrl_settlement_getFinanceLogs = function() { return this.serverUrl + '/settlement/getFinanceLogs'; };

    //=======================资金管理 结束====================
    //=======================顾客管理 开始====================
    /**获取公司下门店列表*/
    _proto.getUrl_subordinate_getSubordinateStore = function() { return this.serverUrl + '/subordinate/getSubordinateStore'; };
    /**获取门店下顾客的所有职业（公司下员工所有职位）*/
    _proto.getUrl_user_getAllUserJob = function() { return this.serverUrl + '/user/getAllUserJob'; };
    /**顾客人数统计*/
    _proto.getUrl_user_statisticsOrderUser = function() { return this.serverUrl + '/user/statisticsOrderUser'; };
    /**添加顾客*/
    _proto.getUrl_user_addCustomer = function() { return this.serverUrl + '/user/addCustomer'; };
    /**获取所属公司的所有顾客*/
    _proto.getUrl_user_getCustomerPager = function() { return this.serverUrl + '/user/getCustomerPager'; };
    /**查询某个门店下的所有会员级别列表*/
    _proto.getUrl_usergrade_getAllList = function() { return this.serverUrl + '/usergrade/getAllList'; };
    /**获取所属门店的所有顾客*/
    _proto.getUrl_user_getSubCustomerPager = function() { return this.serverUrl + '/user/getSubCustomerPager'; };
    /**修改顾客信息*/
    _proto.getUrl_user_updateCustomer = function() { return this.serverUrl + '/user/updateCustomer'; };
    /**手机号模糊查询*/
    _proto.getUrl_user_getUserListByPhone = function() { return this.serverUrl + '/user/getUserListByPhone'; };

    //=======================顾客管理 结束====================

    //=======================会员等级 开始====================
    /**查询某个门店下的所有会员级别列表（查询会员列表）*/
    _proto.getUrl_usergrade_getAllList = function() { return this.serverUrl + '/usergrade/getAllList'; };
    /**添加会员级别*/
    _proto.getUrl_usergrade_add = function() { return this.serverUrl + '/usergrade/add'; };
    /**编辑会员级别信息*/
    _proto.getUrl_usergrade_update = function() { return this.serverUrl + '/usergrade/update'; };
    /**删除当前会员级别*/
    _proto.getUrl_usergrade_del = function() { return this.serverUrl + '/usergrade/del'; };
    //=======================会员等级 结束====================

    //=======================公司管理 开始====================
    /**根据名称搜索门店*/
    _proto.getUrl_subordinate_getSubordinateStoreByName = function() { return this.serverUrl + '/subordinate/getSubordinateStoreByName'; };
    /**创建企业*/
    _proto.getUrl_subordinate_addSubordinate = function() { return this.serverUrl + '/subordinate/addSubordinate'; };
    /**修改企业信息*/
    _proto.getUrl_subordinate_updateSubordinate = function() { return this.serverUrl + '/subordinate/updateSubordinate'; };
    /**修改企业下门店信息*/
    _proto.getUrl_subordinate_updateSubordinateStore = function() { return this.serverUrl + '/subordinate/updateSubordinateStore'; };
    /**企业下创建门店*/
    _proto.getUrl_subordinate_addSubordinateStore = function() { return this.serverUrl + '/subordinate/addSubordinateStore'; };
    /**开启/关闭门店*/
    _proto.getUrl_subordinate_updateOpen = function() { return this.serverUrl + '/subordinate/updateOpen'; };
    /**获取当前公司信息*/
    _proto.getUrl_subordinate_getSubordinate = function() { return this.serverUrl + '/subordinate/getSubordinate'; };
    /**获取当前公司信息*/
    _proto.getUrl_subordinate_getProcessList = function() { return this.serverUrl + '/subordinate/getProcessList'; };
    //=======================公司管理 结束====================

    //=======================支付设置 开始====================
    /**添加门店支付方式*/
    _proto.getUrl_pay_addPay = function() { return this.serverUrl + '/pay/addPay'; };
    /**门店获取支付方式*/
    _proto.getUrl_pay_getAllListBySub = function() { return this.serverUrl + '/pay/getAllListBySub'; };
    /**修改支付方式*/
    _proto.getUrl_pay_updatePay = function() { return this.serverUrl + '/pay/updatePay'; };

    //=======================支付设置 结束====================

    //=======================员工管理 开始====================
    /**员工信息修改*/
    _proto.getUrl_user_updateUser = function() { return this.serverUrl + '/user/updateUser'; };
    /**添加员工*/
    _proto.getUrl_user_add = function() { return this.serverUrl + '/user/add'; };
    /**停用员工*/
    _proto.getUrl_user_updateStatus = function() { return this.serverUrl + '/user/updateStatus'; };
    /**获取所有员工名字*/
    _proto.getUrl_user_getAllUser = function() { return this.serverUrl + '/user/getAllUser'; };
    //=======================员工管理 结束====================

    //=======================工资模块 开始====================
    /**查询某公司工资单导入记录*/
    _proto.getUrl_salaryrecord_getAllList = function() { return this.serverUrl + '/salaryrecord/getAllList'; };
    /**撤销导入*/
    _proto.getUrl_salaryrecord_revoke = function() { return this.serverUrl + '/salaryrecord/revoke'; };
    /**查询某员工 工资单列表*/
    _proto.getUrl_salary_loadSalaryByUser = function() { return this.serverUrl + '/salary/loadSalaryByUser'; };
    //=======================工资模块 结束====================

    //=======================企业端（营销管理） 开始====================
    /**新增短信群发*/
    _proto.getUrl_marketingtimingsms_add = function() { return this.serverUrl + '/marketingtimingsms/add'; };
    /**获取短信群发列表*/
    _proto.getUrl_marketingtimingsms_getCheckPager = function() { return this.serverUrl + '/marketingtimingsms/getCheckPager'; };
    /**编辑短信群发*/
    _proto.getUrl_marketingtimingsms_update = function() { return this.serverUrl + '/marketingtimingsms/update'; };
    /**新增抵用卷*/
    _proto.getUrl_marketingcoupon_add = function() { return this.serverUrl + '/marketingcoupon/add'; };
    /**编辑抵用卷*/
    _proto.getUrl_marketingcoupon_updateMarketing = function() { return this.serverUrl + '/marketingcoupon/updateMarketing'; };
    /**启用/关闭抵用卷*/
    _proto.getUrl_marketingcoupon_updateOpen = function() { return this.serverUrl + '/marketingcoupon/updateOpen'; };
    /**删除抵用卷*/
    _proto.getUrl_marketingcoupon_del = function() { return this.serverUrl + '/marketingcoupon/del'; };
    /**获取抵用卷列表*/
    _proto.getUrl_marketingcoupon_getAllList = function() { return this.serverUrl + '/marketingcoupon/getAllList'; };
    /**创建任务*/
    _proto.getUrl_mission_add = function() { return this.serverUrl + '/mission/add'; };
    /**查询任务列表*/
    _proto.getUrl_mission_getAllList = function() { return this.serverUrl + '/mission/getAllList'; };
    /**删除任务*/
    _proto.getUrl_mission_del = function() { return this.serverUrl + '/mission/del'; };
    /**获取抵用卷列表*/
    _proto.getUrl_marketingcoupon_getAllList = function() { return this.serverUrl + '/marketingcoupon/getAllList'; };
    /**获取抵用卷列表*/
    _proto.getUrl_mission_update = function() { return this.serverUrl + '/mission/update'; };
    /**设置短信营销（待定）*/
    //_proto.getUrl_subordinate_getSu = function(){return this.serverUrl + '/marketingtimingsms/add';};
    /**编辑短信营销(待定)*/
    //_proto.getUrl_subordinate_getSu = function(){return this.serverUrl + '/marketingtimingsms/add';};
    //=======================企业端（营销管理） 结束====================

    //=======================考勤管理 开始====================
    /**获取员工考勤列表*/
    _proto.getUrl_attendance_getEmployAttendance = function() { return this.serverUrl + '/attendance/statistics'; };
    /**员工考勤详情*/
    _proto.getUrl_attendance_getEmployAttendanceList = function() { return this.serverUrl + '/attendance/getAllByUid'; };
    /**请假列表*/
    _proto.getUrl_attendance_getLeaveList = function() { return this.serverUrl + '/leave/getList'; };
    /**编辑请假*/
    _proto.getUrl_attendance_updateLeaveList = function() { return this.serverUrl + '/leave/update'; };
    /**加班列表*/
    _proto.getUrl_attendance_getAddList = function() { return this.serverUrl + '/workovertime/getList'; };
    /**调班列表*/
    _proto.getUrl_attendance_getChangeList = function() { return this.serverUrl + '/changeShift/getList'; };
    /**补卡列表*/
    _proto.getUrl_attendance_getBkaList = function() { return this.serverUrl + '/fillCard/getAllList'; };

    //=======================考勤管理 结束====================

    //=======================商品管理 开始====================
    /**获取商品的属性的值列表*/
    _proto.getUrl_productPropertyvalue_getAllList = function() { return this.serverUrl + '/productpropertyvalue/getAllList'; };
    /**获取企业类目列表*/
    _proto.getUrl_productCategory_getSubAllList = function() { return this.serverUrl + '/productcategory/getSubAllList'; };
    /**获取供应商*/
    _proto.getUrl_productProvider_getSubAllList = function() { return this.serverUrl + '/productprovider/getSubAllList'; };
    /**获取门店下的品牌*/
    _proto.getUrl_productBrand_getSubAllList = function() { return this.serverUrl + '/productbrand/getAllList'; };
    /**获取品牌下系列列表*/
    _proto.getUrl_productSeries_getSubAllList = function() { return this.serverUrl + '/productseries/getAllList'; };
    /**获取商品列表*/
    _proto.getUrl_product_getSPUPager = function() { return this.serverUrl + '/product/getSPUPager'; };
    /**增加商品*/
    _proto.getUrl_product_add = function() { return this.serverUrl + '/product/add'; };
    /**修改商品spu*/
    _proto.getUrl_product_change = function() { return this.serverUrl + '/product/change'; };
    /**删除商品spu*/
    _proto.getUrl_product_delSPU = function() { return this.serverUrl + '/product/delSPU'; };
    /**根据spu的id加载商品信息*/
    _proto.getUrl_product_loadSPU = function() { return this.serverUrl + '/product/loadSPU'; };
    /**开启关闭销售状态*/
    _proto.getUrl_product_updateSaleStatus = function() { return this.serverUrl + '/product/updateSaleStatus'; };

    /**获取所有出库单*/
    _proto.getUrl_inventoryOutBill_getCheckPager = function() { return this.serverUrl + '/inventoryOutBill/getCheckPager'; };
    /**获取当前登陆用户创建的出库单*/
    _proto.getUrl_inventoryOutBill_getCreatePager = function() { return this.serverUrl + '/inventoryOutBill/getCreatePager'; };
    /**获取一个商品的SPU，返回需要确定的所有SKU属性   出库*/
    _proto.getUrl_inventoryOutBill_select = function() { return this.serverUrl + '/inventoryOutBill/select'; };
    /**添加一个出库单*/
    _proto.getUrl_inventoryOutBill_add = function() { return this.serverUrl + '/inventoryOutBill/add'; };
    /**编辑出库单*/
    _proto.getUrl_inventoryOutBill_update = function() { return this.serverUrl + '/inventoryOutBill/update'; };
    /**审核通过出库单*/
    _proto.getUrl_inventoryOutBill_pass = function() { return this.serverUrl + '/inventoryOutBill/pass'; };
    /**删除出库单*/
    _proto.getUrl_inventoryOutBill_del = function() { return this.serverUrl + '/inventoryOutBill/del'; };

    /**获取一个商品的SPU，返回需要确定的所有SKU属性   入库*/
    _proto.getUrl_inventoryInBill_select = function() { return this.serverUrl + '/inventoryinbill/select'; };
    /**获取门店下的编辑状态的入库单列表*/
    _proto.getUrl_inventoryInBill_getCheckPager = function() { return this.serverUrl + '/inventoryinbill/getCheckPager'; };
    /**获取当前登陆用户创建的入库单*/
    _proto.getUrl_inventoryInBill_getCreatePager = function() { return this.serverUrl + '/inventoryinbill/getCreatePager'; };
    /**添加一个入库单*/
    _proto.getUrl_inventoryInBill_add = function() { return this.serverUrl + '/inventoryinbill/add'; };
    /**修改入库单*/
    _proto.getUrl_inventoryInBill_update = function() { return this.serverUrl + '/inventoryinbill/update'; };
    /**审核通过入库单*/
    _proto.getUrl_inventoryInBill_pass = function() { return this.serverUrl + '/inventoryinbill/pass'; };
    /**审核通过入库单*/
    _proto.getUrl_productprovider_add = function() { return this.serverUrl + '/productprovider/add'; };

    /**医疗器械*/
    /**添加供应商/生产商企业*/
    _proto.getUrl_medicalAppliances_addgyshang = function() { return this.serverUrl + '/company/saveCompany'; };
    /**编辑供应商/生产商企业*/
    _proto.getUrl_medicalAppliances_update = function() { return this.serverUrl + '/company/update'; };
    /**获取经营范围*/
    _proto.getUrl_medicalAppliances_getRange = function() { return this.serverUrl + '/company/getRange'; };
    /**获取供应商/生产商企业列表*/
    _proto.getUrl_medicalAppliances_getgyshang = function() { return this.serverUrl + '/company/search'; };
    /**获取过期的生产/供应商企业*/
    _proto.getUrl_medicalAppliances_getguoqiGshang = function() { return this.serverUrl + '/company/searchOverdue'; };
    /**获取生产商/供应商(添加商品下拉框)*/
    _proto.getUrl_medicalAppliances_getgongSheng = function() { return this.serverUrl + '/company/getMp'; };
    /**保存员工培训/体检记录*/
    _proto.getUrl_medicalAppliances_addtjReload = function() { return this.serverUrl + '/userLog/add'; };
    /**获取员工体检/培训记录*/
    _proto.getUrl_medicalAppliances_getPtReload = function() { return this.serverUrl + '/userLog/getInfo'; };
    /**获取所有公司和公司下所有的员工*/
    _proto.getUrl_medicalAppliances_getAllGongSiInfo = function() { return this.serverUrl + '/user/getUserByPsid'; };
    /**获取审核状态*/
    _proto.getUrl_medicalAppliances_sHeTongGuo = function() { return this.serverUrl + '/company/checkStatus'; };
    /**获取未补充证明的商品*/
    _proto.getUrl_medicalAppliances_getNotBuCSMShop = function() { return this.serverUrl + '/product/getNoNirNum'; };
    /**审核医疗器械新增商品(商品列表)*/
    _proto.getUrl_medicalAppliances_checkAddStatus = function(){return this.serverUrl + '/product/checkStatus';};
    /**获取医疗器械所有商品*/
    _proto.getUrl_product_getELSPUPager = function(){return this.serverUrl + '/product/getELSPUPager';};
    /**获取医疗器械销售商品列表*/
    _proto.getUrl_medicalAppliances_getXiaoShouReload = function(){return this.serverUrl + '/businessOrder/getMedicalAllList';};
    /**获取医疗器械出库记录（退货和销毁）*/
    _proto.getUrl_medicalAppliances_getTuiHuoReload = function(){return this.serverUrl + '/inventoryOutBill/getAllPager';};
    /**获取医疗器械入库记录*/
    _proto.getUrl_medicalAppliances_getRuKuReload = function(){return this.serverUrl + '/inventoryinbill/getAllPager';};
    /**销毁/退货医疗器械*/
    _proto.getUrl_medicalAppliances_inventoryOutBill = function(){return this.serverUrl + '/product/updateSpuStatus';};
    /**审核通过入库单*/
    _proto.getUrl_medicalAppliances_rukuInventoryinbill = function(){return this.serverUrl + '/inventoryinbill/pass';};
    //=======================商品管理 结束====================


    //=======================盘点 开始====================
    /**获取门店盘点列表**/
    _proto.getUrl_inventoryCheckBill_getCheckPager = function() { return this.serverUrl + '/inventoryCheckBill/getCheckPager'; };
    /**获取当前人创建的盘点列表**/
    _proto.getUrl_inventoryCheckBill_getCreatePager = function() { return this.serverUrl + '/inventoryCheckBill/getCreatePager'; };
    /**查询盘点单**/
    _proto.getUrl_inventoryCheckBill_select = function() { return this.serverUrl + '/inventoryCheckBill/select'; };
    /**新增盘点**/
    _proto.getUrl_inventoryCheckBill_add = function() { return this.serverUrl + '/inventoryCheckBill/add'; };
    /**编辑盘点**/
    _proto.getUrl_inventoryCheckBill_update = function() { return this.serverUrl + '/inventoryCheckBill/update'; };
    /**删除盘点**/
    _proto.getUrl_inventoryCheckBill_del = function() { return this.serverUrl + '/inventoryCheckBill/del'; };
    /**完成盘点**/
    _proto.getUrl_inventoryCheckBill_end = function() { return this.serverUrl + '/inventoryCheckBill/end'; };
    /**保存盘点**/
    _proto.getUrl_inventoryCheckBill_save = function() { return this.serverUrl + '/inventoryCheckBill/save'; };

    //=======================盘点 结束====================
    //=======================调货 开始====================
    /**获取编辑状态下的调货单**/
    _proto.getUrl_inventoryInvokeBill_getCheckPager = function() { return this.serverUrl + '/inventoryInvokeBill/getCheckPager'; };
    /**获取某个员工创建的调货单**/
    _proto.getUrl_inventoryInvokeBill_getCreatePager = function() { return this.serverUrl + '/inventoryInvokeBill/getCreatePager'; };
    /**查询调货单**/
    _proto.getUrl_inventoryInvokeBill_select = function() { return this.serverUrl + '/inventoryInvokeBill/select'; };
    /**新增调货单**/
    _proto.getUrl_inventoryInvokeBill_add = function() { return this.serverUrl + '/inventoryInvokeBill/add'; };
    /**修改调货单**/
    _proto.getUrl_inventoryInvokeBill_update = function() { return this.serverUrl + '/inventoryInvokeBill/update'; };
    /**审核通过调货单**/
    _proto.getUrl_inventoryInvokeBill_pass = function() { return this.serverUrl + '/inventoryInvokeBill/pass'; };
    /**删除调货单**/
    _proto.getUrl_inventoryInvokeBill_del = function() { return this.serverUrl + '/inventoryInvokeBill/del'; };

    //=======================调货 结束====================


    //=======================订单管理 开始====================
    //获取全部||作废订单
    _proto.getUrl_order_getAllOrder = function() { return this.serverUrl + '/businessOrder/getAllList'; };
    //获取未完成订单
    _proto.getUrl_order_getIncomplete = function() { return this.serverUrl + '/businessOrder/getIncomplete'; };
    //根据职位获取用户列表
    _proto.getUrl_user_getUsersByJob = function() { return this.serverUrl + '/user/getUsersByJob'; };
    //获取订单详情
    _proto.getUrl_order_loadOrder = function() { return this.serverUrl + '/businessOrder/loadOrder'; };
    //=======================订单管理 结束====================

    //=======================售后 开始====================
    /**获取售后记录*/
    _proto.getUrl_afterSale_getLogPager = function() { return this.serverUrl + '/afterSale/getLogPager'; };
    /**查看售后*/
    _proto.getUrl_afterSale_loadDetails = function() { return this.serverUrl + '/afterSale/loadDetails'; };
    /**进行售后*/
    _proto.getUrl_afterSale_add = function() { return this.serverUrl + '/afterSale/add'; };
    //=======================售后 结束====================

    //=======================统计 开始====================
    /**顾客统计--时间范围*/
    _proto.getUrl_statisticsCustomer_statisticsByTime = function() { return this.serverUrl + '/statisticsCustomer/statisticsByTime'; };
    /**顾客统计--本周*/
    _proto.getUrl_statisticsCustomer_statisticsByWeek = function() { return this.serverUrl + '/statisticsCustomer/statisticsByWeek'; };
    /**顾客统计--本月*/
    _proto.getUrl_statisticsCustomer_statisticsByMonth = function() { return this.serverUrl + '/statisticsCustomer/statisticsByMonth'; };
    /**顾客统计--近半年*/
    _proto.getUrl_statisticsCustomer_statisticsByHalfYear = function() { return this.serverUrl + '/statisticsCustomer/statisticsByHalfYear'; };
    /**顾客统计--多店统计时间区间*/
    _proto.getUrl_statisticsCustomer_timeInterval = function() { return this.serverUrl + '/statisticsCustomer/timeInterval'; };
    /**顾客统计--多店对比*/
    _proto.getUrl_statisticsCustomer_statisticsBySubordinates = function() { return this.serverUrl + '/statisticsCustomer/statisticsBySubordinates'; };


    /**客单价-查询客单价占比*/
    _proto.getUrl_statisticsSaleCategory_saleSubordinates = function() { return this.serverUrl + '/statisticsSaleCategory/saleSubordinates'; };

    /**销售额-多店对比查询销售额*/
    _proto.getUrl_statisticsSale_saleSubordinates = function() { return this.serverUrl + '/statisticsSale/saleSubordinates'; };

    /**库存统计*/
    _proto.getUrl_statisticsInventory_saleSubordinates = function() { return this.serverUrl + '/statisticsInventory/saleSubordinates'; };
    //=======================统计 结束====================




    //=======================后台管理品牌系列 开始====================
    /**品牌*/
    _proto.getUrl_productbrand_add = function() { return this.serverUrl + '/productbrand/add'; };
    _proto.getUrl_productbrand_getAllList = function() { return this.serverUrl + '/productbrand/getAllList'; };
    _proto.getUrl_productbrand_update = function() { return this.serverUrl + '/productbrand/update'; };
    _proto.getUrl_productbrand_del = function() { return this.serverUrl + '/productbrand/del'; };
    /**系列*/
    _proto.getUrl_productseries_add = function() { return this.serverUrl + '/productseries/add'; };
    _proto.getUrl_productseries_getAllList = function() { return this.serverUrl + '/productseries/getAllList'; };
    _proto.getUrl_productseries_update = function() { return this.serverUrl + '/productseries/update'; };
    _proto.getUrl_productseries_del = function() { return this.serverUrl + '/productseries/del'; };


    //=======================后台管理品牌系列 结束====================


    //=======================营销管理 开始====================

      //买赠活动--获取进行中的买赠活动
      _proto.getUrl_salesManage_buygive_getIngList = function (){return this.serverUrl + '/salePresent/getIngList';}
      //买赠活动--获取历史的买赠活动接口
      _proto.getUrl_salesManage_buygive_getHistoryList = function (){return this.serverUrl + '/salePresent/getHistoryList';}
      //买赠活动--新增
    _proto.getUrl_salesManage_buygive_add = function (){return this.serverUrl + '/salePresent/add';}
     //买赠活动--删除
     _proto.getUrl_salesManage_buygive_del = function (){return this.serverUrl + '/salePresent/del';}
      //买赠活动--修改
    _proto.getUrl_salesManage_buygive_update = function (){return this.serverUrl + '/salePresent/update';}
     //买赠活动--修改状态
     _proto.getUrl_salesManage_buygive_updateOpen = function (){return this.serverUrl + '/salePresent/updateOpen';}

    //=======================营销管理 结束====================


    Quakoo.class(Config, 'Config', _super);


    return Config;
})(QuakooConfig);
