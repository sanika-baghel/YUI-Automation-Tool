package com.prorigo.service;



import java.io.File;
import java.io.IOException;


public interface JsonToTemplateService {


    public String generateForm(String jsonInput);

    public void writeTemplateToFile(String htmlForm) throws IOException;
    
    
}

