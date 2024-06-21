package project.environment.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	// localhost:8080 으로 접근하면 main으로 보여줄 page setting 
	@GetMapping("/")
	public String root() {
		return "redirect:/board/list";
	}
}