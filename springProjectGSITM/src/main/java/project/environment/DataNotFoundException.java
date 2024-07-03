package project.environment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.NOT_FOUND, reason = "entity not found")
//										   RuntimeException : 실행시 발생하는 예외
public class DataNotFoundException extends RuntimeException{
  //Exception처리할 때 충돌할 수 있으므로 번호를 주어 방지함
   private static final long serialVersionUID = 1L;
   
   public DataNotFoundException(String message) {
      super(message);//RuntimeException 상속
   }

}