package yuicodegenerateinjson.service;
import org.springframework.web.multipart.MultipartFile;
import yuicodegenerateinjson.response.ResponseData;

public interface YUICodeGenerationService {

  //Convert .template File to JSON file
  ResponseData convertTemplateToJSON(MultipartFile templateFile) ;

  //Convert JSON file to .template File
  ResponseData convertJsonToYuiTemplate(MultipartFile jsonFile);

}
