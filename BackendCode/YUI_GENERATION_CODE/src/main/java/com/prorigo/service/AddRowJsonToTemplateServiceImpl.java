package com.prorigo.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class AddRowJsonToTemplateServiceImpl implements AddRowJsonToTemplateService {


  private final Gson gson = new Gson();

  @Override
  public String convertJsonToTemplate(String jsonInput) {

    JsonArray jsonArray = gson.fromJson(jsonInput, JsonArray.class);

    StringBuilder htmlBuilder = new StringBuilder();

    for (JsonElement element : jsonArray) {
      JsonObject jsonObject = element.getAsJsonObject();

      String dataRef = jsonObject.get("data-ref").getAsString();
      String width = jsonObject.get("width").getAsString();
      String cssClass = jsonObject.get("class").getAsString();
      String label = jsonObject.get("label").getAsString();

      htmlBuilder.append("<th");
      htmlBuilder.append(" class=\"").append(cssClass).append("\"");
      htmlBuilder.append(" width=\"").append(width).append("\"");
      htmlBuilder.append(" data-ref=\"").append(dataRef).append("\"");
      htmlBuilder.append(">");
      htmlBuilder.append("{{applbl ")
                 .append("'").append(label).append("'").append("}}");
      htmlBuilder.append(
          "<a class=\"actioncol1\"><i class=\"icon-menu-open small-font pad5-top\"></i></a>");
      htmlBuilder.append("</th>");
    }

    return htmlBuilder.toString();
  }


  @Override
  public void writeTemplateToFile(String htmlForm) throws IOException {
    try (FileWriter fileWriter = new FileWriter("output.template")) {
      fileWriter.write(htmlForm);
    }
  }
}

