package com.prorigo.util;

import com.prorigo.dto.FormData;
import java.util.List;
import java.util.Map;

public class TemplateCreateUtil {
  public static String generateComponentHTML(FormData element) {
    switch (element.getType()) {
      case "TEXTBOX":
      case "CHECKBOX":
      case "RADIO":
      case "LOOKUP":
      case "BARCODE":
      case "LOOKUPANDBARCODE":
      case "CALENDAR":
        return generateInputFieldHTML(element);
      case "DROPDOWN":
        return generateDropdownHTML(element);
      case "BUTTON":
        return generateButtonHTML(element);
      case "TEXTAREA":
        return generateTextareaHTML(element);
      default:
        return ""; // Handle unsupported types
    }
  }

  private static String generateInputFieldHTML(FormData element) {
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";
    String typeName = (element.getType().equalsIgnoreCase("TEXTBOX") || element.getType().equalsIgnoreCase("LOOKUPANDBARCODE")
        || element.getType().equalsIgnoreCase("CALENDAR")) ? "text" : element.getType().toLowerCase();
    StringBuilder inputHTML = new StringBuilder();
    inputHTML.append("\t    <input type=\"").append(typeName).append("\" id=\"")
             .append(element.getId())
             .append("\" name=\"").append(element.getId()).append("\" class=\"")
             .append(element.getClassName()).append("\" value=\"")
             .append(value).append("\"")
             .append(element.isMandatory() ? " data-mize_required" : "")
             .append(element.isReadOnly() ? " disabled" : "")
             .append(" />\n");
    // Add button HTML if applicable
    if (element.getType().equalsIgnoreCase("LOOKUP") || element.getType().equalsIgnoreCase("LOOKUPANDBARCODE")) {
      inputHTML.append("\t    <span class=\"\">\n")
               .append("\t      <button type=\"button\" class=\"\" tabindex=\"-1\">\n")
               .append("\t        <i class=\"icon-search\"></i>\n")
               .append("\t      </button>\n")
               .append("\t    </span>\n");
    }
    if (element.getType().equalsIgnoreCase("LOOKUPANDBARCODE") || element.getType().equalsIgnoreCase("BARCODE")) {
      inputHTML.append("\t    <span class=\"\">\n")
               .append("\t      <button type=\"button\" class=\"\" tabindex=\"-1\">\n")
               .append("\t        <i class=\"icon-barcode big-font\"></i>\n")
               .append("\t      </button>\n")
               .append("\t    </span>\n");
    }
    if (element.getType().equalsIgnoreCase("CALENDAR")) {
      inputHTML.append("\t    <span class=\"\">\n")
               .append("\t      <button type=\"button\" class=\"\" tabindex=\"-1\">\n")
               .append("\t        <i class=\"icon-calendar\"></i>\n")
               .append("\t      </button>\n")
               .append("\t    </span>\n")
               .append("\t  </div>\n");
    }
    return inputHTML.toString();
  }


  private static String generateDropdownHTML(FormData element) {
    StringBuilder dropdownHTML = new StringBuilder();
    dropdownHTML.append("\t    <select id=\"").append(element.getId()).append("\" name=\"")
                .append(element.getId()).append("\" class=\"").append(element.getClassName())
                .append("\"")
                .append(element.isMandatory() ? " data-mize_required" : "")
                .append(element.isReadOnly() ? " disabled" : "").append(">\n");
    for (Map<String, Object> optionsGroupMap : (List<Map<String, Object>>) element.getOptions()) {
      String heading = (String) optionsGroupMap.get("heading");
      List<Map<String, String>> optionsList = (List<Map<String, String>>) optionsGroupMap.get("options");
      dropdownHTML.append("\t     <optgroup label=\"").append(heading).append("\">\n");
      for (Map<String, String> optionMap : optionsList) {
        String text = optionMap.get("text");
        dropdownHTML.append("\t       <option>").append(text).append("</option>\n");
      }
      dropdownHTML.append("\t     </optgroup>\n");
    }
    dropdownHTML.append("\t    </select>\n");
    return dropdownHTML.toString();
  }

  private static String generateButtonHTML(FormData element) {
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";
    return "\t    <button type=\"button\" id=\"" + element.getId() + "\" name=\"" + element.getId()
        + "\" class=\"" + element.getClassName() + "\"" + (element.isMandatory() ? " data-mize_required" : "")
        + (element.isReadOnly() ? " disabled" : "") + " >" + value + "</button>\n";
  }

  private static String generateTextareaHTML(FormData element) {
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";
    return "\t    <textarea id=\"" + element.getId() + "\" name=\"" + element.getId() + "\" class=\"" +
        element.getClassName() + "\"" + (element.isMandatory() ? " data-mize_required" : "") +
        (element.isReadOnly() ? " disabled" : "") + " >" + value + "</textarea>\n";
  }
}