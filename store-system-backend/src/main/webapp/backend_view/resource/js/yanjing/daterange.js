function dateRange(dom,fixed) {
    this.dom = $(dom).eq(0);
    this.fixed = fixed?fixed:false;  //是否固定定位
    this.checkStart = '';  //确定的开始日期
    this.checkEnd = '';  //确定的结束日期
    this.showYear = new Date().getFullYear();  //开始日期的年数
    this.showMonth = new Date().getMonth() + 1;//开始日期的月数
    this.showDays = new Date().getDate();      //开始日期的天数
    this.showYearEnd = new Date(this.showYear,this.showMonth,this.showDays).getFullYear();      //结束日期的年数
    this.showMonthEnd = new Date(this.showYear,this.showMonth,this.showDays).getMonth() + 1;      //结束日期的月数
    this.showDaysEnd = new Date(this.showYear,this.showMonth,this.showDays).getDate();      //结束日期的天数
}
dateRange.prototype.init = function(){
    this.draw();
    this.bindEnvent();
}
dateRange.prototype.draw = function(){
    var str = this.drawCal(this.showYear,this.showMonth,this.showDays,this.showYearEnd,this.showMonthEnd,this.showDaysEnd);
    if(this.dom.find('.yj-date-box')){
        this.dom.find('.yj-date-box').remove()
    }
    this.dom.append(str);
}
dateRange.prototype.drawCal = function(iYear, iMonth ,iDays, endiYear, endiMonth, endiDays) {
    var myMonth = this.bulidCal(iYear, iMonth);
    var endMonth = this.bulidCal(endiYear, endiMonth);
    var htmlstr = '';
    if(this.dom.offset().left + 496 >$('body').width()){
        htmlstr+='<div class="yj-date-box" style="right: 0;left: auto">'
    }else{
        htmlstr+='<div class="yj-date-box" style="left: 0;right: auto">'
    }
    htmlstr += '<div class="yj-date-left">'+
            '<div class="yj-date-header">开始时间<span class="todayBtn">今天</span></div>'+
            '<div class="yj-date-main">'+
                '<div class="yj-date-main-head">'+
                    '<div class="yj-date-prev">'+
                        '<i class="iconfont icon-ico_double_arrow_lef"></i>'+
                        '<i class="iconfont icon-ico_arrow_left"></i>'+
                    '</div>'+
                    '<span class="showTime">'+this.showYear+'年'+this.showMonth+'月</span>'+
                    '<div class="yj-date-next">'+
                        '<i class="iconfont icon-ico_arrow_right "></i>'+
                        '<i class="iconfont icon-ico_double_arrow_rig"></i>'+
                    '</div>'+
                '</div>'+
                '<div class="yj-date-table">'+
                    '<div class="yj-date-tr">'+
                        '<div class="yj-date-th">'+myMonth[0][0]+'</div>'+
                        '<div class="yj-date-th">'+myMonth[0][1]+'</div>'+
                        '<div class="yj-date-th">'+myMonth[0][2]+'</div>'+
                        '<div class="yj-date-th">'+myMonth[0][3]+'</div>'+
                        '<div class="yj-date-th">'+myMonth[0][4]+'</div>'+
                        '<div class="yj-date-th">'+myMonth[0][5]+'</div>'+
                        '<div class="yj-date-th">'+myMonth[0][6]+'</div>'+
                    '</div>'+
                    '<div class="tj-table-tbody">'+this.drawCalendar(iYear,iMonth,iDays)+'</div>'+
                '</div>'+
            '</div>'+
        '</div>'+
        '<div class="yj-date-right">'+
            '<div class="yj-date-header">结束时间</div>'+
            '<div class="yj-date-main">'+
                '<div class="yj-date-main-head">'+
                    '<div class="yj-date-prev">'+
                        '<i class="iconfont icon-ico_double_arrow_lef"></i>'+
                        '<i class="iconfont icon-ico_arrow_left"></i>'+
                    '</div>'+
                    '<span class="showTime">'+this.showYearEnd+'年'+this.showMonthEnd+'月</span>'+
                    '<div class="yj-date-next">'+
                        '<i class="iconfont icon-ico_arrow_right "></i>'+
                        '<i class="iconfont icon-ico_double_arrow_rig"></i>'+
                    '</div>'+
                '</div>'+
                '<div class="yj-date-table">'+
                    '<div class="yj-date-tr">'+
                        '<div class="yj-date-th">'+endMonth[0][0]+'</div>'+
                        '<div class="yj-date-th">'+endMonth[0][1]+'</div>'+
                        '<div class="yj-date-th">'+endMonth[0][2]+'</div>'+
                        '<div class="yj-date-th">'+endMonth[0][3]+'</div>'+
                        '<div class="yj-date-th">'+endMonth[0][4]+'</div>'+
                        '<div class="yj-date-th">'+endMonth[0][5]+'</div>'+
                        '<div class="yj-date-th">'+endMonth[0][6]+'</div>'+
                    '</div>'+
                    '<div class="tj-table-tbody">'+this.drawCalendar(endiYear,endiMonth,endiDays)+'</div>'+
                '</div>'+
            '</div>'+
        '</div>'+
        '<div class="yj-date-btn">'+
            '<button class="btn btn-primary" type="button">确定</button>'+
        '</div>'+
    '</div>';
    return htmlstr;
};
dateRange.prototype.bindEnvent = function(){
    var _this = this;
    window.onresize = function () {
        if(_this.fixed){
            $('.yj-date-box').fadeOut('fast');
        }else{
            if(_this.dom.offset().left + 496 >$('body').width()){
                _this.dom.find('.yj-date-box').css({'position':'absolute','top':'30px','left':'auto','right':0})
            }else{
                _this.dom.find('.yj-date-box').css({'position':'absolute','top':'30px','right':'auto','left':0})
            }
        }
    }
    if(this.dom.parents('.layui-layer').length){
        this.dom.parents('.layui-layer')[0].onclick = function(){
            $('.yj-date-box').fadeOut('fast');
        }
    }

    this.dom.find('.yj-date')[0].onclick = function (e) {
        quakooUtils.stopEventBubble(e)
        if(_this.dom.find('.yj-date-box').css('display')=='none'){
            $('.yj-date-box').fadeOut('fast');
            _this.dom.find('.yj-date-box').fadeIn('fast');

            if(_this.fixed){
                if(e.clientY + _this.dom.find('.yj-date-box')[0].offsetHeight >document.body.clientHeight){
                    _this.dom.find('.yj-date-box').css({'position':'fixed','top':e.clientY-e.offsetY - _this.dom.find('.yj-date-box')[0].offsetHeight,'left':e.clientX-e.offsetX});
                }else{
                    _this.dom.find('.yj-date-box').css({'position':'fixed','top':e.clientY-e.offsetY+30,'left':e.clientX-e.offsetX});
                }
            }
        }else{
            $('.yj-date-box').fadeOut('fast');
            _this.dom.find('.yj-date-box').fadeOut('fast');
        }
    }
    $('body').click(function () {
        $('.yj-date-box').fadeOut('fast');
    })
    this.dom.find('.yj-date-box').on('click',function (e) {
        quakooUtils.stopEventBubble(e)
    })
    this.dom.find(".tj-table-tbody").on('click','.yj-date-record',function () {
        if($(this).parents('.yj-date-left').length){
            _this.showDays = $(this).html();
            _this.dom.find('.yj-date-left .yj-date-td').removeClass('active');
            $(this).addClass('active');
        }
        if($(this).parents('.yj-date-right').length){
            if(new Date(_this.showYear,_this.showMonth,_this.showDays).getTime()>new Date(_this.showYearEnd,_this.showMonthEnd,$(this).html()).getTime()){
                layer.msg('结束时间必须大于开始时间');
                return ;
            }
            _this.showDaysEnd = $(this).html();
            _this.dom.find('.yj-date-right .yj-date-td').removeClass('active');
            $(this).addClass('active');
        }
    });
    //确定
    this.dom.find('.yj-date-btn .btn-primary').on('click',function () {
        if(new Date(_this.showYear,_this.showMonth,_this.showDays).getTime()>new Date(_this.showYearEnd,_this.showMonthEnd,_this.showDaysEnd).getTime()){
            layer.msg('结束时间必须大于开始时间');
            return ;
        }
        _this.checkStart = _this.showYear + '.' + _this.showMonth + '.' + _this.showDays;
        _this.checkEnd = _this.showYearEnd + '.' + _this.showMonthEnd + '.' + _this.showDaysEnd;
        var str = _this.checkStart + '-' + _this.checkEnd;
        _this.dom.find('.yj-date span').html(str);
        _this.dom.find('.yj-date span').addClass("sel-time");
        _this.dom.find('.yj-date-box').fadeOut('fast');
    })
    //今日
    this.dom.find('.todayBtn').on('click',function () {
        var current = new Date();
        _this.showYear=current.getFullYear();
        _this.showMonth=current.getMonth() + 1;
        _this.showDays = current.getDate();
        _this.dom.find('.yj-date-left .tj-table-tbody').html(_this.drawCalendar(_this.showYear,_this.showMonth,_this.showDays))
        _this.dom.find('.yj-date-left .showTime').html(_this.showYear + '年' + _this.showMonth + '月')
    })
    //开始日期的跳转年月
    this.dom.find('.yj-date-left .yj-date-prev i').eq(0).on('click',function () {
        _this.showYear--;
        _this.dom.find('.yj-date-left .tj-table-tbody').html(_this.drawCalendar(_this.showYear,_this.showMonth,_this.showDays))
        _this.dom.find('.yj-date-left .showTime').html(_this.showYear + '年' + _this.showMonth + '月')
    })
    this.dom.find('.yj-date-left .yj-date-prev i').eq(1).on('click',function () {
        if(_this.showMonth==1){
            _this.showMonth = 12;
            _this.showYear--;
        }else{
            _this.showMonth--;
        }
        _this.dom.find('.yj-date-left .tj-table-tbody').html(_this.drawCalendar(_this.showYear,_this.showMonth,_this.showDays))
        _this.dom.find('.yj-date-left .showTime').html(_this.showYear + '年' + _this.showMonth + '月')
    })
    this.dom.find('.yj-date-left .yj-date-next i').eq(0).on('click',function () {
        if(_this.showMonth==12){
            _this.showMonth = 1;
            _this.showYear++;
        }else{
            _this.showMonth++;
        }
        _this.dom.find('.yj-date-left .tj-table-tbody').html(_this.drawCalendar(_this.showYear,_this.showMonth,_this.showDays))
        _this.dom.find('.yj-date-left .showTime').html(_this.showYear + '年' + _this.showMonth + '月')
    });
    this.dom.find('.yj-date-left .yj-date-next i').eq(1).on('click',function () {
        _this.showYear++;
        _this.dom.find('.yj-date-left .tj-table-tbody').html(_this.drawCalendar(_this.showYear,_this.showMonth,_this.showDays))
        _this.dom.find('.yj-date-left .showTime').html(_this.showYear + '年' + _this.showMonth + '月')
    })
    //结束日期的跳转年月
    this.dom.find('.yj-date-right .yj-date-prev i').eq(0).on('click',function () {
        _this.showYearEnd--;
        _this.dom.find('.yj-date-right .tj-table-tbody').html(_this.drawCalendar(_this.showYearEnd,_this.showMonthEnd,_this.showDaysEnd))
        _this.dom.find('.yj-date-right .showTime').html(_this.showYearEnd + '年' + _this.showMonthEnd + '月')
    })
    this.dom.find('.yj-date-right .yj-date-prev i').eq(1).on('click',function () {
        if(_this.showMonthEnd==1){
            _this.showMonthEnd = 12;
            _this.showYearEnd--;
        }else{
            _this.showMonthEnd--;
        }
        _this.dom.find('.yj-date-right .tj-table-tbody').html(_this.drawCalendar(_this.showYearEnd,_this.showMonthEnd,_this.showDaysEnd))
        _this.dom.find('.yj-date-right .showTime').html(_this.showYearEnd + '年' + _this.showMonthEnd + '月')
    })
    this.dom.find('.yj-date-right .yj-date-next i').eq(0).on('click',function () {
        if(_this.showMonthEnd==12){
            _this.showMonthEnd = 1;
            _this.showYearEnd++;
        }else{
            _this.showMonthEnd++;
        }
        _this.dom.find('.yj-date-right .tj-table-tbody').html(_this.drawCalendar(_this.showYearEnd,_this.showMonthEnd,_this.showDaysEnd))
        _this.dom.find('.yj-date-right .showTime').html(_this.showYearEnd + '年' + _this.showMonthEnd + '月')
    })
    this.dom.find('.yj-date-right .yj-date-next i').eq(1).on('click',function () {
        _this.showYearEnd++;
        _this.dom.find('.yj-date-right .tj-table-tbody').html(_this.drawCalendar(_this.showYearEnd,_this.showMonthEnd,_this.showDaysEnd))
        _this.dom.find('.yj-date-right .showTime').html(_this.showYearEnd + '年' + _this.showMonthEnd + '月')
    })
}
dateRange.prototype.getDaysInmonth = function(iMonth, iYear){
    var dPrevDate = new Date(iYear, iMonth, 0);
    return dPrevDate.getDate();
}
dateRange.prototype.bulidCal = function(iYear, iMonth) {
    var aMonth = new Array();
    aMonth[0] = new Array(7);
    aMonth[1] = new Array(7);
    aMonth[2] = new Array(7);
    aMonth[3] = new Array(7);
    aMonth[4] = new Array(7);
    aMonth[5] = new Array(7);
    aMonth[6] = new Array(7);
    var dCalDate = new Date(iYear, iMonth - 1, 1);
    var iDayOfFirst = dCalDate.getDay()-1;
    if(iDayOfFirst<0){
        iDayOfFirst = 6;
    }
    var iDaysInMonth = this.getDaysInmonth(iMonth, iYear);
    var iVarDate = 1;
    var d, w;
    aMonth[0][0] = "一";
    aMonth[0][1] = "二";
    aMonth[0][2] = "三";
    aMonth[0][3] = "四";
    aMonth[0][4] = "五";
    aMonth[0][5] = "六";
    aMonth[0][6] = "日";
    for (d = iDayOfFirst; d < 7; d++) {
        aMonth[1][d] = iVarDate;
        iVarDate++;
    }
    for (w = 2; w < 7; w++) {
        for (d = 0; d < 7; d++) {
            if (iVarDate <= iDaysInMonth) {
                aMonth[w][d] = iVarDate;
                iVarDate++;
            }
        }
    }
    return aMonth;
}
dateRange.prototype.drawCalendar = function(iYear, iMonth ,iDays) {
    var myMonth = this.bulidCal(iYear, iMonth);
    var htmls = new Array();
    var d, w;
    for (w = 1; w < 7; w++) {
        htmls.push('<div class="yj-date-tr">');
        for (d = 0; d < 7; d++) {
            if(typeof(myMonth[w][d]) != 'undefined'){
                if(myMonth[w][d] == iDays){
                    htmls.push('<div class="yj-date-td active yj-date-record">'+myMonth[w][d]+'</div>');
                }else{
                    htmls.push('<div class="yj-date-td yj-date-record">'+myMonth[w][d]+'</div>');
                }
            }else{
                htmls.push('<div class="yj-date-td"></div>');
            }
        }
        htmls.push("</div>");
    }
    return htmls.join('');
}
dateRange.prototype.getTime = function() {
    var add0 = function(m){
        return m < 10 ? '0' + m: m
    };
    var startArr = this.checkStart.split(".");
    add0(startArr[1]);
    add0(startArr[2]);
    var endArr = this.checkEnd.split(".");
    add0(endArr[1]);
    add0(endArr[2]);
    var startTime = startArr[0]+"."+startArr[1]+"."+startArr[2];
    var endTime = endArr[0]+"."+endArr[1]+"."+endArr[2];
    var startTimeStamp = new Date(startTime).getTime();
    var endTimeStamp = new Date(endTime).getTime();
    return [startTime,endTime,startTimeStamp,endTimeStamp];
};

/*
var calUtil = {
    //页面元素
    dom:"",
    isShow:true, //开始年份
    showYear:2018, //开始月份
    showMonth:10, //开始天数
    showDays:1, //结束年份
    showYearEnd:2018, //结束月份
    showMonthEnd:10, //结束天数
    showDaysEnd:1,
    eventName:"load",

    //初始化日历
    init:function(time,endTime){
        var current = new Date();
        var nextMonthCurrent = new Date(current.getFullYear(),current.getMonth() + 1,current.getDate());
        if(time){
            calUtil.showYear = time.split('.')[0];
            calUtil.showMonth = time.split('.')[1];
            calUtil.showDays = time.split('.')[2];
        }else{
            calUtil.showYear=current.getFullYear();
            calUtil.showMonth=current.getMonth() + 1;
            calUtil.showDays = current.getDate();
        }
        if(endTime){
            calUtil.showYearEnd = endTime.split('.')[0];
            calUtil.showMonthEnd = endTime.split('.')[1];
            calUtil.showDaysEnd = endTime.split('.')[2];
        }else{
            calUtil.showYearEnd = nextMonthCurrent.getFullYear();
            calUtil.showMonthEnd = nextMonthCurrent.getMonth() + 1;
            calUtil.showDaysEnd = nextMonthCurrent.getDate();
        }
        calUtil.draw();
        calUtil.bindEnvent();
    },
    draw:function(){
        var str = calUtil.drawCal(calUtil.showYear,calUtil.showMonth,calUtil.showDays,calUtil.showYearEnd,calUtil.showMonthEnd,calUtil.showDaysEnd);
        calUtil.dom.append(str);
    },
    //绑定事件
    bindEnvent:function(){
        calUtil.dom.find('.yj-date').click(function () {
            if(calUtil.isShow){
                calUtil.isShow = false;
                calUtil.dom.find('.yj-date-box').show(400);
            }else{
                calUtil.isShow = true;
                calUtil.dom.find('.yj-date-box').hide(400);
            }
            quakooUtils.stopEventBubble()
        })
        $('body').on('click',function () {
            calUtil.isShow = true;
            calUtil.dom.find('.yj-date-box').hide(400);
        })
        calUtil.dom.find('.yj-date-box').on('click',function () {
            quakooUtils.stopEventBubble()
        })
        calUtil.dom.find(".tj-table-tbody").on('click','.yj-date-record',function () {
            if($(this).parents('.yj-date-left').length){
                calUtil.showDays = $(this).html();
                $('.yj-date-left .yj-date-td').removeClass('active');
                $(this).addClass('active');
            }
            if($(this).parents('.yj-date-right').length){
                if(new Date(calUtil.showYear,calUtil.showMonth,calUtil.showDays).getTime()>new Date(calUtil.showYearEnd,calUtil.showMonthEnd,$(this).html()).getTime()){
                    layer.msg('结束时间必须大于开始时间');
                    return ;
                }
                calUtil.showDaysEnd = $(this).html();
                $('.yj-date-right .yj-date-td').removeClass('active');
                $(this).addClass('active');
            }
        });
        calUtil.dom.find('.yj-date-btn .btn-primary').on('click',function () {
            if(new Date(calUtil.showYear,calUtil.showMonth,calUtil.showDays).getTime()>new Date(calUtil.showYearEnd,calUtil.showMonthEnd,calUtil.showDaysEnd).getTime()){
                layer.msg('结束时间必须大于开始时间');
                return ;
            }
            var str = calUtil.showYear + '.' + calUtil.showMonth + '.' + calUtil.showDays + '-' + calUtil.showYearEnd + '.' + calUtil.showMonthEnd + '.' + calUtil.showDaysEnd;
            calUtil.dom.find('.yj-date span').html(str);
            calUtil.isShow = true;
            calUtil.dom.find('.yj-date-box').hide(400);
        })
        //今日
        calUtil.dom.find('.todayBtn').on('click',function () {
            var current = new Date();
            calUtil.showYear=current.getFullYear();
            calUtil.showMonth=current.getMonth() + 1;
            calUtil.showDays = current.getDate();
            calUtil.dom.find('.yj-date-left .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYear,calUtil.showMonth,calUtil.showDays))
            calUtil.dom.find('.yj-date-left .showTime').html(calUtil.showYear + '年' + calUtil.showMonth + '月')
        })
        calUtil.dom.find('.yj-date-left .yj-date-prev i').eq(0).on('click',function () {
            calUtil.showYear--;
            calUtil.dom.find('.yj-date-left .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYear,calUtil.showMonth,calUtil.showDays))
            calUtil.dom.find('.yj-date-left .showTime').html(calUtil.showYear + '年' + calUtil.showMonth + '月')
        })
        calUtil.dom.find('.yj-date-left .yj-date-prev i').eq(1).on('click',function () {
            if(calUtil.showMonth==1){
                calUtil.showMonth = 12;
                calUtil.showYear--;
            }else{
                calUtil.showMonth--;
            }
            calUtil.dom.find('.yj-date-left .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYear,calUtil.showMonth,calUtil.showDays))
            calUtil.dom.find('.yj-date-left .showTime').html(calUtil.showYear + '年' + calUtil.showMonth + '月')
        })
        calUtil.dom.find('.yj-date-left .yj-date-next i').eq(0).on('click',function () {
            if(calUtil.showMonth==12){
                calUtil.showMonth = 1;
                calUtil.showYear++;
            }else{
                calUtil.showMonth++;
            }
            calUtil.dom.find('.yj-date-left .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYear,calUtil.showMonth,calUtil.showDays))
            calUtil.dom.find('.yj-date-left .showTime').html(calUtil.showYear + '年' + calUtil.showMonth + '月')
        })
        calUtil.dom.find('.yj-date-left .yj-date-next i').eq(1).on('click',function () {
            calUtil.showYear++;
            calUtil.dom.find('.yj-date-left .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYear,calUtil.showMonth,calUtil.showDays))
            calUtil.dom.find('.yj-date-left .showTime').html(calUtil.showYear + '年' + calUtil.showMonth + '月')
        })
        //结束日期的跳转年月
        calUtil.dom.find('.yj-date-right .yj-date-prev i').eq(0).on('click',function () {
            calUtil.showYearEnd--;
            calUtil.dom.find('.yj-date-right .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYearEnd,calUtil.showMonthEnd,calUtil.showDaysEnd))
            calUtil.dom.find('.yj-date-right .showTime').html(calUtil.showYearEnd + '年' + calUtil.showMonthEnd + '月')
        })
        calUtil.dom.find('.yj-date-right .yj-date-prev i').eq(1).on('click',function () {
            if(calUtil.showMonthEnd==1){
                calUtil.showMonthEnd = 12;
                calUtil.showYearEnd--;
            }else{
                calUtil.showMonthEnd--;
            }
            calUtil.dom.find('.yj-date-right .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYearEnd,calUtil.showMonthEnd,calUtil.showDaysEnd))
            calUtil.dom.find('.yj-date-right .showTime').html(calUtil.showYearEnd + '年' + calUtil.showMonthEnd + '月')
        })
        calUtil.dom.find('.yj-date-right .yj-date-next i').eq(0).on('click',function () {
            if(calUtil.showMonthEnd==12){
                calUtil.showMonthEnd = 1;
                calUtil.showYearEnd++;
            }else{
                calUtil.showMonthEnd++;
            }
            calUtil.dom.find('.yj-date-right .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYearEnd,calUtil.showMonthEnd,calUtil.showDaysEnd))
            calUtil.dom.find('.yj-date-right .showTime').html(calUtil.showYearEnd + '年' + calUtil.showMonthEnd + '月')
        })
        calUtil.dom.find('.yj-date-right .yj-date-next i').eq(1).on('click',function () {
            calUtil.showYearEnd++;
            calUtil.dom.find('.yj-date-right .tj-table-tbody').html(calUtil.drawCalendar(calUtil.showYearEnd,calUtil.showMonthEnd,calUtil.showDaysEnd))
            calUtil.dom.find('.yj-date-right .showTime').html(calUtil.showYearEnd + '年' + calUtil.showMonthEnd + '月')
        })
    },
    //获取当前选择的年月
    setMonthAndDay:function(){
        switch(calUtil.eventName)
        {
            case "load":
                var current = new Date();
                calUtil.showYear=current.getFullYear();
                calUtil.showYearEnd=current.getFullYear();
                calUtil.showMonth=current.getMonth() + 1;
                calUtil.showMonthEnd=current.getMonth() + 2;
                break;
            case "prev":
                var nowMonth = calUtil.getMonthNum($("#month").text().split("年")[1].split("月")[0]);
                calUtil.showYear=$("#month").text().split("年")[0];
                calUtil.showMonth=parseInt(nowMonth)-1;
                if(calUtil.showMonth==0)
                {
                    calUtil.showMonth=12;
                    calUtil.showYear-=1;
                }
                break;
            case "next":
                var nowMonth=calUtil.getMonthNum($("#month").text().split("年")[1].split("月")[0]);
                calUtil.showYear=parseInt($("#month").text().split("年")[0]);
                calUtil.showMonth=parseInt(nowMonth)+1;
                if(calUtil.showMonth==13)
                {
                    calUtil.showMonth=1;
                    calUtil.showYear+=1;
                }
                break;
        }
    },
    getDaysInmonth : function(iMonth, iYear){
        var dPrevDate = new Date(iYear, iMonth, 0);
        return dPrevDate.getDate();
    },
    bulidCal : function(iYear, iMonth) {
        var aMonth = new Array();
        aMonth[0] = new Array(7);
        aMonth[1] = new Array(7);
        aMonth[2] = new Array(7);
        aMonth[3] = new Array(7);
        aMonth[4] = new Array(7);
        aMonth[5] = new Array(7);
        aMonth[6] = new Array(7);
        var dCalDate = new Date(iYear, iMonth - 1, 1);
        var iDayOfFirst = dCalDate.getDay()-1;
        if(iDayOfFirst<0){
            iDayOfFirst = 6;
        }
        var iDaysInMonth = calUtil.getDaysInmonth(iMonth, iYear);
        var iVarDate = 1;
        var d, w;
        aMonth[0][0] = "一";
        aMonth[0][1] = "二";
        aMonth[0][2] = "三";
        aMonth[0][3] = "四";
        aMonth[0][4] = "五";
        aMonth[0][5] = "六";
        aMonth[0][6] = "日";
        for (d = iDayOfFirst; d < 7; d++) {
            aMonth[1][d] = iVarDate;
            iVarDate++;
        }
        for (w = 2; w < 7; w++) {
            for (d = 0; d < 7; d++) {
                if (iVarDate <= iDaysInMonth) {
                    aMonth[w][d] = iVarDate;
                    iVarDate++;
                }
            }
        }
        return aMonth;
    },
    drawCal : function(iYear, iMonth ,iDays, endiYear, endiMonth, endiDays) {
        var myMonth = calUtil.bulidCal(iYear, iMonth);
        var endMonth = calUtil.bulidCal(endiYear, endiMonth);
        var htmlstr = `
        <div class="yj-date-box">
        <div class="yj-date-left">
            <div class="yj-date-header">开始时间<span class="todayBtn">今天</span></div>
            <div class="yj-date-main">
                <div class="yj-date-main-head">
                    <div class="yj-date-prev">
                        <i class="iconfont icon-ico_double_arrow_lef"></i>
                        <i class="iconfont icon-ico_arrow_left"></i>
                    </div>
                    <span class="showTime">${calUtil.showYear}年${calUtil.showMonth}月</span>
                    <div class="yj-date-next">
                        <i class="iconfont icon-ico_arrow_right "></i>
                        <i class="iconfont icon-ico_double_arrow_rig"></i>
                    </div>
                </div>
                <div class="yj-date-table">
                    <div class="yj-date-tr">
                        <div class="yj-date-th">${myMonth[0][0]}</div>
                        <div class="yj-date-th">${myMonth[0][1]}</div>
                        <div class="yj-date-th">${myMonth[0][2]}</div>
                        <div class="yj-date-th">${myMonth[0][3]}</div>
                        <div class="yj-date-th">${myMonth[0][4]}</div>
                        <div class="yj-date-th">${myMonth[0][5]}</div>
                        <div class="yj-date-th">${myMonth[0][6]}</div>
                    </div>
                    <div class="tj-table-tbody">
                    ${calUtil.drawCalendar(iYear,iMonth,iDays)}
                    </div>
                </div>
            </div>
        </div>
        <div class="yj-date-right">
            <div class="yj-date-header">结束时间</div>
            <div class="yj-date-main">
                <div class="yj-date-main-head">
                    <div class="yj-date-prev">
                        <i class="iconfont icon-ico_double_arrow_lef"></i>
                        <i class="iconfont icon-ico_arrow_left"></i>
                    </div>
                    <span class="showTime">${calUtil.showYearEnd}年${calUtil.showMonthEnd}月</span>
                    <div class="yj-date-next">
                        <i class="iconfont icon-ico_arrow_right "></i>
                        <i class="iconfont icon-ico_double_arrow_rig"></i>
                    </div>
                </div>
                <div class="yj-date-table">
                    <div class="yj-date-tr">
                        <div class="yj-date-th">${endMonth[0][0]}</div>
                        <div class="yj-date-th">${endMonth[0][1]}</div>
                        <div class="yj-date-th">${endMonth[0][2]}</div>
                        <div class="yj-date-th">${endMonth[0][3]}</div>
                        <div class="yj-date-th">${endMonth[0][4]}</div>
                        <div class="yj-date-th">${endMonth[0][5]}</div>
                        <div class="yj-date-th">${endMonth[0][6]}</div>
                    </div>
                    <div class="tj-table-tbody">
                    ${calUtil.drawCalendar(endiYear,endiMonth,endiDays)}
                    </div>
                </div>
            </div>
        </div>
        <div class="yj-date-btn">
            <button class="btn btn-primary">确定</button>
        </div>
    </div>
        
        `;
        return htmlstr;
    },
    drawCalendar : function(iYear, iMonth ,iDays) {
        var myMonth = calUtil.bulidCal(iYear, iMonth);
        var htmls = new Array();
        var d, w;
        for (w = 1; w < 7; w++) {
            htmls.push('<div class="yj-date-tr">');
            for (d = 0; d < 7; d++) {
                if(typeof(myMonth[w][d]) != 'undefined'){
                    if(myMonth[w][d] == iDays){
                        htmls.push('<div class="yj-date-td active yj-date-record">'+myMonth[w][d]+'</div>');
                    }else{
                        htmls.push('<div class="yj-date-td yj-date-record">'+myMonth[w][d]+'</div>');
                    }
                }else{
                    htmls.push('<div class="yj-date-td"></div>');
                }
            }
            htmls.push("</div>");
        }
        return htmls.join('');
    },
    getMonthNum: function (month) {
        var str;
        switch(month) {
            case '一': str = 1; break;
            case '二': str = 2; break;
            case '三': str = 3; break;
            case '四': str = 4; break;
            case '五': str = 5; break;
            case '六': str = 6; break;
            case '七': str = 7; break;
            case '八': str = 8; break;
            case '九': str = 9; break;
            case '十': str = 10; break;
            case '十一': str = 11; break;
            case '十二': str = 12; break;
            default :str = month
        }
        return str;
    }


};*/
