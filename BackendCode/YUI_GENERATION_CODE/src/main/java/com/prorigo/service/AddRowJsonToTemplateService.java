package com.prorigo.service;

import java.io.IOException;

public interface AddRowJsonToTemplateService {
  //For Table Raw Heading
  public String addRawHeading(String jsonInput);

  //For Table Data
  public String addDataTable(String jsonInput);

  public void writeTemplateToFile(String htmlForm) throws IOException;
}
