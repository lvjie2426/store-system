Dropzone.autoDiscover = false;
function myDropzone(){
	var myDropzone = new Dropzone("#dropz", {
		url: serverUrl + '/storage/handle',//文件提交地址
		method:"post",  //也可用put
		paramName:"file", //默认为file
		maxFiles:3,//一次性上传的文件数量上限
		maxFilesize: 2, //文件大小，单位：MB
		acceptedFiles: ".jpg,.gif,.png,.jpeg", //上传的类型
		addRemoveLinks:true,
		parallelUploads: 1,//一次上传的文件数量
		//previewsContainer:"#preview",//上传图片的预览窗口
		dictDefaultMessage:'拖动文件至此或者点击上传',
		dictMaxFilesExceeded: "您最多只能上传{{maxFiles}}个文件！",
		dictResponseError: '文件上传失败!',
		dictInvalidFileType: "文件类型只能是*.jpg,*.gif,*.png,*.jpeg。",
		dictFallbackMessage:"浏览器不受支持",
		dictFileTooBig:"文件过大上传文件最大支持.",
		dictRemoveLinks: "删除",
		dictCancelUpload: "取消",
		dictRemoveFile:"删除",
		init:function(){
			this.on("success",function(file,data){
				if(data){
					var result = JSON.parse(data);
					if(result.ok != null){
						file.url = result.ok;
					}
				}
			});
			this.on("error",function (file,data) {
			});
			this.on("removedfile",function(file){
				if(file.status == "success"){
					var files = this.files;
					var sunccess = [];
					for(f in files){
						if(files[f].status == "success"){
							sunccess.push(files[f]);
						}else{
							files[f].previewElement.parentNode.removeChild(files[f].previewElement);
						}
					}
					this.files = sunccess;
				}
			});

		}
	});
	myDropzone.filelist = function(){
		var files = this.files;
		var arr = [];
		for(x in files){
			if(files[x].url)
				arr.push(files[x].url);
		}
		return arr;
	}
	myDropzone.initimage = function(){
		this.emit("initimage", ["http://store.tjcampus.com/storage/fangpai/450*300*bd67de78fed04dbb422b0741bffd6800.jpg"]
		);
	}
	return myDropzone;
}


