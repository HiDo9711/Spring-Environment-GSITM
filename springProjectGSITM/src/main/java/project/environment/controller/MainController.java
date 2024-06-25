package project.environment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	// localhost:8081 으로 접근하면 main으로 보여줄 page setting 
	@GetMapping("/")
	public String root() {
		return "redirect:/main";
	}
}