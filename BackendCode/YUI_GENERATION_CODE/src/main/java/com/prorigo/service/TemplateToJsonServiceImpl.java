package com.prorigo.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import com.prorigo.dto.FormData;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class TemplateToJsonServiceImpl implements TemplateToJsonService {

  private final Gson gson = new Gson();

  public List<FormData> convertTemplateToJson(MultipartFile file) throws IOException {
    //Convert File to String
    String htmlContent = new String(file.getBytes());
    Document doc = Jsoup.parse(htmlContent);

    Elements elements = doc.select(
        "input[type=checkbox], input[type=radio], select, input[type=text],input[type=lookup],input[type=barcode],"
            + "input[type=lookup and barcode], input[type=Calendar], textarea,button, input[type=file]");

    // Extract Label Names
    List<String> labelNames = extractLabels(htmlContent);
    List<FormData> formDataList = new ArrayList<>();
    for (Element element : elements) {
      if (!element.toString().startsWith("<!--") && !element.toString().endsWith("-->")) {
        FormData formData = new FormData();
        String extractedContent = "";
        // Iterate over label names and set them in FormData
        int index = elements.indexOf(element);
        if (index < labelNames.size()) {
          formData.setLabel(labelNames.get(index));
        }
        String inputValue = element.attr("value");
        int startIndex = inputValue.indexOf("{{");
        int endIndex = inputValue.indexOf("}}");
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
          extractedContent = inputValue.substring(startIndex + 2, endIndex);
        }
        formData.setType(checkTypeName(element));
        formData.setId(element.attr("id"));
        formData.setValue(extractedContent);
        formData.setClassName(element.hasAttr("class") ? element.attr("class") : "");
        formData.setReadOnly(element.hasAttr("disabled"));

        if (!element.hasAttr("data-mize_required")) {
          formData.setMandatory(false);
        } else {
          formData.setMandatory(element.hasAttr("data-mize_required"));
        }

        if ("TEXTBOX".equals(formData.getType())) {
          formData.setText("Textbox");
        } else if ("CHECKBOX".equals(formData.getType())) {
          formData.setText("Checkbox");
        } else if ("RADIO".equals(formData.getType())) {
          formData.setText("Radio Button");
        } else if ("BUTTON".equals(formData.getType())) {
          formData.setText("Button");
        } else if ("TEXTAREA".equals(formData.getType())) {
          formData.setText("Textarea");
        } else if ("LOOKUP".equals(formData.getType())) {
          formData.setText("Lookup");
        } else if ("LOOKUPANDBARCODE".equals(formData.getType())) {
          formData.setText("text");
        } else if ("BARCODE".equals(formData.getType())) {
          formData.setText("Barcode");
        } else if ("CALENDAR".equals(formData.getType())) {
          formData.setText("Calendar");
        } else if ("ATTACHMENT".equals(formData.getType())) {
          formData.setText("Attachment");
        } else if ("DROPDOWN".equals(formData.getType())) {
          formData.setText("Catalog");
          formData.setOptions(getSelectOptions(element));
        }
        formDataList.add(formData);
      }
    }
    return formDataList;
  }

  //Extract Label
  private List<String> extractLabels(String htmlContent) {
    Document doc = Jsoup.parse(htmlContent);
    Elements labels = doc.select("form#return-info-form label");
    List<String> labelNames = new ArrayList<>();
    for (Element label : labels) {
      String labelExpression = label.text();
      String regex = "'(.*?)'";
      String extractedLabelName = labelExpression.replaceAll("\\{\\{applbl\\s*" + regex + "}}",
          "$1").trim();
      labelNames.add(extractedLabelName);
    }
    return labelNames;
  }

  //Check Type Name of the template
  private String checkTypeName(Element element) {
    String tagName = element.tagName().toLowerCase();
    if ("input".equals(tagName)) {
      String type = element.attr("type").toLowerCase();
      if ("checkbox".equals(type)) {
        return "CHECKBOX";
      } else if ("radio".equals(type)) {
        return "RADIO";
      } else if ("text".equals(type)) {
        return "TEXTBOX";
      } else if ("datetime".equals(type)) {
        return "DATETIME";
      } else if ("file".equals(type)) {
        return "FILE";
      } else if ("lookup".equals(type)) {
        return "LOOKUP";
      } else if ("barcode".equals(type)) {
        return "BARCODE";
      } else if ("lookup and barcode".equals(type)) {
        return "LOOKUPANDBARCODE";
      }
    } else if ("select".equals(tagName)) {
      return "SELECT";
    } else if ("textarea".equals(tagName)) {
      return "TEXTAREA";
    } else if ("button".equals(tagName)) {
      return "BUTTON";
    }
    return null;
  }

  private List<Object> getSelectOptions(Element selectElement) {
    List<Object> options = new ArrayList<>();
    Elements optionElements = selectElement.select("option");
    for (Element option : optionElements) {
      String value = option.attr("value");
      String text = option.text();
      if (!value.isEmpty()) {
        options.add(value);
      } else {
        options.add(text);
      }
    }
    return options;
  }

  //Write data in Json File
  @Override
  public void writeToJsonFile(String json) throws IOException {
    try (FileWriter fileWriter = new FileWriter("output.json")) {
      fileWriter.write(json);
    }
  }
}



