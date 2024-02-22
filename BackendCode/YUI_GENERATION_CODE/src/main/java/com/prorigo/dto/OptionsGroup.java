package com.prorigo.dto;

import java.util.List;
import lombok.Data;

@Data
public class OptionsGroup {

  private String heading;
  private List<Option> options;
}
