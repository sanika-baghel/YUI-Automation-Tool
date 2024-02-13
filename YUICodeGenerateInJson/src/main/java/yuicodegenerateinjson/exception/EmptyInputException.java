package yuicodegenerateinjson.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyInputException  extends RuntimeException{


  public EmptyInputException(String message) {
    super(message);
  }
}
