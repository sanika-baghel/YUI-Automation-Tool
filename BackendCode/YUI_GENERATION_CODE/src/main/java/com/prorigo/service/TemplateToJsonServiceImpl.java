package com.prorigo.service;

import com.prorigo.dto.Option;
import com.prorigo.dto.OptionsGroup;
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
            + " textarea, input[type=file],button:not(span > button)");

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
          formData.setText("radio");
        } else if ("BUTTON".equals(formData.getType())) {
          formData.setText("Button");
        } else if ("TEXTAREA".equals(formData.getType())) {
          formData.setText("Textarea");
        } else if ("LOOKUP".equals(formData.getType())) {
          formData.setText("Lookup");
        } else if ("LOOKUPANDBARCODE".equals(formData.getType())) {
          formData.setText("Lookup & Barcode");
        } else if ("BARCODE".equals(formData.getType())) {
          formData.setText("Barcode");
        } else if ("CALENDAR".equals(formData.getType())) {
          formData.setText("Calendar");
        } else if ("ATTACHMENT".equals(formData.getType())) {
          formData.setText("Attachment");
        } else if ("DROPDOWN".equals(formData.getType())) {
          formData.setText("Catalog");
          // Call getSelectOptions method for the current select element
          List<OptionsGroup> optionsGroups = getSelectOptions(element);
          formData.setOptions(optionsGroups);
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
        if ("text".equals(type)) {
          Element parent = element.parent();
          if (parent != null && parent.hasClass("input-group")) {
            // Check if the parent has a button with class containing "icon-calendar"
            Elements buttons = parent.getElementsByTag("button");
            for (Element button : buttons) {
              if (button.getElementsByClass("icon-calendar").size() > 0) {
                return "CALENDAR";
              }
            }
          }else{
            return "LOOKUPANDBARCODE";
          }
          return "TEXTBOX";
        }
      } else if ("datetime".equals(type)) {
        return "DATETIME";
      } else if ("file".equals(type)) {
        return "FILE";
      } else if ("lookup".equals(type)) {
        return "LOOKUP";
      } else if ("barcode".equals(type)) {
        return "BARCODE";
      }
    } else if ("select".equals(tagName)) {
      return "DROPDOWN";
    } else if ("textarea".equals(tagName)) {
      return "TEXTAREA";
    } else if ("button".equals(tagName)) {
      return "BUTTON";
    }
    return null;
  }

  private static List<OptionsGroup> getSelectOptions(Element selectElement) {
    List<OptionsGroup> optionsGroups = new ArrayList<>();

    if (selectElement != null) {
      // Get the options elements
      Elements optionElements = selectElement.select("option");

      // Create an OptionsGroup object
      OptionsGroup optionsGroup = new OptionsGroup();
      optionsGroup.setHeading("Catalog");

      // Create a list of Option objects
      List<Option> options = new ArrayList<>();
      for (Element optionElement : optionElements) {
        String text = optionElement.text();
        Option option = new Option(text);
        options.add(option);
      }
      // Set the options list in the OptionsGroup object
      optionsGroup.setOptions(options);

      // Add the OptionsGroup to the list
      optionsGroups.add(optionsGroup);
    }
    return optionsGroups;
  }


  //Write data in Json File
  @Override
  public void writeToJsonFile(String json) throws IOException {
    try (FileWriter fileWriter = new FileWriter("output.json")) {
      fileWriter.write(json);
    }
  }
}
