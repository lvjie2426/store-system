/**
 *  雀科科技- http://www.quakoo.com
 *
 *  基础PC网站开发需要的类
 *
*/


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
            localStorage.setItem('user_admin', JSON.stringify(user));
            var timestamp = (new Date()).valueOf();
            localStorage.setItem('user_admin_time', JSON.stringify(timestamp));
        }
    }

    /**
     * 移除数据库中的用户信息
     */
    _proto.removeUserInfo=function(){
        localStorage.setItem('user_admin', '');
    }

    /**
     * 从数据库中获取用户信息
     * @returns {*}
     */
    _proto.getUserInfo=function() {
        var timestamp = (new Date()).valueOf();
        var oldTimestamp = localStorage.getItem('user_admin_time');
        if (isBlank(oldTimestamp)) {
            return null;
        }
        if (timestamp - oldTimestamp > (60 * 60 * 24 * 7 * 1000)) {
            return null;
        }
        var user = localStorage.getItem('user_admin');
        if (isNotBlank(user)) {
            return JSON.parse(user);
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
                    if (isFunction(callback)) {
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