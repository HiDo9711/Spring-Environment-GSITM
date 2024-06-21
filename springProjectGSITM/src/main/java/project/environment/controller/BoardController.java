package project.environment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {
	 @GetMapping("/list")
	 public String list() {
		 return "board_list";
	 }
}
