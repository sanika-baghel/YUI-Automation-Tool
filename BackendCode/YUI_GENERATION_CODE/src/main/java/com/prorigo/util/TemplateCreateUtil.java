package com.prorigo.util;

import com.prorigo.dto.FormData;
import com.prorigo.dto.RowHeader;
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
        return generateInputFieldTemplate(element);
      case "DROPDOWN":
        return generateDropdownTemplate(element);
      case "BUTTON":
        return generateButtonTemplate(element);
      case "TEXTAREA":
        return generateTextareaTemplate(element);
      case "COLLAPSE":
        return generateCollapseTemplate(element);
      case "ADDROWHEADER":
        return generateTableHeader(element);
      default:
        return ""; // Handle unsupported types
    }
  }

  private static String generateInputFieldTemplate(FormData element) {
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";
    String typeName = (element.getType().equalsIgnoreCase("TEXTBOX") || element.getType()
                                                                               .equalsIgnoreCase(
                                                                                   "LOOKUPANDBARCODE")
        || element.getType().equalsIgnoreCase("CALENDAR")) ? "text"
        : element.getType().toLowerCase();

   String maxLength= element.getMaxLen();

    StringBuilder inputHTML = new StringBuilder();
    inputHTML.append("\t    <input type=\"").append(typeName).append("\" id=\"")
             .append(element.getId())
             .append("\" name=\"").append(element.getId()).append("\" class=\"")
             .append(element.getClassName()).append("\" value=\"")
             .append(value).append("\"");
             if(maxLength!=null && !maxLength.equalsIgnoreCase("") ) {
               inputHTML.append(" maxlength=\"").append(maxLength).append("\"");
             }
           inputHTML.append(element.isMandatory() ? " data-mize_required" : "")
             .append(element.isReadOnly() ? " disabled" : "")
             .append(" />\n");
    // Add button HTML if applicable
    if (element.getType().equalsIgnoreCase("LOOKUP") || element.getType().equalsIgnoreCase(
        "LOOKUPANDBARCODE")) {
      inputHTML.append("\t    <span class=\"\">\n")
               .append("\t      <button type=\"button\" class=\"\" tabindex=\"-1\">\n")
               .append("\t        <i class=\"icon-search\"></i>\n")
               .append("\t      </button>\n")
               .append("\t    </span>\n");
    }
    if (element.getType().equalsIgnoreCase("LOOKUPANDBARCODE") || element.getType()
                                                                         .equalsIgnoreCase(
                                                                             "BARCODE")) {
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


  private static String generateDropdownTemplate(FormData element) {
    String maxLength= element.getMaxLen();
    StringBuilder dropdownHTML = new StringBuilder();
    dropdownHTML.append("\t    <select id=\"").append(element.getId()).append("\" name=\"")
                .append(element.getId()).append("\" class=\"").append(element.getClassName());
    if(maxLength!=null && !maxLength.equalsIgnoreCase("")) {
      dropdownHTML.append(" maxlength=\"").append(maxLength).append("\"");
    }
     dropdownHTML.append(element.isMandatory() ? " data-mize_required" : "")
                .append(element.isReadOnly() ? " disabled" : "").append(">\n");
    for (Map<String, Object> optionsGroupMap : (List<Map<String, Object>>) element.getOptions()) {
      String heading = (String) optionsGroupMap.get("heading");
      List<Map<String, String>> optionsList = (List<Map<String, String>>) optionsGroupMap.get(
          "options");
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

  private static String generateButtonTemplate(FormData element) {
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";
    StringBuilder templateBuilder = new StringBuilder();
    templateBuilder.append("\t    <button type=\"button\" id=\"").append(element.getId()).append("\" name=\"").append(element.getId())
                   .append("\" class=\"").append(element.getClassName()).append("\"");

    // Add maxlength attribute if element.getMaxLen() is not null or empty
    if (element.getMaxLen() != null && !element.getMaxLen().isEmpty()) {
      templateBuilder.append(" maxlength=\"").append(element.getMaxLen()).append("\"");
    }

    templateBuilder.append(element.isMandatory() ? " data-mize_required" : "")
                   .append(element.isReadOnly() ? " disabled" : "").append(" >").append(value).append("</button>\n");

    return templateBuilder.toString();
  }

  private static String generateTextareaTemplate(FormData element) {
    String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ?
        "{{" + element.getValue() + "}}" : "";
    StringBuilder templateBuilder = new StringBuilder();

    templateBuilder.append("\t    <textarea id=\"").append(element.getId()).append("\" name=\"").append(element.getId())
                   .append("\" class=\"").append(element.getClassName()).append("\"");

    // Add maxlength attribute if element.getMaxLen() is not null or empty
    if (element.getMaxLen() != null && !element.getMaxLen().isEmpty()) {
      templateBuilder.append(" maxlength=\"").append(element.getMaxLen()).append("\"");
    }
    templateBuilder.append(element.isMandatory() ? " data-mize_required" : "")
                   .append(element.isReadOnly() ? " disabled" : "").append(" >").append(value).append("</textarea>\n");

    return templateBuilder.toString();
  }


  //For Collapse
  public static String generateCollapseTemplate(FormData element) {

    StringBuilder collapseTempl = new StringBuilder();
    String collapseId = element.getCid();
    String collapseName = element.getCname();

    collapseTempl.append(
                     "           <a role=\"button\" data-toggle=\"collapse\"").append(" href=\"")
                 .append("#" + collapseId);

    collapseTempl.append("\"  aria-expanded=\"false\"").append("\n      aria-controls=\"")
                 .append(collapseId)
                 .append("\" class=\"col-xs-12 clearfix no-padding\" tabindex=\"-1\">\n");
    collapseTempl.append("         <div class=\"col-xs-12 pad5-top collapsetabs border\">\n");
    collapseTempl.append("         <h5 class=\"text-uppercase\">").append("{{applbl \"");
    collapseTempl.append(collapseName).append("\"").append("}}").append("</h5>\n");
    collapseTempl.append("         </div>\n");
    collapseTempl.append("       </a>\n");
    collapseTempl.append(
        "       <div class=\"panel-collapse collapse in border\" id=\"" + collapseId + "\">\n");
    collapseTempl.append(
        "         <div class=\"col-sm-12 errorText_overflow no-margin no-padding clearfix\">\n");

    return collapseTempl.toString();
  }

  //For Table Header
  public static String generateTableHeader(FormData element) {
    StringBuilder htmlBuilder = new StringBuilder();
    htmlBuilder.append(
        "  <div class=\"table-responsive border no-margin scrollh col-sm-12 no-padding cc-attrsTab\" id=\"attrsTable\">\n");
    htmlBuilder.append(
        "    <table class=\"table table-striped cc-product-table scroll no-margin auto-scrap claimReqTable\">\n");
    htmlBuilder.append("     <thead>\n");
    htmlBuilder.append("      <tr>\n");

    for (RowHeader header : element.getAddrowheaderkey()) {
      htmlBuilder.append("      <th class=\"").append(header.getClassName()).append("\" width=\"")
                 .append("25%")
                 .append("\" data-ref=\"").append(header.getLabel()).append("\">\n");
      htmlBuilder.append("       {{applbl '").append(header.getLabel()).append("'}}\n");
      htmlBuilder.append(
          "       <a class=\"actioncol1\"><i class=\"icon-menu-open small-font pad5-top\"></i></a>\n");
      htmlBuilder.append("      </th>\n");
    }
    htmlBuilder.append("      </tr>\n");

    htmlBuilder.append("      </table>\n");
    htmlBuilder.append("      </div>\n");

    return htmlBuilder.toString();
  }

  //For Table Data
  public static String generateTableData(FormData element) {

    StringBuilder htmlForm = new StringBuilder();

    htmlForm.append("      <tbody id=\"orderPartTbody\">\n");
    for (FormData header : element.getAddrowkey()) {
      htmlForm.append("        <td class=\"\">\n");
      htmlForm.append(
          "            <div class=\"container clearfix form-group control-group input controls minwidth-75\">\n");
      htmlForm.append("                {{#if @viewonly}}\n");
      htmlForm.append("                    <p class=\"").append("\" id=\"").append(header.getId())
              .append("\" name=\"").append(header.getId()).append("\">").append("{{")
              .append(header.getId()).append("}}").append("</p>\n");
      htmlForm.append("                {{else}}\n");
      htmlForm.append(TemplateCreateUtil.generateComponentHTML(header));
      htmlForm.append("                {{/if}}\n");
      htmlForm.append("            </div>\n");
      htmlForm.append("        </td>\n");
    }
    htmlForm.append("      </tbody>\n");
    return htmlForm.toString();
  }
}