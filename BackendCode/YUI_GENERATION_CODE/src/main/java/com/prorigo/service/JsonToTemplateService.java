package com.prorigo.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

public interface JsonToTemplateService {

    //only one Template generate
    public String convertJsonToTemplate(String jsonInput);

    public void writeTemplateToFile(String htmlForm) throws IOException;

   public String convertJsonToMultiTemplate(String jsonInput) ;

}

