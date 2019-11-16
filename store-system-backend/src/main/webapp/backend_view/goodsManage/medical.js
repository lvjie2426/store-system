//添加商品
function addGoods() {
    var title = '<div class="toastHead">' +
        '<span id="addPopupName">新增隐形眼镜</span>' +
        '<span id="removeStaff">销售状态 ' +
        '<span id="status1" onclick="$(this).toggleClass(\'active\')"></span>' +
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
        closeBtn: 0, //不显示关闭按钮
        resize: false,
        shadeClose: true, //开启遮罩关闭
        content: $('.content-view-addgoods'),
        btn: ['取消', '新增'],
        yes: function () {

        }
    });
};

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
        $('#warnInput').attr('disabled', true)
    } else {
        $('.select.s3').addClass('active')
        $('#warnInput').attr('disabled', false)
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