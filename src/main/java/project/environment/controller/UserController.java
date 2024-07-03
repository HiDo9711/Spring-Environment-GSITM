
package project.environment.controller;


import java.security.Principal;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute UserCreateForm userCreateForm, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) throws Exception{
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        if (!userCreateForm.isPasswordEqualToConfirmPassword()) {
            bindingResult.rejectValue("password2", "password2InCorrect", "비밀번호가 일치하지 않습니다.");
            return "signup";
        }
        
        try {
            userService.createUser(userCreateForm.getLoginId(), userCreateForm.getName(), userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getRegion());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup";
        }
        request.login(userCreateForm.getLoginId(), userCreateForm.getPassword1());
        return "redirect:/";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String userModify(UserCreateForm userCreateForm, Principal principal, Model model) {
        String loginid = principal.getName(); 
        SiteUser siteUser = userService.getUser(loginid);
        model.addAttribute("siteUser", siteUser); 
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
	public String doUserDelete(Principal principal, HttpServletRequest request, @RequestParam("password1") String password1, Model model) {
	    String loginid = principal.getName();
	    SiteUser siteUser = userService.getUser(loginid);

	    String userPassword = siteUser.getPassword();
	    String inputPassword = password1;

	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	    if (passwordEncoder.matches(inputPassword, userPassword)) {
	        userService.deleteUser(loginid);
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.invalidate();
	        }
	        return "redirect:/";
	    } else {
	        model.addAttribute("error", "비밀번호가 일치하지 않습니다."); 
	        return "userDelete"; 
	    }
	}
}