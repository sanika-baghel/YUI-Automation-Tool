package com.prorigo.service;

import com.prorigo.dto.Option;
import com.prorigo.dto.OptionsGroup;
import com.prorigo.dto.RowHeader;
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

  // Define a constant regular expression for extracting label names
  //private static final String LABEL_REGEX = "\\{\\{applbl\\s*(.*?)}}";
  private static final String LABEL_REGEX = "\\{\\{applbl\\s*'*(.*?)'*}}";


  public List<FormData> convertTemplateToJson(MultipartFile file) throws IOException {
    // Convert File to String
    String htmlContent = new String(file.getBytes()); // Convert the MultipartFile to a String
    Document doc = Jsoup.parse(htmlContent); // Parse the HTML content using Jsoup
    Elements elements = doc.select(
        "input[type=checkbox], input[type=radio], select, input[type=text],input[type=barcode],"
            + " textarea, input[type=file],button:not(span > button),a[href],tr, td"); // Select relevant form elements

    // Extract label names from HTML content
    List<String> labelNames = extractLabels(htmlContent);

    // List to store form data
    List<FormData> formDataList = new ArrayList<>();

    // Convert HTML form elements to JSON
    convertElementsToJson(elements, labelNames, formDataList);

    // Return the list of form data in JSON format
    return formDataList;
  }


  private void convertElementsToJson(Elements elements, List<String> labelNames,
      List<FormData> formDataList) {
    // Iterate through each HTML element
    for (Element element : elements) {
      // Exclude commented elements
      if (!element.toString().startsWith("<!--") && !element.toString().endsWith("-->")) {
        // Create a new FormData object to store the data for this HTML element
        FormData formData = new FormData();
        String extractedContent = "";

        // Set label from labelNames list based on index
        int index = elements.indexOf(element);
        if (index < labelNames.size()) {
          formData.setLabel(labelNames.get(index));
        }

        // Extract content within {{ }} if present in the value attribute
        String inputValue = element.attr("value");
        int startIndex = inputValue.indexOf("{{");
        int endIndex = inputValue.indexOf("}}");
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
          extractedContent = inputValue.substring(startIndex + 2, endIndex);
        }

        // Set various attributes of FormData based on element attributes
        formData.setType(checkTypeName(element));
        formData.setId(element.attr("id"));
        formData.setValue(extractedContent);
        formData.setClassName(element.hasAttr("class") ? element.attr("class") : "");
        formData.setReadOnly(element.hasAttr("disabled"));
        formData.setMaxLen(element.hasAttr("maxlength") ? element.attr("maxlength") : "");

        // Extract cid and cname from "href" for collapse elements
        if (element.hasAttr("href")) {
          String hrefValue = element.attr("href");
          if (hrefValue.length() > 1) {
            formData.setCid(hrefValue.substring(1));
            String collapseName = element.text();
            Pattern pattern = Pattern.compile("\\{\\{applbl \"(.*?)\"}}");
            Matcher matcher = pattern.matcher(collapseName);
            if (matcher.find()) {
              formData.setCname(matcher.group(1)); // Extract the text inside the double quotes
            } else {
              formData.setCname("");  // Return empty string if no match found
            }
          }
        }

        // Set whether the element is mandatory or not
        formData.setMandatory(element.hasAttr("data-mize_required"));

        // Set text descriptions based on element type
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
        } else if ("COLLAPSE".equals(formData.getType())) {
          formData.setText("Collapse");
        } else if ("ADDROWHEADER".equals(formData.getType())) {
          formData.setText("Add Table Header");
          // Extract row headers from child elements
          List<RowHeader> rowHeaders = extractRowHeaders(element);
          // Set the list of RowHeaders to the corresponding field in formData
          formData.setAddrowheaderkey(rowHeaders);
        } else if ("ADDROWS".equals(formData.getType())) {
          //TO DO ADDROWS
        }

        // Add the FormData object to the list
        formDataList.add(formData);
      }
    }
  }


  // Method to extract row headers from child elements and create RowHeader objects
  private List<RowHeader> extractRowHeaders(Element parentElement) {
    // List to store RowHeader objects
    List<RowHeader> rowHeaders = new ArrayList<>();

    // Get child elements of the parent element
    Elements childElements = parentElement.children();

    // Iterate through each child element
    for (Element childElement : childElements) {
      // Extract attributes and text content from the child element
      String dataRef = childElement.attr("data-ref");
      String width = childElement.attr("width");
      String className = childElement.className();
      String headerLabel = childElement.text();

      // Extract the label name from the header label using a regular expression
      String extractedLabelName = headerLabel.replaceAll(LABEL_REGEX, "$1").trim();

      // Create a new RowHeader object and add it to the list
      rowHeaders.add(new RowHeader(dataRef, width, className, extractedLabelName));
    }
    // Return the list of RowHeader objects
    return rowHeaders;
  }

  /**
   * Extracts label names from the HTML content.
   *
   * @param htmlContent The HTML content from which labels are to be extracted.
   * @return A list of extracted label names.
   */
  private List<String> extractLabels(String htmlContent) {
    // Parse the HTML content using Jsoup
    Document doc = Jsoup.parse(htmlContent);

    // Select labels within the form with id "return-info-form"
    // Elements labels = doc.select("form#return-info-form label");

    Elements labels = doc.select(".cc-field label");

    // List to store extracted label names
    List<String> labelNames = new ArrayList<>();

    // Iterate through each label element
    for (Element label : labels) {
      // Extract the text content of the label
      String labelExpression = label.text();

      // Use regular expression to extract the label name from the label expression
      String extractedLabelName = labelExpression.replaceAll(LABEL_REGEX, "$1").trim();

      // Add the extracted label name to the list
      labelNames.add(extractedLabelName);
    }

    // Return the list of extracted label names
    return labelNames;
  }

  /**
   * Checks the type name of the HTML element.
   *
   * @param element The HTML element to check the type name for.
   * @return The type name of the element.
   */
  private String checkTypeName(Element element) {
    // Get the tag name of the element in lowercase
    String tagName = element.tagName().toLowerCase();

    // Check different types of elements and return their corresponding type names
    if ("input".equals(tagName)) {
      String type = element.attr("type").toLowerCase();
      if ("checkbox".equals(type)) {
        return "CHECKBOX";
      } else if ("radio".equals(type)) {
        return "RADIO";
      } else if ("text".equals(type)) {
        Element parent = element.parent();
        // Check if the parent has a button with class containing "icon-calendar" or "icon-barcode"
        if (parent != null) {
          Elements buttons = parent.getElementsByTag("button");
          for (Element button : buttons) {
            if (!button.getElementsByClass("icon-calendar").isEmpty()) {
              return "CALENDAR";
            } else if (!button.getElementsByClass("icon-barcode").isEmpty()) {
              return "LOOKUPANDBARCODE";
            } else if (!button.getElementsByClass("icon-search").isEmpty()) {
              return "LOOKUP";
            }
          }
        }
        return "TEXTBOX";
      } else if ("datetime".equals(type)) {
        return "DATETIME";
      } else if ("file".equals(type)) {
        return "FILE";
      } else if ("barcode".equals(type)) {
        return "BARCODE";
      }
    } else if ("select".equals(tagName)) {
      return "DROPDOWN";
    } else if ("textarea".equals(tagName)) {
      return "TEXTAREA";
    } else if ("button".equals(tagName)) {
      return "BUTTON";
    } else if ("a".equals(tagName)) {
      return "COLLAPSE";
    } else if ("tr".equals(tagName)) {
      return "ADDROWHEADER";
    } else if ("tbody#orderPartTbody > td".equals(tagName)) {
      // Assuming this represents a specific type of element, possibly for adding rows
      //TO DO ADDROWS
      return "ADDROWS";
    }
    // Return null if the type name cannot be determined
    return null;
  }

  /**
   * Extracts options from a select element and organizes them into option groups.
   *
   * @param selectElement The select element from which options are to be extracted.
   * @return A list of option groups containing the extracted options.
   */
  private static List<OptionsGroup> getSelectOptions(Element selectElement) {
    // List to store option groups
    List<OptionsGroup> optionsGroups = new ArrayList<>();

    // Check if the select element is not null
    if (selectElement != null) {
      // Get the option elements within the select element
      Elements optionElements = selectElement.select("option");

      // Create an OptionsGroup object
      OptionsGroup optionsGroup = new OptionsGroup();
      optionsGroup.setHeading("Catalog"); // Set a default heading for the options group

      // Create a list of Option objects to store options
      List<Option> options = new ArrayList<>();

      // Iterate through each option element
      for (Element optionElement : optionElements) {
        // Extract the text content of the option
        String text = optionElement.text();

        // Create an Option object with the extracted text
        Option option = new Option(text);

        // Add the Option object to the list of options
        options.add(option);
      }
      // Set the list of options in the OptionsGroup object
      optionsGroup.setOptions(options);

      // Add the OptionsGroup to the list of option groups
      optionsGroups.add(optionsGroup);
    }

    // Return the list of option groups
    return optionsGroups;
  }

  /**
   * Writes JSON data to a file.
   *
   * @param json The JSON data to be written to the file.
   * @throws IOException If an I/O error occurs while writing the file.
   */
  @Override
  public void writeToJsonFile(String json) throws IOException {
    // Try-with-resources block ensures proper resource management and automatic closing of FileWriter
    try (FileWriter fileWriter = new FileWriter("output.json")) {
      // Write the JSON data to the file
      fileWriter.write(json);
    }
  }

}
