package com.store.system.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LiveRecordVideoList {

	@JsonProperty("LiveRecordVideo")  
	private List<LiveRecordVideo> liveRecordVideo;

	public List<LiveRecordVideo> getLiveRecordVideo() {
		return liveRecordVideo;
	}

	public void setLiveRecordVideo(List<LiveRecordVideo> liveRecordVideo) {
		this.liveRecordVideo = liveRecordVideo;
	}

	

	
}
