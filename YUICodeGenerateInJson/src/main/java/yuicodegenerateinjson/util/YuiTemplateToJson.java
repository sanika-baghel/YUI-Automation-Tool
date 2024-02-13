package yuicodegenerateinjson.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.multipart.MultipartFile;

public class YuiTemplateToJson {

  public static File convertYuiTemplateToJson(MultipartFile file) {
    try {
      // Parse HTML using JSoup
      Document doc = null;
      doc = Jsoup.parse(new String(file.getBytes()));

      // Create a JSON object to represent the HTML structure
      JSONObject json = new JSONObject();

      // Process each form element
      Elements forms = doc.select("form");
      for (Element form : forms) {
        JSONObject formJson = processElement(form);
        json.put("form", formJson);
      }

      // Convert the JSON object to a JSON string
      String jsonString = json.toString(2);

      // Write  .template file to JSON File
      File jsonFile = new File("CoreProgramInfoTest.json");
      File FilePathWrite = writeYuiTemplateToJSONFile(jsonString, jsonFile);
      return FilePathWrite;
    } catch (IOException e) {
        throw new RuntimeException(e);
      }
  }

  // Recursive method to process HTML elements
  private static JSONObject processElement(Element element) {
    JSONObject elementJson = new JSONObject();
    elementJson.put("tag", element.tagName());

    // Process attributes
    JSONObject attributesJson = new JSONObject();
    for (var attribute : element.attributes()) {
      attributesJson.put(attribute.getKey(), attribute.getValue());
    }
    elementJson.put("attributes", attributesJson);

    // Process child elements recursively
    Elements children = element.children();
    if (!children.isEmpty()) {
      JSONArray childrenJson = new JSONArray();
      for (Element child : children) {
        childrenJson.put(processElement(child));
      }
      elementJson.put("children", childrenJson);
    } else {
      // Process text content if no children
      elementJson.put("text", element.text());
    }

    return elementJson;
  }
  private static File writeYuiTemplateToJSONFile(String yuiTemplateCode, File filePath) {
    try (FileWriter writer = new FileWriter(filePath)) {
      writer.write(yuiTemplateCode);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return filePath;
  }
}
