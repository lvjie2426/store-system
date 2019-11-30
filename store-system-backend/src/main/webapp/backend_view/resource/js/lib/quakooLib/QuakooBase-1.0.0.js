/**
 *  雀科科技- http://www.quakoo.com
 *
 *  基础开发需要的类
 *  主要内容是一些工具类，所有项目都需要引用（APP项目，H5网站，后台）。
 *
 *  JS基础类规则
 *  1.命名规则：类名（大写开头），变量（小写开头），方法（小写开头），驼峰命名法
 *  2.内部调用的方法采用下划线开头。给外部调用的方法采用小写字母开头
 *  3.开发中，地址相关的请尽量使用相对路径，不要使用绝对路径  (widget://)混合开发的时候
 *
 *
 *  框架使用规则
 *  （由于apiCloud里面的组件质量参差不齐，我们自己也封装了很多组件）
 *  1.如果框架里面已经有的，请勿直接使用apiCloud方法。
 *  2.如果直接使用apiCLoud方法，需要在前端讨论组里面，发起询问。
 *  3.修改框架请提交给框架指定维护人员。
 *
 *
 *
 *  quakooBase里面的代码跟apiCloud没有关系，请不要把原生的部分内容放到这个项目里面
 *  H5混合开发APP，引用的JS（quakooBase,quakooConfig,quakooBaseApp,quakooBaseAppBusiness）
 *  H5网站开发，引用的JS（quakooBase,quakooConfig,quakooBaseh5）
 *  PC网站，后台网站开发，引用的JS（quakooBase,quakooConfig,quakooBasePc）
 *
 *
 *  quakooConfig
 *  跟后端积木工程对应的接口请保持所有项目一致，积木工程接口，请查看积木工程接口文档。
 *  积木工程所有项目接口和返回的数据都是一致的（除了服务器地址不一样）。
 *  这个类需要继承。
 *
 *  quakooBase
 *  里面包含了一些基础的工具类供所有项目使用
 *
 *  quakooBaseApp
 *  基础混合开发APP需要的类（主要是各种插件）
 *  包含基本的插件，基本操作（打开页面，跳转等），数据库,文件,图片处理,视频处理，二维码，版本控制，热更新，用户管理，系统初始化，等等
 *  数据库已经改成同步方法，存储请统一使用数据库，不要使用浏览器存储localStorage。数据库的存储KEY以用户ID开头，所以可以直接切换用户，不需要清理存储。
 *
 *  quakooBaseAppBusiness
 *  混合开发APP需要的类（跟业务相关的逻辑）
 *
 *
 */


var Quakoo=window.Quakoo=(function(window,document){
    var Quakoo={
        __internals:[],
        __packages:{},
        __classmap:{'Object':Object,'Function':Function,'Array':Array,'String':String},
        __sysClass:{'object':'Object','array':'Array','string':'String','dictionary':'Dictionary'},
        __propun:{writable: true,enumerable: false,configurable: true},
        __presubstr:String.prototype.substr,
        __substr:function(ofs,sz){return arguments.length==1?Quakoo.__presubstr.call(this,ofs):Quakoo.__presubstr.call(this,ofs,sz>0?sz:(this.length+sz));},
        __init:function(_classs){_classs.forEach(function(o){o.__init$ && o.__init$();});},
        __isClass:function(o){return o && (o.__isclass || o==Object || o==String || o==Array);},
        __newvec:function(sz,value){
            var d=[];
            d.length=sz;
            for(var i=0;i<sz;i++) d[i]=value;
            return d;
        },
        __extend:function(d,b){
            for (var p in b){
                if (!b.hasOwnProperty(p)) continue;
                var gs=Object.getOwnPropertyDescriptor(b, p);
                var g = gs.get, s = gs.set;
                if ( g || s ) {
                    if ( g && s)
                        Object.defineProperty(d,p,gs);
                    else{
                        g && Object.defineProperty(d, p, g);
                        s && Object.defineProperty(d, p, s);
                    }
                }
                else d[p] = b[p];
            }
            function __() { Quakoo.un(this,'constructor',d); }__.prototype=b.prototype;d.prototype=new __();Quakoo.un(d.prototype,'__imps',Quakoo.__copy({},b.prototype.__imps));
        },
        __copy:function(dec,src){
            if(!src) return null;
            dec=dec||{};
            for(var i in src) dec[i]=src[i];
            return dec;
        },
        __package:function(name,o){
            if(Quakoo.__packages[name]) return;
            Quakoo.__packages[name]=true;
            var p=window,strs=name.split('.');
            if(strs.length>1){
                for(var i=0,sz=strs.length-1;i<sz;i++){
                    var c=p[strs[i]];
                    p=c?c:(p[strs[i]]={});
                }
            }
            p[strs[strs.length-1]] || (p[strs[strs.length-1]]=o||{});
        },
        __hasOwnProperty:function(name,o){
            o=o ||this;
            function classHas(name,o){
                if(Object.hasOwnProperty.call(o.prototype,name)) return true;
                var s=o.prototype.__super;
                return s==null?null:classHas(name,s);
            }
            return (Object.hasOwnProperty.call(o,name)) || classHas(name,o.__class);
        },
        __typeof:function(o,value){
            if(!o || !value) return false;
            if(value===String) return (typeof o==='string');
            if(value===Number) return (typeof o==='number');
            if(value.__interface__) value=value.__interface__;
            else if(typeof value!='string')  return (o instanceof value);
            return (o.__imps && o.__imps[value]) || (o.__class==value);
        },
        __as:function(value,type){
            return (this.__typeof(value,type))?value:null;
        },
        __int:function(value){
            return value?parseInt(value):0;
        },
        interface:function(name,_super){
            Quakoo.__package(name,{});
            var ins=Quakoo.__internals;
            var a=ins[name]=ins[name] || {self:name};
            if(_super)
            {
                var supers=_super.split(',');
                a.extend=[];
                for(var i=0;i<supers.length;i++){
                    var nm=supers[i];
                    ins[nm]=ins[nm] || {self:nm};
                    a.extend.push(ins[nm]);
                }
            }
            var o=window,words=name.split('.');
            for(var i=0;i<words.length-1;i++) o=o[words[i]];
            o[words[words.length-1]]={__interface__:name};
        },
        class:function(o,fullName,_super,miniName){
            _super && Quakoo.__extend(o,_super);
            if(fullName){
                Quakoo.__package(fullName,o);
                Quakoo.__classmap[fullName]=o;
                if(fullName.indexOf('.')>0){
                    if(fullName.indexOf('quakoo.')==0){
                        var paths=fullName.split('.');
                        miniName=miniName || paths[paths.length-1];
                        if(Quakoo[miniName]) console.log("Warning!,this class["+miniName+"] already exist:",Quakoo[miniName]);
                        Quakoo[miniName]=o;
                    }
                }
                else {
                    if(fullName=="Main")
                        window.Main=o;
                    else{
                        if(Quakoo[fullName]){
                            console.log("Error!,this class["+fullName+"] already exist:",Quakoo[fullName]);
                        }
                        Quakoo[fullName]=o;
                    }
                }
            }
            var un=Quakoo.un,p=o.prototype;
            un(p,'hasOwnProperty',Quakoo.__hasOwnProperty);
            un(p,'__class',o);
            un(p,'__super',_super);
            un(p,'__className',fullName);
            un(o,'__super',_super);
            un(o,'__className',fullName);
            un(o,'__isclass',true);
            un(o,'super',function(o){this.__super.call(o);});
        },
        imps:function(dec,src){
            if(!src) return null;
            var d=dec.__imps|| Quakoo.un(dec,'__imps',{});
            function __(name){
                var c,exs;
                if(! (c=Quakoo.__internals[name]) ) return;
                d[name]=true;
                if(!(exs=c.extend)) return;
                for(var i=0;i<exs.length;i++){
                    __(exs[i].self);
                }
            }
            for(var i in src) __(i);
        },
        superSet:function(clas,o,prop,value){
            var fun = clas.prototype["_$set_"+prop];
            fun && fun.call(o,value);
        },
        superGet:function(clas,o,prop){
            var fun = clas.prototype["_$get_"+prop];
            return fun?fun.call(o):null;
        },
        getset:function(isStatic,o,name,getfn,setfn){
            if(!isStatic){
                getfn && Quakoo.un(o,'_$get_'+name,getfn);
                setfn && Quakoo.un(o,'_$set_'+name,setfn);
            }
            else{
                getfn && (o['_$GET_'+name]=getfn);
                setfn && (o['_$SET_'+name]=setfn);
            }
            if(getfn && setfn)
                Object.defineProperty(o,name,{get:getfn,set:setfn,enumerable:false,configurable:true});
            else{
                getfn && Object.defineProperty(o,name,{get:getfn,enumerable:false,configurable:true});
                setfn && Object.defineProperty(o,name,{set:setfn,enumerable:false,configurable:true});
            }
        },
        static:function(_class,def){
            for(var i=0,sz=def.length;i<sz;i+=2){
                if(def[i]=='length')
                    _class.length=def[i+1].call(_class);
                else{
                    function tmp(){
                        var name=def[i];
                        var getfn=def[i+1];
                        Object.defineProperty(_class,name,{
                            get:function(){delete this[name];return this[name]=getfn.call(this);},
                            set:function(v){delete this[name];this[name]=v;},enumerable: true,configurable: true});
                    }
                    tmp();
                }
            }
        },
        un:function(obj,name,value){
            value || (value=obj[name]);
            Quakoo.__propun.value=value;
            Object.defineProperty(obj, name, Quakoo.__propun);
            return value;
        },
        uns:function(obj,names){
            names.forEach(function(o){Quakoo.un(obj,o)});
        }
    };

    window.console=window.console || ({log:function(){}});
    window.trace=window.console.log;
    Error.prototype.throwError=function(){throw arguments;};
    //String.prototype.substr=Quakoo.__substr;
    Object.defineProperty(Array.prototype,'fixed',{enumerable: false});

    return Quakoo;
})(window,document);




/**
 *
 * @type {QuakooUtils}
 */
var QuakooUtils = (function(){
    function QuakooUtils(){
    }
    var _proto = QuakooUtils.prototype;

    /**
     * 检查是否是数字
     * @param num
     * @returns {boolean}
     */
    _proto.isNum=function(num) {
        if (!(/^\d*$/.test(num))) {
            return false;
        }
        return true;
    }

    /**
     * 检查是否为空
     * @param data
     * @returns {boolean}
     */
    _proto.isBlack=function(data) {
        return (data == "" || typeof(data) == "undefined" || data == null || this.isNullJson(data)) ? true : false;
    }

    /**
     * 检查是否不为空
     * @param data
     * @returns {boolean}
     */
    _proto.isNotBlack=function(data) {
        return (data == "" || typeof(data) == "undefined" || data == null || this.isNullJson(data)) ? false : true;
    }

    /**
     * 检查是否是函数
     * @param func
     * @returns {boolean}
     */
    _proto.isFunction=function(func) {
        if (typeof(func) == "function") {
            return true;
        }
        return false;
    }

    /**
     * 检查是否是数组
     * @param o
     * @returns {boolean}
     */
    _proto.isArray=function(o) {
        return Object.prototype.toString.call(o) === '[object Array]';
    }



    /**
     * 检查是否是对象
     * @param obj
     * @returns {boolean}
     */
    _proto.isObject=function(obj) {
        return obj instanceof Object;
    }

    /**
     * 检查是否是空的json对象
     * @param obj
     * @returns {*|boolean}
     */
    _proto.isNullJson=function(obj) {
        return this.isJson(obj) && JSON.stringify(obj) == '{}';
    }

    /**
     * 检查是否是json对象
     * @param obj
     * @returns {boolean}
     */
    _proto.isJson=function(obj) {
        return typeof(obj) == "object" &&
            Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
    }

    /**
     * 检查是否是手机号码
     * @param mobileNum
     * @returns {boolean}
     */
    _proto.isMobileNum=function(mobileNum) {
        var mobile = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/;
        if(!(mobile.test(mobileNum))){
            return false;
        }
        return true;
    }


    /**
     * 检查是否是邮箱
     * @param emails
     * @returns {boolean}
     */
    _proto.isEmail=function(emails) {
        if (!(/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/.test(emails))) {
            return false;
        }
        return true;
    }

    /**
     * 检查是否是身份证
     * @param num
     * @returns {boolean}
     */
    _proto.isIDcard=function(num) {
        if (!(/\d{15}|\d{17}[0-9Xx]/.test(num))) {
            return false;
        }
        return true;
    }


    /**
     * 深度拷贝对象
     * @param source
     * @returns new Object
     */
    _proto.deepCopy = function(source) {
        var result = {};
        for (var key in source) {
            result[key] = typeof source[key] === 'object' ? deepCopy(source[key]) : source[key];
        }
        return result;
    }

    /**
     * 替换摩羯字符
     * @param str
     * @returns {string | *}
     */
    _proto.replaceEmoji=function(str) {
        str += "";
        var patt=/[\ud800-\udbff][\udc00-\udfff]/g;
        str = str.replace(patt,function(char){
            if (char.length===2) {
                return "*";
            } else {
                return char;
            }
        });
        return str;
    }

    /**
     * 获取图片的真是宽高，并且调用回调方法
     * @param url
     * @param callback
     */
    _proto.getImgWh= function(url, callback) {
        var img = new Image();
        img.src = url;
        if (img.complete) {
            callback(img.width, img.height);
        } else {
            img.onload = function() {
                callback(img.width, img.height);
                img.onload = null;
            };
            //test去连的时候 读不到文件
            img.onerror = function() {
                callback(200, 400);
                img.onerror = null;
            }
        };
    };


    /**
     * 阻止事件冒泡
     * @param event
     */
    _proto.stopEventBubble= function(event) {
        var e = event || window.event;

        if (e && e.stopPropagation) {
            e.stopPropagation();
        } else {
            if (e) {
                e.cancelBubble = true;
            }
        }
    }


    /**
     * 获取文件后缀名字 (返回.jpg)
     * @param file_name
     * @returns {{groups: {}}|RegExpExecArray}
     */
    _proto.getFileSuffix= function(file_name) {
        var result = /\.[^\.]+/.exec(file_name);
        return result;
    }

    /**
     * 获取随机数字
       var num = quakooUtils.getRandomNum(1,10);
     * @param Min
     * @param Max
     * @returns {*}
     */
    _proto.getRandomNum= function(Min, Max) {
        var Range = Max - Min;
        var Rand = Math.random();
        return (Min + Math.round(Rand * Range));
    }


    /**
     * 根据当前页面URL根据参数获取值
     * @returns {*}
     */
    _proto.getQueryString= function(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }

    //decodeURI
    _proto.tryDecodeURIComponent= function(value){
        if (value) {
            try{
                return decodeURIComponent(value);
            }catch(err){
                return value;
            }
        }
        return "";
    };


    _proto.getDateDiff=function(dateTimeStamp){
        var minute = 1000 * 60;
        var hour = minute * 60;
        var day = hour * 24;
        var halfamonth = day * 15;
        var month = day * 30;
        var now = new Date().getTime();
        var diffValue = now - dateTimeStamp;
        if(diffValue < -500){return;}
        var monthC =diffValue/month;
        var weekC =diffValue/(7*day);
        var dayC =diffValue/day;
        var hourC =diffValue/hour;
        var minC =diffValue/minute;
        if(monthC>=1){
            result="" + parseInt(monthC) + "月前";
        }
        else if(weekC>=1){
            result="" + parseInt(weekC) + "周前";
        }
        else if(dayC>=1){
            result=""+ parseInt(dayC) +"天前";
        }
        else if(hourC>=1){
            result=""+ parseInt(hourC) +"小时前";
        }
        else if(minC>=1){
            result=""+ parseInt(minC) +"分钟前";
        }else
            result="刚刚";
        return result;
    }
    _proto.formatTimeToDate = function (time){
        return new Date(time).format("yyyy-MM-dd hh:mm");
    };
    _proto.formatTimeToDateDian = function (time){
        return new Date(time).format("yyyy.MM.dd hh:mm");
    };
    _proto.formatTimeToDateCh = function (time){
        return new Date(time).format("yyyy/MM/dd hh:mm");
    };
    _proto.formatTimeToDay = function (time){
        return new Date(time).format("yyyy-MM-dd");
    };
    _proto.formatTimeToDayYang = function (time){
        return new Date(time).format("yyyyMMdd");
    };
    _proto.formatTimeToDateDianLY = function (time){
        return new Date(time).format("yyyy.MM.dd");
    };
    _proto.formatTimeToDayDate = function (time){
        return new Date(time).format("MM月dd日 hh:mm");
    };
    _proto.formatTimeToMonthDian = function (time){
        return new Date(time).format("MM.dd hh:mm");
    };


    /**
     //金额千分位 保留小数点后两位
     * @param s 要格式化的数字
     * @param n 保留几位小数
     * @returns {string}
     */
    _proto.formatMoney=function (s, n) {
        n = n > 0 && n <= 20 ? n : 2;
        s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
        var l = s.split(".")[0].split("").reverse(),
            r = s.split(".")[1];
        var t = "";
        for (var i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
        }
        return t.split("").reverse().join("") + "." + r;
    };




    _proto.getScrollTop=function() {
        var scrollTop = 0,
            bodyScrollTop = 0,
            documentScrollTop = 0;
        if (document.body) {
            bodyScrollTop = document.body.scrollTop;
        }
        if (document.documentElement) {
            documentScrollTop = document.documentElement.scrollTop;
        }
        scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
        return scrollTop;
    }

//文档的总高度
    _proto.getScrollHeight=function() {
        var scrollHeight = 0,
            bodyScrollHeight = 0,
            documentScrollHeight = 0;
        if (document.body) {
            bodyScrollHeight = document.body.scrollHeight;
        }
        if (document.documentElement) {
            documentScrollHeight = document.documentElement.scrollHeight;
        }
        scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
        return scrollHeight;
    }

//浏览器视口的高度
    _proto.getWindowHeight=function() {
        var windowHeight = 0;
        if (document.compatMode == "CSS1Compat") {
            windowHeight = document.documentElement.clientHeight;
        } else {
            windowHeight = document.body.clientHeight;
        }
        return windowHeight;
    }


    return QuakooUtils;
})();



/**
 * Base64 类
 * Base64.encode("string");
 * Base64.decode("base64_string");
 * @type {Base64}
 */
var Base64 = (
    function(){
    function Base64(){
    }
    var _proto = Base64.prototype;

    _proto.encode= function (str) {
        var c1, c2, c3;
        var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        var i = 0,
            len = str.length,
            string = '';

        while (i < len) {
            c1 = str.charCodeAt(i++) & 0xff;
            if (i == len) {
                string += base64EncodeChars.charAt(c1 >> 2);
                string += base64EncodeChars.charAt((c1 & 0x3) << 4);
                string += "==";
                break;
            }
            c2 = str.charCodeAt(i++);
            if (i == len) {
                string += base64EncodeChars.charAt(c1 >> 2);
                string += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
                string += base64EncodeChars.charAt((c2 & 0xF) << 2);
                string += "=";
                break;
            }
            c3 = str.charCodeAt(i++);
            string += base64EncodeChars.charAt(c1 >> 2);
            string += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
            string += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
            string += base64EncodeChars.charAt(c3 & 0x3F)
        }
        return string
    }

    _proto.decode=function (str) {
        var c1, c2, c3, c4;
        var base64DecodeChars = new Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
            58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
            37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1
        );
        var i = 0,
            len = str.length,
            string = '';

        while (i < len) {
            do {
                c1 = base64DecodeChars[str.charCodeAt(i++) & 0xff]
            } while (
                i < len && c1 == -1
                );

            if (c1 == -1) break;

            do {
                c2 = base64DecodeChars[str.charCodeAt(i++) & 0xff]
            } while (
                i < len && c2 == -1
                );

            if (c2 == -1) break;

            string += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));

            do {
                c3 = str.charCodeAt(i++) & 0xff;
                if (c3 == 61)
                    return string;

                c3 = base64DecodeChars[c3]
            } while (
                i < len && c3 == -1
                );

            if (c3 == -1) break;

            string += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));

            do {
                c4 = str.charCodeAt(i++) & 0xff;
                if (c4 == 61) return string;
                c4 = base64DecodeChars[c4]
            } while (
                i < len && c4 == -1
                );

            if (c4 == -1) break;

            string += String.fromCharCode(((c3 & 0x03) << 6) | c4)
        }
        return string;
    }



    return Base64;
})();


/**
 * MD5加密
 * Md5.md5("string");
 * @type {Md5}
 */
var Md5=(function () {
    function Md5() {
    }
    var _proto = Md5.prototype;


    _proto._rotateLeft = function(lValue, iShiftBits) {
        return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits));
    }

    _proto._addUnsigned = function(lX, lY) {
        var lX4, lY4, lX8, lY8, lResult;
        lX8 = (lX & 0x80000000);
        lY8 = (lY & 0x80000000);
        lX4 = (lX & 0x40000000);
        lY4 = (lY & 0x40000000);
        lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF);
        if (lX4 & lY4) return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
        if (lX4 | lY4) {
            if (lResult & 0x40000000) return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
            else return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
        } else {
            return (lResult ^ lX8 ^ lY8);
        }
    }

    _proto._F = function(x, y, z) {
        return (x & y) | ((~x) & z);
    }

    _proto._G = function(x, y, z) {
        return (x & z) | (y & (~z));
    }

    _proto._H = function(x, y, z) {
        return (x ^ y ^ z);
    }

    _proto._I = function(x, y, z) {
        return (y ^ (x | (~z)));
    }

    _proto._FF = function(a, b, c, d, x, s, ac) {
        a = this._addUnsigned(a, this._addUnsigned(this._addUnsigned(this._F(b, c, d), x), ac));
        return this._addUnsigned(this._rotateLeft(a, s), b);
    };

    _proto._GG = function(a, b, c, d, x, s, ac) {
        a = this._addUnsigned(a, this._addUnsigned(this._addUnsigned(this._G(b, c, d), x), ac));
        return this._addUnsigned(this._rotateLeft(a, s), b);
    };

    _proto._HH = function(a, b, c, d, x, s, ac) {
        a = this._addUnsigned(a, this._addUnsigned(this._addUnsigned(this._H(b, c, d), x), ac));
        return this._addUnsigned(this._rotateLeft(a, s), b);
    };

    _proto._II = function(a, b, c, d, x, s, ac) {
        a = this._addUnsigned(a, this._addUnsigned(this._addUnsigned(this._I(b, c, d), x), ac));
        return this._addUnsigned(this._rotateLeft(a, s), b);
    };

    _proto._convertToWordArray = function(string) {
        var lWordCount;
        var lMessageLength = string.length;
        var lNumberOfWordsTempOne = lMessageLength + 8;
        var lNumberOfWordsTempTwo = (lNumberOfWordsTempOne - (lNumberOfWordsTempOne % 64)) / 64;
        var lNumberOfWords = (lNumberOfWordsTempTwo + 1) * 16;
        var lWordArray = Array(lNumberOfWords - 1);
        var lBytePosition = 0;
        var lByteCount = 0;
        while (lByteCount < lMessageLength) {
            lWordCount = (lByteCount - (lByteCount % 4)) / 4;
            lBytePosition = (lByteCount % 4) * 8;
            lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount) << lBytePosition));
            lByteCount++;
        }
        lWordCount = (lByteCount - (lByteCount % 4)) / 4;
        lBytePosition = (lByteCount % 4) * 8;
        lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition);
        lWordArray[lNumberOfWords - 2] = lMessageLength << 3;
        lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29;
        return lWordArray;
    };

    _proto._wordToHex = function(lValue) {
        var WordToHexValue = "",
            WordToHexValueTemp = "",
            lByte, lCount;
        for (lCount = 0; lCount <= 3; lCount++) {
            lByte = (lValue >>> (lCount * 8)) & 255;
            WordToHexValueTemp = "0" + lByte.toString(16);
            WordToHexValue = WordToHexValue + WordToHexValueTemp.substr(WordToHexValueTemp.length - 2, 2);
        }
        return WordToHexValue;
    };

    _proto._uTF8Encode = function(string) {
        string = string.replace(/\x0d\x0a/g, "\x0a");
        var output = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                output += String.fromCharCode(c);
            } else if ((c > 127) && (c < 2048)) {
                output += String.fromCharCode((c >> 6) | 192);
                output += String.fromCharCode((c & 63) | 128);
            } else {
                output += String.fromCharCode((c >> 12) | 224);
                output += String.fromCharCode(((c >> 6) & 63) | 128);
                output += String.fromCharCode((c & 63) | 128);
            }
        }
        return output;
    };

    _proto.md5=function (string) {
        var x = Array();
        var k, AA, BB, CC, DD, a, b, c, d;
        var S11 = 7,
            S12 = 12,
            S13 = 17,
            S14 = 22;
        var S21 = 5,
            S22 = 9,
            S23 = 14,
            S24 = 20;
        var S31 = 4,
            S32 = 11,
            S33 = 16,
            S34 = 23;
        var S41 = 6,
            S42 = 10,
            S43 = 15,
            S44 = 21;
        string = this._uTF8Encode(string);
        x = this._convertToWordArray(string);
        a = 0x67452301;
        b = 0xEFCDAB89;
        c = 0x98BADCFE;
        d = 0x10325476;
        for (k = 0; k < x.length; k += 16) {
            AA = a;
            BB = b;
            CC = c;
            DD = d;
            a = this._FF(a, b, c, d, x[k + 0], S11, 0xD76AA478);
            d = this._FF(d, a, b, c, x[k + 1], S12, 0xE8C7B756);
            c = this._FF(c, d, a, b, x[k + 2], S13, 0x242070DB);
            b = this._FF(b, c, d, a, x[k + 3], S14, 0xC1BDCEEE);
            a = this._FF(a, b, c, d, x[k + 4], S11, 0xF57C0FAF);
            d = this._FF(d, a, b, c, x[k + 5], S12, 0x4787C62A);
            c = this._FF(c, d, a, b, x[k + 6], S13, 0xA8304613);
            b = this._FF(b, c, d, a, x[k + 7], S14, 0xFD469501);
            a = this._FF(a, b, c, d, x[k + 8], S11, 0x698098D8);
            d = this._FF(d, a, b, c, x[k + 9], S12, 0x8B44F7AF);
            c = this._FF(c, d, a, b, x[k + 10], S13, 0xFFFF5BB1);
            b = this._FF(b, c, d, a, x[k + 11], S14, 0x895CD7BE);
            a = this._FF(a, b, c, d, x[k + 12], S11, 0x6B901122);
            d = this._FF(d, a, b, c, x[k + 13], S12, 0xFD987193);
            c = this._FF(c, d, a, b, x[k + 14], S13, 0xA679438E);
            b = this._FF(b, c, d, a, x[k + 15], S14, 0x49B40821);
            a = this._GG(a, b, c, d, x[k + 1], S21, 0xF61E2562);
            d = this._GG(d, a, b, c, x[k + 6], S22, 0xC040B340);
            c = this._GG(c, d, a, b, x[k + 11], S23, 0x265E5A51);
            b = this._GG(b, c, d, a, x[k + 0], S24, 0xE9B6C7AA);
            a = this._GG(a, b, c, d, x[k + 5], S21, 0xD62F105D);
            d = this._GG(d, a, b, c, x[k + 10], S22, 0x2441453);
            c = this._GG(c, d, a, b, x[k + 15], S23, 0xD8A1E681);
            b = this._GG(b, c, d, a, x[k + 4], S24, 0xE7D3FBC8);
            a = this._GG(a, b, c, d, x[k + 9], S21, 0x21E1CDE6);
            d = this._GG(d, a, b, c, x[k + 14], S22, 0xC33707D6);
            c = this._GG(c, d, a, b, x[k + 3], S23, 0xF4D50D87);
            b = this._GG(b, c, d, a, x[k + 8], S24, 0x455A14ED);
            a = this._GG(a, b, c, d, x[k + 13], S21, 0xA9E3E905);
            d = this._GG(d, a, b, c, x[k + 2], S22, 0xFCEFA3F8);
            c = this._GG(c, d, a, b, x[k + 7], S23, 0x676F02D9);
            b = this._GG(b, c, d, a, x[k + 12], S24, 0x8D2A4C8A);
            a = this._HH(a, b, c, d, x[k + 5], S31, 0xFFFA3942);
            d = this._HH(d, a, b, c, x[k + 8], S32, 0x8771F681);
            c = this._HH(c, d, a, b, x[k + 11], S33, 0x6D9D6122);
            b = this._HH(b, c, d, a, x[k + 14], S34, 0xFDE5380C);
            a = this._HH(a, b, c, d, x[k + 1], S31, 0xA4BEEA44);
            d = this._HH(d, a, b, c, x[k + 4], S32, 0x4BDECFA9);
            c = this._HH(c, d, a, b, x[k + 7], S33, 0xF6BB4B60);
            b = this._HH(b, c, d, a, x[k + 10], S34, 0xBEBFBC70);
            a = this._HH(a, b, c, d, x[k + 13], S31, 0x289B7EC6);
            d = this._HH(d, a, b, c, x[k + 0], S32, 0xEAA127FA);
            c = this._HH(c, d, a, b, x[k + 3], S33, 0xD4EF3085);
            b = this._HH(b, c, d, a, x[k + 6], S34, 0x4881D05);
            a = this._HH(a, b, c, d, x[k + 9], S31, 0xD9D4D039);
            d = this._HH(d, a, b, c, x[k + 12], S32, 0xE6DB99E5);
            c = this._HH(c, d, a, b, x[k + 15], S33, 0x1FA27CF8);
            b = this._HH(b, c, d, a, x[k + 2], S34, 0xC4AC5665);
            a = this._II(a, b, c, d, x[k + 0], S41, 0xF4292244);
            d = this._II(d, a, b, c, x[k + 7], S42, 0x432AFF97);
            c = this._II(c, d, a, b, x[k + 14], S43, 0xAB9423A7);
            b = this._II(b, c, d, a, x[k + 5], S44, 0xFC93A039);
            a = this._II(a, b, c, d, x[k + 12], S41, 0x655B59C3);
            d = this._II(d, a, b, c, x[k + 3], S42, 0x8F0CCC92);
            c = this._II(c, d, a, b, x[k + 10], S43, 0xFFEFF47D);
            b = this._II(b, c, d, a, x[k + 1], S44, 0x85845DD1);
            a = this._II(a, b, c, d, x[k + 8], S41, 0x6FA87E4F);
            d = this._II(d, a, b, c, x[k + 15], S42, 0xFE2CE6E0);
            c = this._II(c, d, a, b, x[k + 6], S43, 0xA3014314);
            b = this._II(b, c, d, a, x[k + 13], S44, 0x4E0811A1);
            a = this._II(a, b, c, d, x[k + 4], S41, 0xF7537E82);
            d = this._II(d, a, b, c, x[k + 11], S42, 0xBD3AF235);
            c = this._II(c, d, a, b, x[k + 2], S43, 0x2AD7D2BB);
            b = this._II(b, c, d, a, x[k + 9], S44, 0xEB86D391);
            a = this._addUnsigned(a, AA);
            b = this._addUnsigned(b, BB);
            c = this._addUnsigned(c, CC);
            d = this._addUnsigned(d, DD);
        }
        var tempValue = this._wordToHex(a) + this._wordToHex(b) + this._wordToHex(c) + this._wordToHex(d);
        return tempValue.toLowerCase();
    }




    return Md5;
})();










Array.prototype.contains = function (obj) {
    var i = this.length;
    while (i--) {
        if (this[i] == obj) {
            return true;
        }
    }
    return false;
}
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};

Array.prototype.removeValue = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

Array.prototype.remove = function(item){
    var index=-1;
    for(var i=0;i<this.length;i++){
        if(this[i]==item){
            index=i;
        }
    }
    if(index>=0){
        return this.splice(index,1);
    }
    return this;
};

Array.prototype.randomSort = function() {
    if (this.length == 0) {
        return this;
    }
    var  temp_x;  //临时交换数
    var  tArr  =  this.slice(0); //新数组,复制原数组
    var  random_x;
    for (var  i = this.length; i > 0; i--)  {
        random_x  =  Math.floor(Math.random() * i);  //   取得一个随机数
        temp_x  =  tArr[random_x];
        tArr[random_x]  =  tArr[i - 1];
        tArr[i - 1]  =  temp_x;
    }
    return  tArr;  //返回新数组
}



String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi" : "g")), replaceWith);
    } else {
        return this.replace(reallyDo, replaceWith);
    }
}

String.prototype.startWith = function(str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length)
        return false;
    if (this.substr(0, str.length) == str)
        return true;
    else
        return false;
    return true;
}

String.prototype.endWith = function(str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length)
        return false;
    if (this.substring(this.length - str.length) == str)
        return true;
    else
        return false;
    return true;
};
Date.prototype.format = function(format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
};



