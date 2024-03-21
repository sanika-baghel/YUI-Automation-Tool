package com.prorigo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a table header.
 */
@Data
@AllArgsConstructor
public class TableHeader {
  private String dataRef; // Reference data for the header
  private String width; // Width of the header
  private String classValue; // CSS class value for styling
  private String label; // Label of the header
}
