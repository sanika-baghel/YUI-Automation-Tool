package com.prorigo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Represents form data.
 */
@Data
public class FormData {
	private String type; // Type of the form data
	private String id; // ID of the form data
	private String text; // Text of the form data
	@JsonProperty("class")
	@SerializedName("class")
	private String className; // CSS class of the form data
	private String label; // Label of the form data
	private String value; // Value of the form data
	private boolean readOnly; // Whether the form data is read-only
	private List<?> options = new ArrayList<>(); // Options associated with the form data
	private boolean mandatory; // Whether the form data is mandatory
	private String maxLen; // Maximum length of the form data
	private String templateName; // Name of the template associated with the form data
	private String tabName; // Name of the tab associated with the form data
	private List<CollapseSection> collapse; // List of collapse sections associated with the form data
	private String cname; // Collapse Name
	private String cid; // Collapse Id
	private List<FormData> addrowkey; // List of additional row keys associated with the form data
	private List<RowHeader> addrowheaderkey; // List of additional row headers associated with the form data
}
