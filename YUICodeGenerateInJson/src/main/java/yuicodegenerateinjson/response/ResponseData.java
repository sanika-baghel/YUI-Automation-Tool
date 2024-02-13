package yuicodegenerateinjson.response;

import java.io.File;
import lombok.Data;

@Data
public class ResponseData {

  private String fileName;
  private File generatedFile;

  public ResponseData(String fileName, File generatedFile) {
    this.fileName = fileName;
    this.generatedFile = generatedFile;
  }
}
