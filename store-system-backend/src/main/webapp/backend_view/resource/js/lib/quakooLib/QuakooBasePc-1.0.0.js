/**
 *  雀科科技- http://www.quakoo.com
 *
 *  基础PC网站开发需要的类
 *
*/

var QuakooDb = (function () {
    function QuakooDb() {
    }

    var _proto = QuakooDb.prototype;

    /**
     *
     * 添加数据到localstorage
     * @param key 字符串
     * @param value  字符串
     * @returns {ret}
     */
    _proto.setItem = function (key, value) {
        if(key!="user_admin" && key!='user_admin_time'){
            var user=quakooUser.getUserInfo();
            if (user) {
                var uid = '' + user.id;
                key = uid + key;
            }
        }
        this._setItem(key,value);
    }

    /**
     *
     * 获取数据
     * @param key 字符串
     * @returns {ret}
     * 返回的值 string
     */
    _proto.getItem = function (key) {
        if(key!="user_admin" && key!='user_admin_time'){
            var user=quakooUser.getUserInfo();
            if (user) {
                var uid = '' + user.id;
                key = uid + key;
            }
        }
        var ret= this._getItem(key);
        if(ret.status){
            return ret.data;
        }
    }


    /**
     *
     * 移除数据
     * @param key 字符串
     * @returns {ret}
     * {
     * status: true,//布尔类型；操作成功状态值，true|false
     * }
     */
    _proto.removeItem = function (key) {
        if(key!="user_admin" && key!='user_admin_time'){
            var user=quakooUser.getUserInfo();
            if (user) {
                var uid = '' + user.id;
                key = uid + key;
            }
        }
        this._removeItem(key);
    }






    /**
     *
     * 添加数据到数据库(用户无关,系统初始化时候用到)
     * @param key 字符串
     * @param value  字符串
     * @returns {ret}
     * {
     * status: true,//布尔类型；操作成功状态值，true|false
     * code: '',//数字类型；错误码，详情参考-----附录之‘错误码对照表’。，仅当 status 为 false 时有值。本参数暂仅支持iOS平台
     * msg: ''//字符串类型；错误描述，仅当 status 为 false 时有值
     * }
     */
    _proto._setItem = function (key, value) {
        if(quakooUtils.isJson(value)){
            value="_json_"+JSON.stringify(value);
        }else if(typeof(value) == "number" ){
            value="_number_"+value;
        }else if(typeof(value) =="boolean"){
            value="_boolean_"+value;
        }
        value = value.replace(/\'/g, "\'\'");
        localStorage.setItem(key,value)
    }

    /**
     *
     * 获取数据(用户无关,系统初始化时候用到)
     * @param key 字符串
     * @returns {ret}
     * {
     * status: true,//布尔类型；操作成功状态值，true|false
     * data: ""//返回的值 string
     * }
     */
    _proto._getItem = function (key) {
        var resultData = localStorage.getItem(key);
        if(quakooUtils.isBlack(resultData)){
            return {status: true};
        }
        if(resultData.indexOf("_json_")===0){
            resultData=resultData.substring(6);
            resultData=JSON.parse(resultData);
        } else if(resultData.indexOf("_number_")===0){
            resultData=resultData.substring(8);
            resultData= Number(resultData);
        } else if(resultData.indexOf("_boolean_")===0){
            resultData=resultData.substring(9);
            resultData= Boolean(resultData);
        }
        return {status: true, data: resultData};
    }


    /**
     *
     * 移除数据
     * @param key 字符串
     * @returns {ret}
     * {
     * status: true,//布尔类型；操作成功状态值，true|false
     * }
     */
    _proto._removeItem = function (key) {
        localStorage.removeItem(key);
    }

    Quakoo.class(QuakooDb, 'QuakooDb');

    return QuakooDb;
})();

var QuakooUser = (function () {
    function QuakooUser() {
    }

    var _proto = QuakooUser.prototype;


    /**
     * 设置用户到数据库
     * @param user
     */
    _proto.setUserInfo=function(user){
        if(user){
            quakooDb.setItem("user_admin",user);
            var timestamp = (new Date()).valueOf();
            quakooDb.setItem('user_admin_time', timestamp);
        }
    }

    /**
     * 移除数据库中的用户信息
     */
    _proto.removeUserInfo=function(){
        quakooDb.removeItem('user_admin');
        quakooDb.removeItem('user_admin_time');
    }

    /**
     * 从数据库中获取用户信息
     * @returns {*}
     */
    _proto.getUserInfo=function() {
        var timestamp = (new Date()).valueOf();
        var oldTimestamp = quakooDb.getItem('user_admin_time');
        if (quakooUtils.isBlack(oldTimestamp)) {
            return null;
        }
        if (timestamp - oldTimestamp > (60 * 60 * 24 * 7 * 1000)) {
            return null;
        }
        var user = quakooDb.getItem('user_admin');
        if (quakooUtils.isNotBlack(user)) {
            return user;
        } else {
            return null;
        }
    }
    Quakoo.class(QuakooUser, 'QuakooUser');

    return QuakooUser;
})();

/**
 * QuakooData
 * addDataToHtml(results,append)
 * @type {QuakooData}
 */
var QuakooData = (function () {
    function QuakooData() {

    }

    var _proto = QuakooData.prototype;

    _proto._createCacheKey = function (url, getData) {
        var cacheKey = url;
        for (var key in getData) {
            if (key != "cursor" && key != "size") {
                cacheKey = cacheKey + key + getData[key];
            }
        }
        return md5.md5(cacheKey);
    }
    /**
     * 提交ajax请求,带token
     * @param url 请求url
     * @param data 请求的数据
     * @param callback  服务器传输回数据，回调方法
     * @param dataType  服务器返回的数据类型,默认json
     * @param sync      异步true/同步false,默认true
     */
    _proto.ajaxGetData = function(url, data, callback, dataType, sync){
        this._ajaxSubmitData(url, data, callback, dataType, sync,true)
    }
    /**
     * 提交ajax请求,不带token
     * @param url 请求url
     * @param data 请求的数据
     * @param callback  服务器传输回数据，回调方法
     * @param dataType  服务器返回的数据类型,默认json
     * @param sync      异步true/同步false,默认true
     */
    _proto.ajaxGetDataWithOutUser = function(url, data, callback, dataType, sync){
        this._ajaxSubmitData(url, data, callback, dataType, sync,false)
    }
    /**
     * 提交ajax请求
     * 不需要缓存
     * @param url 请求url
     * @param data 请求的数据
     * @param callback  服务器传输回数据，回调方法
     * @param dataType  服务器返回的数据类型,默认json
     * @param sync      异步true/同步false,默认true
     * @param addUserToken      是否自动添加token true||false 默认为true
     * @private
     */
    _proto._ajaxSubmitData = function(url, data, callback, dataType, sync,addUserToken){
        dataType = dataType?dataType:'json';
        sync = quakooUtils.isBlack(sync)?true:sync;
        var user = quakooUser.getUserInfo();
        if(addUserToken){
            data.token = user.token;
        }
        $.ajax({
            url: url,
            type: "post",
            async: sync,
            data: data,
            dataType: dataType,
            success: function (data) {
                if (data || data == 0) {
                    if (quakooUtils.isFunction(callback)) {
                        callback(data);
                    }
                } else {
                    layer.msg('当前网络不给力', {
                        icon: 2
                    });
                }
            },
            error: function () {
                layer.msg('当前网络不给力', {
                    icon: 2
                });
            }
        })
    }
    Quakoo.class(QuakooData, 'QuakooData');

    return QuakooData;
})();