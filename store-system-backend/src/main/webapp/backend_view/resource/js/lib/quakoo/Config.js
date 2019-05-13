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
        this.isNeedCustomerUser = true;
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


        /**api服务地址*/
        this.serverUrl = "http://39.107.247.82:19996";
        // this.serverUrl = "http://192.168.1.13:50001";
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
    var _proto = Config.prototype;

    Quakoo.class(Config,'Config',_super);


    return Config;
})(QuakooConfig);

