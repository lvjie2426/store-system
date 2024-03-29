var cityJson = [
    {"item_code":"110000","item_name":"北京市"},
    {"item_code":"120000","item_name":"天津市"},
    {"item_code":"130000","item_name":"河北省"},
    {"item_code":"140000","item_name":"山西省"},
    {"item_code":"150000","item_name":"内蒙古自治区"},
    {"item_code":"210000","item_name":"辽宁省"},
    {"item_code":"220000","item_name":"吉林省"},
    {"item_code":"230000","item_name":"黑龙江省"},
    {"item_code":"310000","item_name":"上海市"},
    {"item_code":"320000","item_name":"江苏省"},
    {"item_code":"330000","item_name":"浙江省"},
    {"item_code":"340000","item_name":"安徽省"},
    {"item_code":"350000","item_name":"福建省"},
    {"item_code":"360000","item_name":"江西省"},
    {"item_code":"370000","item_name":"山东省"},
    {"item_code":"410000","item_name":"河南省"},
    {"item_code":"420000","item_name":"湖北省"},
    {"item_code":"430000","item_name":"湖南省"},
    {"item_code":"440000","item_name":"广东省"},
    {"item_code":"450000","item_name":"广西壮族自治区"},
    {"item_code":"460000","item_name":"海南省"},
    {"item_code":"500000","item_name":"重庆市"},
    {"item_code":"510000","item_name":"四川省"},
    {"item_code":"520000","item_name":"贵州省"},
    {"item_code":"530000","item_name":"云南省"},
    {"item_code":"540000","item_name":"西藏自治区"},
    {"item_code":"610000","item_name":"陕西省"},
    {"item_code":"620000","item_name":"甘肃省"},
    {"item_code":"630000","item_name":"青海省"},
    {"item_code":"640000","item_name":"宁夏回族自治区"},
    {"item_code":"650000","item_name":"新疆维吾尔自治区"},


    {"item_code":"110100","item_name":"北京市"},
    {"item_code":"120100","item_name":"天津市"},
    {"item_code":"130100","item_name":"石家庄市"},
    {"item_code":"130200","item_name":"唐山市"},
    {"item_code":"130300","item_name":"秦皇岛市"},
    {"item_code":"130400","item_name":"邯郸市"},
    {"item_code":"130500","item_name":"邢台市"},
    {"item_code":"130600","item_name":"保定市"},
    {"item_code":"130700","item_name":"张家口市"},
    {"item_code":"130800","item_name":"承德市"},
    {"item_code":"130900","item_name":"沧州市"},
    {"item_code":"131000","item_name":"廊坊市"},
    {"item_code":"131100","item_name":"衡水市"},
    {"item_code":"140100","item_name":"太原市"},
    {"item_code":"140200","item_name":"大同市"},
    {"item_code":"140300","item_name":"阳泉市"},
    {"item_code":"140400","item_name":"长治市"},
    {"item_code":"140500","item_name":"晋城市"},
    {"item_code":"140600","item_name":"朔州市"},
    {"item_code":"140700","item_name":"晋中市"},
    {"item_code":"140800","item_name":"运城市"},
    {"item_code":"140900","item_name":"忻州市"},
    {"item_code":"141000","item_name":"临汾市"},
    {"item_code":"141100","item_name":"吕梁市"},
    {"item_code":"150100","item_name":"呼和浩特市"},
    {"item_code":"150200","item_name":"包头市"},
    {"item_code":"150300","item_name":"乌海市"},
    {"item_code":"150400","item_name":"赤峰市"},
    {"item_code":"150500","item_name":"通辽市"},
    {"item_code":"150600","item_name":"鄂尔多斯市"},
    {"item_code":"150700","item_name":"呼伦贝尔市"},
    {"item_code":"150800","item_name":"巴彦淖尔市"},
    {"item_code":"150900","item_name":"乌兰察布市"},
    {"item_code":"152200","item_name":"兴安盟"},
    {"item_code":"152500","item_name":"锡林郭勒盟"},
    {"item_code":"152900","item_name":"阿拉善盟"},
    {"item_code":"210100","item_name":"沈阳市"},
    {"item_code":"210200","item_name":"大连市"},
    {"item_code":"210300","item_name":"鞍山市"},
    {"item_code":"210400","item_name":"抚顺市"},
    {"item_code":"210500","item_name":"本溪市"},
    {"item_code":"210600","item_name":"丹东市"},
    {"item_code":"210700","item_name":"锦州市"},
    {"item_code":"210800","item_name":"营口市"},
    {"item_code":"210900","item_name":"阜新市"},
    {"item_code":"211000","item_name":"辽阳市"},
    {"item_code":"211100","item_name":"盘锦市"},
    {"item_code":"211200","item_name":"铁岭市"},
    {"item_code":"211300","item_name":"朝阳市"},
    {"item_code":"211400","item_name":"葫芦岛市"},
    {"item_code":"220100","item_name":"长春市"},
    {"item_code":"220200","item_name":"吉林市"},
    {"item_code":"220300","item_name":"四平市"},
    {"item_code":"220400","item_name":"辽源市"},
    {"item_code":"220500","item_name":"通化市"},
    {"item_code":"220600","item_name":"白山市"},
    {"item_code":"220700","item_name":"松原市"},
    {"item_code":"220800","item_name":"白城市"},
    {"item_code":"222400","item_name":"延边朝鲜族自治州"},
    {"item_code":"230100","item_name":"哈尔滨市"},
    {"item_code":"230200","item_name":"齐齐哈尔市"},
    {"item_code":"230300","item_name":"鸡西市"},
    {"item_code":"230400","item_name":"鹤岗市"},
    {"item_code":"230500","item_name":"双鸭山市"},
    {"item_code":"230600","item_name":"大庆市"},
    {"item_code":"230700","item_name":"伊春市"},
    {"item_code":"230800","item_name":"佳木斯市"},
    {"item_code":"230900","item_name":"七台河市"},
    {"item_code":"231000","item_name":"牡丹江市"},
    {"item_code":"231100","item_name":"黑河市"},
    {"item_code":"231200","item_name":"绥化市"},
    {"item_code":"232700","item_name":"大兴安岭地区"},
    {"item_code":"310100","item_name":"上海市"},
    {"item_code":"320100","item_name":"南京市"},
    {"item_code":"320200","item_name":"无锡市"},
    {"item_code":"320300","item_name":"徐州市"},
    {"item_code":"320400","item_name":"常州市"},
    {"item_code":"320500","item_name":"苏州市"},
    {"item_code":"320600","item_name":"南通市"},
    {"item_code":"320700","item_name":"连云港市"},
    {"item_code":"320800","item_name":"淮安市"},
    {"item_code":"320900","item_name":"盐城市"},
    {"item_code":"321000","item_name":"扬州市"},
    {"item_code":"321100","item_name":"镇江市"},
    {"item_code":"321200","item_name":"泰州市"},
    {"item_code":"321300","item_name":"宿迁市"},
    {"item_code":"330100","item_name":"杭州市"},
    {"item_code":"330200","item_name":"宁波市"},
    {"item_code":"330300","item_name":"温州市"},
    {"item_code":"330400","item_name":"嘉兴市"},
    {"item_code":"330500","item_name":"湖州市"},
    {"item_code":"330600","item_name":"绍兴市"},
    {"item_code":"330700","item_name":"金华市"},
    {"item_code":"330800","item_name":"衢州市"},
    {"item_code":"330900","item_name":"舟山市"},
    {"item_code":"331000","item_name":"台州市"},
    {"item_code":"331100","item_name":"丽水市"},
    {"item_code":"340100","item_name":"合肥市"},
    {"item_code":"340200","item_name":"芜湖市"},
    {"item_code":"340300","item_name":"蚌埠市"},
    {"item_code":"340400","item_name":"淮南市"},
    {"item_code":"340500","item_name":"马鞍山市"},
    {"item_code":"340600","item_name":"淮北市"},
    {"item_code":"340700","item_name":"铜陵市"},
    {"item_code":"340800","item_name":"安庆市"},
    {"item_code":"341000","item_name":"黄山市"},
    {"item_code":"341100","item_name":"滁州市"},
    {"item_code":"341200","item_name":"阜阳市"},
    {"item_code":"341300","item_name":"宿州市"},
    {"item_code":"341500","item_name":"六安市"},
    {"item_code":"341600","item_name":"亳州市"},
    {"item_code":"341700","item_name":"池州市"},
    {"item_code":"341800","item_name":"宣城市"},
    {"item_code":"350100","item_name":"福州市"},
    {"item_code":"350200","item_name":"厦门市"},
    {"item_code":"350300","item_name":"莆田市"},
    {"item_code":"350400","item_name":"三明市"},
    {"item_code":"350500","item_name":"泉州市"},
    {"item_code":"350600","item_name":"漳州市"},
    {"item_code":"350700","item_name":"南平市"},
    {"item_code":"350800","item_name":"龙岩市"},
    {"item_code":"350900","item_name":"宁德市"},
    {"item_code":"360100","item_name":"南昌市"},
    {"item_code":"360200","item_name":"景德镇市"},
    {"item_code":"360300","item_name":"萍乡市"},
    {"item_code":"360400","item_name":"九江市"},
    {"item_code":"360500","item_name":"新余市"},
    {"item_code":"360600","item_name":"鹰潭市"},
    {"item_code":"360700","item_name":"赣州市"},
    {"item_code":"360800","item_name":"吉安市"},
    {"item_code":"360900","item_name":"宜春市"},
    {"item_code":"361000","item_name":"抚州市"},
    {"item_code":"361100","item_name":"上饶市"},
    {"item_code":"370100","item_name":"济南市"},
    {"item_code":"370200","item_name":"青岛市"},
    {"item_code":"370300","item_name":"淄博市"},
    {"item_code":"370400","item_name":"枣庄市"},
    {"item_code":"370500","item_name":"东营市"},
    {"item_code":"370600","item_name":"烟台市"},
    {"item_code":"370700","item_name":"潍坊市"},
    {"item_code":"370800","item_name":"济宁市"},
    {"item_code":"370900","item_name":"泰安市"},
    {"item_code":"371000","item_name":"威海市"},
    {"item_code":"371100","item_name":"日照市"},
    {"item_code":"371200","item_name":"莱芜市"},
    {"item_code":"371300","item_name":"临沂市"},
    {"item_code":"371400","item_name":"德州市"},
    {"item_code":"371500","item_name":"聊城市"},
    {"item_code":"371600","item_name":"滨州市"},
    {"item_code":"371700","item_name":"菏泽市"},
    {"item_code":"410100","item_name":"郑州市"},
    {"item_code":"410200","item_name":"开封市"},
    {"item_code":"410300","item_name":"洛阳市"},
    {"item_code":"410400","item_name":"平顶山市"},
    {"item_code":"410500","item_name":"安阳市"},
    {"item_code":"410600","item_name":"鹤壁市"},
    {"item_code":"410700","item_name":"新乡市"},
    {"item_code":"410800","item_name":"焦作市"},
    {"item_code":"410900","item_name":"濮阳市"},
    {"item_code":"411000","item_name":"许昌市"},
    {"item_code":"411100","item_name":"漯河市"},
    {"item_code":"411200","item_name":"三门峡市"},
    {"item_code":"411300","item_name":"南阳市"},
    {"item_code":"411400","item_name":"商丘市"},
    {"item_code":"411500","item_name":"信阳市"},
    {"item_code":"411600","item_name":"周口市"},
    {"item_code":"411700","item_name":"驻马店市"},
    {"item_code":"420100","item_name":"武汉市"},
    {"item_code":"420200","item_name":"黄石市"},
    {"item_code":"420300","item_name":"十堰市"},
    {"item_code":"420500","item_name":"宜昌市"},
    {"item_code":"420600","item_name":"襄樊市"},
    {"item_code":"420700","item_name":"鄂州市"},
    {"item_code":"420800","item_name":"荆门市"},
    {"item_code":"420900","item_name":"孝感市"},
    {"item_code":"421000","item_name":"荆州市"},
    {"item_code":"421100","item_name":"黄冈市"},
    {"item_code":"421200","item_name":"咸宁市"},
    {"item_code":"421300","item_name":"随州市"},
    {"item_code":"422800","item_name":"恩施土家族苗族自治州"},
    {"item_code":"430100","item_name":"长沙市"},
    {"item_code":"430200","item_name":"株洲市"},
    {"item_code":"430300","item_name":"湘潭市"},
    {"item_code":"430400","item_name":"衡阳市"},
    {"item_code":"430500","item_name":"邵阳市"},
    {"item_code":"430600","item_name":"岳阳市"},
    {"item_code":"430700","item_name":"常德市"},
    {"item_code":"430800","item_name":"张家界市"},
    {"item_code":"430900","item_name":"益阳市"},
    {"item_code":"431000","item_name":"郴州市"},
    {"item_code":"431100","item_name":"永州市"},
    {"item_code":"431200","item_name":"怀化市"},
    {"item_code":"431300","item_name":"娄底市"},
    {"item_code":"433100","item_name":"湘西土家族苗族自治州"},
    {"item_code":"440100","item_name":"广州市"},
    {"item_code":"440200","item_name":"韶关市"},
    {"item_code":"440300","item_name":"深圳市"},
    {"item_code":"440400","item_name":"珠海市"},
    {"item_code":"440500","item_name":"汕头市"},
    {"item_code":"440600","item_name":"佛山市"},
    {"item_code":"440700","item_name":"江门市"},
    {"item_code":"440800","item_name":"湛江市"},
    {"item_code":"440900","item_name":"茂名市"},
    {"item_code":"441200","item_name":"肇庆市"},
    {"item_code":"441300","item_name":"惠州市"},
    {"item_code":"441400","item_name":"梅州市"},
    {"item_code":"441500","item_name":"汕尾市"},
    {"item_code":"441600","item_name":"河源市"},
    {"item_code":"441700","item_name":"阳江市"},
    {"item_code":"441800","item_name":"清远市"},
    {"item_code":"441900","item_name":"东莞市"},
    {"item_code":"442000","item_name":"中山市"},
    {"item_code":"445100","item_name":"潮州市"},
    {"item_code":"445200","item_name":"揭阳市"},
    {"item_code":"445300","item_name":"云浮市"},
    {"item_code":"450100","item_name":"南宁市"},
    {"item_code":"450200","item_name":"柳州市"},
    {"item_code":"450300","item_name":"桂林市"},
    {"item_code":"450400","item_name":"梧州市"},
    {"item_code":"450500","item_name":"北海市"},
    {"item_code":"450600","item_name":"防城港市"},
    {"item_code":"450700","item_name":"钦州市"},
    {"item_code":"450800","item_name":"贵港市"},
    {"item_code":"450900","item_name":"玉林市"},
    {"item_code":"451000","item_name":"百色市"},
    {"item_code":"451100","item_name":"贺州市"},
    {"item_code":"451200","item_name":"河池市"},
    {"item_code":"451300","item_name":"来宾市"},
    {"item_code":"451400","item_name":"崇左市"},
    {"item_code":"460100","item_name":"海口市"},
    {"item_code":"460200","item_name":"三亚市"},
    {"item_code":"500100","item_name":"重庆市"},
    {"item_code":"510100","item_name":"成都市"},
    {"item_code":"510300","item_name":"自贡市"},
    {"item_code":"510400","item_name":"攀枝花市"},
    {"item_code":"510500","item_name":"泸州市"},
    {"item_code":"510600","item_name":"德阳市"},
    {"item_code":"510700","item_name":"绵阳市"},
    {"item_code":"510800","item_name":"广元市"},
    {"item_code":"510900","item_name":"遂宁市"},
    {"item_code":"511000","item_name":"内江市"},
    {"item_code":"511100","item_name":"乐山市"},
    {"item_code":"511300","item_name":"南充市"},
    {"item_code":"511400","item_name":"眉山市"},
    {"item_code":"511500","item_name":"宜宾市"},
    {"item_code":"511600","item_name":"广安市"},
    {"item_code":"511700","item_name":"达州市"},
    {"item_code":"511800","item_name":"雅安市"},
    {"item_code":"511900","item_name":"巴中市"},
    {"item_code":"512000","item_name":"资阳市"},
    {"item_code":"513200","item_name":"阿坝藏族羌族自治州"},
    {"item_code":"513300","item_name":"甘孜藏族自治州"},
    {"item_code":"513400","item_name":"凉山彝族自治州"},
    {"item_code":"520100","item_name":"贵阳市"},
    {"item_code":"520200","item_name":"六盘水市"},
    {"item_code":"520300","item_name":"遵义市"},
    {"item_code":"520400","item_name":"安顺市"},
    {"item_code":"520500","item_name":"毕节市"},
    {"item_code":"520601","item_name":"铜仁市"},
    {"item_code":"522300","item_name":"黔西南布依族苗族自治州"},
    {"item_code":"522600","item_name":"黔东南苗族侗族自治州"},
    {"item_code":"522700","item_name":"黔南布依族苗族自治州"},
    {"item_code":"530100","item_name":"昆明市"},
    {"item_code":"530300","item_name":"曲靖市"},
    {"item_code":"530400","item_name":"玉溪市"},
    {"item_code":"530500","item_name":"保山市"},
    {"item_code":"530600","item_name":"昭通市"},
    {"item_code":"530700","item_name":"丽江市"},
    {"item_code":"530800","item_name":"普洱市"},
    {"item_code":"530900","item_name":"临沧市"},
    {"item_code":"532300","item_name":"楚雄彝族自治州"},
    {"item_code":"532500","item_name":"红河哈尼族彝族自治州"},
    {"item_code":"532600","item_name":"文山壮族苗族自治州"},
    {"item_code":"532800","item_name":"西双版纳傣族自治州"},
    {"item_code":"532900","item_name":"大理白族自治州"},
    {"item_code":"533100","item_name":"德宏傣族景颇族自治州"},
    {"item_code":"533300","item_name":"怒江傈僳族自治州"},
    {"item_code":"533400","item_name":"迪庆藏族自治州"},
    {"item_code":"540100","item_name":"拉萨市"},
    {"item_code":"542100","item_name":"昌都地区"},
    {"item_code":"542200","item_name":"山南地区"},
    {"item_code":"542300","item_name":"日喀则地区"},
    {"item_code":"542400","item_name":"那曲地区"},
    {"item_code":"542500","item_name":"阿里地区"},
    {"item_code":"542600","item_name":"林芝地区"},
    {"item_code":"610100","item_name":"西安市"},
    {"item_code":"610200","item_name":"铜川市"},
    {"item_code":"610300","item_name":"宝鸡市"},
    {"item_code":"610400","item_name":"咸阳市"},
    {"item_code":"610500","item_name":"渭南市"},
    {"item_code":"610600","item_name":"延安市"},
    {"item_code":"610700","item_name":"汉中市"},
    {"item_code":"610800","item_name":"榆林市"},
    {"item_code":"610900","item_name":"安康市"},
    {"item_code":"611000","item_name":"商洛市"},
    {"item_code":"620100","item_name":"兰州市"},
    {"item_code":"620200","item_name":"嘉峪关市"},
    {"item_code":"620300","item_name":"金昌市"},
    {"item_code":"620400","item_name":"白银市"},
    {"item_code":"620500","item_name":"天水市"},
    {"item_code":"620600","item_name":"武威市"},
    {"item_code":"620700","item_name":"张掖市"},
    {"item_code":"620800","item_name":"平凉市"},
    {"item_code":"620900","item_name":"酒泉市"},
    {"item_code":"621000","item_name":"庆阳市"},
    {"item_code":"621100","item_name":"定西市"},
    {"item_code":"621200","item_name":"陇南市"},
    {"item_code":"622900","item_name":"临夏回族自治州"},
    {"item_code":"623000","item_name":"甘南藏族自治州"},
    {"item_code":"630100","item_name":"西宁市"},
    {"item_code":"632100","item_name":"海东地区"},
    {"item_code":"632200","item_name":"海北藏族自治州"},
    {"item_code":"632300","item_name":"黄南藏族自治州"},
    {"item_code":"632500","item_name":"海南藏族自治州"},
    {"item_code":"632600","item_name":"果洛藏族自治州"},
    {"item_code":"632700","item_name":"玉树藏族自治州"},
    {"item_code":"632800","item_name":"海西蒙古族藏族自治州"},
    {"item_code":"640100","item_name":"银川市"},
    {"item_code":"640200","item_name":"石嘴山市"},
    {"item_code":"640300","item_name":"吴忠市"},
    {"item_code":"640400","item_name":"固原市"},
    {"item_code":"640500","item_name":"中卫市"},
    {"item_code":"650100","item_name":"乌鲁木齐市"},
    {"item_code":"650200","item_name":"克拉玛依市"},
    {"item_code":"652100","item_name":"吐鲁番地区"},
    {"item_code":"652200","item_name":"哈密地区"},
    {"item_code":"652300","item_name":"昌吉回族自治州"},
    {"item_code":"652700","item_name":"博尔塔拉蒙古自治州"},
    {"item_code":"652800","item_name":"巴音郭楞蒙古自治州"},
    {"item_code":"652900","item_name":"阿克苏地区"},
    {"item_code":"653000","item_name":"克孜勒苏柯尔克孜自治州"},
    {"item_code":"653100","item_name":"喀什地区"},
    {"item_code":"653200","item_name":"和田地区"},
    {"item_code":"654000","item_name":"伊犁哈萨克自治州"},
    {"item_code":"654200","item_name":"塔城地区"},
    {"item_code":"654300","item_name":"阿勒泰地区"},
]
$(function() {
    //load city.json

    var sb = new StringBuffer();
    $.each(cityJson,
        function(i, val) {
            if (val.item_code.substr(2, 4) == '0000') {
                sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
            }
        });
    $("#choosePro").after(sb.toString());

}); // 省值变化时 处理市
function doProvAndCityRelation() {
    var city = $("#citys");
    var county = $("#county");
    if (city.children().length > 1) {
        city.empty();
    }
    if (county.children().length > 1) {
        county.empty();
    }
    if ($("#chooseCity").length === 0) {
        city.append("<option id='chooseCity' value='-1'>请选择您所在城市</option>");
    }
    if ($("#chooseCounty").length === 0) {
        county.append("<option id='chooseCounty' value='-1'>请选择您所在区/县</option>");
    }
    var sb = new StringBuffer();
    $.each(cityJson,
        function(i, val) {
            if (val.item_code.substr(0, 2) == $("#province").val().substr(0, 2) && val.item_code.substr(2, 4) != '0000' && val.item_code.substr(4, 2) == '00') {
                sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
            }
        });
    $("#chooseCity").after(sb.toString());
} // 市值变化时 处理区/县
function doCityAndCountyRelation() {
    var cityVal = $("#citys").val();
    var county = $("#county");
    if (county.children().length > 1) {
        county.empty();
    }
    if ($("#chooseCounty").length === 0) {
        county.append("<option id='chooseCounty' value='-1'>请选择您所在区/县</option>");
    }
    var sb = new StringBuffer();
    $.each(cityJson,
        function(i, val) {
            if (cityVal == '110100' || cityVal == "120100" || cityVal == "310100" || cityVal == "500100") {
                if (val.item_code.substr(0, 3) == cityVal.substr(0, 3) && val.item_code.substr(4, 2) != '00') {
                    sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
                }
            } else {
                if (val.item_code.substr(0, 4) == cityVal.substr(0, 4) && val.item_code.substr(4, 2) != '00') {
                    sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
                }
            }
        });
    $("#chooseCounty").after(sb.toString());

}


// 省值变化时 处理市
function doProvAndCityRelations() {
    var city = $("#citysd");
    var county = $("#county");
    if (city.children().length > 1) {
        city.empty();
    }
    if (county.children().length > 1) {
        county.empty();
    }
    if ($("#chooseCitys").length === 0) {
        city.append("<option id='chooseCitys' value='-1'>请选择您所在城市</option>");
    }
    if ($("#chooseCounty").length === 0) {
        county.append("<option id='chooseCounty' value='-1'>请选择您所在区/县</option>");
    }
    var sb = new StringBuffer();
    $.each(cityJson,
        function(i, val) {
            if (val.item_code.substr(0, 2) == $("#provinces").val().substr(0, 2) && val.item_code.substr(2, 4) != '0000' && val.item_code.substr(4, 2) == '00') {
                sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
            }
        });
    $("#chooseCitys").after(sb.toString());
} // 市值变化时 处理区/县
function doCityAndCountyRelations() {
    var cityVal = $("#citysd").val();
    var county = $("#county");
    if (county.children().length > 1) {
        county.empty();
    }
    if ($("#chooseCounty").length === 0) {
        county.append("<option id='chooseCounty' value='-1'>请选择您所在区/县</option>");
    }
    var sb = new StringBuffer();
    $.each(cityJson,
        function(i, val) {
            if (cityVal == '110100' || cityVal == "120100" || cityVal == "310100" || cityVal == "500100") {
                if (val.item_code.substr(0, 3) == cityVal.substr(0, 3) && val.item_code.substr(4, 2) != '00') {
                    sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
                }
            } else {
                if (val.item_code.substr(0, 4) == cityVal.substr(0, 4) && val.item_code.substr(4, 2) != '00') {
                    sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
                }
            }
        });
    $("#chooseCounty").after(sb.toString());

}


function StringBuffer(str) {
    var arr = [];
    str = str || "";
    var size = 0; // 存放数组大小
    arr.push(str);
    // 追加字符串
    this.append = function(str1) {
        arr.push(str1);
        return this;
    };
    // 返回字符串
    this.toString = function() {
        return arr.join("");
    };
    // 清空
    this.clear = function(key) {
        size = 0;
        arr = [];
    };
    // 返回数组大小
    this.size = function() {
        return size;
    };
    // 返回数组
    this.toArray = function() {
        return buffer;
    };
    // 倒序返回字符串
    this.doReverse = function() {
        var str = buffer.join('');
        str = str.split('');
        return str.reverse().join('');
    };
}