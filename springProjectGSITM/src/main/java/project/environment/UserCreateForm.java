package project.environment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
   @Size(min =3, max =25)
   @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
   private String loginId;
   
   @NotEmpty(message = "비밀번호는 필수 항목입니다.")
   private String password1;

   @NotEmpty(message = "비밀번호는 필수 항목입니다.")
   private String password2;

   private String name;

   @NotEmpty(message = "이메일은 필수 항목입니다.")
   @Email
   private String email;

   @NotEmpty(message = "지역을 선택해주세요.")
   private String region;

}