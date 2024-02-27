package com.prorigo.controller;

import com.prorigo.service.AddRowJsonToTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class AddRowController {
 private final AddRowJsonToTemplateService addRowJsonToTemplateService ;

  public AddRowController(AddRowJsonToTemplateService addRowJsonToTemplateService) {
    this.addRowJsonToTemplateService = addRowJsonToTemplateService;
  }

  @PostMapping("/jsonToTemp")
  public ResponseEntity<byte[]> convertJsonToTemplate(
      @RequestParam("json") MultipartFile jsonFile) {
    try {
      String jsonInput = new String(jsonFile.getBytes());
      //
      String htmlForm = addRowJsonToTemplateService.convertJsonToTemplate(jsonInput);
      // Write the HTML form to a file
      addRowJsonToTemplateService.writeTemplateToFile(htmlForm);

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

