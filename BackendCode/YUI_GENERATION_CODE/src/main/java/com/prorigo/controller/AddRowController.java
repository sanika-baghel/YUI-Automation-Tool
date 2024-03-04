package com.prorigo.controller;

import com.prorigo.service.AddRowJsonToTemplateService;
import com.prorigo.service.TemplateToJsonService;
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

  private final AddRowJsonToTemplateService addRowJsonToTemplateService;

  private final TemplateToJsonService templateToJsonService;

  public AddRowController(AddRowJsonToTemplateService addRowJsonToTemplateService,TemplateToJsonService templateToJsonService) {
    this.addRowJsonToTemplateService = addRowJsonToTemplateService;
    this.templateToJsonService=templateToJsonService;
  }

//Table Raw Heading
  @PostMapping("/convertAddRowHead")
  public ResponseEntity<byte[]> addRowHeadingJsonToTemplate(
      @RequestParam("json") MultipartFile jsonFile) {
    try {
      String jsonInput = new String(jsonFile.getBytes());
      String htmlForm = addRowJsonToTemplateService.addRawHeading(jsonInput);
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

  //For Table Data
  @PostMapping("/convertAddTableData")
  public ResponseEntity<byte[]> addTableDataJsonToTemplate(
      @RequestParam("json") MultipartFile jsonFile) {
    try {
      String jsonInput = new String(jsonFile.getBytes());
      String htmlForm = addRowJsonToTemplateService.addDataTable(jsonInput);
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

  //Template to json for Table heading
  @PostMapping("/convertHeadTemplateToJson")
  public ResponseEntity<byte[]> convertHeadTemplateToJson(
      @RequestParam("templateFile") MultipartFile jsonFile) {
    try {
      String jsonInput = new String(jsonFile.getBytes());
      String htmlForm = templateToJsonService.headTemplateToJson(jsonInput);
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

