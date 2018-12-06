/**
 *
 */

//获取所有学段
function getAllDictionary(){
    var url = "/dictionary/getDictionaryByType";
    myAjax(url,{type:1},handle);
    function handle(data,param){
        var html = htmlRadio(data,"dictionary");
        $("#allDictionary").html(html);
    }
}
//获取所有学段
function multipleDictionary(){
    var url = "/dictionary/getDictionaryByType";
    myAjax(url,{type:1},handle);
    function handle(data,param){
        var html = htmlCheckBox(data,"dictionary");
        $("#allDictionary").html(html);
    }
}
//获取所有学科类型
function getSubjectTypeList(){
    var url = "/subjectType/getAllSubjectType";
    myAjax(url,"",handle);
    function handle(data,param){
        var html = htmlCheckBox(data,"subject");
        $("#allSubject").html(html);
    }
}
//获取所有的考试类型
function getTestTypeList(){
    var url = "/testType/getAllList";
    myAjax(url,"",handle);
    function handle(data,param){
        var html = htmlCheckBox(data,"testType");
        $("#testType").html(html);
    }
}
//获取所有新闻类型
function getAllNewType(type){
    var url = "/news/getNewTypeList";
    myAjax(url,{type:type},handle);
    function handle(data,param){
        multipleSelect(data,"#newtype");
    }
}

/*------------------选择学段后再选学科类型-------------------*/
function dictionaryAndSubject(){
    var obj = {};
    var dictionary = $('input[name=dictionary]:checked').data("value");
    var dname = $('input[name=dictionary]:checked').next("label").text();
    var subjectType = [];
    $("input[name='subject']:checkbox").each(function(){
        if(true == $(this).is(':checked')){
            subjectType.push($(this).data("value"));
        }
    });
    if(isBlank(dictionary)){
        showValidate("#allDictionary");
        return;
    }
    //if(subjectType.length<1){
    //    showValidate("#allSubject");
    //    return;
    //}
    if(subjectType.length>0){
        obj.subjectType= subjectType;
    }else{
        obj.subjectType= [0];
    }

    obj.id = dictionary;
    obj.name =dname;

    return obj;
}
function echoDictionary(id){
    $("input[name='dictionary']").prop('checked',false);
    $("input[name='subject']").prop('checked',false);
    //学段选中
    $("input[name='dictionary']:radio").each(function () {
        var data = $(this).data("value");
        if (id == data) {
            $(this).prop('checked',true);
        }

    });

    //学科选中
    if(isNotBlank(subjectObj)){
        var obj = subjectObj[id];
        var sType =  obj.subjectType;
        for (var i = 0; i < sType.length; i++) {
            $("input[name='subject']:checkbox").each(function () {
                var data = $(this).data("value");
                if (sType[i] == data) {
                    $(this).prop('checked',true);
                }

            });
        }

    }
}

function addDictionary(){
    var step1V = dictionaryAndSubject();
    if(isNotBlank(step1V)){
        var obj = subjectObj[step1V.id];

        if(isBlank(obj)){
            addTem("checkedSubjectType",{id:step1V.id,data:step1V.name});
            subjectObj[step1V.id] = step1V;
        }else{
            subjectObj[step1V.id] = step1V;
            showValidateMsg("#checkedSubjectType","当前数据已更新");
        }
    }

}

function deleteSubjectObj(v){
    delete(subjectObj[v]);
}
//动态添加item
function clickTem(){
    $("#checkedSubjectType").on("click",".tagItem", function () {
        var v = $(this).find('span').data("value");
        echoDictionary(v);
    })
}

function deleteTem(){
    $("#checkedSubjectType").on("mousedown",".delete", function () {
        var v = $(this).parent().find('span').data("value");
        $(this).parent().remove();
        deleteSubjectObj(v);
    })
}

function addTem(dom,obj){
    var html = '<div class="tagItem" ><span data-value='+obj.id+' >'+obj.data+'</span><div class="delete"></div></div>';
    $("#"+dom).append(html);
}

