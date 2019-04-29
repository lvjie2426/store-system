package com.store.system.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Result {

    private int Code;
	
	private String Message;
	@JsonProperty("RequestId")  
	private String RequestId;

	@JsonProperty("LiveRecordVideoList")    
	private LiveRecordVideoList LiveRecordVideoList;

	@JsonProperty("PlayInfoList")
	private PlayInfoList PlayInfoList;

	@JsonProperty("Mezzanine")
	private Mezzanine mezzanine;

	
}
