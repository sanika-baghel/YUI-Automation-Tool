package com.prorigo.controller;
import com.prorigo.service.JsonToTemplateService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class MultipleTemplateController {

  private final JsonToTemplateService jsonToTemplateService;

  public MultipleTemplateController(JsonToTemplateService jsonToTemplateService) {
    this.jsonToTemplateService = jsonToTemplateService;
  }


  @PostMapping("/jsonToMultipleTemplate")
  public ResponseEntity<?> jsonToMultipleTemplate(
      @RequestParam("jsonFile") MultipartFile jsonFile) {
    try {
      String jsonInput = new String(jsonFile.getBytes());
      String htmlForm = jsonToTemplateService.convertJsonToMultiTemplate(jsonInput);
      return ResponseEntity.ok().body("Successfully Generated..!");
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error processing file".getBytes());
    }
  }
}
