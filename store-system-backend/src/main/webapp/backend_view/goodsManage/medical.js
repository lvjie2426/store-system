var cid = 3;
var momentPriceData = {};//暂时存储价格信息   k(球id+柱id+左/右眼)->v
var momentRangeData = {};//暂时存储定制范围信息
var momentNowRangeData = {};//暂时存储现货范围信息
var commissionJson = {};//本地提成
var groupFlag=false,personalFlag=false;  //团队提成   个人提成

var params = {};//添加商品所需要的参数  key  类目id；value：所需参数
var updateParams;//当前被编辑的数据
var yjDate;//时间范围
var yxQiuId = 41;       //隐形眼镜球
var yxZhuId = 42;       //隐形眼镜球

//添加商品
function addGoods(isUpdate) {
    var btn,title;
    params[cid] = {};
    if(!isUpdate){
        btn = ['取消','新增'];
        title = '新增';
        params[cid].subid = subid;
        params[cid].cid = cid;
        params[cid].properties = {};
        params[cid].skuJson = [];
        params[cid].productCustomRangeList = [];
        params[cid].ugDiscount = [];
        params[cid].type = params[cid].type||0;
    }else{
        btn = ['取消','编辑'];
        title = '编辑';
        for(var i in updateParams){
            if(i!='skuObj'){
                params[cid][i] = updateParams[i]
            }
        }
        params[cid].skuJson = [];
        if(params[cid].skuList && params[cid].skuList.length>0){
            for(var i=0;i<params[cid].skuList.length;i++){
                var obj = {};
                obj.properties = params[cid].skuList[i].properties;
                obj.code = params[cid].skuList[i].code;
                obj.integralPrice = params[cid].skuList[i].integralPrice;
                obj.retailPrice = params[cid].skuList[i].retailPrice;
                obj.costPrice = params[cid].skuList[i].costPrice;
                obj.num = params[cid].skuList[i].num;
                obj.id = params[cid].skuList[i].id;
                obj.eyeType = params[cid].skuList[i].eyeType;
                params[cid].skuJson.push(obj)
            }
        }
        params[cid].ugDiscount = updateParams.userGradeCategoryDiscountList;
        if( params[cid].integralEndTime ){
            params[cid].integralEndTimeNew = quakooUtils.formatTimeToDateDianLY(params[cid].integralEndTime)
        }
        if(params[cid].integralStartTime){
            params[cid].integralStartTimeNew = quakooUtils.formatTimeToDateDianLY(params[cid].integralStartTime)
        }
        if(params[cid].icon){
            var obj = quakooImg.processImg(params[cid].icon,60,60);
            params[cid].newIcon = obj.url;
            params[cid].style = obj.style;
        }
        if(params[cid].nirNumDate){
            params[cid].nirNumDate = quakooUtils.formatTimeToDay(params[cid].nirNumDate);
        }
    }
    params[cid].categoryName = '隐形眼镜';

    momentPriceData = {};
    momentRangeData = {};
    var html = [];
    html.push('<div class="layui-yj-header"><div class="layui-layer-title mine-title"><div>');
    html.push(title+params[cid].categoryName)
    html.push('</div> </div></div>');
    layer.open({
        type: 1,shade: 0.01,title:html.join(''),
        skin: 'layui-layer-add-goods  layui-layer-addtsgoods', //样式类名
        area: ['360px', '550px'],
        move:'.mine-title',
        anim: 0,
        maxWidth:'360',
        closeBtn: 0, //不显示关闭按钮
        resize:false,
        shadeClose: true, //开启遮罩关闭
        content: $('.content-view-addgoods'),
        btn: btn,
        yes: function(index, layero){
            layer.close(index)
        },
        btn2: function(index){
            addYjGoods(isUpdate,index);
            return false;
        },
        success: function(layero, index){
            groupFlag=false;personalFlag=false;
            openYjGoods(isUpdate);
        }
    });
}
// 新增眼镜
function addYjGoods(isUpdate,index) {
    var nowParam = {};
    for(var i in params[cid]){
        if(i=='nowRanges'){
            nowParam.nowRangesJson = params[cid][i];
        }else if(i=='ranges'){
            nowParam.rangesJson = params[cid][i];
        }else{
            nowParam[i] = params[cid][i]
        }
    }
    if(!nowParam.bid){
        nowParam.brandName = $("#brandName").val();
        nowParam.seriesName = $("#seriesName").val();
        nowParam.bid=0;
        nowParam.sid=0;
    }else{
        if(!nowParam.sid){
            nowParam.seriesName = $("#seriesName").val();
            nowParam.sid=0;
        }
    }
    //预警库存
    if($("#yj-add-kucun").val()){
        nowParam.nowRemind = $("#yj-add-kucun").val();
    }
    //销售状态  0开启  1关闭
    nowParam.saleStatus = 1;

    var flag = false;
    for (var i = 0; i < nowParam.skuJson.length; i++) {
        if(nowParam.skuJson[i].integralPrice){
            flag = true;
        }
    }
    if(flag){
        if(yjDate.getTime()[2]){
            nowParam.integralStartTime = yjDate.getTime()[2];
        }
        if(yjDate.getTime()[3]){
            nowParam.integralEndTime = yjDate.getTime()[3];
        }
        //兑换数量上限
        if($('#yj-add-integralNum').val()){
            nowParam.integralNum = $('#yj-add-integralNum').val();
        }else{
            layer.msg('请选择积分价有效期');
            return
        }
        if(!nowParam.integralStartTime){
            layer.msg('请选择积分价有效期');
            return
        }
        if(!nowParam.integralEndTime){
            layer.msg('请选择积分价有效期');
            return
        }
    }


    nowParam.properties[$('#yj-add-day').attr('data-pnid')] = $('#yj-add-day').val();

    nowParam.properties[7] = $('#yj-add-baozhuang').val();
    nowParam.type=1;
    nowParam.nirNum=$('#nirNum').val();
    nowParam.nirNumDate=new Date($('#nirNumDate').val()).getTime();
    nowParam.nirImg = [];                  //门店照片
    for(var i=0;i<$('#imgBox .imgBox').length;i++){
        nowParam.nirImg.push($('#imgBox .imgBox').eq(i).find('img').attr('real-src'))
    }
    nowParam.nirImg = nowParam.nirImg.join(',');
    /*//会员折扣
    if(quakooUtils.isArray(nowParam.ugDiscount)){
        nowParam.ugDiscount = JSON.stringify(nowParam.ugDiscount);
    }*/
    nowParam.commissionJson = JSON.stringify(nowParam.commissionJson);//提成
    nowParam.nowRangesJson = JSON.stringify(nowParam.nowRangesJson);//现货范围
    nowParam.rangesJson = JSON.stringify(nowParam.rangesJson);//定制范围
    delete nowParam.categoryName;
    if(isUpdate){
        delete nowParam.skuList;
        delete nowParam.userGradeCategoryDiscountList;
        delete nowParam.providerName;
        delete nowParam.categoryName;
        delete nowParam.seriesName;
        delete nowParam.brandName;
        delete nowParam.canUseNum;
        delete nowParam.ctime;
        delete nowParam.utime;
        delete nowParam.sort;
        delete nowParam.integralEndTimeNew;
        delete nowParam.integralStartTimeNew;

        var skuJson = nowParam.skuJson;

        nowParam.addSkuJson = [];
        nowParam.updateSkuJson = [];
        nowParam.delSkuIdJson = [];
        for(var i=0;i<skuJson.length;i++){
            if(updateParams.skuObj[skuJson[i].properties[yxQiuId]+''+skuJson[i].properties[yxZhuId]+skuJson[i].eyeType]){
                skuJson[i].id = updateParams.skuObj[skuJson[i].properties[yxQiuId]+''+skuJson[i].properties[yxZhuId]+skuJson[i].eyeType].id;
                skuJson[i].code = updateParams.skuObj[skuJson[i].properties[yxQiuId]+''+skuJson[i].properties[yxZhuId]+skuJson[i].eyeType].code;
                nowParam.updateSkuJson.push(skuJson[i]);
                delete updateParams.skuObj[skuJson[i].properties[yxQiuId]+''+skuJson[i].properties[yxZhuId]+skuJson[i].eyeType];
            }else{
                nowParam.addSkuJson.push(skuJson[i])
            }
        }
        for(var i in updateParams.skuObj){
            nowParam.delSkuIdJson.push(updateParams.skuObj[i].id)
        }
        nowParam.addSkuJson = JSON.stringify(nowParam.addSkuJson);
        nowParam.updateSkuJson = JSON.stringify(nowParam.updateSkuJson);
        nowParam.delSkuIdJson = JSON.stringify(nowParam.delSkuIdJson);
        quakooData.ajaxGetData(config.getUrl_product_change(),nowParam,function (ret) {
            if(ret.success){
                gStockWarn = {};//清空预警
                layer.msg('修改成功',{icon: 1})
                layer.close(index)
                chooseGoodsType();
            }else{
                layer.msg(ret.data,{icon: 2});

            }
        })
    }else{
        if(JSON.stringify(nowParam.skuJson)!='[{}]'){
            nowParam.skuJson = JSON.stringify(nowParam.skuJson);
        }else{
            delete nowParam.skuJson;
        }

        quakooData.ajaxGetData(config.getUrl_product_add(),nowParam,function (ret) {
            if(ret.success){
                layer.msg('添加成功',{icon: 1});
                layer.close(index);
                initPage();
            }else{
                layer.msg(ret.data,{icon: 2});
            }
        })
    }
}
//新增眼睛弹出框弹出后执行的操作
function openYjGoods(isUpdate) {
    $('.yj-check-input input').click(function (e) {
        quakooUtils.stopEventBubble(e)
    });
    var data = {
        list:params[cid],isUpdate:isUpdate,
        brandData:brand,providerData:gongS,
        shengchanData: shengS,
        typeData:typeData,zhouqiData:zhouqiData,baozhuangData:baozhuangData,
    };
    var html = template('addGoodsTmp',data);
    document.getElementById('addGoodsDom').innerHTML = html;
    layui.use('form', function(){
        var form = layui.form;
        form.render();
        //选择积分还是常规
        form.on('checkbox(defaultGoods)',function(data){
            if(data.elem.checked){
                $('.yj-from').eq(0).slideDown();
            }else{
                $('.yj-from').eq(0).slideUp();
            }
        });
        form.on('checkbox(scoreGoods)',function(data){
            if(data.elem.checked){
                $('.yj-from').eq(1).slideDown();
                params[cid].type = 1;

            }else{
                $('.yj-from').eq(1).slideUp();
                params[cid].type = 0;
            }
        });
        //库存
        form.on('checkbox(kucun-yj)', function(data){
            if(data.elem.checked){
                //是否被选中，true或者false
                $(data.othis).find('input').attr('readonly',false)
            }else{
                $(data.othis).find('input').attr('readonly','readonly').val('')
            }
        });
    });
    //品牌/系列
    selectTag($("#yj-add-brand"),function (ret) {
        params[cid].bid = ret.value;
        $('#yj-add-series').html('');
        params[cid].sid = -1;
        //获取品牌下的系列列表
        quakooData.ajaxGetData(config.getUrl_productSeries_getSubAllList(),{bid:ret.value},function (ret1) {
            if(ret1 && ret1.success){
                var html = template('selectTagInputTmp',{title:'',list:ret1.data});
                $('#yj-add-series').html(html);
                selectTag($("#yj-add-series"),function (series) {
                    params[cid].sid = series.value;
                })
            }
        })
    })
    //供应商
    selectTag($("#yj-add-provider"),function (ret) {
        params[cid].pid = ret.value;
    })

    //类型5
    selectTag($("#yj-add-type"),function (ret) {
        params[cid].properties[ret.el.find('.optionUL li').attr('data-pnid')] = ret.value;
    })
    //周期
    selectTag($("#yj-add-zhouqi"),function (ret) {
        params[cid].properties[ret.el.find('.optionUL li').attr('data-pnid')] = ret.value;
    })
    //生产商
    selectTag($("#yj-add-shengchan"),function (ret) {
        params[cid].createid = ret.value;
    })
    //时间范围
    yjDate = new dateRange('#yj-add-time',true);
    yjDate.init()


    //营业时间
    var opt = {};
    opt.date = {
        preset: 'date'
    };
    opt.datetime = {
        preset: 'datetime'
    };
    opt.time = {
        preset: 'time'
    };

    opt.default = {
        theme: 'mbsc-android-holo', //皮肤样式
        display: 'bubble', //显示方式
        mode: 'scroller', //日期选择模式
        dateFormat: 'yyyy-mm-dd',
        lang: 'zh',
        showNow: true,
        nowText: "今天",
        typeNum: 'day', //day：获取到日，time：获取到时，branch：获取到分；默认是获取到分
    };
    $('.settings').bind('change', function () {
        var demo = 'datetime';

        $('.demo-test-' + demo).scroller('destroy').scroller($.extend(opt['datetime'], opt['default']));
        $('.demo').hide();
        $('.demo-' + demo).show();
    });

    $('#yjdemo').trigger('change');

    //医疗器械注册信息展开
    $('.zhuceInfo-first').click(function () {
        $('.zhuceUl').slideToggle();
        $('.zhuceInfo-first').find('i').toggleClass('icon-ico_arrow_down').toggleClass('icon-ico_arrow_up');
    })
}

//设定提成
function openSettingTicheng() {
    var index = layer.open({
        type: 1,shade: 0.01,title:'设定提成',
        skin: 'layui-layer-yj layui-layer-ticheng', //样式类名
        anim: 0,
        area:['360px','560px'],
        closeBtn: 0, //不显示关闭按钮
        resize:false,
        shadeClose: true, //开启遮罩关闭
        content: $('.content-view-ticheng'),
        btn: ['取消','确定'],
        yes: function(index, layero){
            layer.close(index)
        },
        btn2: function(){
            if(groupFlag){
                $("#tiChengDom .tuanduiInput").each(function (index,item) {
                    if(item.value){
                        if(commissionJson[item.getAttribute('data-id')]){
                            commissionJson[item.getAttribute('data-id')].price = item.value;
                        }else{
                            commissionJson[item.getAttribute('data-id')] = {subId:item.getAttribute('data-id'),price:item.value}
                        }
                    }
                });
            }else{
                for (var i in commissionJson) {
                    commissionJson[i].price = '';
                }
            }
            if(personalFlag){
                $("#tiChengDom .gerenInput").each(function (index,item) {
                    if(item.value){
                        if(commissionJson[item.getAttribute('data-id')]){
                            commissionJson[item.getAttribute('data-id')].users = item.value;
                        }else{
                            commissionJson[item.getAttribute('data-id')] = {subId:item.getAttribute('data-id'),users:item.value}
                        }
                    }
                });
            }else{
                for (var i in commissionJson) {
                    commissionJson[i].users = '';
                }
            }
            params[cid].commissionJson = [];
            for(var i in commissionJson){
                if(commissionJson[i] && (commissionJson[i].users || commissionJson[i].price)){
                    params[cid].commissionJson.push(commissionJson[i]);
                }
            }
            commissionJson = {};
        },
        success: function(layero, index){
            if(params[cid] && params[cid].commissions){
                for(var i=0;i<params[cid].commissions.length;i++){
                    commissionJson[params[cid].commissions[i].subId] = params[cid].commissions[i];
                    if(params[cid].commissions[i].price){
                        groupFlag = true;
                    }
                    if(params[cid].commissions[i].users){
                        personalFlag = true;
                    }
                }
            }
            $("#tiChengDom").html(template('tiChengTmp',{shopData:shopData,commissionJson:commissionJson,group:groupFlag,personal:personalFlag}));
            layui.use('form', function(){
                var form = layui.form;
                form.render();
                form.on('checkbox()', function(data){
                    if(data.elem.checked){
                        if(data.value=='group'){
                            groupFlag = true;
                            $("#tiChengDom .group").show();
                            $(".tuanduiInput").removeAttr("disabled");
                        }else if(data.value=='personal'){
                            personalFlag = true;
                            $("#tiChengDom .personal").show();
                            $(".gerenInput").removeAttr("disabled");
                        }
                    }else{
                        if(data.value=='group'){
                            groupFlag = false;
                            $("#tiChengDom .group").hide();
                            $(".tuanduiInput").attr("disabled",'disabled').val(0);
                        }else if(data.value=='personal'){
                            personalFlag = false;
                            $("#tiChengDom .personal").hide();
                            $(".gerenInput").attr("disabled",'disabled').val(0);
                        }
                    }
                });
            });
        }
    });
}
//初始化价格表格 type  1零售价  2批发价  3备货量   4积分价   5左眼选择    6右眼选择
function setPrice(type) {
    var range = {};     //定制
    var nowRange = {};  //现货
    //现货数据
    if(params[cid] && params[cid].nowRanges){
        for(var i=0;i<params[cid].nowRanges.length;i++){
            nowRange[params[cid].nowRanges[i].ballId+''+params[cid].nowRanges[i].columnId] = params[cid].nowRanges[i];
        }
    }
    //定制数据
    if(params[cid] && params[cid].ranges){
        for(var i=0;i<params[cid].ranges.length;i++){
            range[params[cid].ranges[i].ballId+''+params[cid].ranges[i].columnId] = params[cid].ranges[i];
        }
    }
    if(showInterType==1){
        $('#setPriceBtn li').eq(3).hide().siblings().show();
    }else{
        $('#setPriceBtn li').show();
    }
    var cols = [];//球
    var rangeData = [];//柱
    cols.push({field:'num', minWidth:60, title: '<div class="yj-layui-table-header"><span>球</span><span>柱</span></div>', fixed: 'left'})
    $('#setPriceBtn li').eq(type-1).addClass('active').siblings().removeClass('active');
    for(var i=0;i<ballData.length;i++){
        (function (i) {
            cols.push({field: '', minWidth: 60, title: ballData[i].content, unresize: true,
                templet: function (d) {
                    var str = '';
                    if(range[ballData[i].id+''+d.id] && nowRange[ballData[i].id+''+d.id]){
                        str = "active activenow";
                    }else if(range[ballData[i].id+''+d.id]){
                        str = "active";
                    }else if(nowRange[ballData[i].id+''+d.id]){
                        str = "activenow";
                    }
                    if(range[ballData[i].id+''+d.id] || nowRange[ballData[i].id+''+d.id]){
                        if(type==1) {
                            if (momentPriceData[ballData[i].id + '' + d.id] && momentPriceData[ballData[i].id + '' + d.id].retailPrice!=undefined) {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="'+str+'"><input oninput="priceInputChange(this,'+type+')" type="number" value="'+momentPriceData[ballData[i].id + '' + d.id].retailPrice+'"></div>'
                            } else {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="'+str+'"><input oninput="priceInputChange(this,'+type+')" type="number" value=""></div>'
                            }
                        }else if(type==2){
                            if (momentPriceData[ballData[i].id + '' + d.id] && momentPriceData[ballData[i].id + '' + d.id].costPrice!=undefined) {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="'+str+'"><input oninput="priceInputChange(this,'+type+')" type="number" value="'+momentPriceData[ballData[i].id + '' + d.id].costPrice+'"></div>'
                            } else {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="'+str+'"><input oninput="priceInputChange(this,'+type+')" type="number" value=""></div>'
                            }
                        }else if(type==3){
                            if (momentPriceData[ballData[i].id + '' + d.id] && momentPriceData[ballData[i].id + '' + d.id].num) {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="'+str+'"><input oninput="priceInputChange(this,'+type+')" type="number" value="'+momentPriceData[ballData[i].id + '' + d.id].num+'"></div>'
                            } else {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="'+str+'"><input oninput="priceInputChange(this,'+type+')" type="number" value=""></div>'
                            }
                        }else if(type==4){
                            if (momentPriceData[ballData[i].id + '' + d.id] && momentPriceData[ballData[i].id + '' + d.id].integralPrice) {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="'+str+'"><input oninput="priceInputChange(this,'+type+')" type="number" value="'+momentPriceData[ballData[i].id + '' + d.id].integralPrice+'"></div>'
                            } else {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="'+str+'"><input oninput="priceInputChange(this,'+type+')" type="number" value=""></div>'
                            }
                        }else if(type==5){
                            if (momentPriceData[ballData[i].id + '' + d.id] && momentPriceData[ballData[i].id + '' + d.id].eyeTypeArr.indexOf(1)>=0) {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="iconfont ico_check icon-ico_check '+str+'" ></div>'
                            } else {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="iconfont '+str+'" ></div>'
                            }
                        }else if(type==6){
                            if (momentPriceData[ballData[i].id + '' + d.id] && momentPriceData[ballData[i].id + '' + d.id].eyeTypeArr.indexOf(2)>=0) {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="iconfont ico_check icon-ico_check '+str+'" ></div>'
                            } else {
                                return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="iconfont '+str+'" ></div>'
                            }
                        }
                    }else{
                        return '-';
                    }

                }
            })
        })(i)
    }
    for(var i=0;i<columnData.length;i++){
        rangeData.push({num:columnData[i].content,id:columnData[i].id})
    }
    layui.use('table', function(){
        var table = layui.table;
        table.render({
            elem: '#priceEvent'
            ,data:rangeData
            ,width: 688
            ,height: 416
            ,limit:40
            ,unresize:true
            ,cols: [cols]
        });
        initTableDragInput(type)
    });
}
function initTableDragInput(type) {
    var beginTrIndex;//开始选中的行的下标
    var beginTdIndex;//开始选中的列的下标
    var endTrIndex;//鼠标抬起时的行的下标
    var endTdIndex;//鼠标抬起时列的下标
    var x=0,y=0;//鼠标按下时的浏览器坐标
    var offsetx=0,offsety=0;//鼠标按下时的相对父元素的坐标
    $("#priceEvent").next().find('.layui-table-body tbody').mousedown(function (e) {
        $('.dragBorder').removeClass('dragBorder')
        beginTrIndex = $(e.target).parents('tr').attr('data-index');
        beginTdIndex = $(e.target).parents('td').attr('data-field');
        x = e.clientX;
        y = e.clientY;
        offsetx = e.offsetX;
        offsety = e.offsetY;
        $('#dragDiv').remove();
        $("#priceEvent").next().find('.layui-table-body tbody').append('<div id="dragDiv" style="position:fixed;z-index:99999999;background:rgba(0,0,0,0.3)"></div>')
        return false;
    }).mousemove(function (e) {
        if($('#dragDiv').length){
            if(e.clientX-x>0){
                $('#dragDiv').css('left',x+'px')
            }else{
                $('#dragDiv').css('left',e.clientX+'px')
            }
            if(e.clientY-y>0){
                $('#dragDiv').css('top',y+'px')
            }else{
                $('#dragDiv').css('top',e.clientY+'px')
            }
            $('#dragDiv').css('width',Math.abs(e.clientX-x)+'px')
            $('#dragDiv').css('height',Math.abs(e.clientY-y)+'px')
        }
    }).mouseup(function (e) {
        if(e.target.id=='dragDiv'){
            var w = document.getElementById('dragDiv').offsetWidth;
            var h = document.getElementById('dragDiv').offsetHeight;
            var trW = $("#priceEvent").next().find('.layui-table-body tbody td')[0].offsetWidth;
            var trH = $("#priceEvent").next().find('.layui-table-body tbody td')[0].offsetHeight;
            endTdIndex = beginTdIndex - Math.ceil((w-offsetx)/trW);
            endTrIndex = beginTrIndex - Math.ceil((h-offsety)/trH);
        }else{
            endTrIndex = $(e.target).parents('tr').attr('data-index');
            endTdIndex = $(e.target).parents('td').attr('data-field');
        }
        $('#dragDiv').remove();
        var startTr = Math.min(beginTrIndex,endTrIndex);
        var startTd = Math.min(beginTdIndex,endTdIndex);
        var endTr = Math.max(beginTrIndex,endTrIndex);
        var endTd = Math.max(beginTdIndex,endTdIndex);
        priceCheck = [];
        for(var i=startTr;i<=endTr;i++){
            for(var j=startTd;j<=endTd;j++){
                var dom = $("#priceEvent").next().find('.layui-table-body tbody tr').eq(i).find('td').eq(j).find('div div');
                if(type==5 || type==6){
                    if(dom.length>0){
                        var ballId = $(dom).attr('data-ballId');
                        var columnId = $(dom).attr('data-columnId');
                        if(dom.hasClass('ico_check')){
                            momentPriceData[ballId+''+columnId].eyeTypeArr.remove(type-4);
                        }else{
                            if(momentPriceData[ballId+''+columnId]){
                                momentPriceData[ballId+''+columnId].eyeTypeArr.push(type-4);
                            }else{
                                var param = {};
                                param.properties = {};
                                param.properties[yxQiuId] = ballId;
                                param.properties[yxZhuId] = columnId;
                                param.eyeTypeArr = [type-4];
                                momentPriceData[ballId+''+columnId] = param;
                            }
                        }
                        dom.toggleClass('ico_check')
                        dom.toggleClass('icon-ico_check')
                    }
                }else{
                    if(startTr==endTr && startTd==endTd){
                        $("#priceEvent").next().find('.layui-table-body tbody tr').eq(startTr).find('td').eq(startTd).find('input').focus();
                    }else{
                        dom.addClass('dragBorder')
                    }
                }

                var obj = {dom:dom};
                if(dom.length>0){
                    priceCheck.push(obj);
                }
            }
        }
    });
    $(document).mouseup(function () {
        var time = setInterval(function () {
            if($('#dragDiv').length>0){
                $('#dragDiv').remove();
            }else{
                clearInterval(time)
            }
        })
        return false
    })
}

function changeYjStatus(self) {
    $(self).toggleClass('active');
}
var showInterType;//  1零售价  4 积分价
function openZeroPrice(type) {
    showInterType = type;
    var html = [];
    html.push('<div class="layui-yj-header"> <div class="layui-layer-title mine-title"> <div>设定价格<span>规格: 片</span></div> </div>');
    // html.push('<div class="title-right" onclick="openRedTip()" > 设置预警库存 </div>');
    html.push('</div> </div>');
    layer.open({
        type: 1,shade: 0.01,title:html.join(''),
        skin: 'layui-layer-yj layui-layer-add-goods layui-layer-view-price', //样式类名
        anim: 0,
        move:'.mine-title',
        area:['720px','560px'],
        closeBtn: 0, //不显示关闭按钮
        resize:false,
        shadeClose: true, //开启遮罩关闭
        content: $('.content-view-price'),
        btn: ['取消','保存'],
        yes: function(index, layero){
            layer.close(index)
        },
        btn2: function(){
            params[cid].skuJson = [];
            for(var i in momentPriceData){
                if(momentPriceData[i].eyeTypeArr){
                    for(var j=0;j<momentPriceData[i].eyeTypeArr.length;j++){
                        var obj = {};
                        for(var k in momentPriceData[i]){
                            if(k!='eyeTypeArr'){
                                obj[k] = momentPriceData[i][k];
                            }
                        }
                        obj.eyeType =momentPriceData[i].eyeTypeArr[j];
                        params[cid].skuJson.push(obj)
                    }
                }
            }
            for (var i = 0; i < params[cid].skuJson.length; i++) {
                delete params[cid].skuJson[i].eyeTypeArr;
            }
            momentPriceData = {};
        },
        success: function(layero, index){
            if(params[cid] && params[cid].skuJson){
                for(var i=0;i<params[cid].skuJson.length;i++){
                    if(momentPriceData[params[cid].skuJson[i].properties[yxQiuId]+''+params[cid].skuJson[i].properties[yxZhuId]]){
                        momentPriceData[params[cid].skuJson[i].properties[yxQiuId]+''+params[cid].skuJson[i].properties[yxZhuId]].eyeTypeArr.push(params[cid].skuJson[i].eyeType);
                    }else{
                        params[cid].skuJson[i].eyeTypeArr = [params[cid].skuJson[i].eyeType];
                        momentPriceData[params[cid].skuJson[i].properties[yxQiuId]+''+params[cid].skuJson[i].properties[yxZhuId]] = params[cid].skuJson[i];
                    }
                }
            }
            $('.layui-layer-view-price .layui-layer-btn').append('<div class="view-price-add"><input class="yj-input" placeholder="输入数量" type="number" id="numInput"><button class="btn btn-primary" onclick="numInputBtn('+type+')">键入</button></div>')
            setPrice(type)
        }
    });
}

function openGoodsRange(title,isUpdate) {
    layer.open({
        type: 1,shade: 0.01,title:'设定'+title+'范围<span>点击或框选单元格来设定</span>',
        skin: 'layui-layer-yj', //样式类名
        anim: 0,
        area:['720px','560px'],
        closeBtn: 0, //不显示关闭按钮
        resize:false,
        shadeClose: true, //开启遮罩关闭
        content: $('.content-view-range'),
        btn: ['取消','保存'],
        yes: function(index, layero){
            layer.close(index)
        },
        btn2: function(index, layero){
            params[cid].nowRanges = [];
            for(var i in momentNowRangeData){
                if(momentNowRangeData[i]){
                    params[cid].nowRanges.push(momentNowRangeData[i])
                }
            }
            momentNowRangeData = {};
            params[cid].ranges = [];
            for(var i in momentRangeData){
                if(momentRangeData[i]){
                    params[cid].ranges.push(momentRangeData[i])
                }
            }
            momentRangeData = {};

        },
        success: function(layero, index){
            setRange(title,isUpdate)
        }
    });
}
//初始化范围表格
function setRange(title,isUpdate) {
    //现货数据
    if(params[cid] && params[cid].nowRanges){
        for(var i=0;i<params[cid].nowRanges.length;i++){
            momentNowRangeData[params[cid].nowRanges[i].ballId+''+params[cid].nowRanges[i].columnId] = params[cid].nowRanges[i];
        }
    }
    //定制数据
    if(params[cid] && params[cid].ranges){
        for(var i=0;i<params[cid].ranges.length;i++){
            momentRangeData[params[cid].ranges[i].ballId+''+params[cid].ranges[i].columnId] = params[cid].ranges[i];
        }
    }
    var cols = [];//球
    var rangeData = [];//柱
    cols.push({field:'num', minWidth:60,align:'center', title: '<div class="yj-layui-table-header"><span>球</span><span>柱</span></div>', fixed: 'left'});
    for(var i=0;i<ballData.length;i++){
        (function (i) {
            cols.push({field:'', minWidth:60,align:'center', title: ballData[i].content, id:ballData[i].id, unresize:true,templet:function (d) {
                    if(momentRangeData[ballData[i].id+''+d.id] && momentNowRangeData[ballData[i].id+''+d.id]){
                        return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="active activenow"></div>'
                    }else if(momentRangeData[ballData[i].id+''+d.id]){
                        return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="active"></div>'
                    }else if(momentNowRangeData[ballData[i].id+''+d.id]){
                        return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'" class="activenow"></div>'
                    }else{
                        return '<div data-ballId="'+ballData[i].id+'" data-columnId="'+d.id+'"></div>'
                    }
                }})
        })(i)
    }
    for(var i=0;i<columnData.length;i++){
        rangeData.push({num:columnData[i].content,id:columnData[i].id})
    }
    layui.use('table', function(){
        var table = layui.table;
        table.render({
            elem: '#rangeEvent'
            ,data:rangeData
            ,width: 688
            ,height: 416
            ,limit:40
            ,unresize:true
            ,cols: [cols]
        });
        initTableDrag(title)
    });
}
function initTableDrag(title) {
    var beginTrIndex;//开始选中的行的下标
    var beginTdIndex;//开始选中的列的下标
    var endTrIndex;//鼠标抬起时的行的下标
    var endTdIndex;//鼠标抬起时列的下标
    var x=0,y=0;//鼠标按下时的浏览器坐标
    var offsetx=0,offsety=0;//鼠标按下时的相对父元素的坐标
    $("#rangeEvent").next().find('.layui-table-body tbody').mousedown(function (e) {
        beginTrIndex = $(e.target).parents('tr').attr('data-index');
        beginTdIndex = $(e.target).parents('td').attr('data-field');
        x = e.clientX;
        y = e.clientY;
        offsetx = e.offsetX;
        offsety = e.offsetY;
        $('#dragDiv').remove();
        $("#rangeEvent").next().find('.layui-table-body tbody').append('<div id="dragDiv" style="position:fixed;z-index:99999999;background:rgba(0,0,0,0.3)"></div>')
        return false;
    }).mousemove(function (e) {
        if($('#dragDiv').length){
            if(e.clientX-x>0){
                $('#dragDiv').css('left',x+'px')
            }else{
                $('#dragDiv').css('left',e.clientX+'px')
            }
            if(e.clientY-y>0){
                $('#dragDiv').css('top',y+'px')
            }else{
                $('#dragDiv').css('top',e.clientY+'px')
            }
            $('#dragDiv').css('width',Math.abs(e.clientX-x)+'px');
            $('#dragDiv').css('height',Math.abs(e.clientY-y)+'px')
        }
    }).mouseup(function (e) {
        if(e.target.id=='dragDiv'){
            var w = document.getElementById('dragDiv').offsetWidth;
            var h = document.getElementById('dragDiv').offsetHeight;
            var trW = $("#rangeEvent").next().find('.layui-table-body tbody td')[0].offsetWidth;
            var trH = $("#rangeEvent").next().find('.layui-table-body tbody td')[0].offsetHeight;
            endTdIndex = beginTdIndex - Math.ceil((w-offsetx)/trW);
            endTrIndex = beginTrIndex - Math.ceil((h-offsety)/trH);
        }else{
            endTrIndex = $(e.target).parents('tr').attr('data-index');
            endTdIndex = $(e.target).parents('td').attr('data-field');
        }
        $('#dragDiv').remove();
        var startTr = Math.min(beginTrIndex,endTrIndex);
        var startTd = Math.min(beginTdIndex,endTdIndex);
        var endTr = Math.max(beginTrIndex,endTrIndex);
        var endTd = Math.max(beginTdIndex,endTdIndex);
        for(var i=startTr;i<=endTr;i++){
            for(var j=startTd;j<=endTd;j++){
                if(title=='现货'){
                    var dom = $("#rangeEvent").next().find('.layui-table-body tbody tr').eq(i).find('td').eq(j).find('div div');
                    if(dom.hasClass('active')){
                        momentRangeData[dom.attr('data-ballId')+''+dom.attr('data-columnId')] = '';
                        dom.removeClass('active')
                    }
                    if(dom.hasClass('activenow')){
                        momentNowRangeData[dom.attr('data-ballId')+''+dom.attr('data-columnId')] = '';
                    }else{
                        momentNowRangeData[dom.attr('data-ballId')+''+dom.attr('data-columnId')] = {ballId:dom.attr('data-ballId'),columnId:dom.attr('data-columnId')};
                    }
                    dom.toggleClass('activenow')
                }else if(title=='定制'){
                    var dom = $("#rangeEvent").next().find('.layui-table-body tbody tr').eq(i).find('td').eq(j).find('div div');
                    if(!dom.hasClass('activenow')){
                        if(dom.hasClass('active')){
                            momentRangeData[dom.attr('data-ballId')+''+dom.attr('data-columnId')] = '';
                        }else{
                            momentRangeData[dom.attr('data-ballId')+''+dom.attr('data-columnId')] = {ballId:dom.attr('data-ballId'),columnId:dom.attr('data-columnId')};
                        }
                        dom.toggleClass('active')
                    }
                }
            }
        }
    });
    $(document).mouseup(function () {
        var time = setInterval(function () {
            if($('#dragDiv').length>0){
                $('#dragDiv').remove();
            }else{
                clearInterval(time)
            }
        });
        return false
    })
}
//改变会员折扣
function changeVipStatus(self) {
    quakooUtils.stopEventBubble()
    $(self).toggleClass('active')
}

//输入品牌
function changeBrand() {
    params[cid].bid = '';
    params[cid].sid = '';
}
//输入系列
function changeSeries() {
    params[cid].sid = '';
}
//设定价格  键入
function numInputBtn(t) {
    var type;
    if(t==4){
        type=4;
    }else{
        type = $('#setPriceBtn li.active').index()+1
    }
    var val = $("#numInput").val();
    if(!quakooUtils.isNum(val)){
        layer.msg('请输入数字');
        $("#numInput").val('');
        return
    }
    for(var i=0;i<priceCheck.length;i++){
        priceCheck[i].dom.find('input').val(val);
        priceInputChange(priceCheck[i].dom.find('input')[0],type)
        priceCheck[i].dom.removeClass('dragBorder')
    }
}
//价格改变
function priceInputChange(self,type) {
    if(!quakooUtils.isNum(self.value)){
        layer.msg('请输入数字');
        self.value = '';
        return
    }
    var str;
    if(type==1){
        str = 'retailPrice'
    }else if(type==2){
        str = 'costPrice'
    }else if(type==3){
        str = 'num'
    }else if(type==4){
        str = 'integralPrice'
    }
    var ballId = $(self).parent().attr('data-ballId');
    var columnId = $(self).parent().attr('data-columnId');
    if(momentPriceData[ballId+''+columnId]){
        momentPriceData[ballId+''+columnId][str] = self.value;
    }else{
        var param = {};
        param.properties = {};
        param.properties[yxQiuId] = ballId;
        param.properties[yxZhuId] = columnId;
        param[str] = self.value;
        param.eyeTypeArr = [];
        momentPriceData[ballId+''+columnId] = param;
    }
}

//上传门店图片
function uploadImg(self) {
    quakooImg.open(function (data) {
        for(var i=0;i<data.length;i++){
            if($(self).prevAll('.imgBox').length<3){
                var obj = quakooImg.processImg(data[i],100,100)
                var str = '<div class="imgBox"><img src="'+obj.url+'" real-src="'+data[i]+'" style="'+obj.style+'" alt=""><div class="del" onclick="delRegImg(this)"></div></div>'
                $(self).before(str)
            }
        }
        if($(self).prevAll('.imgBox').length>=3){
            $(self).hide();
        }
    })
}
function delRegImg(self) {
    $(self).parent('.imgBox').remove()
    if ($('.imgBox .imgBox').length < 3) {
        $("#test2").show();
    }
}
//编辑商品
function updateGoods(id) {
    quakooUtils.stopEventBubble()
    quakooData.ajaxGetData(config.getUrl_product_loadSPU(),{id:id},function (ret) {
        if(ret.success){
            updateParams = ret.data;
            updateParams.skuObj = {};
            for (var i = 0; i < updateParams.skuList.length; i++) {
                updateParams.skuObj[updateParams.skuList[i].properties[41]+''+updateParams.skuList[i].properties[42]+updateParams.skuList[i].eyeType] = updateParams.skuList[i]
            }
            addGoods(true)
        }
    })
}