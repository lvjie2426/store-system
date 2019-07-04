var areaJson = function (level) {

    this.oneList = {};

    this.towList = {};

    this.threeList = {};

    this.level = level || 3;

    var self = this;

    this.init = function () {
        myAjaxes("../resource/js/area/area_first.json", function (ret) {

            self.oneList = ret;
        });

        if (self.level == 2) {
            myAjaxes("../resource/js/area/area_second.json", function (ret) {

                self.towList = ret;
            });
        }

        if (self.level == 3) {
            myAjaxes("../resource/js/area/area_three.json", function (ret) {

                for(var index in ret.levelRelation){
                    ret.relation[index] = ret.levelRelation[index];
                }

                for(var index in ret.levelSubList){

                    ret.subList[index] = ret.levelSubList[index];
                }
                self.threeList = ret;

            });

            myAjaxes("../resource/js/area/area_second.json", function (ret) {


                for(var index in ret.levelRelation){
                    ret.relation[index] = ret.levelRelation[index];
                }

                for(var index in ret.levelSubList){

                    ret.subList[index] = ret.levelSubList[index];
                }

                self.towList = ret;
            });
        }


    }

    this.init();
};


areaJson.prototype = {

    //获取一级省份列表
    getOneLevel: function () {

        var listJson = this.oneList;
        var list = [];
        for (var index in  listJson) {
            list.push(listJson[index]);
        }

        return list;
    },
    //获取二级所有的列表
    getTowLevel: function () {

        var listJson = this.towList.subList;
        var list = [];
        for (var index in  listJson) {
            list.push(listJson[index]);
        }

        return list;
    },
    //获取三级所有的列表
    getThreeLevel: function () {

        var listJson = this.threeList.subList;
        var list = [];
        for (var index in  listJson) {
            list.push(listJson[index]);
        }

        return list;
    },
    //通过父级的id获取子集列表
    getSubList: function (parentId) {

        var area = this.oneList[parentId];
        var subJson = this.towList.subList;
        var towRelation = this.towList.relation;
        if (!area) {
            area = subJson[parentId];

            if (area) {
                var threeJson = this.threeList.subList;
                var threeRelation = this.threeList.relation;

                var listId = threeRelation[parentId];

                var list = [];

                for (var i = 0; i < listId.length; i++) {

                    list.push(threeJson[listId[i]]);
                }

                return list;
            }
        } else {

            var listId = towRelation[parentId];

            var list = [];

            for (var i = 0; i < listId.length; i++) {

                list.push(subJson[listId[i]]);
            }

            return list;

        }


        return new Array();

    },

    //通过id获取地区的详情
    getAreaByAid: function (aid) {
        var area = this.oneList[aid];
        if (!area) {
            var subJson = this.towList.subList;
            area = subJson[aid];
            if (!area) {
                var threeJson = this.threeList.subList;
                area = threeJson[aid];
                if (area) {
                    return area;
                }
            }
        }

        return area;
    }
};


function myAjaxes(url, func) {
    $.ajaxSettings.async = false;
    $.getJSON(url, function (data) {

        if (data) {
            //layer.close(index);
            if (typeof func == 'function') {
                func(data);
            }
        }

    });
}

