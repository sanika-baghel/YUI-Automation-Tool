package com.prorigo.service;

import java.io.IOException;

public interface JsonToTemplateService {

    //only one Template generate
    public String convertJsonToTemplate(String jsonInput);
    public String convertJsonToMultiTemplate(String jsonInput) ;

    public void writeTemplateToFile(String htmlForm) throws IOException;
}

