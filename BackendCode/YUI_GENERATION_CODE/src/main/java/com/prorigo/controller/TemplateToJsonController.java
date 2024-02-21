package com.prorigo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class TemplateToJsonController {

    @Autowired
    private TemplateToJsonService templateToJsonService;
    
    @Autowired
    private JsonToTemplateService jsonToTemplateService;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting HTML to JSON");
        }
    }

    //Convert JSON file to Template
//    @PostMapping("/jsonToTemplate")
//    public ResponseEntity<String> convertJSONToTemplate(@RequestParam("jsonFile") MultipartFile file) {
//        if (file.isEmpty()) {
//            return new ResponseEntity<>("No file provided.", HttpStatus.BAD_REQUEST);
//        }
//        try {
//            String jsonInput = new String(file.getBytes());
//            String htmlForm = jsonToTemplateService.generateForm(jsonInput);
//            jsonToTemplateService.writeTemplateToFile(htmlForm);
//
//            return new ResponseEntity<>("Form generated successfully. Output file created.", HttpStatus.OK);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Error occurred while generating form.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PostMapping("/jsonToTemplate")
    public ResponseEntity<byte[]> generateForm(@RequestParam("jsonFile") MultipartFile jsonFile) {
        try {
            String jsonInput = new String(jsonFile.getBytes());
            String htmlForm = jsonToTemplateService.generateForm(jsonInput);
            // Write the HTML form to a file
            jsonToTemplateService.writeTemplateToFile(htmlForm);

            // Read the file
            Path path = Paths.get("output.template");
            byte[] content = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "output.template");
            headers.setContentLength(content.length);

            return ResponseEntity.ok().headers(headers).body(content);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing file".getBytes());
        }
    }
}
