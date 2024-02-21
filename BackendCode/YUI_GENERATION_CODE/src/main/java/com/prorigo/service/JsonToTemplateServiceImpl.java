package com.prorigo.service;

import com.prorigo.dto.FormData;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class JsonToTemplateServiceImpl implements JsonToTemplateService {

  @Override
  public String convertJsonToTemplate(String jsonInput) {
    if (jsonInput == null || jsonInput.isEmpty()) {
      return "No JSON input provided.";
    }

    // Convert JSON to Java objects
    Gson gson = new Gson();
    FormData[] formElements = gson.fromJson(jsonInput, FormData[].class);

    // Check if formElements is null
    if (formElements == null) {
      return "Error parsing JSON input.";
    }
    // Generate Template form
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

    for (FormData element : formElements) {
      String typeName;
      htmlForm.append("\t      <div class=\"cc-field\">\n");
      htmlForm.append("\t      <label class=\"\">").append("{{applbl ")
              .append("'").append(element.getLabel()).append("'").append("}}").append("</label>\n");

      htmlForm.append("\t      <div class=\"controls\">\n");

      htmlForm.append("\t      {{#if @viewonly}}\n");
      htmlForm.append("\t      <p class=\"").append("\" id=\"").append(element.getId())
              .append("\" name=\"").append(element.getId()).append("\">").append("{{")
              .append(element.getId()).append("}}").append("</p>\n");

      htmlForm.append("\t      {{else}}\n");

      // Check if the value is null or not
      String value = (element.getValue() != null && !element.getValue().equalsIgnoreCase("")) ? "{{"
          + element.getValue() + "}}" : "";

      switch (element.getType()) {

        //For Button
        case "BUTTON":
          htmlForm.append("   <button type=\"button\" id=\"").append(element.getId())
                  .append("\" name=\"").append(element.getId())
                  .append("\" class=\"").append(element.getClassName())
                  .append("\" value=\"")
                  .append(value).append("\"")
                  .append(element.isMandatory() ? " data-mize_required" : "")
                  .append(element.isReadOnly() ? " disabled" : "").append(" >\n")
                  .append("</button>\n");
          break;

        //For Dropdown
        case "DROPDOWN":
          htmlForm.append("\t<select id=\"").append(element.getId()).append("\" name=\"")
                  .append(element.getId()).append("\" class=\"").append(element.getClassName());

          htmlForm.append("\" value=\"")
                  .append(value).append("\"")
                  .append(element.isMandatory() ? " data-mize_required" : "")
                  .append(element.isReadOnly() ? " disabled" : "").append(">");

          htmlForm.append("<option value=\"").append(element.getValue()).append("\">")
                  .append(element.getText()).append("</option>");

//          for (OptionsGroup optionsGroup : element.getOptions()) {
//            htmlForm.append("<optgroup label=\"").append(optionsGroup.getHeading()).append("\">");
//            for (Option option : optionsGroup.getOptions()) {
//              htmlForm.append("<option value=\"").append(option.getText()).append("\">").append(option.getText()).append("</option>\n");
//            }
//            htmlForm.append("\t</optgroup>\n");
//          }

          htmlForm.append("\t</select>\n");

          break;

        //For TextArea
        case "TEXTAREA":
          htmlForm.append("\t<textarea").append(" id=\"").append(element.getId())
                  .append("\" name=\"").append(element.getId()).append("\" class=\"")
                  .append(element.getClassName()).append("\" value=\"")
                  .append(value).append("\"")
                  .append(element.isMandatory() ? " data-mize_required" : "")
                  .append(element.isReadOnly() ? " disabled" : "")
                  .append(" >")
                  .append("</textarea>\n");
          break;

       // For Textbox,Checkbox,Radio,Lookup,Barcode,LookupAndBarcode
        case "TEXTBOX":
        case "CHECKBOX":
        case "RADIO":
        case "LOOKUP":
        case "BARCODE":
        case "LOOKUPANDBARCODE":

          if (element.getType().equalsIgnoreCase("TEXTBOX") || element.getType().equalsIgnoreCase(
              "LOOKUPANDBARCODE")) {
            typeName = "text";
          } else {
            typeName = element.getType().toLowerCase();
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
            htmlForm.append("\t<span class=\"").append("\">")
                    .append("\t<button type=\"button\"").append(" class=\"")
                    .append("\" tabindex=\"-1\"").append(">")
                    .append("<i class=\"icon-search\"></i>").append("</button>\n")
                    .append("\t</span>\n");
          }
          if (element.getType().equalsIgnoreCase("LOOKUPANDBARCODE") || element.getType()
                                                                               .equalsIgnoreCase(
                                                                                   "BARCODE")) {
            htmlForm.append("\t<span class=\"").append("\">")
                    .append("\t<button type=\"button\"").append(" class=\"")
                    .append("\" tabindex=\"-1\"").append(" id=\"").append("\">")
                    .append("<i class=\"icon-barcode big-font\"></i>").append("</button>\n")
                    .append("\t</span>\n");
          }
          break;
        default:
          break;
      }
      htmlForm.append("\t       {{/if}}\n");
      htmlForm.append("\t      </div>\n");
      htmlForm.append("\t     </div>\n");
    }
    htmlForm.append("        </div>\n");
    htmlForm.append("       </div>\n");
    htmlForm.append("      </div>\n");
    htmlForm.append("    </div>\n");
    htmlForm.append("   </div>\n");
    htmlForm.append("  </div>\n");
    htmlForm.append(" </div>\n");
    htmlForm.append("</form>");
    return htmlForm.toString();
  }

  //Write data in Template File
  public void writeTemplateToFile(String htmlForm) throws IOException {
    try (FileWriter fileWriter = new FileWriter("output.template")) {
      fileWriter.write(htmlForm);
    }
  }

}

