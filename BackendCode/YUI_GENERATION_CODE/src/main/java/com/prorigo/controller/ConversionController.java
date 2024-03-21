package com.prorigo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import com.prorigo.dto.FormData;
import com.prorigo.service.JsonToTemplateService;
import com.prorigo.service.TemplateToJsonService;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/convert")
public class ConversionController {

  private final TemplateToJsonService templateToJsonService;
  private final JsonToTemplateService jsonToTemplateService;

  public ConversionController(JsonToTemplateService jsonToTemplateService,
      TemplateToJsonService templateToJsonService) {
    this.jsonToTemplateService = jsonToTemplateService;
    this.templateToJsonService = templateToJsonService;
  }

  //Convert Template to JSON
  @PostMapping("/templateToJson")
  public ResponseEntity<?> convertTemplateToJson(@RequestParam("templateFile") MultipartFile file) {
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body("Please upload a file");
    }
    try {
      List<FormData> formDataList = templateToJsonService.convertTemplateToJson(file);
      String json = new Gson().toJson(formDataList);
      templateToJsonService.writeToJsonFile(json);
      return ResponseEntity.ok(formDataList);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Error converting HTML to JSON");
    }
  }
  //Convert Json to Template File

  @PostMapping("/jsonToTemplate")
  public ResponseEntity<byte[]> convertJsonToTemplate(
      @RequestParam("jsonFile") MultipartFile jsonFile) {
    try {
      String jsonInput = new String(jsonFile.getBytes());
      String htmlForm = jsonToTemplateService.convertJsonToTemplate(jsonInput);
      // Write the HTML form to a file
      jsonToTemplateService.writeTemplateToFile(htmlForm);

      // Read the file
      Path path = Paths.get("output.template");
      byte[] content = Files.readAllBytes(path);

      return ResponseEntity.ok().body(content);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error processing file".getBytes());
    }
  }
}