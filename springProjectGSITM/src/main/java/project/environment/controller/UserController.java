package project.environment.controller;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.environment.UserCreateForm;
import project.environment.service.UserService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup";
	}

	//UserCreateForm 내용 검증, 에러 처리
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}

		//회원가입 시 중복 처리
		try { 
		//중복되지 않은 이름,이메일,비밀번호를 넣었을 때 생성
			userService.create(userCreateForm.getLoginId(), userCreateForm.getEmail(), userCreateForm.getPassword1());
		//사용자 id또는 이메일 주소가 이미 존재할 경우에 발생하는 예외. 
		} catch (DataIntegrityViolationException e) { 
			// 데이터 무결성 위반 예외 발생 시
			e.printStackTrace();
			//오류코드, 오류 메시지
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "signup";
		} catch (Exception e) { 
			//기타 예외발생 시
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup";
		}

		return "redirect:/";
	}

	//로그인 페이지
	  @GetMapping("/login")
	   public String login() {
	      return "login";
	   }

	

}