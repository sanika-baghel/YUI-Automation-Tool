package com.prorigo.util;
import com.google.gson.Gson;
import com.prorigo.dto.FormData;
import java.util.List;
import java.util.Map;

public class TableRowData {

  private final Gson gson = new Gson();
  public String addTableRowData(String jsonInput) {

    FormData[] formElements = gson.fromJson(jsonInput, FormData[].class);

    StringBuilder htmlForm = new StringBuilder();

    htmlForm.append("<tr id=\"partsRow\" class=\"partsRow orderItems partsRow_{{@index}}\" data-index=\"{{@index}}\">\n");
    for (FormData element : formElements) {

      // Check if the value is null or not
      String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ? "{{"
          + element.getValue() + "}}" : "";

      htmlForm.append("  <td class=\"\">\n");
      htmlForm.append("    <div class=\"cc-field width100perc\">\n");
      htmlForm.append("      <div class=\"cc-field-12 controls\">\n");
      htmlForm.append("        <p class=\"").append("\" id=\"").append(element.getId())
              .append("\" name=\"").append(element.getId()).append("\">").append("{{")
              .append(element.getId()).append("}}").append("</p>\n");

      switch (element.getType()) {

        // For Textbox,Checkbox,Radio,Lookup,Barcode,LookupAndBarcode
        case "TEXTBOX":
        case "CHECKBOX":
        case "RADIO":
        case "LOOKUP":
        case "BARCODE":
        case "LOOKUPANDBARCODE":
        case "CALENDAR":
          String typeName="";
          if (element.getType().equalsIgnoreCase("TEXTBOX") || element.getType().equalsIgnoreCase(
              "LOOKUPANDBARCODE") || element.getType().equalsIgnoreCase(
              "CALENDAR")) {
            typeName = "text";
          } else {
            typeName = element.getType().toLowerCase();
          }
          if (element.getType().equalsIgnoreCase("CALENDAR")) {
            htmlForm.append("\t     <div class=\"input-group\">\n");
          }
          htmlForm.append("\t      <input type=\"").append(typeName).append("\" id=\"")
                  .append(element.getId())
                  .append("\" name=\"").append(element.getId()).append("\" class=\"")
                  .append(element.getClassName()).append("\" value=\"")
                  .append(value).append("\"")
                  .append(element.isMandatory() ? " data-mize_required" : "")
                  .append(element.isReadOnly() ? " disabled" : "")
                  .append(" />\n");

          if (element.getType().equalsIgnoreCase("LOOKUP") || element.getType().equalsIgnoreCase(
              "LOOKUPANDBARCODE")) {
            htmlForm.append("\t    <span class=\"").append("\">")
                    .append("<button type=\"button\"").append(" class=\"")
                    .append("\" tabindex=\"-1\"").append(">")
                    .append("<i class=\"icon-search\"></i>").append("</button>\n")
                    .append("\t    </span>\n");
          }
          if (element.getType().equalsIgnoreCase("LOOKUPANDBARCODE") || element.getType()
                                                                               .equalsIgnoreCase(
                                                                                   "BARCODE")) {
            htmlForm.append("\t       <span class=\"").append("\">")
                    .append("<button type=\"button\"").append(" class=\"")
                    .append("\" tabindex=\"-1\"").append(" id=\"").append("\">")
                    .append("<i class=\"icon-barcode big-font\"></i>").append("</button>\n")
                    .append("\t      </span>\n");
          }

          if (element.getType().equalsIgnoreCase("CALENDAR")) {
            htmlForm.append("\t       <span class=\"").append("\">")
                    .append("<button type=\"button\"").append(" class=\"")
                    .append("\" tabindex=\"-1\"").append(" id=\"").append("\">")
                    .append("<i class=\"icon-calendar\"></i>").append("</button>\n")
                    .append("\t       </span>\n").append("\t   </div>\n");
          }
          break;

        // For Dropdown
        case "DROPDOWN":
          htmlForm.append("\t      <select id=\"").append(element.getId()).append("\" name=\"")
                  .append(element.getId()).append("\" class=\"").append(element.getClassName())
                  .append("\" value=\"")
                  .append(value).append("\"")
                  .append(element.isMandatory() ? " data-mize_required" : "")
                  .append(element.isReadOnly() ? " disabled" : "").append(">\n");

          for (Map<String, Object> optionsGroupMap : (List<Map<String, Object>>) element.getOptions()) {
            String heading = (String) optionsGroupMap.get("heading");
            List<Map<String, String>> optionsList = (List<Map<String, String>>) optionsGroupMap.get(
                "options");

            htmlForm.append("\t     <optgroup label=\"").append(heading).append("\">\n");

            for (Map<String, String> optionMap : optionsList) {
              String text = optionMap.get("text");
              htmlForm.append("\t   <option>").append(text).append("</option>\n");
            }
            htmlForm.append("\t     </optgroup>\n");
          }
          htmlForm.append("\t       </select>\n");
          break;

        //For Button
        case "BUTTON":
          htmlForm.append("\t      <button type=\"button\" id=\"").append(element.getId())
                  .append("\" name=\"").append(element.getId())
                  .append("\" class=\"").append(element.getClassName())
                  .append("\" value=\"")
                  .append(value).append("\"")
                  .append(element.isMandatory() ? " data-mize_required" : "")
                  .append(element.isReadOnly() ? " disabled" : "").append(" >\n")
                  .append("\t      </button>\n");
          break;

        //For TextArea
        case "TEXTAREA":
          htmlForm.append("\t    <textarea").append(" id=\"").append(element.getId())
                  .append("\" name=\"").append(element.getId()).append("\" class=\"")
                  .append(element.getClassName()).append("\" value=\"")
                  .append(value).append("\"")
                  .append(element.isMandatory() ? " data-mize_required" : "")
                  .append(element.isReadOnly() ? " disabled" : "")
                  .append(" >")
                  .append("\t   </textarea>\n");
          break;
      }
      htmlForm.append("    </div>\n");
      htmlForm.append("  </div>\n");
      htmlForm.append("</td>\n");

    }
    return htmlForm.toString();
  }
}
