package com.prorigo.util;

import com.prorigo.dto.FormData;
import java.util.List;
import java.util.Map;

public class TemplateCreateUtil {

  /**
   * Generates HTML template for a form component based on the provided form element data.
   * @param element The FormData object representing the form component.
   * @return The HTML representation of the form component.
   */
  public static String generateComponentHTML(FormData element) {
    // Switch based on the type of the form element
    switch (element.getType()) {
      // For supported input types, generate corresponding HTML template
      case "TEXTBOX":
      case "CHECKBOX":
      case "RADIO":
      case "LOOKUP":
      case "BARCODE":
      case "LOOKUPANDBARCODE":
      case "CALENDAR":
        return generateInputFieldTemplate(element); // Generate input field HTML template
      case "DROPDOWN":
        return generateDropdownTemplate(element); // Generate dropdown HTML template
      case "BUTTON":
        return generateButtonTemplate(element); // Generate button HTML template
      case "TEXTAREA":
        return generateTextareaTemplate(element); // Generate textarea HTML template
      default:
        return ""; // Handle unsupported types
    }
  }


  /**
   * Generates HTML input field based on the provided form element data. Handles various input types
   * such as Textbox, Checkbox, Radio, Lookup, Barcode, and Calendar.
   *
   * @param element The FormData object representing the form element.
   * @return The HTML representation of the input field.
   */
  private static String generateInputFieldTemplate(FormData element) {
    // Get the value and type of the element
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";
    String typeName = (element.getType().equalsIgnoreCase("TEXTBOX") || element.getType()
                                                                               .equalsIgnoreCase(
                                                                                   "LOOKUPANDBARCODE")
        || element.getType().equalsIgnoreCase("CALENDAR") || element.getType()
                                                                    .equalsIgnoreCase("LOOKUP")
        || element.getType().equalsIgnoreCase("BARCODE")) ? "text"
        : element.getType().toLowerCase();

    String maxLength = element.getMaxLen();

    StringBuilder inputHTML = new StringBuilder();

    // Check if the element type requires additional HTML structure
    if (element.getType().equalsIgnoreCase("LOOKUP") || element.getType()
                                                               .equalsIgnoreCase("LOOKUPANDBARCODE")
        || element.getType().equalsIgnoreCase("CALENDAR") || element.getType()
                                                                    .equalsIgnoreCase("BARCODE")) {
      inputHTML.append("\t <div class=\"input-group yui3-skin-sam gs_lookup_field beCodeCls\">\n");
    }

    // Construct the input element
    inputHTML.append("\t    <input type=\"").append(typeName).append("\" id=\"")
             .append(element.getId())
             .append("\" name=\"").append(element.getId()).append("\" class=\"")
             .append(element.getClassName()).append("\" value=\"")
             .append(value).append("\"");

    // Add maxlength attribute if specified
    if (maxLength != null && !maxLength.equalsIgnoreCase("")) {
      inputHTML.append(" maxlength=\"").append(maxLength).append("\"");
    }

    // Add additional attributes based on element properties
    inputHTML.append(element.isMandatory() ? " data-mize_required" : "")
             .append(element.isReadOnly() ? " disabled" : "")
             .append(" />\n");

// Add additional HTML elements based on element type
    if (element.getType().equalsIgnoreCase("LOOKUP") || element.getType().equalsIgnoreCase(
        "LOOKUPANDBARCODE")) {
      inputHTML.append("\t    <span class=\"input-group-btn gs_lookup_businessentity\">\n")
               .append("\t      <button type=\"button\" class=\"btn btn-default\" tabindex=\"-1\">")
               .append("<i class=\"icon-search\" readonly></i>")
               .append("</button>\n")
               .append("\t    </span>\n");
    }

    if (element.getType().equalsIgnoreCase("LOOKUPANDBARCODE") || element.getType()
                                                                         .equalsIgnoreCase(
                                                                             "BARCODE")) {
      inputHTML.append("\t    <span class=\"input-group-btn\">\n")
               .append("\t      <button type=\"button\" class=\"btn btn-default\" tabindex=\"-1\">")
               .append(" <i class=\"icon-barcode big-font\"></i>")
               .append("</button>\n")
               .append("\t    </span>\n");
    }
    if (element.getType().equalsIgnoreCase("CALENDAR")) {
      inputHTML.append("\t    <span class=\"input-group-btn\">\n")
               .append("\t      <button type=\"button\" class=\"btn btn-default\" tabindex=\"-1\">")
               .append(" <i class=\"icon-calendar\"></i>")
               .append("</button>\n")
               .append("\t    </span>\n")
               .append("\t  </div>\n");
    }

    // Close additional HTML structure if necessary
    if (element.getType().equalsIgnoreCase("LOOKUP") || element.getType()
                                                               .equalsIgnoreCase("LOOKUPANDBARCODE")
        || element.getType().equalsIgnoreCase("CALENDAR") || element.getType()
                                                                    .equalsIgnoreCase("BARCODE")) {
      inputHTML.append("\t </div>\n");
    }

    // Return the generated HTML input field
    return inputHTML.toString();
  }

  /**
   * Generates HTML dropdown (select) element based on the provided form element data.
   * @param element The FormData object representing the dropdown element.
   * @return The HTML representation of the dropdown element.
   */
  private static String generateDropdownTemplate(FormData element) {
    // Get the maximum length attribute
    String maxLength = element.getMaxLen();

    // Initialize StringBuilder to construct the dropdown HTML
    StringBuilder dropdownHTML = new StringBuilder();

    // Construct the select element
    dropdownHTML.append("\t    <select id=\"").append(element.getId()).append("\" name=\"")
                .append(element.getId()).append("\" class=\"")
                .append(element.getClassName() + "\"");

    // Add maxlength attribute if specified
    if (maxLength != null && !maxLength.equalsIgnoreCase("")) {
      dropdownHTML.append(" maxlength=\"").append(maxLength).append("\"");
    }

    // Add additional attributes based on element properties
    dropdownHTML.append(element.isMandatory() ? " data-mize_required" : "")
                .append(element.isReadOnly() ? " disabled" : "").append(">\n");

    // Iterate through each options group
    for (Map<String, Object> optionsGroupMap : (List<Map<String, Object>>) element.getOptions()) {
      // Get the heading of the options group
      String heading = (String) optionsGroupMap.get("heading");

      // Get the list of options within this group
      List<Map<String, String>> optionsList = (List<Map<String, String>>) optionsGroupMap.get("options");

      // Add optgroup element with label
      dropdownHTML.append("\t     <optgroup label=\"").append(heading).append("\">\n");

      // Iterate through each option within this group
      for (Map<String, String> optionMap : optionsList) {
        // Get the text of the option
        String text = optionMap.get("text");

        // Add option element
        dropdownHTML.append("\t       <option>").append(text).append("</option>\n");
      }

      // Close optgroup element
      dropdownHTML.append("\t     </optgroup>\n");
    }

    // Close select element
    dropdownHTML.append("\t    </select>\n");

    // Return the generated HTML dropdown element
    return dropdownHTML.toString();
  }

  /**
   * Generates HTML button element based on the provided form element data.
   * @param element The FormData object representing the button element.
   * @return The HTML representation of the button element.
   */
  private static String generateButtonTemplate(FormData element) {
    // Get the value of the button
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";

    // Initialize StringBuilder to construct the button HTML
    StringBuilder templateBuilder = new StringBuilder();

    // Construct the button element
    templateBuilder.append("\t    <button type=\"button\" id=\"").append(element.getId())
                   .append("\" name=\"").append(element.getId())
                   .append("\" class=\"").append(element.getClassName()).append("\"");

    // Add maxlength attribute if specified
    if (element.getMaxLen() != null && !element.getMaxLen().isEmpty()) {
      templateBuilder.append(" maxlength=\"").append(element.getMaxLen()).append("\"");
    }

    // Add additional attributes based on element properties
    templateBuilder.append(element.isMandatory() ? " data-mize_required" : "")
                   .append(element.isReadOnly() ? " disabled" : "").append(" >").append(value)
                   .append("</button>\n");

    // Return the generated HTML button element
    return templateBuilder.toString();
  }

  /**
   * Generates HTML textarea element based on the provided form element data.
   * @param element The FormData object representing the textarea element.
   * @return The HTML representation of the textarea element.
   */
  private static String generateTextareaTemplate(FormData element) {
    // Get the value of the textarea
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";

    // Initialize StringBuilder to construct the textarea HTML
    StringBuilder templateBuilder = new StringBuilder();

    // Construct the textarea element
    templateBuilder.append("\t    <textarea id=\"").append(element.getId()).append("\" name=\"")
                   .append(element.getId())
                   .append("\" class=\"").append(element.getClassName()).append("\"");

    // Add maxlength attribute if specified
    if (element.getMaxLen() != null && !element.getMaxLen().isEmpty()) {
      templateBuilder.append(" maxlength=\"").append(element.getMaxLen()).append("\"");
    }

    // Add additional attributes based on element properties
    templateBuilder.append(element.isMandatory() ? " data-mize_required" : "")
                   .append(element.isReadOnly() ? " disabled" : "").append(" >").append(value)
                   .append("</textarea>\n");

    // Return the generated HTML textarea element
    return templateBuilder.toString();
  }

  /**
   * Generates HTML template for a collapsible section based on the provided form element data.
   * @param element The FormData object representing the collapsible section.
   * @return The HTML representation of the collapsible section.
   */
  public static String generateCollapseTemplate(FormData element) {
    // Initialize StringBuilder to construct the collapsible section HTML
    StringBuilder collapseTempl = new StringBuilder();

    // Get collapse id and name from the FormData object
    String collapseId = element.getCid();
    String collapseName = element.getCname();

    // Construct the collapsible section HTML
    collapseTempl.append("        <div class=\"col-sm-12 no-padding return-info-form\">\n");
    collapseTempl.append(
        "            <div class=\"col-xs-12 print-halfbox no-padding clearfix pad10-bottom\">\n");

    collapseTempl.append(
                     "           <a role=\"button\" data-toggle=\"collapse\"").append(" href=\"")
                 .append("#" + collapseId);

    collapseTempl.append("\"  aria-expanded=\"false\"").append("\n      aria-controls=\"")
                 .append(collapseId)
                 .append("\" class=\"col-xs-12 clearfix no-padding\" tabindex=\"-1\">\n");
    collapseTempl.append(
        "         <div class=\"col-xs-12 pad5-top collapsetabs border-bottom\">\n");
    collapseTempl.append("         <h5 class=\"text-uppercase\">").append("{{applbl \"");
    collapseTempl.append(collapseName).append("\"").append("}}").append("</h5>\n");
    collapseTempl.append("         </div>\n");
    collapseTempl.append("        </a>\n");

    collapseTempl.append(
        "       <div class=\"panel-collapse collapse in \" id=\"" + collapseId + "\">\n");
    collapseTempl.append(
        "         <div class=\"col-sm-12 errorText_overflow no-margin no-padding clearfix\">\n");

    // Return the generated HTML for the collapsible section
    return collapseTempl.toString();
  }
}