package com.prorigo.service;

import com.prorigo.dto.CollapseSection;
import com.prorigo.dto.FormData;
import com.prorigo.dto.TemplateSection;
import com.prorigo.util.TableDataUtil;
import com.prorigo.util.TemplateCreateUtil;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class JsonToTemplateServiceImpl implements JsonToTemplateService {

  private final Gson gson = new Gson();

  @Override

  public String convertJsonToMultiTemplate(String jsonInput) {
    if (jsonInput == null || jsonInput.isEmpty()) {
      return "No JSON input provided.";
    }

    // Parse JSON input into FormData array
    FormData[] formElements = gson.fromJson(jsonInput, FormData[].class);
    if (formElements == null) {
      return "Error parsing JSON input.";
    }
    StringBuilder htmlForm = new StringBuilder();

    // Iterate over each template
    for (FormData template : formElements) {
      String templateName = template.getTemplateName();
      String tabName = template.getTabName();

      // Start building HTML form
      htmlForm.append("<form role=\"form\" class=\"form-horizontal\" id=\"return-info-form\">\n");
      htmlForm.append(" <div class=\"col-sm-12 col-md-12 col-xs-12 clearfix no-padding\"> \n");
      htmlForm.append("  <div class=\"col-sm-12 clearfix no-padding\"> \n");
      htmlForm.append("    <div class=\"col-sm-12 no-padding return-info-form\"> \n");
      htmlForm.append(
          "      <div class=\"col-xs-12 print-halfbox no-padding clearfix pad10-bottom\">\n");

      // Iterate over collapse sections for the current template
      for (CollapseSection collapse : template.getCollapse()) {
        String collapseName = collapse.getCname();
        String collapseId = collapse.getCid();

        // Start building template section
        htmlForm.append(
                    "           <a role=\"button\" data-toggle=\"collapse\"").append(" href=\"")
                .append("#" + collapseId);

        htmlForm.append("\"  aria-expanded=\"false\"").append("\n      aria-controls=\"")
                .append(collapseId)
                .append("\" class=\"col-xs-12 clearfix no-padding\" tabindex=\"-1\">\n");
        htmlForm.append("         <div class=\"col-xs-12 pad5-top collapsetabs border-bottom\">\n");
        htmlForm.append("         <h5 class=\"text-uppercase\">").append("{{applbl \"");
        htmlForm.append(collapseName).append("\"").append("}}").append("</h5>\n");
        htmlForm.append("         </div>\n");
        htmlForm.append("       </a>\n");

        htmlForm.append(
            "       <div class=\"panel-collapse collapse in border\" id=\"" + collapseId + ">\n");
        htmlForm.append(
            "         <div class=\"col-sm-12 errorText_overflow no-margin no-padding clearfix\">\n");

        // Iterate over fields within the current collapse section

        for (FormData element : collapse.getFields()) {
          htmlForm.append("\t      <div class=\"cc-field\">\n");
          htmlForm.append("\t      <label class=\"\">").append("{{applbl ")
                  .append("'").append(element.getLabel()).append("'").append("}}")
                  .append("</label>\n");

          htmlForm.append("\t      <div class=\"controls\">\n");

          htmlForm.append("\t      {{#if @viewonly}}\n");
          htmlForm.append("\t      <p class=\"").append("\" id=\"").append(element.getId())
                  .append("\" name=\"").append(element.getId()).append("\">").append("{{")
                  .append(element.getId()).append("}}").append("</p>\n");

          htmlForm.append("\t      {{else}}\n");

          htmlForm.append(TemplateCreateUtil.generateComponentHTML(element));
          htmlForm.append("           {{/if}}\n");
          htmlForm.append("          </div>\n");
          htmlForm.append("        </div>\n");
        }

        // End of collapse section
        htmlForm.append("          </div>\n");
        htmlForm.append("        </div>\n");
      }

      // End of template section
      htmlForm.append("    </div>\n");
      htmlForm.append("   </div>\n");
      htmlForm.append("  </div>\n");
      htmlForm.append(" </div>\n");
      htmlForm.append("</form>");

      // Write HTML form to file
      writeToFile(htmlForm.toString(), templateName + ".template");

      // Clear StringBuilder for the next template
      htmlForm.setLength(0);
    }
    // End of HTML form
    return htmlForm.toString();
  }


  /**
   * Converts JSON input representing form templates into HTML templates.
   *
   * @param jsonInput The JSON input string representing form templates.
   * @return The HTML representation of the form templates.
   */
  public String convertJsonToTemplate(String jsonInput) {
    // Check if JSON input is provided
    if (jsonInput == null || jsonInput.isEmpty()) {
      return "No JSON input provided.";
    }

    // Parse JSON input into TemplateSection array
    TemplateSection[] formElements = gson.fromJson(jsonInput, TemplateSection[].class);

    // Check if parsing was successful and array is not empty
    if (formElements == null || formElements.length == 0) {
      return "Error parsing JSON input or empty array.";
    }

    // Initialize StringBuilder to construct HTML form
    StringBuilder htmlForm = new StringBuilder();

    // Iterate over each template section
    for (TemplateSection template : formElements) {
      String templateName = template.getTemplateName();
      String tabName = template.getTabName();
      boolean firstCollapse = true; // Initialize firstCollapse for each template

      // Open form tag with role, class, and id attributes
      htmlForm.append("<form role=\"form\" class=\"form-horizontal\" id=\"return-info-form\">\n");
      htmlForm.append(" <div class=\"col-sm-12 col-md-12 col-xs-12 clearfix no-padding\"> \n");
      htmlForm.append("  <div class=\"col-sm-12 clearfix no-padding\"> \n");
      htmlForm.append("    <div class=\"col-sm-12 no-padding return-info-form\"> \n");
      htmlForm.append(
          "      <div class=\"col-xs-12 print-halfbox no-padding clearfix pad10-bottom\">\n");

      // Iterate over each form element in the template section
      for (FormData element : template.getFields()) {
        // Check element type and generate corresponding HTML
        if ("COLLAPSE".equalsIgnoreCase(element.getType())) {
          if (!firstCollapse) {
            // Close the previous collapse panel if it's not the first one
            htmlForm.append("          </div>\n");
            htmlForm.append("        </div>\n");
            htmlForm.append("       </div>\n");
            htmlForm.append("      </div>\n");
          } else {
            // If it's the first collapse, mark it as false to avoid skipping the next div
            firstCollapse = false;
          }
          htmlForm.append(TemplateCreateUtil.generateCollapseTemplate(element));
        } else if ("ADDROWHEADER".equalsIgnoreCase(element.getType())) {
          htmlForm.append(TableDataUtil.generateTableHeader(element));
        } else if ("ADDROWS".equalsIgnoreCase(element.getType())) {
          htmlForm.append(TableDataUtil.generateTableData(element));
        } else {
          htmlForm.append("\t      <div class=\"cc-field\">\n");
          htmlForm.append("\t      <label class=\"\">").append("{{applbl ")
                  .append("'").append(element.getLabel()).append("'").append("}}")
                  .append("</label>\n");
          htmlForm.append("\t      <div class=\"controls\">\n");
          htmlForm.append("\t      {{#if @viewonly}}\n");
          htmlForm.append("\t      <p class=\"").append("\" id=\"").append(element.getId())
                  .append("\" name=\"").append(element.getId()).append("\">").append("{{")
                  .append(element.getId()).append("}}").append("</p>\n");
          htmlForm.append("\t      {{else}}\n");
          htmlForm.append(TemplateCreateUtil.generateComponentHTML(element));
          htmlForm.append("           {{/if}}\n");
          htmlForm.append("          </div>\n");
          htmlForm.append("         </div>\n");
        }
      }

      // Close the last collapse panel if there was one
      if (!firstCollapse) {
        htmlForm.append("         </div>\n");
        htmlForm.append("        </div>\n");
        htmlForm.append("       </div>\n");
        htmlForm.append("      </div>\n");
      }

      // Close divs for form structure
      htmlForm.append("     </div>\n");
      htmlForm.append("    </div>\n");
      htmlForm.append("   </div>\n");
      htmlForm.append("  </div>\n");
      htmlForm.append("</form>");

      // Write HTML form to file
      writeToFile(htmlForm.toString(), templateName + ".template");

      // Clear StringBuilder for the next template
      htmlForm.setLength(0);
    }

    // Return the generated HTML
    return htmlForm.toString();
  }

  /**
   * Writes the provided content to a file with the given filename.
   * @param content The content to write to the file.
   * @param filename The name of the file to write to.
   */
  private void writeToFile(String content, String filename) {
    try (FileWriter writer = new FileWriter(filename)) {
      writer.write(content); // Write content to the file
    } catch (IOException e) {
      e.printStackTrace(); // Print stack trace if an IOException occurs
    }
  }
}