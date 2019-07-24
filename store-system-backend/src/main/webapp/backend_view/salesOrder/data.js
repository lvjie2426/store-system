/**购物车数据
 * 金额存储全部是分（传给后台全部是分），展示时做元的计算
 *
 * */
var locShopCart= {
    orginalPrice:100,//订单原价共计
    discountPrice:80,//折后金额
    collectPrice:60,//实收金额
    bossDiscount:7.0,//最终折扣
    reductionPrice:100,//特惠减免
    skuidsList:[{//商品列表
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
        "加工费":19
    },{
        "验光费":23
    }],
};
/**添加商品数据*/
var locOrder = {
    type: 1,//按钮传值，1(常规商品) 2(积分商品);
    class:1,//类目ID，（镜片，镜架等）
    brand:1,//品牌ID

};