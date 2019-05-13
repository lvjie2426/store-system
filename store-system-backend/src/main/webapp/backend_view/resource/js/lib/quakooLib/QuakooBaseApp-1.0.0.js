/**
 *  雀科科技- http://www.quakoo.com
 *
 *  基础混合开发APP需要的类
 *  包含基本的插件，基本操作（打开页面，跳转等），数据库,文件,图片处理,视频处理，二维码，版本控制，热更新等等
 *
 */


var QuakooDb = (function () {
    function QuakooDb() {
    }

    var _proto = QuakooDb.prototype;


    /**
     *
     * 初始化数据库，APP打开的时候 调用一次
     * @returns {ret}
     * {
     * status: true,//布尔类型；操作成功状态值，true|false
     * code: '',//数字类型；错误码，详情参考-----附录之‘错误码对照表’。，仅当 status 为 false 时有值。本参数暂仅支持iOS平台
     * msg: ''//字符串类型；错误描述，仅当 status 为 false 时有值
     * }
     */
    _proto.initDb = function () {
        if(!this.db){
            this.db = api.require('db');
        }

        var ret = this.db.openDatabaseSync({
            name: 'app',
            path: 'fs://quakoo.db'
        });

        if (ret.status) {
            ret = this.db.executeSqlSync({
                name: 'app',
                sql: 'drop table if exists app1 ;'//测试时候用。
            });
            if (ret.status) {
                ret = this.db.executeSqlSync({
                    name: 'app',
                    sql: 'CREATE TABLE  if not exists app(key varchar(255), value varchar(55235));'
                });
                if (ret.status) {
                    ret = this.db.executeSqlSync({
                        name: 'app',
                        sql: 'create unique index if not exists index1 on app(key);'
                    });
                }
            }
        }
        return ret;
    }

    /**
     *
     * 清除数据库
     * @returns {ret}
     * {
     * status: true,//布尔类型；操作成功状态值，true|false
     * code: '',//数字类型；错误码，详情参考-----附录之‘错误码对照表’。，仅当 status 为 false 时有值。本参数暂仅支持iOS平台
     * msg: ''//字符串类型；错误描述，仅当 status 为 false 时有值
     * }
     */
    _proto.cleanDb = function () {
        if(!this.db){
            this.db = api.require('db');
        }

        var ret = this.db.executeSqlSync({
            name: 'app',
            sql: 'drop table if exists app ;'
        });
        return ret;
    }


    /**
     *
     * 添加数据到数据库
     * @param key 字符串
     * @param value  字符串
     * @returns {ret}
     * {
     * status: true,//布尔类型；操作成功状态值，true|false
     * code: '',//数字类型；错误码，详情参考-----附录之‘错误码对照表’。，仅当 status 为 false 时有值。本参数暂仅支持iOS平台
     * msg: ''//字符串类型；错误描述，仅当 status 为 false 时有值
     * }
     */
    _proto.setItem = function (key, value) {
        if(key!="userInfo"){
            var user=quakooUser.getUserInfo();
            if (user) {
                var uid = '' + user.id;
                key = uid + key;
            }
        }
        return this._setItem(key,value);
    }

    /**
     *
     * 获取数据
     * @param key 字符串
     * @returns {ret}
     * 返回的值 string
     */
    _proto.getItem = function (key) {
        if(key!="userInfo"){
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
        if(key!="userInfo"){
            var user=quakooUser.getUserInfo();
            if (user) {
                var uid = '' + user.id;
                key = uid + key;
            }
        }
        return this._removeItem(key);
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
        if(!this.db){
            this.db = api.require('db');
        }
        if(quakooUtils.isJson(value)){
            value="_json_"+JSON.stringify(value);
        }else if(typeof(value) == "number" ){
            value="_number_"+value;
        }else if(typeof(value) =="boolean"){
            value="_boolean_"+value;
        }
        value = value.replace(/\'/g, "\'\'");
        var sql = 'INSERT OR REPLACE INTO app (key,value) values (\'' + key + '\',\'' + value + '\')';
        var ret = this.db.executeSqlSync({
            name: 'app',
            sql: sql
        });
        return ret;
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
        if(!this.db){
            this.db = api.require('db');
        }
        var sql = 'select value from app where key=\'' + key + '\'';
        var ret = this.db.selectSqlSync({
            name: 'app',
            sql: sql
        });
        if (ret.status) {
            var data = ret.data;
            if (data.length >= 1) {
                var resultData=data[0].value;
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
            } else {
                return {status: true};
            }
        } else {
            return ret;
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
    _proto._removeItem = function (key) {
        if(!this.db){
            this.db = api.require('db');
        }
        var sql = 'delete from app where key="' + key + '"';
        var ret = this.db.executeSqlSync({
            name: 'app',
            sql: sql
        });
        return ret;
    }

    Quakoo.class(QuakooDb, 'QuakooDb');

    return QuakooDb;
})();


var QuakooFs = (function () {
    function QuakooFs() {
    }

    var _proto = QuakooFs.prototype;


    /**
     *
     * 初始化文件系统，创建文件夹，APP打开的时候 调用一次
     * @returns {ret}
     * {
     * status: true,//布尔类型；操作成功状态值，true|false
     * code: '',//数字类型；错误码，详情参考-----附录之‘错误码对照表’。，仅当 status 为 false 时有值。本参数暂仅支持iOS平台
     * msg: ''//字符串类型；错误描述，仅当 status 为 false 时有值
     * }
     */
    _proto.initFs = function () {
        if(!this.fs){
            this.fs = api.require('fs');
        }

        var ret = this.fs.existSync({
            path: 'fs://quakoo'
        });
        if (!ret.exist) {
            ret = this.fs.createDirSync({path: 'fs://quakoo'});
        }
        return ret;
    }
    Quakoo.class(QuakooFs, 'QuakooFs');


    return QuakooFs;
})();


var QuakooImg = (function () {
    function QuakooImg() {
    }

    var _proto = QuakooImg.prototype;


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
    _proto.openMedia=function(imgNum,type,callBack){
        api.accessNative({
            name: "openMedia",
            extra: {
                maxstr:imgNum,
                type:type,//img（只有图片）,video（只有视频）,all||'' (所有)
            }
        }, function (ret, err) {
        });


        api.addEventListener({
            name: 'openMediaListener' //监听选择完的事件
        }, function(ret, err) {
            //ret.thumbnails
            //返回缩略图地址（file开头的）[file://xxxxx/xxxx/xxx.jpg,file://xxxxx/xxxx/xxx.jpg]

            //ret.imgs
            //返回实际图片地址（file开头的）[file://xxxxx/xxxx/xxx.jpg,file://xxxxx/xxxx/xxx.jpg]

            //ret.cover
            //返回视频封面地址（file开头的）file://xxxxx/xxxx/xxx.jpg

            //ret.video
            //返回视频地址（file开头的）file://xxxxx/xxxx/xxx.mp4

            callBack(ret);
        });

    }



    /**
     *
     * 调用原生方法上传图片
     * 这个时候原生的过程应该是自己打开上传进度条 （1/20）（19/20）（20/20）
     * @param files    （注意是本地文件）文件:["file://xxxxx/xxxx/xxx.jpg","file://xxxxx/xxxx/xxx.jpg"],//实际图片的路径（file开头的地址）
     * @param callBackOnUpload
     */
    _proto.uploadImgs=function(files,callBackOnUpload){
        api.accessNative({
            name: "uploadImgs",
            extra: {
                files:files,
            }
        }, function (ret, err) {
        });


        api.addEventListener({
            name: 'uploadImgsListener' //监听选择完的事件
        }, function(ret, err) {
            //ret.urls
            //返回上传后图片地址（http开头的）[http://xxxxx/xxxx/xxx.jpg,http://xxxxx/xxxx/xxx.jpg]

            callBackOnUpload(ret);
        });

    }









    //上传头像，或者证件
    //打开相册或者拍照（单个图片，带裁剪功能）
    //返回的结果是已经上传的图片
    /**
     *
     * @crop 是否裁剪，0表示不裁剪  1表示裁剪
     * @param callBackOnUpload
     */
    _proto.openAlbumAndUpload=function(crop,callBackOnUpload){
        api.accessNative({
            name: "openAlbum",
            extra: {
                crop:crop //是否裁剪，0表示不裁剪  1表示裁剪
            }
        }, function (ret, err) {
        });


        api.addEventListener({
            name: 'openAlbumListener' //监听选择完的事件
        }, function(ret, err) {
            //ret.url
            //返回上传之后的图片地址（http开头的）http://xxxxx/xxxx/xxx.jpg
            callBackOnUpload(ret);
        });

    }

    /**
     * 批量获取图片缓存地址
     * @param imgs
     * @param callBack 回调map结构（key为原始图片，value为缓存地址）
     * @private
     */
    _proto._batchGetImgsCache=function(imgs,callBack){
        var result = {};
        var j = 0;
        var hasCall = false;
        for (var i = 0; i < imgs.length; i++) {
            var img = imgs[i];
            (function (img) {
                api.imageCache({
                    url: img,
                    thumbnail : false,
                    policy:'cache_only'
                },function( ret, err ){
                    if(ret){
                        result[img] = ret.url;
                    }else{
                        result[img] = img;
                    }
                    j++;
                    if (j == imgs.length && !hasCall) {
                        hasCall = true;
                        callBack(result);
                    }
                });

            })(img);
        }
    }

    /**
     * 预览图片,大图浏览模式
     * 需要传入在页面点击大图浏览前的尺寸。
     * 该方法通过在传入之前的尺寸，获取对应的缩略图（之前已经获取过了，并且有缓存）
     * 1.先把缓存的缩略图图片传给原生（原生先显示本地的缓存图片，加快速度）
     * 2.再把原始图片地址传给原生
     *
     * @param placeholderImg //占位图（一般是APP，LOGO)
     * @param showWidth 当前图片的展示的宽度（在页面点击大图浏览前的宽度）
     * @param showHieght 当前图片的展示的高度（在页面点击大图浏览前前的宽度）
     * @param imgs 所有图片的原始URL，数组
     * @param activeIndex 当前打开第几个图片
     */
    _proto.photoBrowser=function(placeholderImg,showWidth,showHieght,imgs,activeIndex){
        var thumbnails=[];
        for(var i=0;i<imgs.length;i++){
            //获取缩略图
            var thumbnail=  this.processImg(imgs[i],showWidth,showHieght).url;
            thumbnails.push(thumbnail);
        }

        var thumbnailCaches=[];

        this._batchGetImgsCache(thumbnails,function (result) {
            for(var i=0;i<thumbnails.length;i++){
                thumbnailCaches.push(result[thumbnails[i]]);
            }

            api.accessNative({
                name: "photoBrowser",
                extra: {
                    placeholderImg:placeholderImg,//占位图（一般是APP，LOGO）
                    thumbnails:thumbnailCaches,//缩略图（小图）
                    imgs:imgs,//所有图片（大图）
                    activeIndex:activeIndex,//打开的索引
                }
            }, function (ret, err) {
            });

        });

    }


    /**
     * 用模块批量压缩图片，该模块收费
     * 需要传入在imgList 需要压缩的本地图片数据
     *
     * @param imgList //图片数组（一般是APP，LOGO)
     * @param callBack 返回压缩后的数组列表
     * @param size 压缩的品质
     */
    _proto.compressImageList =function(imgList,size,callBack){
        var compactPicture = api.require('compactPicture');
        compactPicture.HittingPic({
            picpatharray: imgList,
            size: size
        }, function(ret) {
            if(quakooApp.isNotBlack(ret)  && quakooApp.isNotBlack(ret.states)){
                //转换成小写
                var resultList = new Array;
                var states = ret.states;
                for(var i = 0;i < states.length;i++){
                    resultList.push(coverToSuffix(states[i]));
                }
                if(callBack){
                    callBack(resultList);
                }

            }else {
                if(callBack){
                    callBack([]);
                }
            }
        });

        //图片后缀的大小写转换
        function coverToSuffix(src){
            var header = src.substring(0,src.lastIndexOf("."));
            var suffix = src.substring(src.lastIndexOf("."));
            return header+suffix.toLocaleLowerCase();
        }
    }









    Quakoo.class(QuakooImg, 'QuakooImg');


    return QuakooImg;
})();


var QuakooVideo = (function () {
    function QuakooVideo() {
    }

    var _proto = QuakooVideo.prototype;


    /**
     *
     * 调用原生方法上传视频
     * 这个时候原生的过程应该是自己打开上传进度条
     * @param cover 视频封面文件 file://xxxxx/xxxx/xxx.jpg
     * @param video 视频文件 file://xxxxx/xxxx/xxx.mp4
     * @param callBackOnUpload
     */
    _proto.uploadVideo=function(cover,video,callBackOnUpload){
        api.accessNative({
            name: "uploadVideo",
            extra: {
                cover:cover,//上传视频的封面"file://xxxxx/xxxx/xxx.jps"
                video:video//上传的视频的真实路径"file://xxxxx/xxxx/xxx.mp4"
            }
        }, function (ret, err) {
        });


        api.addEventListener({
            name: 'uploadVideoListener' //监听选择完的事件
        }, function(ret, err) {

            //ret.cover
            //返回上传后封面地址（http开头的）http://xxxxx/xxxx/xxx.jpg

            //ret.video
            //返回上传后视频播放地址（http开头的）http://xxxxx/xxxx/xxx.mp4

            callBackOnUpload(ret);
        });

    }



    /**
     * 拍摄视频（）
     * @param type 模式：0:基本版，1：中级班（开启美颜，开启配乐），3：高级版
     * @param minTime 最少时间
     * @param maxTime 最大时间
     * @param callBack
     */
    _proto.recordVideo=function(type,minTime,maxTime,callBack){
        api.accessNative({
            name: "recordVideo",
            extra: {
                type:type ,//模式：开启美颜，开启配乐
                minTime:1, //拍摄最小时间
                maxTime:10 //拍摄最大时间
            }
        }, function (ret, err) {
        });


        api.addEventListener({
            name: 'recordVideoListener' //监听选择完的事件
        }, function(ret, err) {
            //ret.cover  （file开头的）file://xxxxx/xxxx/xxx.jpg
            //ret.video （file开头的）file://xxxxx/xxxx/xxx.mp4
            callBack(ret);
        });

    }

    //预览视频
    /**
     *
     * @param showWidth 封面图片在页面上的展示宽度
     * @param showHieght 封面图片在页面上的展示高度
     * @param cover 封面
     * @param video 视频地址
     */
    _proto.videoBrowser=function(showWidth,showHieght,cover,video){

        var thumbnail=  this.processImg(cover,showWidth,showHieght).url;
        api.imageCache({
            url: thumbnail,
            thumbnail : false,
            policy:'cache_only'
        },function( ret, err ){
            var cacheUrl;
            if(ret){
                cacheUrl= ret.url;
            }else{
                cacheUrl= thumbnail;
            }

            api.accessNative({
                name: "videoBrowser",
                extra: {
                    cover:cacheUrl, //封面
                    video:video
                }
            }, function (ret, err) {
            });

        });


    }

    Quakoo.class(QuakooVideo, 'QuakooVideo');


    return QuakooVideo;
})();


var QuakooSound = (function () {
    function QuakooSound() {
    }

    var _proto = QuakooSound.prototype;


    /**
     * 录制音频（弹出跟微信一样的录制音频的页面）
     * @param minTime 录制最小时间
     * @param maxTime 录制最大时间
     * @param callBackOnUpload
     */
    _proto.recordSound=function(minTime,maxTime,callBackOnUpload){
        api.accessNative({
            name: "recordSound",
            extra: {
                minTime:minTime, //录制最小时间
                maxTime:maxTime //录制最大时间
            }
        }, function (ret, err) {
        });


        api.addEventListener({
            name: 'recordSoundListener' //监听选择完的事件
        }, function(ret, err) {
            //ret.url
            //返回上传之后的音频地址（http开头的）http://xxxxx/xxxx/xxx.mp3
            callBackOnUpload(ret);
        });

    }
    Quakoo.class(QuakooSound, 'QuakooSound');


    return QuakooSound;
})();


var QuakooApp = (function () {
    function QuakooApp() {
    }

    var _proto = QuakooApp.prototype;


    /**
     *
     * 系统初始化（index.html调用）

     */
    _proto.systemInit = function () {
        var ret=quakooDb.initDb();
        if(!ret.status){
            quakooMsg.toast("无法创建本地数据库！");
            return;
        }
        ret=quakooFs.initFs();
        if(!ret.status&&!ret.exist){
            quakooMsg.toast("无法建立本地文件夹！");
            return;
        }
        
        //判断是否第一次打开
        var alreadyShowGuide = quakooDb.getItem("quakooShowGuideKey_"+config.version);
        if(config.isShowGuide&&!alreadyShowGuide){
            //播放引导页
            quakooDb.setItem("quakooShowGuideKey_"+config.version,true);
            //FIXME 播放引导页
        }else{
            this.openWin_noAnimation("main","html/main/main.html",{});
        }


    }

    /**
     * 无动画打开新页面
     * @param name
     * @param url
     * @param pageParam
     */
    _proto.openWin_noAnimation=function (name,url,pageParam) {
        api.openWin({
            name: name,
            historyGestureEnabled:false,
            slidBackEnabled:false,
            url: url,
            pageParam: pageParam,
            animation:{
                type:"none",                //动画类型（详见动画类型常量
                duration:10                //动画过渡时间，默认300毫秒
            }
        });
    }

    /**
     * 打开一个新的页面窗口
     * @param name
     * @param url
     * @param pageParam
     * @param overload
     */
    _proto.openNewWindow=function(name, url, pageParam, overload) {
        var delay = 0;
        var sysType = api.systemType;
        var params = {
            name : name,
            url : url,
            pageParam : pageParam,
            bounces : false,//页面是否弹动
            bgColor : 'rgba(255,255,255,0)',
            scrollToTop : false,
            vScrollBarEnabled : false,
            hScrollBarEnabled : false,
            scaleEnabled : false,//页面是否可以缩放，为true时softInputMode参数无效
            slidBackEnabled : true,//是否支持滑动返回。iOS7.0及以上系统中，在新打开的页面中向右滑动，可以返回到上一个页面，该字段只iOS有效
            showProgress : false,//是否显示等待框，只在url为网址时有效
            delay : delay,//window显示延迟时间，适用于将被打开的window中可能需要打开有耗时操作的模块时，可延迟window展示到屏幕的时间，保持UI的整体性
            reload : false,//页面已经打开时，是否重新加载页面，重新加载页面后apiready方法将会被执行
            allowEdit : true//是否允许长按页面时弹出选择菜单
        };
        if(sysType == 'android'){
            var ver = api.systemVersion;
            ver = parseFloat(ver);
            if(ver >= 5.0){
                params.delay = 100;
            }
            params.animation={
                type:"push",                //动画类型（详见动画类型常量）
                subType:"from_right",       //动画子类型（详见动画子类型常量）
                duration:300                //动画过渡时间，默认300毫秒
            }
        }

        if(overload) {
            for (var m in overload) {
                params[m] = overload[m];
            }
        }
        api.openWin(params);
    }

    /**
     * 内部打开一个外部链接
     * @param url 跳转   内部跳转 inner://xxx/xxx 外部跳转 http://xxx/xxx
     * @param currentDirectory 当前目录  最外层目录小于0（companyManage.html） 第一级目录（html文件夹下的，目前没有） 第二级目录（html文件夹下的文件夹）
     */
    _proto.openSystemForword = function(url,currentDirectory,overLoad){
        currentDirectory=currentDirectory||0;
        var path="../";
        if(currentDirectory <= 0){
            path="./html/";
        } else if(currentDirectory==2){
            path="../";
        }else if(currentDirectory == 1){
            path="./";
        }

        if(url.startWith("inner")){
            var content=url.substring("inner://".length);
            var name;
            var uri;
            var queryStr;
            var param={};
            var type;
            if(content.indexOf("?")>0){
                uri=content.substr(0,content.indexOf("?"));
                queryStr=content.substr(content.indexOf("?")+1);
            }else{
                uri=content;
            }
            var parts=uri.split("/");
            type=parts[0];
            name=parts[parts.length-1].replace(".html","");

            if(quakooUtils.isNotBlack(queryStr)){
                var params=queryStr.split("&");
                for(var i=0;i<params.length;i++){
                    param[params[i].split("=")[0]]=params[i].split("=")[1];
                }
            }
            this.openNewWindow(name,path+uri,param,overLoad);
        }else if(url.startsWith("http://") || url.startsWith("https://")){
            var linkUrl;
            var queryStr;
            var param = {};

            if(url.indexOf("?")>0){
                linkUrl = url.substr(0,url.indexOf("?"));
                queryStr = url.substr(url.indexOf("?")+1);
            }else{
                linkUrl = url;
            }
            if(quakooUtils.isNotBlack(queryStr)){
                var params=queryStr.split("&");
                for(var i=0;i<params.length;i++){
                    param[params[i].split("=")[0]]=params[i].split("=")[1];
                }
            }

            param.url = linkUrl;
            var browser = api.require('webBrowser');
            browser.open({
                url:url
            });
//      quakooApp.openNewWindow(hex_md5(url), path + "outside/outside.html",param,overLoad);
        }else{
            var content=url.substring("native://".length);
            var name;
            var queryStr;
            var param={};

            if(url.indexOf("?")>0){
                name = content.substr(0,content.indexOf("?"));
                queryStr = content.substr(content.indexOf("?")+1);
            }else{
                name = url;
            }
            if(quakooUtils.isNotBlack(queryStr)){
                var params=queryStr.split("&");
                for(var i=0;i<params.length;i++){
                    param[params[i].split("=")[0]]=params[i].split("=")[1];
                }
            }
            api.accessNative({
                name: name,
                extra:param
            }, function(ret, err) {

                if (ret) {

                } else {
                    quakooMsg.toast("启动失败")
                }
            });
        }
    }


    /**
     * 打窗口内打开frame
     * @param name 名称
     * @param url 地址
     * @param pageParam 页面传递参数
     * @param overload
     */
    _proto.openFrameInWin=function(name, url, pageParam, overload) {
        this.openFrame(name,url,pageParam,config.winHeadHeight,config.winBottomHeight,overload);
    }

    _proto.initFrameInWin=function(pageParam,overload,height){
        var statusBarAppearance = api.statusBarAppearance;//是否支持沉浸式
        var header = document.querySelector('#topbar');
        $api.fixStatusBar(header);
        header.style.paddingTop = api.safeArea.top + 'px';
        header.style.display = 'block';
        config.winHeadHeight = header.offsetHeight;
        if(height){
            config.winHeadHeight += height;
        }
        this.openFrameInWin(api.winName + '_body','./' + api.winName + '_body.html',pageParam,overload);
    }

    /**
     * 打开frame
     * @param name
     * @param url
     * @param pageParam
     * @param headHeight
     * @param bottomHeight
     * @param overload
     */
    _proto.openFrame=function(name,url,pageParam,headHeight,bottomHeight,overload) {
        var params = {
            name : name,
            url : url,
            pageParam : pageParam,
            rect : {
                x : 0,
                marginTop : headHeight,
                w : api.frameWidth,
                marginBottom:bottomHeight
            },
            bounces : false,
            opaque : false,
            //bgColor:'#EDEDED',
            bgColor : '#ededed',
            useWKWebView: true, //设置在 ios 平台使用 wkWebview 显示页面,$api.getStorage会失效。需要使用api.setPerfs
            allowEdit : true,
            vScrollBarEnabled : true,
            showProgress : false,
            hScrollBarEnabled : false,
            reload : false
        };

        if(overload) {
            for (var m in overload) {
                params[m] = overload[m];
            }
        }

        api.openFrame(params);
    }


    /**
     * 关闭当前app
     */
    _proto.closeApp=function(){
        api.closeWidget({
            retData: {name: 'closeWidget'},
            silent: true,
            animation: {
                type: 'none',
                subType: 'from_bottom',
                duration: 500
            }
        });
    }



    _proto.toast = function(msg){
        var strDM = api.systemType;//系统
        if (strDM == 'ios') {
            api.toast({
                msg: msg,
                duration:2000,
                location: 'middle'
            });
        } else if (strDM == 'android') {
            api.toast({
                msg: msg,
                duration:2000,
                location: 'bottom'
            });
        }
    }


    /**
     * 关闭当前窗口
     */
    _proto.closeWin=function(){
        api.closeWin({
            name: api.winName
        });
    }

    /**
     * 无动画关闭此页面
     */
    _proto.closeThisWin=function(){
        api.closeWin({
            name: api.winName,
            animation:{type:"none"}
        });
    }






    /**
     * 热更新版本更新
     * @param callBack 更新成功后的回调 可以不传
     */
    _proto.startHotUpdate=function(callBack){
        //循环获取
        quakooData._ajaxGetData(config.getUrl_web_version_hotUpdate(),{version:config.hotVersion,time:new Date().getTime()},function (ret) {
            if(ret && ret.success){
                if(isNotBlack(ret.data)){
                    if(isNotBlack(ret.data.url)){
                        if(ret.data.version != config.hotVersion){
                            api.accessNative({
                                name: "hotUpdate",
                                extra: {
                                    url:ret.data.url //热更新包下载地址
                                }
                            }, function (ret, err) {});
                            api.addEventListener({
                                name: 'hotUpdateListener' //监听选择完的事件
                            }, function(ret, err) {
                                //ret.status
                                //下载完成
                                config.hotVersion=ret.data.version;
                                if(callBack){
                                    callBack(ret);
                                }
                            });
                        }
                    }
                }
            }
            setTimeout(function () {
                quakooApp.startHotUpdate(callBack);
            },600000);
        },true,false,false,function () {
            setTimeout(function () {
                quakooApp.startHotUpdate(callBack);
            },600000);
        });

    }








    //扫一扫
    _proto.scanning=function(callBack){
        api.accessNative({
            name: "scanning",
            extra: {
            }
        }, function (ret, err) {
        });


        api.addEventListener({
            name: 'scanningListener' //监听选择完的事件
        }, function(ret, err) {
            //ret.url
            //返回扫描之后的url
            callBack(ret);
        });

    }


    //打开密码输入页面
    //其他的控制多次输入等等。是否正确等等由H5控制
    _proto.passwordPage=function(callBack){
        api.accessNative({
            name: "passwordPage",
            extra: {
            }
        }, function (ret, err) {
        });

        api.addEventListener({
            name: 'passwordPageListener' //监听选择完的事件
        }, function(ret, err) {
            //ret.password
            //输入的密码
            callBack(ret);
        });

    }

    //打开某一个原生页面(传页面参数)
    /**
     *
     * @param name 页面名字
     * @param param 页面参数, （map结构json）
     * @param animation 动画效果参数等,（map结构json） 动画参数由原生同学构建一下，先构建最基本的从哪打开，多少毫秒
     */
    _proto.openNativePage=function(name,param,animation){
        api.accessNative({
            name: "openWin",//
            extra: {
                name:name,//页面名字
                param:param,//页面参数, （map结构json）
                animation:animation//动画效果参数等,动画参数由原生同学构建一下，先构建最基本的从哪打开，多少毫秒
            }
        }, function (ret, err) {
        });

    }


    //打开小程序页面(实际上就是打开h5页面，不过没有头部栏，右侧有一个三个小点和关闭按钮 就是样子和微信一样)
    /**
     *
     * @param url
     */
    _proto.openMiniPage=function(url){
        api.accessNative({
            name: "openMiniPage",//
            extra: {
                url:url ,//页面地址
            }
        }, function (ret, err) {
        });

    }
    Quakoo.class(QuakooApp, 'QuakooApp');


    return QuakooApp;
})();


var QuakooDevice = (function () {
    function QuakooDevice() {
    }

    var _proto = QuakooDevice.prototype;



    /**
     * 获取当前连接的wifi信息
     * @param callBack ret.name wifi名字,ret.ssid wifi ssid
     */
    _proto.getWifi = function (callBack) {
        api.accessNative({
            name: "getWifi",
            extra: {
            }
        }, function (ret, err) {
        });

        api.addEventListener({
            name: 'getWifiListener' //监听选择完的事件
        }, function(ret, err) {
            //ret.name wifi名字
            //ret.ssid wifi ssid
            callBack(ret);
        });
    }


    Quakoo.class(QuakooDevice, 'QuakooDevice');

    return QuakooDevice;
})();


var QuakooMsg = (function () {
    function QuakooMsg() {
    }

    var _proto = QuakooMsg.prototype;



    /**
     * 调用原生方法展示加载中的组件 loading
     */
    _proto.showProgress = function () {
        // api.showProgress({});

         var divhub = api.require('divhub');
         divhub.startAnimating()
    }

    /**
     * 调用原生方法隐藏加载中的组件 loading
     */
    _proto.hideProgress = function () {
        // api.hideProgress({});

         var divhub = api.require('divhub');
         divhub.stopAnimating()
    }


    /**
     * 确认提示框（无取消按钮）
     * @param title 标题
     * @param cotent 内容
     * @param rightAction 用户点击确认按钮后回调
     */
    _proto.showFixDialog=function(title,cotent,rightAction){
        if(!this.dialogBox){
            this.dialogBox = api.require('dialogBox');
        }
        this.dialogBox.alert ({
            texts: {
                title: title,
                content:cotent,
                rightBtnTitle: '确定'
            },
            tapClose:true,
            styles:{
                bg: '#fff',
                w: 300,
                title:{
                    marginT : 20,
                    titleSize : 14,
                    titleColor : '#000'
                },
                content:{
                    color: '#000',
                    size: 16
                },
                right:{
                    marginB: 0,
                    marginL: 0,
                    w: 300,
                    h: 40,
                    corner: 0,
                    bg: '#000',
                    size: 14,
                    color:'#ffffff'
                }
            }
        },function(ret){
            if(ret.eventType == 'right'){
                rightAction();
                quakooMsg.dialogBox.close ({
                    dialogName: 'alert'
                });
            }
        });
    }



    //弹出两个按钮
    _proto.showDialog=function(title,cotent,leftBtnTitle,rightBtnTitle,rightAction,leftAction){
        if(!this.dialogBox){
            this.dialogBox = api.require('dialogBox');
        }
        var styles;
        if(title){
            styles = {
                bg: '#fff',
                w: 300,
                title:{
                    marginT : 20,
                    titleSize : 14,
                    titleColor : '#5AC8E1'
                },
                content:{
                    color: '#000',
                    size: 16
                },
                left:{
                    marginB: 0,
                    marginL: 0,
                    w: 150,
                    h: 40,
                    color:'#000000',
                    corner: 0,
                    bg: '#E8E8E8',
                    size: 14
                },
                right:{
                    marginB: 0,
                    marginL: 0,
                    w: 150,
                    h: 40,
                    corner: 0,
                    bg: '#282828',
                    size: 14,
                    color:'#ffffff'
                }
            }
        }else{
            styles = {
                bg: '#fff',
                w: 300,
                content:{
                    color: '#000',
                    size: 16
                },
                left:{
                    marginB: 0,
                    marginL: 0,
                    w: 150,
                    h: 40,
                    color:'#000000',
                    corner: 0,
                    bg: '#E8E8E8',
                    size: 14
                },
                right:{
                    marginB: 0,
                    marginL: 0,
                    w: 150,
                    h: 40,
                    corner: 0,
                    bg: '#282828',
                    size: 14,
                    color:'#ffffff'
                }
            }
        }
        this.dialogBox.alert ({
            texts: {
                title: title,
                content:cotent,
                leftBtnTitle: leftBtnTitle,
                rightBtnTitle: rightBtnTitle
            },
            tapClose:false,
            styles:styles
        },function(ret){
            if (ret.eventType == 'left') {
                if (leftAction) {
                    leftAction();
                }
                quakooMsg.dialogBox.close ({
                    dialogName: 'alert'
                });
            }else if(ret.eventType == 'right'){
                rightAction();
                quakooMsg.dialogBox.close ({
                    dialogName: 'alert'
                });
            }
        });
    }


    //弹出一个按钮
    _proto.showOneDialog=function(title,cotent,rightBtnTitle,rightAction){
        if(!this.dialogBox){
            this.dialogBox = api.require('dialogBox');
        }
        dialogBox.alert ({
            texts: {
                title: title,
                content:cotent,
                rightBtnTitle: rightBtnTitle
            },
            tapClose:false,
            styles:{
                bg: '#fff',
                w: 300,
                title:{
                    marginT : 20,
                    titleSize : 14,
                    titleColor : '#FFA257'
                },
                content:{
                    color: '#000',
                    size: 16
                },
                right:{
                    marginB: 0,
                    marginL: 0,
                    w: 300,
                    h: 40,
                    corner: 0,
                    bg: '#FFA257',
                    size: 14,
                    color:'#ffffff'
                }
            }
        },function(ret){
            if(ret.eventType == 'right'){
                rightAction();
                dialogBox.close ({
                    dialogName: 'alert'
                });
            }
        });
    }


    /**
     * 带输入框的弹出框
     *
     */

    _proto.dialogBoxInput = function(keyboardType,title,placeholder,fn){
        dialogBox.input({
            tapClose:true,
            keyboardType: keyboardType,
            texts: {
                title: title,
                placeholder: placeholder,
                leftBtnTitle: '取消',
                rightBtnTitle: '确定'
            },
            styles: {
                bg: '#fff',
                corner:3,
                w: 300,
                h: 145,
                title: {
                    h: 30,
                    marginT:20,
                    alignment: 'center',
                    size: 18,
                    color: '#333333',
                },
                input: {
                    h:31,
                    marginT :3,
                    marginB:20,
                    textSize: 16,
                    textColor: '#333' ,
                    alignment: 'center',
                },
                dividingLine: {             //（可选项）JSON对象；按钮与输入框之间的分割线的配置
                    width: 0,              //（可选项）数字类型；分割线粗细；默认：0.5
                    color: 'rgba(0,0,0,0)'         //（可选项）字符串类型；分割线颜色，支持rgb、rgba、#；默认：#696969
                },
                left: {
                    bg: '#fff',
                    color: '#999999',
                    size: 15,
                    h:50,
                    corner: 3,
                },
                right: {
                    marginT:22,
                    bg: '#fff',
                    color: '#ffa257',
                    size: 15,
                    h:50,
                    corner: 3,
                },
                horizontalLine:{
                    color:'rgba(237,237,237,1)', //（可选项）字符串类型；左右按钮上边横线的颜色，支持rgb、rgba、#；默认：'rgba(245,245,245,0)'
                    height:1                //（可选项）数字类型；左右边按钮横线的高度；默认：0
                },
                verticalLine:{
                    color:'rgba(237,237,237,1)', //（可选项）字符串类型；左右按钮中间竖线的颜色，支持rgb、rgba、#；默认：'rgba(245,245,245,0)'
                    width:1                 //（可选项）数字类型；左右边按钮竖线的高度；默认：0

                }
            }
        }, function(ret) {
            if (ret.eventType == 'left') {
                dialogBox.close({
                    dialogName: 'input'
                });
            }else if(ret.eventType == 'right'){
                fn(ret)

            }
        });
    }
    /**
     * toast提示消息
     * @param message
     */
    _proto.toast=function(message){
        var strDM = api.systemType;//系统
        var msg=message;
        if(quakooUtils.isJson(message)){
            msg=message.msg;
        }
        if (strDM == 'ios') {
            api.toast({
                msg: msg,
                duration:2000,
                location: 'middle'
            });
        } else if (strDM == 'android') {
            api.toast({
                msg: msg,
                duration:2000,
                location: 'bottom'
            });
        }
    }

    Quakoo.class(QuakooMsg, 'QuakooMsg');

    return QuakooMsg;
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
            quakooDb.setItem("userInfo",user);
        }
    }

    /**
     * 移除数据库中的用户信息
     */
    _proto.removeUserInfo=function(){
        quakooDb.removeItem("userInfo");
    }

    /**
     * 从数据库中获取用户信息
     * @returns {*}
     */
    _proto.getUserInfo=function() {
        return quakooDb.getItem("userInfo");
    }

    /**
     * 注册游客
     * @param callback
     */
    _proto.registerCustomer=function(callback) {
        quakooData._ajaxSubmitData(config.getUrl_web_user_loadCustomer(),{},function (ret) {
            ret.data.realUser = false;
            quakooUser.setUserInfo(ret.data);
            if(callback){
                callback();
            }
        },false,true,false);
    }


    /**
     * 判断是否是登陆用户
     * @returns {boolean}
     */
    _proto.isLoginUser=function() {
        var user=this.getUserInfo();
        return user&&user.realUser;
    }



    /**
     * 注册原生
     * @param callback
     */
    _proto.registerNative=function() {
        var userInfo=this.getUserInfo();
        if(quakooUtils.isBlack(userInfo.icon)){
            userInfo.icon = '';
        }
        if(quakooUtils.isBlack(userInfo.name)){
            userInfo.name = '';
        }
        api.accessNative({
            name: "login",
            extra: {
                token: userInfo.token,
                id: userInfo.id,
                chatid: userInfo.id,// （用户聊天id）
	            noticeId: userInfo.id,// 推送ID（用户主键ID）
                icon:userInfo.icon,
                name:userInfo.name,
                chatServerUrl:config.chatServerUrl,
                chatPort:config.chatPort,
                pushServerUrl:config.pushServerUrl,
                pushPort:config.pushPort,
                uploadImgUrl:config.uploadImageUrl
            }
        }, function (ret, err) {
        });
    }


    Quakoo.class(QuakooUser, 'QuakooUser');



    return QuakooUser;
})();


/**
 * QuakooPage
 * addDataToHtml(results,append)
 * @type {QuakooPage}
 */
var QuakooPage = (function () {
    function QuakooPage(drawingData, url, getData) {
        this.orgGetData = quakooUtils.deepCopy(getData);
        this.drawingData = drawingData;
        this.url = url;
        this.getData = getData;

        this.downNum = config.quakooPage_downNum;
        this.upNum = config.quakooDate_upNum;
        this.threshold = config.quakooDate_threshold;
        this.openUpAction = config.quakooDate_openUpAction;

        this.currentNum = 0;//当前页面上拉加载的游标
        this.preloadNumSize = 40;//预加载个数
        this.isGetDataByUp = false;
        this.isGetDataByDown = false;
        this.isGetDataByPerload = false;
        this.hasWaitingGetUp = false;//如果上拉加载和异步预加载冲突，上拉加载会开启等待效果，等到异步加载的结果
        this.hasNoMoreData = false;
        this.cacheKey = this._createCacheKey(url, this.orgGetData);
        this.cursorOnDbError = 0;//db错误的时候的游标
        this.isInit = false;
        this.ext = null;

        var _quakooPage = this;
        api.setFrameAttr({
            name: api.frameName,
            bounces: true
        });
        if (config.quakooDate_customRefreshHeaderInfo) {
            api.setCustomRefreshHeaderInfo({
                bgColor: '#fff',
                //isScale: true,
                image: {
                    pull: 'widget://image/refresh/1.jpg',
                    transform: [
                        'widget://image/refresh/1.jpg',
                        'widget://image/refresh/2.jpg',
                        'widget://image/refresh/3.jpg',
                        'widget://image/refresh/4.jpg',
                        'widget://image/refresh/5.jpg',
                        'widget://image/refresh/6.jpg',
                        'widget://image/refresh/7.jpg',
                        'widget://image/refresh/8.jpg'
                    ],
                    load: [
                        'widget://image/refresh/8.jpg',
                        'widget://image/refresh/7.jpg',
                        'widget://image/refresh/6.jpg',
                        'widget://image/refresh/5.jpg',
                        'widget://image/refresh/4.jpg',
                        'widget://image/refresh/3.jpg',
                        'widget://image/refresh/2.jpg',
                        'widget://image/refresh/1.jpg'
                    ]
                }
            }, function (ret, err) {
                _quakooPage._afterPullDown();
            });
        } else {
            api.setRefreshHeaderInfo({
                visible: true,
                bgColor: 'rgba(0,0,0,0)',
                textColor: '#666',
                textDown: '下拉刷新...',
                textUp: '松开刷新...'
            }, function (ret, err) {
                _quakooPage._afterPullDown();
            });
        }


        api.addEventListener({
            name: 'scrolltobottom',
            extra: {
                threshold: _quakooPage.threshold   //设置距离底部多少就触发
            }
        }, function (ret, err) {
            _quakooPage._afterPullUp();
        });



        this.initData();
    }


    var _proto = QuakooPage.prototype;


    /**
     * 开始刷新数据
     */
    _proto.initData = function () {
        //api.refreshHeaderLoading();
        this.showProgress=true;
        quakooMsg.showProgress();
        this._afterPullDown();
    }

    /**
     * 下拉刷新结束时候，触发的方法，
     * 从服务器获取数据，刷新数据或者展示数据
     */
    _proto._afterPullDown=function(){
        var _quakooPage = this;
        if (this.isInit) {
            this._refresh();
        } else {
            this.isInit = true;
            this._getDataByDown(function (result) {
                _quakooPage.drawingData(result, false);
                api.sendEvent({
                    name: 'refresh_data_' + api.frameName,
                    extra: {
                        refresh : 'isInit'
                    }
                });
            }, function (result) {
                _quakooPage.drawingData(result, false);
                api.sendEvent({
                    name: 'refresh_data_' + api.frameName,
                    extra: {
                        refresh : 'isInit'
                    }
                });

            });
        }
    }


    _proto._refresh = function () {
        var _quakooPage = this;
        var loadMoreDiv = document.getElementById("loadMoreDiv");
        if (loadMoreDiv) {
            document.body.removeChild(loadMoreDiv);
        }
        this._getDataByDown(function (result) {
            _quakooPage.drawingData(result, false);
            api.sendEvent({
                name: 'refresh_data_' + api.frameName,
                extra: {
                    refresh : 'refresh_done'
                }
            });
        });

    }




    _proto._afterPullUp = function () {
        var _quakooPage = this;
        this._getDataByUp(function (result) {
            _quakooPage.drawingData(result, true)
        });
    }


    _proto._getDataByDown = function (callBackOnGetDatas, callBackOnGetCacheDatas) {
        var _quakooPage = this;
        var startTime = new Date().getTime();
        //判断当前是否正在下拉刷新
        if (this.isGetDataByDown) {
            return;
        }
        this.isGetDataByDown = true;

        //如果有上拉，下拉也不生效（防止上拉后重置数据库，之前的下拉请求返回的数据扰乱本地数据库）
        if (this.isGetDataByUp) {
            this._downEnd();
            return;
        }
        var getData = quakooUtils.deepCopy(this.getData);
        getData.token = quakooUser.getUserInfo().token;
        getData.cursor = 0;
        getData.size = this.preloadNumSize; //默认加载2倍


        //从缓存拉取数据
        var cacheDataResultStr="";
        if (callBackOnGetCacheDatas) {
            var cacheDataResult = [];
            var value=quakooDb.getItem(this.cacheKey);
            if (quakooUtils.isNotBlack(value)) {
                if (value.data) {
                    var j = 0;
                    var k = j + _quakooPage.downNum;
                    //加载数据
                    for (; j < k && j < value.data.length; j++) {
                        cacheDataResult.push(value.data[j]);
                    }
                    _quakooPage.currentNum = cacheDataResult.length;
                    cacheDataResultStr=JSON.stringify(cacheDataResult);
                    callBackOnGetCacheDatas(cacheDataResult);
                }
            }

        }

        //去服务器拉取数据
        this._getPagerDataFormServer(getData, true, true, function (ret, err) {
            _quakooPage.hasNoMoreData = false;
            if (ret.status) {
                var serverResult = ret.data;
                //如果写入数据库成功展示一半的数据
                if (ret.cached) {
                    var dataResult = [];
                    for (var n = 0; n < _quakooPage.downNum && n < serverResult.data.length; n++) {
                        dataResult.push(serverResult.data[n]);
                    }
                    _quakooPage.currentNum = dataResult.length;
                    if(cacheDataResultStr!=JSON.stringify(dataResult)||!callBackOnGetCacheDatas){
                        callBackOnGetDatas(dataResult);
                    }

                    if (!serverResult.hasnext && serverResult.data.length <= _quakooPage.downNum) {
                        _quakooPage.hasNoMoreData = true;
                    }
                } else {
                    //写入数据库失败，在页面上数据全部展示出来
                    if(cacheDataResultStr!=JSON.stringify(dataResult)){
                        callBackOnGetDatas(serverResult.data);
                    }
                    _quakooPage.cursorOnDbError = serverResult.nextCursor;
                    if (!serverResult.hasnext) {
                        _quakooPage.hasNoMoreData = true;
                    }
                }
            }
            _quakooPage._downEnd();
        });
    }


    _proto._getDataByUp = function (callBackOnGetDatas) {
        var _quakooPage = this;
        //检查当前是否正在上拉或者下拉.
        if (this.isGetDataByDown || this.isGetDataByUp) {
            return;
        }
        this.isGetDataByUp = true;
        //如果已经没有数据了 直接返回
        if (this.hasNoMoreData) {
            this.isGetDataByUp = false;
            return;
        }
        var dataResult = [];
        var getData = quakooUtils.deepCopy(this.getData);
        getData.token = quakooUser.getUserInfo().token;

        //开始上拉效果
        this._startUpEffect();
        setTimeout(function () {
            //如果数据库存入失败,pageCursor有值
            if (_quakooPage.cursorOnDbError > 0) {
                getData.cursor = _quakooPage.cursorOnDbError;
                getData.size = _quakooPage.upNum;
                _quakooPage._getPagerDataFormServer(getData, false, true, function (ret, err) {
                    if (ret.status) {
                        var serverResult = ret.data;
                        callBackOnGetDatas(serverResult.data);
                        _quakooPage.cursorOnDbError = serverResult.nextCursor;
                    }
                    _quakooPage._upEnd();
                });
                return;
            }

            //从缓存中加载数据
            var value=quakooDb.getItem(_quakooPage.cacheKey);
            if ( quakooUtils.isBlack(value)) {
                _quakooPage._upEnd();
                return;
            }
            //获取当前上拉数据在缓存当中得位置
            //游标数据的开始位置
            var j = _quakooPage.currentNum;
            var k = _quakooPage.currentNum + _quakooPage.upNum;
            //加载数据
            if(value &&value.data){
                for (; j < k && j < value.data.length; j++) {
                    dataResult.push(value.data[j]);
                }
            }


            //没有数据
            if (dataResult.length == 0) {

                //如果当前正在异步加载，那么上拉加载的效果不停，不调用end方法，由异步加载后的程序来调用。
                if (_quakooPage.isGetDataByPerload) {
                    _quakooPage.hasWaitingGetUp = true;
                    return;
                }
                //设置游标为当前游标。开始异步拉取数据
                getData.cursor = value.nextCursor;
                getData.size = _quakooPage.upNum * 2;
                _quakooPage._getPagerDataFormServer(getData, true, true, function (ret, err) {
                    if (ret.status) {
                        var serverResult = ret.data;
                        //如果写入数据库成功展示一半的数据
                        if (ret.cached) {
                            var dataResult = [];
                            for (var n = 0; n < _quakooPage.upNum && n < serverResult.data.length; n++) {
                                dataResult.push(serverResult.data[n]);
                            }

                            _quakooPage.currentNum = _quakooPage.currentNum + dataResult.length;
                            callBackOnGetDatas(dataResult);

                            if (serverResult.hasnext == false && serverResult.data.length <= _quakooPage.upNum) {
                                _quakooPage.hasNoMoreData = true;
                            }
                        } else {
                            //写入数据库失败，在页面上一次展示多一倍的数据
                            callBackOnGetDatas(serverResult.data);
                            if (serverResult.hasnext == false) {

                                _quakooPage.hasNoMoreData = true;
                            }
                            _quakooPage.cursorOnDbError = serverResult.nextCursor;
                        }
                    }

                    _quakooPage._upEnd();
                });

            } else {
                //有数据
                _quakooPage.currentNum = _quakooPage.currentNum + dataResult.length;
                callBackOnGetDatas(dataResult);
                if (!value.hasnext) {
                    if (dataResult.length < _quakooPage.upNum) {
                        _quakooPage.hasNoMoreData = true;
                    }
                    if (dataResult.length == _quakooPage.upNum && j >= value.data.length) {
                        _quakooPage.hasNoMoreData = true;
                    }
                }
                _quakooPage._upEnd();

                if (value.hasnext == false) {
                    return;
                }
                //异步加载
                if (value.data.length - j <= _quakooPage.upNum && !_quakooPage.isGetDataByPerload) {
                    _quakooPage.isGetDataByPerload = true;
                    getData.cursor = value.nextCursor;
                    getData.size = _quakooPage.preloadNumSize;

                    _quakooPage._getPagerDataFormServer(getData, true, false, function (ret, err) {
                        if (ret.status) {
                            var serverResult = ret.data;
                            //如果写入数据库成功展示一半的数据
                            if (ret.cached) {
                                var dataResult = [];
                                for (var n = 0; n < _quakooPage.upNum && n < serverResult.data.length; n++) {
                                    dataResult.push(serverResult.data[n]);
                                }
                                if (_quakooPage.hasWaitingGetUp) {
                                    _quakooPage.currentNum = _quakooPage.currentNum + dataResult.length;
                                    callBackOnGetDatas(dataResult);
                                    if (!serverResult.hasnext && serverResult.data.length <= _quakooPage.upNum) {
                                        _quakooPage.hasNoMoreData = true;
                                    }
                                }
                            } else {
                                //写入数据库失败，在页面上一次展示多一倍的数据
                                if (_quakooPage.hasWaitingGetUp) {
                                    callBackOnGetDatas(serverResult.data);
                                }
                                if (serverResult.hasnext == false) {
                                    _quakooPage.hasNoMoreData = true;
                                }
                                _quakooPage.cursorOnDbError = serverResult.nextCursor;
                            }
                        } else {
                            if (_quakooPage.hasWaitingGetUp) {
                                quakooMsg.toast("获取信息失败");
                            }
                        }

                        if (_quakooPage.hasWaitingGetUp) {
                            _quakooPage._upEnd();
                        }
                        _quakooPage.isGetDataByPerload = false;
                        _quakooPage.hasWaitingGetUp = false;
                    });

                }

            }

        }, 0);


    }
    _proto._downEnd = function (nomoreData) {
        this._endUpEffect();
        if(this.showProgress){
            this.showProgress=false;
            quakooMsg.hideProgress();
        }else{
            api.refreshHeaderLoadDone();
        }
        this.isGetDataByDown = false;
    }

    _proto._upEnd = function (nomoreData) {
        this._endUpEffect();
        this.isGetDataByUp = false;
    }
    _proto._startUpEffect = function () {
        var loadMoreDiv = document.getElementById("loadMoreDiv");
        if (loadMoreDiv) {
        } else {
            if (this.openUpAction) {
                var div = document.createElement("div");
                div.setAttribute("id", "loadMoreDiv");
                // div.innerHTML = '<img  src="' + upLoadingImgSrc + '"/> 全力加载中...';
                div.innerHTML = '全力加载中...';
                document.body.appendChild(div);
            }
        }
    }
    _proto._endUpEffect = function () {
        var loadMoreDiv = document.getElementById("loadMoreDiv");
        if (loadMoreDiv) {
            if (this.hasNoMoreData) {
                setTimeout(function () {
                    loadMoreDiv.innerHTML = '已显示全部信息';
                }, 200);
            }
        } else {
            if (this.hasNoMoreData && this.openUpAction) {
                var div = document.createElement("div");
                div.setAttribute("id", "loadMoreDiv");
                div.innerHTML = '已显示全部信息';
                document.body.appendChild(div);
            }
        }
    }


    _proto._getPagerDataFormServer = function (getData, saveToDb, alertServerFail, callback) {
        var _quakooPage = this;
        if (this.isGetDataByDown) {
            this.ext = null;
        }
        if (quakooUtils.isNotBlack(this.ext)) {
            getData.ext = this.ext;
        }
        getData._version=config.version;
        api.ajax({
            url: _quakooPage.url,
            method: 'post',
            timeout: 30,
            dataType: 'json',
            returnAll: false,
            headers: {
                "Accept-Encoding": "gzip",
                "type": 1
            },
            data: {
                values: getData
            }
        }, function (ret, err) {
            //读取数据失败。。
            if (err) {
                var msg = err.msg || "网络连接错误，请稍后再试！";
                quakooMsg.toast(msg);
                callback({status: false, cached: false}, err);
                return;
            }
            if (ret && (ret.code == 400||ret.code == 401 )&&!context.isReLogin) {
                context.isReLogin=true;
                quakooUser.removeUserInfo();
                quakooMsg.showFixDialog("温馨提示", "您的账号在其他手机上登录，您被迫下线！", "确定", function () {
                    context.isReLogin=false;
                    quakooApp.openNewOfIndex("login", "../../html/login/login.html", {}, {slidBackEnabled: false});
                });
                return;
            }
            if (!ret) {
                if (alertServerFail) {
                    quakooMsg.toast({msg: "获取信息失败"});
                }
                callback({status: false, cached: false}, err);
                return;
            }

            var serverResult = ret;
            if (quakooUtils.isBlack(serverResult.data)) {
                serverResult.data = [];
            }


            //不保存到数据库，直接返回
            if (!saveToDb) {
                callback({status: true, cached: false, data: serverResult}, err);
                return;
            }
            var value=quakooDb.getItem(_quakooPage.cacheKey);
            if(quakooUtils.isJson(value)){
                if (value.data && quakooUtils.isNotBlack(getData.cursor) && getData.cursor != "0") {
                    for (var n = 0; n < serverResult.data.length; n++) {
                        value.data.push(serverResult.data[n]);
                    }
                } else {
                    value.data = serverResult.data;
                }
            }else{
                value = {};
            }

            value.hasnext = serverResult.hasnext;
            value.nextCursor = serverResult.nextCursor;
            //写入缓存
            ret=quakooDb.setItem(_quakooPage.cacheKey, value);
            if (ret.status) {
                callback({status: true, cached: true, data: serverResult}, err);
            } else {
                callback({status: true, cached: false, data: serverResult}, err);
            }

        });
    }

    _proto._createCacheKey = function (url, getData) {
        var cacheKey = url;
        for (var key in getData) {
            if (key != "cursor" && key != "size") {
                cacheKey = cacheKey + key + getData[key];
            }
        }
        return md5.md5(cacheKey);
    }


    Quakoo.class(QuakooPage, 'QuakooPage');


    return QuakooPage;
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
     * 获取数据的ajax请求
     * @param url 请求url
     * @param reqData 请求的数据
     * @param callBackOnData(ret) 有缓存的数据回回调这个方法，当请求的时候发现新数据跟缓存不一致也会调用这个方法。有可能调用两次。
     */
    _proto.ajaxGetData = function (url, reqData, callBackOnData) {
        this._ajaxGetData(url,reqData,callBackOnData,true);
    }

    /**
     * 获取数据的ajax请求（带错误的回调）
     * @param url 请求url
     * @param reqData 请求的数据
     * @param callBackOnData(ret) 有缓存的数据回回调这个方法，当请求的时候发现新数据跟缓存不一致也会调用这个方法。有可能调用两次。
     */
    _proto.ajaxGetDataWithError = function (url, reqData, callBackOnData,callBackOnError) {
        this._ajaxGetData(url,reqData,callBackOnData,true,true,true,callBackOnError);
    }

    /**
     * 获取数据的ajax请求(不带)
     * @param url 请求url
     * @param reqData 请求的数据
     * @param callBackOnData(ret) 有缓存的数据回回调这个方法，当请求的时候发现新数据跟缓存不一致也会调用这个方法。有可能调用两次。
     */
    _proto.ajaxGetDataWithOutUser = function (url, reqData, callBackOnData) {
        this._ajaxSubmitData(url,reqData,callBackOnData,false);
    }

    /**
     * 获取数据的ajax请求
     * @param url 请求url
     * @param reqData 请求的数据
     * @param callBackOnData(ret) 有缓存的数据回回调这个方法，当请求的时候发现新数据跟缓存不一致也会调用这个方法。有可能调用两次。
     * @param addUserToken 是否自动添加token true||false 默认为true
     * @param showErrorMsg 是否展示错误提示 (默认为展示)
     * @param showProcess 是否展示加载中的提示框 (默认为展示)
     * @param callBackOnError(ret,err) 当加载错误的时候回调方法
     */
    _proto._ajaxGetData = function (url, reqData, callBackOnData,addUserToken,showErrorMsg,showProcess,callBackOnError) {
        if(showProcess!=false){
            quakooMsg.showProgress({});
        }

        //组装请求参数
        if (quakooUtils.isBlack(reqData)) {
            reqData = {};
        }
        var user = quakooUser.getUserInfo();
        if(addUserToken){
            reqData.token = user.token;
        }
        reqData._version=config.version;

        //获取缓存数据
        var cacheKey = this._createCacheKey(url, reqData);
        var value=quakooDb.getItem(cacheKey);
        var stringValue = JSON.stringify(value);
        if (quakooUtils.isNotBlack(value)) {
            callBackOnData(value);
        }
        //请求服务器获取数据
        api.ajax({
            url: url,
            method: 'post',
            timeout: 30,
            dataType: 'json',
            returnAll: false,
            headers: {
                "Accept-Encoding": "gzip",
                "type": 1
            },
            data: {
                values: reqData
            }
        }, function (ret, err) {
            if (ret) {
                var data = ret;
                if (data.success == false) {
                    if (ret && (ret.code == 400||ret.code == 401 )&&!context.isReLogin) {
                        context.isReLogin=true;
                        quakooUser.removeUserInfo();
                        quakooMsg.showFixDialog("温馨提示", "您的账号在其他手机上登录，您被迫下线！", "确定", function () {
                            context.isReLogin=false;
                            quakooApp.openNewWindow("login", "../../html/login/login.html", {}, {slidBackEnabled: false});
                        });
                        return;
                    } else {
                        var errorMessage = data.msg||data.errorMessage || '获取信息失败';
                        if (showErrorMsg!=false) {
                            quakooMsg.toast(errorMessage);
                        }
                        if(callBackOnError){
                            callBackOnError(ret,err);
                        }
                    }

                } else if (JSON.stringify(data) != stringValue) {
                    quakooDb.setItem(cacheKey, (data));
                    callBackOnData(data);
                }
            } else {
                if (showErrorMsg!=false) {
                    if(config.isTest){
                        alert("req"+JSON.stringify(reqData)+","+url+""+JSON.stringify(ret)+",err"+JSON.stringify(err));
                    }else{
                        quakooMsg.toast("获取信息失败");
                    }
                }
                if(callBackOnError){
                    callBackOnError(ret,err);
                }
            }
            if (showProcess!=false) {
                //api.refreshHeaderLoadDone();//如果采用下拉动作 来刷新页面（非分页）
                quakooMsg.hideProgress({});
            }
        });

    }




    /**
     * 提交ajax请求
     * 不需要缓存
     * @param url 请求url
     * @param reqData 请求的数据
     * @param callBackOnServerData(ret) 服务器传输回数据，回调方法
     */
    _proto.ajaxSubmitData = function (url, reqData, callBackOnServerData) {
        this._ajaxSubmitData(url, reqData, callBackOnServerData,true);
    }



    /**
     * 上传ajax请求
     * 不需要缓存
     * @param url 请求url
     * @param callback 回调函数
     * @param errback(ret) 上传失败方法
     */
    _proto.uploadImg = function(file,callback,errback) {

        quakooMsg.showProgress({});
        api.ajax({
            url:getUploadImageUrl,
            method: 'post',
            timeout: 120,
            report: true,
            dataType: 'json',
            returnAll: false,
            data: {
                files: {file: file}
            }
        }, function (ret, err) {
            quakooMsg.hideProgress({});
            if (ret.status == 1) {
               if(callback){
                   callback(ret);
               }
            }else {
                if(errback){
                    errback(ret);
                }
            }

        });
    }


    /**
     * 提交ajax请求
     * 不需要缓存
     * @param url 请求url
     * @param reqData 请求的数据
     * @param callBackOnServerData(ret) 服务器传输回数据，回调方法
     * @param addUserToken 是否自动添加token true||false
     * @param showErrorMsg 是否展示错误提示 (默认为展示)
     * @param showProcess 是否展示加载中的提示框 (默认为展示)
     * @param callBackOnError(ret,err) 当加载错误的时候回调方法
     */
    _proto._ajaxSubmitData = function (url, reqData, callBackOnServerData,addUserToken,showErrorMsg,showProcess,callBackOnError) {
        if(showProcess!=false){
            quakooMsg.showProgress({});
        }

        //组装请求参数
        if (quakooUtils.isBlack(reqData)) {
            reqData = {};
        }
        var user = quakooUser.getUserInfo();
        if(addUserToken){
            reqData.token = user.token;
        }
        reqData._version=config.version;

        //请求服务器获取数据
        api.ajax({
            url: url,
            method: 'post',
            timeout: 30,
            dataType: 'json',
            returnAll: false,
            headers: {
                "Accept-Encoding": "gzip",
                "type": 1
            },
            data: {
                values: reqData
            }
        }, function (ret, err) {
            if (ret) {
                var data = ret;
                if (data.success == false) {
                    if (ret && (ret.code == 400||ret.code == 401 )&&!context.isReLogin) {
                        context.isReLogin=true;
                        quakooUser.removeUserInfo();
                        quakooMsg.showFixDialog("温馨提示", "您的账号在其他手机上登录，您被迫下线！", "确定", function () {
                            context.isReLogin=false;
                            quakooApp.openNewWindow("login", "../../html/login/login.html", {}, {slidBackEnabled: false});
                        });
                        return;
                    } else {
                        var errorMessage = data.msg||data.errorMessage||data.data || '获取信息失败';
                        if (showErrorMsg!=false) {
                            quakooMsg.toast(errorMessage);
                        }
                        if(callBackOnError){
                            callBackOnError(ret,err);
                        }
                    }

                } else {
                    callBackOnServerData(data);
                }
            } else {
                if (showErrorMsg!=false) {
                    quakooMsg.toast("获取信息失败");
                }
                if(callBackOnError){
                    callBackOnError(ret,err);
                }
            }
            if (showProcess!=false) {
                //api.refreshHeaderLoadDone();//如果采用下拉动作 来刷新页面（非分页）
                quakooMsg.hideProgress({});
            }
        });



    }

    Quakoo.class(QuakooData, 'QuakooData');






    return QuakooData;
})();

