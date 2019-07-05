var barArr = [];//添加进去的标签
var moveBar = '';//当前移动的html元素
function initDrag(){
    $('#fill-div').html(TextAddSpan($('#fill-div').html(),barArr))
}
function drag(ev) {
    ev.dataTransfer.setData("id",ev.target.id);
    ev.dataTransfer.setData("Text",ev.target.outerHTML);
    moveBar = ev.target.outerHTML;
    var moveObj = {html:ev.target.outerHTML,text:ev.target.innerText};
    var flag = true;
    for(var i=0;i<barArr.length;i++){
        if(barArr[i].text == ev.target.innerText){
            flag = false;
            break ;
        }
    }
    if(flag){
        barArr.push(moveObj);
    }

}
function allowDrop(ev) {
    ev.preventDefault();
}
function drop(ev) {
    ev.preventDefault();
    if(ev.target.className.indexOf('movebar')>=0){
        $('#fill-div').html($('#fill-div').html().replace(/<span>/g,'').replace(/<\/span>/g,''))
        return
    }
    if(ev.target.id=='moveBar'){
        for(var i=0;i<barArr.length;i++){
            if(barArr[i].html==moveBar){
                barArr.splice(i,1)
            }
        }
    }
    ev.target.appendChild(document.getElementById(ev.dataTransfer.getData("id")))
}
function changeArea(e) {
    var html = $('#fill-div').html();
    for(var i=0;i<barArr.length;i++){
        html = html.replace(barArr[i].html,'&'+barArr[i].text+'&');
    }
    html = html.replace(/<span>/g,'').replace(/<\/span>/g,'')
    $('#fill-text').val(html).css('display','block').focus();
    window.event? window.event.cancelBubble = true : e.stopPropagation();
}
function textareaClick(e) {
    window.event? window.event.cancelBubble = true : e.stopPropagation();
}
function textareaBlur() {
    if($('#fill-text').length){
        var text = $('#fill-text').val().split('&');
        var newarr = [];
        for(var m=0;m<barArr.length;m++){
            newarr.push(barArr[m])
        }
        for(var i=0;i<text.length;i++){
            for(var j=0;j<newarr.length;j++){
                if(text[i] == newarr[j].text){
                    text[i] = newarr[j].html;
                    newarr.splice(j,1)
                }
            }
        }
        $('#fill-div').html(TextAddSpan(text.join(''),barArr))
        $('#fill-text').css('display','none');
    }

}
$('body').click(function () {
    textareaBlur()
})
function TextAddSpan(html,arr) {
    // 尊敬的&顾客姓名&你好，你在&门店名称&消费了元，欢迎你下次再来。
    if(html.indexOf('<span>')<0){
        var htmlToSpan = '';
        for(var i =0;i<arr.length;i++){
            // debugger
            if(html.indexOf(arr[i].html)>-1){
                var newstr = arr[i].html.replace(/\//g,'\\/').replace(/\(/g,'\\(').replace(/\)/g,'\\)');
                html = html.replace(new RegExp(newstr,'gm'),'&'+i+'&');
            }
        }
        for(var j = 0;j<html.length;j++){
            if(html[j]=='&'){
                htmlToSpan += arr[html[j+1]].html;
                j = j+2;
            }else{
                htmlToSpan += '<span>'+html[j]+'</span>';
            }
        }
        return htmlToSpan;
    }else{
        return html;
    }
}