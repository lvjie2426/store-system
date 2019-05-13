var fileName;
var videoUrl;
var playDom, valDom, viewDomPub, videoId, uploadAuth, uploadAddress,realPath,index,showDurationDom;

function include_js(path) {
    var sobj = document.createElement('script');
    sobj.type = "text/javascript";
    sobj.src = path;
    var headobj = document.getElementsByTagName('head')[0];
    headobj.appendChild(sobj);
}

include_js('../resource/js/plugins/aliyun/es6-promise.min.js');
include_js('../resource/js/plugins/aliyun/aliyun-upload-sdk1.3.1.min.js');
include_js('../resource/js/plugins/aliyun/aliyun-oss-sdk4.13.2.min.js');

var videoHtml = '<div  class="wrapper wrapper-content animated fadeInRight showVideoView" style="display: none">\n' +
    '    <div class="ibox float-e-margins">\n' +
    '        <div class="ibox-content">\n' +
    '            <div class="row row-lg">\n' +
    '                <div class="col-sm-12">\n' +
    '                    <video id="video" controls src="" >\n' +
    '                    </video>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '    </div>\n' +
    '</div>';

var showProgress = '<div  class="wrapper wrapper-content animated fadeInRight showProgress" style="display: none">\n' +
    '    <div class="ibox float-e-margins">\n' +
    '        <div class="ibox-content" >\n' +
    '        <form method="get" class="form-horizontal" id="showProgress"></form>'+
    '        </div>\n' +
    '    </div>\n' +
    '</div>';


var formHtml = '  <form style="opacity: 0" action="" id="uploadAliyun" autocomplete="off" enctype="multipart/form-data">\n' +
    '        <input style="height: 0;width: 0;"  accept="video/*" type="file" name="file"    />\n' +
    '    </form>';
$("body").append(formHtml);

$("body").append(videoHtml);
$("body").append(showProgress);

var session;

//上传文件的input 点击上传按钮
//viewDom 预览播放的地址
function initUploadForm(clickDom,progressDom,  viewDom,  showVal,showDuration) {
    playDom = progressDom;
    valDom = showVal;
    viewDomPub = progressDom;
    showDurationDom = showDuration;
    var inputDom = $("#uploadAliyun input");
    $(clickDom).click(function () {
        $(inputDom).click();
    });

    $(viewDom).click(function () {
        showVideoView()
    });
    $(inputDom).change(function () {
        var self = this;
        if(showDuration){
            setFileInfo(self);
        }
        _starUploadFile(inputDom)
    });

}



function setFileInfo(self) {
    var files = self.files;
    var video = document.createElement('video');
    video.preload = 'metadata';

    video.onloadedmetadata = function() {
        window.URL.revokeObjectURL(video.src);
        var duration = video.duration;
        $(showDurationDom).val(duration);
    };

    video.src = URL.createObjectURL(files[0]);
}

var uploader;
setTimeout(function () {
    uploader = new AliyunUpload.Vod({
        'onUploadstarted': function (uploadInfo) {
            ajaxProtocol(serverUrl + "/storage/createSecurityToken", {
                "session": session,
            }, function (data) {
                if (data && data.success && data.data) {
                    expiration = data.data.Expiration;
                    accessKeyId = data.data.AccessKeyId;
                    accessKeySecret = data.data.AccessKeySecret;
                    secretToken = data.data.SecurityToken;
                    videoUrl = data.data.returnUrl;
                    var showProgress =
                        '                   <div class="form-group">' +
                        '                        <div  class="col-sm-8 col-sm-offset-2">' +
                        '                            <div class="progress progress-striped active">' +
                        '                                <div class="progress-bar progress-bar-success" role="progressbar"' +
                        '                                     aria-valuenow="60" aria-valuemin="0" id="progressDiv" aria-valuemax="100"' +
                        '                                     style="width: 0%;">' +
                        '                                    <span class="sr-only" id="progressSpan">0% 完成</span>' +
                        '                                </div>' +
                        '                            </div>' +
                        '                        </div>' +
                        '                    </div>';
                    // $(viewDomPub).append(showProgress);
                    showUploadProgress();
                    uploader.setSTSToken(uploadInfo, accessKeyId, accessKeySecret,secretToken);
                }
            })
        },
        // 文件上传成功
        'onUploadSucceed': function (uploadInfo) {
            layer.msg('上传成功！', {
                icon: 6
            });
            var real = videoUrl + uploadInfo.videoId + "." + fileName.split(".")[1];
            $(valDom).val(real);
            realPath = real;
            $("#showProgressDom").remove();
            $(valDom).val(realPath);
        },
        // 文件上传失败
        'onUploadFailed': function (uploadInfo, code, message) {
            $("#showProgressDom").remove();
            layer.msg('上传失败！', {
                icon: 5
            });
        },
        // 文件上传进度，单位：字节
        'onUploadProgress': function (uploadInfo, totalSize, loadedPercent) {
            var msg =  (loadedPercent * 100.00).toFixed(2) + '%';
            $("#progressDiv").css("width",msg);
            $("#progressSpan").html(msg+" 完成");
        },
        // 上传凭证超时
        'onUploadTokenExpired': function (uploadInfo) {
            ajaxProtocol(serverUrl + "/storage/createSecurityToken", {
                "session": session,
            }, function (data) {
                if (data && data.success && data.data) {
                    expiration = data.data.Expiration;
                    accessKeyId = data.data.AccessKeyId;
                    accessKeySecret = data.data.AccessKeySecret;
                    secretToken = data.data.SecurityToken;

                    uploader.resumeUploadWithSTSToken(accessKeyId, accessKeySecret, secretToken, expiration);
                }
            })
        },
        //全部文件上传结束
        'onUploadEnd':function(uploadInfo){
        }
    });
}, 2000);

function starUploadFile() {
    _starUploadFile($("#uploadAliyun input"))
}

function _starUploadFile(inputDom) {
    var fp = $(inputDom);
    var lg = fp[0].files.length; // get length
    var items = fp[0].files;
    for (var i = 0; i < lg; i++) {
        fileName = items[i].name; // get file name
    }
    session = new Date().getTime();
    var userData = '{"Vod":{"Title":"' + fileName + '"}}';
    if (fileName) {
        uploader.addFile(items[0], null, null, null, userData);
        uploader.startUpload();
    }
}


//上传文件
function ajaxProtocol(url, param, callback) {
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        data: param,
        success: function (data) {
            if (data && data.success) {
                if (typeof callback == 'function') {
                    callback(data, param);
                }
            } else {
                layer.msg("<em style='color:red'>" + data.msg + "</em>", {time: 1200, icon: 5});
            }
        },
        error: function () {
            layer.msg("请求出错", {
                icon: 2
            });
        }
    });
}

function showVideoView() {
    if(!realPath){
        layer.msg("你还没有上传视频", {
            icon: 2
        });
    }
    var index = layer.open({
        type: 1,shade: 0.01,
        skin: 'layui-layer-demo', //样式类名
        area: ['500px','auto'],
        anim: 2,
        title:"视频预览",
        shadeClose: true, //开启遮罩关闭
        content: $('.showVideoView'),
        zIndex:9999,
        btn2: function(){},
        success: function(layero, index){
            $(".showVideoView video").attr("src",realPath);
        }
    });
}


function showUploadProgress() {

    var showProgress =
        '                            <div id="showProgressDom" class="progress progress-striped active">' +
        '                                <div class="progress-bar progress-bar-success" role="progressbar"' +
        '                                     aria-valuenow="60" aria-valuemin="0" id="progressDiv" aria-valuemax="100"' +
        '                                     style="width: 0%;">' +
        '                                    <span class="sr-only" id="progressSpan">0% 完成</span>' +
        '                                </div>' +
        '                            </div>' ;

    $(valDom).parent().append(showProgress);

}

