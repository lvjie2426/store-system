/**
 * Created by Beyond on 2018/11/21.
 */
/*自定义单选框*/
$(function() {
    $('.prop label').click(function(){
        var radioId = $(this).attr('name');
        $('.prop label').removeAttr('class') && $(this).attr('class', 'selected');
        $('input[type="radio"]').removeAttr('checked') && $('#' + radioId).attr('checked', 'checked');
    });
    $('.sex label').click(function(){
        var radioId = $(this).attr('name');
        $('.sex label').removeAttr('class') && $(this).attr('class', 'selected');
        $('input[type="radio"]').removeAttr('checked') && $('#' + radioId).attr('checked', 'checked');
    });
});
$(function(){
   $('.three label').click(function(){
       var radioId = $(this).attr('name');
       $('.three label').removeAttr('class')&& $(this).attr('class','selected');
       $(this).child('input[type="radio"]').removeAttr('checked')&& $('#'+radioId).attr('checked','checked');
   });
});


/*自定义开关按钮*/
$(function(){
   $(".onOff i").click(function(){
       var status=$(this).attr('class');
       if(status==="on"){
           $(this).attr('class',"off").siblings('span').html('未开启');
       }else{
           $(this).attr('class',"on").siblings('span').html('已开启');
       }
       status=!status;
   });
});






/*自定义进度条*/
/*三种状态：down move up*/
/**/
var flag=false;
var cur={
    x:0,
    y:0
};
function down(){
    flag=false;
}



var moveElem = document.querySelector('#circle1'); //待拖拽元素

var dragging=false; //是否激活拖拽状态
var initX,newX,moveX; //鼠标按下时相对于选中元素的位移

//监听鼠标按下事件
document.addEventListener('mousedown', function(e) {
    if (e.target == moveElem) {
        dragging = true; //激活拖拽状态
        var moveElemRect = moveElem.getBoundingClientRect();
        var a=moveElemRect.left;
        initX= e.clientX;
        console.log("initX:"+initX);
    }
});

//监听鼠标放开事件
document.addEventListener('mouseup', function(e) {
    dragging = false;
});

//监听鼠标移动事件
document.addEventListener('mousemove', function(e) {
    if (dragging) {
        moveX= e.clientX-initX;
        moveElem.style.left = moveX + 'px';
        //console.log(moveElem.style.left);

    }
});
