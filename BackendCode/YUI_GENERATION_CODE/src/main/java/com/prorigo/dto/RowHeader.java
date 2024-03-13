package com.prorigo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RowHeader {

  private String data_ref;
  private String  width;
  @JsonProperty("class")
  @SerializedName("class")
  private String  className;
  private String  label;


}
