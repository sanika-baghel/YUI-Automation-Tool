package yuicodegenerateinjson.util;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Test {

  private static void generateYuiTemplateRecursively(JsonNode node, StringBuilder yuiTemplateCode, String indent) {
    if (node.isObject()) {
      Iterator<Entry<String, JsonNode>> fields = node.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> fieldEntry = fields.next();
        String fieldName = fieldEntry.getKey();
        JsonNode fieldValue = fieldEntry.getValue();

        // Open YUI template tag
        yuiTemplateCode.append(indent).append("<").append(fieldName);

        // Assuming object to be a set of attributes
        if (fieldValue.isObject()) {
          Iterator<Entry<String, JsonNode>> attributes = fieldValue.fields();
          while (attributes.hasNext()) {
            Map.Entry<String, JsonNode> attr = attributes.next();
            if (attr.getValue().isValueNode()) {
              yuiTemplateCode.append(" ")
                             .append(attr.getKey())
                             .append("=\"")
                             .append(attr.getValue().asText())
                             .append("\"");
            }
          }
        }

        yuiTemplateCode.append(">\n");

        // Recursive call if it's an object or array
        if (fieldValue.isObject() || fieldValue.isArray()) {
          generateYuiTemplateRecursively(fieldValue, yuiTemplateCode, indent + "\t");
        }

        // Close YUI template tag
        yuiTemplateCode.append(indent).append("</").append(fieldName).append(">\n");
      }
    } else if (node.isArray()) {
      for (JsonNode arrayElement : node) {
        generateYuiTemplateRecursively(arrayElement, yuiTemplateCode, indent);
      }
    } else if (node.isTextual()) {
      // Text content
      yuiTemplateCode.append(indent).append(node.asText()).append("\n");
    }
  }
}
