/**
 *  雀科科技- http://www.quakoo.com
 *
 *  混合开发APP需要的类
 *  常见的业务逻辑（如果某个业务有特殊需求，可以直接覆盖）
 *  业务类型的要标注好对应的HTML。以及重要的节点。切图的同学，应该在现有的HTML上 重新修改排版。项目负责人尽量不要让美术偏离现有已经设计好的东西
 *  封装好的HTML命名时候要以quakoo开头
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


var QuakooLogin = (function () {
    function QuakooLogin() {
        this.sendFlag = true;
    }

    var _proto = QuakooLogin.prototype;


    //初始化页面的时候，从本地内存获取验证码时间
    _proto.initSmsTime=function(){
        var lastSmsTime = quakooDb.getItem("Storage_Sms_Time");//$api.getStorage(Storage_Sms_Time);
        if(quakooUtils.isNotBlack(lastSmsTime)){
            var now = new Date().getTime();
            if((now - lastSmsTime) < 120000){
                this.sendFlag=false;
                this.setTimeToCode(Math.ceil(120 - ((now - lastSmsTime) / 1000)));
            }
        }
    }


    //发送验证码
    _proto.sms=function(type){
        if(!this.sendFlag){
            return;
        }
        this.sendFlag = false;
        var tel = $api.byId('tel').value;
        if(!quakooUtils.isMobileNum(tel)){
            this.sendFlag = true;
            quakooMsg.toast("手机号码不正确");
            return;
        }
        var url = config.getUrl_web_user_createAuthCodeOnUpdatePassword();
        if(type=='register'){
            url = config.getUrl_web_user_getCode();
        }
        if(type=='login'){
            url = config.createAuthCodeOnLogin();
        }
        quakooData.ajaxGetData(url,{
            phone : tel
        },function(ret){
            if(ret){
                quakooMsg.toast("验证码发送成功，请注意查收");
                _proto.setTimeToCode();
                quakooDb.setItem("Storage_Sms_Time",new Date().getTime());
            } else {
                this.sendFlag = true;
                var errorMsg="出错了，请稍后。";
                if(ret&&ret.msg){
                    errorMsg=ret.msg;
                }
                api.toast({
                    msg : errorMsg
                });
            }
        });
    }



    /***************验证码倒计时***************************/
    _proto.setTimeToCode=function(time){
        var code = document.getElementById('sms')||document.getElementById('yzm');
        time=time||120;
        code.innerHTML ="剩余"+ time+"s";
        var codeInterval =  setInterval(function(){
            if(time > 0){
                time--;
                code.innerHTML ="剩余"+time+"s";
            }else{
                code.innerHTML = "获取验证码";
                this.sendFlag = true;
                clearInterval(codeInterval);
            }
        },1000)

    }
    Quakoo.class(QuakooLogin, 'QuakooLogin');


    return QuakooLogin;
})();


/**
 * 支付js
 */
var QuakooPay = (function () {
    function QuakooPay() {
        this.sendFlag = true;
    }

    var _proto = QuakooPay.prototype;
    _proto.pay= function(payType,params,url,callback){
        this.sendFlag = false;
        if(payType == 1){
            aliPay(params,url,callback);
        } else if (payType == 2) {
            weixinPay(params,url,callback);
        }
    }


    _proto.weixinPay = function(params,url,callback){
        quakooData.ajaxGetData(url,params,function(ret,err){
            this.sendFlag = true;
            if(ret){

                var wxPay = api.require('wxPay');
                var apiKey = ret.appid;
                var wxOrderId = ret.prepayid;//1
                var mchId = ret.partnerid;//1
                var nonce_str = ret.noncestr;//1
                var sign = ret.sign;//1
                var timeStamp = ret.timestamp;
                var wxPackage = ret.package;

                wxPay.payOrder({
                    apiKey:apiKey,
                    orderId: wxOrderId,
                    mchId: mchId,
                    nonceStr: nonce_str,
                    timeStamp: timeStamp,
                    package : wxPackage,
                    sign: sign
                }, function(ret, err){
                    if(ret.status == true){
                        callback(ret);
                    }else{
//
                        callback(ret);
                    }
                });
            }else{
                api.toast({
                    msg : ret.errorMessage
                });
            }
        });
    }


    _proto.aliPay = function(params,url,callback){
        quakooData.ajaxGetData(url,params,function(ret,err){
            this.sendFlag = true;
            if(ret){
                var aliPayPlus = api.require('aliPayPlus');
                var orderInfo = ret.data.payParam;
                aliPayPlus.payOrder({
                    orderInfo : orderInfo
                },function(ret,err) {
                    if(ret.code == 9000){
                        callback(ret);
                    }else{
                        callback(ret);
                    }
                });
            } else {
                api.toast({
                	msg : ret.errorMessage
                });
            }
        });
    }




    Quakoo.class(QuakooPay, 'QuakooPay');


    return QuakooPay;
})();
//share 分享


var QuakooShare = (function () {
    function QuakooShare() {

    }

    var _proto = QuakooShare.prototype;

    _proto.method=function () {

    }
    /***
     *  打开分享
     * @param title             要分享的标题
     * @param shareTitle        要分享的描述
     * @param shareImg          要分享的缩略图的url
     * @param shareDynamicUrl   要分享的链接地址
     * @param params            需要传递的参数
     */
    _proto.openShare=function (title,shareTitle,shareImg,shareDynamicUrl,params) {
        //处理参数
        var param = '?';
        for(var i in params){
            param += i + '=' + params[i] + '&';
        }
        param = param.slice(0,param.length-1);
        var dialogBox = api.require('dialogBox');
        dialogBox.actionMenu({
            rect:{
                h: 125
            },
            tapClose:true,
            items:[
                {
                    text: '微信',
                    icon: 'widget://image/wx.png'
                },
                {
                    text: '朋友圈',
                    icon: 'widget://image/pyq.png'
                },
                {
                    text: 'QQ',
                    icon: 'widget://image/qq.png'
                },
                {
                    text: '微博',
                    icon: 'widget://image/wb.png'
                }
            ],
            styles:{
                bg:'#FFF',
                column: 4,
                itemText: {
                    color: '#999',
                    size: 12,
                    marginT:5
                },
                itemIcon:{
                    size:40
                }
            }
        }, function(ret){
            var dialogBox = api.require('dialogBox');
            dialogBox.close({
                dialogName: 'actionMenu'
            });
            if(ret.index == 0){
                var wx = api.require('wx');
                wx.shareWebpage({
                    apiKey: '',
                    scene: 'session',// 单聊
                    title: title,
                    description:shareTitle,
                    thumb:shareImg,
                    contentUrl: shareDynamicUrl+ param
                }, function(ret, err) {
                    if (ret.status) {
                        quakooMsg.toast('分享成功');
                    } else {
                        if(err.code == 2){
                            quakooMsg.toast('分享失败')
                        }
                    }
                });
            }else if(ret.index == 1){
                var wx = api.require('wx');
                wx.shareWebpage({
                    apiKey: '',
                    scene: 'timeline',// 朋友圈
                    title: title,
                    description: shareTitle,
                    thumb:shareImg,
                    contentUrl: shareDynamicUrl + param
                }, function(ret, err) {
                    if (ret.status) {
                        quakooMsg.toast('分享成功');
                    } else {
                        if(err.code == 2){
                            quakooMsg.toast('分享失败')
                        }
                    }
                });
            }else if(ret.index == 2){
                //QQPlus模块的imgUrl,在Android上只支持网络图片，所以用的qq模块
                var qq = api.require('qq');
                // var qq = api.require('QQPlus');
                qq.shareNews({
                    url: shareDynamicUrl+param,
                    title:title,
                    description: shareTitle,
                    imgUrl: shareImg,
                    type:'QFriend'
                },function(ret,err){
                    if (ret.status){
                        quakooMsg.toast('分享成功')
                    } else {
                        if(err.code == 10009){
                            quakooMsg.toast('当前设备未安装qq客户端')
                        }else{
                            quakooMsg.toast('分享失败')
                        }
                    }
                });
            }else if(ret.index == 3){
                var dialogBox = api.require('dialogBox');
                dialogBox.close({
                    dialogName: 'actionMenu'
                });
                quakooMsg.toast('微博分享正在开发中…')
            }
        });
    }


    Quakoo.class(QuakooShare, 'QuakooShare');

    return QuakooShare;
})();


//dianzan 点赞

//common 评论

//点赞
/**
 * @param tid 业务ID
 * @param self 当前对象
 * @param btype 业务类型 1.评论 2.timeline 3.通知 4.宝宝评语 5.文档
 *
 * 1.切换本地数据状态，防止加载过程中多次点击导致数据不正确
 * 2.如果失败切回本地数据状态
 *
 */
function support(tid,self,btype){
    QuakooUtils.stopEventBubble();
    var type = $api.attr(self,"type");

    if(type == 1){
        var supportval = $api.html(self);
        $api.html(self,(Number(supportval) +1));
        $api.css(self,"background-image:url(../../image/dianzan02.png)");
        $api.attr(self,"type","2");
        quakooData.ajaxGetData(config.getUrl_web_system_supportAdd(),{type:btype,typeId:tid},function(ret,err){
            if(ret&&ret.success){
                quakooMsg.toast("点赞成功！");
            }else{
                $api.html(self,(supportval));
                $api.css(self,"background-image:url(../../image/dianzan01.png)");
                $api.attr(self,"type","1");
                quakooMsg.toast("网络连接失败，请重试！")
            }
        })
    }else if(type == 2){
        var supportval =  $api.html(self);
        $api.html(self,(Number(supportval) -1));
        $api.css(self,"background-image:url(../../image/dianzan01.png)");
        $api.attr(self,"type","1");
        quakooData.ajaxGetData(config.getUrl_web_system_supportDelete(),{type:btype,typeId:tid},function(ret,err){
            if(ret&&ret.success){
                quakooMsg.toast("取消点赞成功！");
            }else{
                $api.html(self,(supportval));
                $api.css(self,"background-image:url(../../image/dianzan02.png)");
                $api.attr(self,"type","2");
                quakooMsg.toast("网络连接失败，请重试！")
            }
        })
    }
}

//收藏
/**
 * @param tid 业务ID
 * @param self 当前对象
 * @param btype 业务类型 1.评论 2.timeline 3.通知 4.宝宝评语 5.文档
 *
 *
 *
 */
function favorite(tid,self,btype){
    QuakooUtils.stopEventBubble();
    var type = $api.attr(self,"type");
    if(type == 1){
        var favoriteVal = $api.html(self);
        $api.html(self,(Number(favoriteVal) +1));
        $api.css(self,"background-image:url(../../image/mystudy04.png)");
        $api.attr(self,"type","2");
        quakooData.ajaxGetData(config.getUrl_web_system_favoriteAdd(),{type:5,typeId:tid},function(ret,err){
            if(ret&&ret.success){
                quakooMsg.toast("收藏成功！");
            }else{
                $api.html(self,(supportval));
                $api.css(self,"background-image:url(../../image/mystudy05.png)");
                $api.attr(self,"type","1");
                quakooMsg.toast("网络连接失败，请重试！")
            }
        })
    }
    else{
        var favoriteVal = $api.html(self);
        $api.html(self,(Number(favoriteVal) -1));
        $api.css(self,"background-image:url(../../image/mystudy05.png)");
        $api.attr(self,"type","1");
        quakooData.ajaxGetData(config.getUrl_web_system_favoriteDelete(),{type:5,typeId:tid},function(ret,err){
            if(ret&&ret.success){
                quakooMsg.toast("取消收藏成功！");
            }else{
                $api.html(self,(supportval));
                $api.css(self,"background-image:url(../../image/mystudy04.png)");
                $api.attr(self,"type","2");
                quakooMsg.toast("网络连接失败，请重试！")
            }
        })
    }
}










