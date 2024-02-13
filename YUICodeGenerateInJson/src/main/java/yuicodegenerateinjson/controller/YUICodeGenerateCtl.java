package yuicodegenerateinjson.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import yuicodegenerateinjson.exception.EmptyInputException;
import yuicodegenerateinjson.response.ErrorResponse;
import yuicodegenerateinjson.response.ResponseData;
import yuicodegenerateinjson.service.YUICodeGenerationService;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class YUICodeGenerateCtl {

  YUICodeGenerationService yuiCodeGenerationService;

  public YUICodeGenerateCtl(YUICodeGenerationService yuiCodeGenerationService) {
    super();
    this.yuiCodeGenerationService = yuiCodeGenerationService;
  }

  // Convert .template File to JSON File
  @PostMapping("/convertTemplateToJSON")
  public ResponseEntity<?> convertTemplateToJSON(@RequestParam("templateFile") MultipartFile templateFile)
      {
    try {
     ResponseData responseData = yuiCodeGenerationService.convertTemplateToJSON(templateFile);
     return ResponseEntity.status(201).body(responseData);
    }catch (EmptyInputException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),400));
    }
  }

  //Convert JSON file to .template File
  @PostMapping("/convertJSONToTemplate")
  public ResponseEntity<?> convertJSONToTemplate(@RequestParam("jsonFile") MultipartFile jsonFile)
  {
    try {
      ResponseData responseData = yuiCodeGenerationService.convertJsonToYuiTemplate(jsonFile);
      return ResponseEntity.status(201).body(responseData);
    }catch (EmptyInputException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),404));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),400));
    }
  }


}

