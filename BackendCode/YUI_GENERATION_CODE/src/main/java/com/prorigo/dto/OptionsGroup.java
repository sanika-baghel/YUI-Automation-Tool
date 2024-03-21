package com.prorigo.dto;

import java.util.List;
import lombok.Data;

/**
 * Represents a group of options.
 */
@Data
public class OptionsGroup {
  private String heading; // Heading for the group of options
  private List<Option> options; // List of options within the group
}