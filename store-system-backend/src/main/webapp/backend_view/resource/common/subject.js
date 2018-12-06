/**
 *
 */

//获取所有学科类型
function getSubjectTypeSelect(){
    var url = "/subjectType/getAllSubjectType";
    myAjax(url,"",handle);
    function handle(data,param){
        multipleSelect(data,"#basic");
    }
}


//校验code是否重复
function searchSubjectByCode(){
    var code = $(this).val();
    var url = "/subject/searchSubjectByCode";
    if(isNotBlank(code)){
        myAjax(url,{code:code},handle);
        function handle(data,param){
            if(data && data.success){
                $(".codevalidate").css({color:"#23c6c8"});
                $('.codevalidate').text("学科编码验证通过");
                $('.codevalidate').show().delay(3000).hide(300);
            }else{
                $("#code").val("");
                $(".codevalidate").css({color:"red"});
                $('.codevalidate').text("学科编码已存在");
                $('.codevalidate').show().delay(3000).hide(300);
            }
        }
    }

}