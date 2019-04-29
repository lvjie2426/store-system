package com.store.system.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LiveRecordVideo implements java.io.Serializable {
//    StreamName	String	直播流名称
//    DomainName	String	域名
//    AppName	String	App名称
//    PlaylistId	String	播单ID
//    RecordStartTime	String	录制开始时间
//    RecordEndTime	String	录制结束时间
//    Video	Video	视频信息


	@JsonProperty("StreamName")  
    private String  streamName;//		直播流名称
	@JsonProperty("DomainName")  
    private String    domainName;//	String	域名
	@JsonProperty("AppName")  
    private String    appName;//	String	App名称
	@JsonProperty("PlaylistId")  
    private String    playlistId;//	String	播单ID
	@JsonProperty("RecordStartTime")  
    private String    recordStartTime;//	String	录制开始时间
	@JsonProperty("RecordEndTime")  
    private String   recordEndTime;//	String	录制结束时间
	@JsonProperty("Video")  
    private Video video;//	Video	视频信息

}
