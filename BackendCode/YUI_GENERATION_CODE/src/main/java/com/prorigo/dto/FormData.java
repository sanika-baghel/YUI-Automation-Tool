package com.prorigo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class FormData {

	private String type;
	private String id;
	private String text;
	@JsonProperty("class")
	@SerializedName("class")
	private String className;
	private String label;
	private String value;
	private boolean readOnly;
	// Use a wildcard to represent both OptionsGroup and Option
	private List<?> options = new ArrayList<>();
	private boolean mandatory;
	private String maxLen;
	private String templateName;
	private String tabName;
	private String width;
	private List<CollapseSection> collapse;
	private String cname; //Collapse Name
	private String cid;   //Collapse Id
	private List<RowHeader> addrowheaderkey ;
	private List<FormData> addrowkey ;


}


