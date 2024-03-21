package com.prorigo.service;

public interface JsonToTemplateService {

    //only one Template generate
    public String convertJsonToTemplate(String jsonInput);
    public String convertJsonToMultiTemplate(String jsonInput) ;

}

