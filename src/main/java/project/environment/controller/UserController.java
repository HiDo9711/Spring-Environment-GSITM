
package project.environment.controller;


import java.security.Principal;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.environment.entity.SiteUser;
import project.environment.form.UserCreateForm;
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
    public String signup(@Valid @ModelAttribute UserCreateForm userCreateForm, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) throws Exception{
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        //회원가입 시 중복 처리
        try {
            //중복되지 않은 이름,이메일,비밀번호를 넣었을 때 생성
            userService.createUser(userCreateForm.getLoginId(), userCreateForm.getName(), userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getRegion());
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
        request.login(userCreateForm.getLoginId(), userCreateForm.getPassword1());
        return "redirect:/";
    }

    //로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //회원 정보 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String userModify(UserCreateForm userCreateForm, Principal principal, Model model) {
        String loginid = principal.getName();  //현재 로그인한 사용자의 login id가져옴
        SiteUser siteUser = userService.getUser(loginid);
        model.addAttribute("siteUser", siteUser); //siteUser 객체를 siteUser라는 이름으로 모델에 추가
        return "userModify";
    }

    @PostMapping("/modify")
    public String userModify(@ModelAttribute @Valid UserCreateForm userCreateForm, Principal principal) {
        String loginid = principal.getName();
        userService.updateUser(userCreateForm, loginid);
        return "redirect:/";
    }

	@GetMapping("/delete")
	public String userDelete(Principal principal){

		return "userDelete";
	}

	@PostMapping("/delete")
	public String doUserDelete(Principal principal, HttpServletRequest request, @RequestParam("password1") String password1) {
		String loginid = principal.getName();
		SiteUser siteUser = userService.getUser(loginid);

		String userPassword = siteUser.getPassword();
		String inputPassword = password1;

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		if(passwordEncoder.matches(inputPassword,userPassword )) {
			userService.deleteUser(loginid);
			HttpSession session = request.getSession(false);
			if (session != null) {
                session.invalidate();
            }
			return "redirect:/";
		} else {
			return "/delete";
		}

	}

}