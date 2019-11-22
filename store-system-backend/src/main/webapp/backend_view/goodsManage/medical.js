//添加商品
function addGoods() {
    console.log($('#addGoodsDom'));
    var title = '<div  class="toastHead">' +
        '<span  id="addPopupName">新增隐形眼镜</span>' +
        '<span  id="removeStaff">销售状态  ' +
        '<span  id="status1"  onclick="$(this).toggleClass(\'active\')"></span>' +
        '</span></div>';
    layer.open({
        type: 1,
        shade: 0.01,
        title: title,
        move: '#addPopupName',
        skin: 'addGoods',
        area: ['360px', '550px'],
        anim: 0,
        maxWidth: '360',
        closeBtn: 0,  //不显示关闭按钮
        resize: false,
        shadeClose: true,  //开启遮罩关闭
        content: $('.content-view-addgoods'),
        btn: ['取消', '新增'],
        yes: function () {

        },
        success: function () {
            openYjGoods();
        }
    });
    $('.select.s1').click(function () {
        if ($('.select.s1').hasClass('active')) {
            $('.select.s1').removeClass('active')
            $('.goot-goot-huiyuan').slideToggle()
        } else {
            $('.select.s1').addClass('active')
            $('.goot-goot-huiyuan').slideToggle();
        }
    })
    $('.select.s2').click(function () {
        if ($('.select.s2').hasClass('active')) {
            $('.select.s2').removeClass('active')
            $('.goot-goot-putong.uItem').slideToggle()
        } else {
            $('.select.s2').addClass('active')
            $('.goot-goot-putong.uItem').slideToggle();
        }
    })
    $('.zhuceInfo-first').click(function () {
        $('.zhuceUl').slideToggle();
        $('.zhuceInfo-first').find('i').toggleClass('icon-ico_arrow_down').toggleClass('icon-ico_arrow_up');
    })
    $('.select.s3').click(function () {
        if ($('.select.s3').hasClass('active')) {
            $('.select.s3').removeClass('active')
            $('#warn1').attr('disabled', true)
        } else {
            $('.select.s3').addClass('active')
            $('#warn1').attr('disabled', false)
        }
    })
    //选择会员折扣
    $('.yj-radio').click(function () {
        if ($('.yj-radio').hasClass('active')) {
            $('.yj-radio').removeClass('active')
        } else {
            $('.yj-radio').addClass('active')
        }
    })
};


//新增商品弹出框
function openYjGoods() {
    var data = {
        typeData: typeData,
        shengS: shengS,
        gongS: gongS
    };
    var html = template('addGoodsTmp', data);
    document.getElementById('addGoodsDom').innerHTML = html;
    selectTag($("#yj-add-type"), function (value) {  //  类型下拉框
    })
    selectTag($("#yj-gongS"), function (value) {  //  供应商下拉框
    })
    selectTag($("#yj-shengS"), function (value) {  //  生产商下拉框
    })
    var html1 = template('selectTagTmpAll', {
        title: '品牌',
        list: brand
    });
    //商品列表的筛选
    $('#select-type').html(html1);
    selectTag($("#select-type"), function (data) {
        if (data.value) {
            bid = data.value;
            //获取品牌下的系列列表
            quakooData.ajaxGetData(config.getUrl_productSeries_getSubAllList(), {
                bid: data.value
            }, function (ret1) {
                if (ret1 && ret1.success) {
                    if (ret1.data.length > 0) {
                        var html2 = template('selectTagTmpAll', {
                            title: '系列',
                            list: ret1.data
                        });
                        $('#select-series').html(html2).show();
                        selectTag($("#select-series"), function (series) {
                            sid = series.value;
                        })
                    } else {
                        $('#select-series').hide();
                    }
                }
            })
        } else {
            $('#select-series').hide();
        }
    })
    //日期范围插件开始
    date5 = new dateRange(".yj-youxiao-time");
    date5.init();
    //日期范围插件结束
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
        if (!demo.match(/select/i)) {
            $('.demo-test-' + demo).val('');
        }
        $('.demo-test-' + demo).scroller('destroy').scroller($.extend(opt['datetime'], opt['default']));
        $('.demo').hide();
        $('.demo-' + demo).show();
    });

    $('#yjdemo').trigger('change');
}






