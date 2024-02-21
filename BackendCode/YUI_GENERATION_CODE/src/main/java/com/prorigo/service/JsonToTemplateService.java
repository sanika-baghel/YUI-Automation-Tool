package com.prorigo.service;
import java.io.IOException;

public interface JsonToTemplateService {

    public String convertJsonToTemplate(String jsonInput);

    public void writeTemplateToFile(String htmlForm) throws IOException;
    
    
}

