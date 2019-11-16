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