package com.prorigo.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prorigo.util.TableRowData;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class AddRowJsonToTemplateServiceImpl implements AddRowJsonToTemplateService {

  private final Gson gson = new Gson();

  @Override
  public String addRawHeading(String jsonInput) {

    JsonArray jsonArray = gson.fromJson(jsonInput, JsonArray.class);

    StringBuilder htmlBuilder = new StringBuilder();

    htmlBuilder.append(
        "<div class=\"panel-collapse collapse in border\" id=\"collapsePartsHeader\">\n");
    htmlBuilder.append(
        " <div class=\"col-sm-12 errorText_overFlow no-margin no-padding clearfix\">\n");
    htmlBuilder.append(
        "  <div class=\"table-responsive border no-margin scrollh col-sm-12 no-padding cc-attrsTab\" id=\"attrsTable\">\n");
    htmlBuilder.append(
        "    <table class=\"table table-striped cc-product-table scroll no-margin auto-scrap claimReqTable\">\n");
    htmlBuilder.append("     <thead>\n");
    htmlBuilder.append("      <tr>\n");

    for (JsonElement element : jsonArray) {
      JsonObject jsonObject = element.getAsJsonObject();

      String className = jsonObject.get("class").getAsString();
      String label = jsonObject.get("label").getAsString();

      htmlBuilder.append("      <th class=\"").append(className).append("\" width=\"").append("25%")
                 .append("\" data-ref=\"").append(label).append("\">\n");
      htmlBuilder.append("       {{applbl '").append(label).append("'}}\n");
      htmlBuilder.append(
          "       <a class=\"actioncol1\"><i class=\"icon-menu-open small-font pad5-top\"></i></a>\n");
      htmlBuilder.append("      </th>\n");
    }
    htmlBuilder.append("       </tr>\n");
    htmlBuilder.append("       <tbody id=\"orderPartTbody\">");
    //TO DO ADD ROW DATA
    htmlBuilder.append("    </tbody>\n");
    htmlBuilder.append("   </table>\n");
    htmlBuilder.append("  </div>\n");
    htmlBuilder.append(" </div>\n");
    htmlBuilder.append(" </div>");
    return htmlBuilder.toString();
  }

  @Override
  public String addDataTable(String jsonInput) {
    TableRowData tableRawData = new TableRowData();
    return tableRawData.addTableRowData(jsonInput);
  }

  @Override
  public void writeTemplateToFile(String htmlForm) throws IOException {
    try (FileWriter fileWriter = new FileWriter("output.template")) {
      fileWriter.write(htmlForm);
    }
  }
}

