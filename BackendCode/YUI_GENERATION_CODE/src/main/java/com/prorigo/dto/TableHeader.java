package com.prorigo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableHeader {

  private String dataRef;
  private String width;
  private String classValue;
  private String label;

}
