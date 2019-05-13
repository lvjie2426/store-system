/**
 *
 */
function initSelectSubject(dom,url,multiple,nomin){
	var minimumInputLength=1;if(nomin){minimumInputLength=0;}
	if(!url.startsWith(serverUrl)){
		url=serverUrl+url;
	}
	
    var subject = $(dom);//元素
    var Select2 = subject.select2({
        ajax: {
            url: url,
            dataType: 'json',
            data: function (params) {
                return {
                    name: params.term, // 搜索参数
                };
            },
            processResults: function (data, params) {
                // console.log(data);
                for (var i = 0; i < data.length; i++) {
                    data[i].id = data[i].id;
                    data[i].text = data[i].name;
                }
                return {
                    results: data
                };
            },
            cache: true
        },
        placeholder: "请选择或输入条件",
        allowClear: true,    //选中之后，可手动点击删除
        escapeMarkup: function (markup) { return markup; }, // 让template的html显示效果，否则输出代码
        minimumInputLength: minimumInputLength,    //搜索框至少要输入的长度，此处设置后需输入才显示结果
        language: "zh-CN",         //中文
        multiple:multiple, //是否多选
        width: 'resolve',
        closeOnSelect:false,
        templateResult: formatSubject, // 自定义下拉选项的样式模板
        templateSelection: formatSubjectSelection     // 自定义选中选项的样式模板
    });

    return Select2;
}

function formatSubject(item) {
    if (item.loading) return item;
    var markup = '<div> <p class="text-primary">学校名称：' + item.name|| item.text + '</p>';
    //markup += '这里可以添加其他选项...';
    markup += ' </div>';
    return markup;
}

function formatSubjectSelection(item) {
    return item.name || item.text;
}
//回显数据
function echoSelect2(dom,value){
    $.each(value,function(index,value){
        $(dom).append(new Option(value.name, value.id, false, true));
    });
    $(dom).trigger("change");
}
