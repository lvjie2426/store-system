/**购物车数据
 * 金额存储全部是分（传给后台全部是分），展示时做元的计算
 * */
var locShopCart= {
    id:2,
    saleUid:1,//销售员ID
    machinistId:1,//加工师id
    desc:"神秘法师地方",//订单备注
    goodsNumber:12,//商品总个数
    originalPrice:100,//订单原价共计
    discountPrice:80,//折后金额
    collectPrice:60,//实收金额
    bossDiscount:7.0,//最终折扣
    reductionPrice:100,//特惠减免
    totalPrice:2341234,//老马用
    couponId:2,//抵用券ID
    couponArr:[{ //促销方式（目前只能选择一个）
        id:23,
        name:"daf",
        price:12
    }],
    skuidsList:[{//商品列表
        spuid:23,
        id:1,//skuid
        code:1008,//产品编码
        subtotal:10,//小计
        num:10,//数量
        retailPrice:100,//零售价
        discount:4,//商品会员折扣
        canUseNum:12,//库存
    }],
    valueCard:[{//储值卡
        name:"储值卡",
        price:120000
    }],
    surchargesJson:[{//附加费用
        name:"加工费",
        price:13
    }],

};
/**结算清单*/
var settlementList = {
    noPayNum:23,//未支付
    ali: {//支付宝支付
        money:2,
        id:23,
        status:1//1已支付&2已退款3未支付
    },
    weixin: {//微信支付
        money:34,
        id:23,
        status: 1//1已支付&2已退款3未支付
    },
    cash:0,//现金
    bankCard:2342,//银行卡
    integral:{hasNum:23,usedNum:23},//积分
    valueCard:{hasNum:23,usedNum:23},//储值卡
    otherValue:{//他人储值卡
        type:1,//1是未验证，2是已验证
        hasNum:32,
        usedNum:23
    },
    giftVouchers:34,//礼品券
    total:1000000,//总计
    desc:"小票备注",//小票备注
    pickupStatus:1,//取货状态（是否勾选未取货）（1=>没有勾选，2=>勾选未取货）
};
/**小票数据(目前是提交订单的所有数据)*/
var receiptInfo = {
    id:23,//订单ID
    subName:"青稞眼镜责任有限公司",
    cTime:324,//下单时间
    orderNo:123234532,//订单号
    staffName:"麝隳笊",//销售员
    status:0,//0未缴费，1已交费，2已退款



};
/**添加商品数据*/
var locOrder = {
    type: 1,//按钮传值，1(常规商品) 2(积分商品);
    class:1,//类目ID，（镜片，镜架等）
    brand:1,//品牌ID
};
/**顾客信息*/
var cusLocInfo = {
    id:23,//顾客id

};
/**验光信息*/
var ygInfo = {
    now: {
        id:1,
    }
};

/**
 * 商品管理
 * @type {{}}
 */
var goodsManageData = {
    cid : 2,//类目ID；
    pid : 2,//供应商ID
    bid : 23,//品牌iID
    sid : 23,//系列ID
    name : "",//搜索关键字
    saleStatus : 0,//筛选条件的销售状态，（0=>开启）（1=>关闭）
    supplierLocData : {},//供应商数据
    priceCheck : [],//设定加个选中的单元格
    ballData : '',//球数据
    columnData : '',//柱数据
    brandData : '',//品牌数据
    providerData : '',//供应商数据
    jiaodianData : '',//焦点数据
    zheshelvData : '',//
    gongnengData : '',//
    typeData : '',//
    zhouqiData : '',//
    baozhuangData : '',//
    type10Data : '',//
    baozhuang11Data : '',//
    shopData : '',//
    yjDate : '',//
    params : '',//
    updateParams : '',//
    columns : '',//
    columnsTeshu : '',//
    $table : '',//
    TableInit : '',//
};



function Yj(id) {
    this.name = "yanjing";
    this.elem = document.getElementById(id)
}

Yj.prototype.enterSearch = function (id,callback){
    document.getElementById(id).onkeypress = function (e) {
        var keycode = e.keyCode;
        if(keycode == 13){
            document.getElementById(id).blur();
            e.preventDefault();
            name = document.getElementById(id).value;
            callback();
        }
    }


    var key = $(id).val();
    if(quakooUtils.isBlack(key)){
        layer.msg("请输入搜索内容");
        return;
    }

    callback(key);
};

