package com.store.system.web.controller;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aliyuncs.vod.model.v20170321.*;
import com.coremedia.iso.IsoFile;
import com.google.common.collect.Maps;
import com.googlecode.mp4parser.MemoryDataSourceImpl;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.property.PropertyLoader;
import com.quakoo.baseFramework.secure.Base64Util;
import com.quakoo.baseFramework.secure.MD5Utils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.Mezzanine;
import com.store.system.bean.PlayInfo;
import com.store.system.bean.VideoClient;
import com.store.system.client.ResultClient;
import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.protocol.HTTP;
import org.jaudiotagger.audio.mp3.ByteArrayMP3AudioHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/storage")
public class StorageController extends BaseController implements InitializingBean {

    private int compress_no = 0;
    private int compress_jpg = 1;
    private int compress_gif = 2;
    private int compress_png = 3;

    private int handle_common = 0;
    private int handle_gif = 1;
    private int handle_mp3 = 2;
    private int handle_aac = 3;
    private int handle_jpg = 4;
    private int handle_jpeg = 5;
    private int handle_png = 6;

    private Map<String, String> response_content_type;
    private Map<String, Integer> compress_type;
    private Map<String, Integer> handle_type;

    @Override
    public void afterPropertiesSet() throws Exception {
        response_content_type = Maps.newHashMap();
        response_content_type.put(".mp3", "audio/mp3");
        response_content_type.put(".aac", "audio/aac");
        response_content_type.put(".m4a", "audio/m4a");
        response_content_type.put(".gif", "image/gif");
        response_content_type.put(".jpeg", "image/jpeg");
        response_content_type.put(".jpg", "image/jpg");
        response_content_type.put(".mp4", "video/mp4");
        response_content_type.put(".png", "image/png");

        compress_type = Maps.newHashMap();
        compress_type.put(".gif", compress_gif);
        compress_type.put(".jpeg", compress_jpg);
        compress_type.put(".jpg", compress_jpg);
        compress_type.put(".png", compress_png);

        handle_type = Maps.newHashMap();
        handle_type.put(".gif", handle_gif);
        handle_type.put(".mp3", handle_mp3);
        handle_type.put(".aac", handle_aac);
        handle_type.put(".m4a", handle_aac);
        handle_type.put(".jpg", handle_jpg);
        handle_type.put(".jpeg", handle_jpeg);
        handle_type.put(".png", handle_png);

    }

    public int getHandleType(String suffix) {
        int res = handle_common;
        for(Map.Entry<String, Integer> entry : handle_type.entrySet()) {
            String key = entry.getKey();
            if(suffix.toLowerCase().contains(key)) {
                res = entry.getValue().intValue();
                break;
            }
        }
        return res;
    }

    public int getCompressType(String fileName) {
        int res = compress_no;
        for(Map.Entry<String, Integer> entry : compress_type.entrySet()) {
            String key = entry.getKey();
            if(fileName.toLowerCase().contains(key)) {
                res = entry.getValue().intValue();
                break;
            }
        }
        return res;
    }

    public String getContentType(String fileName) {
        String res = null;
        for(Map.Entry<String, String> entry : response_content_type.entrySet()) {
            String key = entry.getKey();
            if(fileName.toLowerCase().contains(key)) {
                res = entry.getValue();
                break;
            }
        }
        if(res==null){return "application/octet-stream";}
        return res;
    }

    Logger logger = LoggerFactory.getLogger(StorageController.class);
	PropertyLoader loader = PropertyLoader.getInstance("storage.properties");

	String return_url_format = loader.getProperty("return_url_format","http://127.0.0.1:10004/storage/%s");
	String return_url_format_video = loader.getProperty("return_url_format_video","http://127.0.0.1:10004/video/storage/%s");

	String endpoint = loader.getProperty("endpoint","http://oss-cn-beijing.aliyuncs.com");
	String accessKeyId = loader.getProperty("accessKeyId","j7aHxtaMnUS4aZJG");
	String accessKeySecret = loader.getProperty("accessKeySecret","4mghcK0qx4zn5oDdNKUvRfyniCcDA2");
	String bucketName=loader.getProperty("bucketName","quakoo");

	//访问域名
	String ossDomain=loader.getProperty("ossDomain","http://oss.quakoo.com/");
    String ramRoleArn = loader.getProperty("ramRoleArn", "");
    String ramAccessKeyId = loader.getProperty("ramAccessKeyId");
    String ramAccessKeySecret = loader.getProperty("ramAccessKeySecret");

    @RequestMapping(value="/{fileName:.+}")
	public void file(@PathVariable(value="fileName") String fileName,
					 HttpServletRequest request, HttpServletResponse response) throws Exception {
        Date expiration = new Date(new Date().getTime() + 60 * 60 * 1000);
		byte[] data = null;
        OSSClient ossClient = null;
        InputStream in = null;
		try {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            boolean found = ossClient.doesObjectExist(bucketName, fileName);
            if(found){
                String suffix = null;
                if(fileName.contains(".")) {
                    suffix = fileName.substring(fileName.lastIndexOf("."));
                    int handleType = this.getHandleType(suffix);
                    if (handleType == handle_jpg || handleType == handle_jpeg ||
                            handleType == handle_png||handleType == handle_gif) {
//                        String url=ossDomain+fileName;
                        String url = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
                        response.setHeader("Access-Control-Allow-Origin", "*");
                        response.sendRedirect(url);
                        return;
                    }
                }
                OSSObject ossObject = ossClient.getObject(bucketName, fileName);
                in = ossObject.getObjectContent();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                int rc = 0;
                byte[] buf = new byte[1024];
                while ((rc = in.read(buf, 0,  buf.length)) > 0) {
                    swapStream.write(buf, 0, rc);
                }
                data=swapStream.toByteArray();
            }else{
                if(StringUtils.countMatches(fileName, "_") == 2) {
                    int compressType = this.getCompressType(fileName);
                    if(compressType == compress_no)
                        throw new IllegalStateException("this type can't compress!");
                    String prefix = fileName;
                    String suffix = null;
                    if(fileName.contains(".")) {
                        prefix = fileName.substring(0, fileName.lastIndexOf("."));
                        suffix = fileName.substring(fileName.lastIndexOf("."));
                    }
                    String orgFileName=prefix.substring(0,prefix.indexOf("_")) + suffix;

                    int width = Integer.parseInt(StringUtils.substringBetween(prefix, "_"));
                    int height = Integer.parseInt(StringUtils.substringAfterLast(prefix, "_"));

                    GeneratePresignedUrlRequest ossReq = new GeneratePresignedUrlRequest(bucketName, orgFileName);
                    ossReq.setExpiration(expiration);
                    ossReq.setMethod(HttpMethod.GET);

                    String resizeStr = "image/resize";
                    if(width>0){resizeStr += ",w_"+width;}
                    if(height>0){resizeStr += ",h_"+height;}
                    ossReq.setProcess(resizeStr);

                    String url = ossClient.generatePresignedUrl(ossReq).toString();
//                    String url=ossDomain+orgFileName+"?x-oss-process=image/resize";
//                    if(width>0){url=url+",w_"+width;}
//                    if(height>0){url=url+",h_"+height;}
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.sendRedirect(url);
                    return;
                }
            }

            if(null != data && data.length > 0){
                String contentType = this.getContentType(fileName);
                response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=2592000");
                response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + 1000*60*60*24*30l);
                response.setHeader("Access-Control-Allow-Origin", "*");
                if(StringUtils.isNotBlank(contentType))
                    response.addHeader("Content-Type", contentType);
                response.setHeader(HTTP.CONTENT_LEN, String.valueOf(data.length));
                OutputStream out = response.getOutputStream();
                out.write(data);
                out.flush();
            }else{
                response.setStatus(500);
            }
        } finally {
		    IOUtils.closeQuietly(in);
		    if(null != ossClient) ossClient.shutdown();
        }
	}



    @RequestMapping("/handle64")
	public ModelAndView handle64(
			@RequestParam(required = true, value = "file") String file,
			@RequestParam(required = true, value = "suffix") String suffix, 
			HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>(1);
		try {
			if (file.indexOf(";base64,") > 0) {
	            file = file.substring(file.indexOf(";base64,") + 8);
	        }
			logger.info("suffix : " + suffix);
			byte[] data = Base64Util.decode(file);
			suffix = "." + suffix;
			int handleType = this.getHandleType(suffix);
			if(handleType == handle_jpg || handleType == handle_jpeg ||
					handleType == handle_png) {
				String returnUrl = this.handleJpgPng(data, suffix);
				res.put("ok", returnUrl);
			} else if(handleType == handle_gif) {
				Map<String, String> map = this.handleGif(data);
				res.put("ok", map);
			} else if (handleType == handle_mp3) {
				Map<String, String> map = this.handleMp3(data);
				res.put("ok", map);
			} else if (handleType == handle_aac) {
				Map<String, String> map = this.handleAac(data);
				res.put("ok", map);
			} else {
				String returnUrl = this.handle(data, suffix);
				res.put("ok", returnUrl);
			}
		} catch (Exception e) {
			res.put("error", e.getMessage());
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,X_Requested_With");
	    return this.viewNegotiating(request, response, res);
	}

    @RequestMapping("/handle/richText")
    public void uploadimage(@RequestParam(value="upload") MultipartFile file,
                            HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        String returnRes = null;
        String callback = request.getParameter("CKEditorFuncNum");
        try {
            String suffix = null;
            String fileName = file.getOriginalFilename();
            if (fileName.contains(".")) {
                suffix = fileName.substring(fileName.lastIndexOf("."));
            }
            byte[] data = file.getBytes();
            int handleType = this.getHandleType(suffix);
            if (handleType == handle_jpg || handleType == handle_jpeg || handleType == handle_png) {
                returnRes = this.handleJpgPng(data, suffix);
            } else if (handleType == handle_gif) {
                Map<String, String> map = this.handleGif(data);
                returnRes = JsonUtils.toJson(map);
            } else if (handleType == handle_mp3) {
                Map<String, String> map = this.handleMp3(data);
                returnRes = JsonUtils.toJson(map);
            } else if (handleType == handle_aac) {
                Map<String, String> map = this.handleAac(data);
                returnRes = JsonUtils.toJson(map);
            } else {
                returnRes = this.handle(data, suffix);
            }
        } catch (Exception e) {
            response.setStatus(500);
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,X_Requested_With");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + returnRes + "',''" + ")");
        out.println("</script>");
        out.flush();
        out.close();
    }

    @RequestMapping("/handle")
    public ModelAndView handle(
    		@RequestParam(required = false, value = "file") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>(1);
		try {
			String suffix = null;
			String fileName = file.getOriginalFilename();
			if(fileName.contains(".")) {
				suffix = fileName.substring(fileName.lastIndexOf("."));
			}
			byte[] data = file.getBytes();
			int handleType = this.getHandleType(suffix);
			logger.info("fileName : "+fileName + " ,handleType : " + handleType);
			if(handleType == handle_jpg || handleType == handle_jpeg ||
					handleType == handle_png) {
				String returnUrl = this.handleJpgPng(data, suffix);
				res.put("ok", returnUrl);
			} else if(handleType == handle_gif) {
				Map<String, String> map = this.handleGif(data);
				res.put("ok", map);
			} else if (handleType == handle_mp3) {
				Map<String, String> map = this.handleMp3(data);
				res.put("ok", map);
			} else if (handleType == handle_aac) {
				Map<String, String> map = this.handleAac(data);
				res.put("ok", map);
			} else {
				String returnUrl = this.handle(data, suffix);
				res.put("ok", returnUrl);
			}
		} catch (Exception e) {
			res.put("error", e.getMessage());
		}
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,X_Requested_With");
		return this.viewNegotiating(request, response, res);
	}


	/**
	 * 上传视频
	 */
    @RequestMapping("/handle/video")
	public ModelAndView video(
            @RequestParam(required = true, value = "title") String title,
			@RequestParam(required = false, value = "file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>(1);
		try {
			String suffix = null;
			String fileName = file.getOriginalFilename();
			if(fileName.contains(".")) {
				suffix = fileName.substring(fileName.lastIndexOf("."));
			}
			byte[] data = file.getBytes();
			String md5 = MD5Utils.md5ReStr(data);
			String filePath = "/tmp/" + md5;
			if(StringUtils.isNotBlank(suffix))
				filePath += suffix;
			File localFile = new File(filePath);
			FileUtils.writeByteArrayToFile(localFile, data);
			String videoId=uploadVideo(accessKeyId, accessKeySecret, title, filePath);
			if(StringUtils.isBlank(videoId)){
				res.put("error", "上传失败");
			}else {
				String returnUrl = String.format(return_url_format_video, videoId+"."+suffix);
				res.put("ok", returnUrl);
			}
			logger.info("fileName : "+fileName + " ,handleType : video" );

		} catch (Exception e) {
			res.put("error", e.getMessage());
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,X_Requested_With");
		return this.viewNegotiating(request, response, res);
	}

	/**
	 * 播放视频
	 * :definition:清晰度
	 * 参考类：PlayInfo
	 */
    @RequestMapping(value="/video/{videoId:.+}")
	public void videoFile(
						  @PathVariable(value="videoId") String videoId,
						  @RequestParam(required = false, defaultValue = "LD") String definition,
					 HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("video/mp4");
        VideoClient videoClient=new VideoClient(accessKeyId,accessKeySecret,"");
        videoId = videoId.substring(0, videoId.lastIndexOf("."));
		List<PlayInfo> playInfos=videoClient.getPlayInfo(videoId);
		for (PlayInfo playInfo:playInfos){
			if(definition.equals(playInfo.getDefinition())){
				response.sendRedirect(playInfo.getPlayURL());
				return;
			}
		}
		if(playInfos.size()>0){
			response.sendRedirect(playInfos.get(0).getPlayURL());
			return;
		}
		response.setStatus(404);
	}

	//华为手机wifi播放问题
    @RequestMapping(value="/videoplayurl/{videoId:.+}")
    public ModelAndView videoplayurl(
            @PathVariable(value="videoId") String videoId,
            @RequestParam(required = false, defaultValue = "LD") String definition,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String playurl = "";
        VideoClient videoClient=new VideoClient(accessKeyId,accessKeySecret,"");
        videoId = videoId.substring(0, videoId.lastIndexOf("."));
        List<PlayInfo> playInfos=videoClient.getPlayInfo(videoId);
        for (PlayInfo playInfo:playInfos){
            if(definition.equals(playInfo.getDefinition())){
                playurl = playInfo.getPlayURL();
                break;
            }
        }
        if(playInfos.size()>0){
            playurl = playInfos.get(0).getPlayURL();
        }
        ResultClient resultClient = null;
        if(StringUtils.isNotBlank(playurl)) resultClient = new ResultClient(true, playurl);
        else resultClient = new ResultClient(false, "播放地址错误");
        return this.viewNegotiating(request, response, resultClient);
    }

    @RequestMapping("/createUploadVideo")
    public ModelAndView createUploadVideo(
            @RequestParam(required = true, value = "fileName") String fileName,
            @RequestParam(required = true, value = "name") String name,
            HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        DefaultAcsClient aliyunClient =  new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai",accessKeyId,accessKeySecret));

        return this.viewNegotiating(request, response,new ResultClient( _createUploadVideo(aliyunClient,fileName,name)));
    }

    @RequestMapping("/refreshUploadVideo")
    public ModelAndView refreshUploadVideo(
            @RequestParam(required = true, value = "videoId") String videoId,
            HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        DefaultAcsClient aliyunClient =  new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai",accessKeyId,accessKeySecret));
        return this.viewNegotiating(request, response, new ResultClient(_refreshUploadVideo(aliyunClient,videoId)));
    }

    @RequestMapping("/videoPlayAuth")
    public ModelAndView videoPlayAuth(
            @RequestParam(required = true, value = "videoId") String videoId,
            HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        DefaultAcsClient aliyunClient =  new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret));
        return this.viewNegotiating(request, response, new ResultClient(_videoPlayAuth(aliyunClient,videoId)));
    }

    @RequestMapping("/createSecurityToken")
    public ModelAndView createSecurityToken(
            @RequestParam(required = false, value = "session") String session,
            HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", ramAccessKeyId, ramAccessKeySecret);
        DefaultAcsClient aliyunClient = new DefaultAcsClient(profile);
        session = "user_" + session;
        return this.viewNegotiating(request, response, new ResultClient(_createSecurityToken(aliyunClient, session)));
    }

    @RequestMapping("/videoDownloadUrl")
    public ModelAndView videoDownloadUrl(@RequestParam(required = false, value = "videoId") String videoId,
                                         @RequestParam(required = false, value = "playUrl") String playUrl,
                                         HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        if(StringUtils.isBlank(videoId) && StringUtils.isNotBlank(playUrl)) {
            String content = StringUtils.substringAfterLast(playUrl, "/");
            videoId = StringUtils.substringBefore(content, ".");
        }
        String downUrl = "";
        if(StringUtils.isNotBlank(videoId)) {
            VideoClient videoClient=new VideoClient(accessKeyId, accessKeySecret,"");
            Mezzanine mezzanine = videoClient.getMezzanine(videoId, 60 * 60);
            downUrl = mezzanine.getFileURL();
        }
        return this.viewNegotiating(request, response, new ResultClient(true, downUrl));
    }


    private Map<String,String> _createSecurityToken(DefaultAcsClient client, String session) throws Exception {
        AssumeRoleRequest request = new AssumeRoleRequest();
        AssumeRoleResponse response = null;
        try {
            request.setMethod(MethodType.POST);
            request.setRoleArn(ramRoleArn);
            request.setRoleSessionName(session);
            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            logger.info("CreateSecurityToken Server Exception:");
            logger.error(e.getMessage(),e);
            return null;
        } catch (ClientException e) {
            logger.info("CreateSecurityToken Client Exception:");
            logger.error(e.getMessage(),e);
            return null;
        }
        Map<String,String> map = Maps.newHashMap();
        map.put("Expiration", response.getCredentials().getExpiration());
        map.put("AccessKeyId", response.getCredentials().getAccessKeyId());
        map.put("AccessKeySecret", response.getCredentials().getAccessKeySecret());
        map.put("SecurityToken", response.getCredentials().getSecurityToken());
        map.put("returnUrl", return_url_format_video);
        return map;
    }

    private Map<String,String> _videoPlayAuth(DefaultAcsClient client, String videoId) {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        GetVideoPlayAuthResponse response = null;
        try {
            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            throw new RuntimeException("GetVideoPlayAuthRequest Server failed");
        } catch (ClientException e) {
            throw new RuntimeException("GetVideoPlayAuthRequest Client failed");
        }
        logger.info("PlayAuth:" + response.getPlayAuth());
        Map<String,String> map = Maps.newHashMap();
        map.put("playAuth",response.getPlayAuth());
        return map;
    }

    private Map<String,String> _refreshUploadVideo(DefaultAcsClient client, String videoId) {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        RefreshUploadVideoResponse response = null;
        try {
            request.setVideoId(videoId);
            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            logger.info("RefreshUploadVideoRequest Server Exception:");
            logger.error(e.getMessage(),e);
            return null;
        } catch (ClientException e) {
            logger.info("RefreshUploadVideoRequest Client Exception:");
            logger.error(e.getMessage(),e);
            return null;
        }
        logger.info("RequestId:" + response.getRequestId());
        logger.info("UploadAuth:" + response.getUploadAuth());
        Map<String,String> map = Maps.newHashMap();
        map.put("uploadAuth",response.getUploadAuth());
        return map;
    }

    private Map<String,String> _createUploadVideo(DefaultAcsClient client, String fileName, String name) {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        CreateUploadVideoResponse response = null;
        try {
            request.setFileName(fileName);
            request.setTitle(fileName);
            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            logger.info("CreateUploadVideoRequest Server Exception:");
            logger.error(e.getMessage(),e);
            return null;
        } catch (ClientException e) {
            logger.info("CreateUploadVideoRequest Client Exception:");
            logger.error(e.getMessage(),e);
            return null;
        }
        Map<String,String> map = Maps.newHashMap();
        map.put("uploadAuth",response.getUploadAuth());
        map.put("uploadAddress",response.getUploadAddress());
        map.put("videoId",response.getVideoId());
        map.put("returnUrl", return_url_format_video);
        return map;
    }

    private static String uploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
		UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
		request.setPartSize(10 * 1024 * 1024L);     //可指定分片上传时每个分片的大小，默认为10M字节
		request.setTaskNum(1);   //可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）
		request.setIsShowWaterMark(true);           //是否使用默认水印
//		request.setCallback("http://callback.sample.com");  //设置上传完成后的回调URL(可选)
//		request.setCateId(0);                       //视频分类ID(可选)
//		request.setTags("标签1,标签2");              //视频标签,多个用逗号分隔(可选)
//		request.setDescription("视频描述");          //视频描述(可选)
//		request.setCoverURL("http://cover.sample.com/sample.jpg"); //封面图片(可选)
		UploadVideoImpl uploader = new UploadVideoImpl();
		UploadVideoResponse response = uploader.uploadVideo(request);
		System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
		if (response.isSuccess()) {
			System.out.print("VideoId=" + response.getVideoId() + "\n");
		} else {
			//如果设置回调URL无效，不影响视频上传，返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
			System.out.print("VideoId=" + response.getVideoId() + "\n");
			System.out.print("ErrorCode=" + response.getCode() + "\n");
			System.out.print("ErrorMessage=" + response.getMessage() + "\n");
		}
		return response.getVideoId();

	}

    private String handle(byte[] data, String suffix) throws Exception {
        OSSClient ossClient = null;
        try {
            String md5 = MD5Utils.md5ReStr(data);
            if(StringUtils.isNotBlank(suffix))
                md5 += suffix;
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(this.getContentType("."+suffix));
            ossClient.putObject(bucketName, md5, new ByteArrayInputStream(data),meta);
            ossClient.shutdown();
            String returnUrl = String.format(return_url_format, md5);
            return returnUrl;
        } finally {
            if(null != ossClient) ossClient.shutdown();
        }
    }


    private String handleJpgPng(byte[] data, String suffix) throws Exception {
        InputStream is = null;
        OSSClient ossClient = null;
        try {
            is = new ByteArrayInputStream(data);
            BufferedImage imageBuffer = ImageIO.read(is);
            int width = imageBuffer.getWidth();
            int height = imageBuffer.getHeight();
            String realMd5=MD5Utils.md5ReStr(data);
            String md5 = width + "*" + height + "*" + realMd5;
            if(StringUtils.isNotBlank(suffix))
                md5 += suffix;

            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(this.getContentType("."+suffix));
            ossClient.putObject(bucketName, md5, new ByteArrayInputStream(data) ,meta);

            String returnUrl = String.format(return_url_format, md5);
            return returnUrl;
        } finally {
            IOUtils.closeQuietly(is);
            if(null != ossClient) ossClient.shutdown();
        }
    }
	
	private byte[] getGifCoverToJpg(byte[] data){
		byte[] coverJpg = null;
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		try {
		    inputStream = new ByteArrayInputStream(data);
			ImageProducer image = Jimi.getImageProducer(inputStream);
			JimiWriter writer = Jimi.createJimiWriter("jpg");
			writer.setSource(image);
			outputStream = new ByteArrayOutputStream();
			writer.putImage(outputStream);
			coverJpg = outputStream.toByteArray();
		} catch (Exception e) {
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
		return coverJpg;
	}

    private Map<String, String> handleGif(byte[] data) throws Exception {
        Map<String, String> res = Maps.newHashMap();
        byte[] coverJpg = this.getGifCoverToJpg(data);
        if(null != coverJpg && coverJpg.length > 0) {
            String coverJpgReturnUrl = this.handle(coverJpg, ".jpg");
            res.put("jpg", coverJpgReturnUrl);
        }
        String returnUrl = this.handle(data, ".gif");
        res.put("gif", returnUrl);
        return res;
    }
	
    private Map<String, String> handleMp3(byte[] data) throws Exception {
        Map<String, String> res = Maps.newHashMap();
        ByteArrayMP3AudioHeader header = new ByteArrayMP3AudioHeader(data);
        long trackLength = header.getTrackLength();
        res.put("trackLength", String.valueOf(trackLength));
        String returnUrl = this.handle(data, ".mp3");
        res.put("url", returnUrl);
        return res;
    }
	
    private Map<String, String> handleAac(byte[] data) throws Exception {
        Map<String, String> res = Maps.newHashMap();
        IsoFile isoFile = null;
        try {
            isoFile = new IsoFile(new MemoryDataSourceImpl(data));
            double lengthInSeconds = (double)isoFile.getMovieBox().getMovieHeaderBox().getDuration()
                    / isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
            long trackLength = (long) lengthInSeconds;
            res.put("trackLength", String.valueOf(trackLength));
        } finally {
            if(null != isoFile) isoFile.close();
        }
        String returnUrl = this.handle(data, ".aac");
        res.put("url", returnUrl);
        return res;
    }
	
}
