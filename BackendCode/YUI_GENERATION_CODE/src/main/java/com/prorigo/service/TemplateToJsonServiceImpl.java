package com.prorigo.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

  public List<FormData> convertTemplateToJson(MultipartFile file) {
    String htmlContent = null;
    try {
      htmlContent = new String(file.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return parseHtml(htmlContent);
  }

  private List<FormData> parseHtml(String htmlContent) {
    Document doc = Jsoup.parse(htmlContent);

    String label1 = (extractLabels(htmlContent));

    Elements elements = doc.select(
        "input[type=checkbox], input[type=radio], select, input[type=text],input[type=lookup],input[type=barcode],"
            + "input[type=lookup and barcode], input[type=Calendar], textarea,button, input[type=file]");

    List<FormData> formDataList = new ArrayList<>();
    for (Element element : elements) {
      if (!element.toString().startsWith("<!--") && !element.toString().endsWith("-->")) {
        FormData formData = new FormData();

        formData.setLabel(label1);
        String extractedContent = "";
        String inputValue = element.attr("value");
        int startIndex = inputValue.indexOf("{{");
        int endIndex = inputValue.indexOf("}}");
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
          extractedContent = inputValue.substring(startIndex + 2, endIndex);
        }

        formData.setType(getType(element));
        formData.setId(element.attr("id"));
        formData.setClassName(element.hasAttr("class") ? element.attr("class") : "");

        formData.setValue(extractedContent);
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

  private String check(String htmlContent) {
    Document doc = Jsoup.parse(htmlContent);
    Elements labels = doc.select("form#return-info-form label");
    String labelName = "";
    for (Element label : labels) {
      String labelExpression = label.text();
      String regex = "'(.*?)'";
      labelName = labelExpression.replaceAll("\\{\\{applbl\\s*" + regex + "}}", "$1").trim();
      System.out.println("Label Name: " + labelName);
    }
    return labelName;
  }

  private String extractLabels(String htmlContent) {
    Document doc = Jsoup.parse(htmlContent);
    Elements labels = doc.select("form#return-info-form label");
    // List<String> labelNames = new ArrayList<>();
    String labelName = "";
    for (Element label : labels) {
      String labelExpression = label.text();
      String regex = "'(.*?)'";
      labelName = labelExpression.replaceAll("\\{\\{applbl\\s*" + regex + "}}", "$1").trim();
      System.out.println("Label Name: " + labelName);
      //  labelNames.add(labelName);
    }
    return labelName;
  }

//  private static void extractLabels(Document doc,List<FormData> formDataList) {
//   // Document doc = Jsoup.parse(htmlContent);
//    Elements labels = doc.select("label");
//    int labelsCount = labels.size();
//    for (int i = 0; i < labelsCount; i++) {
//      String labelText = labels.get(i).text().trim();
//      if (!labelText.isEmpty() && i < formDataList.size()) {
//        // Set label value in the FormData object at index i
//        formDataList.get(i).setLabel(labelText);
//      } else {
//        // Handle the case where formDataList size is smaller than the number of labels
//        System.err.println("Warning: Not enough FormData objects to set labels.");
//        break; // Exit the loop
//      }
//    }
//  }

  private String extractLabelName(String htmlContent) {
    // Regular expression to extract the label name from the {{applbl }} expression
    String regex = "\\{\\{applbl\\s*'([^']+)'\\}\\}";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(htmlContent);
    if (matcher.find()) {
      System.out.println("iff=======");
      return matcher.group(1); // Extract the label name
    } else {
      return ""; // Return empty string if no label name is found
    }
  }

  private String getType(Element element) {
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

  public static String removeComments(String html) {
    // Regular expression to match HTML comments
    String commentRegex = "<!--(.*?)-->";
    Pattern pattern = Pattern.compile(commentRegex, Pattern.DOTALL);
    Matcher matcher = pattern.matcher(html.trim());

    // Remove comments from the HTML string
    StringBuffer cleanedHtml = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(cleanedHtml, "");
    }
    matcher.appendTail(cleanedHtml);

    return cleanedHtml.toString();
  }

  @Override
  public void writeToJsonFile(String json) throws IOException {
    try (FileWriter fileWriter = new FileWriter("output.json")) {
      fileWriter.write(json);
    }
  }
}



