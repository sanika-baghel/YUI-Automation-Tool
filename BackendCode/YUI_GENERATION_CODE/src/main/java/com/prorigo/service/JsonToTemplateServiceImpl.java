package com.prorigo.service;

import com.prorigo.dto.CollapseSection;
import com.prorigo.dto.FormData;

import com.prorigo.util.TemplateCreateUtil;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class JsonToTemplateServiceImpl implements JsonToTemplateService {
  private final Gson gson = new Gson();
  @Override
  //Write data in Template File
  public void writeTemplateToFile(String htmlForm) throws IOException {
    try (FileWriter fileWriter = new FileWriter("output.template")) {
      fileWriter.write(htmlForm);
    }
  }

  public String convertJsonToTemplate(String jsonInput) {
    if (jsonInput == null || jsonInput.isEmpty()) {
      return "No JSON input provided.";
    }

    FormData[] formElements = gson.fromJson(jsonInput, FormData[].class);
    // Check if formElements is null
    if (formElements == null) {
      return "Error parsing JSON input.";
    }
    StringBuilder htmlForm = new StringBuilder();

    htmlForm.append("<form role=\"form\" class=\"form-horizontal\" id=\"return-info-form\">\n");
    htmlForm.append(" <div class=\"col-sm-12 col-md-12 col-xs-12 clearfix no-padding\"> \n");
    htmlForm.append("  <div class=\"col-sm-12 clearfix no-padding\"> \n");
    htmlForm.append("    <div class=\"col-sm-12 no-padding return-info-form\"> \n");
    htmlForm.append(
        "      <div class=\"col-xs-12 print-halfbox no-padding clearfix pad10-bottom\">\n");
    htmlForm.append(
        "       <div class=\"panel-collapse collapse in col-xs-12 no-margin no-padding pad15-top id=\"\">\n");
    htmlForm.append(
        "         <div class=\"col-sm-12 errorText_overFlow no-margin no-padding clearfix\">\n");
    htmlForm.append("           <div class=\"cc-field control-group drpDwn_cc hide\">\n");

    htmlForm.append(
                "           <a role=\"button\" data-toggle=\"collapse\"").append(" href=\"")
            .append("#" + "");

    htmlForm.append("\"  aria-expanded=\"false\"").append("\n      aria-controls=\"").append("")
            .append("\" class=\"col-xs-12 clearfix no-padding\" tabindex=\"-1\">\n");

    htmlForm.append("         <div class=\"col-xs-12 pad5-top collapsetabs border\">\n");
    htmlForm.append("         <h5 class=\"text-uppercase\">").append("{{applbl \"");
    htmlForm.append("").append("\"").append("}}").append("</h5>\n");
    htmlForm.append("         </div>\n");
    htmlForm.append("       </a>\n");

    htmlForm.append(
        "       <div class=\"panel-collapse collapse in border\" id=\"collapseCoreInspectInfo\">\n");
    htmlForm.append(
        "         <div class=\"col-sm-12 errorText_overflow no-margin no-padding clearfix\">\n");

    for (FormData element : formElements) {
      htmlForm.append("\t      <div class=\"cc-field\">\n");
      htmlForm.append("\t      <label class=\"\">").append("{{applbl ")
              .append("'").append(element.getLabel()).append("'").append("}}").append("</label>\n");

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
    htmlForm.append("         </div>\n");
    htmlForm.append("        </div>\n");
    htmlForm.append("       </div>\n");
    htmlForm.append("      </div>\n");
    htmlForm.append("     </div>\n");
    htmlForm.append("    </div>\n");
    htmlForm.append("   </div>\n");
    htmlForm.append("  </div>\n");
    htmlForm.append(" </div>\n");
    htmlForm.append("</form>");
    return htmlForm.toString();
  }


  public String convertJsonToMultiTemplate(String jsonInput) {
    if (jsonInput == null || jsonInput.isEmpty()) {
      return "No JSON input provided.";
    }

    // Parse JSON input into FormData array
    FormData[] formElements = gson.fromJson(jsonInput, FormData[].class);
    System.out.println("formElements=="+formElements);
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
    htmlForm.append(
        "       <div class=\"panel-collapse collapse in col-xs-12 no-margin no-padding pad15-top id=\"\">\n");
    htmlForm.append(
        "         <div class=\"col-sm-12 errorText_overFlow no-margin no-padding clearfix\">\n");
    htmlForm.append("           <div class=\"cc-field control-group drpDwn_cc hide\">\n");

      // Iterate over collapse sections for the current template
      for (CollapseSection collapse : template.getCollapse()) {
        String collapseName = collapse.getCollapseName();
        String collapseId = collapse.getCollapseId();

      // Start building template section
      htmlForm.append(
                  "           <a role=\"button\" data-toggle=\"collapse\"").append(" href=\"")
              .append("#" + collapseId);

      htmlForm.append("\"  aria-expanded=\"false\"").append("\n      aria-controls=\"").append(collapseId)
              .append("\" class=\"col-xs-12 clearfix no-padding\" tabindex=\"-1\">\n");
      htmlForm.append("         <div class=\"col-xs-12 pad5-top collapsetabs border\">\n");
      htmlForm.append("         <h5 class=\"text-uppercase\">").append("{{applbl \"");
      htmlForm.append(collapseName).append("\"").append("}}").append("</h5>\n");
      htmlForm.append("         </div>\n");
      htmlForm.append("       </a>\n");

      htmlForm.append(
          "       <div class=\"panel-collapse collapse in border\" id=\"collapseCoreInspectInfo\">\n");
      htmlForm.append(
          "         <div class=\"col-sm-12 errorText_overflow no-margin no-padding clearfix\">\n");


        // Iterate over fields within the current collapse section

        for (FormData element : collapse.getFields()) {
          htmlForm.append("\t      <div class=\"cc-field\">\n");
          htmlForm.append("\t      <label class=\"\">").append("{{applbl ")
                  .append("'").append(element.getLabel()).append("'").append("}}").append("</label>\n");

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
      htmlForm.append("       </div>\n");
      htmlForm.append("      </div>\n");
      htmlForm.append("     </div>\n");
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
  private void writeToFile(String content, String filename) {
    try (FileWriter writer = new FileWriter(filename)) {
      writer.write(content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

