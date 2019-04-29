package com.store.system.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LiveRecordVideoList {

	@JsonProperty("LiveRecordVideo")  
	private List<LiveRecordVideo> liveRecordVideo;

}
