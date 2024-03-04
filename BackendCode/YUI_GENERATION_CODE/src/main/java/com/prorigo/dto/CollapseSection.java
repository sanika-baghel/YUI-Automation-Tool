package com.prorigo.dto;

import java.util.List;
import lombok.Data;

@Data
public class CollapseSection {
  private String collapseName;
  private String collapseId;
  private List<FormData> fields;
}
