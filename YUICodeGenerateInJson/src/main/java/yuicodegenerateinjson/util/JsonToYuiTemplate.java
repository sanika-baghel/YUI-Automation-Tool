package yuicodegenerateinjson.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class JsonToYuiTemplate {

  public static File convertJsonToYuiTemplate(MultipartFile jsonFile)  {
    File writeYuiTemplateToFile=null;
    try {
        // Read JSON content
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonData = objectMapper.readTree(jsonFile.getInputStream());

        // Generate YUI template code
        String yuiTemplateCode = generateYuiTemplate(jsonData);

        // Write .template file to JSON File
        File templateFile = new File("D://TextBox.template");
        writeYuiTemplateToFile  = writeYuiTemplateToFile(yuiTemplateCode.toString(), templateFile);
        return writeYuiTemplateToFile;
      } catch (IOException e) {
      throw new RuntimeException(e);
      }
  }

  private static String generateYuiTemplate(JsonNode jsonData) {
    StringBuilder yuiTemplateCode = new StringBuilder();

    // Generate YUI template code based on JSON structure
    generateInputBoxTemplate(jsonData, yuiTemplateCode);

    System.out.println("yuiTemplateCode.toString()==>"+yuiTemplateCode.toString());
    return yuiTemplateCode.toString();
  }


//  public static JsonNode getObjectsWithType(JsonNode rootNode, String type) {
//    // Create an array node to store matching objects
//    ObjectMapper mapper = new ObjectMapper();
//    JsonNode resultArray = mapper.createArrayNode();
//
//    // Iterate over the root node
//    for (JsonNode node : rootNode) {
//      // Check if the object has the specified type
//      if (node.has("type") && node.get("type").asText().equals(type)) {
//        // Add the object to the result array
//        ((com.fasterxml.jackson.databind.node.ArrayNode) resultArray).add(node);
//      }
//    }
//    return resultArray;
//  }



    //Input Box template
  private static void generateInputBoxTemplate(JsonNode jsonNode, StringBuilder yuiTemplateCode) {

    System.out.println("jsonNode=="+jsonNode);

    if (jsonNode == null || !jsonNode.isArray()) {
      return; // Exit early if jsonNode is null or not an array
    }

    generateElement(jsonNode,yuiTemplateCode);
    }


    private static void generateElement( JsonNode elementObjects,StringBuilder yuiTemplateCode){

      yuiTemplateCode.append(
          "<form role=\"form\" class=\"form-horizontal\" id=\"return-info-form\">\n");

      // Iterate over JSON array elements
      for (JsonNode node : elementObjects) {

        if (node.isObject()) { // Check if the node is an object

          JsonNode typeNode = node.get("type");
          JsonNode idNode = node.get("id");
          JsonNode labelNode = node.get("label");
          JsonNode classNode = node.get("class");
          JsonNode mandatoryNode = node.get("mandatory");
          JsonNode readOnlyNode = node.get("readOnly");

          if (typeNode != null && idNode != null && labelNode != null && classNode != null &&
              mandatoryNode != null && readOnlyNode != null &&
              typeNode.isTextual() && idNode.isTextual() && labelNode.isTextual() &&
              classNode.isTextual() && mandatoryNode.isBoolean() && readOnlyNode.isBoolean()) {

            String type = typeNode.asText();
            System.out.println("type==" + type);

            // Convert only if type is TEXTBOX,CheckBox,Radio
            if ("TEXTBOX".equalsIgnoreCase(type) || "CHECKBOX".equalsIgnoreCase(type) || "RADIO".equalsIgnoreCase(type)) {

              yuiTemplateCode.append("  <div>\n");
              yuiTemplateCode.append("\t"); // Indentation for label

              // Get values from JSON

              String id = idNode.asText();
              String label = labelNode.asText();
              String className = classNode.asText();
             String typeName=typeNode.asText();
             if(typeName.equalsIgnoreCase("TEXTBOX"))
                typeName="text";
                else if(typeName.equalsIgnoreCase("CHECKBOX"))
                typeName="checkbox";
                else if(typeName.equalsIgnoreCase("RADIO"))
                  typeName="radio";

              System.out.println("typeName==>"+typeName);

              // Add label and input element to HTML

              yuiTemplateCode.append("<label class=\"").append("\">").append("{{applbl ")
                             .append("'")
                             .append(label).append("'").append("}}").append("</label>\n");

                yuiTemplateCode.append("\t<input type=\"").append(typeName).append( "\" id=\"").append(id)
                               .append("\" name=\"")
                               .append(id).append("\"");

              yuiTemplateCode.append(" class=\"").append(className).append("\"");

              // Add the required attribute if required is true
              if (mandatoryNode.asBoolean()) {
                yuiTemplateCode.append(" data-mize_required ");
              }
              if (readOnlyNode.asBoolean()) {
                yuiTemplateCode.append(" disabled");
              }
              yuiTemplateCode.append(" />\n");
              yuiTemplateCode.append("  </div>\n");
            }
          }
        }
      }
      yuiTemplateCode.append("</form>\n");
    }





  //Write YUI Template file code
    private static File writeYuiTemplateToFile(String yuiTemplateCode, File filePath) {
    try (FileWriter writer = new FileWriter(filePath)) {
      writer.write(yuiTemplateCode);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return filePath;
  }

}


