package com.prorigo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RowHeader {

  private String data_ref;
  private String  width;
  @JsonProperty("class")
  @SerializedName("class")
  private String  className;
  private String  label;


}
