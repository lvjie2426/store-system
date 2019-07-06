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
        var loading = layer.load();
        dataType = dataType?dataType:'json';
        sync = sync==undefined?true:sync;
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
                layer.close(loading)
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
                layer.close(loading)
                layer.msg('当前网络不给力', {
                    icon: 2
                });
            }
        })
    }
    Quakoo.class(QuakooData, 'QuakooData');

    return QuakooData;
})();

var QuakooImg = (function () {
    function QuakooImg() {
    }

    var _proto = QuakooImg.prototype;



    /**
     *  获取图片真实尺寸
     *  @returns {ret}
     * {
     * w: int,//图片真实宽度
     * h: int,//图片真实高度
     * }
     */
    _proto.getImageRealSize=function(url){
        var obj ={w:0,h:0};
        if(url){
            var group=url.split("*");
            var strs=group[0].split("/");
            var orgWidth=parseFloat(strs[strs.length-1]);
            var orgHeight=parseFloat(group[1]);
            obj.w=orgWidth;
            obj.h=orgHeight;
            return obj;
        }
        return obj
    }



    /**
     *
     * 图片处理（压缩变形处理）
     * @param url 图片地址
     * @param showWidth 页面上展示宽度
     * @param showHieght 页面上展示的高度，传'auto'或者0，表示高度不固定。根据图片的实际形状展示。
     *
     * @returns {ret}
     * {
     * url: '',  //真实的URL
     * style: '',//裁剪样式（如果showHieght是固定的话）
     * }
     */
    _proto.processImg = function (url,showWidth,showHieght) {
        if(url&&url.startWith("http")){
            //从图片地址中获取图片的真实宽高
            var realSize=this.getImageRealSize(url);
            var orgWidth=realSize.w;
            var orgHeight=realSize.h;
            // 根据         要展示的宽 计算 2倍图所需要的宽度
            var width = Math.round(showWidth*2);
            //如果【 图片真实宽度】 大于 【2倍图所需要的宽度】 那么就 对图片 地址  拼接参数 进行 图片 压缩
            if (width < orgWidth) {
                var imgTypes = url.substring(url.lastIndexOf("."));
                var imgName = url.split(imgTypes)[0]+"_"+width+"_0";
                url = imgName+imgTypes;
            }

            // 进行图片裁剪  并把裁剪结果 返回
            if(showHieght&&showHieght>0){
                //根据   原真实宽高  和 压缩后宽度 计算  ---> 压缩后 高度
                var h = Math.round((showWidth/orgWidth)*orgHeight);//显示宽度除以图片真实宽度 乘以 图片真实高度
                //根据 需要高度 与  图片 压缩后 高度 进行比较  判断 是 需要 【左右裁剪 展示中间部分】还是【上下裁剪显示中间部分】 以此来不让图片变形
                if(showHieght > h){
                    //【左右裁剪 展示中间部分】  即 高度固定是   showHieght
                    var w = Math.round((showHieght/orgHeight)*orgWidth);//   算出左右超出部分值
                    var over = 0-Math.round((w - showWidth)/2);//  通过算出的值左右截取
                    var obj = {url:url,style:"margin-left:"+over+"px"+"; margin-right:"+over+"px;height:"+showHieght+"px"};
                    return obj;
                }else{
                    var over =  0-Math.round((h - showHieght)/2); //  通过算出的值上下截取
                    var obj = {url:url,style:"margin-top:"+over+"px"+";margin-bottom:"+over+"px;width:"+showWidth+"px"};
                    return obj;
                }
            }
        }

        return {url:url};
    }
    /**
     *  获取图片真实尺寸
     *  @returns {ret}
     * {
     * w: int,//图片真实宽度
     * h: int,//图片真实高度
     * }
     */
    _proto.getImageRealSize=function(url){
        var obj ={w:0,h:0};
        if(url){
            var group=url.split("*");
            var strs=group[0].split("/");
            var orgWidth=parseFloat(strs[strs.length-1]);
            var orgHeight=parseFloat(group[1]);
            obj.w=orgWidth;
            obj.h=orgHeight;
            return obj;
        }
        return obj
    }


    /**
     * 打开相册选择照片或者视频
     * @param imgNum 最大个数 int
     * @param type 字符串 img（只有图片）,video（只有视频）,all (所有)
     * @param callBack
     */
    _proto.open=function(callBack){
        $("#uploadForm input").click();
        this.upload(callBack)
    }
    _proto.upload=function(callBack){
        document.querySelector("#uploadForm input").onchange = function () {
            if(this.value){
                var loading1 = layer.load();
                var resultList = [];
                var filesList = $(this)[0].files;
                for(var i=0;i<filesList.length;i++){
                    (function (i) {
                        var formData = new FormData();
                        formData.append('file',filesList[i])
                        $.ajax({
                            url: config.uploadImageUrl, //Server script to process data
                            type: 'POST',
                            data: formData,
                            sync: false,
                            cache: false,
                            contentType: false,
                            processData: false,
                            dataType: "json",
                            success: function (result) {
                                if (result.ok != undefined) {
                                    resultList.push(result.ok);
                                    if(i==filesList.length-1){
                                        layer.close(loading1)
                                        if(callBack){
                                            callBack(resultList)
                                        }
                                    }
                                } else {
                                    layer.close(loading1)
                                    layer.msg('上传失败！', {
                                        icon: 2
                                    });
                                }
                            },
                            error: function () {
                                layer.close(loading1)
                                layer.msg('上传失败！', {
                                    icon: 2
                                });
                            }
                        });
                    })(i)

                }

                // return
                // formData.append('file',)

            }
        };
    }




    Quakoo.class(QuakooImg, 'QuakooImg');


    return QuakooImg;
})();