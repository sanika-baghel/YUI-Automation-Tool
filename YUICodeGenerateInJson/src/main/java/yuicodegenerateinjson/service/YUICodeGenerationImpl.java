package yuicodegenerateinjson.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import yuicodegenerateinjson.exception.EmptyInputException;
import yuicodegenerateinjson.response.ResponseData;
import yuicodegenerateinjson.util.JsonToYuiTemplate;
import java.io.File;
import org.springframework.stereotype.Service;
import yuicodegenerateinjson.util.YuiTemplateToJson;

@Service
public class YUICodeGenerationImpl implements YUICodeGenerationService {

  // Convert .template file to JSON file
  @Override
  public ResponseData convertTemplateToJSON(MultipartFile templateFile)  {
    // Check if the file is not empty
    if (templateFile.getOriginalFilename() == null || templateFile.getOriginalFilename().trim().isEmpty()) {
      throw new EmptyInputException("Template File cannot be empty");
    }
    File convertedFile= YuiTemplateToJson.convertYuiTemplateToJson(templateFile);
    return new ResponseData(templateFile.getOriginalFilename(),convertedFile);
  }


  // Convert JSON file to .template file
  public ResponseData convertJsonToYuiTemplate(MultipartFile jsonFile){
    // Check if the file is not empty
    if (jsonFile.getOriginalFilename() == null || jsonFile.getOriginalFilename().trim().isEmpty()) {
      throw new EmptyInputException("JSON File cannot be empty");
    }
    File convertedFile= JsonToYuiTemplate.convertJsonToYuiTemplate(jsonFile);
    return new ResponseData(jsonFile.getOriginalFilename(),convertedFile);
  }


}
