
//刷新所有地区
function updataData(){
  var url = "/area/getAllList";
  myAjax(url,"",handle);
  function handle(data,param){
    localStorage.setItem("area",JSON.stringify(data));
  }
}

function initAllAreaData(){
  var totalArea = localStorage.getItem("area");
  var AreaData,provinces = [],levelTwo = {},totalList = {};
  if(isNotBlank(totalArea)){
    AreaData = JSON.parse(totalArea);
  }else{
    updataData();
  }

  for(var i in AreaData){
    var nowValue = AreaData[i];
    totalList[nowValue.code] = nowValue;
    if(nowValue.level == 1){
      provinces.push(nowValue.code);
    }
    if(nowValue.level == 2){
      var code =nowValue.code.substring(0,2)+"0";
      var index = $.inArray(code,provinces);
      if(index >= 0){
        var levelTwoArray = levelTwo[provinces[index]];
        if(isBlank(levelTwoArray)){
          levelTwo[provinces[index]] = new Array(nowValue.code);
        }else{
          levelTwo[provinces[index]].push(nowValue.code);
        }
      }
    }
    if(nowValue.level == 3){
      var code = nowValue.code.substring(0,6);
      var index = levelTwo[code];
      if(isBlank(index)){
        levelTwo[code] = new Array(nowValue.code);
      }else{
        levelTwo[code].push(nowValue.code);
      }
    }
  }
  __LocalDataCities.list = totalList;
  __LocalDataCities.relations = levelTwo;
  __LocalDataCities.category = {
    "provinces":provinces
  }

}


//console.log(totalList);
//console.log(levelTwo);
//console.log(provinces);

var __LocalDataCities = {};
initAllAreaData();
