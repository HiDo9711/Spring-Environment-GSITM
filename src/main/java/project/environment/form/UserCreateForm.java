package project.environment.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

   @NotEmpty(message = "사용자 ID는 필수 입력 항목입니다.")
   private String loginId;
   
   @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
   private String password1;

   @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
   private String password2;

   @NotEmpty(message = "이름은 필수 입력 항목입니다.")
   private String name;

   @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
   @Email
   private String email;

   private String region;

}