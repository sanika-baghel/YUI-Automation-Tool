package com.prorigo.util;
import com.google.gson.Gson;
import com.prorigo.dto.FormData;


public class TableRowData {
  private final Gson gson = new Gson();
  public String addTableRowData(String jsonInput) {
    FormData[] formElements = gson.fromJson(jsonInput, FormData[].class);
    StringBuilder htmlForm = new StringBuilder();

    htmlForm.append("<tr id=\"partsRow\" class=\"partsRow orderItems partsRow_{{@index}}\" data-index=\"{{@index}}\">\n");
    for (FormData element : formElements) {
      htmlForm.append("  <td class=\"\">\n");
      htmlForm.append("    <div class=\"cc-field width100perc\">\n");
      htmlForm.append("      <div class=\"cc-field-12 controls\">\n");
      htmlForm.append("        <p class=\"").append("\" id=\"").append(element.getId())
              .append("\" name=\"").append(element.getId()).append("\">").append("{{")
              .append(element.getId()).append("}}").append("</p>\n");

      htmlForm.append(TemplateCreateUtil.generateComponentHTML(element));
      htmlForm.append("    </div>\n");
      htmlForm.append("  </div>\n");
      htmlForm.append("  </td>\n");
    }
    return htmlForm.toString();
  }
}