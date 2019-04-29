package com.store.system.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlayInfoList {

	@JsonProperty("PlayInfo")  
	private List<PlayInfo> playInfo;

}
