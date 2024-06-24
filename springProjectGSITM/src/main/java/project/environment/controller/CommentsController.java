package project.environment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import project.environment.entity.Board;
import project.environment.service.BoardService;
import project.environment.service.CommentsService;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class CommentsController {
	private final BoardService boardservice;
	private final CommentsService commentsService;
	
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content) {
		Board board = this.boardservice.getBoardById(id);
		this.commentsService.create(board, content);
		return String.format("redirect:/board/read/%s", id);
	}
	
	


}
