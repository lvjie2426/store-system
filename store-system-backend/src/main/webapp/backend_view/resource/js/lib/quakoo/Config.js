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



    Quakoo.class(Config,'Config',_super);


    return Config;
})(QuakooConfig);

