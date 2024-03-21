package com.prorigo.util;

import com.prorigo.dto.FormData;
import com.prorigo.dto.RowHeader;

public class TableDataUtil {

  /**
   * Generates HTML template for a table header based on the provided form element data.
   * @param element The FormData object representing the table header.
   * @return The HTML representation of the table header.
   */
  public static String generateTableHeader(FormData element) {
    // Initialize StringBuilder to construct the table header HTML
    StringBuilder htmlBuilder = new StringBuilder();

    // Construct the table header HTML
    htmlBuilder.append(
        "  <div class=\"table-responsive border no-margin scrollh col-sm-12 no-padding cc-attrsTab\" id=\"attrsTable\">\n");
    htmlBuilder.append(
        "    <table class=\"table table-striped cc-product-table scroll no-margin auto-scrap claimReqTable\">\n");
    htmlBuilder.append("     <thead>\n");
    htmlBuilder.append("      <tr>\n");

    // Iterate through each row header and add table header cells
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

    // Return the generated HTML for the table header
    return htmlBuilder.toString();
  }

  /**
   * Generates HTML template for table data based on the provided form element data.
   * @param element The FormData object representing the table data.
   * @return The HTML representation of the table data.
   */
  public static String generateTableData(FormData element) {
    // Initialize StringBuilder to construct the table data HTML
    StringBuilder htmlForm = new StringBuilder();

    // Open the tbody tag for table data
    htmlForm.append("      <tbody id=\"orderPartTbody\">\n");

    // Iterate through each row of table data and add table cells
    for (FormData header : element.getAddrowkey()) {
      htmlForm.append("        <td class=\"\">\n");
      htmlForm.append(
          "            <div class=\"container clearfix form-group control-group input controls minwidth-75\">\n");
      htmlForm.append("                {{#if @viewonly}}\n");
      htmlForm.append("                    <p class=\"").append("\" id=\"").append(header.getId())
              .append("\" name=\"").append(header.getId()).append("\">").append("{{")
              .append(header.getId()).append("}}").append("</p>\n");
      htmlForm.append("                {{else}}\n");
      htmlForm.append(TemplateCreateUtil.generateComponentHTML(header)); // Generate HTML for the component
      htmlForm.append("                {{/if}}\n");
      htmlForm.append("            </div>\n");
      htmlForm.append("        </td>\n");
    }

    // Close the tbody tag for table data
    htmlForm.append("      </tbody>\n");

    // Return the generated HTML for the table data
    return htmlForm.toString();
  }
}
