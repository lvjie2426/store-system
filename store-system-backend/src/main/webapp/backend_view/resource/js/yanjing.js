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
        return opDN(false);
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