/***
 * 选择下拉框
 * @param ele       绑定的元素
 * @param callback  选择的值{text:"",value:""}  text 文本内容，value li自定义属性值
 */
function selectTag(ele, callback) {
    var optionBox = ele.find('.optionBox'),
        content;
    function opDN(type, e) {
        $('.optionBox').hide(500);
        type ? optionBox.show(500) : optionBox.hide(500);
        ele.addClass('ON');
        if (!type) {
            ele.find('.zi_dingyi_input').val('');
            ele.find('.zi_dingyi_input_Box').hide();
            ele.find('.zdy_pingpai').show();
            ele.find('.select-box-header i').removeClass('icon-ico_arrow_up')
        } else {
            ele.find('.select-box-header i').addClass('icon-ico_arrow_up')
        }
        quakooUtils.stopEventBubble(e)
    }
    ele[0].onclick = function(event){
        $(".ON").find('.select-box-header i').removeClass('icon-ico_arrow_up');
        if(event.preventDefault){
            event.preventDefault();
        }else{
            window.event.returnValue == false;
        }
        return opDN(optionBox.css('display')=='none' ? true : false, event);
    }

    document.body.onclick = function(){
        $('.optionBox').hide(500);
    }
    if($('.layui-layer').length){
        if($('.layui-layer').attr('addClickHandleFlag')!=1){
            $('.layui-layer').attr('addClickHandleFlag',1)
            $('.layui-layer').click(function () {
                return opDN(false);
            })
        }
    }


    ele.find('.optionUL>li').on('click', function (event) {
        content = $(this).contents().filter(function (index, content) {
            return content.nodeType === 3;
        }).text();
        opDN(false, event);
        ele.find('.text').text(content).css('color','#393953');
        if (callback) {
            if ($(this).attr('value')) {
                callback({el:ele, value: $(this).attr('value'), text: content })
            } else {
                callback({el:ele,  value: content, text: content })
            }
        }
    });
    ele.find('.optionUL>li>i').on('click',function(e){
        if($(this).parents(".optionUL").find('li').length==1){
           opDN(false); 
        }
        $(this).parent().remove();
        quakooUtils.stopEventBubble(e)
    })
    if (ele.find('.zi_dingyi').length) {
        ele.css('min-width', '100px')
        ele.find('.zi_dingyi').on('click', function (e) {
            quakooUtils.stopEventBubble(e)
        });
        ele.find('.zdy_pingpai').on('click', function () {
            $(this).hide(500);
            ele.find('.zi_dingyi_input_Box').show(500);
        });
        ele.find('.btn_conten').on('click', function (event) {
            content = $(this).parent().children('input').val();
            if (content == '') return alert('内容不能为空');
            ele.find('.text').text(content).css('color','#393953');
            if (callback) {
                callback({el:ele,  text: content, value: content })
            }
            opDN(false, event);
        });
    }

}

// canvas进度圈
function canvasFn(element, number) {

    var dom = null;
    dom = $('<canvas class="canvas" width="16" height="16"></canvas>')[0];

    var aaa = canFor(dom, element);

    function canFor(canvas, num) {
        var context = canvas.getContext('2d'),  //获取画图环境，指明为2d
            centerX = canvas.width / 2,   //Canvas中心点x轴坐标
            centerY = canvas.height / 2,  //Canvas中心点y轴坐标
            rad = Math.PI * 2 / 100, //将360度分成100份，那么每一份就是rad度
            speed = num; //加载的快慢就靠它了

        //绘制5像素宽的运动外圈
        function blueCircle(n) {
            context.save();
            context.strokeStyle = "#DD5B4B"; //设置描边样式
            context.lineWidth = 2; //设置线宽
            context.beginPath(); //路径开始
            context.arc(centerX, centerY, number / 2, -Math.PI / 2, -Math.PI / 2 + n * rad, false); //用于绘制圆弧context.arc(x坐标，y坐标，半径，起始角度，终止角度，顺时针/逆时针)
            context.stroke(); //绘制
            context.closePath(); //路径结束
            context.restore();
        }

        //绘制白色外圈
        function whiteCircle() {
            context.save();
            context.beginPath();
            context.lineWidth = 2; //设置线宽
            context.strokeStyle = "#FBEEED";
            context.arc(centerX, centerY, number / 2, 0, Math.PI * 2, false);
            context.stroke();
            context.closePath();
            context.restore();
        }

        function inte() {
            context.clearRect(0, 0, canvas.width, canvas.height);
            whiteCircle();
            blueCircle(speed);
            var ele = $('<img src=""/>')[0];
            ele.src = canvas.toDataURL("image/png");
            return ele
        }

        return inte();
    }

    //新Image对象，可以理解为DOM

    // canvas.toDataURL 返回的是一串Base64编码的URL
    // 指定格式 PNG
    var D = document.createElement('div');
    D.appendChild(aaa);
    return D.innerHTML;
}