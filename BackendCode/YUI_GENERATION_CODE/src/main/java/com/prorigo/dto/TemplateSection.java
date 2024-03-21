package com.prorigo.dto;

import java.util.List;
import lombok.Data;

/**
 * Represents a section of a template.
 */
@Data
public class TemplateSection {
  private String tabName; //Tab Name
  private String templateName;   //Template Name
  private List<FormData> fields;
}
