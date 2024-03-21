package com.prorigo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a row header.
 */
@Data
@AllArgsConstructor
public class RowHeader {
  private String data_ref; // Reference data for the header
  private String width; // Width of the header
  @JsonProperty("class")
  @SerializedName("class")
  private String className; // CSS class name for styling
  private String label; // Label of the header
}
