package com.prorigo.dto;

import java.util.List;
import lombok.Data;

@Data
public class CollapseSection {
  private String cname; //Collapse Name
  private String cid;   //Collapse Id
  private List<FormData> fields;
}
