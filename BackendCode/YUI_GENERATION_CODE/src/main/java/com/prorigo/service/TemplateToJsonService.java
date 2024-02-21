package com.prorigo.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.prorigo.dto.FormData;

public interface TemplateToJsonService {
	
	 public List<FormData> convertTemplateToJson(MultipartFile file) throws IOException;
	 
	 public void writeToJsonFile(String json) throws IOException ;

}
