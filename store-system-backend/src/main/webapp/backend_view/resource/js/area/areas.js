$(function(){
    document.body.attributes += "<script language=javascript src='areaUtils.js'></script>"
    var sd = areaJson.oneList;
    var arr = [];
    for (i in sd) {
        arr[i] = sd[i];
    }

    var er = [];
    var sele = document.getElementById("province");
    var obj1 = '<option value="">--请选择省份--</option>';
    for (var i = 1; i < arr.length; i++) {
        var pre = pre;
        if (pre == i) {
            sele.innerHTML = obj1 += "<option value='" + arr[i].id + "' name='" + arr[i].name + "'  >" + arr[i].name + "</option>"
        }}
    var sd = $("#province").val()
    var areaInfo = areaJson.getSubList(sd);
    var sele = document.getElementById("citys");
    var obj1 = '<option value="">--请选择城市--</option>';

    for(var i=0;i<areaInfo.length;i++){
                sele.innerHTML = obj1 += "<option value='" + areaInfo[i].id + "' name='" + areaInfo[i].name + "'  >" + areaInfo[i].name + "</option>"

        }
})
function cityList(){
    var sd = $("#citys").val()
    var areaInfo = areaJson.getSubList(sd);
    var sele = document.getElementById("district");
    var obj1 = '<option value="">--请选择城市--</option>';
    for(var i=0;i<areaInfo.length;i++){
        sele.innerHTML = obj1 += "<option value='" + areaInfo[i].id + "' name='" + areaInfo[i].name + "'  >" + areaInfo[i].name + "</option>"
    }
}