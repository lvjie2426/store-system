/**
 *  雀科科技- http://www.quakoo.com
 *
 *  眼睛后台管理系统所需要的类
 *
 *
 *  登录注册业务： 登录，注册，找回密码，第三方登录（常见的几种登录注册方案：如（电商登录注册方案，小红书登录注册方案）），跟后端的接口要固定好
 *
 *  轮播图业务：跟后端的轮播图业务要一一对应
 *
 *  地址管理业务：跟后端的地址管理业务要一一对应
 *
 *  购物车业务：跟后端的购物车业务要一一对应
 *
 *  钱包业务：跟后端的钱包业务要一一对应
 *
 *  我的订单业务：跟后端的订单业务要一一对应
 *
 *  支付业务：
 *
 *  帮助中心业务：
 *
 *  优惠券业务：
 *
 *  设置业务：
 *
 *
 *
 */
var QuakooYanJing = (function () {
    function QuakooYanJing() {
        //设置属性
    }

    var _proto = QuakooYanJing.prototype;


    /***
     * 选择下拉框
     * @param ele       绑定的元素
     * @param callback  选择的值{text:"",value:""}  text 文本内容，value li自定义属性值
     * @param showZdy   是否有自定义
     */
    _proto.selectTag=function(ele,callback,showZdy){
        ele = $(ele);
        var optionBox = ele.find('.optionBox'), content,_this = this;
        ele.on('click', function () {
            return _this._opDN(optionBox.is(':hidden') ? true : false, ele);
        });
        $(document).on('click', function () {
            alert(1)
            return _this._opDN(false,ele);
        });
        ele.find('.optionUL>li').on('click', function () {
            content = $(this).text();
            _this._opDN(false, ele);
            ele.find('.text').text(content);
            if(callback){
                if($(this).attr('value')){
                    callback({value:$(this).attr('value'),text:content})
                }else{
                    callback({value:content,text:content})
                }
            }
        });
        if(showZdy){
            ele.find('.zi_dingyi').on('click', function () {
                quakooUtils.stopEventBubble()
            });
            ele.find('.zdy_pingpai').on('click', function () {
                $(this).hide(500);
                ele.find('.zi_dingyi_input_Box').show(500);
            });
            ele.find('.btn_conten').on('click', function () {
                content = $(this).parent().children('input').val();
                if (content == '') return alert('内容不能为空');
                ele.find('.text').text(content);
                if(callback){
                    callback({text:content})
                }
                _this._opDN(false, ele);
            });
        }

    }
    _proto._opDN= function(type,ele){
        var optionBox = ele.find('.optionBox')
        type ? optionBox.show(500) : optionBox.hide(500);
        ele.find('#jt').css('transform', type ? 'rotate(134deg)' : 'rotate(-45deg)');

        if (!type) {
            ele.find('.zi_dingyi_input').val('');
            ele.find('.zi_dingyi_input_Box').hide();
            ele.find('.zdy_pingpai').show();
        }
        quakooUtils.stopEventBubble()
    }

    Quakoo.class(QuakooYanJing, 'QuakooYanJing');


    return QuakooYanJing;
})();
