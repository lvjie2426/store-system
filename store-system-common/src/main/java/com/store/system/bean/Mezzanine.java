package com.store.system.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Mezzanine {

    @JsonProperty("FileName")
    private String fileName;

    @JsonProperty("Duration")
    private String duration;

    @JsonProperty("CreationTime")
    private String creationTime;

    @JsonProperty("FileURL")
    private String fileURL;

}
